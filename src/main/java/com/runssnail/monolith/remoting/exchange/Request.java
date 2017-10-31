/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.runssnail.monolith.remoting.exchange;


import com.runssnail.monolith.common.util.StringUtils;
import com.runssnail.monolith.remoting.transport.netty4.Message;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Request.
 *
 */
public class Request extends Message {

    private static final long serialVersionUID = -7909342436430616833L;

    private static final AtomicLong REQUEST_ID = new AtomicLong(0);

    public static final String READONLY_EVENT = "R";

    private String version;

    private boolean twoWay = true;

    private boolean broken = false;

    public Request() {
        super(newId());
    }

    public Request(long id) {
         super(id);
    }

    private static long newId() {
        // getAndIncrement()增长到MAX_VALUE时，再增长会变为MIN_VALUE，负数也可以做为ID
        return REQUEST_ID.getAndIncrement();
    }

    private static String safeToString(Object data) {
        if (data == null) return null;
        String dataStr;
        try {
            dataStr = data.toString();
        } catch (Throwable e) {
            dataStr = "<Fail toString of " + data.getClass() + ", cause: " +
                    StringUtils.toString(e) + ">";
        }
        return dataStr;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public void setTwoWay(boolean twoWay) {
        this.twoWay = twoWay;
    }

    @Override
    public byte getState() {
        return 0;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean mBroken) {
        this.broken = mBroken;
    }

}
