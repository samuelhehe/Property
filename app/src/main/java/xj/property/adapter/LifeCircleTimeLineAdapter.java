package xj.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Arrays;
import java.util.List;

import xj.property.R;
import xj.property.activity.LifeCircle.ZoneItemActivity;
import xj.property.beans.MyLifeCircleBean;
import xj.property.beans.MyLifeCircleDetialBean;
import xj.property.utils.SmileUtils;
import xj.property.utils.other.Config;

/**
 * Created by che on 2015/6/8.
 */
public class LifeCircleTimeLineAdapter extends BaseAdapter {

    private Context context;
    private DisplayImageOptions options;
    private List<MyLifeCircleBean.LifeCircleSimpleBean> timeLineBean;
    private String emobid;

    public LifeCircleTimeLineAdapter(Context context, List<MyLifeCircleBean.LifeCircleSimpleBean> timeLineBean, String emobid) {
        this.context = context;
        this.timeLineBean = timeLineBean;
        this.emobid = emobid;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.head_portrait_personage)
                .showImageOnFail(R.drawable.head_portrait_personage).showImageOnLoading(R.drawable.head_portrait_personage)
                .build();
    }

    public void addData(List<MyLifeCircleBean.LifeCircleSimpleBean> list) {
        this.timeLineBean.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return timeLineBean.size();
    }

    @Override
    public Object getItem(int position) {
        return timeLineBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_life_circle_time_line, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position != 0 && timeLineBean.get(position).getTimstamp().equals(timeLineBean.get(position - 1).getTimstamp())) {
//            viewHolder.iv_time_line_pic.setBackgroundResource(R.drawable.timeaxisorange_line);
            viewHolder.tv_time_line_time.setVisibility(View.INVISIBLE);
            viewHolder.iv_time_line_pic.setVisibility(View.INVISIBLE);
        } else {
//            viewHolder.iv_time_line_pic.setBackgroundResource(R.drawable.time_axis_timeline);
            viewHolder.tv_time_line_time.setVisibility(View.VISIBLE);
            viewHolder.iv_time_line_pic.setVisibility(View.VISIBLE);
        }

        viewHolder.tv_time_line_time.setText(timeLineBean.get(position).getTimstamp());
        createTimeLineDetial(viewHolder, timeLineBean.get(position));
//        viewHolder.ll_time_line_detial.addView(createTimeLineDetial(timeLineBean.get(position)));

//        viewHolder.ll_time_line_detial.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(context,"emobid="+emobid+"内容"+timeLineBean.get(position).getContent()+"  lifeCircleId="+timeLineBean.get(position).getLifeCircleId(),Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, ZoneItemActivity.class);
//                intent.putExtra(Config.INTENT_PARMAS1, emobid);
//                intent.putExtra(Config.INTENT_PARMAS2, timeLineBean.get(position).getLifeCircleId());
//                context.startActivity(intent);
//            }
//        });


