package fun.wanke.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.build.gradle.internal.scope.VariantScope
import com.android.build.gradle.internal.variant.ApkVariantData
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class RobustPatchPlugin implements Plugin<Project> {
    void apply(Project project) {

        println "hello this my RobustPatchPlugin in standalone ------- "

//        project.


        def extensions = project.extensions
        def android = extensions.getByType(AppExtension)



//        android.registerTransform(new RobustPatchTrans(project))


        if (project.plugins.hasPlugin(AppPlugin)) {
            android.applicationVariants.all { ApplicationVariantImpl variant ->

                toTestVariant(variant)


                ApkVariantData data = variant.variantData

                println("variant : ${data}")

                VariantScope scope = data.scope

                println("scope : $scope")

//                def generateBuildConfigTask = VariantCompat.getGenerateBuildConfigTask(variant)

                def generateBuildConfigTask = variant.getGenerateBuildConfig() as Task

//                println("task--------  $task")

                println("generateBuildConfigTask: $generateBuildConfigTask")

                //rpGenerateDebugHostConfig rpGenerateReleaseHostConfig
                def generateHostConfigTaskName = scope.getTaskName("rpGenerate", "HostConfig")
                def generateHostConfigTask = project.task(generateHostConfigTaskName)

                generateHostConfigTask.group = "rpTaskGroup"
                generateHostConfigTask.doLast {
                    println("generateHostConfigTask invoked !!!! ")

                    File buildConfigGeneratedDir = variant.getVariantData().getScope().getBuildConfigSourceOutputDir()

                    File fileDir = new File(buildConfigGeneratedDir , "/com/jd/build/")
                    fileDir.mkdirs()


                    File releFile = new File(fileDir,"BuildMe.java")

                    releFile.write(getFileContent(),'UTF-8')

                    releFile.deleteOnExit()
                    releFile.createNewFile()
                }

                if (generateBuildConfigTask) {
                    generateHostConfigTask.dependsOn generateBuildConfigTask
                    generateBuildConfigTask.finalizedBy generateHostConfigTask
                }


                // assets

                def mergeAssetsTask = variant.getMergeAssets() as Task

                def generateAssetsTaskName = scope.getTaskName("rpGenerate", "AssetBuild")
                def generateAssetsTask = project.task(generateAssetsTaskName)
                generateAssetsTask.group = "rpTaskGroup"
                generateAssetsTask.doLast {

                    def outputs = mergeAssetsTask.outputs
                    outputs.each {
                        file->
                            file.files.each {
                                f ->
                                    File testFile = new File(f ,"test.txt")
                                    testFile.deleteOnExit()
                                    testFile.createNewFile()
                            }
                    }
                    println("generateAssetsTask invoked !!! ")
                }



                if (mergeAssetsTask) {
                    generateAssetsTask.dependsOn mergeAssetsTask
                    mergeAssetsTask.finalizedBy generateAssetsTask
                }

                // manifest
                def processManifestTaskName = scope.getTaskName("process","Manifest")
                def processManifestTask = project.tasks.getByName(processManifestTaskName)

                println("processManifestTask : $processManifestTask")

//                processManifestTask.doLast {
//
//                    Task task ->
//                        task.outputs.files.each {
//                            println("processManifestTaskTask : "+it.absolutePath)
//                        }
//                }

                def handManifestTaskName = scope.getTaskName("rpGenerate", "HandManifest")
                def handManifestTask = project.task(handManifestTaskName)
                handManifestTask.group = "rpTaskGroup"

                handManifestTask.doLast {
                    println("handManifestTask invoked .... ")
                }

                if (processManifestTask) {
                    handManifestTask.dependsOn processManifestTask
                    processManifestTask.finalizedBy handManifestTask
                }



            }
        }

    }


    String getFileContent() {
        return """
package com.jd.build;

/**
 * 注意：此文件由插件化框架自动生成，请不要手动修改。
 */
public class BuildMe {

    
}"""
    }

    def TAG = "ApplicationVariantImpl"

    void toTestVariant(ApplicationVariantImpl variant) {

        variant.productFlavors.each {
            value->

                println("$TAG , productFlavors : $value.name")

        }
        println("-----------------")
        def name = variant.name
        println("$TAG , variantName : $name")

        variant.metaPropertyValues.each {
            value ->
                println("$TAG , metaPropertyValues : $value.name")
        }
        println("$TAG -------------------")

        variant.properties.each {
            value ->
                println("$TAG , properties : $value.key , $value.value")
        }
        println("$TAG -------------------")
        variant.outputs.each {
            value ->
                println("$TAG , outputs : ${value.name } , ${value.dirName}")
        }
        println("$TAG -------------------")

        println("$TAG variant.dirName : ${variant.dirName}")

        ApkVariantData data = variant.variantData
        println("$TAG variantData : ${data.name}")

        VariantScope scope = data.scope

        println("$TAG data.scope: ${data.scope}")




    }

    void log(def msg) {
        println("$TAG : $msg")
    }

}