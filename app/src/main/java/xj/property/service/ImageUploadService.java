package xj.property.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import xj.property.utils.ImageUploadUtils;

public class ImageUploadService extends IntentService {


    private final String TAG = "ImageUploadService";

    public ImageUploadService() {
        super("xj.property.service.ImageUploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent ");
        final String path = intent.getStringExtra("path");
        final String token = intent.getStringExtra("token");
        final String resKey = intent.getStringExtra("reskey");

        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageUploadUtils.uploadImage(token, path, resKey);
            }
        }).start();
    }

}
