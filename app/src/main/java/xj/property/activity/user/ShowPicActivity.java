package xj.property.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.activities.BitmapHelper;
import xj.property.utils.other.Config;
import xj.property.widget.avatar.ClipSquareImageView;

/**
 * Created by Administrator on 2015/3/25.
 */
public class ShowPicActivity extends HXBaseActivity {

    private String photoName;


    private ImageView iv_submit;
    private ImageView iv_back;
    private Bitmap oldbitmap;
    private Bitmap bitmap;

    private String path;

    private File fileFolder = null;
    private File jpgFile = null;
    private byte[] data = null;
    private ClipSquareImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpicc);

        photoName = getIntent().getStringExtra("photoName");

        try {
            BitmapHelper.revitionImageSizeBydegree(photoName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        oldbitmap = BitmapFactory.decodeFile(photoName);

        imageView = (ClipSquareImageView) findViewById(R.id.cimg);
        imageView.setImageBitmap(oldbitmap);
        Log.i("onion", "photoName: " + photoName);
        intView();

    }


    private void intView() {
        iv_submit = (ImageView) findViewById(R.id.iv_submit);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("photoName", "");
                setResult(4, data);
                finish();
            }
        });
        iv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 此处获取剪裁后的bitmap
                bitmap = imageView.clip();

                // 由于Intent传递bitmap不能超过40k,此处使用二进制数组传递
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photoName = saveToSDCard(baos);
                Intent data = new Intent();
                data.putExtra("photoName", photoName);
                setResult(4, data);
                finish();
            }
        });
    }

    private String saveToSDCard(final ByteArrayOutputStream baos) {
        String fileName;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data = baos.toByteArray();
        fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";

        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            path = Environment.getExternalStorageDirectory()
                    + File.separator + Config.BASE_GROUP_CACHE +
                    File.separator + "image";
            fileFolder = new File(path);
        } else {
            path = ShowPicActivity.this.getCacheDir()
                    + File.separator + Config.BASE_GROUP_CACHE +
                    File.separator + "image";
            fileFolder = new File(path);
        }

        if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdir();
        }
        jpgFile = new File(fileFolder, fileName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(jpgFile); // 文件输出流
            outputStream.write(data); // 写入sd卡中
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {// 关闭输出流
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return path + File.separator + fileName;
    }


    @Override
    public void onClick(View v) {

    }

    private String getFileName() {
        String fileName = String.valueOf(System.currentTimeMillis());
        String path;
        File sdcardPath = Environment.getExternalStorageDirectory();
        path = sdcardPath.getAbsolutePath() + File.separator + Config.BASE_GROUP_CACHE +
                File.separator + "image";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator + fileName + ".jpg";
    }
}
