package org.tianlong.rna.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tianlong.rna.adapter.HelpAdapter;
import org.tianlong.rna.utlis.Utlis;
import org.tianlong.rna.utlis.WifiUtlis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class HelpActivity extends Activity {
	private String uName;
	private int uId;
	private TextView help_logo_uname;
	private ListView help_body_left_body_lv;
	private RelativeLayout help_body_right_rl;
	private Button help_right_about_back_btn;
	private Button help_back_btn;
	private View view;
	private TextView about_system_num_info;
	private TextView about_machine_num_info;
	private TextView machine_wifi_name_tv;

	private List<String> receive;
	private String receiveMeg; // 接收信息
	private boolean queryFlag = true;
	private String machineVersionString = null;

	private WifiUtlis wifiUtlis;
	private QueryMachineInfoThread queryMachineInfoThread;

	public static int listChooseId = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Intent intent = getIntent();
		uName = intent.getStringExtra("Uname");
		uId = intent.getIntExtra("U_id", uId);

		try {
			wifiUtlis = new WifiUtlis(HelpActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

		help_logo_uname = (TextView) findViewById(R.id.help_top_uname_tv);
		help_body_left_body_lv = (ListView) findViewById(R.id.help_left_lv);
		help_body_right_rl = (RelativeLayout) findViewById(R.id.help_right_rl);
		machine_wifi_name_tv = (TextView)findViewById(R.id.machine_wifi_name_tv);
		machine_wifi_name_tv.setText(MainActivity.machine_wifi_name);

		showAbout();
		help_logo_uname.setText(uName);
		final HelpAdapter helpAdapter = new HelpAdapter(getList(),
				HelpActivity.this);
		help_body_left_body_lv.setAdapter(helpAdapter);
		help_body_left_body_lv
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						help_body_right_rl.removeAllViews();
						listChooseId = arg2;
						if (arg2 == 0) {
							showAbout();
							helpAdapter.notifyDataSetChanged();
						} else if (arg2 == 1) {
							showAboutUs();
							helpAdapter.notifyDataSetChanged();
						}
					}
				});
	}

	private void showAbout() {

		// 系统版本号 获取
		String systemNum = "";
		try {
			systemNum = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// 仪器版本号 未获取
		queryMachineInfoThread = new QueryMachineInfoThread();
		new Thread(queryMachineInfoThread).start();
		try {
			wifiUtlis.sendMessage(Utlis.queryMachineInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}

		view = LayoutInflater.from(HelpActivity.this).inflate(
				R.layout.activity_help_about, null);
		about_system_num_info = (TextView) view
				.findViewById(R.id.about_system_num_info);
		about_machine_num_info = (TextView) view
				.findViewById(R.id.about_machine_num_info);

		help_back_btn = (Button) view.findViewById(R.id.help_back_btn);
		help_back_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HelpActivity.this,
						MainActivity.class);
				intent.putExtra("Uname", uName);
				intent.putExtra("U_id", uId);
				startActivity(intent);
				finish();
			}
		});
		

		
		about_system_num_info.setText(systemNum);
		help_body_right_rl.addView(view);
	}

	private void showAboutUs() {
		view = LayoutInflater.from(HelpActivity.this).inflate(
				R.layout.activity_help_us, null);
		help_right_about_back_btn = (Button) view
				.findViewById(R.id.help_right_about_back_btn);
		help_right_about_back_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HelpActivity.this,
						MainActivity.class);
				intent.putExtra("Uname", uName);
				intent.putExtra("U_id", uId);
				startActivity(intent);
				finish();
			}
		});
		help_body_right_rl.addView(view);
	}

	private List<Map<String, Object>> getList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("image", R.drawable.version);
		map1.put("name", R.string.About);
		list.add(map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("image", R.drawable.about_us);
		map2.put("name", R.string.about_us);
		list.add(map2);
		return list;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					HelpActivity.this);
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

	private Handler queryMachineInfoHandler = new Handler() {
		public void handleMessage(Message msg) {
			byte[] info = (byte[]) msg.obj;
			if (info != null) {
				if (receive != null) {
					receive.removeAll(receive);
				}
				receive = Utlis.getReceive(info);
				for (int i = 0; i < receive.size(); i++) {
					receiveMeg = receive.get(i);
					Log.i("info", "发送数据回复:" + receiveMeg);
					queryFlag = false;
						
					if (receiveMeg.substring(12, 14).equals("08") && receiveMeg.substring(15, 17).equals("00") && receiveMeg.substring(18, 20).equals("00")){
						String hole = receiveMeg.substring(21, 23);
						machineVersionString = convertHexToString(hole);
						hole = receiveMeg.substring(24, 26);
						machineVersionString = machineVersionString + convertHexToString(hole);
						hole = receiveMeg.substring(27, 29);
						machineVersionString = machineVersionString + convertHexToString(hole);;
						hole = receiveMeg.substring(30, 32);
						machineVersionString = machineVersionString + convertHexToString(hole);
						hole = receiveMeg.substring(33, 35);
						machineVersionString = machineVersionString + convertHexToString(hole);
						hole = receiveMeg.substring(36, 38);
						machineVersionString = machineVersionString + convertHexToString(hole);
						hole = receiveMeg.substring(39, 41);
						machineVersionString = machineVersionString + convertHexToString(hole);
						about_machine_num_info.setText(machineVersionString);
					}
				}
			} else {
				Log.i("info", "空");
			}
		}
	};


	// 查询仪器版本号
	class QueryMachineInfoThread implements Runnable {
		public void run() {
			while (queryFlag) {
				try {
					Message message = queryMachineInfoHandler.obtainMessage();
					message.obj = wifiUtlis.getByteMessage();
					queryMachineInfoHandler.sendMessage(message);
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	public String convertHexToString(String hex){  
		  
	      StringBuilder sb = new StringBuilder();  
	      StringBuilder temp = new StringBuilder();  
	  
	      //49204c6f7665204a617661 split into two characters 49, 20, 4c...  
	      for( int i=0; i<hex.length()-1; i+=2 ){  
	  
	          //grab the hex in pairs  
	          String output = hex.substring(i, (i + 2));  
	          //convert hex to decimal  
	          int decimal = Integer.parseInt(output, 16);  
	          //convert the decimal to character  
	          sb.append((char)decimal);  
	  
	          temp.append(decimal);  
	      }  
	  
	      return sb.toString();  
	      }  
}
