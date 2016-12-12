package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/4/7.
 *
 * v3  2016/03/18
 * 创建一个新表 push_event_modelv3  存储推送过来的message ，
 * 在原来表 修改pushid--> messageId
 *
 */
@Table(name = "push_event_modelv3")
public class PushEventModel extends Model {
    @Column(name="messageId")
    public long messageId;
    @Column(name="cmd_code")
    public int cmd_code;
    @Column(name="title")
    public String title;
    @Column(name="content")
    public String content;
    @Column(name="timestemp")
    public int timestemp;
    @Column(name="stauts")
    public String stauts;
    @Column(name="emobid")
    public String emobid;

    public int getCmd_code() {
        return cmd_code;
    }

    public void setCmd_code(int cmd_code) {
        this.cmd_code = cmd_code;
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

    public int getTimestemp() {
        return timestemp;
    }

    public void setTimestemp(int timestemp) {
        this.timestemp = timestemp;
    }

    public String getStauts() {
        return stauts;
    }

    public void setStauts(String stauts) {
        this.stauts = stauts;
    }

    public String getEmobid() {
        return emobid;
    }

    public void setEmobid(String emobid) {
        this.emobid = emobid;
    }

    @Override
    public String toString() {
        return "PushEventModel{" +
                "messageId=" + messageId +
                ", cmd_code=" + cmd_code +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", timestemp=" + timestemp +
                ", stauts='" + stauts + '\'' +
                ", emobid='" + emobid + '\'' +
                '}';
    }
}
