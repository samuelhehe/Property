package xj.property.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/16.
 */
public class WelfareInfoEntity implements Serializable {

    /**
     * welfareId : 1
     * title : 五常大米 9.9元
     * poster : http://xxxx.com
     * summary : 优质 好吃 真品 只要九块九 手快有 手慢无
     * content : http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/3
     * charactervalues : 5
     * total : 20
     * rule : 此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全
     * phone : 15877449663
     * address : 十号楼101室，物业管理中心
     * status : ongoing
     * startTime : 1442299992
     * endTime : 1442299992
     * price : 100
     * provideTime : 1442299992
     * createTime : 1442299992
     * modifyTime : 1442299992
     * reason : 天干物燥
     * buyed : 3
     * users : [{"userId":3177,"emobId":"2011eb792db7b1029341faab3ad65919","nickname":"lei","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt"},{"userId":3177,"emobId":"2011eb792db7b1029341faab3ad65919","nickname":"lei","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt"},{"userId":434,"emobId":"58a4e33fbc97abca4051130ad9b2e2cf","nickname":"我是009","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt"}]
     */


    //// 与查看历史福利详情bean 合并

    private int remain;
    private int communityId;//
    private String code;///

    private String expire; //// 福利是否过期


    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }



    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    /////////////


    private int welfareId;
    private String title;
    private String poster;
    private String summary;
    private String content;
    private int charactervalues;
    private int total;
    private String rule;
    private String phone;
    private String address;
    private String status;
    private int startTime;
    private int endTime;



    private float price;
    private int provideTime;
    private int createTime;
    private int modifyTime;
    private String reason;
    private int buyed;

    private String provideInstruction;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    public String getProvideInstruction() {
        return provideInstruction;
    }

    public void setProvideInstruction(String provideInstruction) {
        this.provideInstruction = provideInstruction;
    }

    private List<WelfareUsersEntity> users;

    public void setWelfareId(int welfareId) {
        this.welfareId = welfareId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCharactervalues(int charactervalues) {
        this.charactervalues = charactervalues;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }


    public void setProvideTime(int provideTime) {
        this.provideTime = provideTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setModifyTime(int modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setBuyed(int buyed) {
        this.buyed = buyed;
    }

    public void setUsers(List<WelfareUsersEntity> users) {
        this.users = users;
    }

    public int getWelfareId() {
        return welfareId;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
        return content;
    }

    public int getCharactervalues() {
        return charactervalues;
    }

    public int getTotal() {
        return total;
    }

    public String getRule() {
        return rule;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getProvideTime() {
        return provideTime;
    }

    public int getCreateTime() {
        return createTime;
    }

    public int getModifyTime() {
        return modifyTime;
    }

    public String getReason() {
        return reason;
    }

    public int getBuyed() {
        return buyed;
    }

    public List<WelfareUsersEntity> getUsers() {
        return users;
    }


}
