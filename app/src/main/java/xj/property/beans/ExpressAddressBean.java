package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/5/25.
 *
 * v3 2016/03/15
 */
public class ExpressAddressBean {


    /**
     * //        "page": {页码},
     //                "limit": {页面大小},
     //                "data": [{
     //            "expressName": "{快递名称}",
     //                    "expressLogo": "{快递logo}",
     //                    "expressPhone": "{快递电话}",
     //                    "beginTime": "{快递上班时间}",
     //                    "endTime": "{快递下班时间}"
     //        }]

     */




    /**
     * page : 1
     * limit : 10
     * data : [{"expressName":"圆通速递-赵俊杰","expressLogo":"http://7d9lcl.com2.z0.glb.qiniucdn.com/QQ截图20150421125447.png","expressPhone":"18911337177","beginTime":"8:00","endTime":"19:00"},{"expressName":"申通快递-张晓峰","expressLogo":"http://7d9lcl.com2.z0.glb.qiniucdn.com/QQ截图20150421125516.png","expressPhone":"18010217869","beginTime":"8:00","endTime":"19:00"},{"expressName":"汇通快运-吴欢","expressLogo":"http://7d9lcl.com2.z0.glb.qiniucdn.com/QQ截图20150421125516.png","expressPhone":"18911808079","beginTime":"8:00","endTime":"19:00"},{"expressName":"天天快递-魏东东","expressLogo":"http://7d9lcl.com2.z0.glb.qiniucdn.com/QQ截图20150421125831.png","expressPhone":"13581796849","beginTime":"8:00","endTime":"19:00"},{"expressName":"国通快递-张灿富","expressLogo":"http://7d9lcl.com2.z0.glb.qiniucdn.com/QQ截图20150421125939.png","expressPhone":"15201624836","beginTime":"8:00","endTime":"19:00"},{"expressName":"中通速递-谷文涛","expressLogo":"http://7d9lcl.com2.z0.glb.qiniucdn.com/8720677111.png","expressPhone":"18010217872","beginTime":"8:00","endTime":"19:00"}]
     */

    private int page;
    private int limit;
    private List<CourierDetailBean> data;

    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setData(List<CourierDetailBean> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public List<CourierDetailBean> getData() {
        return data;
    }

    public static class CourierDetailBean {
        /**
         * expressName : 圆通速递-赵俊杰
         * expressLogo : http://7d9lcl.com2.z0.glb.qiniucdn.com/QQ截图20150421125447.png
         * expressPhone : 18911337177
         * beginTime : 8:00
         * endTime : 19:00
         */

        private String expressName;
        private String expressLogo;
        private String expressPhone;
        private String beginTime;
        private String endTime;

        public void setExpressName(String expressName) {
            this.expressName = expressName;
        }

        public void setExpressLogo(String expressLogo) {
            this.expressLogo = expressLogo;
        }

        public void setExpressPhone(String expressPhone) {
            this.expressPhone = expressPhone;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getExpressName() {
            return expressName;
        }

        public String getExpressLogo() {
            return expressLogo;
        }

        public String getExpressPhone() {
            return expressPhone;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public String getEndTime() {
            return endTime;
        }
    }

}
