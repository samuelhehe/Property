package xj.property.beans;

import xj.property.netbase.*;

/**
 * Created by n on 2015/4/16.
 */
public class UpdateUserPasswordRequest extends xj.property.netbase.BaseBean implements XJ{
    private String password;
    /**
     * authCode : 5569
     * username : 18600113751
     *
     "password": "fcea920f7412b5da7be0cf42b8c93759"
     *
     * v3 2016/02/29 找回密码
     */

    private String authCode;
    private String username;
    /**
     * newPassword : {新密码}
     * v3 2016/02/29 重置密码
     *
     */

    private String newPassword;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getUsername() {
        return username;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
