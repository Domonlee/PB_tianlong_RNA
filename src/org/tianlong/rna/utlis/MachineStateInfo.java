package org.tianlong.rna.utlis;

import java.util.List;

import org.tianlong.rna.activity.RunExperimentActivity;
import org.tianlong.rna.activity.R.string;

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
	private static WifiUtlis wifiUtlis;
	private List<String> receive;
	private String receiveMeg; // 接收信息
	private int stateNo;
	private String perfixString;
	private static String TAG = "MACHINE";
	private static int STATE_STOP = 5;
	private static int STATE_RUN = 3;
	private static int STATE_PAUSE = 4;

	public boolean runflag = true;
	public boolean pauseflag = true;
	public TextView experiment_run_top_mstate_tv;
	private SelectStateThread selectInfoThread;

	public MachineStateInfo(Context context) {
		this.context = context;
	}

	public MachineStateInfo(Context context, TextView expetiment_left_new_btn) {
		this.context = context;
		this.experiment_run_top_mstate_tv = expetiment_left_new_btn;
	}

	public static synchronized String sendMessageSyn(){
		return wifiUtlis.getMessage();	
	}
	public void init() {
		perfixString = "仪器当前状态：";
		selectInfoThread = new SelectStateThread();
		try {
			wifiUtlis = new WifiUtlis(context);
		} catch (Exception e) {
			Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
		}
		new Thread(selectInfoThread).start();
	}

	public void queryState() {
		init();
	}

	// class SelectStateThread implements Runnable {
	// public void run() {
	// while (flag) {
	// try {
	// Log.w(TAG, "Machine query Thread");
	// Message message = selectInfoHandler.obtainMessage();
	// message.obj = wifiUtlis.getByteMessage();
	// selectInfoHandler.sendMessage(message);
	// Thread.sleep(1000);
	// wifiUtlis.sendMessage(Utlis.getseleteMessage(6));
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }

	class SelectStateThread implements Runnable {
		public void run() {
			while (runflag) {
				while (pauseflag) {
					try {
						Log.w(TAG, "Machine query Thread");
						Message message = selectInfoHandler.obtainMessage();
						message.obj = wifiUtlis.getByteMessage();
						selectInfoHandler.sendMessage(message);
						Thread.sleep(1000);
						wifiUtlis.sendMessage(Utlis.getseleteMessage(6));
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(1000);
						Log.w(TAG+"---", "Machine query run Thread");
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
					if (receiveMeg.substring(24, 26).equals("00")) {
						stateNo = STATE_STOP;
						experiment_run_top_mstate_tv.setText(perfixString
								+ getStateString(stateNo));
					}
					if (receiveMeg.substring(24, 26).equals("03")) {
						stateNo = STATE_RUN;
						experiment_run_top_mstate_tv.setText(perfixString
								+ getStateString(stateNo));
					}
					if (receiveMeg.substring(24, 26).equals("04")) {
						stateNo = STATE_PAUSE;
						experiment_run_top_mstate_tv.setText(perfixString
								+ getStateString(stateNo));
					}
					if (receiveMeg.substring(24, 26).equals("05")) {
						stateNo = STATE_STOP;
						experiment_run_top_mstate_tv.setText(perfixString
								+ getStateString(stateNo));
					}
				}
			}
		};
	};

	public String getStateString(int stateNo) {
		String str = null;
		switch (stateNo) {
		case 3:
			str = "Run";
			break;
		case 4:
			str = "Pause";
			break;
		case 5:
			str = "Stop";
			break;
		}
		return str;
	}
}
