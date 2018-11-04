package com.bazaarvoice.jolt.functions.adapters;

import com.bazaarvoice.jolt.common.Optional;
import com.bazaarvoice.jolt.functions.KnownTypes;

public class AdaptToString implements Adapter<String>{

    @Override
    public Optional<String> adapt( Object obj ) {

        KnownTypes knownType = KnownTypes.identifyDataObject( obj );

        switch (knownType) {
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
}
