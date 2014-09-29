package org.tianlong.rna.utlis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.tianlong.rna.activity.MachineActivity;
import org.tianlong.rna.activity.R;
import org.tianlong.rna.activity.UpAndDownActivity;
import org.tianlong.rna.activity.UpAndDownActivity.DownAdapter;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Import Templet Database
 * 
 * @date 2014-9-17
 * @author Domon
 */
@SuppressLint({ "HandlerLeak", "UseValueOf" })
public class MachineStateInfo {

	private Context context;
	private WifiUtlis wifiUtlis;
	private List<String> receive;
	private String receiveMeg; // 接收信息
	private static String TAG = "MACHINE";
	int j = 0;
	public boolean flag = true;
	TextView expetiment_left_new_btn;

	private SelectInfoThread selectInfoThread = new SelectInfoThread();

	public MachineStateInfo(Context context) {
		Log.w(TAG, "Machine State Info");
		this.context = context;
	}

	public MachineStateInfo(Context context,TextView expetiment_left_new_btn) {
		Log.w(TAG, "Machine State Info");
		this.context = context;
		this.expetiment_left_new_btn = expetiment_left_new_btn;
	}

	public String queryState() {
		String str = null;
		try {
			Log.w(TAG, "Machine queryState");
			wifiUtlis = new WifiUtlis(context);
			wifiUtlis.sendMessage(Utlis.getseleteMessage(6));
			printHexString(Utlis.getseleteMessage(6));
			new Thread(selectInfoThread).start();
		} catch (Exception e) {
			Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
		}
		str = wifiUtlis.getMessage();
		Log.w("getMessage string--", str);
		return str;
	}

	class SelectInfoThread implements Runnable {
		public void run() {
			while (flag) {
				try {
					Log.w(TAG, "Machine query Thread");
					Message message = selectInfoHandler.obtainMessage();
					message.obj = wifiUtlis.getByteMessage();
					selectInfoHandler.sendMessage(message);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Handler selectInfoHandler = new Handler() {
		public void handleMessage(Message msg) {
			byte[] info = (byte[]) msg.obj;
			Log.w(TAG, "Machine query Handler");
			if (info.length != 0) {
				if (receive != null) {
					receive.removeAll(receive);
				}
				receive = Utlis.getReceive(info);
				for (int i = 0; i < receive.size(); i++) {
					receiveMeg = receive.get(i);
					// 00 00 为关闭
						 Log.w("receiveSubString--", receiveMeg.substring(21, 23));
					 if (receiveMeg.substring(21, 23).equals("00")) {
						 Log.w("receiveSubString--", receiveMeg.substring(24, 26));
						 if (receiveMeg.substring(24, 26).equals("00")) {
							 j = 1;
							 expetiment_left_new_btn.setText(""+j);
						}
						 //01 00 为运行一次后 关闭
					 }else if (receiveMeg.substring(21, 23).equals("01")) {
						 if (receiveMeg.substring(24, 26).equals("00")) {
							 j = 1;
							 expetiment_left_new_btn.setText(""+j);
						}
					}
					Log.w("receiveString--", receiveMeg + "");
				}
				flag =false;
			}
		};
	};
	
	public static void printHexString(byte[] b) {
		String hexString = null;
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString = hex + " " + hexString;
		}
		Log.w("HexString--", hexString);
	}
}
