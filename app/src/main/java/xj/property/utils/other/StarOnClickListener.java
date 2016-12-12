package xj.property.utils.other;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import xj.property.R;

/**
 * Created by Administrator on 2015/4/16.
 */
public class StarOnClickListener implements View.OnClickListener {
    public int starnum=5;
    private LinearLayout ll;
    private ArrayList<ImageView> ivs = new ArrayList<ImageView>();

    private StarOnClickListener(LinearLayout ll) {
        this.ll = ll;
        for (int i = 0; i < ll.getChildCount(); i++) {
            View v = ll.getChildAt(i);
            if (v instanceof ImageView) {
                ivs.add((ImageView) v);
            }
        }
        changeBigStar(5);
    }

    public void setStarOnClick() {
        for (int i = 0; i < ll.getChildCount(); i++) {
            ll.getChildAt(i).setOnClickListener(this);
        }

    }

    public static StarOnClickListener getInstance(LinearLayout ll) {
        return new StarOnClickListener(ll);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_star0:
                changeBigStar(0);
                break;
            case R.id.iv_star1:
                changeBigStar(1);
                break;
            case R.id.iv_star2:
                changeBigStar(2);
                break;
            case R.id.iv_star3:
                changeBigStar(3);
                break;
            case R.id.iv_star4:
                changeBigStar(4);
                break;
            case R.id.iv_star5:
                changeBigStar(5);
                break;
        }
    }

    public void changeColor(int position) {
        starnum = position;
        for (int i = 0; i < ivs.size(); i++) {
            if (i < position) ivs.get(i).setImageResource(R.drawable.snacks_star_light);
            else ivs.get(i).setImageResource(R.drawable.snacks_star_dark);
        }
    }

    public void changeBigStar(int position) {
        starnum = position;
        for (int i = 0; i < ivs.size(); i++) {
            if (i < position) ivs.get(i).setImageResource(R.drawable.bt_star_pressed);
            else ivs.get(i).setImageResource(R.drawable.bt_star_normal);
        }
    }
}
