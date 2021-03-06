package cn.edu.hust.memcached.server.message.enums;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxy on 2017/10/20.
 * 支持的命令类型
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
        List<String> valueList = Lists.newArrayList();
        CommandType[] commands = CommandType.values();
        for (CommandType command : commands) {
            valueList.add(command.getValue());
        }
        return valueList;
    }
}