//        for(int i=0 ; i<timeLineBean.get(position).getInfo().size(); i++){
//            TimeLineBeanDetail  timeLineBeanDetail = timeLineBean.get(position).getInfo().get(i);
//            if(i>=viewHolder.ll_time_line_detial.getChildCount()){
//                viewHolder.ll_time_line_detial.addView(createTimeLineDetial(timeLineBeanDetail));
//            }else {
//                LinearLayout linearLayout = (LinearLayout) viewHolder.ll_time_line_detial.getChildAt(i);
//                FrameLayout img_father = (FrameLayout) linearLayout.findViewById(R.id.img_father);
//                ImageView img_one = (ImageView) linearLayout.findViewById(R.id.img_one);
//                ImageView img_two = (ImageView) linearLayout.findViewById(R.id.img_two);
//                ImageView img_three = (ImageView) linearLayout.findViewById(R.id.img_three);
//                ImageView img_four = (ImageView) linearLayout.findViewById(R.id.img_four);
//                ImageView img_only_one = (ImageView) linearLayout.findViewById(R.id.img_only_one);
//                TextView tv_content = (TextView) linearLayout.findViewById(R.id.tv_content);
//                TextView tv_photo_size= (TextView) linearLayout.findViewById(R.id.tv_photo_size);
//                TextView tv_comment_size= (TextView) linearLayout.findViewById(R.id.tv_comment_size);
//                TextView tv_praise_size= (TextView) linearLayout.findViewById(R.id.tv_praise_size);
//                int imgsize =timeLineBeanDetail.getImages().size();
//                if(timeLineBeanDetail.getImages().size() >4){
//                    imgsize = 4;
//                }
//                tv_content.setMaxLines(3);
//
//                switch (imgsize){
//                    case 0:
//                        img_father.setVisibility(View.GONE);
//                        tv_photo_size.setVisibility(View.GONE);
//                        tv_content.setMaxLines(6);
//                        break;
//                    case 1:
//                        tv_photo_size.setVisibility(View.VISIBLE);
//                        img_only_one.setVisibility(View.VISIBLE);
//                        ImageLoader.getInstance().displayImage(timeLineBeanDetail.getImages().get(0),img_only_one,options );
//                        break;
//                    case 2:
//                        img_only_one.setVisibility(View.GONE);
//                        tv_photo_size.setVisibility(View.VISIBLE);
//                        ImageLoader.getInstance().displayImage(timeLineBeanDetail.getImages().get(0),img_one,options );
//                        ImageLoader.getInstance().displayImage(timeLineBeanDetail.getImages().get(1),img_two,options );
//                        img_three.setVisibility(View.GONE);
//                        img_four.setVisibility(View.GONE);
//                        break;
//                    case 3:
//                        img_only_one.setVisibility(View.GONE);
//                        tv_photo_size.setVisibility(View.VISIBLE);
//                        ImageLoader.getInstance().displayImage(timeLineBeanDetail.getImages().get(0),img_one,options );
//                        ImageLoader.getInstance().displayImage(timeLineBeanDetail.getImages().get(1),img_two,options );
//                        ImageLoader.getInstance().displayImage(timeLineBeanDetail.getImages().get(2),img_three,options );
//                        img_three.setVisibility(View.VISIBLE);
//                        img_four.setVisibility(View.GONE);
//                        break;
//                    case 4:
//                        img_only_one.setVisibility(View.GONE);
//                        tv_photo_size.setVisibility(View.VISIBLE);
//                        ImageLoader.getInstance().displayImage(timeLineBeanDetail.getImages().get(0),img_one,options );
//                        ImageLoader.getInstance().displayImage(timeLineBeanDetail.getImages().get(1),img_two,options );
//                        ImageLoader.getInstance().displayImage(timeLineBeanDetail.getImages().get(2),img_three,options );
//                        ImageLoader.getInstance().displayImage(timeLineBeanDetail.getImages().get(3),img_four,options );
//                        img_three.setVisibility(View.VISIBLE);
//                        img_four.setVisibility(View.VISIBLE);
//                        break;
//                }
//
//                tv_content.setText(timeLineBeanDetail.getContent());
//                tv_photo_size.setText(timeLineBeanDetail.getPhoto_size());
//                tv_comment_size.setText(timeLineBeanDetail.getComment_size());
//                tv_praise_size.setText(timeLineBeanDetail.getPraise_size());
//            }
//            final int copyi =i;
//            viewHolder.ll_time_line_detial.getChildAt(i).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context,"内容"+timeLineBean.get(position).getInfo().get(copyi).getContent(),Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//
//        if (viewHolder.ll_time_line_detial.getChildCount() > 0) {//删除多余的
//            for (int i = 0; i < viewHolder.ll_time_line_detial.getChildCount(); i++)
//                viewHolder.ll_time_line_detial.removeViewAt(i);
//        }

        return convertView;
    }

    class ViewHolder {
        ImageView iv_time_line_pic;
        TextView tv_time_line_time;
        LinearLayout ll_time_line_detial;

        //        LinearLayout linearLayout = (LinearLayout) View.inflate(context, R.layout.itme_life_circle_time_line_detial, null);
        FrameLayout img_father;
        ImageView img_one;
        ImageView img_two;
        ImageView img_three;
        ImageView img_four;
        ImageView img_only_one;
        TextView tv_content;
        TextView tv_photo_size;
        TextView tv_comment_size;
        TextView tv_praise_size;


        ViewHolder(View v) {
            img_father = (FrameLayout) v.findViewById(R.id.img_father);
            img_one = (ImageView) v.findViewById(R.id.img_one);
            img_two = (ImageView) v.findViewById(R.id.img_two);
            img_three = (ImageView) v.findViewById(R.id.img_three);
            img_four = (ImageView) v.findViewById(R.id.img_four);
            img_only_one = (ImageView) v.findViewById(R.id.img_only_one);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            tv_photo_size = (TextView) v.findViewById(R.id.tv_photo_size);
            tv_comment_size = (TextView) v.findViewById(R.id.tv_comment_size);
            tv_praise_size = (TextView) v.findViewById(R.id.tv_praise_size);
            iv_time_line_pic = (ImageView) v.findViewById(R.id.iv_time_line_pic);
            tv_time_line_time = (TextView) v.findViewById(R.id.tv_time_line_time);
            ll_time_line_detial = (LinearLayout) v.findViewById(R.id.ll_time_line_detial);
            v.setTag(this);
        }
    }

    private void createTimeLineDetial(ViewHolder viewHolder, MyLifeCircleBean.LifeCircleSimpleBean timeLineBeanDetail) {
//        LinearLayout linearLayout = (LinearLayout) View.inflate(context, R.layout.itme_life_circle_time_line_detial, null);
//        FrameLayout img_father = (FrameLayout) linearLayout.findViewById(R.id.img_father);
//        ImageView img_one = (ImageView) linearLayout.findViewById(R.id.img_one);
//        ImageView img_two = (ImageView) linearLayout.findViewById(R.id.img_two);
//        ImageView img_three = (ImageView) linearLayout.findViewById(R.id.img_three);
//        ImageView img_four = (ImageView) linearLayout.findViewById(R.id.img_four);
//        ImageView img_only_one = (ImageView) linearLayout.findViewById(R.id.img_only_one);
//        TextView tv_content = (TextView) linearLayout.findViewById(R.id.tv_content);
//        TextView tv_photo_size= (TextView) linearLayout.findViewById(R.id.tv_photo_size);
//        TextView tv_comment_size= (TextView) linearLayout.findViewById(R.id.tv_comment_size);
//        TextView tv_praise_size= (TextView) linearLayout.findViewById(R.id.tv_praise_size);

        int imgsize = 0;
        if (TextUtils.isEmpty(timeLineBeanDetail.getPhotoes())) {
            imgsize = 0;
        } else {
            String[] split = timeLineBeanDetail.getPhotoes().split(",");
            List<String> pics = Arrays.asList(split);
            imgsize = pics.size();
            if (pics.size() > 4) {
                imgsize = 4;
            }
            viewHolder.img_father.setVisibility(View.VISIBLE);
            viewHolder.tv_content.setMaxLines(3);
            switch (imgsize) {
                case 0:
                    viewHolder.img_father.setVisibility(View.GONE);
                    viewHolder.tv_photo_size.setVisibility(View.GONE);
                    viewHolder.tv_content.setMaxLines(6);
                    break;
                case 1:
                    viewHolder.tv_photo_size.setVisibility(View.VISIBLE);
                    viewHolder.img_only_one.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(pics.get(0), viewHolder.img_only_one, options);
                    break;
                case 2:
                    viewHolder.img_only_one.setVisibility(View.GONE);
                    viewHolder.tv_photo_size.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(pics.get(0), viewHolder.img_one, options);
                    ImageLoader.getInstance().displayImage(pics.get(1), viewHolder.img_two, options);
                    viewHolder.img_three.setVisibility(View.GONE);
                    viewHolder.img_four.setVisibility(View.GONE);
                    break;
                case 3:
                    viewHolder.img_only_one.setVisibility(View.GONE);
                    viewHolder.tv_photo_size.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(pics.get(0), viewHolder.img_one, options);
                    ImageLoader.getInstance().displayImage(pics.get(1), viewHolder.img_two, options);
                    ImageLoader.getInstance().displayImage(pics.get(2), viewHolder.img_three, options);
                    viewHolder.img_three.setVisibility(View.VISIBLE);
                    viewHolder.img_four.setVisibility(View.GONE);
                    break;
                case 4:
                    viewHolder.img_only_one.setVisibility(View.GONE);
                    viewHolder.tv_photo_size.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(pics.get(0), viewHolder.img_one, options);
                    ImageLoader.getInstance().displayImage(pics.get(1), viewHolder.img_two, options);
                    ImageLoader.getInstance().displayImage(pics.get(2), viewHolder.img_three, options);
                    ImageLoader.getInstance().displayImage(pics.get(3), viewHolder.img_four, options);
                    viewHolder.img_three.setVisibility(View.VISIBLE);
                    viewHolder.img_four.setVisibility(View.VISIBLE);
                    break;
            }

            Spannable spanAll = SmileUtils.getSmiledText(context, timeLineBeanDetail.getContent());
            viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
//        viewHolder.tv_content.setText(timeLineBeanDetail.getContent());
            if (pics != null) {
                viewHolder.tv_photo_size.setText("" + pics.size() + "张");
            } else {
                viewHolder.tv_photo_size.setText("0张");
            }
            viewHolder.tv_comment_size.setText("" + timeLineBeanDetail.getContentSum() + "条");
            viewHolder.tv_praise_size.setText("" + timeLineBeanDetail.getPraiseSum());
        }

    }


}
