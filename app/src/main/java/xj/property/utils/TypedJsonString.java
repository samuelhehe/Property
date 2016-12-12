package xj.property.utils;

import retrofit.mime.TypedString;

/**
 * 作者：asia on 2016/3/7 12:41
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class TypedJsonString extends TypedString {
    public TypedJsonString(String body) {
        super(body);
    }

    @Override public String mimeType() {
        return "application/json";
    }
}
