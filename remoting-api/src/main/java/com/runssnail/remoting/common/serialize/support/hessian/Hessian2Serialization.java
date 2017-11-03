package com.runssnail.remoting.common.serialize.support.hessian;


import com.runssnail.remoting.common.serialize.ObjectInput;
import com.runssnail.remoting.common.serialize.ObjectOutput;
import com.runssnail.remoting.common.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Hessian2Serialization implements Serialization {

    public static final byte ID = 2;

    public byte getTypeId() {
        return ID;
    }

    public ObjectOutput serialize(OutputStream out) throws IOException {
        return new Hessian2ObjectOutput(out);
    }

    public ObjectInput deserialize(InputStream is) throws IOException {
        return new Hessian2ObjectInput(is);
    }

}