package org.tianlong.rna.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.tianlong.rna.adapter.RunFileAdapert;
import org.tianlong.rna.utlis.Utlis;
import org.tianlong.rna.utlis.WifiUtlis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class RunFileActivity extends Activity {

	private Button runfile_right_back_btn;
	private TextView runfile_right_tv;
	private ListView runfile_left_lv;
	private List<Map<String, Object>> strings = new ArrayList<Map<String, Object>>();
	private List<String> receive = new ArrayList<String>();
	private boolean readListFlag = true, readInfoFlag = true;
	private WifiUtlis wifiUtlis;
	private readListThread listThread;
	private readInfoThread infoThread;

	private int U_id;
	private String Uname;

	// 读取下位机运行日志列表接受handler
	private Handler readListHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			byte[] info = (byte[]) msg.obj;
			if (info.length != 0) {
				if (receive != null) {
					receive.removeAll(receive);
				}
				receive = Utlis.getReceive(info);
				strings = Utlis.getRunFileList(receive);
				runfile_left_lv.setAdapter(new RunFileAdapert(strings,
						RunFileActivity.this));
				readListFlag = false;
			}
		};
	};

	// 读取下位机运行日志列表接受线程
	class readListThread implements Runnable {
		public void run() {
			while (readListFlag) {
				try {
					Message message = readListHandler.obtainMessage();
					message.obj = wifiUtlis.getByteMessage();
					readListHandler.sendMessage(message);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
	};

	// 读取下位机运行日志详细信息接受handler
	private Handler readInfoHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			byte[] info = (byte[]) msg.obj;
			if (info.length != 0) {
				if (receive != null) {
					receive.removeAll(receive);
				}
				receive = Utlis.getReceive(info);
				runfile_right_tv.setText(Utlis.getRunFileInfo(receive));
				readInfoFlag = false;
			}
		};
	};

	// 读取下位机运行日志详细信息接受线程
	class readInfoThread implements Runnable {
		public void run() {
			while (readInfoFlag) {
				try {
					Message message = readInfoHandler.obtainMessage();
					message.obj = wifiUtlis.getByteMessage();
					readInfoHandler.sendMessage(message);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_runfile);
		listThread = new readListThread();
		infoThread = new readInfoThread();
		try {
			wifiUtlis = new WifiUtlis(RunFileActivity.this);
			wifiUtlis.sendMessage(Utlis.sendSelectRunfileList());
			new Thread(listThread).start();
		} catch (Exception e) {
			Toast.makeText(RunFileActivity.this,
					getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
		}

		Intent intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");

		runfile_right_tv = (TextView) findViewById(R.id.runfile_right_tv);
		runfile_right_back_btn = (Button) findViewById(R.id.runfile_right_back_btn);
		runfile_left_lv = (ListView) findViewById(R.id.runfile_left_lv);

		runfile_left_lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				runfile_right_tv.setText("");
				if (!readInfoFlag) {
					readInfoFlag = true;
				}
				if (wifiUtlis == null) {
					try {
						wifiUtlis = new WifiUtlis(RunFileActivity.this);
						wifiUtlis.sendMessage(Utlis
								.sendSelectRunfileInfo((Integer) strings.get(
										arg2).get("id")));
						new Thread(infoThread).start();
					} catch (Exception e) {
						Toast.makeText(RunFileActivity.this,
								getString(R.string.wifi_error),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					try {
						wifiUtlis.sendMessage(Utlis
								.sendSelectRunfileInfo((Integer) strings.get(
										arg2).get("id")));
						new Thread(infoThread).start();
					} catch (Exception e) {
						Toast.makeText(RunFileActivity.this,
								getString(R.string.wifi_error),
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		runfile_right_back_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(RunFileActivity.this,
						MainActivity.class);
				intent.putExtra("U_id", U_id);
				intent.putExtra("Uname", Uname);
				startActivity(intent);
				finish();
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					RunFileActivity.this);
			builder.setTitle(getString(R.string.sure_exit));
			builder.setPositiveButton(getString(R.string.sure),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Process.killProcess(android.os.Process.myPid());
						}
					});
			builder.setNegativeButton(getString(R.string.cancle),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.show();
		}
		return super.onKeyDown(keyCode, event);
	}
}
