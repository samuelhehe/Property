package xj.property.beans;

/**
 * Created by che on 2015/6/26.
 *
 * v3 2016/03/18
 */
public class UnreadBean {
    private String title;
    private String content;
    private int CMD_CODE;
    private String nickname;
    private String type;
    private String emobId;
    private long messageId;
    private int timestamp;


        /*
        {
        "messageId": "{消息ID}",
        "emobId": "{用户环信ID}",
        "nickname": "{用户昵称}",
        "communityId": {小区ID},
        "title": "{消息标题}",
        "content": "{消息内容}",
        "timestamp": {消息发送时间，单位毫秒},
        "CMD_CODE": {CMD_CODE}
             }
         */


    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCMD_CODE() {
        return CMD_CODE;
    }

    public void setCMD_CODE(int CMD_CODE) {
        this.CMD_CODE = CMD_CODE;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}
