package xj.property.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.activeandroid.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.beans.InvatePhoneResult;
import xj.property.beans.MobilePhoneRequest;
import xj.property.beans.MobliePhone;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * 作者：asia on 2016/1/19 18:17
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 */
public class BackGroundService extends IntentService {

    private static final int UPDATE_PHONE = 1;

    private final String TAG = "CursorService";

    public BackGroundService() {
        super("xj.property.service.CursorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int type = intent.getIntExtra("type", 0);
        switch (type) {
            case UPDATE_PHONE:
                Log.i(TAG, "onHandleIntent===================UPDATE_PHONE");
                getContactInfo();
                break;
            default:
                Log.i(TAG, "onHandleIntent===================default");
                break;
        }
    }

    public static void updatePhone(Activity context) {
        Intent intent = new Intent(context, BackGroundService.class);
        intent.putExtra("type", UPDATE_PHONE);
        context.startService(intent);
    }

    interface IntentService {
        /**
         * 通过手机号进行邀请
         */
//        @POST("/api/v1/users/{emobId}/neighbors/phoneList")
//        void inviateByPhoneNum(@Header("signature") String signature, @Body MobilePhoneRequest acr, @Path("emobId") String emobId, Callback<InvatePhoneResult> cb);
//        @POST("/api/v1/users/{emobId}/neighbors/phoneList")

//        /api/v3/communities/{小区ID}/users/{用户环信ID}/addressBook
        @POST("/api/v3/communities/{communityId}/users/{emobId}/addressBook")
        void inviateByPhoneNum(@Path("communityId") int communityId, @Path("emobId") String emobId, @Body MobilePhoneRequest mobilePhoneRequest, Callback<CommonRespBean<String>> cb);
    }

    /**
     * 通过手机号发送短信邀请邻居
     */
    private void invateByPhone(List<MobliePhone> list) {
        String emobid = "";
        UserInfoDetailBean info = PreferencesUtil.getLoginInfo(getApplicationContext());
        if (info != null) {
            emobid = info.getEmobId();
        } else {
            emobid = PreferencesUtil.getTouristEmob(getApplicationContext());
        }
        if (!TextUtils.isEmpty(emobid) && list != null && list.size() > 0) {
            MobilePhoneRequest mobilePhoneRequest = new MobilePhoneRequest();
            mobilePhoneRequest.setPhones(list);
            IntentService service = RetrofitFactory.getInstance().create(this, mobilePhoneRequest, IntentService.class);
            Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
                @Override
                public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                    if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {
                        PreferencesUtil.saveUpdatePhone(getApplicationContext(), true);
                    } else {
                        Log.i(TAG, "invateByPhone     no======");
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i(TAG, "invateByPhone     error======" + error.toString());
                }
            };
            service.inviateByPhoneNum(PreferencesUtil.getCommityId(this), emobid, mobilePhoneRequest, callback);
        }
    }

    /**
     * 获取联系人信息列表
     */
    public void getContactInfo() {
        List<MobliePhone> list = new ArrayList<MobliePhone>();
        ContentResolver resolver = getContentResolver();
        //查询联系人
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        // 取得联系人名字 (显示出来的名字)，实际内容在 ContactsContract.Contacts中
        int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        for (cursor.moveToFirst(); (!cursor.isAfterLast()); cursor.moveToNext()) {
            //获取联系人ID
            String contactId = cursor.getString(idIndex);
            // 根据联系人ID查询对应的电话号码
            Cursor phoneNumbers = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            String phoneStr = "";

            // 取得电话号码(可能存在多个号码)
            while (phoneNumbers.moveToNext()) {
                String strPhoneNumber = phoneNumbers.getString(phoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneStr = strPhoneNumber + ",";
            }
            if (!"".equals(phoneStr)) {
                MobliePhone mobliePhone = new MobliePhone();
                mobliePhone.setName(cursor.getString(nameIndex));
                mobliePhone.setPhone(phoneStr.substring(0, phoneStr.length() - 2));
                list.add(mobliePhone);
            }

            phoneNumbers.close();
            // 根据联系人ID查询对应的email
//            Cursor emails = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
//                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
////取得email(可能存在多个email)
//            while (emails.moveToNext())
//            {
//                String strEmail = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//            }
//            emails.close();
        }
        cursor.close();
        invateByPhone(list);
    }

}
