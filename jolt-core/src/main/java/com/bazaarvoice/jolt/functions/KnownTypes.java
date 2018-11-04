package com.bazaarvoice.jolt.functions;

import com.bazaarvoice.jolt.common.Optional;
import com.bazaarvoice.jolt.functions.adapters.*;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * Purpose of this class is to centralize / corral all the
 *
 * 1) Nasty instance of lookups.
 * 2) Processing of reflection Params
 * 3) Adapter lookup / logic.
 */
public enum KnownTypes {

    NULL   ( new NoopAdapter() ),
    STRING ( new AdaptToString() ),
    INT    ( new AdaptToInteger() ),
    LONG   ( new NoopAdapter() ),
    FLOAT  ( new NoopAdapter() ),
    DOUBLE ( new AdaptToDouble() ),
    BOOL   ( new AdaptToBoolean() ),
    LIST   ( new NoopAdapter() ),
    MAP    ( new NoopAdapter() ),
    UNKNOWN( new NoopAdapter() );

    private Adapter adapter;

    KnownTypes( Adapter adapter ) {
        this.adapter = adapter;
    }

    public Optional adaptFrom( Object obj ) {
        return this.adapter.adapt( obj );
    }

    /**
     * Given an unknown data object, figure out what KnownType it is.
     */
    public static KnownTypes identifyDataObject( Object obj ) {

        if ( obj == null ) {
            return NULL;
        }
        else if ( obj instanceof String ) {
            return STRING;
        }
        else if ( obj instanceof Integer ) {
            return INT;
        }
        else if ( obj instanceof Long ) {
            return LONG;
        }
        else if ( obj instanceof Float ) {
            return FLOAT;
        }
        else if ( obj instanceof Double ) {
            return DOUBLE;
        }
        else if ( obj instanceof Boolean ) {
            return BOOL;
        }
        else if ( obj instanceof List ) {
            return LIST;
        }
        else if ( obj instanceof Map ) {
            return MAP;
        }

        return UNKNOWN;
    }

    /**
     * Given a parameter from reflection, figure out which our
     *  KnownTypes it corresponds too.
     */
    public static KnownTypes identifyParameter( Parameter p ) {

        Class<?> pType = p.getType();

        // Integer.TYPE wat?  That is how we can tell if the parameter is a primitive type.

        if ( Integer.TYPE == pType || pType.isAssignableFrom( Integer.class ) ) {
            return INT;
        }
        else if ( Long.TYPE == pType || pType.isAssignableFrom( Long.class ) ) {
            return LONG;
        }
        else if ( Float.TYPE == pType || pType.isAssignableFrom( Float.class ) ) {
            return FLOAT;
        }
        else if ( Double.TYPE == pType || pType.isAssignableFrom( Double.class ) ) {
            return DOUBLE;
        }
        else if ( Boolean.TYPE == pType || pType.isAssignableFrom(  Boolean.class ) ) {
            return BOOL;
        }
        else if ( pType.isAssignableFrom( List.class ) ) {
            return LIST;
        }
        else if ( pType.isAssignableFrom( Map.class ) ) {
            return MAP;
        }
        else if ( pType.isAssignableFrom( String.class ) ) {
            return STRING;
        }

        return UNKNOWN;
    }
}
