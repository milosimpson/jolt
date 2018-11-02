package com.bazaarvoice.jolt.functions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.METHOD)
public @interface Function {
    String value(); // value is special / default can be just the single one
}
