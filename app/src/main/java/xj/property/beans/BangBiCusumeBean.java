package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/9.
 */
public class BangBiCusumeBean {


    /**
     * page : 1
     * limit : 10
     * data : [{"bonuscoinId":3062,"createTime":1456142849,"bonuscoinCount":10,"bonuscoinSource":"分享快店","emobId":"9064d94ce3da4ad68979b1151b2f6588"},{"bonuscoinId":3063,"createTime":1456140023,"bonuscoinCount":21,"bonuscoinSource":"快店购物","emobId":"9064d94ce3da4ad68979b1151b2f6588"},{"bonuscoinId":3064,"createTime":1456139571,"bonuscoinCount":16,"bonuscoinSource":"快店购物","emobId":"9064d94ce3da4ad68979b1151b2f6588"},{"bonuscoinId":3065,"createTime":1455945956,"bonuscoinCount":10,"bonuscoinSource":"分享快店","emobId":"9064d94ce3da4ad68979b1151b2f6588"},{"bonuscoinId":3066,"createTime":1455945891,"bonuscoinCount":18,"bonuscoinSource":"快店购物","emobId":"9064d94ce3da4ad68979b1151b2f6588"},{"bonuscoinId":3067,"createTime":1455945889,"bonuscoinCount":16,"bonuscoinSource":"快店购物","emobId":"9064d94ce3da4ad68979b1151b2f6588"},{"bonuscoinId":3068,"createTime":1455874111,"bonuscoinCount":16,"bonuscoinSource":"快店购物","emobId":"9064d94ce3da4ad68979b1151b2f6588"},{"bonuscoinId":3069,"createTime":1455785700,"bonuscoinCount":19,"bonuscoinSource":"快店购物","emobId":"9064d94ce3da4ad68979b1151b2f6588"},{"bonuscoinId":3070,"createTime":1454773593,"bonuscoinCount":23,"bonuscoinSource":"快店购物","emobId":"9064d94ce3da4ad68979b1151b2f6588"},{"bonuscoinId":3071,"createTime":1454641602,"bonuscoinCount":32,"bonuscoinSource":"快店购物","emobId":"9064d94ce3da4ad68979b1151b2f6588"}]
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
         * bonuscoinId : 3062
         * createTime : 1456142849
         * bonuscoinCount : 10
         * bonuscoinSource : 分享快店
         * emobId : 9064d94ce3da4ad68979b1151b2f6588
         */

        private int bonuscoinId;
        private int createTime;
        private int bonuscoinCount;
        private String bonuscoinSource;
        private String emobId;

        public void setBonuscoinId(int bonuscoinId) {
            this.bonuscoinId = bonuscoinId;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setBonuscoinCount(int bonuscoinCount) {
            this.bonuscoinCount = bonuscoinCount;
        }

        public void setBonuscoinSource(String bonuscoinSource) {
            this.bonuscoinSource = bonuscoinSource;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public int getBonuscoinId() {
            return bonuscoinId;
        }

        public int getCreateTime() {
            return createTime;
        }

        public int getBonuscoinCount() {
            return bonuscoinCount;
        }

        public String getBonuscoinSource() {
            return bonuscoinSource;
        }

        public String getEmobId() {
            return emobId;
        }
    }
}
