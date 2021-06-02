package wanke

import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import fun.wanke.plugin.ExtParams
import fun.wanke.plugin.InjectU
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

class RobustPatchTrans extends Transform {

    Project project

    ClassPool pool = ClassPool.default
    private java.util.Map<java.lang.String, java.lang.Integer> injectMethodMap
    Map<String, Set<String>> modifiedMap = new HashMap<>()
    Class modifyAnnotationClass
    private RobustPatchU robustPatchU

    RobustPatchTrans(Project project) {
        this.project = project
        robustPatchU = new RobustPatchU(project , pool)
    }

    @Override
    String getName() {
        return RobustPatchTrans.simpleName
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs,
                   Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {

        super.transform(context ,inputs,referencedInputs ,outputProvider ,isIncremental)

        readInjectMethodFromFile()
        generateModifiedInfoMap(inputs)
        generatePatchClasses()

        throw new NullPointerException(("robust exit successfully"))
    }

    private void generatePatchClasses(){
        File pathDir = new File(project.getProjectDir().getAbsolutePath() + "/robust/patch");
        if (pathDir.exists()) {
            pathDir.delete()
        }
        pathDir.mkdirs()

//        [com.example.pluginproject.People:[com.example.pluginproject.People.getAge(int)]]
        modifiedMap.entrySet().each {
            // key : com.example.pluginproject.People
            def originClazz = pool.get(it.key)

//            value : [com.example.pluginproject.People.getAge(int)]
            Set<String> methodLongNames = it.value

            //生成path
            CtClass patchClazz = robustPatchU.createPatchClass(originClazz, methodLongNames)
            patchClazz.writeFile(pathDir.getAbsolutePath())
            patchClazz.defrost()







        }


    }

    private void generateModifiedInfoMap(Collection<TransformInput> inputs){
        Map<String, Set<String>> modifiedMap = new HashMap<>()
        inputs.each {
                it.directoryInputs.each { DirectoryInput input ->
                    pool.insertClassPath(input.file.absolutePath)


                    input.file.eachFileRecurse {

                        handFile(it , input.file.absolutePath,modifiedMap)
                    }

                }

            it.jarInputs.each {
                pool.insertClassPath(it.file.absolutePath)
            }
        }

        this.modifiedMap = modifiedMap

        println("modifiedMap= " + this.modifiedMap)
    }

    private void handFile(File file ,String dir , Map<String, Set<String>> modifiedMap){

//        println("RobustPatchTrans=handFile = " + file.absolutePath)

        if (!file.absolutePath.endsWith(SdkConstants.DOT_CLASS)) {
            return;
        }

        def className = file.absolutePath.replace(dir, "")
                .replace(File.separator, ".")
                .replace(SdkConstants.DOT_CLASS, "")
                .substring(1)


        CtClass ctClass = pool.get(className)

        if (modifyAnnotationClass == null) {
            modifyAnnotationClass = pool.get("com.example.pluginproject.Modify").toClass()
        }

        Set<String> modifiedMethodNames = new HashSet<>()

        ctClass.getDeclaredMethods().each {
            CtMethod method ->
                if (method.hasAnnotation(modifyAnnotationClass)) {
                    modifiedMethodNames.add(method.longName)
                    println("modifiedMap= methodLongName " + method.longName)
                }
        }

        if (modifiedMethodNames.size() > 0) {
            modifiedMap.put(ctClass.name, modifiedMethodNames)
        }

    }

    private Map<String, Integer> readInjectMethodFromFile() {
        Map<String, String> methodMap = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(project.getProjectDir().getAbsolutePath() + "/robust/methodMap.txt"))
            String line = "";
            while ((line = br.readLine()) != null && line.length() > 0) {
                String[] ss = line.split(":");
//                com.a.robust.data.M.a(int):1
                methodMap.put(ss[0], Integer.parseInt(ss[1]))
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.injectMethodMap = methodMap
        println("injectMethodMap:" + injectMethodMap)
        return methodMap;

    }


}