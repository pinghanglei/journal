package com.qtest.journal.storge;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qtest.journal.model.MessageConstantField;
import com.qtest.journal.model.Share;


public class MessageListDataStorge {

	private SQLiteDatabase database;
	private String tableName;

	public MessageListDataStorge(Context context) {
		database = DataBaseHelper.getInstance(context).getWritableDatabase();
		tableName = DataBaseHelper.MESSAGE_TABLE_NAME;
	}

	public boolean insert(List<Share> messageList) {
		if (database == null || messageList == null || messageList.size() == 0)
			return false;
		database.beginTransaction();
		try {
			database.delete(tableName, null, null);
			int length = messageList.size();
			for (int i = 0; i < length; i++) {
				Share share = messageList.get(i);
				ContentValues value = getRowData(share);
				if (value != null) {
					database.insert(tableName, null, value);
				}
			}
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			database.endTransaction();
		}
		return true;
	}

	/**
	 * 获取数据库保存的最新消息列表 
	 * 
	 * @return
	 */
	public List<Share> getMessageList() {
		List<Share> list = new ArrayList<Share>();
		if (database != null) {
			Cursor cursor = database.query(tableName, null, null, null, null,
					null, null);
			if (cursor == null || cursor.getCount() == 0) {
				return list;
			}
			while (cursor.moveToNext()) {
				Share share = new Share();
				String id = cursor.getString(cursor
						.getColumnIndex(MessageConstantField.ID));
			
				int support = cursor.getInt(cursor
						.getColumnIndex(MessageConstantField.READ));
				int title = cursor.getInt(cursor
						.getColumnIndex(MessageConstantField.TITLE));
				int name = cursor.getInt(cursor
						.getColumnIndex(MessageConstantField.NAME));
				
			
				share.setReadNum(String.valueOf(support));
				share.setName(String.valueOf(name));
				share.setTitle(String.valueOf(title));
				list.add(share);
			}
		}
		return list;
	}

	public ContentValues getRowData(Share share) {
		ContentValues value = new ContentValues();

		value.put(MessageConstantField.ID, share.getId());
		value.put(MessageConstantField.NAME, share.getName());
		value.put(MessageConstantField.READ, share.getReadNum());
		value.put(MessageConstantField.TITLE, share.getReadNum());
		

		return value;

	}

}
