package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/4/3.
 */
@Table(name = "order_model")
public class OrderModel extends Model {
    @Column(name = "msg_id")
    public String msg_id;
    @Column(name = "serial")
    public String serial;
    @Column(name = "cmd_code")
    public int cmd_code;

    public OrderModel() {
    }

    public OrderModel(String msg_id, String serial, int cmd_code) {
        this.msg_id = msg_id;
        this.serial = serial;
        this.cmd_code = cmd_code;
    }

    public int getCmd_code() {

        return cmd_code;
    }

    public void setCmd_code(int cmd_code) {
        this.cmd_code = cmd_code;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }


    @Override
    public String toString() {
        return "OrderModel{" +
                "msg_id='" + msg_id + '\'' +
                ", serial='" + serial + '\'' +
                ", cmd_code='" + cmd_code + '\'' +
                '}';
    }
}
