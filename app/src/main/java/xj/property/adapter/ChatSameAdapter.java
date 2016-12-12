package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.ChatSameDataBean;
import xj.property.beans.ChatSameLabelBean;
import xj.property.utils.other.Config;
import xj.property.widget.FilterFlowLayout;

/**
 * 作者：asia on 2015/12/30 15:36
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class ChatSameAdapter extends BaseAdapter {

    private Context mContext;
    public List<ChatSameDataBean> mList;
    private List<String> mTags;
    private DisplayImageOptions options;

    public ChatSameAdapter(Activity context,List<ChatSameDataBean> mList){
        this.mContext = context;
        this.mList = mList;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_picture)
                .showImageForEmptyUri(R.drawable.default_picture)
                .showImageOnFail(R.drawable.default_picture)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    public void LoadMoreRefresh(List<ChatSameDataBean> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null){
            holder = new ViewHolder();
            convertView=View.inflate(mContext,R.layout.row_same,null);
            holder.mAvatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.mName = (TextView) convertView.findViewById(R.id.name);
            holder.mMytags_mgr_system_defaulttags_fflay = (FilterFlowLayout) convertView.findViewById(R.id.mytags_mgr_system_defaulttags_fflay);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final ChatSameDataBean lab = mList.get(position);
        ImageLoader.getInstance().displayImage(lab.getAvatar(), holder.mAvatar, options);
        holder.mName.setText((lab.getNickname() != null ? lab.getNickname():"邻居")+"");
        showTag(holder.mMytags_mgr_system_defaulttags_fflay,lab.getLabels());
        List<String> userTags = new ArrayList<>();
//        initSystemDefaultTags(holder.mMytags_mgr_system_defaulttags_fflay,mTags,userTags);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mList.get(position).getEmobId();
                Log.i("onion", "username" + username);
                if (username.equals(XjApplication.getInstance().getUserName()))
                    Toast.makeText(mContext, "不能和自己聊天", Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
//                    Intent intent = new Intent();
//                    XJContactHelper.saveContact(username, mChatSameAdapter.mList.get(position).getNickname(), mChatSameAdapter.mList.get(position).getAvatar(), "-1");
//                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
//                    intent.putExtra("userId", username);
//                    intent.putExtra(Config.EXPKey_avatar, mChatSameAdapter.mList.get(position).getAvatar());
//                    intent.putExtra(Config.EXPKey_nickname, mChatSameAdapter.mList.get(position).getNickname());
//                    intent.setClass(getActivity(), ChatActivity.class);
//                    startActivity(intent);
                    Intent userGroupInfoActivity = new Intent(mContext,UserGroupInfoActivity.class);
                    userGroupInfoActivity.putExtra(Config.INTENT_PARMAS2,username);
                    mContext.startActivity(userGroupInfoActivity);
                }
            }
        });
        return convertView;
    }

    private void showTag(FilterFlowLayout filterFlowLayout ,List<ChatSameLabelBean> labels){
        filterFlowLayout.removeAllViews();
        for (int i = 0 ; i < labels.size() ; i++){
            View common_tags_item_fora2b=null;
            if(labels.get(i).getMatch()!=null&&labels.get(i).getMatch().equals("yes")){
                common_tags_item_fora2b = View.inflate(mContext,R.layout.common_tags_same_item, null);
                TextView common_tags_name_tv = (TextView) common_tags_item_fora2b.findViewById(R.id.common_tags_name_tv);
                common_tags_name_tv.setText(labels.get(i).getLabelContent());
            }else {
                common_tags_item_fora2b = View.inflate(mContext,R.layout.common_tags_other_item, null);
                TextView common_tags_name_tv = (TextView) common_tags_item_fora2b.findViewById(R.id.common_tags_name_tv);
                common_tags_name_tv.setText(labels.get(i).getLabelContent());
            }
            filterFlowLayout.addView(common_tags_item_fora2b);
        }
    }

    public void clearDate() {
        mList.clear();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private ImageView mAvatar;
        private TextView mName;
        private FilterFlowLayout mMytags_mgr_system_defaulttags_fflay;
    }
}
