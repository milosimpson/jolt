package com.bazaarvoice.jolt.functions;

import com.bazaarvoice.jolt.common.Optional;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public enum KnownTypes {

    NULL {
        public Optional<Object> adaptFrom( Object obj ) {
            return Optional.empty();
        }
    },


    STRING {
        public Optional<String> adaptFrom( Object obj ) {

            KnownTypes adaptee = KnownTypes.identifyObject( obj );

            switch (adaptee) {
                case NULL:
                    return Optional.empty();
                case STRING:
                        return Optional.of( (String) obj );
                case BOOL:
                    // return "true" or "false"
                    return Optional.of( ((Boolean) obj).toString() );
                case INT:
                    return Optional.of( Integer.toString( ((Integer) obj ) ) );
                case LONG:
                    return Optional.of( Long.toString( ((Long) obj ) ) );
                case FLOAT:
                    return Optional.of( Float.toString( ((Float) obj ) ) );
                case DOUBLE:
                    return Optional.of( Double.toString( ((Double) obj ) ) );
                default:
                    return Optional.empty();
            }
        }
    },

    INT {
        public Optional<Integer> adaptFrom( Object obj ) {

            KnownTypes adaptee = KnownTypes.identifyObject( obj );

            switch (adaptee) {
                case NULL:
                    return Optional.empty();
                case STRING:
                    Integer adapted = null;
                    try {
                        adapted = Integer.parseInt( (String) obj );
                    }
                    catch( NumberFormatException nfe ) {}

                    if ( adapted == null ) {
                        return Optional.empty();
                    }
                    else {
                        return Optional.of( adapted );
                    }
                case BOOL:
                    // truthy eval of Bool to 1 or 0
                    return Optional.of( ((Boolean) obj) ? 1 : 0 );
                case INT:
                    return Optional.of( (Integer) obj );
                case LONG:
                    return Optional.of( ((Long) obj).intValue() );
                case FLOAT:
                    return Optional.of( ((Float) obj).intValue() );
                case DOUBLE:
                    return Optional.of( ((Double) obj).intValue() );
                default:
                    return Optional.empty();
            }
        }
    },

    LONG {
        public Optional<Long> adaptFrom( Object obj ) {
            return Optional.empty();
        }
    },

    FLOAT {
        public Optional<Float> adaptFrom( Object obj ) {
            return Optional.empty();
        }
    },

    DOUBLE {
        public Optional<Double> adaptFrom( Object obj ) {
            return Optional.empty();
        }
    },

    BOOL {
        public Optional<Boolean> adaptFrom( Object obj ) {

            KnownTypes adaptee = KnownTypes.identifyObject( obj );

            switch (adaptee) {
                case NULL:
                    return Optional.of( Boolean.FALSE );
                case STRING:
                    return Optional.of( adapt( (String) obj ) );
                case BOOL:
                    return Optional.of( (Boolean) obj );
                case INT:
                    return Optional.of( truthyAdapt( (int) obj ) );
                case LONG:
                    return Optional.of( truthyAdapt( (long) obj ) );
                default:
                    return Optional.empty();
            }
        }

        private Boolean adapt( String str ) {
            return Boolean.valueOf( str );
        }

        private Boolean truthyAdapt( int number ) {
            return number >= 1;
        }

        private Boolean truthyAdapt( long number ) {
            return number >= 1;
        }
    },

    LIST {
        public Optional<List> adaptFrom( Object obj ) {
            return Optional.empty();
        }
    },

    MAP {
        public Optional<Map> adaptFrom( Object obj ) {
            return Optional.empty();
        }
    },

    UNKNOWN{
        public Optional<Object> adaptFrom( Object obj ) {
            return Optional.empty();
        }
    };


    public abstract Optional adaptFrom( Object obj );


    public static KnownTypes identifyObject( Object obj ) {

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
