package fun.wanke.plugin.replugin

import groovy.xml.MarkupBuilder
import org.gradle.internal.impldep.com.googlecode.jatl.MarkupWriter
import org.gradle.internal.impldep.com.googlecode.jatl.XmlWriter


public class XmlBuilder {

    def static final infix = 'loader.a.Activity'


    def static final name = 'android:name'
    def static final process = 'android:process'
    def static final task = 'android:taskAffinity'
    def static final launchMode = 'android:launchMode'
    def static final authorities = 'android:authorities'
    def static final multiprocess = 'android:multiprocess'

    def static final cfg = 'android:configChanges'
    def static final cfgV = 'keyboard|keyboardHidden|orientation|screenSize'

    def static final exp = 'android:exported'
    def static final expV = 'false'

    def static final ori = 'android:screenOrientation'
    def static final oriV = 'portrait'

    def static final theme = 'android:theme'
    def static final themeTS = '@android:style/Theme.Translucent.NoTitleBar'

    def static final THEME_NTS_USE_APP_COMPAT = '@style/Theme.AppCompat'
    def static final THEME_NTS_NOT_USE_APP_COMPAT = '@android:style/Theme.NoTitleBar'
    def static themeNTS = THEME_NTS_NOT_USE_APP_COMPAT


    String build(def applicationID , def path) {

        Writer writer = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(writer)

        builder.appliation {

            2.times { j ->
                activity(
                        "${name}": "${applicationID}.${infix}${j}",
                        "${cfg}": "${cfgV}",
                        "${exp}": "${expV}",
                        "${ori}": "${oriV}",
                        "${theme}": "${themeNTS}"
                )
            }

        }

        return writer.toString().replace("<appliation>","")
        .replace("</appliation>","")

//        File file = new File(path , "output.xml")
//        PrintWriter pw = new PrintWriter(file.getAbsolutePath())
//        pw.write(writer.toString())
//        pw.close()


    }


}