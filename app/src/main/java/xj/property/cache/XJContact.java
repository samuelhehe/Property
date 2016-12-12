package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/4/9.
 */
@Table(name = "xj_contact")
public class XJContact extends Model{
    @Column(name = "emobid")
    public String emobid;


    @Column(name = "nikename")
    public String nikename;

    @Column(name = "sort")
    public String sort;
    @Column(name = "avatar")
    public String avatar;

    public XJContact() {
    }

    public XJContact(String emobid,String avatar, String nikename, String sort ) {
        this.emobid = emobid;
        this.nikename = nikename;
        this.sort = sort;
        this.avatar = avatar;
    }

    public String getEmobid() {
        return emobid;
    }

    public void setEmobid(String emobid) {
        this.emobid = emobid;
    }

    public String getNikename() {
        return nikename;
    }

    public void setNikename(String nikename) {
        this.nikename = nikename;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
