package xj.property.beans;

import java.util.List;

/**
 * Created by chenxiangyu on 2015/4/1.
 *
 * v3 2016/03/21
 */
public class SendWaterListBean {


    /**
     * page : 1
     * limit : 10
     * data : [{"name":"雀巢","phone":"18888888888","beginTime":"8:00","endTime":"20:00"},{"name":"娃哈哈","phone":"18888889999","beginTime":"8:00","endTime":"20:00"}]
     */

    private int page;
    private int limit;

    private List<DataEntity> data;

    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * name : 雀巢
         * phone : 18888888888
         * beginTime : 8:00
         * endTime : 20:00
         */

        private String name;
        private String phone;
        private String beginTime;
        private String endTime;

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public String getEndTime() {
            return endTime;
        }
    }
}
