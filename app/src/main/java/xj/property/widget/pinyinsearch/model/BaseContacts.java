package xj.property.widget.pinyinsearch.model;

public class BaseContacts implements Cloneable{
	private String mId;
	private String mName;
	private String mPhoneNumber;


    public String getRegistNickname() {
        return registNickname;
    }

    public void setRegistNickname(String registNickname) {
        this.registNickname = registNickname;
    }

    public String getRegistEmobId() {
        return registEmobId;
    }

    public void setRegistEmobId(String registEmobId) {
        this.registEmobId = registEmobId;
    }

    public String getRegistAvatar() {
        return registAvatar;
    }

    public void setRegistAvatar(String registAvatar) {
        this.registAvatar = registAvatar;
    }

    private String registNickname;
    private String registEmobId;
    private String registAvatar;


	
	public String getId() {
		return mId;
	}
	
	public void setId(String id) {
		mId = id;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getPhoneNumber() {
		return mPhoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		
		return super.clone();
	}
}
