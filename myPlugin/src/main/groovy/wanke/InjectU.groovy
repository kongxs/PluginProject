package fun.wanke.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor

class InjectU {


    static ClassPool pool = ClassPool.getDefault()

    static String injectStr = "System.out.println(\"I Love android\" ); "


    /**
     *
     * @param path file path
     */
    static void inject(String path) {

        pool.appendClassPath(path)


        def dir = new File(path);

        if (dir.isDirectory()) {

            dir.eachFileRecurse { File file ->

                def filePath = file.absolutePath



                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {

                    println "file path is ${filePath}"

                    int index = filePath.indexOf("com/example/pluginproject");

                    if (index != -1) {


                        int end = filePath.length() - 6 // .class = 6

                        String className = filePath.substring(index, end)
                                .replace('\\', '.').replace('/', '.')

                        println "not -1 class name is ${className}"

                        def c = pool.getCtClass(className)

                        if (c.isFrozen()) {
                            c.defrost()
                        }


                        CtConstructor[] declaredConstructors = c.getDeclaredConstructors();
                        if (declaredConstructors == null || declaredConstructors.length == 0) {
                            CtConstructor constructor = new CtConstructor(new CtClass[0], c);
                            c.addConstructor(constructor);
                            constructor.insertBeforeBody(injectStr);
                        } else {
                            declaredConstructors[0].insertBeforeBody(injectStr);
                        }

                        c.writeFile(path)
                        c.detach()
                    }
                }

            }
        }
    }


}