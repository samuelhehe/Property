package xj.property.cache;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;


/**
 * v3  2016/03/18
 * 创建一个新表 xj_notify  存储推送过来的message ，
 * 在原来表 修改pushid--> messageId
 */
@Table(name = "xj_notify")
public class XJNotify extends Model implements Serializable {

    /// 2015/12/4 去重id 标识 添加字段
    @Column(name = "messageId")
    public Integer messageId;

    @Column(name = "content")
    public String content;

    @Column(name = "emobid")
    public String emobid;
    @Column(name = "cmd_code")
    public int CMD_CODE;
    @Column(name = "title")
    public String title;

    //订单类型：哪部分维修，如保洁为时长
    @Column(name = "timestamp")
    public int timestamp;

    @Column(name = "isreaded")
    public boolean isReaded;

    //已废弃
    @Column(name = "hasreaded")
    public String hasReaded;

    @Column(name = "read_status")
    public String read_status;

        /*
            "title": "温馨提示",
            "content": " 关于开展消防演习让火灾远离家园！\n",
            "timestamp": 1446953170,
            "CMD_CODE": 100,
            "messageId": 1446953170451,
            "number": 1
          */

    public XJNotify(String emobid, int CMD_CODE, String title, String content, int timestamp, boolean isReaded) {
        this.emobid = emobid;
        this.CMD_CODE = CMD_CODE;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.isReaded = isReaded;
    }

    public XJNotify(int messageId, String emobid, int CMD_CODE, String title, String content, int timestamp, boolean isReaded) {
        this.messageId = messageId;
        this.emobid = emobid;
        this.CMD_CODE = CMD_CODE;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.isReaded = isReaded;
    }

    public XJNotify(String emobid, int CMD_CODE, String title, String content, int timestamp, boolean isReaded, String read_status) {
        this.emobid = emobid;
        this.CMD_CODE = CMD_CODE;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.isReaded = isReaded;
        this.read_status = read_status;
    }

    public XJNotify(int messageId, String emobid, int CMD_CODE, String title, String content, int timestamp, boolean isReaded, String read_status) {
        this.messageId = messageId;
        this.emobid = emobid;
        this.CMD_CODE = CMD_CODE;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.isReaded = isReaded;
        this.read_status = read_status;
    }


    public XJNotify() {
    }

    public String getHasReaded() {
        return hasReaded;
    }

    public void setHasReaded(String hasReaded) {
        this.hasReaded = hasReaded;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    @Override
    public String toString() {
        return "XJNotify{" +
                "CMD_CODE=" + CMD_CODE +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", isReaded=" + isReaded +
                '}';
    }
}
