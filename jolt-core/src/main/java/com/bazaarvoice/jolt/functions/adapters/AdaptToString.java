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
