package xj.property.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xj.property.R;
import xj.property.activity.user.BlackListLifeCircleActivity;
import xj.property.activity.user.BlackListViewActivity;
import xj.property.beans.BlackUserInfo;

/**
 * Created by n on 2015/4/14.
 */
public class LifeCircleBalckListAdapter extends BaseAdapter {
    Context context;
    ArrayList<BlackUserInfo> list;
    DisplayImageOptions option_1 = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_avatar)
            .showImageOnFail(R.drawable.default_avatar).showImageOnLoading(R.drawable.default_avatar)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(true)
            .build();
    LifeCircleBalckListAdapter(){

    }

    public LifeCircleBalckListAdapter(Context context, ArrayList<BlackUserInfo> list){
        if (list != null)
            this.list = list;
        this.context = context;

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public BlackUserInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BlackUserInfo data=list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_black, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_name.setText(data.getNickname());
//        vh.iv_mine.setImageResource(context.getResources().getIdentifier("", "drawable",
//                "xj.property"));
        ImageLoader.getInstance().displayImage(data.getAvatar(),vh.iv_mine,option_1);
        vh.iv_detele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BlackListLifeCircleActivity)context).refresh(position);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        ImageView iv_mine;
        TextView tv_name;
        ImageView iv_detele;

        ViewHolder(View v) {
            iv_mine=(ImageView)v.findViewById(R.id.iv_mine);
            tv_name=(TextView)v.findViewById(R.id.tv_name);
            iv_detele=(ImageView)v.findViewById(R.id.iv_detele);
            v.setTag(this);
        }

    }


}
