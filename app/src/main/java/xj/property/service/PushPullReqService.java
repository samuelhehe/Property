package xj.property.service;

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
import xj.property.utils.PushServiceReqUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

public class PushPullReqService extends IntentService {


    private final String TAG = "PushPullReqService";

    public PushPullReqService() {
        super("xj.property.service.PushPullReqService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent ");
        PushServiceReqUtils pushServiceReqUtils = new PushServiceReqUtils();
        pushServiceReqUtils.getUnreadInfo(getApplicationContext());

    }

}
