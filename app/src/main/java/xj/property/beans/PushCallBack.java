package xj.property.beans;

/**
 * Created by Administrator on 2015/6/18.
 * v3 2016/03/18
 */
public class PushCallBack extends  xj.property.netbase.BaseBean {
    /**
     * emobId : {用户环信ID}
     * messageId : {推送消息ID}
     */

    private String emobId;
    private Long messageId;

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }


    public String getEmobId() {
        return emobId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }


    /*

    {
    "emobId": "{用户环信ID}",
    "messageId": "{推送消息ID}"
}
     */



}
