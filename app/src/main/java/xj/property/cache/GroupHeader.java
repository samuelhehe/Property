package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/4/3.
 */
@Table(name = "group_header")
public class GroupHeader extends Model {
    @Column(name = "group_id")
    public String group_id;
    @Column(name = "header_id")
    public String header_id;
    @Column(name = "num")
    public int  num;
    public GroupHeader() {
    }

    public GroupHeader(String group_id, String header_id, int num) {
        this.group_id = group_id;
        this.header_id = header_id;
        this.num = num;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getHeader_id() {
        return header_id;
    }

    public void setHeader_id(String header_id) {
        this.header_id = header_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
