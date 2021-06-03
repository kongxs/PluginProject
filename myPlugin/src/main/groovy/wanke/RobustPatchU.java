package wanke;

import org.gradle.api.Project;

import java.util.Map;
import java.util.Set;

import javassist.ClassMap;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.ClassFile;

public class RobustPatchU {

    Project project ;
    ClassPool pool ;

    public RobustPatchU(Project project , ClassPool pool) {
        this.project = project;
        this.pool = pool;
    }

    /**
     *
     * @param originClass
     * @param methodLongNames
     * @return
     * @throws Exception
     */
    public CtClass createPatchClass(CtClass originClass, Set<String> methodLongNames) throws Exception {

        String patchClassName = originClass.getName() + "Patch";

        System.out.println("createPatchClass = " + patchClassName);

        CtClass patchClazz = pool.makeClass(patchClassName);
        patchClazz.getClassFile().setMajorVersion(ClassFile.JAVA_7);
        patchClazz.setSuperclass(pool.get("java.lang.Object"));
        patchClazz.setModifiers(AccessFlag.clear(patchClazz.getModifiers(), AccessFlag.ABSTRACT));

        //add originClass field
        patchClazz.addField(CtField.make(originClass.getName() + " originClass;",patchClazz));

        //add Constructor
        StringBuilder patchClassConstruct = new StringBuilder();
        patchClassConstruct.append(" public " + patchClazz.getSimpleName() + "(Object o) {");
        patchClassConstruct.append("    originClass=(" + originClass.getName() + ")o;");
        patchClassConstruct.append("}");
        CtConstructor constructor = CtNewConstructor.make(patchClassConstruct.toString(), patchClazz);
        patchClazz.addConstructor(constructor);


        //copy field and method from sourceClass
        for (CtField field : originClass.getDeclaredFields()) {
            patchClazz.addField(new CtField(field, patchClazz));
        }

        ClassMap classMap = new ClassMap();
        classMap.put(patchClassName, originClass.getName());
        classMap.fix(originClass);
        for (CtMethod method : originClass.getDeclaredMethods()) {
            if (methodLongNames.contains(method.getLongName())) {
                CtMethod newCtMethod = new CtMethod(method, patchClazz, classMap);
                patchClazz.addMethod(newCtMethod);
            }
        }


        return patchClazz;
    }

    public CtClass createPatchControlClass(CtClass patchClass,
                                           CtClass originClass, Set<String> methodLongNames,
                                           Map<String, Integer> injectMethodMap) throws Exception{

        return null;

    }


}
