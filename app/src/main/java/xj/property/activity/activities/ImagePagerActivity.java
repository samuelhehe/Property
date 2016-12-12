package xj.property.activity.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;

public class ImagePagerActivity extends HXBaseActivity {
    /**
     * logger
     */
    /**
     * result code to NewActivity
     */
    public final static int resultCode_to_newAcitivity = 1;

    private ArrayList<View> listViews = null;
    private ViewPager pager;
    private MyPageAdapter adapter;
    private int count;

    public List<Bitmap> bitmapListMemory = new ArrayList<Bitmap>();
    public List<String> imageListStorage = new ArrayList<String>();
    public List<String> delList = new ArrayList<String>();
    public int imageCount;

    RelativeLayout photo_relativeLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);
        initTitle("", "图片", "删除");

        photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
        photo_relativeLayout.setBackgroundColor(0x70000000);

        for (int i = 0; i < BitmapHelper.bitmapListMemory.size(); i++) {
            bitmapListMemory.add(BitmapHelper.bitmapListMemory.get(i));
        }
        for (int i = 0; i < BitmapHelper.imageListStorage.size(); i++) {
            imageListStorage.add(BitmapHelper.imageListStorage.get(i));
        }
        imageCount = BitmapHelper.imageCount;

        Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
        photo_bt_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();
            }
        });
        Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
        photo_bt_del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deletePic();
            }
        });
        Button photo_bt_enter = (Button) findViewById(R.id.photo_bt_enter);
        photo_bt_enter.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                BitmapHelper.bitmapListMemory = bitmapListMemory;
                BitmapHelper.imageListStorage = imageListStorage;
                BitmapHelper.imageCount = imageCount;
                for (int i = 0; i < delList.size(); i++) {
                    FileUtils.delFile(delList.get(i) + ".JPEG");
                }
                Intent intentPop = new Intent();
                setResult(resultCode_to_newAcitivity, intentPop);
                finish();
            }
        });

        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < bitmapListMemory.size(); i++) {
            initListViews(bitmapListMemory.get(i));//
        }

        adapter = new MyPageAdapter(listViews);// 构造adapter
        pager.setAdapter(adapter);// 设置适配器
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);
    }

    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        ImageView img = new ImageView(this);// 构造textView对象
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        listViews.add(img);// 添加view
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {// 页面选择响应函数
            count = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

        }

        public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

        }
    };

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;// content

        private int size;// 页数

        public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
            // 初始化viewpager的时候给的一个页面
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {// 返回数量
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
            ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {// 返回view对象
            try {
                ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    private void deletePic() {
        if (listViews.size() == 1) {
            BitmapHelper.bitmapListMemory.clear();
            BitmapHelper.imageListStorage.clear();
            BitmapHelper.uploadImageStorage.clear();
            BitmapHelper.imageCount = 0;
            FileUtils.deleteDir();
            Intent intentPop = new Intent();
            setResult(resultCode_to_newAcitivity, intentPop);
            finish();
        } else {
            Log.i("onion","删除"+count);
            String newStr = imageListStorage.get(count).substring(
                    imageListStorage.get(count).lastIndexOf("/") + 1,
                    imageListStorage.get(count).lastIndexOf("."));
            bitmapListMemory.remove(count);
            imageListStorage.remove(count);
            BitmapHelper.imageListStorage.remove(count);
            BitmapHelper.bitmapListMemory.remove(count);
            BitmapHelper.uploadImageStorage.remove(count);
            BitmapHelper.imageCount--;
            delList.add(newStr);
            imageCount--;
            pager.removeAllViews();
            listViews.remove(count);
            adapter.setListViews(listViews);
            adapter.notifyDataSetChanged();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                deletePic();
                break;
            case R.id.iv_back:
                BitmapHelper.bitmapListMemory = bitmapListMemory;
                BitmapHelper.imageListStorage = imageListStorage;
                BitmapHelper.imageCount = imageCount;
                for (int i = 0; i < delList.size(); i++) {
                    FileUtils.delFile(delList.get(i) + ".JPEG");
                }
                Intent intentPop = new Intent();
                setResult(resultCode_to_newAcitivity, intentPop);
                finish();
                break;

        }
    }
}
