package com.bazaarvoice.jolt.functions.adapters;

import com.bazaarvoice.jolt.common.Optional;
import com.bazaarvoice.jolt.functions.KnownTypes;

public class AdaptToDouble implements Adapter<Double>{

    @Override
    public Optional<Double> adapt( Object obj ) {

        KnownTypes knownType = KnownTypes.identifyDataObject( obj );

        switch (knownType) {
            case NULL:
                return Optional.empty();
            case STRING:
                Double adapted = null;
                try {
                    adapted = Double.parseDouble( (String) obj );
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
                return Optional.of( ((Boolean) obj) ? 1.0 : 0.0 );
            case INT:
                return Optional.of( ((Integer) obj).doubleValue() );
            case LONG:
                return Optional.of( ((Long) obj).doubleValue() );
            case FLOAT:
                return Optional.of( ((Float) obj).doubleValue() );
            case DOUBLE:
                return Optional.of( (Double) obj );
            default:
                return Optional.empty();
        }
    }
}
