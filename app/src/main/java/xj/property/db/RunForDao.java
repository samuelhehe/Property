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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.activeandroid.query.Select;

import xj.property.beans.RunForAllV3Bean;
import xj.property.beans.RunForBean;
import xj.property.beans.RunFordbBean;
import xj.property.domain.InviteMessage;

public class RunForDao {
    public static final String TABLE_NAME = "run_for_arrow";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_EMOBID = "emobId";
    public static final String COLUMN_RANK = "rank";
	private DbOpenHelper dbHelper;

	public RunForDao(Context context){
		dbHelper = DbOpenHelper.getInstance(context);
	}
	
	/**
	 * 保存message
	 * @param message
	 * @return  返回这条messaged在db中的id
	 */
	public synchronized void saveMessage(RunForBean message){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put(COLUMN_EMOBID, message.getEmobId());
			values.put(COLUMN_RANK, message.getRank());
			db.insert(TABLE_NAME, null, values);
		}
	}
	
	/**
	 * 更新message
	 * @param emobId
	 * @param values
	 */
	public void updateEmob(String emobId,ContentValues values){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.update(TABLE_NAME, values, COLUMN_EMOBID + " = ?", new String[]{String.valueOf(emobId)});
		}
	}

    /**
     * 取得对应uid的值。
     * @param uid 表里的某个uid
     */
    public RunForBean query(String uid){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        RunForBean runForBean = new RunForBean();
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+COLUMN_EMOBID+"=\'"+uid+"\'", null);
            while(cursor.moveToNext()){
                InviteMessage msg = new InviteMessage();
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
                String from = cursor.getString(cursor.getColumnIndex(COLUMN_EMOBID));
                String groupid = cursor.getString(cursor.getColumnIndex(COLUMN_RANK));
                runForBean = new RunForBean();
                runForBean.setEmobId(from);
                runForBean.setRank(Integer.parseInt(groupid));
            }
            cursor.close();
        }
        return runForBean;
    }


	public static RunFordbBean getRunFordbBean(String emobId) {
		return new Select()
				.from(RunFordbBean.class)
				.where("emobId = ?", emobId)
				.executeSingle();
	}


	public static void saveRunFordbBean(RunForAllV3Bean.RunForDataV3Bean runForBean) {
		RunFordbBean item = new RunFordbBean();
		item.setEmobId(runForBean.getEmobId());
		item.setRank(runForBean.getRank());
		item.save();
	}

	public static void updateRunFordbBean(RunFordbBean runFordbBean){
		runFordbBean.save();
	}

//	public static  void updateRunFordbBean(){
//		new Update(RunFordbBean.class).where("",).set("read_status = ?", "no").execute();
//	}



    public void deleteMessage(String from){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_EMOBID + " = ?", new String[]{from});
		}
	}
}
