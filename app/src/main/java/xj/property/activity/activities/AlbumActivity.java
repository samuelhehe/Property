package xj.property.activity.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import java.io.File;
import java.io.Serializable;
import java.util.List;

import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.BaseUtils;
import xj.property.utils.other.Config;

public class AlbumActivity extends HXBaseActivity {
    /**
     * logger
     */

    /**
     * list of album model
     */
    private List<AlbumModel> albumModelList;
    /**
     * gridview for album
     */
    private GridView gv_album;
    /**
     * adapter for album
     */
    private AlbumAdapter albumAdapter;
    /**
     * album helper
     */
    private AlbumHelper helper;
    /**
     * key for iamge list transfer
     */
    public static final String EXTRA_IMAGE_LIST = "imageList";

    /**
     * reqeust code to album detail
     */
    public static final int requestCode_to_albumDetail = 1;

    public static final int resultCode_to_NewActivity = 2;

    /**
     * bitmap
     */
    public static Bitmap bimap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initTitle("","相册","");
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        /*IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_SCANNER_STARTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addDataScheme("file");
        registerReceiver(scanReceiver, intentFilter);*/
//        if(BaseUtils.getAndroidOSVersion()<19) {
//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +
//                    Environment.getExternalStorageDirectory())));
//        } else MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
//            /*
//             *   (non-Javadoc)
//             * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
//             */
//            public void onScanCompleted(String path, Uri uri) {
//            }
//        });

        initData();
        initView();

    }


//    public File getNewestFileInDirectory() {
//        File newestFile = null;
//
//        // start loop trough files in directory
//        File file = new File(filePath);
//        if (newestFile == null || file.lastModified().after(newestFile.lastModified())) {
//            newestFile = file;
//        }
//        // end loop trough files in directory
//
//        return newestFile;
//    }

    /**
     * init data
     */
    private void initData() {
        albumModelList = helper.getImagesBucketList(false);
    }

    /**
     * init view
     */
    private void initView() {
        gv_album = (GridView) findViewById(R.id.gv_album);

        albumAdapter = new AlbumAdapter(AlbumActivity.this, albumModelList);

        gv_album.setAdapter(albumAdapter);

        gv_album.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(AlbumActivity.this,
                        AlbumDetailActivity.class);
                Log.i("onion","albmac:"+getIntent().getIntExtra(Config.INTENT_IMAGECOUNT,6));
                intent.putExtra("ImageCount",getIntent().getIntExtra(Config.INTENT_IMAGECOUNT,6));
                MediaScannerConnection.scanFile(AlbumActivity.this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    /*
                     *   (non-Javadoc)
                     * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                     */
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });


                intent.putExtra(AlbumActivity.EXTRA_IMAGE_LIST,(Serializable) albumModelList.get(position).imageList);

               if(getIntent().getIntExtra(Config.INTENT_PARMAS1,0)== Config.SelectAblum) {
                   intent.putExtra(Config.INTENT_PARMAS1,Config.SelectAblum);
                   startActivityForResult(intent,Config.SelectAblum);
//                   startActivityForResult(intent, Config.SelectAblum);
               }else  startActivityForResult(intent, requestCode_to_albumDetail);



            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode ,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == AlbumDetailActivity.resultCode_to_album){
            if (BitmapHelper.imageListStorage.size()!=0){
                Intent intentPop = new Intent();
                intentPop.setClass(AlbumActivity.this,NewActivityActivity.class);
                setResult(resultCode_to_NewActivity,intentPop);
                if(data != null){
                }
                finish();
            }

        }else if(resultCode==RESULT_OK){
            Intent intentPop = new Intent();
            intentPop.setClass(AlbumActivity.this,ChatActivity.class);
            setResult(RESULT_OK,intentPop);
            finish();
        }

    }

    @Override
    public void onClick(View v) {

    }
}
