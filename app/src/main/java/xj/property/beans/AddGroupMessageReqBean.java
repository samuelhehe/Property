package xj.property.beans;

import xj.property.netbase.*;

/**
 * Created by Administrator on 2015/12/31.
 */
public class AddGroupMessageReqBean extends  xj.property.netbase.BaseBean {


    /**
     * messageContent : 大家一起玩
     * messageId : 1451369301003
     * emobIdFrom : ce04f45b22793b5a2425962b38c74d08
     * emobIdTo : 1e45c249f64eead873aa8a580b30733c
     * groupId : 1429702488658259
     * type : accept
     */


    /**
     *
     *
     * {
     "groupId": "{环信群组ID}",
     "emobIdTo": "{接收方环信ID，多个请用英文逗号隔开}",
     "type": "{消息类型，invited，accept}",
     "messageContent": "{消息内容}",
     "emobIdFrom": "{发送人环信ID}",
     "messageId": "{消息ID}",
     "communityId": {小区ID}
     }

     *{
     "groupId": "168209969693327808",
     "emobIdTo": "fcb6adf78bef4ee4940d2af8ee7905f9",
     "type": "invited",
     "messageContent": "大家好，一起来吧",
     "emobIdFrom": "d463b16dfc014466a1e441dd685ba505",
     "messageId": "155998529100186104"，
     "communityId": 2
     }
     *
     *
     */




    private String messageContent;
    private String messageId;
    private String emobIdFrom;
    private String emobIdTo;
    private String groupId;
    private String type;

    /**
     * communityId : 2
     * v3 2016/03/03
     */

    private int communityId;

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getType() {
        return type;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getCommunityId() {
        return communityId;
    }
}
