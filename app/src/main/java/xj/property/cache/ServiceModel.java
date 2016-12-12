package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/4/9.
 */
@Table(name = "servicemodel")
public class ServiceModel extends Model{
    @Column(name = "Service_emobid")
    public String Service_emobid;

    //已废弃
    @Column(name = "User_emobid")
    public String User_emobid;

    @Column(name = "useremobid")
    public String useremobid;

    public String getService_emobid() {
        return Service_emobid;
    }

    public void setService_emobid(String service_emobid) {
        this.Service_emobid = service_emobid;
    }

    public String getUser_emobid() {
        return User_emobid;
    }

    public void setUser_emobid(String user_emobid) {
        User_emobid = user_emobid;
    }


    public String getUseremobid() {
        return useremobid;
    }

    public void setUseremobid(String useremobid) {
        this.useremobid = useremobid;
    }

    public ServiceModel() {
    }

    public ServiceModel(String service_emobid) {
        this.Service_emobid = service_emobid;
    }

    public ServiceModel(String service_emobid,String user_emobid) {
        this.Service_emobid = service_emobid;
        this.useremobid=user_emobid;
    }
}
