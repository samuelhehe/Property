package xj.property.beans;

/**
 * Created by che on 2015/7/16.
 * <p/>
 * v3 2016/03/04
 */
public class GetShareGoodsResultPicBean {

    /**
     * status : yes
     * info : ["图片url","http://ltzmaxwell.qiniudn.com/FnIgJmQfL503BPdHZh0C8PwKT0oI","...."]
     */
    private String[] list;
    /**
     * emobIdShop : fcb6adf78bef4ee4940d2af8ee7905f9
     * list : ["http://7d9lcl.com2.z0.glb.qiniucdn.com/Fi3gqlVD_U5T3DeiEav-aucUsMBT"]
     */
    private String emobIdShop;
    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }
    public String getEmobIdShop() {
        return emobIdShop;
    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

}
