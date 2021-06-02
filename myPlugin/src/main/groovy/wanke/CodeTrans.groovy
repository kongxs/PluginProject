package fun.wanke.plugin

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

class CodeTrans extends Transform {

    Project project;

    CodeTrans(Project project) {
        this.project = project
        println "new transform ----- "
    }

    @Override
    String getName() {
        return CodeTrans.simpleName
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

        println "2121-transform 1"

    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        println "2121-transform 2"
//        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)
        def injectU = new InjectU()
        ExtParams params = project.extParams

        println " params.logEnable is ${ params.logEnable }"

        println "transform method is invoked  ====== --------------- "
//        System.out.println("=======================================doPathTransform{ context=${context}, inputs=${inputs}, referencedInputs=${referencedInputs}, outputProvider=${outputProvider}, isIncremental=${isIncremental}")


        inputs.each { TransformInput input ->

            input.directoryInputs.each { DirectoryInput directoryInput ->

//                println "2121-transform directoryInput name  is ${directoryInput.file.absolutePath}"
//                println "2121-transform directoryInput name  is ${directoryInput.file.name}"


                injectU.inject(directoryInput.file.absolutePath)


                //获取输出目录
               def dest = outputProvider.getContentLocation(
                       directoryInput.name,directoryInput.contentTypes,directoryInput.scopes,
                       Format.DIRECTORY)

                com.android.utils.FileUtils.copyDirectory(
                        directoryInput.file,dest)
            }
            injectU.generatteMap(project)

            input.jarInputs.each { JarInput jarInput ->

                //jar文件一般是第三方依赖库jar文件
                // 重命名输出文件（同目录copyFile会冲突）

                def jarName = jarInput.name
                def md5 = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0,jarName.length() - 4 )
                }

                //输出
                def dest = outputProvider.getContentLocation(jarName,
                    jarInput.contentTypes,jarInput.scopes,Format.JAR)

//                println " jar name location is ${jarInput.name}"

                FileUtils.copyFile(jarInput.file,dest)

            }
        }

    }
}