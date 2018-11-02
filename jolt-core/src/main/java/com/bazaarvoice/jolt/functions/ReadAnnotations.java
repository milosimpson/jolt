package com.bazaarvoice.jolt.functions;

import com.bazaarvoice.jolt.common.Optional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class ReadAnnotations {

    public static class FStruct {

        public final String name;
        public final Method method;
        public final Object obj;
        public final boolean hasDefaultParam;

        public FStruct( String name, Method method, Object obj ) {
            this.name = name;
            this.method = method;
            this.obj = obj;


            boolean hasDef = false;
            Parameter[] params = method.getParameters();
            for ( int index = 0; index < params.length; index++ ) {
                Parameter p = params[index];

                boolean defaultExists = p.isAnnotationPresent( com.bazaarvoice.jolt.functions.DefaultParam.class );

                if ( index == 0 ) {
                    hasDef = defaultExists;
                }
                else if ( hasDef == true && defaultExists ) {
                    throw new RuntimeException( "can't have two default Params" );
                }
                else if ( defaultExists ) {
                    throw new RuntimeException( "DefaultParam must be the first " );
                }
            }

            hasDefaultParam = hasDef;
        }

        public boolean isStatic() {
            return Modifier.isStatic( method.getModifiers() );
        }

        public int numParams() {
            return method.getParameterCount();
        }

        public Object invoke( Object arg1 ) throws Exception {

            KnownTypes methodType = KnownTypes.identifyParameter( method.getParameters()[0] );

            Optional adapted = methodType.adaptFrom( arg1 );

            if ( adapted.isPresent() ) {
                return method.invoke( obj, adapted.get() );
            }
            else {
                return null;
            }
        }
    }

    public static void main( String[] args ) throws Exception {

        Map<String, FStruct> functions = new HashMap<>();


        String testClassName = TestFunctions.class.getCanonicalName();
        System.out.println( "testClassName:" + testClassName );

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        Class testClass = classLoader.loadClass( testClassName );

        Object functionClass = testClass.newInstance();

        for ( Method m : testClass.getMethods() ) {

            //System.out.println( " found method named : " + m.getName() );

            if ( m.isAnnotationPresent( com.bazaarvoice.jolt.functions.Function.class ) && Modifier.isPublic( m.getModifiers() ) ) {
                Annotation annotation = m.getAnnotation( com.bazaarvoice.jolt.functions.Function.class );

                String functionName = (String) annotation.annotationType().getMethod( "value" ).invoke( annotation );

                if ( Modifier.isStatic( m.getModifiers() ) ) {
                    functions.put( functionName, new FStruct( functionName, m, null ) );
                }
                else {
                    functions.put( functionName, new FStruct( functionName, m, functionClass ) );
                }

                m.getParameters()[0].getType();

                // m.invoke first param can be null, as it
                // System.out.println( " functionName " + functionName + " returned : " + m.invoke( null ) );
            }
        }

        {
            FStruct trim = functions.get( "trim" );

            System.out.println( " functionName " + trim.name + " bool true  returned : " + trim.invoke( "  shoes!  " ) );
            System.out.println();
        }


        {
            FStruct isTrueStatic = functions.get( "isTrueStatic" );

            System.out.println( " functionName " + isTrueStatic.name + " bool true  returned : " + isTrueStatic.invoke( true ) );
            System.out.println( " functionName " + isTrueStatic.name + " bool false returned : " + isTrueStatic.invoke( false ) );
            System.out.println( " functionName " + isTrueStatic.name + " BOOL true  returned : " + isTrueStatic.invoke( Boolean.TRUE ) );
            System.out.println( " functionName " + isTrueStatic.name + " BOOL false returned : " + isTrueStatic.invoke( Boolean.FALSE ) );

            System.out.println();

            // Throws java.lang.IllegalArgumentException: argument type mismatch
            System.out.println( " functionName " + isTrueStatic.name + " returned 1 : " + isTrueStatic.invoke( 1 ) );
            System.out.println( " functionName " + isTrueStatic.name + " returned 0 : " + isTrueStatic.invoke( 0 ) );

            System.out.println( " functionName " + isTrueStatic.name + " returned str true  : " + isTrueStatic.invoke( "true" ) );
            System.out.println( " functionName " + isTrueStatic.name + " returned str false : " + isTrueStatic.invoke( "false" ) );
        }

        {
            FStruct isTrueMember = functions.get( "isTrueMember" );

            System.out.println( " functionName " + isTrueMember.name + " returned : " + isTrueMember.method.invoke( isTrueMember.obj, true ) );
            System.out.println( " functionName " + isTrueMember.name + " returned : " + isTrueMember.method.invoke( isTrueMember.obj, false ) );
        }


    }
}
