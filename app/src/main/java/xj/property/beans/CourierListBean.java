package xj.property.beans;

import java.util.ArrayList;

/**
 * Created by chenxiangyu on 2015/4/1.
 */
public class CourierListBean implements  XJ{
    private String status;
    private Info info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
    public static class Info implements XJ {

        private String begin;
        private String end;
        private String first;
        private String last;
        private String navNum;
        private String next;
        private String num;
        private int pageCount;
        private String pageSize;
        private String prev;
        private String rowCount;
        private String startRow;

        public ArrayList<pageData> pageData;

        public String getBegin() {
            return begin;
        }

        public void setBegin(String begin) {
            this.begin = begin;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getNavNum() {
            return navNum;
        }

        public void setNavNum(String navNum) {
            this.navNum = navNum;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getPrev() {
            return prev;
        }

        public void setPrev(String prev) {
            this.prev = prev;
        }

        public String getRowCount() {
            return rowCount;
        }

        public void setRowCount(String rowCount) {
            this.rowCount = rowCount;
        }

        public String getStartRow() {
            return startRow;
        }

        public void setStartRow(String startRow) {
            this.startRow = startRow;
        }

        public ArrayList<CourierListBean.pageData> getPageData() {
            return pageData;
        }

        public void setPageData(ArrayList<CourierListBean.pageData> pageData) {
            this.pageData = pageData;
        }
    }
    public static class pageData implements XJ {
        private String address ;
        private String authCode ;
        public String businessStartTime;
        private String businessEndTime;
        private long communityId ;
        private String  createTime;
        private String emobId;
        private String  logo;
        private String orderSum;
        private String phone;
        private String score;
        private String shopId;
        private String shopName;
        private String shopsDesc;
        private String sort;
        private String status;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAuthCode() {
            return authCode;
        }

        public void setAuthCode(String authCode) {
            this.authCode = authCode;
        }

        public String getBusinessStartTime() {
            return businessStartTime;
        }

        public void setBusinessStartTime(String businessStartTime) {
            this.businessStartTime = businessStartTime;
        }

        public String getBusinessEndTime() {
            return businessEndTime;
        }

        public void setBusinessEndTime(String businessEndTime) {
            this.businessEndTime = businessEndTime;
        }

        public long getCommunityId() {
            return communityId;
        }

        public void setCommunityId(long communityId) {
            this.communityId = communityId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEmobId() {
            return emobId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getOrderSum() {
            return orderSum;
        }

        public void setOrderSum(String orderSum) {
            this.orderSum = orderSum;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopsDesc() {
            return shopsDesc;
        }

        public void setShopsDesc(String shopsDesc) {
            this.shopsDesc = shopsDesc;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


}
