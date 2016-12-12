package xj.property.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.beans.XJ;
import xj.property.utils.other.Config;

/**
 * 显示列表的适配器
 *
 * @author onion
 */
@SuppressWarnings("NullArgumentToVariableArgMethod")
public class XJBaseAdapter extends BaseAdapter {
    int layoutId;
    protected List<? extends XJ> list;
    protected Context context;
    protected SingleChildCallBack singleChildCallBack;
    /**
     * 用来指定显示属性顺序
     */
    String[] filedNames;
    String[] getImgMetHode;
    boolean picisNative = false;//是否使用本地图片

    int[] ivIds;
    DisplayImageOptions displayImageOptions;

    public XJBaseAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        list = new ArrayList<XJ>();
    }

    public XJBaseAdapter(Context context, int layoutId, List<? extends XJ> list) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
    }

    public XJBaseAdapter(Context context, int layoutId,
                         List<? extends XJ> list, String[] filedNames) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
    }

    public XJBaseAdapter(Context context, int layoutId,
                         List<? extends XJ> list, String[] filedNames,ArrayList appends) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
        this.appends=appends;
    }
    public SingleChildCallBack getSingleChildCallBack() {
        return singleChildCallBack;
    }

    public void setSingleChildCallBack(SingleChildCallBack singleChildCallBack) {
        this.singleChildCallBack = singleChildCallBack;
    }

    public XJBaseAdapter(Context context, int layoutId,
                         List<? extends XJ> list, String[] filedNames, String[] getImgMetHode, DisplayImageOptions displayImageOptions) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
        this.getImgMetHode = getImgMetHode;
        this.displayImageOptions = displayImageOptions;
    }

    public XJBaseAdapter(Context context, int layoutId,
                         List<? extends XJ> list, String[] filedNames, String[] getImgMetHode) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
        this.getImgMetHode = getImgMetHode;
    }
    ArrayList appends;
    public XJBaseAdapter(Context context, int layoutId,
                         List<? extends XJ> list, String[] filedNames,ArrayList appends, String[] getImgMetHode) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
        this.getImgMetHode = getImgMetHode;
        this.appends=appends;
    }
    public XJBaseAdapter(Context context, int layoutId,
                         List<? extends XJ> list, String[] filedNames, String[] getImgMetHode, int[] ivIds,DisplayImageOptions displayImageOptions) {
        this(context,layoutId,list,filedNames,getImgMetHode,ivIds);
        this.displayImageOptions = displayImageOptions;

    }
    public XJBaseAdapter(Context context, int layoutId,
                         List<? extends XJ> list, String[] filedNames, String[] getImgMetHode, int[] ivIds) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
        this.getImgMetHode = getImgMetHode;
        this.ivIds = ivIds;
    }
    private String packageName;
    public XJBaseAdapter(Context context, int layoutId,
                         List<? extends XJ> list, String[] filedNames, String[] getImgMetHode, boolean picisNative) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
        this.getImgMetHode = getImgMetHode;
        this.picisNative = picisNative;
        packageName=context.getPackageName();
    }

    public void changeData(List<? extends XJ> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public XJ getItem(int index) {
        return list.get(index);
    }

    @Override
    public long getItemId(int index) {
//		String str = list.get(index).getId();
//		if (str == null || TextUtils.isEmpty(str))
//			return 0;
//		return Long.parseLong(str);
        return index;
    }

    @Override
    public View getView(int index, View convertview, ViewGroup arg2) {
        ViewHolder vh = null;
        XJ yb = list.get(index);

        Class<? extends XJ> cc = yb.getClass();
        if (convertview == null) {

            convertview = View.inflate(context, layoutId, null);

            if (singleChildCallBack != null && !singleChildCallBack.initindexs.isEmpty()) {//添加监听事件
                for(int i=0;i<singleChildCallBack.initindexs.size();i++)
                singleChildCallBack.initSingleChildView(((LinearLayout) convertview).getChildAt(i),index,i);
            }
            vh = new ViewHolder((LinearLayout) convertview);
            convertview.setTag(vh);
        } else {
            vh = (ViewHolder) convertview.getTag();
        }
        int n = vh.tvs.size();
        int m = cc.getFields().length;

        Object o = "";
        if (filedNames == null) {// 没有指定顺序
            for (int i = 0; i < n && i < m; i++) {
                Field f = cc.getFields()[i];
                try {
                    o = f.get(yb);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


                vh.tvs.get(i).setText(o.toString());
            }
        } else {// 有指定顺序
            for (int i = 0; i < n && i < m && i < filedNames.length; i++) {
                try {
                    Field f = cc.getField(filedNames[i]);
                    o = f.get(yb);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(appends!=null)      vh.tvs.get(i).setText(appends.get(i)+o.toString());
          else      vh.tvs.get(i).setText(o.toString());
            }
        }
        if (getImgMetHode != null) {//有图片，指定图片摆放顺序
            for (int i = 0; i < vh.ivs.size() && i < getImgMetHode.length; i++) {
                try {
//                    Log.d("getImgMetHode[i]  ---", getImgMetHode[i] );
                    Class<?>[]  tempclazz = null;  //2015/11/26
                    Method method = cc.getMethod(getImgMetHode[i], tempclazz);// 图片路径通过方法反射获得
                    Object[] objs = null ; /// 2015/11/26

                    o = method.invoke(yb, objs);
//                    Log.d("getImgMetHode  ---","o " + o+"method "+ method.getName() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String path;
                if(o!=null){
                    path = o.toString();
                }else{
                    path = "";
                }
                if (picisNative) {
                    if (o.toString().contains("."))//去掉本地图片后缀
                        path = path.substring(0, o.toString().lastIndexOf("."));
//                    ImageLoader.getInstance().displayImage(
//                            "drawable://" + context.getResources().getIdentifier(path, "drawable",
//                                    packageName), vh.ivs.get(i));
                    int id=   context.getResources().getIdentifier(path, "drawable",         packageName);
                    if(id!=0){
                        vh.ivs.get(i).setImageResource(id);
                    }else {
                        vh.ivs.get(i).setImageResource(R.drawable.ic_launcher);
                    }
                } else {
                    if (displayImageOptions == null)
                        ImageLoader.getInstance().displayImage(Config.IMG_IRL + path, vh.ivs.get(i));
                    else ImageLoader.getInstance().displayImage(
                            Config.IMG_IRL + path, vh.ivs.get(i), displayImageOptions);
                }

            }
        }

        //执行差异化更改
        if (singleChildCallBack != null && !singleChildCallBack.diffindexs.isEmpty()) {
            for(int i=0;i<singleChildCallBack.diffindexs.size();i++)
                singleChildCallBack.differenceChildView(((LinearLayout) convertview).getChildAt(i),index,i);
        }
        return convertview;
    }

    class ViewHolder {
        ArrayList<TextView> tvs;
        ArrayList<ImageView> ivs;

        ViewHolder(LinearLayout l) {
            tvs = new ArrayList<TextView>();
            ivs = new ArrayList<ImageView>();
            if (ivIds != null) {
                for (int i = 0; i < ivIds.length; i++)
                    ivs.add((ImageView) l.findViewById(ivIds[i]));
            }
            addTextViews(l);
        }

        private void addTextViews(LinearLayout l) {
            for (int i = 0; i < l.getChildCount(); i++) {
                View v = l.getChildAt(i);
                if (v instanceof TextView && (TextUtils.isEmpty(((TextView) v).getText().toString()) || "TextView".equals(((TextView) v).getText().toString()))) {
                    tvs.add((TextView) v);
                }
                if (v instanceof ImageView)
                    ivs.add((ImageView) v);
                if (v instanceof LinearLayout) {
                    addTextViews((LinearLayout) v);
                }
            }
        }
    }

    public static interface SingleChildCallBack {
        public ArrayList<Integer> initindexs = new ArrayList<>();//需要添加监听事件的一级子view的角标
        public ArrayList<Integer> diffindexs = new ArrayList<>();//需要更改背景、显示值等等二级子view的角标

        public void initSingleChildView(View v, int position, int childindex);

        public void differenceChildView(View v, int position, int childindex);
    }

}
