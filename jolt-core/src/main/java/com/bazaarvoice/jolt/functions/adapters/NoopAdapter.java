package com.bazaarvoice.jolt.functions.adapters;

import com.bazaarvoice.jolt.common.Optional;

public class NoopAdapter implements Adapter{

    @Override
    public Optional<Boolean> adapt( Object obj ) {
        return Optional.empty();
    }
}
