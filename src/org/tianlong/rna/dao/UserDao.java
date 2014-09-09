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
	
	//得到数据库连接
	public RnaOpenHelper getDatebase(Context context){
		RnaOpenHelper helper = new RnaOpenHelper(context);
		return helper;
	}
	
	//查询所有用户
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
	
	//查询所有管理员用户
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
	
	//通过用户ID查询用户
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
	
	//查询默认登录用户
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
	
	//按用户名和密码查询用户
	public User getUserByUnameAndUpass(String Uname,String Upass,Context context){
		//建立User类的对象user
		User user = null;
		//建立存放查询到的用户信息列表
		List<User> uList = getAllUser(context);
		//遍历uList
		for (int i = 0; i < uList.size(); i++) {
			//判断数组中的user信息是否有和参数Uname和Upass相同的user对象
			if(uList.get(i).getUname().equals(Uname)&&uList.get(i).getUpass().equals(Upass)){
				user = uList.get(i);
			}
		}
		//返回查到的user对象
		return user;
	}
	
	//按用户名查询用户
	public boolean getUserByUname(String Uname, Context context){
		//创建boolean类型变量显示查询是否成功
		boolean flag = false;
		//建立存放查询到的用户信息列表
		List<User> uList = getAllUser(context);
		//遍历uList
		for (int i = 0; i < uList.size(); i++) {
			//判断数组中的user信息是否有和参数Uname
			if(uList.get(i).getUname().equals(Uname)){
				//将boolean类型变量值改为ture
				flag = true;
			}
		}
		//返回查询结果
		return flag;
	}
	
	//按用户名查询用户并返回该用户
	public User getUserByRUname(String Uname, Context context){
		//创建User对象
		User user = new User();
		//建立存放查询到的用户信息列表
		List<User> uList = getAllUser(context);
		//遍历uList
		for (int i = 0; i < uList.size(); i++) {
			//判断数组中的user信息是否有和参数Uname
			if(uList.get(i).getUname().equals(Uname)){
				//将boolean类型变量值改为ture
				user = uList.get(i);
			}
		}
		//返回查询结果
		return user;
	}
	
	//添加用户
	public boolean insertUser(User user,Context context){
		if(user.getUdefault() == 1){
			updateUserDefault(context);
		}
		boolean flag = false;
		//创建所有用户信息列表
		List<User> uList = getAllUser(context);
		//遍历所有用户信息列表
		for (int i = 0; i < uList.size(); i++) {
			//判断所有用户列表中是否含有参数用户信息
			if(uList.get(i)==user||uList.get(i).equals(user)){
				//如果有将变量改为true
				flag = true;
			}
		}
		//判断所有用户列表中是否含有参数用户信息如果没有则插入参数用户信息
		if(!flag){
			RnaOpenHelper helper = getDatebase(context);
			SQLiteDatabase database = helper.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put("Uname", user.getUname());
			values.put("Upass", user.getUpass());
			values.put("Uadmin", user.getUadmin());
			values.put("Udefault", user.getUdefault());
			//将values对象插入数据库的user表中得到状态long值
			long id =database.insert("user", null, values);
			//判断long值是否为-1(如果为-1则插入不成功)
			if(id != -1){
				//将boolean值改为true
				flag = true;
			}
			//关闭数据库对象
			database.close();
			//关闭数据库连接对象
			helper.close();
		}
		//返回新建状态
		return flag;
	}
	
	//修改用户信息
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
		//判断修改返回值是否为-1(如果为-1说明修改不成功)
		if(id != -1){
			//将boolean值改为true
			flag = true;
		}
		//关闭数据库对象
		database.close();
		//关闭数据库连接对象
		helper.close();
		//返回修改状态
		return flag;
	}
	
	//修改用户默认登录信息
	public boolean updateDefault(int defaultNum,int U_id,Context context){
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("Udefault", defaultNum);
		updateUserDefault(context);
		int id = database.update("user", values, "U_id="+U_id, null);
		//判断修改返回值是否为-1(如果为-1说明修改不成功)
		if(id != -1){
			//将boolean值改为true
			flag = true;
		}
		//关闭数据库对象
		database.close();
		//关闭数据库连接对象
		helper.close();
		//返回修改状态
		return flag;
	}
	//修改用户信息
	public boolean updatePassword(User user,Context context){
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("Upass", user.getUpass());
		updateUserDefault(context);
		int id = database.update("user", values, "U_id="+user.getU_id(), null);
		//判断修改返回值是否为-1(如果为-1说明修改不成功)
		if(id != -1){
			//将boolean值改为true
			flag = true;
		}
		//关闭数据库对象
		database.close();
		//关闭数据库连接对象
		helper.close();
		//返回修改状态
		return flag;
	}
	//修改全部默认登录用户
	private void updateUserDefault(Context context){
		List<User> list = getAllUser(context);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setUdefault(0);
			updateUser(list.get(i), context);
		}
	}
	
	//删除用户
	public void deleteUser(User user,Context context){
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		database.delete("user","U_id="+user.getU_id(), null);
		database.close();
		helper.close();
	}
}
