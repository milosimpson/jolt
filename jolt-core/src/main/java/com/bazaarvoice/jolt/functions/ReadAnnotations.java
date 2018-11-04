/*
 * Copyright 2018 Bazaarvoice, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bazaarvoice.jolt.functions;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


/**
 *
 *  How to call
 * https://coderanch.com/t/328722/java/Passing-array-vararg-method-Reflection
 * https://yourmitra.wordpress.com/2008/09/26/using-java-reflection-to-invoke-a-method-with-array-parameters
 * https://stackoverflow.com/questions/15951521/invoke-method-with-an-array-parameter-using-reflection
 *
 *  How to Know if something is an array param
 * https://docs.oracle.com/javase/tutorial/reflect/special/arrayComponents.html
 */

public class ReadAnnotations {

    public static Map<String, FunctionStruct> processClass ( String testClassName ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, FunctionStruct> functions = new HashMap<>();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class testClass = classLoader.loadClass( testClassName );

        Object functionClass = testClass.newInstance();

        for ( Method m : testClass.getMethods() ) {

            //System.out.println( " found method named : " + m.getName() );

            if ( m.isAnnotationPresent( Function.class ) && Modifier.isPublic( m.getModifiers() ) ) {
                Annotation annotation = m.getAnnotation( Function.class );

                String functionName = (String) annotation.annotationType().getMethod( "value" ).invoke( annotation );

                if ( Modifier.isStatic( m.getModifiers() ) ) {
                    functions.put( functionName, new FunctionStruct( functionName, m, null ) );
                }
                else {
                    functions.put( functionName, new FunctionStruct( functionName, m, functionClass ) );
                }

                m.getParameters()[0].getType();

                // m.invoke first param can be null, as it
                // System.out.println( " functionName " + functionName + " returned : " + m.invoke( null ) );
            }
        }
        return functions;
    }


    public static void main( String[] args ) throws Exception {

        // given a class
        String testClassName = "com.bazaarvoice.jolt.functions.TestFunctions";

        // extract all the Jolt functions
        Map<String, FunctionStruct> functions = processClass( testClassName );

        // "Test" them
        {
            FunctionStruct join = functions.get( "join" );

//            System.out.println( " functionName " + join.name + " , a b  returned : " + join.invoke( ",", "a", "b" ).get() );
            System.out.println();
        }

        {
            FunctionStruct trim = functions.get( "trim" );

            System.out.println( " functionName " + trim.name + " bool true  returned : " + trim.invoke( "  shoes!  " ).get() );
            System.out.println();
        }

        {
            FunctionStruct intAdd = functions.get( "intAdd" );

            System.out.println( " functionName " + intAdd.name + " 1 + 2  returned : " + intAdd.invoke( 1.0d, 2.0f ).get() );
            System.out.println();
        }

        {
            FunctionStruct isTrue = functions.get( "isTrue" );

            System.out.println( " functionName " + isTrue.name + " bool true  returned : " + isTrue.invoke( true ).get() );
            System.out.println( " functionName " + isTrue.name + " bool false returned : " + isTrue.invoke( false ).get() );
            System.out.println( " functionName " + isTrue.name + " BOOL true  returned : " + isTrue.invoke( Boolean.TRUE ).get() );
            System.out.println( " functionName " + isTrue.name + " BOOL false returned : " + isTrue.invoke( Boolean.FALSE ).get() );

            System.out.println();

            // Throws java.lang.IllegalArgumentException: argument type mismatch
            System.out.println( " functionName " + isTrue.name + " returned 1 : " + isTrue.invoke( 1 ).get() );
            System.out.println( " functionName " + isTrue.name + " returned 0 : " + isTrue.invoke( 0 ).get() );

            System.out.println( " functionName " + isTrue.name + " returned str true  : " + isTrue.invoke( "true" ).get() );
            System.out.println( " functionName " + isTrue.name + " returned str false : " + isTrue.invoke( "false" ).get() );
        }
    }
}
