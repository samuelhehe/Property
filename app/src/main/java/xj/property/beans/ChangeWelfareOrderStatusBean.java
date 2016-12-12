package xj.property.beans;

/**
 * Created by che on 2015/9/18.
 * v3 2016/03/14
 */
public class ChangeWelfareOrderStatusBean extends xj.property.netbase.BaseBean {


    public ChangeWelfareOrderStatusBean(String messageId) {
        this.messageId = messageId;
    }

    /**
     * messageId : a8e7703369a14a2ba3222cc2373f3b6b
     */


    private String messageId;

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}
