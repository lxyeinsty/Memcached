package cn.edu.hust.memcached.server.message.exeception;

import cn.edu.hust.memcached.server.message.enums.Status;

/**
 * Created by lxy on 2017/10/21.
 *
 */
public class MessageException extends Exception {
    private String statusCode;
    private String statusMsg;
    private String statusDetail;

    public MessageException(Status status) {
        super(status.getStatusMsg() + status.getStatusDetail());
        this.statusCode = status.getStatusCode();
        this.statusMsg = status.getStatusMsg();
        this.statusDetail = status.getStatusDetail();
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMsg() {
        return this.statusMsg;
    }

    public String getStatusDetail() {
        return this.statusDetail;
    }
}
