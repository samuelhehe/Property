package xj.property.utils.other;

import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.ArrayList;
import java.util.List;

import xj.property.XjApplication;
import xj.property.beans.FastShopDetailListBean;
import xj.property.cache.FastShopCatModel;

/**
 * Created by n on 2015/4/3.
 */
public class FastShopCarDBUtil {

    public static void initData(Context context) {
        SharedPreferences sp = context.getSharedPreferences("xj", context.MODE_PRIVATE);
        if (sp.getBoolean("FastShopCatModel", false) == false) {
            FastShopCatModel modeltemp = new FastShopCatModel("11", "1", 1);
            modeltemp.save();
            try {
                new Delete().from(FastShopCatModel.class).execute();
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("FastShopCatModel", true).commit();

            } catch (Exception e) {
            }
        }

    }

    public static boolean isDBEmpty(String emobId) {
        if (getAll(emobId) != null && getAll(emobId).size() != 0) {
            return false;
        }
        return true;
    }

    public static boolean addCarAble(int serviceId) {
        String emobid = PreferencesUtil.getLogin(XjApplication.getInstance())  ?
                PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId() :
                PreferencesUtil.getTourist(XjApplication.getInstance());

        List<FastShopCatModel> fastShopCatModels = FastShopCarDBUtil.isExistByserviceId(serviceId, emobid);

        Log.i("onion", serviceId + "/" + fastShopCatModels + emobid);

        if (fastShopCatModels.isEmpty()) return true;
        else if (fastShopCatModels.get(0).getBuy_num() != 0 && fastShopCatModels.get(0).getCount() + 1 > fastShopCatModels.get(0).getBuy_num()) {
            return false;
        } else return true;
    }

