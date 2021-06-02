package wanke

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import fun.wanke.plugin.ExtParams
import fun.wanke.plugin.InjectU
import javassist.ClassPool
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

class RobustPatchTrans extends Transform {

    Project project

    ClassPool pool = ClassPool.default
    private java.util.Map<java.lang.String, java.lang.Integer> injectMethodMap

    RobustPatchTrans(Project project) {
        this.project = project
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

        throw new NullPointerException(("robust exit successfully"))
    }

    private void generateModifiedInfoMap(Collection<TransformInput> inputs){

        inputs.each {
                it.directoryInputs.each {
                    pool.insertClassPath(it.file.absolutePath)


                    it.file.eachFileRecurse {

                        handFile(it)
                    }

                }

            it.jarInputs.each {
                pool.insertClassPath(it.file.absolutePath)
            }
        }

//        inputs.each {
//            DirectoryInput input ->
//                pool.insertClassPath(input.file.absolutePath)
//
//                println("RobustPatchTrans = " + input.file.name)
//        }


    }

    private void handFile(File file){
        Map<String, Set<String>> modifiedMap = new HashMap<>()

        println("RobustPatchTrans=" + file.absolutePath)



    }

    private Map<String, Integer> readInjectMethodFromFile() {
        Map<String, String> methodMap = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(project.getBuildDir().getAbsolutePath() + "/robust/methodMap.txt"))
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