package xj.property.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;

/**
 * Created by Administrator on 2015/3/25.
 */
public class ShowBigImageActivity extends HXBaseActivity {
    private ImageView iv_big_avatar;
    private RelativeLayout rl;

    private String avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbig_image);
        avatar=getIntent().getStringExtra("avatar");
        initView();
    }

    private void initView() {
        iv_big_avatar=(ImageView)findViewById(R.id.iv_big_avatar);
        rl=(RelativeLayout)findViewById(R.id.rl);
        rl.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(avatar, iv_big_avatar, new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageOnFail(R.drawable.head_portrait_personage).showImageForEmptyUri(R.drawable.head_portrait_personage).build());
        iv_big_avatar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        finish();
    }






}
