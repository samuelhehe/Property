/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xj.property.domain;

import com.easemob.chat.EMContact;

public class User extends EMContact {
	private int unreadMsgCount;
	private String header;
    public  String communityid;
    public String avatar;
    public String sort;
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if(!(o instanceof  User)){
            return  false;
        }

        User user = (User) o;
        if (avatar != null ? !avatar.equals(user.avatar) : user.avatar != null) return false;
        if (nick != null ? !nick.equals(user.nick) : user.nick != null) return false;
        if (sort != null ? !sort.equals(user.sort) : user.sort != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = avatar != null ? avatar.hashCode() : 0;
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (nick != null ? nick.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "unreadMsgCount=" + unreadMsgCount +
                ", communityid='" + communityid + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sort='" + sort + '\'' +
                '}';
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
