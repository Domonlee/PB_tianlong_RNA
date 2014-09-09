package org.tianlong.rna.dao;

import java.util.ArrayList;
import java.util.List;
import org.tianlong.rna.db.RnaOpenHelper;
import org.tianlong.rna.pojo.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
	
	//�õ����ݿ�����
	public RnaOpenHelper getDatebase(Context context){
		RnaOpenHelper helper = new RnaOpenHelper(context);
		return helper;
	}
	
	//��ѯ�����û�
	public List<User> getAllUser(Context context){
		List<User> userList = new ArrayList<User>();
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from user", null);
		while (cursor.moveToNext()) {
			User user = new User();
			user.setU_id(cursor.getInt(cursor.getColumnIndex("U_id")));
			user.setUname(cursor.getString(cursor.getColumnIndex("Uname")));
			user.setUpass(cursor.getString(cursor.getColumnIndex("Upass")));
			user.setUadmin(cursor.getInt(cursor.getColumnIndex("Uadmin")));
			user.setUdefault(cursor.getInt(cursor.getColumnIndex("Udefault")));
			userList.add(user);
		}
		cursor.close();
		database.close();
		helper.close();
		return userList;
	}
	
	//��ѯ���й���Ա�û�
	public List<User> getAllUserByAdmin(Context context){
		List<User> list = getAllUser(context);
		List<User> uList = new ArrayList<User>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getUadmin() != 1){
				uList.add(list.get(i));
			}
		}
		return uList;
	}
	
	//ͨ���û�ID��ѯ�û�
	public User getUserById(int U_id,Context context){
		User user = null;
		List<User> list = getAllUser(context);
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getU_id() == U_id){
				user = list.get(i);
				break;
			}
		}
		return user;
	}
	
	//��ѯĬ�ϵ�¼�û�
	public User getDefaultUser(Context context){
		User user = null;
		List<User> ulist = getAllUser(context);
		for (int i = 0; i < ulist.size(); i++) {
			if(ulist.get(i).getUdefault() == 1){
				user = ulist.get(i);
			}
		}
		return user;
	}
	
	//���û����������ѯ�û�
	public User getUserByUnameAndUpass(String Uname,String Upass,Context context){
		//����User��Ķ���user
		User user = null;
		//������Ų�ѯ�����û���Ϣ�б�
		List<User> uList = getAllUser(context);
		//����uList
		for (int i = 0; i < uList.size(); i++) {
			//�ж������е�user��Ϣ�Ƿ��кͲ���Uname��Upass��ͬ��user����
			if(uList.get(i).getUname().equals(Uname)&&uList.get(i).getUpass().equals(Upass)){
				user = uList.get(i);
			}
		}
		//���ز鵽��user����
		return user;
	}
	
	//���û�����ѯ�û�
	public boolean getUserByUname(String Uname, Context context){
		//����boolean���ͱ�����ʾ��ѯ�Ƿ�ɹ�
		boolean flag = false;
		//������Ų�ѯ�����û���Ϣ�б�
		List<User> uList = getAllUser(context);
		//����uList
		for (int i = 0; i < uList.size(); i++) {
			//�ж������е�user��Ϣ�Ƿ��кͲ���Uname
			if(uList.get(i).getUname().equals(Uname)){
				//��boolean���ͱ���ֵ��Ϊture
				flag = true;
			}
		}
		//���ز�ѯ���
		return flag;
	}
	
	//���û�����ѯ�û������ظ��û�
	public User getUserByRUname(String Uname, Context context){
		//����User����
		User user = new User();
		//������Ų�ѯ�����û���Ϣ�б�
		List<User> uList = getAllUser(context);
		//����uList
		for (int i = 0; i < uList.size(); i++) {
			//�ж������е�user��Ϣ�Ƿ��кͲ���Uname
			if(uList.get(i).getUname().equals(Uname)){
				//��boolean���ͱ���ֵ��Ϊture
				user = uList.get(i);
			}
		}
		//���ز�ѯ���
		return user;
	}
	
	//����û�
	public boolean insertUser(User user,Context context){
		if(user.getUdefault() == 1){
			updateUserDefault(context);
		}
		boolean flag = false;
		//���������û���Ϣ�б�
		List<User> uList = getAllUser(context);
		//���������û���Ϣ�б�
		for (int i = 0; i < uList.size(); i++) {
			//�ж������û��б����Ƿ��в����û���Ϣ
			if(uList.get(i)==user||uList.get(i).equals(user)){
				//����н�������Ϊtrue
				flag = true;
			}
		}
		//�ж������û��б����Ƿ��в����û���Ϣ���û�����������û���Ϣ
		if(!flag){
			RnaOpenHelper helper = getDatebase(context);
			SQLiteDatabase database = helper.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put("Uname", user.getUname());
			values.put("Upass", user.getUpass());
			values.put("Uadmin", user.getUadmin());
			values.put("Udefault", user.getUdefault());
			//��values����������ݿ��user���еõ�״̬longֵ
			long id =database.insert("user", null, values);
			//�ж�longֵ�Ƿ�Ϊ-1(���Ϊ-1����벻�ɹ�)
			if(id != -1){
				//��booleanֵ��Ϊtrue
				flag = true;
			}
			//�ر����ݿ����
			database.close();
			//�ر����ݿ����Ӷ���
			helper.close();
		}
		//�����½�״̬
		return flag;
	}
	
	//�޸��û���Ϣ
	public boolean updateUser(User user,Context context){
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("Uname", user.getUname());
		values.put("Upass", user.getUpass());
		values.put("Uadmin", user.getUadmin());
		values.put("Udefault", user.getUdefault());
		int id = database.update("user", values, "U_id="+user.getU_id(), null);
		//�ж��޸ķ���ֵ�Ƿ�Ϊ-1(���Ϊ-1˵���޸Ĳ��ɹ�)
		if(id != -1){
			//��booleanֵ��Ϊtrue
			flag = true;
		}
		//�ر����ݿ����
		database.close();
		//�ر����ݿ����Ӷ���
		helper.close();
		//�����޸�״̬
		return flag;
	}
	
	//�޸��û�Ĭ�ϵ�¼��Ϣ
	public boolean updateDefault(int defaultNum,int U_id,Context context){
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("Udefault", defaultNum);
		updateUserDefault(context);
		int id = database.update("user", values, "U_id="+U_id, null);
		//�ж��޸ķ���ֵ�Ƿ�Ϊ-1(���Ϊ-1˵���޸Ĳ��ɹ�)
		if(id != -1){
			//��booleanֵ��Ϊtrue
			flag = true;
		}
		//�ر����ݿ����
		database.close();
		//�ر����ݿ����Ӷ���
		helper.close();
		//�����޸�״̬
		return flag;
	}
	//�޸��û���Ϣ
	public boolean updatePassword(User user,Context context){
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("Upass", user.getUpass());
		updateUserDefault(context);
		int id = database.update("user", values, "U_id="+user.getU_id(), null);
		//�ж��޸ķ���ֵ�Ƿ�Ϊ-1(���Ϊ-1˵���޸Ĳ��ɹ�)
		if(id != -1){
			//��booleanֵ��Ϊtrue
			flag = true;
		}
		//�ر����ݿ����
		database.close();
		//�ر����ݿ����Ӷ���
		helper.close();
		//�����޸�״̬
		return flag;
	}
	//�޸�ȫ��Ĭ�ϵ�¼�û�
	private void updateUserDefault(Context context){
		List<User> list = getAllUser(context);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setUdefault(0);
			updateUser(list.get(i), context);
		}
	}
	
	//ɾ���û�
	public void deleteUser(User user,Context context){
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		database.delete("user","U_id="+user.getU_id(), null);
		database.close();
		helper.close();
	}
}
