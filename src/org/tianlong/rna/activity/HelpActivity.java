package org.tianlong.rna.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tianlong.rna.adapter.HelpAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		Intent intent = getIntent();
		uName = intent.getStringExtra("Uname");
		uId = intent.getIntExtra("U_id", uId);
		help_logo_uname = (TextView) findViewById(R.id.help_top_uname_tv);
		help_body_left_body_lv = (ListView) findViewById(R.id.help_left_lv);
		help_body_right_rl = (RelativeLayout) findViewById(R.id.help_right_rl);
		showAbout();
		help_logo_uname.setText(uName);
		help_body_left_body_lv.setAdapter(new HelpAdapter(getList(),
				HelpActivity.this));
		help_body_left_body_lv
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						help_body_right_rl.removeAllViews();
						switch (arg2) {
						case 0:
							showAbout();
							break;
						case 1:
							showAboutUs();
							break;
						default:
							break;
						}
					}
				});
	}

	private void showAbout() {

		String systemNum = "";
		try {
			systemNum = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		view = LayoutInflater.from(HelpActivity.this).inflate(
				R.layout.activity_help_about, null);
		about_system_num_info = (TextView) view
				.findViewById(R.id.about_system_num_info);

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
}
