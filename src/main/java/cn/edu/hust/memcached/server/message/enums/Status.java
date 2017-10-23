package cn.edu.hust.memcached.server.message.enums;

/**
 * Created by lxy on 2017/10/22.
 * 服务端响应给客户端的状态标识
 */
public enum Status {
    //正确操作命令返回状态
    STORED("10001", "STORED", ""),
    NOT_STORED("10002", "NOT_STORED", ""),
    EXISTS("10003", "EXISTS", ""),
    NOT_FOUND("10004", "NOT_FOUND", ""),
    DELETED("10005", "DELETED", ""),
    END("100006", "END", ""),

    //错误,客户端或者服务端错误
    ERROR("20001", "ERROR", ""),
    CLIENT_ERROR_BAD_DATA("20002", "CLIENT_ERROR", " bad data chunk\nERROR"),
    CLIENT_ERROR_BAD_COMMAND("20003", "CLIENT_ERROR", " bad command line format"),
    CLIENT_ERROR_BATCH_DEL("20004", "CLIENT_ERROR", " bad command line format.  Usage: delete <key> [noreply]"),
    SERVER_ERROR("20005", "SERVER_ERROR", "");

    private String statusCode;
    private String statusMsg;
    private String statusDetail;

    public String getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMsg() {
        return this.statusMsg;
    }

    public String getStatusDetail() {
        return this.statusDetail;
    }

    Status(String statusCode, String statusMsg, String statusDetail) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.statusDetail = statusDetail;
    }
}
