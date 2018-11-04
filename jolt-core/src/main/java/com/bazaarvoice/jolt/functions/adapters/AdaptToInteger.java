package com.bazaarvoice.jolt.functions.adapters;

import com.bazaarvoice.jolt.common.Optional;
import com.bazaarvoice.jolt.functions.KnownTypes;

public class AdaptToInteger implements Adapter<Integer>{

    @Override
    public Optional<Integer> adapt( Object obj ) {

        KnownTypes knownType = KnownTypes.identifyDataObject( obj );

        switch (knownType) {
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
}
