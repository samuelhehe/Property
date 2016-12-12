package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/6/15.
 */
@Table(name = "zambia_cache_host")
public class ZambiaCache extends Model{
//    @Column(name = "lifecircle_id")
//    public int lifecircle_id;
//
//    @Column(name = "circledetail_id")
//    public int circledetail_id;
@Column(name ="emobid")
public String emobid;
    @Column(name ="zambiatime")
    public int zambiatime;

    @Column(name ="emobidhost")
    public String emobidhost;
//    public int getLifecircle_id() {
//        return lifecircle_id;
//    }
//
//    public void setLifecircle_id(int lifecircle_id) {
//        this.lifecircle_id = lifecircle_id;
//    }
//
//    public int getCircledetail_id() {
//        return circledetail_id;
//    }
//
//    public void setCircledetail_id(int circledetail_id) {
//        this.circledetail_id = circledetail_id;
//    }
//
    public int getZambiatime() {
        return zambiatime;
    }

    public void setZambiatime(int zambiatime) {
        this.zambiatime = zambiatime;
    }

    public String getEmobid() {
        return emobid;
    }

    public String getEmobidhost() {
        return emobidhost;
    }

    public void setEmobidhost(String emobidhost) {
        this.emobidhost = emobidhost;
    }

    public void setEmobid(String emobid) {
        this.emobid = emobid;
    }


    @Override
    public String toString() {
        return "ZambiaCache{" +
                "emobid='" + emobid + '\'' +
                ", zambiatime=" + zambiatime +
                ", emobidhost='" + emobidhost + '\'' +
                '}';
    }
}
