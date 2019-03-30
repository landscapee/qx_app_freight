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
    private int taskType;
    private boolean cancelFlag;
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

