package xj.property.netbase;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by Administrator on 2016/2/25.
 */
public class CustomJsonConverter implements Converter {

    private final Gson gson = new Gson();
    private String charset;

    /**
     * Create an instance using the supplied {@link Gson} object for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use the specified charset.
     */
    public CustomJsonConverter() {
        this.charset = "utf-8";
    }

    public Object fromTBody(TypedInput body, Type type) throws ConversionException {
        String charset = this.charset;
        if (body.mimeType() != null) {
            charset = MimeUtil.parseCharset(body.mimeType(), charset);
        }
        try {
            String s = Inputstr2Str_Reader(body.in(), charset);
            JSONObject jsonObject = new JSONObject(s);
            CommonRespBean commonRespBean = new CommonRespBean();
            for (Iterator<String> keys = jsonObject.keys(); keys.hasNext(); ) {
                String key = keys.next();
                Object value = jsonObject.get(key);
                if (!TextUtils.equals("data", key)) {
                    commonRespBean.setField(key, value);
                } else {
                    if (value instanceof JSONObject ) {
                        commonRespBean.setData(gson.fromJson(jsonObject.getJSONObject("data").toString(), type));
                    } else if( value instanceof JSONArray){
                        commonRespBean.setData(gson.fromJson(jsonObject.getJSONArray("data").toString(), type));
                    } else {
                        commonRespBean.setData(value);
                    }
                }
            }
            return commonRespBean;
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        try {
            Log.d("CustomJsonConverter ", "fromBody-->" + ((ParameterizedType) type).getActualTypeArguments()[0]);
            return fromTBody(body, ((ParameterizedType) type).getActualTypeArguments()[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("CustomJsonConverter ", "fromBody-->" + type);
            return fromTBody(body, type);
        }
//       return fromTBody(body, (Class<?>) ((ParameterizedType) ((Class<?>) type).getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Override
    public TypedOutput toBody(Object object) {
        try {
            return new JsonTypedOutput(gson.toJson(object).getBytes(charset), charset);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        JsonTypedOutput(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + encode;
        }

        @Override
        public String fileName() {
            return null;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return jsonBytes.length;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }

    /**
     * 利用BufferedReader实现Inputstream转换成String <功能详细描述>
     *
     * @param in
     * @return String
     */

    public static String Inputstr2Str_Reader(InputStream in, String encode) throws Exception {
        if (encode == null || encode.equals("")) {
            encode = "utf-8";// 默认以utf-8形式
        }
        BufferedReader reader = null;
        try {
            String str = null;
            reader = new BufferedReader(new InputStreamReader(in, encode));
            StringBuilder sb = new StringBuilder();
            while ((str = reader.readLine()) != null) {
                sb.append(str).append("\n");
            }
            return sb.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
