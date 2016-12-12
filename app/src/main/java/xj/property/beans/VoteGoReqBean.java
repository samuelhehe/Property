package xj.property.beans;

/**
 * Created by Administrator on 2015/11/24.
 *
 * v3 2016/03/16
 */
public class VoteGoReqBean extends  xj.property.netbase.BaseBean  {


    /**
     * voteId : 1
     * voteOptionsId : 1
     * emobId :
     * createTime : 1448281294
     */

    private int voteId;
    private int voteOptionsId;
    private String emobId;
    private String emobIdTo; //{投票创建者的环信ID}


//    {
//        "voteId": {投票ID},
//        "voteOptionsId": {投票选项ID，和投票的顺序是一直的，第一个选项的ID为0，以此类推},
//        "emobId": "{当前投票人环信ID}",
//            "emobIdTo": "{投票创建者的环信ID}"
//    }



    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public void setVoteOptionsId(int voteOptionsId) {
        this.voteOptionsId = voteOptionsId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }


    public int getVoteId() {
        return voteId;
    }

    public int getVoteOptionsId() {
        return voteOptionsId;
    }

    public String getEmobId() {
        return emobId;
    }


    public String getEmobIdTo() {
        return emobIdTo;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }
}
