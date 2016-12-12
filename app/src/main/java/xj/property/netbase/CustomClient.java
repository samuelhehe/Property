package xj.property.netbase;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/12/15.
 */
public class CustomClient extends OkClient {

    private HashMap<String, String> queryMap;
    private Gson gson = new Gson();

    private BaseBean baseBean;

    public CustomClient(Context context, BaseBean baseBean) {
        baseBean.setV("a" + PreferencesUtil.getVersionName(context));
        this.baseBean = baseBean;
    }

    public CustomClient(Context context, BaseBean baseBean, HashMap<String, String> option) {
        baseBean.setV("a" + PreferencesUtil.getVersionName(context));
        this.baseBean = baseBean;
        this.queryMap = option;
    }

    @Override
    public Response execute(Request request) throws IOException {
        JSONObject param = null;
        if (null != baseBean) {
            try {
                param = new JSONObject(gson.toJson(baseBean));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (null == param) {
            param = new JSONObject();
        }
        try {

//            queryMap = new HashMap<>();
//            queryMap.put("a","1");
            if (null != queryMap) {
                Set<Map.Entry<String, String>> entries = queryMap.entrySet();

                for (Map.Entry<String, String> entry : entries) {
                    if (!TextUtils.isEmpty(entry.getKey())) {
                        param.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            param.put("url", request.getUrl());
            param.put("method", request.getMethod().toUpperCase());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Header> headers = new ArrayList<>();
        headers.addAll(request.getHeaders());
        headers.add(new Header("signature", StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(param))));

        return super.execute(new Request(request.getMethod(), request.getUrl(), headers, request.getBody()));
//        return super.execute(new Request(request.getMethod(), request.getUrl() + "?a=1", headers, request.getBody()));
    }

    public BaseBean getBaseBean() {
        return baseBean;
    }

    public void setBaseBean(BaseBean baseBean) {
        this.baseBean = baseBean;
    }
}
