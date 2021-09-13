/*
 * Copyright (C) 2005-2017 Qihoo 360 Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package wanke

import javassist.CannotCompileException
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

/**
 * @author RePlugin Team
 */
public class GMeExprEditor extends ExprEditor {


    @Override
    public void edit(MethodCall m) throws CannotCompileException {

        String className1 = m.getClassName();
        System.out.println("instrument getClassName: "+ className1);
        String methodName = m.getMethodName();
        System.out.println("instrument getMethodName: " + methodName);
        System.out.println("instrument getSignature "+m.getSignature());
        System.out.println("instrument getLineNumber: "+ m.getLineNumber());
        System.out.println("instrument getFileName: "+m.getFileName());


        if (methodName.equals("log") && className1.contains("TestLog")) {

            System.out.println("instrument log method invoked \$2");

            m.replace('{ $1 = \"' + "inject from groovy" + '\"; ' +
                    '$2 = \"' + "inject from groovy 2 " + '\";'+
                    '$_ = $proceed($$);' +
                    ' }')
//            m.replace('{ $2 = \"' + "inject from groovy 2" + '\"; ' +
//                    '$_ = $proceed($$);' +
//                    ' }')
        }

//                                                super.edit(m);

    }
}
