package xj.property.beans;

/**
 * Created by Administrator on 2015/11/4.
 *
 * v3 2016/03/15
 */
public class VoteDetailEveReqBean extends  xj.property.netbase.BaseBean {


    public VoteDetailEveReqBean(int voteId, String emobIdFrom, String emobIdTo, String chatContent , int isComment) {
        this.voteId = voteId;
        this.emobIdFrom = emobIdFrom;
        this.emobIdTo = emobIdTo;
        this.chatContent = chatContent;
        this.isComment = isComment;
    }


    private int voteId;
    private String emobIdFrom;
    private String emobIdTo;
    private String chatContent;
    private  int isComment ;/// {是否是评论：1->是，0->否（0的情况是回复）},


    /**
     *
     * {
     "voteId": {投票ID},
     "isComment": {是否是评论：1->是，0->否（0的情况是回复）},
     "chatContent": "{评论内容}",
     "emobIdFrom": "{评论人环信ID}",
     "emobIdTo": "{被评论人环信ID}",
     }
     *
     */


    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }


    public int getVoteId() {
        return voteId;
    }

    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public String getChatContent() {
        return chatContent;
    }


    public int getIsComment() {
        return isComment;
    }

    public void setIsComment(int isComment) {
        this.isComment = isComment;
    }
}
