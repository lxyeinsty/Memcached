package cn.edu.hust.memcached.server.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxy on 2017/10/20.
 *
 */
public enum CommandType {
    GET("get"),
    SET("set"),
    DELETE("delete");

    private final String value;

    CommandType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    //获取命令列表
    public static List<String> getValueList() {
        List<String> valueList = new ArrayList<String>();
        CommandType[] commands = CommandType.values();
        for (CommandType command : commands) {
            valueList.add(command.getValue());
        }
        return valueList;
    }
}
