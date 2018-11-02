package com.bazaarvoice.jolt.functions;

import java.util.HashMap;
import java.util.Map;

import static com.bazaarvoice.jolt.functions.KnownTypes.*;

public class Adapters {

    private static Map<KnownTypes,Adapter> adapters = new HashMap<>();

    static {
        adapters.put( BOOL,   new AdaptToBoolean() );
        adapters.put( INT,    new AdaptToInteger() );
        adapters.put( STRING, new AdaptToString() );
    }

    public static Adapter lookupAdapter( KnownTypes knownType ) {
        return adapters.get( knownType );
    }
}
