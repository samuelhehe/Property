package xj.property.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.beans.EvaBean;
import xj.property.utils.image.utils.StrUtils;

/**
 * Created by Administrator on 2015/5/5.
 */
public class EvaAdapter extends BaseAdapter {
    List<EvaBean> evaBeanList;
Context context;

    public EvaAdapter(List<EvaBean> evaBeanList, Context context) {
        this.evaBeanList = evaBeanList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
      EvaBean bean=  evaBeanList.get(position);
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_eva,null);
            holder=new ViewHolder(convertView);
        }else {
holder=(ViewHolder)convertView.getTag();
        }
      ImageLoader.getInstance().displayImage(bean.getAvatar(), holder.ivLogo);
        holder.tvContent.setText(bean.getContent());
        holder.tvNike.setText(bean.getNickname());
        holder.tvTime.setText(StrUtils.getTime42Millions(bean.getCreateTime()*1000L));
        switch (Integer.parseInt(bean.getScore())){
            case 0:holder.ivScoer.setImageResource(R.drawable.star0);break;
            case 1:holder.ivScoer.setImageResource(R.drawable.star1);break;
            case 2:holder.ivScoer.setImageResource(R.drawable.star2);break;
            case 3:holder.ivScoer.setImageResource(R.drawable.star3);break;
            case 4:holder.ivScoer.setImageResource(R.drawable.star4);break;
            case 5:holder.ivScoer.setImageResource(R.drawable.star5);break;
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return evaBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return evaBeanList.get(position);
    }

    class ViewHolder{
TextView tvNike;
TextView tvTime;
TextView tvContent;
        ImageView ivLogo;
        ImageView ivScoer;

        ViewHolder(View v) {
            this.tvNike =(TextView) v.findViewById(R.id.tv_nick);
            this.tvTime = (TextView) v.findViewById(R.id.tv_time);
            this.tvContent =(TextView)v.findViewById(R.id.tv_content);
            this.ivLogo = (ImageView)v.findViewById(R.id.iv_logo);
            this.ivScoer = (ImageView)v.findViewById(R.id.iv_scoer);
        v.setTag(this);
        }
    }
}
