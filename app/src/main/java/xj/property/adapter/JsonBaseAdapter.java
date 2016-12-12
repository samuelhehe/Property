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

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
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
public class JsonBaseAdapter extends BaseAdapter {
    int layoutId;
    protected List<JSONObject> list;
    protected Context context;
    /**
     * 用来指定显示属性顺序
     */
    String[] filedNames;
    String[] getImgMetHode;
    boolean picisNative = false;//是否使用本地图片

    int[] ivIds;
    DisplayImageOptions displayImageOptions;

    public JsonBaseAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        list = new ArrayList<JSONObject>();
    }

    public JsonBaseAdapter(Context context, int layoutId, List<JSONObject> list) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
    }

    public JsonBaseAdapter(Context context, int layoutId,
                           List<JSONObject> list, String[] filedNames) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
    }

    public JsonBaseAdapter(Context context, int layoutId,
                           List<JSONObject> list, String[] filedNames, String[] getImgMetHode, DisplayImageOptions displayImageOptions) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
        this.getImgMetHode = getImgMetHode;
        this.displayImageOptions = displayImageOptions;
    }

    public JsonBaseAdapter(Context context, int layoutId,
                           List<JSONObject> list, String[] filedNames, String[] getImgMetHode) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
        this.getImgMetHode = getImgMetHode;
    }

    public JsonBaseAdapter(Context context, int layoutId,
                           List<JSONObject> list, String[] filedNames, String[] getImgMetHode, int[] ivIds, DisplayImageOptions displayImageOptions) {
        this(context, layoutId, list, filedNames, getImgMetHode, ivIds);
        this.displayImageOptions = displayImageOptions;

    }

    public JsonBaseAdapter(Context context, int layoutId,
                           List<JSONObject> list, String[] filedNames, String[] getImgMetHode, int[] ivIds) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
        this.getImgMetHode = getImgMetHode;
        this.ivIds = ivIds;
    }

    private String packageName;

    public JsonBaseAdapter(Context context, int layoutId,
                           List<JSONObject> list, String[] filedNames, String[] getImgMetHode, boolean picisNative) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filedNames = filedNames;
        this.getImgMetHode = getImgMetHode;
        this.picisNative = picisNative;
        packageName = context.getPackageName();
    }

    public void changeData(List<JSONObject> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public JSONObject getItem(int index) {
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

    private Object getFiled(String filedName, JSONObject jsonObject) {
        Object obj = jsonObject.optString(filedName);
        if (obj != null) return obj;
        else {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String jsonContent = jsonObject.optString(keys.next()).toString();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (jsonObj != null) {
                    Object objChild = getFiled(filedName, jsonObj);
                    if (objChild != null) return objChild;
                }
            }
            return null;
        }
    }

    @Override
    public View getView(int index, View convertview, ViewGroup arg2) {
        ViewHolder vh = null;
        JSONObject jsonObject = list.get(index);
        if (convertview == null) {
            convertview = View.inflate(context, layoutId, null);
            vh = new ViewHolder((LinearLayout) convertview);
            convertview.setTag(vh);
        } else {
            vh = (ViewHolder) convertview.getTag();
        }
        int n = vh.tvs.size();
        ArrayList<String> texts = new ArrayList<>();
        ArrayList<String> picture = new ArrayList<>();
        if (filedNames != null)
            for (int i = 0; i < filedNames.length; i++) {
                Object o = getFiled(filedNames[i], jsonObject);
                if (o != null)
                    texts.add(o.toString());
            }
        if (getImgMetHode != null)
            for (int i = 0; i < getImgMetHode.length; i++) {
                Object o = getFiled(getImgMetHode[i], jsonObject);
                if (o != null)
                    picture.add(o.toString());
            }
        int m = texts.size();
        int i = 0;
        if (texts.isEmpty()) {// 没有指定顺序
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext() && i < n) {
                String content = jsonObject.optString(iterator.next());
                if (content != null) {
                    vh.tvs.get(i).setText(content);
                    i++;
                }
            }
        } else {// 有指定顺序
            for (; i < n && i < texts.size() && i < filedNames.length; i++) {
                vh.tvs.get(i).setText(texts.get(i));
            }
        }
        i = 0;
        if (picture.isEmpty()) {//有图片，指定图片摆放顺序
            for (; i < vh.ivs.size() && i < picture.size(); i++) {
                String path = picture.get(i).toString();
                if (picisNative) {
                    if (path.contains("."))//去掉本地图片后缀
                        path = path.substring(0, path.lastIndexOf("."));
//                    ImageLoader.getInstance().displayImage(
//                            "drawable://" + context.getResources().getIdentifier(path, "drawable",
//                                    packageName), vh.ivs.get(i));
                    int id = context.getResources().getIdentifier(path, "drawable",
                            packageName);
                    if (id != 0) {
                        vh.ivs.get(i).setImageResource(id);
                    } else {
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


}
