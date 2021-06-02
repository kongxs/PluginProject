package fun.wanke.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class MyPlugin implements Plugin<Project> {
    void apply(Project project) {
        println "hello this my plugin in standalone ------- "


        project.tasks.create("jdJar",JdJarTask.class)

        project.extensions.create("extParams",ExtParams)

        project.configurations.all { configuration ->
            def name = configuration.name
            System.out.println("this configuration is ${name}")
            if (name != "implementation" && name != "compile") {
                return
            }
            //为Project加入Gson依赖
            configuration.dependencies.add(project.dependencies.create("com.google.code.gson:gson:2.8.2"))
        }

        ExtParams params = project.extParams

        println " params.logEnable is ${params.logEnable} ------ "


        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new CodeTrans(project))

        //将jdJar task 挂到构建生命周期中

        def checkTask = project.tasks.getByName("check")
        def buildTask = project.tasks.getByName("build")
        def jdJarTask = project.tasks.getByName("jdJar")

        jdJarTask.dependsOn(checkTask)
        buildTask.dependsOn(jdJarTask)

//        jdJarTask.mustRunAfter buildTask


        buildTask.doLast {
            println("build task finished ....... ")

            project.add(new JdJarTask())
        }




    }
}