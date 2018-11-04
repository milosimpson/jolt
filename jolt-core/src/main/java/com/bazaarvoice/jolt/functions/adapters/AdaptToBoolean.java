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

package com.bazaarvoice.jolt.functions.adapters;

import com.bazaarvoice.jolt.common.Optional;
import com.bazaarvoice.jolt.functions.KnownTypes;

public class AdaptToBoolean implements Adapter<Boolean>{

    @Override
    public Optional<Boolean> adapt( Object obj ) {

        KnownTypes knownType = KnownTypes.identifyDataObject( obj );

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
