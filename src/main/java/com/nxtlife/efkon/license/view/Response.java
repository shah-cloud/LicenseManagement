package com.nxtlife.efkon.license.view;

import com.nxtlife.efkon.license.util.LongObfuscator;

public interface Response {

    default Long mask(final Long number) {
        return number != null ? LongObfuscator.INSTANCE.obfuscate(number) : null;
    }
}
