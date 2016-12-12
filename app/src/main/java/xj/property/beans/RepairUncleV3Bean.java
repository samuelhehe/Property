package xj.property.beans;

import java.util.List;

/**
 * 作者：che on 2016/3/16 14:46
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class RepairUncleV3Bean implements XJ {
    private int page;
    private int limit;
    private List<RepairUncleV3DataBean> data;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<RepairUncleV3DataBean> getData() {
        return data;
    }

    public void setData(List<RepairUncleV3DataBean> data) {
        this.data = data;
    }

    public class RepairUncleV3DataBean implements XJ{
        private int shopId;
        private String shopName;
        private String shopDesc;
        private String emobId;
        private String phone;
        private String logo;
        private String status;
        private Long createTime;
        private Long updateTime;

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopDesc() {
            return shopDesc;
        }

        public void setShopDesc(String shopDesc) {
            this.shopDesc = shopDesc;
        }

        public String getEmobId() {
            return emobId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
