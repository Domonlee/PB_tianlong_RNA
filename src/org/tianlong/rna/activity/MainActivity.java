package org.tianlong.rna.activity;

import java.util.List;

import org.tianlong.rna.activity.R;
import org.tianlong.rna.activity.RunFileActivity.readListThread;
import org.tianlong.rna.adapter.MainApplyAdapter;
import org.tianlong.rna.utlis.Utlis;
import org.tianlong.rna.utlis.WifiUtlis;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
//			wifiUtlis.sendMessage(Utlis.getseleteMessage(8));
			new Thread(queryUVThread).start();
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, getString(R.string.wifi_error),
					Toast.LENGTH_SHORT).show();
		}

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
			while (true) {
				Log.w("da", "thread");
				try {
					Message message = queryUVHandler.obtainMessage();
					message.obj = wifiUtlis.getMessage();
					queryUVHandler.sendMessage(message);
					Thread.sleep(1000);
					wifiUtlis.sendMessage(Utlis.getseleteMessage(8));
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
				Log.w("da", "handler");
			String info = (String) msg.obj;
			if (info.length() != 0) {
				Log.w("da", info);
				Log.w("da", info.substring(21,23));

				if (info.substring(21, 23).equals("00")) {
					// dialog.dismiss();
					Log.w("da", "close");
				} else if (info.substring(21, 23).equals("01")) {
					Log.w("da", "open");
				}
//					queryUVHandler.removeCallbacks(queryUVThread);
			}
		};
	};

	// 将指定byte数组以16进制的形式打印到控制台
	public void printHexString(byte[] b) {
		String hexString = null;
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString = hex + " " + hexString;
		}
		Log.w("HexString--", hexString.toUpperCase());

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

}
