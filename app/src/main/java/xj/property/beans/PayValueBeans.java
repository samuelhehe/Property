package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/4/13.
 */
public class PayValueBeans {
 private String status;
private info info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PayValueBeans.info getInfo() {
        return info;
    }

    public void setInfo(PayValueBeans.info info) {
        this.info = info;
    }

    public static class  info{
        private   String unitPrice;
private List<PayValueBean> list;

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public List<PayValueBean> getList() {
            return list;
        }

        public void setList(List<PayValueBean> list) {
            this.list = list;
        }
    }
public static class PayValueBean{
private int entryId;
private String price;
private String entrySum;
private int standardId;
private int communityId;
    private  int sort;
    private  long createTime;

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEntrySum() {
        return entrySum;
    }

    public void setEntrySum(String entrySum) {
        this.entrySum = entrySum;
    }

    public int getStandardId() {
        return standardId;
    }

    public void setStandardId(int standardId) {
        this.standardId = standardId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
}
