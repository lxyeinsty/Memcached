package cn.edu.hust.memcached.server.message.exeception;

/**
 * Created by lxy on 2017/10/21.
 *
 */
public class UnSupportedCommandException extends Exception {

    public UnSupportedCommandException(String command) {
        super("Unsupported the command : " + command);
    }
}
