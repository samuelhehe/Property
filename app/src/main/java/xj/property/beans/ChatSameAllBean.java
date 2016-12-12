package xj.property.beans;

/**
 * 作者：asia on 2015/12/30 18:55
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class ChatSameAllBean extends BaseBean {

    private String status;

    private ChatSameInfoBean info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChatSameInfoBean getInfo() {
        return info;
    }

    public void setInfo(ChatSameInfoBean info) {
        this.info = info;
    }
}
