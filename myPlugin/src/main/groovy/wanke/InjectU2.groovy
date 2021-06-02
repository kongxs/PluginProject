package wanke

import javassist.*
import javassist.bytecode.AccessFlag
import javassist.bytecode.LocalVariableAttribute

import java.util.concurrent.atomic.AtomicInteger

class InjectU2 {


    static ClassPool pool = ClassPool.getDefault()

    static String injectStr = "System.out.println(\"I Love android-----\" ); "



    def methodMap = new HashMap()
    def insertMethodCount = new AtomicInteger(0);

    /**
     *
     * @param path file path
     */
    public void inject(String path) {

        pool.appendClassPath(path)


        def dir = new File(path);

        if (dir.isDirectory()) {

            dir.eachFileRecurse { File file ->

                def filePath = file.absolutePath

//                println "2121-transform directoryInput name  is ${file.absolutePath}"
//                println "2121-transform directoryInput name  is ${file.name}"



                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {

//                    println "file path is ${filePath}"

                    int index = filePath.indexOf("com/example/pluginproject/People");

                    if (index != -1) {


                        int end = filePath.length() - 6 // .class = 6

                        String className = filePath.substring(index, end)
                                .replace('\\', '.').replace('/', '.')

                        println "not -1 class name is ${className}"
//
                        def c = pool.getCtClass(className)
                        if (c.isInterface()) {
                            return
                        }
//
                        if (c.isFrozen()) {
                            c.defrost()
                        }

                        //添加一个静态成员变量
                        def type = pool.getOrNull("com.example.pluginproject.ChangeQuickRedirect")
                        def field = new CtField(type, "changeQuickRedirect", c)
                        field.setModifiers(AccessFlag.PUBLIC | AccessFlag.STATIC)
                        c.addField(field)


                        def methods = c.getDeclaredMethods()

                        for (CtMethod method : methods) {

                            methodMap.put(method.longName , insertMethodCount.incrementAndGet())

                            def isStatic = (method.getModifiers() & AccessFlag.STATIC) != 0
                            def returnType = method.getReturnType()
                            def returnTypeName = returnType.getName()


                            def methodInfo = method.methodInfo
                            def codeAttribute = methodInfo.codeAttribute
                            def attr = (LocalVariableAttribute)codeAttribute.getAttribute(LocalVariableAttribute.tag)

                            if (attr  != null) {
                                int len = method.getParameterTypes().length
                                println("len= "  + len)
                                // 非静态的成员函数的第一个参数是this
                                int pos = isStatic ? 0 : 1

                                for (int i = 0; i < len; i++) {
                                    System.out.print("helloworld"+attr.variableName(i + pos) + ' ');
                                }
                                System.out.println()
                            }


//                            String body = "Object argThis = null;";
//                            if (!isStatic) {
//                                body += "argThis = $0;";
//                            }

//                            def paramsClazzTypes = getParametersClassType(method)
//                            println("paramsClazzTypes = " + paramsClazzTypes)
//                            int methodNumber = methodMap.get(method.getLongName())


//                            body += "   if (changeQuickRedirect!=null && changeQuickRedirect.isSupport($args, argThis, " + isStatic + ", " + methodNumber + ")) {";
//                            body += getReturnStatement(returnTypeString, isStatic, methodNumber);
//                            body += "   }";
//                            ctMethod.insertBefore(body);

//                            StringBuilder builder = new StringBuilder()
//                            builder.append("if(changeQuickRedirect != null && changeQuickRedirect.isSupport())")
//                            builder.append("{")
//
//                            builder.append("changeQuickRedirect.accessDispatch()")
//
//                            builder.append("}")
//
//                            method.insertBeforeBody(builder.toString())
                        }



                        CtConstructor[] declaredConstructors = c.getDeclaredConstructors();
                        if (declaredConstructors == null || declaredConstructors.length == 0) {
                            CtConstructor constructor = new CtConstructor(new CtClass[0], c);
                            c.addConstructor(constructor)
                            constructor.insertBeforeBody("System.out.println(\"I Love android-- {this.changeQuickRedirect} ---\" ); ");
                        } else {
                            declaredConstructors[0].insertBeforeBody("System.out.println(\"I Love android-- {this.changeQuickRedirect} ---\" ); ");

                        }
//
                        c.writeFile(path)
                        c.detach()
                    }
                }

            }
        }
    }

    private String getParametersClassType(CtMethod method) throws NotFoundException {
        if (method.getParameterTypes().length == 0) {
            return " null ";
        }
        StringBuilder parameterType = new StringBuilder();
        parameterType.append("new Class[]{");
        for (CtClass paramterClass : method.getParameterTypes()) {
            parameterType.append(paramterClass.getName()).append(".class,");
        }
        //remove last ','
        if (',' == parameterType.charAt(parameterType.length() - 1))
            parameterType.deleteCharAt(parameterType.length() - 1);
        parameterType.append("}");
        return parameterType.toString();
    }


}