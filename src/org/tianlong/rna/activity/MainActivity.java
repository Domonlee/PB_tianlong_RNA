package org.tianlong.rna.activity;

import java.util.Date;
import java.util.List;

import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.pojo.Step;
import org.tianlong.rna.utlis.MachineStateInfo;
import org.tianlong.rna.utlis.Utlis;
import org.tianlong.rna.utlis.WifiUtlis;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {

	private RelativeLayout main_top_logout_rl;
	private RelativeLayout main_body_rl;
	private TextView main_top_uname_tv;
	private Intent intent;
	private int U_id;
	private String Uname;
	private GestureDetector gd;
	public static int id = 1;
	public static String machine_wifi_name = null;
	private TextView machine_wifi_name_tv;
	private QueryUVThread queryUVThread;

	private WifiUtlis wifiUtlis;

	// 紫外灯标志位
	public static boolean uvFlag = true;
	private Dialog dialog;
	private String TAG = "UV Thread";
	
	
	private List<String> receive;
	private List<String> infoList; // 转换后的文件列表
	private int exp_id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// 开启uv状态获取线程
		uvFlag = true;
		MachineStateInfo machineStateInfo = new MachineStateInfo(MainActivity.this);
		machineStateInfo.runflag = false;
		intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");

		gd = new GestureDetector(handListener);

		main_top_uname_tv = (TextView) findViewById(R.id.main_top_uname_tv);
		main_top_logout_rl = (RelativeLayout) findViewById(R.id.main_top_logout_rl);
		main_body_rl = (RelativeLayout) findViewById(R.id.main_body_rl);
		machine_wifi_name_tv = (TextView) findViewById(R.id.machine_wifi_name_tv);

		displayWifiName();
		machine_wifi_name_tv.setText(machine_wifi_name);
		SwitchActivity(id);

		queryUVThread = new QueryUVThread();
		try {
			wifiUtlis = new WifiUtlis(MainActivity.this);
			new Thread(queryUVThread).start();
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, getString(R.string.wifi_error),
					Toast.LENGTH_SHORT).show();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage(getString(R.string.ultraviolet));
		builder.setCancelable(false);
//		 queryUVHandler.postDelayed(queryUVThread, 1000);
		builder.setNegativeButton(getString(R.string.stop),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {
							wifiUtlis.sendMessage(Utlis
									.ultravioletCloseMessage());
						} catch (Exception e) {
							e.printStackTrace();
						}
						dialog.dismiss();
//						 queryUVHandler.removeCallbacks(queryUVThread);
					}
				});
		dialog = builder.create();
		dialog.dismiss();

		main_top_uname_tv.setText(Uname);

		main_top_logout_rl.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				intent.putExtra("logout", 1);
				intent.putExtra("U_id", U_id);
				startActivity(intent);
				finish();
			}
		});
	}

	private void SwitchActivity(int tid) {
		main_body_rl.removeAllViews();
		Intent intent = null;
		switch (tid) {
		case 1:
			intent = new Intent(MainActivity.this, MainApplyActivity.class);
			intent.putExtra("Uname", Uname);
			intent.putExtra("U_id", U_id);
			id = 1;
			break;
		case 2:
			intent = new Intent(MainActivity.this,
					QuickExperimentActivity.class);
			intent.putExtra("Uname", Uname);
			intent.putExtra("U_id", U_id);
			id = 2;
			break;
		default:
			break;
		}
		Window subActivity = getLocalActivityManager().startActivity(
				"subActivity", intent);
		main_body_rl.addView(subActivity.getDecorView(),
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	private OnGestureListener handListener = new OnGestureListener() {
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		public void onShowPress(MotionEvent e) {
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		public void onLongPress(MotionEvent e) {
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if ((e1.getX() - e2.getX()) > 150 && Math.abs(velocityX) > 150) {
				id++;
				if (id > 2) {
					id--;
				}
			} else if ((e2.getX() - e1.getX()) > 150
					&& Math.abs(velocityX) > 150) {
				id--;
				if (id < 1) {
					id++;
				}
			}
			SwitchActivity(id);
			return false;
		}

		public boolean onDown(MotionEvent e) {
			return false;
		}
	};

	public boolean dispatchTouchEvent(MotionEvent ev) {
		gd.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	public String displayWifiName() {
		WifiUtlis wifiUtlis = null;
		try {
			wifiUtlis = new WifiUtlis(MainActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (wifiUtlis == null) {
			machine_wifi_name = "仪器未连接";
			machine_wifi_name_tv.setText(machine_wifi_name);
		} else {
			machine_wifi_name = wifiUtlis.getWifiName();
		}
		return machine_wifi_name;
	}

	class QueryUVThread implements Runnable {
		@Override
		public void run() {
			while (uvFlag) {
//				Log.w(TAG, "thread");
				try {
					Message message = queryUVHandler.obtainMessage();
					message.obj = wifiUtlis.getMessage();
					queryUVHandler.sendMessage(message);
					Thread.sleep(500);
					wifiUtlis.sendMessage(Utlis.getseleteMessage(13));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Handler queryUVHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
//			Log.w(TAG, "handler");
			String info = (String) msg.obj;
			if (info.length() != 0) {
				Log.w(TAG, info);
				// 仪器运行状态
				// Log.w(TAG, info.substring(21, 23));
				// 紫外灯运行状态
				// Log.w(TAG, info.substring(24, 26));

				// TODO 目前只检测了一步，关于紫外灯的检测
				if (info.substring(24, 26).equals("00")) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					Log.w(TAG, "外层关闭");
					}
				} else if (info.substring(24, 26).equals("01")) {
					Log.w(TAG, "外层打开");

					dialog.show();
				}

				// 仪器状态检测
				if (info.substring(21, 23).equals("00")
						|| info.substring(21, 23).equals("05")) {
					//测试代码 机器停止状态 跳转到另外一个地址 并且发送数据。
//					new Thread(getExperimentInfoThread).start()	;
//					if (exp_id == 0 ) {
//						Intent intent = new Intent(MainActivity.this,RunExpActivity2.class);
//						startActivity(intent);
//						intent.putExtra("E_id", 9999);
//						Log.w(TAG, ""+exp_id);
//					}
					Log.w(TAG, "machine is stop ");
				} else if (info.substring(21, 23).equals("03")) {
					Log.w(TAG, "machine is runing ");
//					uvFlag= false;
					Intent intent = new Intent(MainActivity.this,RunExpActivity2.class);
//					Intent intent = new Intent(MainActivity.this,RunExpActivity.class);
					startActivity(intent);
				} else if (info.substring(21, 23).equals("04")) {
					Toast.makeText(getApplicationContext(), "测试用--Pause", 1000).show();
					Log.w(TAG, "machine is pause");
				}
				// else {
				// Log.w(TAG, "error");
				// uvFlag = false;
				// }
			}
		};
	};

	protected void onStop() {
		uvFlag = false;
		Log.w("TAG", "onStop");
		super.onStop();
	};

	@Override
	protected void onStart() {
//		new Thread(queryUVThread).start();
		uvFlag = true;
		Log.w("TAG", "onStart");
		super.onStart();
	}

	@Override
	protected void onPause() {
		uvFlag = false;
		Log.w("TAG", "onStop");
		super.onPause();
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
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

	
//	private Handler getExperimentInfoHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			Log.w("Handler", "handler");
//			byte[] info = (byte[]) msg.obj;
//			if (info.length != 0) {
//				if (receive != null) {
//					receive.removeAll(receive);
//				}
//				receive = Utlis.getReceive(info);
////				Log.w(TAG + "receive", receive + "");
//				infoList = Utlis.getExperimentInfoList(receive);
//				Log.w(TAG + "infoList", infoList + "");
//				Experiment experiment = new Experiment();
//				try {
//					if (infoList.size() != 0) {
//						Log.w("发送文件子串", infoList.get(infoList.size() - 2)
//								.substring(0, 9) + "");
//						Log.w("发送文件索引", infoList.get(infoList.size() - 2)
//								.substring(0, 9).indexOf("#END_FILE")
//								+ "");
//						if ((infoList.get(infoList.size() - 2).substring(0, 9))
//								.indexOf("#END_FILE") != -1) {
//							experiment.setU_id(U_id);
//							experiment.setEname(infoList.get(0).substring(
//									infoList.get(0).indexOf(":") + 1,
//									infoList.get(0).length()));
//							String date = Utlis.systemFormat.format(new Date());
//							experiment.setCdate(date);
//							experiment.setRdate(date);
//							experiment.setEremark(infoList.get(2).substring(
//									infoList.get(2).indexOf(":") + 1,
//									infoList.get(2).length()));
//							experiment.setEDE_id(0);
//							experiment.setEquick(0);
//
//							// TODO 得不到数据
//							exp_id = experiment.getE_id();
//							Log.w("实验id-Handler", exp_id + "");
//
//							Log.w("RunExp", "得到实验数据正常");
//
//						} else {
//							Log.w("RunExp", "得到实验数据失败");
//						}
//					}
//				} catch (Exception e) {
//					Log.w("异常", "异常----");
//				}
//			}
//		};
//	};
//	
//	private Thread getExperimentInfoThread = new Thread() {
//		public void run() {
//			while (exp_id == 0 ) {
//				try {
//					try {
//						wifiUtlis = new WifiUtlis(MainActivity.this);
//						wifiUtlis.sendMessage(Utlis.sendExperimentId(3));
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//					
//						
//			Log.w(TAG, ""+exp_id);
//					Log.w("Thread", "thread");
//					Message message = getExperimentInfoHandler.obtainMessage();
//					message.obj = wifiUtlis.getByteMessage();
//					getExperimentInfoHandler.sendMessage(message);
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			Intent intent = new Intent(MainActivity.this,RunExpActivity2.class);
//			startActivity(intent);
//			intent.putExtra("E_id", exp_id);
//			Log.w(TAG, ""+exp_id);
//		};
//	};
}
