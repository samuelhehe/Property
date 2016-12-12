package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/4/3.
 */
@Table(name = "item_info")
public class PanicBuyingItemInfo extends Model {

    @Column(name = "crazySalesId")
    public int crazySalesId;
    @Column(name = "emobId")
    public String emobId;

    public PanicBuyingItemInfo() {
    }

    public PanicBuyingItemInfo(int crazySalesId,String emobId) {
        this.crazySalesId = crazySalesId;
        this.emobId=emobId;
    }

    public int getCrazySalesId() {
        return crazySalesId;
    }

    public void setCrazySalesId(int crazySalesId) {
        this.crazySalesId = crazySalesId;
    }
}
