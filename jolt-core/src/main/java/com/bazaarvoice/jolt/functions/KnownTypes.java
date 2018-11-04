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
    OBJECT ( new NoopAdapter() ); // Default / Unknown

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

        return OBJECT;
    }

    /**
     * Given a parameter from reflection, figure out which our
     *  KnownTypes it corresponds too.
     */
    public static KnownTypes identifyParameter( Parameter p ) {

        p.isVarArgs();

        Class<?> type = p.getType();

        // pType works until you have an array.   getComponentType() works with things like String[]

        if ( type.isArray() ) {
            type = type.getComponentType();
        }

        // Primitive types (int, long) can be used with "isAssignableFrom"
        // Instead check to see if the type == Integer.TYPE, Long.TYPE, etc
        //
        if ( Integer.TYPE == type || type.isAssignableFrom( Integer.class ) ) {
            return INT;
        }
        else if ( Long.TYPE == type || type.isAssignableFrom( Long.class ) ) {
            return LONG;
        }
        else if ( Float.TYPE == type || type.isAssignableFrom( Float.class ) ) {
            return FLOAT;
        }
        else if ( Double.TYPE == type || type.isAssignableFrom( Double.class ) ) {
            return DOUBLE;
        }
        else if ( Boolean.TYPE == type || type.isAssignableFrom(  Boolean.class ) ) {
            return BOOL;
        }
        else if ( type.isAssignableFrom( List.class ) ) {
            return LIST;
        }
        else if ( type.isAssignableFrom( Map.class ) ) {
            return MAP;
        }
        else if ( type.isAssignableFrom( String.class ) ) {
            return STRING;
        }

        return OBJECT;
    }
}
