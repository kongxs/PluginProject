package fun.wanke.plugin;


import com.android.utils.FileUtils;
import com.google.common.io.Files;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.ResourceGroovyMethods;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import groovy.lang.Closure;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class InjectU {

    public void  generatteMap(Project project) throws IOException {

        StringBuilder sb=new StringBuilder();
        for (Map.Entry<String, Integer> entry : methodMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(":");
            sb.append(entry.getValue());
            sb.append("\n");
        }
//        System.out.println(project.getBuildDir());
        File file=new File(project.getBuildDir().getAbsolutePath()+"/robust/methodMap.txt");
//        FileUtils.writeToFile(file,sb.toString());
        Files.createParentDirs(file);
        Files.asCharSink(file, StandardCharsets.UTF_8).write(sb.toString());
    }


    /**
     * @param path file path
     */
    public void inject(final String path) throws Exception {

        pool.appendClassPath(path);


        File dir = new File(path);

        if (dir.isDirectory()) {

            ResourceGroovyMethods.eachFileRecurse(dir, new Closure<Object>(this, this) {
                public void doCall(File file) throws  Exception {

                    String filePath = file.getAbsolutePath();

//                println "2121-transform directoryInput name  is ${file.absolutePath}"
//                println "2121-transform directoryInput name  is ${file.name}"


                    //确保当前文件是class文件，并且不是系统自动生成的class文件
                    if (filePath.endsWith(".class") && !filePath.contains("R$") && !filePath.contains("R.class") && !filePath.contains("BuildConfig.class")) {

//                    println "file path is ${filePath}"

                        int index = filePath.indexOf("com/example/pluginproject/People");

                        if (index != -1) {


                            int end = filePath.length() - 6;// .class = 6

                            final String className = filePath.substring(index, end).replace("\\", ".").replace("/", ".");

                            DefaultGroovyMethods.println(InjectU.this, "not -1 class name is " + className);
//
                            CtClass c = getPool().getCtClass(className);
                            if (c.isInterface()) {
                                return;

                            }

//
                            if (c.isFrozen()) {
                                c.defrost();
                            }


                            //添加一个静态成员变量
                            CtClass type = getPool().getOrNull("com.example.pluginproject.ChangeQuickRedirect");
                            CtField field = new CtField(type, "changeQuickRedirect", c);
                            field.setModifiers(AccessFlag.PUBLIC | AccessFlag.STATIC);
                            c.addField(field);


                            CtMethod[] methods = c.getDeclaredMethods();

                            for (CtMethod method : methods) {

                                getMethodMap().put(method.getLongName(), getInsertMethodCount().incrementAndGet());

                                Boolean isStatic = (method.getModifiers() & AccessFlag.STATIC) != 0;
                                CtClass returnType = method.getReturnType();
                                String returnTypeNameString = returnType.getName();


                                String body = "Object argThis = null;";
                                if (!isStatic) {
                                    body += "argThis = $0;";
                                }
                                System.out.println(method.getLongName());

                                int methodNumber = methodMap.get(method.getLongName());

                                body += "   if (changeQuickRedirect!=null && changeQuickRedirect.isSupport($args, argThis, " + isStatic + ", " + methodNumber + ")) {";
                                body += getReturnStatement(returnTypeNameString, isStatic, methodNumber);
                                body += "   }";
                                method.insertBefore(body);

                            }


                            CtConstructor[] declaredConstructors = c.getDeclaredConstructors();
                            if (declaredConstructors == null || declaredConstructors.length == 0) {
                                CtConstructor constructor = new CtConstructor(new CtClass[0], c);
                                c.addConstructor(constructor);
                                constructor.insertBeforeBody("System.out.println(\"I Love android-- {this.changeQuickRedirect} ---\" ); ");
                            } else {
                                declaredConstructors[0].insertBeforeBody("System.out.println(\"I Love android-- {this.changeQuickRedirect} ---\" ); ");

                            }

//
                            c.writeFile(path);
                            c.detach();
                        }

                    }


                }

            });
        }

    }

    @NotNull
    private Boolean findMethodName(CtMethod method) throws NotFoundException {
        Boolean isStatic = (method.getModifiers() & AccessFlag.STATIC) != 0;

        MethodInfo methodInfo = method.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

        if (attr != null) {
            int len = method.getParameterTypes().length;
            DefaultGroovyMethods.println(InjectU.this, "len= " + len);
            // 非静态的成员函数的第一个参数是this
            int pos = isStatic ? 0 : 1;

            for (int i = 0;  i < len ;i++){
                System.out.print("helloworld" + attr.variableName(i + pos) + " ");
            }

            System.out.println();
        }
        return isStatic;
    }

    private String getParametersClassType(CtMethod method) throws NotFoundException {
        if (method.getParameterTypes().length == 0) {
            return " null ";
        }

        StringBuilder parameterType = new StringBuilder();
        parameterType.append("new Class[]{");
        for (CtClass paramterClass : method.getParameterTypes()) {
            parameterType.append(paramterClass.getName()).append(".class,");
        }

        //remove last ','
        if (",".equals(parameterType.charAt(parameterType.length() - 1))){
            parameterType.deleteCharAt(parameterType.length() - 1);}
        parameterType.append("}");
        return parameterType.toString();
    }

    public static ClassPool getPool() {
        return pool;
    }

    public static void setPool(ClassPool pool) {
        InjectU.pool = pool;
    }

    public static String getInjectStr() {
        return injectStr;
    }

    public static void setInjectStr(String injectStr) {
        InjectU.injectStr = injectStr;
    }

    public HashMap getMethodMap() {
        return methodMap;
    }

    public void setMethodMap(HashMap methodMap) {
        this.methodMap = methodMap;
    }

    public AtomicInteger getInsertMethodCount() {
        return insertMethodCount;
    }

    public void setInsertMethodCount(AtomicInteger insertMethodCount) {
        this.insertMethodCount = insertMethodCount;
    }

    private String getReturnStatement(String type, boolean isStatic, int methodNumber) {
        switch (type) {
            case "int":
                return "   return ((java.lang.Integer)changeQuickRedirect.accessDispatch( $args, argThis, " + isStatic + "," + methodNumber + ")).intValue();";
            default:
                return "   return (" + type + ")changeQuickRedirect.accessDispatch( $args, argThis, " + isStatic + "," + methodNumber + ");";
        }
    }

    private static ClassPool pool = ClassPool.getDefault();
    private static String injectStr = "System.out.println(\"I Love android-----\" ); ";
    private HashMap<String,Integer> methodMap = new HashMap();
    private AtomicInteger insertMethodCount = new AtomicInteger(0);
}
