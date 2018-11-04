package com.bazaarvoice.jolt.functions.adapters;

import com.bazaarvoice.jolt.common.Optional;

public interface Adapter<T> {

    Optional<T> adapt( Object obj );
}
