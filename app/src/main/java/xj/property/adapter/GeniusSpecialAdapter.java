package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xj.property.R;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.GeniusBean;
import xj.property.utils.other.Config;
import xj.property.widget.CircleImageView;

/**
 * Created by asia on 15/11/20.
 */
public class GeniusSpecialAdapter extends BaseAdapter {

    private Context mContext;

    private List<GeniusBean> mlist;

    private Map<String,View> mViewMap = new HashMap<String,View>();

    private DisplayImageOptions options;

    public GeniusSpecialAdapter(Context mContext, List<GeniusBean> mlist){
        this.mContext = mContext;
        this.mlist = mlist;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_picture)
                .showImageForEmptyUri(R.drawable.default_picture)
                .showImageOnFail(R.drawable.default_picture)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mViewMap.get(position+"")==null) {
            holder = new ViewHolder();
            convertView = ((Activity) (mContext)).getLayoutInflater()
                    .inflate(R.layout.item_genius_special, parent, false);
            holder.mTv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.mAvatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            holder.mTv_intro = (TextView) convertView.findViewById(R.id.tv_intro);
            holder.mTv_more = (TextView) convertView.findViewById(R.id.tv_more);
            holder.mLl_genius_list = (LinearLayout)convertView.findViewById(R.id.ll_genius_list);
            convertView.setTag(holder);
        } else {
            convertView=mViewMap.get(position+"");
            holder = (ViewHolder) convertView.getTag();
        }
        final GeniusBean runForBean = mlist.get(position);
        ImageLoader.getInstance().displayImage(runForBean.getAvatar(), holder.mAvatar, options);
        holder.mTv_nickname.setText(runForBean.getNickname());
        holder.mTv_intro.setText("");
        holder.mTv_intro.setText(runForBean.getFamousIntroduce());

        holder.mTv_intro.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (holder.mTv_intro.getLineCount() <= 3) {
                    holder.mTv_more.setVisibility(View.INVISIBLE);
                    runForBean.setIsShowMore(false);
                } else {
                    holder.mTv_more.setVisibility(View.VISIBLE);
                }
            }
        });

        if (!runForBean.isMore()) {
            holder.mTv_more.setVisibility(View.VISIBLE);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        if (runForBean.isShowMore()) {
            holder.mTv_more.setVisibility(View.VISIBLE);
        } else {
            holder.mTv_more.setVisibility(View.INVISIBLE);
        }
        holder.mLl_genius_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.mTv_intro.setLines();
                if ("更多".equals(holder.mTv_more.getText().toString())) {
                    if (holder.mTv_intro.getLayout().getLineCount() > 3) {
                        holder.mTv_intro.setLines(holder.mTv_intro.getLayout().getLineCount());
                        Drawable drawable = holder.mTv_more.getResources().getDrawable(R.drawable.trilateral1);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                        holder.mTv_more.setCompoundDrawables(null, null, drawable, null);//画在右边
                        holder.mTv_more.setText("收起");
                    } else {
                        holder.mTv_more.setVisibility(View.INVISIBLE);
                        runForBean.setIsShowMore(false);
                    }
                } else {
                    holder.mTv_intro.setLines(3);
                    Drawable drawable = holder.mTv_more.getResources().getDrawable(R.drawable.trilateral);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    holder.mTv_more.setCompoundDrawables(null, null, drawable, null);//画在右边
                    holder.mTv_more.setText("更多");
                }

                runForBean.setMore(true);
            }
        });
        holder.mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userGroupInfoActivity = new Intent(mContext,UserGroupInfoActivity.class);
                userGroupInfoActivity.putExtra(Config.INTENT_PARMAS2, runForBean.getEmobId());
                mContext.startActivity(userGroupInfoActivity);
            }
        });

        mViewMap.put(position+"",convertView);
        return convertView;
    }

    public void ChangeRefresh(List<GeniusBean> list){
        mlist.clear();
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    public void LoadMoreRefresh(List<GeniusBean> list){
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    private static final class ViewHolder {
        private CircleImageView mAvatar;
        private TextView mTv_nickname;
        private TextView mTv_intro;
        private TextView mTv_more;
        private LinearLayout mLl_genius_list;
    }

}
