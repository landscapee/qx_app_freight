package qx.app.freight.qxappfreight.utils;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import lombok.Data;

@Data
public class CommonJson4List<T> implements Serializable {
    private String workerId;
    private int taskType;//当cancelFlag为true时，taskType的值如果为1则代表装卸机任务取消，为2则代表运输任务取消；当cancelFlag为false时，taskType的值如果为1或2则代表装卸机新任务，为0则代表运输新任务
    private boolean cancelFlag;//是否是取消装卸机或运输任务
    private boolean changeWorkerUser;//是否是任务换人
    private boolean splitTask;//是否是拆分任务
    private String taskId;//取消任务时对应的taskId标识
    private boolean confirmTask=true;
    private boolean transportTaskAutoDone;//任务超时自动完成
    private List<T> taskData;
    public  CommonJson4List<T> fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(CommonJson4List.class, clazz);
        return gson.fromJson(json, objectType);
    }
    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }
            public Type[] getActualTypeArguments() {
                return args;
            }
            public Type getOwnerType() {
                return null;
            }
        };
    }
}

