package xj.property.beans;

import java.util.List;

/**
 * 作者：che on 2016/3/9 16:23
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class WelfareHistoryV3Bean {
    private int page;
    private int limit;
    private List<WelfareHistoryDataV3Bean> data;

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

    public List<WelfareHistoryDataV3Bean> getData() {
        return data;
    }

    public void setData(List<WelfareHistoryDataV3Bean> data) {
        this.data = data;
    }

    public class WelfareHistoryDataV3Bean{
        private int welfareId;
        private String title;
        private String poster;
        private String content;
        private int charactervalues;
        private int total;
        private String rule;
        private String phone;
        private String status;
        private Long startTime;
        private Long endTime;
        private float price;
        private Long createTime;
        private Long modifyTime;
        private String provideInstruction;
        private int communityId;
        private int remain;

        public int getWelfareId() {
            return welfareId;
        }

        public void setWelfareId(int welfareId) {
            this.welfareId = welfareId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCharactervalues() {
            return charactervalues;
        }

        public void setCharactervalues(int charactervalues) {
            this.charactervalues = charactervalues;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Long getStartTime() {
            return startTime;
        }

        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }

        public Long getEndTime() {
            return endTime;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(Long modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getProvideInstruction() {
            return provideInstruction;
        }

        public void setProvideInstruction(String provideInstruction) {
            this.provideInstruction = provideInstruction;
        }

        public int getCommunityId() {
            return communityId;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public int getRemain() {
            return remain;
        }

        public void setRemain(int remain) {
            this.remain = remain;
        }
    }
}
