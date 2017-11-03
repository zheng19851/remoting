package com.runssnail.remoting.common.serialize;

import java.io.IOException;

/**
 * Object output.
 *
 */
public interface ObjectOutput extends DataOutput {

    /**
     * write object.
     *
     * @param obj object.
     */
    void writeObject(Object obj) throws IOException;

}