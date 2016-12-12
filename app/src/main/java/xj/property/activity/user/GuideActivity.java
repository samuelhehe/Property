package xj.property.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.Config;

/**
 * Created by Administrator on 2015/4/17.
 */
public class GuideActivity extends HXBaseActivity {
    ViewPager viewPager;
    LinearLayout.LayoutParams paramsselect;
    LinearLayout.LayoutParams paramsnormal;
    int[] picId = new int[]{R.drawable.welcome_first_720x1281, R.drawable.welcome_second_720x1281, R.drawable.welcome_third_720x1281, R.drawable.welcome_four_720x1281};
    ArrayList<View> views = new ArrayList<>();
    LinearLayout ll_indicator;
    ImageView[] mIndicators = new ImageView[4];
    Button btn_start_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);
        ll_indicator = (LinearLayout) findViewById(R.id.ll_indicator);
        btn_start_location = (Button) findViewById(R.id.btn_start_location);
        btn_start_location.setOnClickListener(this);
        paramsselect = new LinearLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.size_guipop_select), getResources().getDimensionPixelOffset(R.dimen.size_guipop_select));
        paramsselect.setMargins(14, 14, 14, 14);
        paramsnormal = new LinearLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.size_guipop_normal), getResources().getDimensionPixelOffset(R.dimen.size_guipop_normal));
        paramsnormal.setMargins(15, 15, 15, 15);
        for (int i = 0; i < 4; i++) {
            if (i < 3) {
                ImageView view = new ImageView(this);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                views.add(view);
            } else {
                View view = View.inflate(this, R.layout.last_welcome, null);
//                view.findViewById(R.id.btn_welcome).setOnClickListener(this);

//                btn_start_location = (Button) view.findViewById(R.id.btn_start_location);
//                btn_start_location.setOnClickListener(this);

                views.add(view);
            }
            mIndicators[i] = (ImageView) ll_indicator.getChildAt(i);

        }
        viewPager = (ViewPager) findViewById(R.id.vp_index);
        viewPager.setAdapter(new PagerAdapter() {


            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }

            // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
            @Override
            public void destroyItem(ViewGroup view, int position, Object object) {
                view.removeView(views.get(position));
            }

            // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
            @Override
            public Object instantiateItem(ViewGroup view, int position) {
                view.addView(views.get(position));
                if (position < 3)
                    ((ImageView) views.get(position)).setImageResource(picId[position]);

                return views.get(position);
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < mIndicators.length; i++) {
                    if (i == position) {
                        mIndicators[i].setImageResource(R.drawable.welcome_indicator_choiced);
                        mIndicators[i].setLayoutParams(paramsselect);
                    } else {
                        mIndicators[i].setImageResource(R.drawable.welcome_indicator_normal);
                        mIndicators[i].setLayoutParams(paramsnormal);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
//        startActivity(new Intent(this, MainActivity.class));
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra(Config.UPDATE_USERINFO, 100);
        startActivity(intent);
        finish();
    }
}
