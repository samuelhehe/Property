package xj.property.beans;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * 作者：asia on 2015/12/26 11:48
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
@Table(name = "run_for_arrow")
public class RunFordbBean extends Model implements Serializable {

    @Column(name = "emobId")
    private String emobId;
    @Column(name = "rank")
    private int rank;

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
