package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.fitmentfinish.FitMentFinishDetailsActivity;
import xj.property.activity.fitmentfinish.FitMentFinishExperienceActivity;
import xj.property.activity.fitmentfinish.FitMentFinishSchemeIndexActivity;
import xj.property.beans.FitmentFinishCompanyData;

/**
 * Created by asia on 15/11/20.
 */
public class FitmentFinishAdapter extends BaseAdapter {

    private Context mContext;

    public List<FitmentFinishCompanyData> mlist;

    private DisplayImageOptions options;

    private View headerView;

    private View bottomView;



    public FitmentFinishAdapter(Context mContext, List<FitmentFinishCompanyData> mlist){
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

    public void ChangeRefresh(List<FitmentFinishCompanyData> list){
        mlist.clear();
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    public void LoadMoreRefresh(List<FitmentFinishCompanyData> list){
        mlist.addAll(list);
        notifyDataSetChanged();
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
        ViewHolder holder = null;
        if (convertView == null && position!=0&&position!=mlist.size()-1) {
            holder = new ViewHolder();
            convertView = ((Activity) (mContext)).getLayoutInflater()
                    .inflate(R.layout.item_fitment_index, parent, false);
            holder.mIv_business = (ImageView) convertView.findViewById(R.id.iv_business);
            convertView.setTag(holder);
        } else if(convertView == null && position == 0){
            headerView = ((Activity) (mContext)).getLayoutInflater()
                    .inflate(R.layout.activity_fitment_index_header, parent, false);
        }else if(convertView == null && position == mlist.size()-1){
            bottomView = ((Activity) (mContext)).getLayoutInflater()
                    .inflate(R.layout.activity_fitment_index_bottom, parent, false);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final FitmentFinishCompanyData fitmentFinish = mlist.get(position);

        if(position!=0 && position!=mlist.size()-1){
            ImageLoader.getInstance().displayImage(fitmentFinish.getLogo(), holder.mIv_business, options);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FitMentFinishDetailsActivity.class);
                    intent.putExtra("decorationId",fitmentFinish.getDecorationId());
                    mContext.startActivity(intent);
                }
            });
        }else if(position == 0){
            LinearLayout mLl_schmeme = (LinearLayout) headerView.findViewById(R.id.ll_schmeme);
            ImageView mIv_experience=(ImageView)headerView.findViewById(R.id.iv_experience);

            mLl_schmeme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,FitMentFinishExperienceActivity.class));
                }
            });

            mIv_experience.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,  FitMentFinishSchemeIndexActivity.class));
                }
            });

        }

        if(position!=0 && position!=mlist.size()-1){
            return convertView;
        }else if(position == 0){
            return headerView;
        }else if(position == mlist.size()-1){
            return bottomView;
        }else {
            return convertView;
        }
    }

    private static final class ViewHolder {
        private ImageView mIv_business;
    }

}
