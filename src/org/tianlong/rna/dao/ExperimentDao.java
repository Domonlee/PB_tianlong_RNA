package org.tianlong.rna.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tianlong.rna.db.RnaOpenHelper;
import org.tianlong.rna.pojo.DefaultExperiment;
import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.utlis.DBTempletManager;

import org.tianlong.rna.activity.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExperimentDao {
	public SQLiteDatabase sqLiteDatabase;

	// 得到数据库连接
	public RnaOpenHelper getDatebase(Context context) {
		RnaOpenHelper helper = new RnaOpenHelper(context);
		return helper;
	}

	// 查询所有模板实验
	public List<DefaultExperiment> getAllDefaultExperiments(Context context) {
		List<DefaultExperiment> list = new ArrayList<DefaultExperiment>();

		try {
			sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
					DBTempletManager.DB_PATH + "/" + DBTempletManager.DB_NAME,
					null);
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}

		Cursor cursor = sqLiteDatabase.rawQuery(
				"select * from defaultexperiment", null);
		while (cursor.moveToNext()) {
			DefaultExperiment defaultExperiment = new DefaultExperiment();
			defaultExperiment.setE_id(cursor.getInt(cursor
					.getColumnIndex("DE_id")));
			defaultExperiment.setEname(cursor.getString(cursor
					.getColumnIndex("DEname")));
			list.add(defaultExperiment);
		}
		cursor.close();
		sqLiteDatabase.close();
		return list;
	}

	// 根据用户ID查询实验
	public List<Experiment> getAllExperimentsByU_id(Context context, int U_id) {
		List<Experiment> list = new ArrayList<Experiment>();
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.rawQuery(
				"select * from experiment where U_id=" + U_id, null);
		while (cursor.moveToNext()) {
			Experiment experiment = new Experiment();
			experiment.setE_id(cursor.getInt(cursor.getColumnIndex("E_id")));
			experiment
					.setEname(cursor.getString(cursor.getColumnIndex("Ename")));
			experiment
					.setCdate(cursor.getString(cursor.getColumnIndex("Cdate")));
			experiment
					.setRdate(cursor.getString(cursor.getColumnIndex("Rdate")));
			experiment.setEremark(cursor.getString(cursor
					.getColumnIndex("Eremark")));
			experiment
					.setEquick(cursor.getInt(cursor.getColumnIndex("Equick")));
			experiment
					.setEDE_id(cursor.getInt(cursor.getColumnIndex("EDE_id")));
			experiment.setU_id(cursor.getInt(cursor.getColumnIndex("U_id")));
			list.add(experiment);
		}
		cursor.close();
		database.close();
		helper.close();
		return list;
	}

	/**
	 * 
	*  Title: hasEname 
	*  Modified By：  Domon                                        
	*  Modified Date: 2014-9-20 
	*  @param context
	*  @param Ename
	*  @return
	 */
	public boolean hasEname(Context context,String Ename,int U_id){
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from experiment where Ename='" + Ename + "' and U_id="+ U_id, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			return false;
		}
			cursor.close();
		return true;
	}
	
	// 根据实验名称查询实验
	public Experiment getExperimentByEname(String Ename, Context context,
			int U_id) {
		Experiment experiment = null;
		List<Experiment> list = getAllExperimentsByU_id(context, U_id);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getEname().equals(Ename)) {
				experiment = list.get(i);
			}
		}
		return experiment;
	}

	// --添加实验
	public boolean insertExperiment(Experiment experiment, Context context) {
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put("Ename", experiment.getEname());
		values.put("Cdate", experiment.getCdate());
		values.put("Rdate", experiment.getRdate());
		values.put("Eremark", experiment.getEremark());
		values.put("Equick", experiment.getEquick());
		values.put("EDE_id", experiment.getEDE_id());
		values.put("U_id", experiment.getU_id());
		long id = database.insert("experiment", null, values);
		if (id != -1) {
			flag = true;
		} else {
			flag = false;
		}
		database.close();
		helper.close();
		return flag;
	}

	// 修改实验
	public boolean updateExperiment(Experiment experiment, Context context) {
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Cdate", experiment.getCdate());
		values.put("Rdate", experiment.getRdate());
		values.put("Ename", experiment.getEname());
		values.put("Eremark", experiment.getEremark());
		values.put("Equick", experiment.getEquick());
		values.put("EDE_id", experiment.getEDE_id());
		values.put("U_id", experiment.getU_id());
		int id = database.update("experiment", values,
				"E_id = " + experiment.getE_id(), null);
		if (id != -1) {
			flag = true;
		}
		System.out.println(id);
		database.close();
		helper.close();
		return flag;
	}

	// 删除实验
	public void deleteExperiment(int E_id, Context context) {
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		database.delete("experiment", "E_id = " + E_id, null);
		database.close();
		helper.close();
	}

	// 根据创建时间查询实验
	public Experiment getExperimentByCdate(String Cdate, Context context,
			int U_id) {
		Experiment experiment = null;
		List<Experiment> list = getAllExperimentsByU_id(context, U_id);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCdate().equals(Cdate)) {
				experiment = list.get(i);
			}
		}
		return experiment;
	}

	// 根据实验ID查询实验
	public Experiment getExperimentById(int E_id, Context context, int U_id) {
		Experiment experiment = null;
		List<Experiment> list = getAllExperimentsByU_id(context, U_id);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getE_id() == E_id) {
				experiment = list.get(i);
			}
		}
		return experiment;
	}

	// 查询快捷实验
	public List<Map<String, Object>> getExperimentByQuick(Context context,
			int U_id) {
		List<Map<String, Object>> EList = new ArrayList<Map<String, Object>>();
		List<Experiment> list = getAllExperimentsByU_id(context, U_id);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getEquick() == 1) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("image", R.drawable.experiment_iv);
				map.put("info", list.get(i).getEname());
				map.put("id", list.get(i).getE_id());
				EList.add(map);
			}
		}
		return EList;
	}
}
