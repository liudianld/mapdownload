package com.shenlandt.wh.internals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * image abstraction proxy
 */
public abstract class PureImageProxy {
    public abstract PureImage FromStream(ByteArrayInputStream stream);

    public final PureImage FromArray(byte[] data) throws IOException {
        PureImage pi = null;
        
        ByteArrayInputStream bin = new ByteArrayInputStream(data);

        pi = FromStream(bin);
        pi.Data = bin;
        
        bin.close();
        return pi;
    }
}