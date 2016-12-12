package xj.property.activity.user;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.truba.touchgallery.GalleryWidget.BasePagerAdapter;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;

/**
 * Created by Administrator on 2015/3/25.
 */
public class ShowBigImageViewPager extends HXBaseActivity {
//    private ViewPager vp_pic;

    private ArrayList<String> images;
//    private MyPagerAdapter adapter;
    private int position;
    private ImageView image;
    private List<ImageView> list;
    private LinearLayout pointGroup;
    MulitPointTouchListener mptl;

    private int lastPosition;

    private GalleryViewPager mViewPager;
    private TextView tv_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbigimage_vp);
        images = getIntent().getStringArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);
        tv_count = (TextView) findViewById(R.id.tv_count);

//        initView();
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, images);
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener()
        {
            @Override
            public void onItemChange(int currentPosition)
            {
//				Toast.makeText(GalleryUrlActivity.this, "Current item is " + currentPosition, Toast.LENGTH_SHORT).show();
                tv_count.setText(""+(currentPosition+1)+"/"+images.size());
            }
        });

        mViewPager = (GalleryViewPager)findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(position);
//        mViewPager.setHandler(handler);
        mViewPager.setOnItemClickListener(new GalleryViewPager.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                finish();
            }
        });
    }



    @Override
    public void onClick(View v) {

    }


}
