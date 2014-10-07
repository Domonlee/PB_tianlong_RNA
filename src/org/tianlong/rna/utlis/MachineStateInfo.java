package org.tianlong.rna.utlis;

import java.util.List;

import org.tianlong.rna.activity.RunExperimentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
	private String stateString = "1";
	private static String TAG = "MACHINE";
	private static String STATE_STOP = "stop";
	private static String STATE_RUN = "run";
	private static String STATE_PAUSE = "pause";

	public boolean flag = true;
	TextView expetiment_left_new_btn;

	private SelectStateThread selectInfoThread;

	public MachineStateInfo(Context context) {
		this.context = context;
	}

	public MachineStateInfo(Context context, TextView expetiment_left_new_btn) {
		this.context = context;
		this.expetiment_left_new_btn = expetiment_left_new_btn;
	}

	public String queryState() {
		String str = null;
		selectInfoThread = new SelectStateThread();
		try {
			// Log.w(TAG, "Machine queryState");
			wifiUtlis = new WifiUtlis(context);
			wifiUtlis.sendMessage(Utlis.getseleteMessage(6));
			printHexString(Utlis.getseleteMessage(6));
			new Thread(selectInfoThread).start();
		} catch (Exception e) {
			Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
		}
		str = wifiUtlis.getMessage();
		// str = stateString;
		if (flag == false) {
			return stateString;
		}
		return stateString + "true";
	}

	public String getState() {

		return stateString;
	}

	class SelectStateThread implements Runnable {
		public void run() {
			while (flag) {
				try {
					Log.w(TAG, "Machine query Thread");
					Message message = selectInfoHandler.obtainMessage();
					message.obj = wifiUtlis.getByteMessage();
					selectInfoHandler.sendMessage(message);
					Thread.sleep(2000);
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
						Log.w("receiveSubString--",
								receiveMeg.substring(24, 26));
						if (receiveMeg.substring(24, 26).equals("00")) {
							stateString = STATE_STOP;

						}
						if (receiveMeg.substring(24, 26).equals("03")) {
							stateString = STATE_RUN;
						}
						if (receiveMeg.substring(24, 26).equals("04")) {
							stateString = STATE_PAUSE;
						}
						if (receiveMeg.substring(24, 26).equals("05")) {
							stateString = STATE_STOP;
						}
						// 01 00 为运行一次后 关闭
					} else if (receiveMeg.substring(21, 23).equals("01")) {
						if (receiveMeg.substring(24, 26).equals("00")) {
							stateString = STATE_STOP;
						}
						if (receiveMeg.substring(24, 26).equals("03")) {
							stateString = STATE_RUN;
						}
						if (receiveMeg.substring(24, 26).equals("04")) {
							stateString = STATE_PAUSE;
						}
						if (receiveMeg.substring(24, 26).equals("05")) {
							stateString = STATE_STOP;
						}
					}
				}
//				RunExperimentActivity runExperimentActivity = new RunExperimentActivity();
//				runExperimentActivity.setStateString(stateString);
				expetiment_left_new_btn.setText(stateString);
				flag = false;
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
