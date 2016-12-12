package xj.property.beans;

import java.util.List;

/**
 * 作者：asia on 2015/12/14 13:44
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class FitmentFinishDetailsInfo {

    private Integer decorationId;
    private Integer communityId;
    private String companyName;
    private String logo;
    private String feature;
    private String phone;
    private String mobilePhone;
    private Long foundTime;
    private String registerMoney;
    private String registerOrganization;
    private String address;
    private String introduction;
    private Integer level;
    private String caseImage;
    private String status;
    private List<FitmentDetailsFeatures> decorationFeatures;
    private List<FitmentDetailsComment> decorationComment;
    private List<FitmentDetailsDecorationUsers> decorationUsers;
    private List<FitmentDetailsViewedUser> viewedUser;
    private List<FitmentDetailsUser> user;

    public Integer getDecorationId() {
        return decorationId;
    }

    public void setDecorationId(Integer decorationId) {
        this.decorationId = decorationId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(Long foundTime) {
        this.foundTime = foundTime;
    }

    public String getRegisterMoney() {
        return registerMoney;
    }

    public void setRegisterMoney(String registerMoney) {
        this.registerMoney = registerMoney;
    }

    public String getRegisterOrganization() {
        return registerOrganization;
    }

    public void setRegisterOrganization(String registerOrganization) {
        this.registerOrganization = registerOrganization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCaseImage() {
        return caseImage;
    }

    public void setCaseImage(String caseImage) {
        this.caseImage = caseImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FitmentDetailsFeatures> getDecorationFeatures() {
        return decorationFeatures;
    }

    public void setDecorationFeatures(List<FitmentDetailsFeatures> decorationFeatures) {
        this.decorationFeatures = decorationFeatures;
    }

    public List<FitmentDetailsComment> getDecorationComment() {
        return decorationComment;
    }

    public void setDecorationComment(List<FitmentDetailsComment> decorationComment) {
        this.decorationComment = decorationComment;
    }

    public List<FitmentDetailsDecorationUsers> getDecorationUsers() {
        return decorationUsers;
    }

    public void setDecorationUsers(List<FitmentDetailsDecorationUsers> decorationUsers) {
        this.decorationUsers = decorationUsers;
    }

    public List<FitmentDetailsViewedUser> getViewedUser() {
        return viewedUser;
    }

    public void setViewedUser(List<FitmentDetailsViewedUser> viewedUser) {
        this.viewedUser = viewedUser;
    }

    public List<FitmentDetailsUser> getUser() {
        return user;
    }

    public void setUser(List<FitmentDetailsUser> user) {
        this.user = user;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
