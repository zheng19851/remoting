package com.runssnail.remoting.common.serialize;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serialization. (SPI, Singleton, ThreadSafe)
 *
 */
public interface Serialization {

    /**
     * get type id
     *
     * @return type id
     */
    byte getTypeId();

    /**
     * create serializer
     *
     * @param output
     * @return serializer
     * @throws IOException
     */
    ObjectOutput serialize(OutputStream output) throws IOException;

    /**
     * create deserializer
     *
     * @param input
     * @return deserializer
     * @throws IOException
     */
    ObjectInput deserialize(InputStream input) throws IOException;

}