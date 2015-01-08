package org.tianlong.rna.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tianlong.rna.adapter.MainApplyAdapter;
import org.tianlong.rna.dao.MachineDao;
import org.tianlong.rna.pojo.Machine;
import org.tianlong.rna.utlis.Utlis;
import org.tianlong.rna.utlis.WifiUtlis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainApplyActivity extends Activity {
	private GridView ma_bottom_gv;
	private String uName = null;
	private int uId;
	private Dialog dialog;
	private WifiUtlis utlis;

//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			String info = (String) msg.obj;
//			if (info.length() != 0) {
//				Log.w("MainApply", info);
//				if (info.substring(21, 23).equals("00")) {
////					dialog.dismiss();
//					handler.removeCallbacks(runnable);
//					ma_bottom_gv.setAdapter(new MainApplyAdapter(
//							MainApplyActivity.this, getguanList()));
//				} else {
//					try {
//						utlis.sendMessage(Utlis.getseleteMessage(8));
//					} catch (Exception e) {
//						Toast.makeText(MainApplyActivity.this,
//								getString(R.string.wifi_error),
//								Toast.LENGTH_SHORT).show();
//					}
//				}
//			}
//		};
//	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maina);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Intent intent = getIntent();
		try {
			utlis = new WifiUtlis(MainApplyActivity.this);
		} catch (Exception e) {
			Toast.makeText(MainApplyActivity.this,
					getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
		}
		uName = intent.getStringExtra("Uname");
		uId = intent.getIntExtra("U_id", 9999);
		ma_bottom_gv = (GridView) findViewById(R.id.ma_bottom_gv);
		ma_bottom_gv.setAdapter(new MainApplyAdapter(MainApplyActivity.this,
				getguanList()));
		
		ma_bottom_gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = null;
				switch (arg2) {
				case 0:
					intent = new Intent(MainApplyActivity.this,
							ExperimentActivity.class);
					break;
				case 1:
					try {
						if (utlis != null) {
							utlis.close();
						}
						utlis = new WifiUtlis(MainApplyActivity.this);
						intent = new Intent(MainApplyActivity.this,
								RunFileActivity.class);
					} catch (Exception e) {
						Toast.makeText(MainApplyActivity.this,
								getString(R.string.wifi_error),
								Toast.LENGTH_SHORT).show();
					}
					break;
				case 2:
					intent = new Intent(MainApplyActivity.this,
							MachineActivity.class);
					break;
				case 3:
					intent = new Intent(MainApplyActivity.this,
							UpAndDownActivity.class);
					break;
				case 4:
					//TODO　消毒App
					if (utlis != null) {
						try {
//							MainActivity.uvFlag = false;
							MachineDao machineDao = new MachineDao();
							Machine machine = machineDao.getMachine(MainApplyActivity.this);
							String disinfectStr = machine.getMDtime();
							setUVtime(disinfectStr);
							utlis.sendMessage(Utlis.ultravioletOpenMessage());
							MainActivity.closeFlag = false;
							Thread.sleep(50);
//							utlis.sendMessage(Utlis.getseleteMessage(8));
						} catch (Exception e) {
							e.printStackTrace();
						}
//						AlertDialog.Builder builder = new AlertDialog.Builder(
//								MainApplyActivity.this);
//						builder.setMessage(getString(R.string.ultraviolet));
//						builder.setCancelable(false);
//						handler.postDelayed(runnable, 1000);
//						builder.setNegativeButton(getString(R.string.stop),
//								new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int which) {
//										try {
//											utlis.sendMessage(Utlis
//													.ultravioletCloseMessage());
//										} catch (Exception e) {
//											e.printStackTrace();
//										}
//										dialog.dismiss();
//										Log.w("Main", "内层关闭");
//										handler.removeCallbacks(runnable);
//										ma_bottom_gv
//												.setAdapter(new MainApplyAdapter(
//														MainApplyActivity.this,
//														getguanList()));
//									}
//								});
//						dialog = builder.show();
						ma_bottom_gv.setAdapter(new MainApplyAdapter(
								MainApplyActivity.this, getguanList()));
					} else {
						Toast.makeText(MainApplyActivity.this,
								getString(R.string.wifi_error),
								Toast.LENGTH_SHORT).show();
					}
					break;
				case 5:
					intent = new Intent(MainApplyActivity.this,
							HelpActivity.class);
					break;
				default:
					break;
				}
				if (arg2 != 4) {
					if (intent != null) {
						intent.putExtra("Uname", uName);
						intent.putExtra("U_id", uId);
						finish();
						startActivity(intent);
					}
				}
			}
		});
	}

//	Runnable runnable = new Runnable() {
//		public void run() {
//			Message message = handler.obtainMessage();
//			message.obj = utlis.getMessage();
//			handler.sendMessage(message);
//			handler.postDelayed(runnable, 1000);
//		}
//	};

	private List<Map<String, Object>> getList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("image", R.drawable.exp_setting);
		map1.put("info", getString(R.string.experiment));
		map1.put("id", 1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("image", R.drawable.log);
		map2.put("info", getString(R.string.log));
		map2.put("id", 2);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("image", R.drawable.sys_setting);
		map3.put("info", getString(R.string.setting));
		map3.put("id", 3);
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("image", R.drawable.up_down);
		map4.put("info", getString(R.string.up_down));
		map4.put("id", 4);
		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("image", R.drawable.ziwaideng);
		map5.put("info", getString(R.string.disinfection));
		map5.put("id", 5);
		Map<String, Object> map6 = new HashMap<String, Object>();
		map6.put("image", R.drawable.help);
		map6.put("info", getString(R.string.help));
		map6.put("id", 6);

		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		list.add(map6);
		return list;
	}

	private List<Map<String, Object>> getguanList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("image", R.drawable.exp_setting);
		map1.put("info", getString(R.string.experiment));
		map1.put("id", 1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("image", R.drawable.log);
		map2.put("info", getString(R.string.log));
		map2.put("id", 2);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("image", R.drawable.sys_setting);
		map3.put("info", getString(R.string.setting));
		map3.put("id", 3);
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("image", R.drawable.up_down);
		map4.put("info", getString(R.string.up_down));
		map4.put("id", 4);
		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("image", R.drawable.ziwaideng_guan);
		map5.put("info", getString(R.string.disinfection));
		map5.put("id", 5);
		Map<String, Object> map6 = new HashMap<String, Object>();
		map6.put("image", R.drawable.help);
		map6.put("info", getString(R.string.help));
		map6.put("id", 6);

		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		list.add(map6);
		return list;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

    @Override
	protected void onPause() 
	{
		super.onPause();
        //关闭对话框
//		AlertDialog.dismiss();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainApplyActivity.this);
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
	
	public void setUVtime(String mdTime){
		String[] time =mdTime.split(":");
		
		String h = Integer.toHexString(Integer.parseInt(time[0]));
		String m = Integer.toHexString(Integer.parseInt(time[1]));
		String s = Integer.toHexString(Integer.parseInt(time[2]));
		if (h.length() ==1) {
			h = "0"+h;
		}
		if (m.length() ==1) {
			m = "0"+m;
		}
		if (s.length() ==1) {
			s = "0"+s;
		}
		Utlis.uvHour = h;
		Utlis.uvMin = m;
		Utlis.uvSec = s;
		Log.w("----------", h+" "+ m +" " +s);
	}
}
