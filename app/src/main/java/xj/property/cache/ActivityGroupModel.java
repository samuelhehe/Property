package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by maxwell on 15/1/27.
 */
@Table(name = "activity_group")
public class ActivityGroupModel extends Model{
    @Column(name = "emob_group_id")
    public String emobGroupId;

    @Column(name = "emob_user_id")
    public String emobUserId;

    @Column(name = "group_status")
    public String groupStatus;

    @Column(name = "community_id")
    public String communityId;

    @Column(name = "create_time")
    public String createTime;

    @Column(name = "nick_name")
    public String nickName;

    @Column(name = "avatar")
    public String avatar;


//    public ActivityGroupModel(){
//        super();
//    }
//    public ActivityGroupModel(String emobGroupId,String emobUserId){
//        super();
//        this.emobGroupId = emobGroupId;
//        this.emobUserId = emobUserId;
//    }
}
