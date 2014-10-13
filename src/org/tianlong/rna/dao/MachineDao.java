package org.tianlong.rna.dao;

import org.tianlong.rna.db.RnaOpenHelper;
import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.pojo.Instrument;
import org.tianlong.rna.pojo.Machine;
import org.tianlong.rna.pojo.Net;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MachineDao {
	//得到数据库连接
	public RnaOpenHelper getDatebase(Context context){
		RnaOpenHelper helper = new RnaOpenHelper(context);
		return helper;
	}
	
	public Machine getMachine(Context context){
		Machine machine = null;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from machine", null);
		while (cursor.moveToNext()) {
			machine = new Machine();
			machine.setM_id(cursor.getInt(cursor.getColumnIndex("M_id")));
			machine.setMlanguage(cursor.getInt(cursor.getColumnIndex("Mlanguage")));
			machine.setMflux(cursor.getInt(cursor.getColumnIndex("Mflux")));
			machine.setMblend(cursor.getFloat(cursor.getColumnIndex("Mblend")));
			machine.setMmagnet(cursor.getFloat(cursor.getColumnIndex("Mmagnet")));
			machine.setMhole(cursor.getFloat(cursor.getColumnIndex("Mhole")));
			machine.setMname(cursor.getString(cursor.getColumnIndex("Mname")));
			machine.setMip(cursor.getString(cursor.getColumnIndex("Mip")));
			machine.setMmac(cursor.getString(cursor.getColumnIndex("Mmac")));
			machine.setMgateway(cursor.getString(cursor.getColumnIndex("Mgateway")));
			machine.setMmask(cursor.getString(cursor.getColumnIndex("Mmask")));
			machine.setMDtime(cursor.getString(cursor.getColumnIndex("MDtime")));
			machine.setMholeSpace(cursor.getInt(cursor.getColumnIndex("MholeSpace")));
		}
		cursor.close();
		database.close();
		helper.close();
		return machine;
	}
	
	public void insertMachine(Context context, Machine machine){
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Mlanguage", machine.getMlanguage());
		values.put("Mflux", machine.getMflux());
		values.put("Mblend", machine.getMblend());
		values.put("Mmagnet", machine.getMmagnet());
		values.put("Mhole", machine.getMhole());
		values.put("Mname", machine.getMname());
		values.put("Mip", machine.getMip());
		values.put("Mmac", machine.getMmac());
		values.put("Mgateway", machine.getMgateway());
		values.put("Mmask", machine.getMmask());
		values.put("MDtime", machine.getMDtime());
		values.put("MholeSpace", machine.getMholeSpace());
		database.insert("machine", null, values);
		database.close();
		helper.close();
	}
	
	public void updateMachineFlux(Machine machine,Context context,int fluxNum){
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		machine.setMflux(fluxNum);
		values.put("Mflux",machine.getMflux());
		database.update("machine", values,
				"M_id = 1", null);
		database.close();
		helper.close();
	}
	
	public boolean upDateLanguage(int language,Context context){
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Mlanguage", language);
		int id = database.update("machine", values, null, null);
		if(id != -1){
			flag = true;
		}
		database.close();
		helper.close();
		return flag;
	}
	
	//TODO 更新消毒时间
	public boolean upDateDisinfectTime(String time,Context context){
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("MDtime", time);
		int id = database.update("machine", values, null, null);
		if(id != -1){
			flag = true;
		}
		database.close();
		helper.close();
		return flag;
	}
	
	public boolean upDateNet(Net net,Context context){
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Mname", net.getMname());
		values.put("Mip", net.getMip());
		values.put("Mmac", net.getMmac());
		values.put("Mgateway", net.getMgateway());
		values.put("Mmask", net.getMmask());
		int id = database.update("machine", values, null, null);
		if(id != -1){
			flag = true;
		}
		database.close();
		helper.close();
		return flag;
	}
	
	public boolean upDateInstrument(Instrument instrument,Context context){
		boolean flag = false;
		RnaOpenHelper helper = getDatebase(context);
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Mflux", instrument.getFlux());
		values.put("Mblend", instrument.getBlend());
		values.put("Mmagnet", instrument.getMagnet());
		values.put("Mhole", instrument.getHole());
		int id = database.update("machine", values, null, null);
		if(id != -1){
			flag = true;
		}
		database.close();
		helper.close();
		return flag;
	}
}
