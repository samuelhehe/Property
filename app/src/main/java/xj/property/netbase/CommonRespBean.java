package xj.property.netbase;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Administrator on 2016/2/23.
 */
public class CommonRespBean<T> {

    private Map<String, Object> objectList = new HashMap<>();


    private static final String FIELD_CODE = "code";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_DATA = "data";
    private static final String FIELD_MESSAGE = "message";

    /**
     * code : 200
     * status : yes
     * data : 2930
     */
    private T data;

    public void setField(String key, Object value) {
        objectList.put(key, value);
    }

    public Object getField(String key) {
        return objectList.get(key);
    }

    public <T> T getField(String key, Class<T> clazz) {
        return (T) getField(key);
    }


    public String getMessage() {
        return getField(FIELD_MESSAGE, String.class);
    }


    public int getCode() {
        return getField(FIELD_CODE, Integer.class);
    }

    public String getStatus() {
        return getField(FIELD_STATUS, String.class);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
