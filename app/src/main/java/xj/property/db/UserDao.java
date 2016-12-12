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
package xj.property.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import xj.property.Constant;
import xj.property.beans.ErrorBean;
import xj.property.cache.XJContact;
import xj.property.domain.User;
import xj.property.ums.common.ErrorSave;

import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;
import com.easemob.util.HanziToPinyin;

public class UserDao {
    public static final String TABLE_NAME = "uers";
    public static final String COLUMN_NAME_ID = "username";
    public static final String COLUMN_NAME_NICK = "nick";
    public static final String COLUMN_NAME_IS_STRANGER = "is_stranger";
    public static final String COLUMN_COMMUNITY_ID = "communityid";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_SORT = "sort";
    private DbOpenHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = DbOpenHelper.getInstance(context);
    }

    /**
     *
     */

    /**
     * 保存好友list
     *
     * @param contactList
     */
    public void saveContactList(List<User> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            //db.delete(TABLE_NAME, null, null);
            for (User user : contactList) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_ID, user.getUsername());
                values.put(COLUMN_AVATAR, user.avatar);
                values.put(COLUMN_COMMUNITY_ID, user.communityid);
                values.put(COLUMN_SORT, user.sort);
                if (user.getNick() != null)
                    values.put(COLUMN_NAME_NICK, user.getNick());
                db.replace(TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 获取好友list
     *
     * @return
     */
    public Map<String, User> getContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, User> users = new HashMap<String, User>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
                User user = new User();
                user.setUsername(username);
                user.setNick(nick);
                user.communityid = cursor.getString(cursor.getColumnIndex(COLUMN_COMMUNITY_ID));
                user.avatar = cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR));
                user.sort = cursor.getString(cursor.getColumnIndex(COLUMN_SORT));
                String headerName = null;
                if (!TextUtils.isEmpty(user.getNick())) {
                    headerName = user.getNick();
                } else {
                    headerName = user.getUsername();
                }
                if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)) {
                    user.setHeader("");
                } else if (Character.isDigit(headerName.charAt(0))) {
                    user.setHeader("#");
                } else {
                    user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1))
                            .get(0).target.substring(0, 1).toUpperCase());
                    char header = user.getHeader().toLowerCase().charAt(0);
                    if (header < 'a' || header > 'z') {
                        user.setHeader("#");
                    }
                }
                users.put(username, user);
            }
            cursor.close();
        }
        return users;
    }

    /**
     * 删除一个联系人
     *
     * @param username
     */
    public void deleteContact(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[]{username});
        }
    }

    /**
     * 保存一个联系人
     *
     * @param user
     */
    public void saveContact(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, user.getUsername());
        if (user.getNick() != null)
            values.put(COLUMN_NAME_NICK, user.getNick());
        values.put(COLUMN_SORT, user.sort);
        values.put(COLUMN_COMMUNITY_ID, user.communityid);
        values.put(COLUMN_AVATAR, user.avatar);
//		if(db.isOpen()){
//	try {
        long i = db.replace(TABLE_NAME, null, values);

        // new XJContact(user.getEid(),user.getAvatar(),user.getNick(),user.sort).save();


//    }catch (Exception e){
//        Log.i("onion","存联系人异常"+e.toString());
//        new ErrorSave(new ErrorBean()).start();
//
//    }

//		}
    }


    public User selectContact(String userid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (!db.isOpen()) return null;
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_NAME_ID + "= ?", new String[]{userid}, null, null, null, "1");
        if (cursor == null) return null;
        if (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
            String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
            User user = new User();
            user.setUsername(username);
            user.setNick(nick);
            user.communityid = cursor.getString(cursor.getColumnIndex(COLUMN_COMMUNITY_ID));
            user.avatar = cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR));
            user.sort = cursor.getString(cursor.getColumnIndex(COLUMN_SORT));
            cursor.close();
            return user;
        }
      /*  else {
            XJContact  contact=   new Select().from(XJContact.class).where("emobid = ? ",userid).executeSingle();
            if(contact!=null) {
                User user = new User();
                user.setUsername(contact.getEmobid());
                user.setNick(contact.getNikename());
                user.avatar = contact.avatar;
                user.sort = contact.sort;
                //return  user;
                return  null;
            }
        }*/
        return null;
    }
}