    public static void insert(FastShopDetailListBean.PagerItemBean bean, String emobId) {
        FastShopCatModel model = new FastShopCatModel(emobId, "1", 1);
        model.emobId = emobId;
        //// v3 2016/03/17
        model.shopItemSkuId=bean.getShopItemSkuId();
        model.brandId = bean.getBrandId();
        model.catId = bean.getCatId();
        model.shopId = bean.getShopId();
        model.shopName = bean.getShopName();
        model.serviceId = bean.getServiceId();
        model.serviceName = bean.getServiceName() ;
        model.shopEmobId = bean.getShopEmobId();
        model.buy_num = Integer.MAX_VALUE;/// 没有限购
//        model.setBuy_num(bean.getPurchase());/// 限购
//        model.setBuy_num(1);
//        Log.i("onion", "Purchase" + bean.getPurchase());
        try {
            model.price = Double.parseDouble(bean.getCurrentPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.serviceImg = bean.getServiceImg();
        model.save();
    }



//    public static void insert(FastShopDetailListBean.PagerItemBean bean, String emobId) {
//        FastShopCatModel model = new FastShopCatModel(emobId, "1", 1);
//        model.emobId = emobId;
//        model.brandId = bean.getBrandId();
//        model.catId = bean.getCatId();
//        model.shopId = bean.getShopId();
//        model.shopName = bean.getShopName();
//        model.serviceId = bean.getServiceId();
//        model.serviceName = bean.getServiceName() + (bean.getAttrName() == null ? "" : bean.getAttrName());
//        model.shopEmobId = bean.getShopEmobId();
//        model.setBuy_num(bean.getPurchase());
////        model.setBuy_num(1);
//        Log.i("onion", "Purchase" + bean.getPurchase());
//        try {
//            model.price = Double.parseDouble(bean.getCurrentPrice());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        model.serviceImg = bean.getServiceImg();
//        model.status = bean.status;
//        model.createTime = bean.getCreateTime();
//        model.save();
//    }

    public static List<FastShopCatModel> getAll(int userId) {
        //  return new Select().from(FastShopCatModel.class).where("state=1").execute();
        return new Select().from(FastShopCatModel.class).where("userId = ?", userId).execute();
    }

    public static List<FastShopCatModel> getAll(String emboid) {
        //  return new Select().from(FastShopCatModel.class).where("state=1").execute();
        return new Select().from(FastShopCatModel.class).where("emobid = ?", emboid).execute();
    }

    public static List<FastShopCatModel> getAllAndState(String emobId) {
        return new Select().from(FastShopCatModel.class).where("state=1 and emobId = ?", emobId).execute();
    }

    public static List<FastShopCatModel> isExistByserviceId(int serviceId, String emobId) {
        return new Select().from(FastShopCatModel.class).where("serviceId=? and emobId=?", serviceId, emobId).execute();
    }
    public static List<FastShopCatModel> isExistByserviceIdAndSkuId(int serviceId, int skuId,String emobId) {
        return new Select().from(FastShopCatModel.class).where("serviceId=? and emobId=? and shopItemSkuId=?", serviceId, emobId, skuId).execute();
    }

    public static void updateGoodCountByServiceId(int serviceId, String emobId) {
        new Update(FastShopCatModel.class).set("count=count+1").where("serviceId=? and emobId=?", serviceId, emobId).execute();
    }

    public static void updateGoodCountByServiceIdAndSkuId(int serviceId,int skuId, String emobId) {
        new Update(FastShopCatModel.class).set("count=count+1").where("serviceId=? and emobId=? and shopItemSkuId=?", serviceId, emobId, skuId).execute();
    }

    public static void updateGoodCountByServiceId(int serviceId, String emobId, int count) {
        new Update(FastShopCatModel.class).set("count=count+" + count).where("serviceId=? and emobId=?", serviceId, emobId).execute();
    }

    public static void subGoodCountByServiceId(int serviceId, String emobId) {
        new Update(FastShopCatModel.class).set("count=count-1").where("serviceId=? and emobId=?", serviceId, emobId).execute();
    }

    public static void subGoodCountByServiceIdAndSkuId(int serviceId, int skuId, String emobId) {
        new Update(FastShopCatModel.class).set("count=count-1").where("serviceId=? and emobId=? and shopItemSkuId=? ", serviceId, emobId, skuId).execute();
    }

    public static void updateGoodCountByServiceId(int serviceId, int count, String emobId) {
        new Update(FastShopCatModel.class).set("count = ?", count).where("serviceId=? and emobId=?", serviceId, emobId).execute();
    }

    public static List<FastShopCatModel> isExistByshopId(int shopId, String emobId) {
        return new Select().from(FastShopCatModel.class).where("shopId=? and emobId=?", shopId, emobId).execute();
    }

    /**
     * 计算所有商品的总价格
     * @param lists
     * @return
     */
    public static double getAllGoodsPrice(List<FastShopCatModel> lists) {
        double sum = 0;
        if (lists != null) {
            for (FastShopCatModel list : lists) {
                double count = list.count;
                //sum = sum + list.price * list.count;
                sum = Arith.add(sum, Arith.mul(list.price, count));
            }
        }
        return sum;
    }
    /**
     *
     * 商品总数
     * @param lists
     * @return
     */
    public static int getGoodsCount(List<FastShopCatModel> lists) {
        int count = 0;
        if (lists != null) {
            for (FastShopCatModel list : lists) {
                count = count + list.count;
            }
        }
        return count;
    }

    public static int getSingleGoodCount(int serviceId, String emobId) {
        FastShopCatModel model = new Select().from(FastShopCatModel.class).where("serviceId=? and emobId=?", serviceId, emobId).executeSingle();
        return model.count;
    }

    public static double getSumPriceByShopId(int shopId, String emobId) {
        double sum = 0;
        List<FastShopCatModel> model = new Select().from(FastShopCatModel.class).where("state=1 and shopId=? and emobId = ?", shopId, emobId).execute();
        for (FastShopCatModel temp : model) {
            //sum = sum + temp.count * temp.price;
            sum = Arith.add(sum, Arith.mul(temp.price, temp.count));
        }
        return sum;
    }


    public static int getSumCountByShopId(String shopEmobId, String emobId) {
        int sum = 0;
        List<FastShopCatModel> model = new Select().from(FastShopCatModel.class).where("state=1 and shopEmobId = ? and emobId = ?", shopEmobId, emobId).execute();
        for (FastShopCatModel temp : model) {
            //sum = sum + temp.count * temp.price;
            sum += temp.count;
        }
        return sum;
    }

    public static int getStateByServiceId() {
        return 0;
    }

    //根据商家id获取商品list
    public static ArrayList<FastShopDetailListBean.PagerItemBean> findByShopId2(int shopId, String emobId) {
        ArrayList<FastShopDetailListBean.PagerItemBean> templist = new ArrayList<FastShopDetailListBean.PagerItemBean>();
        List<FastShopCatModel> model = new Select().from(FastShopCatModel.class).where("shopId=? and emobId=?", shopId, emobId).execute();
        for (FastShopCatModel temp : model) {
            FastShopDetailListBean.PagerItemBean bean = new FastShopDetailListBean.PagerItemBean();
            bean.setBrandId(temp.brandId);
            bean.setCatId(temp.catId);
            bean.setShopId(temp.shopId);
            bean.setShopName(temp.shopName);
            bean.setServiceId(temp.serviceId);
            bean.setServiceName(temp.serviceName);
            bean.setCurrentPrice(temp.price + "");
            bean.setServiceImg(temp.serviceImg);
            bean.setShopEmobId(temp.getShopEmobId());
            templist.add(bean);
        }
        return templist;
    }

    public static ArrayList<FastShopCatModel> findByShopId(int shopId, String emobId) {
        return (ArrayList) new Select().from(FastShopCatModel.class).where("shopId=?  and emobId = ?", shopId, emobId).execute();
    }


    public static void updateGoodStateByServiceId(int serviceId, boolean flag, String emobId) {
        if (flag == true) {
            new Update(FastShopCatModel.class).set("state=1").where("serviceId=? and emobId = ?", serviceId, emobId).execute();
        } else {
            //false 取消订单
            new Update(FastShopCatModel.class).set("state=4").where("serviceId=? and emobId = ?", serviceId, emobId).execute();
        }
    }

    public static FastShopCatModel findByServiceId(int serviceId, String emobId) {
        return new Select().from(FastShopCatModel.class).where("serviceId=? and emobId = ?", serviceId, emobId).executeSingle();
    }

    public static void deleteByServiceId() {
        new Delete().from(FastShopCatModel.class).where("state=1").execute();
    }

    public static void deleteByServiceId(String emobId) {
        new Delete().from(FastShopCatModel.class).where("state=1 and emobId=?", emobId).execute();
    }

    public static void deleteByServiceId(String emobId, String shopEmobid) {
        new Delete().from(FastShopCatModel.class).where("emobId = ? and shopEmobId = ?", emobId, shopEmobid).execute();
    }

    public static void deleteByState(String emobId) {
        new Delete().from(FastShopCatModel.class).where("state=4 and emobId = ? ", emobId).execute();
    }


}
