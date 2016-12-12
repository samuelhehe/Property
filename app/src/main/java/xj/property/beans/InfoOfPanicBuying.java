package xj.property.beans;

import java.util.List;

/**
 * Created by n on 2015/8/13.
 */
public class InfoOfPanicBuying {
    /**
     * total : 100
     * createTime : 462365
     * title : 物美价廉3
     * emobId : 34347c343003e57232a5d21f14fe399e
     * crazySalesId : 12
     * skimCount : 12
     * remain : 98
     * status : ongoing
     * perLimit : 2
     * crazySalesUser : [{"createTime":1439381791,"crazySalesId":12,"crazySalesUserId":0,"status":"unuse","userEmobId":"34347c343003e57232a5d21f14fe399e","code":"164925222204","shopEmobId":"34347c343003e57232a5d21f14fe399e","useTime":0},{"createTime":1439381791,"crazySalesId":12,"crazySalesUserId":0,"status":"unuse","userEmobId":"34347c343003e57232a5d21f14fe399e","code":"482231149650","shopEmobId":"34347c343003e57232a5d21f14fe399e","useTime":0}]
     * description : 好东西，物美价廉3
     * endTime : 1436684409
     */
    private int total;
    private int createTime;
    private String title;
    private String emobId;
    private int crazySalesId;
    private int skimCount;
    private int remain;
    private String status;
    private int perLimit;
    private List<InfoOfCrazySalesShop> crazySalesUser;
    private String description;
    private int endTime;
    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setCrazySalesId(int crazySalesId) {
        this.crazySalesId = crazySalesId;
    }

    public void setSkimCount(int skimCount) {
        this.skimCount = skimCount;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPerLimit(int perLimit) {
        this.perLimit = perLimit;
    }

    public void setCrazySalesUser(List<InfoOfCrazySalesShop> crazySalesUser) {
        this.crazySalesUser = crazySalesUser;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getTotal() {
        return total;
    }

    public int getCreateTime() {
        return createTime;
    }

    public String getTitle() {
        return title;
    }

    public String getEmobId() {
        return emobId;
    }

    public int getCrazySalesId() {
        return crazySalesId;
    }

    public int getSkimCount() {
        return skimCount;
    }

    public int getRemain() {
        return remain;
    }

    public String getStatus() {
        return status;
    }

    public int getPerLimit() {
        return perLimit;
    }

    public List<InfoOfCrazySalesShop> getCrazySalesUser() {
        return crazySalesUser;
    }

    public String getDescription() {
        return description;
    }

    public int getEndTime() {
        return endTime;
    }


}
