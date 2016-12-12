package xj.property.widget.wheelview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.contactphone.FastShopIndexActivity;
import xj.property.adapter.XJBaseAdapter;
import xj.property.beans.FastShopDetailListBean;
import xj.property.utils.other.FastShopCarDBUtil;
import xj.property.utils.other.PreferencesUtil;

@SuppressLint("ViewConstructor")
public class MyPopItems extends PopupWindow {

    private ImageView tvCancel;
    private ListView listView;
    List<FastShopDetailListBean.PagerItemBean> list;
    private View mMenuView;

    public MyPopItems(final Activity context, final List<FastShopDetailListBean.PagerItemBean> list) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.goodsitem_pop, null);
        tvCancel = (ImageView) mMenuView.findViewById(R.id.pop_no);
        // 取消按钮
        tvCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        this.list = list;

        listView = (ListView) mMenuView.findViewById(R.id.lv_itemgood);
//         for(int i=0;i<list.size();i++){
//             if(!list.get(i).getCurrentPrice().contains("￥"))
//             list.get(i).setCurrentPrice("￥" + list.get(i).getCurrentPrice());
//         }
        ArrayList<String> appends = new ArrayList<>();
        appends.add("");
        appends.add("￥");
        listView.setAdapter(new XJBaseAdapter(context, R.layout.item_fastshop_choice, list, new String[]{"attrName", "currentPrice"}, appends));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!FastShopCarDBUtil.addCarAble(list.get(position).getServiceId())) {
                    Toast.makeText(context, R.string.fast_shop_add_error, Toast.LENGTH_SHORT).show();
                    return;
                }
//                FastShopDetailListBean.PagerItemBean pagerItemBean=new FastShopDetailListBean.PagerItemBean();
//                pagerItemBean.setCurrentPrice(  list.get(position).getCurrentPrice().substring(1));
//                pagerItemBean.setBrandId(  list.get(position).getBrandId());
//                pagerItemBean.setAttrName(  list.get(position).getAttrName());
//                pagerItemBean.setServiceId(  list.get(position).getServiceId());
//                pagerItemBean.setShopEmobId(list.get(position).getShopEmobId());
//                pagerItemBean.setShopId(list.get(position).getShopId());
//                pagerItemBean.setCatId(  list.get(position).getCatId());
//                pagerItemBean.setShopName(  list.get(position).getShopName());
//                pagerItemBean.setAttrId(  list.get(position).getAttrId());
//                pagerItemBean.setPurchase(  list.get(position).getPurchase());
//                pagerItemBean.setServiceImg(  list.get(position).getServiceImg());
//                pagerItemBean.setServiceName(list.get(position).getServiceName());
//                pagerItemBean.setCreateTime(list.get(position).getCreateTime());
//                pagerItemBean.setOriginPrice(list.get(position).getOriginPrice());


                saveItemToDB(list.get(position));
                ((FastShopIndexActivity) context).refresh();
                Toast.makeText(context, "已加入购物车", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_main).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }


    private void saveItemToDB(FastShopDetailListBean.PagerItemBean itemBean) {

        //第一条数据直接插入
        String emobid = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
        if (FastShopCarDBUtil.isDBEmpty(emobid)) {
            FastShopCarDBUtil.insert(itemBean, emobid);
        } else {
            if (itemBean.getShopItemSkuId() != 0) {
                if (FastShopCarDBUtil.isExistByserviceIdAndSkuId(itemBean.getServiceId(), itemBean.getShopItemSkuId(), emobid).size() > 0) {
                    FastShopCarDBUtil.updateGoodCountByServiceIdAndSkuId(itemBean.getServiceId(), itemBean.getShopItemSkuId(), emobid);
                } else {
                    FastShopCarDBUtil.insert(itemBean, emobid);
                }
            } else {
                if (FastShopCarDBUtil.isExistByserviceId(itemBean.getServiceId(), emobid).size() > 0) {
                    FastShopCarDBUtil.updateGoodCountByServiceId(itemBean.getServiceId(), emobid);
                } else {
                    FastShopCarDBUtil.insert(itemBean, emobid);
                }
            }

        }

    }
}