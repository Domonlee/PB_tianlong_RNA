package org.tianlong.rna.utlis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.tianlong.rna.activity.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * Import Templet Database
 * @date 2014-9-17 
 * @author Domon
 */
@SuppressLint({ "HandlerLeak", "UseValueOf" })
public class DBTempletManager {

	private final int BUFFER_SIZE = 400000;
	public static final String DB_NAME = "rnatemplet.db"; // ��������ݿ��ļ���
	public static final String PACKAGE_NAME = "org.tianlong.rna.activity";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME + "/databases";

	private SQLiteDatabase database;
	private Context context;

	public DBTempletManager(Context context) {
		this.context = context;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	public void openDatabase() {
		System.out.println(DB_PATH + "/" + DB_NAME);
		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}

	private SQLiteDatabase openDatabase(String dbfile) {

		try {
			File fileDir = new File(DB_PATH);
			if (!(fileDir.exists())) {
				fileDir.mkdirs();
			}
			
			File file = new File(dbfile); 

			if (!(file.exists())) {
				// �ж����ݿ��ļ��Ƿ���ڣ�����������ִ�е��룬����ֱ�Ӵ����ݿ�
				file.createNewFile();
				InputStream is = this.context.getResources().openRawResource(
						R.raw.rnatemplet); // ����������ݿ�
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}

			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
					null);
			return db;

		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
		return null;
	}

	public void closeDatabase() {
		this.database.close();
	}

}
