package com.runssnail.remoting.exchange;

/**
 * Response
 */
public class Response extends Message {

    private static final long serialVersionUID = 3909869540630529640L;

    public static final String HEARTBEAT_EVENT = null;

    public static final String READONLY_EVENT = "R";

    /**
     * ok.
     */
    public static final byte OK = 20;

    /**
     * clien side timeout.
     */
    public static final byte CLIENT_TIMEOUT = 30;

    /**
     * server side timeout.
     */
    public static final byte SERVER_TIMEOUT = 31;

    /**
     * request format error.
     */
    public static final byte BAD_REQUEST = 40;

    /**
     * response format error.
     */
    public static final byte BAD_RESPONSE = 50;

    /**
     * service not found.
     */
    public static final byte SERVICE_NOT_FOUND = 60;

    /**
     * service error.
     */
    public static final byte SERVICE_ERROR = 70;

    /**
     * internal server error.
     */
    public static final byte SERVER_ERROR = 80;

    /**
     * internal server error.
     */
    public static final byte CLIENT_ERROR = 90;

    /**
     * server side threadpool exhausted and quick return.
     */
    public static final byte SERVER_THREADPOOL_EXHAUSTED_ERROR = 100;

    private byte status = OK;

    private String errorMsg;

    public Response() {
    }

    public Response(int id) {
        super(id);
    }

    public Response(int id, short version) {
        super(id);
        this.version = version;
    }

    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public boolean isRequest() {
        return false;
    }

    @Override
    public boolean isTwoWay() {
        return false;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "version=" + version +
                ", id=" + id +
                ", remark='" + remark + '\'' +
                ", data=" + data +
                ", event=" + event +
                ", status=" + status +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}