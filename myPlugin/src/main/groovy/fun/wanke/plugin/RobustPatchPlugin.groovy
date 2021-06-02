package fun.wanke.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import wanke.RobustPatchTrans

class RobustPatchPlugin implements Plugin<Project> {
    void apply(Project project) {

        println "hello this my RobustPatchPlugin in standalone ------- "


        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new RobustPatchTrans(project))


    }
}