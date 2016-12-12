package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/3/18.
 */
@Table(name = "order_repair")
public class OrderRepair extends Model {
    @Column(name = "msg_id")
    public String msg_id;
    @Column(name = "serivce_id")
    public String serivce_id;
    @Column(name = "name")
    public String name;
    //订单类型：哪部分维修，如保洁为时长
    @Column(name = "type")
    public String type;


    @Column(name = "price")
    public double price;
    //下单时间
    @Column(name = "time")
    public String time;

    //订单号
    @Column(name = "serial")
    public String serial;

 /*   //订单状态
    @Column(name = "state")
    public String state;

    public static final String UNDO = "undo";
    public static final String BEGIN = "begin";
    public static final String DOING = "doing";
    public static final String COMPLETE = "complete";
    public static final String CANCEL = "cancel";*/

    public OrderRepair(String msg_id, String serial) {
        this.msg_id = msg_id;
        this.serial = serial;
    }

    public OrderRepair() {
    }

    @Override
    public String toString() {
        return "OrderRepair{" +
                "msg_id='" + msg_id + '\'' +
                ", serial='" + serial + '\'' +
                '}';
    }
}
