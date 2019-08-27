package fun.wanke.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class JdJarTask extends DefaultTask {



    @TaskAction
    void action( ) {
        println "task action in jd jar task "
    }

}