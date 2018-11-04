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
