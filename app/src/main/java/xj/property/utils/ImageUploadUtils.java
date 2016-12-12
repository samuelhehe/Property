package xj.property.utils;

import android.text.TextUtils;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Administrator on 2016/2/23.
 */
public class ImageUploadUtils {

    private static final String TAG ="ImageUploadUtils" ;

    public ImageUploadUtils(){

    }

    /**
     * 上传多张图到七牛云存储
     *
     * 注意： 当前使用的token的使用过期时长是60分钟。
     * @param token
     * @param path$uniquekeys
     */
    public static void uploadImages(String token , Map<String,String> path$uniquekeys){
        if(TextUtils.isEmpty(token)||path$uniquekeys==null||path$uniquekeys.size()<1){
            throw  new IllegalArgumentException("token "+ token +" path&uniquekey "+ path$uniquekeys);
        }
        Set<Map.Entry<String, String>> entries = path$uniquekeys.entrySet();
        for (Map.Entry<String,String> entry: entries){
            uploadImage(token,entry.getKey(),entry.getValue());
        }
    }

    /**
     * 上传单张图片到七牛云存储
     *
     * @param token  七牛token
     * @param path  上传需要的图片路径
     * @param resuniquekey  该路径对应上传到的resunique key 位置。
     */
    public static  void uploadImage(String token, String path, String resuniquekey) {
        if(TextUtils.isEmpty(token)||TextUtils.isEmpty(path)||TextUtils.isEmpty(resuniquekey)){
            throw  new IllegalArgumentException("token "+ token +" path "+ path +" resuniquekey "+ resuniquekey);
        }
        Log.d(TAG,"uploadImage ---token "+ token +" path "+ path +" resuniquekey "+ resuniquekey);
        UploadManager um = new UploadManager();
        um.put(path, resuniquekey, token, new UpCompletionHandler() {
            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                try {
                    String headerIconUrl = jsonObject.optString("key");
                    Log.d(TAG, "complete headerIconUrl " + headerIconUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    /**
     * 生成资源定位，唯一key
     *
     * @return
     */
    public static  String generateResKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
