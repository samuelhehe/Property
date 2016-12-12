package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/4/3.
 */
@Table(name = "group_info")
public class GroupInfo extends Model {
    @Column(name = "group_name")
    public String group_name;
    @Column(name = "group_id")
    public String group_id;
    @Column(name = "max_value")
    public String  max_value;
    public GroupInfo() {
    }

    public GroupInfo(String group_name, String group_id, String max_value) {
        super();
        this.group_name = group_name;
        this.group_id = group_id;
        this.max_value = max_value;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getMax_value() {
        return max_value;
    }

    public void setMax_value(String max_value) {
        this.max_value = max_value;
    }
}
