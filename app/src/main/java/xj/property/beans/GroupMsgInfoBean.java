package xj.property.beans;

/**
 * Created by Administrator on 2016/1/6.
 */
public class GroupMsgInfoBean {


    /**
     * {
     * "messageContent": "{消息内容}",
     * "messageId": "{消息ID}",
     * "emobIdFrom": "{消息发送者环信ID}",
     * "emobIdTo": "{消息接收者环信ID}",
     * "groupId": "{环信群组ID}",
     * "type": "{消息类型}",
     * "createTime": {创建时间}
     * }
     */


    private String messageContent;
    private String messageId;
    private String emobIdFrom;
    private String groupId;
    private String type;
    private int createTime;
    private String fromNickname;
    private String fromAvatar;
    private String groupName;
    /**
     * emobIdTo : fcb6adf78bef4ee4940d2af8ee7905f9
     * <p/>
     * v3 2016/03/03
     */

    private String emobIdTo;


    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setFromNickname(String fromNickname) {
        this.fromNickname = fromNickname;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public String getGroupId() {
        return groupId;
    }

    public String getType() {
        return type;
    }

    public int getCreateTime() {
        return createTime;
    }

    public String getFromNickname() {
        return fromNickname;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }
}
