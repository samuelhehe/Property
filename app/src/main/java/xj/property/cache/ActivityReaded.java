package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/4/23.
 */
@Table(name = "activity_isreaded")
public class ActivityReaded extends Model{
    @Column(name = "activity_id")
    public int activity_id;
    @Column(name = "isreaded")
    public boolean isreaded;

    public ActivityReaded(int activity_id, boolean isreaded) {
        this.activity_id = activity_id;
        this.isreaded = isreaded;
    }

    public ActivityReaded() {
    }
}
