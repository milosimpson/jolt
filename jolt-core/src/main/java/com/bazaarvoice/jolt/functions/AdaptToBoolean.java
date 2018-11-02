package com.bazaarvoice.jolt.functions;

import com.bazaarvoice.jolt.common.Optional;

public class AdaptToBoolean implements Adapter<Boolean>{

    @Override
    public Optional<Boolean> adapt( Object obj ) {

        KnownTypes knownType = KnownTypes.identifyObject( obj );

        switch (knownType) {
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

    public static Boolean adapt( String str ) {
        return Boolean.valueOf( str );
    }

    public static Boolean truthyAdapt( int number ) {
        return number >= 1;
    }

    public static Boolean truthyAdapt( long number ) {
        return number >= 1;
    }
}
