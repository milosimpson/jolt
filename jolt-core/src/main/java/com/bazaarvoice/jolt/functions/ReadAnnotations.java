package com.bazaarvoice.jolt.functions;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

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
            FunctionStruct trim = functions.get( "trim" );

            System.out.println( " functionName " + trim.name + " bool true  returned : " + trim.invoke( "  shoes!  " ) );
            System.out.println();
        }

        {
//            FunctionStruct trim = functions.get( "intAdd" );
//
//            System.out.println( " functionName " + trim.name + " bool true  returned : " + trim.invoke( "  shoes!  " ) );
//            System.out.println();
        }



        {
            FunctionStruct isTrue = functions.get( "isTrue" );

            System.out.println( " functionName " + isTrue.name + " bool true  returned : " + isTrue.invoke( true ) );
            System.out.println( " functionName " + isTrue.name + " bool false returned : " + isTrue.invoke( false ) );
            System.out.println( " functionName " + isTrue.name + " BOOL true  returned : " + isTrue.invoke( Boolean.TRUE ) );
            System.out.println( " functionName " + isTrue.name + " BOOL false returned : " + isTrue.invoke( Boolean.FALSE ) );

            System.out.println();

            // Throws java.lang.IllegalArgumentException: argument type mismatch
            System.out.println( " functionName " + isTrue.name + " returned 1 : " + isTrue.invoke( 1 ) );
            System.out.println( " functionName " + isTrue.name + " returned 0 : " + isTrue.invoke( 0 ) );

            System.out.println( " functionName " + isTrue.name + " returned str true  : " + isTrue.invoke( "true" ) );
            System.out.println( " functionName " + isTrue.name + " returned str false : " + isTrue.invoke( "false" ) );
        }
    }
}
