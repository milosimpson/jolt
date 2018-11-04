package com.bazaarvoice.jolt.functions;

import com.bazaarvoice.jolt.common.Optional;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 * Purpose of this class is to encapsulate all the stuff we need to know
 *  about the "method" that implements a Jolt Function.
 */
public class FunctionStruct {

    public final String name;
    public final Method method;
    public final Object obj;
    public final boolean hasDefaultParam;

    public FunctionStruct( String name, Method method, Object obj ) {
        this.name = name;
        this.method = method;
        this.obj = obj;


        boolean hasDef = false;
        Parameter[] params = method.getParameters();
        for ( int index = 0; index < params.length; index++ ) {
            Parameter p = params[index];

            boolean defaultExists = p.isAnnotationPresent( DefaultParam.class );

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
