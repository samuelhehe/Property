package xj.property.beans;

/**
 * Created by n on 2015/4/9.
 *
 * v3 2016/3/18
 * 获取帮帮券列表
 */
public class UserBonusBean {
    /**
     {  "bonusId":88,
     "bonusName":"物业券",
     "bonusType":7,
     "bonusPrice":"2",
     "bonusDetail":"物业费专用券",
     "bonusR":"139",
     "bonusG":"194",
     "bonusB":"74",
     "createTime":144829256,
     "used":"no",
     "startTime":1456379744,
     "expireTime":1475251200
     }
     */

    /**
     * {
     * "bonusId": {帮帮券ID},
     * "bonusName": "{帮帮券名称}",
     * "bonusType": {帮帮劵类型},
     * "bonusDetail": "{帮帮劵详情}",
     * "bonusR": "{帮帮劵RGB颜色的R值}",
     * "bonusG": "{帮帮劵RGB颜色的G值}",
     * "bonusB": "{帮帮劵RGB颜色的B值}",
     * "createTime": {帮帮券创建时间},
     * "bonusPrice": {帮帮券抵扣金额},
     * "bonusUrl": {帮帮券图片地址},
     * "used": "{是否已经使用(yes->已使用,no->未使用)}",
     * "startTime": {有效起始时间},
     * "expireTime": {有效截止时间}
     * }
     */

    private int bonusId;
    private String bonusName;
    private int bonusType;
    private String bonusPrice;
    private String bonusDetail;
    private String bonusR;
    private String bonusG;
    private String bonusB;
    private int createTime;
    private String used;
    private int startTime;
    private int expireTime;

    public void setBonusId(int bonusId) {
        this.bonusId = bonusId;
    }

    public void setBonusName(String bonusName) {
        this.bonusName = bonusName;
    }

    public void setBonusType(int bonusType) {
        this.bonusType = bonusType;
    }

    public void setBonusPrice(String bonusPrice) {
        this.bonusPrice = bonusPrice;
    }

    public void setBonusDetail(String bonusDetail) {
        this.bonusDetail = bonusDetail;
    }

    public void setBonusR(String bonusR) {
        this.bonusR = bonusR;
    }

    public void setBonusG(String bonusG) {
        this.bonusG = bonusG;
    }

    public void setBonusB(String bonusB) {
        this.bonusB = bonusB;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public int getBonusId() {
        return bonusId;
    }

    public String getBonusName() {
        return bonusName;
    }

    public int getBonusType() {
        return bonusType;
    }

    public String getBonusPrice() {
        return bonusPrice;
    }

    public String getBonusDetail() {
        return bonusDetail;
    }

    public String getBonusR() {
        return bonusR;
    }

    public String getBonusG() {
        return bonusG;
    }

    public String getBonusB() {
        return bonusB;
    }

    public int getCreateTime() {
        return createTime;
    }

    public String getUsed() {
        return used;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getExpireTime() {
        return expireTime;
    }



}
