package org.tianlong.rna.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RnaOpenHelper extends SQLiteOpenHelper {

	private static final String DBNAME = "rna.db";
	private static final int VERSION = 1;

	public RnaOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table user(U_id integer primary key autoincrement,Uname varchar(32),Upass varchar(32),Udefault integer,Uadmin integer)");
		db.execSQL("create table experiment(E_id integer primary key autoincrement,Ename varchar(32),Cdate varchar(32),Rdate varchar(32),Eremark varchar(50),Equick integer,EDE_id integer,U_id integer)");
		db.execSQL("create table defaultexperiment(DE_id integer primary key autoincrement,DEname varchar(32))");
		db.execSQL("create table step(S_id integer primary key autoincrement,Sname varchar(32),Shole integer,Sspeed integer,Svol integer,Swait varchar(32),Sblend varchar(32),Smagnet varchar(32),Sswitch integer,Stemp integer,E_id integer)");
//		db.execSQL("create table defaultstep(DS_id integer primary key autoincrement,DSname varchar(32),DShole integer,DSspeed integer,DSvol integer,DSwait varchar(32),DSblend varchar(32),DSmagnet varchar(32),DSswitch integer,DStemp integer,DE_id integer)");
		db.execSQL("create table defaultstep(DS_id integer primary key autoincrement,DSname varchar(32),DStemplet integer,DShole integer,DSspeed integer,DSvol integer,DSwait varchar(32),DSblend varchar(32),DSmagnet varchar(32),DSswitch integer,DStemp integer,DE_id integer)");
		db.execSQL("create table machine(M_id integer primary key autoincrement,Mlanguage integer,Mname varchar(32),Mip varchar(32),Mmac varchar(32),Mgateway varchar(32),Mmask varchar(32),MDtime varchar(32),Mflux integer,Mblend float,Mmagnet float,Mhole float)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
