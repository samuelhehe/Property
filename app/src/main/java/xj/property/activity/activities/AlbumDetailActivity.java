package xj.property.activity.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.Config;

public class AlbumDetailActivity extends HXBaseActivity {
    /**
     * logger
     */
    /**
     * tag to get iamge list
     */
	public static final String EXTRA_IMAGE_LIST = "imageList";
    /**
     * result code fo album detail
     */
    public static final int resultCode_to_album = 1;

    /**
     * image list
     */
	private List<ImageModel> imageModelList;
    /**
     * gridview for album
     */
	private GridView gv_album_detail;
    /**
     * adapter for album
     */
	private AlbumDetailGridAdapter albumDetailGridAdapter;
    /**
     * album helper class
     */
	private AlbumHelper helper;
    /**
     * button for image selected
     */
	private Button btn_image_selected;
    private int imageCount;

    /**
     * handler
     */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
                	Toast.makeText(AlbumDetailActivity.this, "最多选择"+imageCount+"张图片", Toast.LENGTH_SHORT).show();
//				Toast.makeText(AlbumDetailActivity.this, "最多选择6张图片", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

    @Override
    public void onClick(View v) {


    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_detail);
        initTitle("","相册","");
        imageCount=getIntent().getIntExtra("ImageCount",6);
        Log.i("onion","imageCount: "+imageCount);
        //get helper
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
        //get image model list
        imageModelList = (List<ImageModel>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);
		initView();
	}

    /**
     * init view
     */
	private void initView() {
        gv_album_detail = (GridView) findViewById(R.id.gv_album_detail);
        gv_album_detail.setSelector(new ColorDrawable(Color.TRANSPARENT));

		albumDetailGridAdapter = new AlbumDetailGridAdapter(AlbumDetailActivity.this,imageCount, imageModelList,
				mHandler);
        gv_album_detail.setAdapter(albumDetailGridAdapter);
        albumDetailGridAdapter.setTextCallback(new AlbumDetailGridAdapter.TextCallback() {
			public void onListen(int count) {
				btn_image_selected.setText("完成" + "(" + count + ")");
			}
		});

        gv_album_detail.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                albumDetailGridAdapter.notifyDataSetChanged();
            }

        });

        btn_image_selected = (Button) findViewById(R.id.btn_image_selected);
        btn_image_selected.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = albumDetailGridAdapter.map.values();

                for(String cpath : c){
                    File file = new File(cpath);
                    //// 过滤空图片,不存在的图片..
                    if(file.exists()&&file.length()>0&&file.isFile()){
                        list.add(cpath);
                    }else{
                        showToast("无法加载图片");
                    }
                }

//                if (BitmapHelper.act_bool) {
//                    logger.info("go to newActivity ");
//                    logger.info("act_boll is :"+BitmapHelper.act_bool);
//                    Intent intent = new Intent(AlbumDetailActivity.this,NewActivityActivity.class);
//                    startActivity(intent);
//                    BitmapHelper.act_bool = false;
//                }
                Intent intentPop = new Intent();
                intentPop.setClass(AlbumDetailActivity.this,AlbumActivity.class);
                if(getIntent().getIntExtra(Config.INTENT_PARMAS1,0)==Config.SelectAblum){
                    /// selected imgs .... 2015/12/28
                    BitmapHelper.bitmapChatListStorage=list;
                    setResult(RESULT_OK, intentPop);
                }else {
                    for (int i = 0; i < list.size(); i++) {
                        if (BitmapHelper.imageListStorage.size() < imageCount) {
//                            String filename = getFileName();
//                            BitmapHelper.copyfile(new File(list.get(i)),new File(filename),true);
                            BitmapHelper.imageListStorage.add(list.get(i));
                        }
                    }
//                    Intent intent = new Intent();
//                    intent.setClass(AlbumDetailActivity.this,NewActivityActivity.class);
//                    startActivity(intent);
                    setResult(resultCode_to_album, intentPop);
                }

                finish();
            }
        });
	}

    private String getFileName() {
        String fileName = String.valueOf(System.currentTimeMillis());
        String path;
        File sdcardPath = Environment.getExternalStorageDirectory();
        path = sdcardPath.getAbsolutePath() + File.separator + Config.BASE_IMAGE_CACHE +
                File.separator + "image";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator + fileName + ".jpg";
    }
}
