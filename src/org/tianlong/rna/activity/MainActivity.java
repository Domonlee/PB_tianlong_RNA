package org.tianlong.rna.activity;

import java.util.Locale;

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
	public static boolean closeFlag = false;
	public static boolean runFlag = true;
	private Dialog dialog;
	private String TAG = "UV Thread";
	public static String currentUserName = "";
	public static int currentUserId = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		getLanguageEnv();

		// 开启uv状态获取线程
//		uvFlag = true;
		MachineStateInfo machineStateInfo = new MachineStateInfo(
				MainActivity.this);
		machineStateInfo.runflag = false;
		intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");
		currentUserName = Uname;
		currentUserId = U_id;

		Log.w("MainActivity", "Uname="+ currentUserName+";U_id="+ currentUserId	);

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
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, getString(R.string.wifi_error),
					Toast.LENGTH_SHORT).show();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage(getString(R.string.ultraviolet));
		builder.setCancelable(false);
		// queryUVHandler.postDelayed(queryUVThread, 1000);
		builder.setNegativeButton(getString(R.string.stop),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {
							wifiUtlis.sendMessage(Utlis
									.ultravioletCloseMessage());
							queryUVHandler.removeCallbacks(queryUVThread);
							Log.w(TAG, "发送关闭紫外灯消息");
							closeFlag = true;
							// Thread.sleep(50);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		dialog = builder.create();
		Log.w("创建dialog", "创建dialog");
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1) {
			U_id = data.getIntExtra("U_id", 9999);
			Uname = data.getStringExtra("Uname");
		}
		
		super.onActivityResult(requestCode, resultCode, data);
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
			machine_wifi_name = getString(R.string.machine_not_connect);
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
				try {
					wifiUtlis.sendMessage(Utlis.getseleteMessage(13));
					Message message = queryUVHandler.obtainMessage();
					message.what = 1;
					message.obj = wifiUtlis.getMessage();
					queryUVHandler.sendMessage(message);
//					Thread.sleep(50);
					//FIXME 测试代码，降低发送平率
					Thread.sleep(5000);
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
			// Log.w(TAG, "handler");
			String info = (String) msg.obj;
			if (info.length() != 0) {
				 Log.w(TAG, info);
				 //11.27增加获取信息判断
					
				 
//				if (info.substring(0, 18).equals("ff ff 0b 51 02 0d ") &&info.substring(24, 26).equals("00")) {
				if (info.substring(24, 26).equals("00")) {
					dialog.dismiss();
					closeFlag = false;
					Log.w(TAG, "关闭--close");
				} else {
					if (closeFlag == false) {
						dialog.show();
					}else {
						dialog.dismiss();
					}
					Log.w(TAG, "打开--open");
				}

				// 仪器状态检测
				if (info.substring(21, 23).equals("00")
						|| info.substring(21, 23).equals("05")) {
					// Log.w(TAG, "machine is stop ");
				} else if (info.substring(21, 23).equals("03")) {
					// Log.w(TAG, "machine is runing ");
					if (runFlag == true) {
					Intent intent = new Intent(MainActivity.this,
							RunExpActivity2.class);
					startActivityForResult(intent, 1);
					runFlag = false;
					}
				}
			}
		};
	};

	protected void onStop() {
		// 11.20 停止后线程持续运行
		// uvFlag = false;
		Log.w("TAG", "onStop");
		super.onStop();
	};

	@Override
	protected void onStart() {
		new Thread(queryUVThread).start();
		closeFlag = false;
		uvFlag = true;
		runFlag = true;
		Log.w("TAG", "onStart");
		super.onStart();
	}

	@Override
	protected void onPause() {
		uvFlag = false;
		Log.w("TAG", "onPause");
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		uvFlag = false;
		Log.w("TAG", "onDestroy");
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		Log.w("TAG", "onResume");
		displayWifiName();
		machine_wifi_name_tv.setText(machine_wifi_name);
		super.onResume();
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
	
	private String getLanguageEnv() {  
	       Locale l = Locale.getDefault();  
	       String language = l.getLanguage();  
	       String country = l.getCountry().toLowerCase();  
	       if ("zh".equals(language)) {  
	           if ("cn".equals(country)) {  
	               language = "zh-CN";  
	           }  
	       } else if ("us".equals(language)) {  
	               language = "US";  
	       }  
	       return language;  
	   }  
}
