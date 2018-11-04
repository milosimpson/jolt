package com.bazaarvoice.jolt.functions;

import com.bazaarvoice.jolt.common.Optional;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Purpose of this class is to encapsulate all the stuff we need to know
 *  about the "method" that implements a Jolt Function.
 */
public class FunctionStruct {

    public final String name;
    public final Method method;
    public final Object obj;

    // If there is a default param it should be the first param
    public final boolean hasDefaultParam;
    private final List<KnownTypes> paramKnownTypes;

    public FunctionStruct( String name, Method method, Object obj ) {
        this.name = name;
        this.obj = obj;
        this.method = method;


        boolean hasDef = false;
        Parameter[] params = method.getParameters();

        paramKnownTypes = new ArrayList<>( params.length );

        for ( int index = 0; index < params.length; index++ ) {
            Parameter p = params[index];

            boolean defaultExists = p.isAnnotationPresent( DefaultParam.class );

            if ( index == 0 ) {
                hasDef = defaultExists;
            }
            else if ( hasDef == true && defaultExists ) {
                throw new RuntimeException( "Can't have two default Params" );
            }
            else if ( defaultExists ) {
                throw new RuntimeException( "DefaultParam must be the first " );
            }

            KnownTypes paramType = KnownTypes.identifyParameter( p );

            if ( KnownTypes.UNKNOWN == paramType ) {
                throw new RuntimeException( "For function:" + name + " parameter #" + index + " has an unknown type.");
            }

            paramKnownTypes.add( paramType );
        }

        hasDefaultParam = hasDef;
    }

    public boolean isStatic() {
        return Modifier.isStatic( method.getModifiers() );
    }

    public int numParams() {
        return method.getParameterCount();
    }

    public Object invoke( Object ... args ) throws Exception {

        if ( args.length != numParams() ) {
            return null;
        }

        Object[] adaptedArgs = new Object[ args.length ];

        boolean couldAdaptAll = true;
        for ( int index = 0; index < args.length && couldAdaptAll; index++ ) {
            Optional adapted = paramKnownTypes.get(index).adaptFrom( args[index] );

            if ( adapted.isPresent() ) {
                adaptedArgs[index] = adapted.get();
            }
            else {
                couldAdaptAll = false;
            }
        }

        if ( couldAdaptAll ) {
            return method.invoke( obj, adaptedArgs );
        }
        else {
            return null;
        }
    }
}
