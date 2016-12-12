package xj.property.beans;

import java.util.ArrayList;
import java.util.List;

import xj.property.cache.ShopContact;


/**
 * Created by Administrator on 2015/3/16.
 */
public class RepairUncleBean implements XJ {
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
        private String rowCount;
        private String pageSize;
        private String num;
        private String startRow;
        private String next;
        private String prev;
        private String pageCount;
        private String begin;
        private String end;
        private String first;
        private String last;
        private String navNum;
        private List<ShopContact> pageData;

        public List<ShopContact> getPageData() {
            return pageData;
        }

        public void setPageData(List<ShopContact> pageData) {
            this.pageData = pageData;
        }

        public String getRowCount() {
            return rowCount;
        }

        public void setRowCount(String rowCount) {
            this.rowCount = rowCount;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getStartRow() {
            return startRow;
        }

        public void setStartRow(String startRow) {
            this.startRow = startRow;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getPrev() {
            return prev;
        }

        public void setPrev(String prev) {
            this.prev = prev;
        }

        public String getPageCount() {
            return pageCount;
        }

        public void setPageCount(String pageCount) {
            this.pageCount = pageCount;
        }

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
    }

    /*public static class PagerItemBean implements XJ {

        private int shopId;
        public String shopName;
        private String shopsDesc;
        private String address;
        private String communityId;
        private String emobId;
        private String phone;
        private String logo;
        public String status;
        private String sort;
        private String createTime;
        private String authCode;
        private String orderSum;
        private String score;

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

        public String getShopsDesc() {
            return shopsDesc;
        }

        public void setShopsDesc(String shopsDesc) {
            this.shopsDesc = shopsDesc;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAuthCode() {
            return authCode;
        }

        public void setAuthCode(String authCode) {
            this.authCode = authCode;
        }

        public String getOrderSum() {
            return orderSum;
        }

        public void setOrderSum(String orderSum) {
            this.orderSum = orderSum;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }*/

}
