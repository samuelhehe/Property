package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/26.
 */
public class SendNotifyReqBean extends  BaseBean{


    /**
     * title : 测试客服标题 个人3
     * content : 测试客服  个人3
     * CMD_CODE : 500
     * senderObject : portion
     * users : ["611fc6d87fcb648df34c0976c3a5f13c"]
     */

    private String title;
    private String content;
    private int CMD_CODE;
    private String senderObject;
    private List<String> users;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCMD_CODE(int CMD_CODE) {
        this.CMD_CODE = CMD_CODE;
    }

    public void setSenderObject(String senderObject) {
        this.senderObject = senderObject;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getCMD_CODE() {
        return CMD_CODE;
    }

    public String getSenderObject() {
        return senderObject;
    }

    public List<String> getUsers() {
        return users;
    }
}
