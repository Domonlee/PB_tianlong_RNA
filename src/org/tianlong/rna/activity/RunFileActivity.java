package org.tianlong.rna.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Domon
 * 
 */
@SuppressLint("HandlerLeak")
public class RunFileActivity extends Activity {

	private Button runfile_right_back_btn;
	private Button runfile_right_output_btn;
	private TextView runfile_right_output_location_tv;
	private TextView runfile_right_tv;
	private TextView machine_wifi_name_tv;
	private ListView runfile_left_lv;
	private List<Map<String, Object>> strings = new ArrayList<Map<String, Object>>();
	private List<String> receive = new ArrayList<String>();
	private boolean readListFlag = true, readInfoFlag = true;
	private WifiUtlis wifiUtlis;
	private readListThread listThread;
	private readInfoThread infoThread;

	private RunFileAdapert logListAdapert;

	private int U_id;
	private String Uname;
	private String experNameString;
	public static int listChooseId = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		MainActivity.uvFlag = false;

		listThread = new readListThread();
		infoThread = new readInfoThread();
		setContentView(R.layout.activity_runfile);
		try {
			wifiUtlis = new WifiUtlis(RunFileActivity.this);
			try {
				wifiUtlis.sendMessage(Utlis.sendSelectRunfileList());
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		runfile_right_output_btn = (Button) findViewById(R.id.runfile_right_output_btn);
		runfile_left_lv = (ListView) findViewById(R.id.runfile_left_lv);
		runfile_right_output_location_tv = (TextView) findViewById(R.id.runfile_right_output_location_tv);
		machine_wifi_name_tv = (TextView) findViewById(R.id.machine_wifi_name_tv);
		machine_wifi_name_tv.setText(MainActivity.machine_wifi_name);

		runfile_left_lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				runfile_right_tv.setText("");
				listChooseId = arg2;
				logListAdapert.notifyDataSetChanged();

				if (!readInfoFlag) {
					readInfoFlag = true;
				}
				if (wifiUtlis == null) {
					try {
						wifiUtlis = new WifiUtlis(RunFileActivity.this);
						wifiUtlis.sendMessage(Utlis
								.sendSelectRunfileInfo((Integer) strings.get(
										arg2).get("id")));
						experNameString = (String) strings.get(arg2)
								.get("info");

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
						experNameString = (String) strings.get(arg2)
								.get("info");
						Log.w("右侧信息输入--", "arg2=" + arg2 + ";experNameString="
								+ experNameString);
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
				listChooseId = 0;
				intent.putExtra("U_id", U_id);
				intent.putExtra("Uname", Uname);
				MainActivity.uvFlag = false;
				startActivity(intent);
				finish();
			}
		});

		/**
		 * 导出日志
		 * 
		 * @author Domon
		 */

		runfile_right_output_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String logString = runfile_right_tv.getText().toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String fileNameString = experNameString
						+ sdf.format(new Date()) + ".txt";
				try {
					if (Environment.MEDIA_MOUNTED.equals(Environment
							.getExternalStorageState())) {
						File file = new File(Environment
								.getExternalStorageDirectory(), fileNameString);
						FileOutputStream fos = new FileOutputStream(file);
						fos.write(logString.getBytes());
						fos.flush();
						fos.close();
						runfile_right_output_location_tv.setText("导出地址为：手机目录/"
								+ fileNameString);
						Toast.makeText(RunFileActivity.this,
								getString(R.string.log_output_successful),
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(RunFileActivity.this,
								getString(R.string.log_sdcard_error),
								Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.log_output_error),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	// 读取下位机运行日志列表接受handler
	private Handler readListHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			byte[] info = (byte[]) msg.obj;
			if (info != null) {
				if (info.length != 0) {
					if (receive != null) {
						receive.removeAll(receive);
					}
					receive = Utlis.getReceive(info);
					// Log.w("runfile receive", receive.toString());
					if (!(receive.toString()
							.equals("[ff ff 0b 51 02 0d ff 05 00 6d fe ]"))) {
						strings = Utlis.getRunFileList(receive);
						Log.w("Strings", strings + "");
						logListAdapert = new RunFileAdapert(strings,
								RunFileActivity.this);
						runfile_left_lv.setAdapter(logListAdapert);
						readListFlag = false;
					} else {
						try {
							wifiUtlis
									.sendMessage(Utlis.sendSelectRunfileList());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
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
	}

	// 读取下位机运行日志详细信息接受handler
	private Handler readInfoHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			byte[] info = (byte[]) msg.obj;
			if (info.length != 0) {
				if (receive != null) {
					receive.removeAll(receive);
				}
				receive = Utlis.getReceive(info);
//				Log.w("runfile receive---", receive.toString().substring(0, 26));
//				Log.w("runfile receive---", receive.toString()
//						.substring(16, 21));

				if (!(receive.toString().substring(16, 21).equals("0c ff"))) {
					runfile_right_tv.setText(Utlis.getRunFileInfo(receive));
					readInfoFlag = false;
				}
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
