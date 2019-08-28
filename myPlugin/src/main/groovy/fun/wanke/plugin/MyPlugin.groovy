package fun.wanke.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project> {
    void apply(Project project) {
        println "hello this my plugin in standalone ------- "

//        JdJarTask task = new JdJarTask();

        project.extensions.create("extParams",ExtParams)


        ExtParams params = project.extParams

        println " params.logEnable is ${params.logEnable} ------ "


        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new CodeTrans(project))


    }
}