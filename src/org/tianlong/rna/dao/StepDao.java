package org.tianlong.rna.dao;

import java.util.ArrayList;
import java.util.List;

import org.tianlong.rna.db.RnaOpenHelper;
import org.tianlong.rna.pojo.DefaultStep;
import org.tianlong.rna.pojo.Step;
import org.tianlong.rna.utlis.DBTempletManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StepDao {
	private SQLiteDatabase sqLiteDatabase;

	// 得到数据库连接
	public RnaOpenHelper getDatebase(Context context) {
		RnaOpenHelper helper = new RnaOpenHelper(context);
		return helper;
	}

	// 查询所有步骤
	public List<Step> getAllStep(Context context, int E_id) {
		List<Step> steps = new ArrayList<Step>();
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from step where E_id="
				+ E_id, null);
		while (cursor.moveToNext()) {
			Step step = new Step();
			step.setS_id(cursor.getInt(cursor.getColumnIndex("S_id")));
			step.setSname(cursor.getString(cursor.getColumnIndex("Sname")));
			step.setShole(cursor.getInt(cursor.getColumnIndex("Shole")));
			step.setSspeed(cursor.getInt(cursor.getColumnIndex("Sspeed")));
			step.setSvol(cursor.getInt(cursor.getColumnIndex("Svol")));
			step.setSwait(cursor.getString(cursor.getColumnIndex("Swait")));
			step.setSblend(cursor.getString(cursor.getColumnIndex("Sblend")));
			step.setSmagnet(cursor.getString(cursor.getColumnIndex("Smagnet")));
			step.setSswitch(cursor.getInt(cursor.getColumnIndex("Sswitch")));
			step.setStemp(cursor.getInt(cursor.getColumnIndex("Stemp")));
			step.setE_id(cursor.getInt(cursor.getColumnIndex("E_id")));
			steps.add(step);
		}
		cursor.close();
		database.close();
		helper.close();
		return steps;
	}

	// 添加步骤
	public boolean insertStep(Step step, Context context) {
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Sname", step.getSname());
		values.put("Shole", step.getShole());
		values.put("Sspeed", step.getSspeed());
		values.put("Svol", step.getSvol());
		values.put("Swait", step.getSwait());
		values.put("Sblend", step.getSblend());
		values.put("Smagnet", step.getSmagnet());
		values.put("Sswitch", step.getSswitch());
		values.put("Stemp", step.getStemp());
		values.put("E_id", step.getE_id());
		long id = database.insert("step", null, values);
		if (id != -1) {
			flag = true;
		}
		database.close();
		helper.close();
		return flag;
	}

	// 修改步骤
	public boolean updateStep(Step step, Context context) {
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Sname", step.getSname());
		values.put("Shole", step.getShole());
		values.put("Sspeed", step.getSspeed());
		values.put("Svol", step.getSvol());
		values.put("Swait", step.getSwait());
		values.put("Sblend", step.getSblend());
		values.put("Smagnet", step.getSmagnet());
		values.put("Sswitch", step.getSswitch());
		values.put("Stemp", step.getStemp());
		values.put("E_id", step.getE_id());
		long id = database.update("step", values, "S_id=" + step.getS_id(),
				null);
		if (id != -1) {
			flag = true;
		}
		database.close();
		helper.close();
		return flag;
	}

	// 删除步骤
	public boolean deleteStep(Step step, Context context) {
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		long id = database.delete("step", "E_id=" + step.getE_id(), null);
		if (id != -1) {
			flag = true;
		}
		database.close();
		helper.close();
		return flag;
	}

	// 查询所有模板步骤
	public List<DefaultStep> getAllDStep(Context context, int DE_id,int fluxNum) {
		List<DefaultStep> dsteps = new ArrayList<DefaultStep>();
		sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DBTempletManager.DB_PATH
				+ "/" + DBTempletManager.DB_NAME, null);
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select * from defaultstep3 where DE_id=" + DE_id + " and DStemplet =" + fluxNum, null);
		while (cursor.moveToNext()) {
			DefaultStep step = new DefaultStep();
			step.setDS_id(cursor.getInt(cursor.getColumnIndex("DS_id")));
			step.setDSname(cursor.getString(cursor.getColumnIndex("DSname")));
			step.setDShole(cursor.getInt(cursor.getColumnIndex("DShole")));
			step.setDSspeed(cursor.getInt(cursor.getColumnIndex("DSspeed")));
			step.setDSvol(cursor.getInt(cursor.getColumnIndex("DSvol")));
			step.setDSwait(cursor.getString(cursor.getColumnIndex("DSwait")));
			step.setDSblend(cursor.getString(cursor.getColumnIndex("DSblend")));
			step.setDSmagnet(cursor.getString(cursor.getColumnIndex("DSmagnet")));
			step.setDSswitch(cursor.getInt(cursor.getColumnIndex("DSswitch")));
			step.setDStemp(cursor.getInt(cursor.getColumnIndex("DStemp")));
			step.setDStemplet(cursor.getInt(cursor.getColumnIndex("DStemplet")));
			step.setDE_id(cursor.getInt(cursor.getColumnIndex("DE_id")));
			dsteps.add(step);
		}
		cursor.close();
		sqLiteDatabase.close();
		return dsteps;
	}

	// 添加模板步骤,现在靠DB导入，不需要再使用方法。
//	public boolean insertDStep(DefaultStep step, Context context) {
//		boolean flag = false;
//		RnaOpenHelper helper = getDatebase(context);
//		SQLiteDatabase database = helper.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put("DSname", step.getDSname());
//		values.put("DShole", step.getDShole());
//		values.put("DSspeed", step.getDSspeed());
//		values.put("DSvol", step.getDSvol());
//		values.put("DSwait", step.getDSwait());
//		values.put("DSblend", step.getDSblend());
//		values.put("DSmagnet", step.getDSmagnet());
//		values.put("DSswitch", step.getDSswitch());
//		values.put("DStemp", step.getDStemp());
//		values.put("DStemplet", step.getDStemplet());
//		values.put("DE_id", step.getDE_id());
//		long id = database.insert("defaultstep", null, values);
//		if (id != -1) {
//			flag = true;
//		}
//		database.close();
//		helper.close();
//		return flag;
//	}
}
