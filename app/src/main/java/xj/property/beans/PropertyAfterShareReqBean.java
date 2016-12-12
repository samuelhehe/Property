package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2016/3/2.
 */
public class PropertyAfterShareReqBean extends  BaseBean {

    /**
     * lifeContent : ${文字内容}
     * lifePhotos : ["${分享图片地址}","${分享图片地址}"]
     */

    private String lifeContent;
    private List<String> lifePhotos;

    public void setLifeContent(String lifeContent) {
        this.lifeContent = lifeContent;
    }

    public void setLifePhotos(List<String> lifePhotos) {
        this.lifePhotos = lifePhotos;
    }

    public String getLifeContent() {
        return lifeContent;
    }

    public List<String> getLifePhotos() {
        return lifePhotos;
    }
}
