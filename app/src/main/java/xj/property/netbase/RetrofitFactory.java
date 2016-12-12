package xj.property.netbase;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;

import retrofit.RestAdapter;
import xj.property.utils.other.Config;

/**
 * Created by Administrator on 2016/2/23.
 */
public class RetrofitFactory {

    private static RetrofitFactory retrofitFactory;

    private RetrofitFactory(){}

    public static  RetrofitFactory getInstance(){
        if(retrofitFactory ==null){
            synchronized (RetrofitFactory.class){
                if(retrofitFactory==null){
                    retrofitFactory = new RetrofitFactory();
                }
            }
        }
        return retrofitFactory;
    }

    /**
     * 包含签名的创建RequestService的方法。 不需要Query 参数的情况下使用。
     * 待测
     * @param context
     * @param request
     * @param clazz
     * @return
     */
    public <T> T create(Context context, BaseBean request, Class<T> clazz){
       return create(context,request,null,clazz);
    }



    /**
     * 包含签名的创建RequestService的方法。 不需要Bean 参数的情况下使用。
     * 待测
     * @param context
     * @param request
     * @param clazz
     * @return
     */
    public <T> T create(Context context, HashMap<String,String> request, Class<T> clazz){
       return create(context,null,request,clazz);
    }

    /**
     * 包含参数创建的
     *
     * 包含签名的创建RequestService的方法。需要Query 参数的情况下使用。
     *  这一部分使用： eg： http://api.xiaojian.com/api/v3/communities/1/authcode? abc= ddd & ddd
     * @param context
     * @param request
     * @param option
     * @param clazz
     * @return
     */
    public <T> T create(Context context, BaseBean request, HashMap<String, String> option, Class<T> clazz){
        RestAdapter restAdapter  = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).setConverter(new CustomJsonConverter()).build();
        if(Config.SHOW_lOG){
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        return  restAdapter.create(clazz);

//        RestAdapter restAdapter  = new RestAdapter.Builder()
//                .setClient(new CustomClient(context,request, option))
//                .setEndpoint(Config.NET_BASE)
//                .setConverter(new CustomJsonConverter()).build();
//        if(Config.SHOW_lOG){
//            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        }
//        return  restAdapter.create(clazz);
    }

    /**
     * 包含参数创建的
     *
     * 包含签名的创建RequestService的方法。需要Query 参数的情况下使用。
     *  这一部分使用： eg： http://api.xiaojian.com/api/v3/communities/1/authcode? abc= ddd & ddd
     * @param context
     * @param clazz
     * @return
     */
    public <T> T create(Context context,  Class<T> clazz){
        return create(context,null,null,clazz);
    }

    /**
     * 不包含签名
     *
     * @param clazz
     * @return
     */
//    public <T> T create(Class<T> clazz){
//        RestAdapter restAdapter  = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).setConverter(new CustomJsonConverter()).build();
//        if(Config.SHOW_lOG){
//            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        }
//        return  restAdapter.create(clazz);
//    }


}
