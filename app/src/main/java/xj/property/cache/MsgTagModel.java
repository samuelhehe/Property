package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by maxwell on 15/2/11.
 *
 */
@Table(name = "msg_tag")
public class MsgTagModel extends Model{
    //primary key ,auto increament
    @Column(name = "msg_tag_id")
    public String msgTagId;

    @Column(name = "serail")
    public String serail;

    @Column(name = "cmd_code")
    public int CMD_CODE;

    @Column(name = "createtime")
    public int createTime;

    @Column(name = "message_id")
    public int messageId;

    @Column(name = "order_detail")
    public String orderDetail;

}
