package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/4/3.
 */
@Table(name = "item_praisereocder")
public class ItemPraiseRecoder extends Model {
    @Column(name = "host_emobid")
    public String host_emobid;
    @Column(name = "custor_emobid")
    public String custor_emobid;
    @Column(name = "lifecircle_id")
    public int  lifecircle_id;
    public ItemPraiseRecoder() {
    }

    public ItemPraiseRecoder(String host_emobid, String custor_emobid, int lifecircle_id) {
        this.host_emobid = host_emobid;
        this.custor_emobid = custor_emobid;
        this.lifecircle_id = lifecircle_id;
    }

    public void setLifecircle_id(int lifecircle_id) {
        this.lifecircle_id = lifecircle_id;
    }

    public String getHost_emobid() {
        return host_emobid;
    }

    public void setHost_emobid(String host_emobid) {
        this.host_emobid = host_emobid;
    }

    public String getCustor_emobid() {
        return custor_emobid;
    }

    public void setCustor_emobid(String custor_emobid) {
        this.custor_emobid = custor_emobid;
    }



}
