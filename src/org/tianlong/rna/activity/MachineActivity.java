package org.tianlong.rna.activity;

//import java.io.File;
//import java.io.FileInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tianlong.rna.adapter.ArrayWheelAdapter;
import org.tianlong.rna.adapter.ExperimentAdapter;
import org.tianlong.rna.adapter.MachineAdapter;
import org.tianlong.rna.adapter.MachineAdminAdapter;
import org.tianlong.rna.dao.MachineDao;
import org.tianlong.rna.dao.UserDao;
import org.tianlong.rna.pojo.Machine;
import org.tianlong.rna.pojo.User;
import org.tianlong.rna.utlis.TimeWheelView;
import org.tianlong.rna.utlis.Utlis;
import org.tianlong.rna.utlis.WifiUtlis;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MachineActivity extends Activity {

	private ListView machine_left_lv;
	private Button machine_right_back_btn;
	private RelativeLayout machine_right_rl;
	private RelativeLayout machine_right_user_btn_rl;
	private Button machine_user_right_btn_save;
	private Button machine_user_right_btn_pass;
	private Button machine_user_right_btn_default;
	private Button machine_user_right_btn_pass_default;

	// 语言设置
	private RadioGroup machine_language_body_rg;
	private RadioButton machine_language_body_chinese;
	private RadioButton machine_language_body_english;
	private Button machine_language_bottom_btn_save;

	// 网络设置
	private ListView machine_net_wifi_lv;
	private Button machine_net_bottom_btn_save;
	private WifiManager wifiManager;
	private TextView wifi_SignalStrenth_tv;
	private TextView wifi_tv;
	private ImageView wifi_Iv;
	List<ScanResult> list;

	// 消毒设置
	private TextView machine_dismdect_body_time_et;
	private Button machine_dismdect_bottom_btn_save;
	private TimeWheelView time_hour_hour;
	private TimeWheelView time_hour_minutes;
	private TimeWheelView time_hour_seconds;
	private String disinfectStr;

	// 用户设置
	private Button machine_user_body_default_btn;
	private int defaultControNum, userViewControNum;;
	private EditText machine_user_pass_body_original_et;
	private EditText machine_user_pass_body_new_et;
	private EditText machine_user_pass_body_confirm_et;
	private ListView machine_admin_body_lv;
	private TextView machine_admin_body_head_name_tv;
	private TextView machine_admin_body_head_admin_tv;
	private TextView machine_admin_body_head_login_tv;
	private TextView machine_admin_body_head_modify_psw_tv;
	private boolean modifyFlag = false;

	byte[] b = new byte[1];

	// 仪器设置
	private TextView machine_instrument_body_flux_info_tv;
	private EditText machine_instrument_body_run_parameter_blend_info_et;
	private EditText machine_instrument_body_run_parameter_magnetic_info_et;
	private EditText machine_instrument_body_run_parameter_hole_info_et;
	private Button machine_instrument_bottom_btn_save;
	private RadioGroup machine_instrument_flux_item_rg;
	private RadioButton machine_instrument_flux_item_rb_one;
	private RadioButton machine_instrument_flux_item_rb_two;
	private RadioButton machine_instrument_flux_item_rb_there;
	private Button machine_instrument_body_run_parameter_blend_info_reset_btn;
	private Button machine_instrument_body_run_parameter_magnetic_info_reset_btn;
	private Button machine_instrument_body_run_parameter_hole_info_reset_btn;
	private Button machine_instrument_body_run_parameter_blend_info_send_btn;
	private Button machine_instrument_body_run_parameter_magnetic_info_send_btn;
	private Button machine_instrument_body_run_parameter_hole_info_send_btn;
	private TextView machine_instrument_body_run_parameter_blend_info_tv;
	private TextView machine_instrument_body_run_parameter_magnetic_info_tv;
	private TextView machine_instrument_body_run_parameter_hole_info_tv;
	private RelativeLayout machine_instrument_body_run_parameter_blend_btn_rl;
	private RelativeLayout machine_instrument_body_run_parameter_magnetic_btn_rl;
	private RelativeLayout machine_instrument_body_run_parameter_hol_btn_rl;
	private RelativeLayout machine_instrument_body_run_parameter_blend_info_btn_rl;
	private RelativeLayout machine_instrument_body_run_parameter_magnetic_info_btn_rl;
	private RelativeLayout machine_instrument_body_run_parameter_hole_info_btn_rl;
	private int fluxNum;
	private boolean selectInfoFlag, tempInfoFlag, detectionInfoFlag = true;

	// 仪器检测
	private ImageView machine_detection_body_bottom_left_info_iv;
	private TextView machine_detection_body_bottom_left_tv;
	private TextView machine_detection_body_bottom_right_tv;
	private ScrollView machine_detection_body_bottom_right_sv;
	private Button machine_detection_bottom_btn_check;
	private int detectionCheckNum, detectionNum, checkNum;
	private RotateAnimation rotateAnimation;
	private AnimationDrawable animationDrawable;
	private ImageView detection_item_left_power_iv;
	private TextView detection_item_right_sensor_tv;
	private TextView detection_item_right_heating_tv;
	private ImageView detection_item_left_info_power_iv;
	private ImageView detection_item_left_sensor_iv;
	private ImageView detection_item_left_info_sensor_iv;
	private ImageView detection_item_left_heating_iv;
	private ImageView detection_item_left_info_heating_iv;
	private ImageView detection_item_left_shock_iv;
	private ImageView detection_item_left_info_shock_iv;
	private ImageView detection_item_left_magnet_iv;
	private ImageView detection_item_left_info_magnet_iv;
	private ImageView detection_item_left_level_iv;
	private ImageView detection_item_left_info_level_iv;

	public List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
	public Map<String, Object> map = new HashMap<String, Object>();

	private int U_id, index, fluxInfoNum;
	private float hole, blend, magnetic;
	private String Uname;
	private List<User> users;
	private List<String> receive;
	private Machine machine;
	private MachineDao machineDao;
	private User user;
	private UserDao userDao;
	private View view;
	private WifiUtlis wifiUtlis;
	private SelectInfoThread selectInfoThread;
	private DetectionInfoThread detectionInfoThread;
	private String receiveMeg; // 接收信息
	private List<Map<String, Object>> listViews;
	private Map<String, Object> listMap;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_machine);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Intent intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");
		listViews = new ArrayList<Map<String, Object>>();
		listMap = new HashMap<String, Object>();
		userDao = new UserDao();
		machineDao = new MachineDao();
		selectInfoThread = new SelectInfoThread();
		detectionInfoThread = new DetectionInfoThread();
		user = userDao.getUserById(U_id, MachineActivity.this);
		machine = machineDao.getMachine(MachineActivity.this);

		machine_right_back_btn = (Button) findViewById(R.id.machine_right_back_btn);
		machine_left_lv = (ListView) findViewById(R.id.machine_left_lv);
		machine_right_rl = (RelativeLayout) findViewById(R.id.machine_right_rl);
		machine_right_user_btn_rl = (RelativeLayout) findViewById(R.id.machine_right_user_btn_rl);
		machine_user_right_btn_save = (Button) findViewById(R.id.machine_user_right_btn_save);
		machine_user_right_btn_pass = (Button) findViewById(R.id.machine_user_right_btn_pass);
		machine_user_right_btn_default = (Button) findViewById(R.id.machine_user_right_btn_default);
		machine_user_right_btn_pass_default = (Button) findViewById(R.id.machine_user_right_btn_pass_default);

		machine_left_lv.setAdapter(new MachineAdapter(MachineActivity.this,
				getList()));
		showView(0);

		machine_left_lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectInfoFlag = false;
				tempInfoFlag = false;
				detectionInfoFlag = false;
				if (detectionNum == 1) {
					Toast.makeText(MachineActivity.this,
							getString(R.string.detection_not_check),
							Toast.LENGTH_SHORT).show();
				} else {
					if (MachineAdapter.views.size() != 0) {
						for (int i = 0; i < MachineAdapter.views.size(); i++) {
							MachineAdapter.views.get(i).setBackgroundResource(
									R.drawable.list_bg);
						}
						MachineAdapter.views.removeAll(ExperimentAdapter.views);
					}
					if (view != null) {
						machine_right_rl.removeView(view);
						view = null;
					}
					if (listViews.size() != 0) {
						if (((Integer) listViews.get(0).get("id")) == arg2) {
							((View) listViews.get(0).get("view"))
									.setBackgroundResource(R.drawable.list_bg);
							listViews.remove(0);
							listMap.clear();
						} else {
							((View) listViews.get(0).get("view"))
									.setBackgroundResource(R.drawable.list_bg);
							listViews.remove(0);
							listMap.clear();
							listMap.put("id", arg2);
							listMap.put("view", arg1);
							listViews.add(listMap);
							arg1.setBackgroundResource(R.drawable.list_select);
							showView(arg2);
						}
					} else {
						listMap.put("id", arg2);
						listMap.put("view", arg1);
						listViews.add(listMap);
						arg1.setBackgroundResource(R.drawable.list_select);
						showView(arg2);
					}
					if (arg2 == 2) {
						if (machine_right_user_btn_rl.isShown()) {
							machine_right_user_btn_rl.setVisibility(View.GONE);
						} else {
							machine_right_user_btn_rl
									.setVisibility(View.VISIBLE);
						}
					} else {
						machine_right_user_btn_rl.setVisibility(View.GONE);
					}
				}
			}
		});

		machine_right_back_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (detectionNum == 1) {
					Toast.makeText(MachineActivity.this,
							getString(R.string.detection_not_back),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(MachineActivity.this,
							MainActivity.class);
					intent.putExtra("U_id", U_id);
					intent.putExtra("Uname", Uname);
					startActivity(intent);
					selectInfoFlag = false;
					tempInfoFlag = false;
					detectionInfoFlag = false;
					finish();
				}
			}
		});

		machine_user_right_btn_save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (userViewControNum == 3) {
					if (userDao.updateDefault(user.getUdefault(),
							user.getU_id(), MachineActivity.this)) {
						Toast.makeText(MachineActivity.this,
								getString(R.string.user_default_success),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MachineActivity.this,
								getString(R.string.user_default_failure),
								Toast.LENGTH_SHORT).show();
					}
				} else if (userViewControNum == 6) {
					if (machine_user_pass_body_original_et.getText().toString()
							.equals("")) {
						Toast.makeText(
								MachineActivity.this,
								getString(R.string.user_original_pass_not_null),
								Toast.LENGTH_SHORT).show();
					} else {
						if (!machine_user_pass_body_original_et.getText()
								.toString().equals(user.getUpass())) {
							Toast.makeText(
									MachineActivity.this,
									getString(R.string.user_original_pass_error),
									Toast.LENGTH_SHORT).show();
						} else {
							if (machine_user_pass_body_new_et.getText()
									.toString().equals("")) {
								Toast.makeText(
										MachineActivity.this,
										getString(R.string.user_new_pass_not_null),
										Toast.LENGTH_SHORT).show();
							} else {
								if (machine_user_pass_body_confirm_et.getText()
										.toString().equals("")) {
									Toast.makeText(
											MachineActivity.this,
											getString(R.string.user_confirm_pass_not_null),
											Toast.LENGTH_SHORT).show();
								} else {
									if (machine_user_pass_body_new_et
											.getText()
											.toString()
											.equals(machine_user_pass_body_confirm_et
													.getText().toString())) {
										user.setUpass(machine_user_pass_body_new_et
												.getText().toString());
										if (userDao.updatePassword(user,
												MachineActivity.this)) {
											Toast.makeText(
													MachineActivity.this,
													getString(R.string.user_pass_success),
													Toast.LENGTH_SHORT).show();
										} else {
											Toast.makeText(
													MachineActivity.this,
													getString(R.string.user_pass_failure),
													Toast.LENGTH_SHORT).show();
										}
									} else {
										Toast.makeText(
												MachineActivity.this,
												getString(R.string.two_pass_error),
												Toast.LENGTH_SHORT).show();
									}
								}
							}
						}
					}
				}
			}
		});

		machine_user_right_btn_pass.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				machine_right_rl.removeView(view);
				showView(7);
			}
		});

		machine_user_right_btn_default
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						machine_right_rl.removeView(view);
						if (user.getUdefault() == 1) {
							defaultControNum = 1;
						}
						defaultLogin();
					}
				});

		// --初始化密码 完成
		machine_user_right_btn_pass_default
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (modifyFlag == true) {
							modifyFlag = false;
							machine_user_right_btn_pass_default
									.setText(getString(R.string.user_pass_default));
						} else {
							modifyFlag = true;
							machine_user_right_btn_pass_default
									.setText(getString(R.string.user_pass_default_close));
						}
						MachineAdminAdapter machineAdminAdapter = new MachineAdminAdapter(
								MachineActivity.this, users, modifyFlag);
						machine_admin_body_lv.setAdapter(machineAdminAdapter);
					}
				});
	}

	/**
	 * 
	 * Title: getList Description: Modified By： Domon Modified Date: 2014-9-11
	 */
	private List<String> getList() {
		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.system_language));
		list.add(getString(R.string.system_net));
		list.add(getString(R.string.system_user));
		list.add(getString(R.string.system_clean));
		list.add(getString(R.string.system_qr));
		if (user.getUadmin() == 1) {
			list.add(getString(R.string.system_machine));
			list.add(getString(R.string.system_detection));
		}
		return list;
	}

	private void showView(int id) {
		switch (id) {
		// 语言设置
		case 0:
			view = LayoutInflater.from(MachineActivity.this).inflate(
					R.layout.activity_machine_language, null);
			machine_language_body_rg = (RadioGroup) view
					.findViewById(R.id.machine_language_body_rg);
			machine_language_body_chinese = (RadioButton) view
					.findViewById(R.id.machine_language_body_chinese);
			machine_language_body_english = (RadioButton) view
					.findViewById(R.id.machine_language_body_english);
			machine_language_bottom_btn_save = (Button) view
					.findViewById(R.id.machine_language_bottom_btn_save);

			if (machine.getMlanguage() == 0) {
				machine_language_body_chinese.setChecked(true);
			} else {
				machine_language_body_english.setChecked(true);
			}

			machine_language_body_rg
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							switch (checkedId) {
							case R.id.machine_language_body_chinese:
								machine.setMlanguage(0);
								LoginActivity.language = 0;
								break;
							case R.id.machine_language_body_english:
								machine.setMlanguage(2);
								LoginActivity.language = 2;
								break;
							default:
								break;
							}
						}
					});

			machine_language_bottom_btn_save
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (machineDao.upDateLanguage(
									machine.getMlanguage(),
									MachineActivity.this)) {
								Utlis.setLanguage(MachineActivity.this,
										LoginActivity.language);
								machine_right_rl.removeView(view);
								Intent intent = new Intent(
										MachineActivity.this,
										MachineActivity.class);
								intent.putExtra("Uname", Uname);
								intent.putExtra("U_id", U_id);
								finish();
								startActivity(intent);
								Toast.makeText(MachineActivity.this,
										getString(R.string.language_success),
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(MachineActivity.this,
										getString(R.string.language_failure),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			break;
		// -- 网络设置
		case 1:
			wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			list = wifiManager.getScanResults();
			view = LayoutInflater.from(MachineActivity.this).inflate(
					R.layout.activity_machine_net, null);
			machine_net_wifi_lv = (ListView) view
					.findViewById(R.id.machine_net_wifi_lv);
			if (!wifiManager.isWifiEnabled()) {
				Intent intent = new Intent("android.settings.WIFI_SETTINGS");
				startActivity(intent);
			} else {

				// machine_net_bottom_btn_save.setVisibility(View.GONE);
				if (list == null) {
					Toast.makeText(this, getString(R.string.net_wifi_unopen),
							Toast.LENGTH_LONG).show();
				} else {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).SSID.length() <= 6) {
							list.remove(i);
							--i;
						} else if (!list.get(i).SSID.substring(0, 6).equals(
								"NP968_")) {
							list.remove(i);
							--i;
						}
					}
				}
				machine_net_wifi_lv.setAdapter(new WifiListAdapter(
						MachineActivity.this, list));
				// --跳转系统wifi设置。
				machine_net_wifi_lv
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Intent intent = new Intent(
										"android.settings.WIFI_SETTINGS");
								startActivity(intent);
							}
						});
			}
			break;

		// 用户设置
		case 2:
			if (user.getUadmin() != 1) {
				defaultLogin();
			} else {
				showAdamin();
			}
			break;

		// 消毒设置
		case 3:
			view = LayoutInflater.from(MachineActivity.this).inflate(
					R.layout.activity_machine_disinfect, null);

			machine_dismdect_body_time_et = (TextView) view
					.findViewById(R.id.machine_dismdect_body_time_et);
			machine_dismdect_bottom_btn_save = (Button) view
					.findViewById(R.id.machine_dismdect_bottom_btn_save);

			// TODO 消毒时间error
			if (machine.getMDtime().equals("null:null:null")
					|| machine.getMDtime().equals("null")) {
				machine.setMDtime("01:00:00");
			}
			machine_dismdect_body_time_et.setText(machine.getMDtime());
			machine_dismdect_body_time_et
					.setOnClickListener(new OnClickListener() {
						@SuppressWarnings("deprecation")
						public void onClick(View v) {
							Date date = null;
							try {
								date = Utlis.timeFormat.parse(machine
										.getMDtime());
							} catch (ParseException e) {
								e.printStackTrace();
							}

							View timeView = LayoutInflater.from(
									MachineActivity.this).inflate(
									R.layout.activity_time_hour, null);

							time_hour_hour = (TimeWheelView) timeView
									.findViewById(R.id.time_hour_hour);
							time_hour_minutes = (TimeWheelView) timeView
									.findViewById(R.id.time_hour_minutes);
							time_hour_seconds = (TimeWheelView) timeView
									.findViewById(R.id.time_hour_seconds);

							String[] hour = getResources().getStringArray(
									R.array.hour_array);
							String[] min = getResources().getStringArray(
									R.array.minute_array);
							String[] sec = getResources().getStringArray(
									R.array.second_array);

							time_hour_hour
									.setAdapter(new ArrayWheelAdapter<String>(
											hour));
							time_hour_minutes
									.setAdapter(new ArrayWheelAdapter<String>(
											min));
							time_hour_seconds
									.setAdapter(new ArrayWheelAdapter<String>(
											sec));
							time_hour_hour.setCyclic(true);
							time_hour_minutes.setCyclic(true);
							time_hour_seconds.setCyclic(true);
							time_hour_hour.setCurrentItem(date.getHours());
							time_hour_minutes.setCurrentItem(date.getMinutes());
							time_hour_seconds.setCurrentItem(date.getSeconds() / 5);

							AlertDialog.Builder builder = new AlertDialog.Builder(
									MachineActivity.this);
							builder.setTitle(getString(R.string.dismdect));
							builder.setView(timeView);
							builder.setPositiveButton(getString(R.string.sure),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

											String timeTotal = timeFormat(
													time_hour_hour
															.getCurrentItem(),
													time_hour_minutes
															.getCurrentItem(),
													time_hour_seconds
															.getCurrentItem());

											disinfectStr = timeTotal;
											machine_dismdect_body_time_et
													.setText(timeTotal);
										}
									});
							builder.setNegativeButton(
									getString(R.string.cancle),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									});
							builder.show();
						}
					});
			machine_dismdect_bottom_btn_save
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (machineDao.upDateDisinfectTime(disinfectStr,
									MachineActivity.this)) {
								Toast.makeText(MachineActivity.this,
										getString(R.string.dismdect_success),
										Toast.LENGTH_SHORT).show();
								machine.setMDtime(disinfectStr);
							} else {
								Toast.makeText(MachineActivity.this,
										getString(R.string.dismdect_failure),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			break;

		// 二维码
		case 4:
			view = LayoutInflater.from(MachineActivity.this).inflate(
					R.layout.activity_machine_qr, null);
			TextView machine_qr_name_tv = (TextView) view
					.findViewById(R.id.machine_qr_name_tv);
			break;
		// 仪器设置
		case 5:
			try {
				if (wifiUtlis == null) {
					wifiUtlis = new WifiUtlis(MachineActivity.this);
				}
				selectInfoFlag = true;
				new Thread(selectInfoThread).start();
				wifiUtlis.sendMessage(Utlis.getseleteMessage(7));
			} catch (Exception e) {
				e.printStackTrace();
			}
			view = LayoutInflater.from(MachineActivity.this).inflate(
					R.layout.activity_machine_instrument, null);
			machine_instrument_body_flux_info_tv = (TextView) view
					.findViewById(R.id.machine_instrument_body_flux_info_tv);
			machine_instrument_body_run_parameter_blend_info_et = (EditText) view
					.findViewById(R.id.machine_instrument_body_run_parameter_blend_info_et);
			machine_instrument_body_run_parameter_magnetic_info_et = (EditText) view
					.findViewById(R.id.machine_instrument_body_run_parameter_magnetic_info_et);
			machine_instrument_body_run_parameter_hole_info_et = (EditText) view
					.findViewById(R.id.machine_instrument_body_run_parameter_hole_info_et);
			machine_instrument_bottom_btn_save = (Button) view
					.findViewById(R.id.machine_instrument_bottom_btn_save);
			machine_instrument_body_run_parameter_blend_info_reset_btn = (Button) view
					.findViewById(R.id.machine_instrument_body_run_parameter_blend_info_reset_btn);
			machine_instrument_body_run_parameter_magnetic_info_reset_btn = (Button) view
					.findViewById(R.id.machine_instrument_body_run_parameter_magnetic_info_reset_btn);
			machine_instrument_body_run_parameter_hole_info_reset_btn = (Button) view
					.findViewById(R.id.machine_instrument_body_run_parameter_hole_info_reset_btn);
			machine_instrument_body_run_parameter_blend_info_send_btn = (Button) view
					.findViewById(R.id.machine_instrument_body_run_parameter_blend_info_send_btn);
			machine_instrument_body_run_parameter_magnetic_info_send_btn = (Button) view
					.findViewById(R.id.machine_instrument_body_run_parameter_magnetic_info_send_btn);
			machine_instrument_body_run_parameter_hole_info_send_btn = (Button) view
					.findViewById(R.id.machine_instrument_body_run_parameter_hole_info_send_btn);
			machine_instrument_body_run_parameter_blend_btn_rl = (RelativeLayout) view
					.findViewById(R.id.machine_instrument_body_run_parameter_blend_btn_rl);
			machine_instrument_body_run_parameter_magnetic_btn_rl = (RelativeLayout) view
					.findViewById(R.id.machine_instrument_body_run_parameter_magnetic_btn_rl);
			machine_instrument_body_run_parameter_hol_btn_rl = (RelativeLayout) view
					.findViewById(R.id.machine_instrument_body_run_parameter_hole_btn_rl);
			machine_instrument_body_run_parameter_blend_info_btn_rl = (RelativeLayout) view
					.findViewById(R.id.machine_instrument_body_run_parameter_blend_info_btn_rl);
			machine_instrument_body_run_parameter_magnetic_info_btn_rl = (RelativeLayout) view
					.findViewById(R.id.machine_instrument_body_run_parameter_magnetic_info_btn_rl);
			machine_instrument_body_run_parameter_hole_info_btn_rl = (RelativeLayout) view
					.findViewById(R.id.machine_instrument_body_run_parameter_hole_info_btn_rl);
			machine_instrument_body_run_parameter_blend_info_tv = (TextView) view
					.findViewById(R.id.machine_instrument_body_run_parameter_blend_info_tv);
			machine_instrument_body_run_parameter_magnetic_info_tv = (TextView) view
					.findViewById(R.id.machine_instrument_body_run_parameter_magnetic_info_tv);
			machine_instrument_body_run_parameter_hole_info_tv = (TextView) view
					.findViewById(R.id.machine_instrument_body_run_parameter_hole_info_tv);

			fluxNum = machine.getMflux();
			machine_instrument_body_flux_info_tv.setText(getNum(fluxNum));

			machine_instrument_body_run_parameter_blend_info_et.setText(machine
					.getMblend() + "");
			machine_instrument_body_run_parameter_magnetic_info_et
					.setText(machine.getMmagnet() + "");
			machine_instrument_body_run_parameter_hole_info_et.setText(machine
					.getMhole() + "");
			machine_instrument_body_run_parameter_blend_info_tv.setText(machine
					.getMblend() + "");
			machine_instrument_body_run_parameter_magnetic_info_tv
					.setText(machine.getMmagnet() + "");
			machine_instrument_body_run_parameter_hole_info_tv.setText(machine
					.getMhole() + "");

			// 混合电机控制输入框监听
			machine_instrument_body_run_parameter_blend_info_et
					.addTextChangedListener(new TextWatcher() {
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							String str = s.toString();
							if (str.length() != 0) {
								if (str.length() == 1) {
									if (str.equals(".")) {
										machine_instrument_body_run_parameter_blend_info_et
												.setText("");
									}
								} else {
									String inuptInfo = str.substring(
											str.length() - 1, str.length());
									if (inuptInfo.equals(".")) {
										if (str.substring(0, str.length() - 1)
												.indexOf(".") != -1) {
											machine_instrument_body_run_parameter_blend_info_et
													.setText(str.substring(0,
															str.length() - 1));
											machine_instrument_body_run_parameter_blend_info_et
													.setSelection(str.length() - 1);
										}
									} else {
										if (str.indexOf(".") != -1) {
											if (str.length()
													- (str.indexOf(".") + 1) != 1) {
												machine_instrument_body_run_parameter_blend_info_et.setText(str.substring(
														0, str.length() - 1));
												machine_instrument_body_run_parameter_blend_info_et
														.setSelection(str
																.length() - 1);
											}
										}
									}
								}
							}
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});

			// 磁吸电机控制输入框监听
			machine_instrument_body_run_parameter_magnetic_info_et
					.addTextChangedListener(new TextWatcher() {
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							String str = s.toString();
							if (str.length() != 0) {
								if (str.length() == 1) {
									if (str.equals(".")) {
										machine_instrument_body_run_parameter_magnetic_info_et
												.setText("");
									}
								} else {
									// Toast.makeText(MachineActivity.this,
									// "转换前："+str, Toast.LENGTH_SHORT).show();
									String inuptInfo = str.substring(
											str.length() - 1, str.length());
									if (inuptInfo.equals(".")) {
										if (str.substring(0, str.length() - 1)
												.indexOf(".") != -1) {
											machine_instrument_body_run_parameter_magnetic_info_et
													.setText(str.substring(0,
															str.length() - 1));
											machine_instrument_body_run_parameter_magnetic_info_et
													.setSelection(str.length() - 1);
										}
									} else {
										if (str.indexOf(".") != -1) {
											if (str.length()
													- (str.indexOf(".") + 1) != 1) {
												machine_instrument_body_run_parameter_magnetic_info_et.setText(str.substring(
														0, str.length() - 1));
												machine_instrument_body_run_parameter_magnetic_info_et
														.setSelection(str
																.length() - 1);
											}
										}
									}
								}
							}
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});
			// 孔位电机控制输入框监听
			machine_instrument_body_run_parameter_hole_info_et
					.addTextChangedListener(new TextWatcher() {
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							String str = s.toString();
							if (str.length() != 0) {
								if (str.length() == 1) {
									if (str.equals(".")) {
										machine_instrument_body_run_parameter_hole_info_et
												.setText("");
									}
								} else {
									// Toast.makeText(MachineActivity.this,
									// "转换前："+str, Toast.LENGTH_SHORT).show();
									String inuptInfo = str.substring(
											str.length() - 1, str.length());
									if (inuptInfo.equals(".")) {
										if (str.substring(0, str.length() - 1)
												.indexOf(".") != -1) {
											machine_instrument_body_run_parameter_hole_info_et
													.setText(str.substring(0,
															str.length() - 1));
											machine_instrument_body_run_parameter_hole_info_et
													.setSelection(str.length() - 1);
										}
									} else {
										if (str.indexOf(".") != -1) {
											if (str.length()
													- (str.indexOf(".") + 1) != 1) {
												machine_instrument_body_run_parameter_hole_info_et.setText(str.substring(
														0, str.length() - 1));
												machine_instrument_body_run_parameter_hole_info_et
														.setSelection(str
																.length() - 1);
											}
										}
									}
								}
							}
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});
			/*
			 * 通量设置 Modified By： Domon Modified Date: 2014-9-11
			 */
			machine_instrument_body_flux_info_tv
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							View fluxView = LayoutInflater.from(
									MachineActivity.this).inflate(
									R.layout.activity_machine_instrument_flux,
									null);
							machine_instrument_flux_item_rg = (RadioGroup) fluxView
									.findViewById(R.id.machine_instrument_flux_item_rg);
							machine_instrument_flux_item_rb_one = (RadioButton) fluxView
									.findViewById(R.id.machine_instrument_flux_item_rb_one);
							machine_instrument_flux_item_rb_two = (RadioButton) fluxView
									.findViewById(R.id.machine_instrument_flux_item_rb_two);
							machine_instrument_flux_item_rb_there = (RadioButton) fluxView
									.findViewById(R.id.machine_instrument_flux_item_rb_there);

							switch (fluxNum) {
							case 1:
								machine_instrument_flux_item_rb_one
										.setChecked(true);
								break;
							case 2:
								machine_instrument_flux_item_rb_two
										.setChecked(true);
								break;
							case 3:
								machine_instrument_flux_item_rb_there
										.setChecked(true);
								break;
							}

							machine_instrument_flux_item_rg
									.setOnCheckedChangeListener(new OnCheckedChangeListener() {
										public void onCheckedChanged(
												RadioGroup group, int checkedId) {
											switch (checkedId) {
											case R.id.machine_instrument_flux_item_rb_one:
												fluxNum = 1;
												break;
											case R.id.machine_instrument_flux_item_rb_two:
												fluxNum = 2;
												break;
											case R.id.machine_instrument_flux_item_rb_there:
												fluxNum = 3;
												break;
											default:
												break;
											}
										}
									});

							AlertDialog.Builder builder = new AlertDialog.Builder(
									MachineActivity.this);
							builder.setTitle(getString(R.string.instrument_flux_selete));
							builder.setView(fluxView);
							builder.setPositiveButton(getString(R.string.sure),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											machine.setMflux(fluxNum);
											machine_instrument_body_flux_info_tv
													.setText(getNum(fluxNum));
											byte[] byteList = new byte[9];
											byteList = Utlis
													.sendSettingflux(getNum(fluxNum));
											try {
												if (wifiUtlis == null) {
													wifiUtlis = new WifiUtlis(
															MachineActivity.this);
												}
												wifiUtlis.sendMessage(byteList);
												selectInfoFlag = true;
												new Thread(selectInfoThread)
														.start();
												wifiUtlis.sendMessage(Utlis
														.getseleteMessage(7));
												String string = wifiUtlis
														.getMessage();
												Log.w("getMessage string--",
														string);
												Toast.makeText(
														MachineActivity.this,
														getString(R.string.instrument_success),
														Toast.LENGTH_SHORT)
														.show();
											} catch (Exception e) {
												Toast.makeText(
														MachineActivity.this,
														getString(R.string.wifi_error),
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									});
							builder.setNegativeButton(
									getString(R.string.cancle),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									});
							builder.show();
						}
					});

			// 系统复位
			machine_instrument_bottom_btn_save
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							try {
								if (wifiUtlis == null) {
									wifiUtlis = new WifiUtlis(
											MachineActivity.this);
								}
								selectInfoFlag = true;
								new Thread(selectInfoThread).start();
								byte[] byteList = new byte[9];
								wifiUtlis.sendMessage(Utlis.getseleteMessage(9));
								String string = wifiUtlis.getMessage();
								Toast.makeText(getApplicationContext(), string,
										1000).show();
							} catch (Exception e) {
								Toast.makeText(MachineActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			// 混合电机复位
			machine_instrument_body_run_parameter_blend_info_reset_btn
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (wifiUtlis != null) {
								try {
									wifiUtlis.sendMessage(Utlis
											.sendSystemResetMessage());
								} catch (Exception e) {
									Toast.makeText(MachineActivity.this,
											getString(R.string.wifi_error),
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(MachineActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			// 磁吸电机复位
			machine_instrument_body_run_parameter_magnetic_info_reset_btn
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (wifiUtlis != null) {
								// wifiUtlis.sendMessage(Utlis.resetMotorMessage(1));
								machine_instrument_body_run_parameter_blend_info_btn_rl
										.setVisibility(View.VISIBLE);
								machine_instrument_body_run_parameter_blend_info_et
										.setVisibility(View.VISIBLE);
								machine_instrument_body_run_parameter_blend_info_tv
										.setVisibility(View.GONE);
							} else {
								Toast.makeText(MachineActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			// 孔位电机复位
			machine_instrument_body_run_parameter_hole_info_reset_btn
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (wifiUtlis != null) {
								// wifiUtlis.sendMessage(Utlis.resetMotorMessage(2));
								machine_instrument_body_run_parameter_magnetic_info_btn_rl
										.setVisibility(View.VISIBLE);
								machine_instrument_body_run_parameter_magnetic_info_et
										.setVisibility(View.VISIBLE);
								machine_instrument_body_run_parameter_magnetic_info_tv
										.setVisibility(View.GONE);
							} else {
								Toast.makeText(MachineActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			// 混合电机移动
			machine_instrument_body_run_parameter_blend_info_send_btn
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (wifiUtlis != null) {
								try {
									wifiUtlis.sendMessage(Utlis.moveMotorMessage(
											0,
											Float.valueOf(machine_instrument_body_run_parameter_blend_info_et
													.getText().toString())));
								} catch (Exception e) {
									Toast.makeText(MachineActivity.this,
											getString(R.string.wifi_error),
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(MachineActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			// 磁吸电机移动
			machine_instrument_body_run_parameter_magnetic_info_send_btn
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (wifiUtlis != null) {
								try {
									wifiUtlis.sendMessage(Utlis.moveMotorMessage(
											1,
											Float.valueOf(machine_instrument_body_run_parameter_magnetic_info_et
													.getText().toString())));
								} catch (Exception e) {
									Toast.makeText(MachineActivity.this,
											getString(R.string.wifi_error),
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(MachineActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			// 孔位电机移动
			machine_instrument_body_run_parameter_hole_info_send_btn
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (wifiUtlis != null) {
								try {
									wifiUtlis.sendMessage(Utlis.moveMotorMessage(
											2,
											Float.valueOf(machine_instrument_body_run_parameter_hole_info_et
													.getText().toString())));
								} catch (Exception e) {
									Toast.makeText(MachineActivity.this,
											getString(R.string.wifi_error),
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(MachineActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			break;
		case 6:
			view = LayoutInflater.from(MachineActivity.this).inflate(
					R.layout.activity_machine_detection, null);
			machine_detection_body_bottom_left_info_iv = (ImageView) view
					.findViewById(R.id.machine_detection_body_bottom_left_info_iv);
			machine_detection_bottom_btn_check = (Button) view
					.findViewById(R.id.machine_detection_bottom_btn_check);
			machine_detection_body_bottom_left_tv = (TextView) view
					.findViewById(R.id.machine_detection_body_bottom_left_tv);
			detection_item_right_sensor_tv = (TextView) view
					.findViewById(R.id.detection_item_right_sensor_tv);
			detection_item_right_heating_tv = (TextView) view
					.findViewById(R.id.detection_item_right_heating_tv);
			machine_detection_body_bottom_right_tv = (TextView) view
					.findViewById(R.id.machine_detection_body_bottom_right_tv);
			machine_detection_body_bottom_right_sv = (ScrollView) view
					.findViewById(R.id.machine_detection_body_bottom_right_sv);
			detection_item_left_power_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_power_iv);
			detection_item_left_info_power_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_info_power_iv);
			detection_item_left_sensor_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_sensor_iv);
			detection_item_left_info_sensor_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_info_sensor_iv);
			detection_item_left_heating_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_heating_iv);
			detection_item_left_info_heating_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_info_heating_iv);
			detection_item_left_shock_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_shock_iv);
			detection_item_left_info_shock_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_info_shock_iv);
			detection_item_left_magnet_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_magnet_iv);
			detection_item_left_info_magnet_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_info_magnet_iv);
			detection_item_left_level_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_level_iv);
			detection_item_left_info_level_iv = (ImageView) view
					.findViewById(R.id.detection_item_left_info_level_iv);

			animationDrawable = (AnimationDrawable) detection_item_left_power_iv
					.getBackground();

			rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
					MachineActivity.this, R.drawable.rotate);
			LinearInterpolator interpolator = new LinearInterpolator();
			rotateAnimation.setInterpolator(interpolator);
			rotateAnimation.setRepeatCount(-1);
			rotateAnimation.setDuration(2000);

			machine_detection_bottom_btn_check
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							detectionCheckNum++;
							if (detectionCheckNum % 2 == 0) {
								detectionInfoFlag = false;
								receive.removeAll(receive);
								receiveMeg = "";
								index = 0;
								machine_detection_bottom_btn_check
										.setText(getString(R.string.detection_check));
								machine_detection_body_bottom_left_info_iv
										.setVisibility(View.GONE);
								machine_detection_body_bottom_left_tv
										.setVisibility(View.INVISIBLE);
								detection_item_left_power_iv
										.setVisibility(View.GONE);
								detection_item_left_info_power_iv
										.setVisibility(View.GONE);
								detection_item_left_sensor_iv
										.setVisibility(View.GONE);
								detection_item_left_info_sensor_iv
										.setVisibility(View.GONE);
								detection_item_left_heating_iv
										.setVisibility(View.GONE);
								detection_item_left_info_heating_iv
										.setVisibility(View.GONE);
								detection_item_left_shock_iv
										.setVisibility(View.GONE);
								detection_item_left_info_shock_iv
										.setVisibility(View.GONE);
								detection_item_left_magnet_iv
										.setVisibility(View.GONE);
								detection_item_left_info_magnet_iv
										.setVisibility(View.GONE);
								detection_item_left_level_iv
										.setVisibility(View.GONE);
								detection_item_left_info_level_iv
										.setVisibility(View.GONE);
								machine_detection_body_bottom_right_tv
										.setText("");
								detectionNum = 0;
								machine_detection_body_bottom_left_info_iv
										.clearAnimation();
								animationDrawable.stop();
								checkNum = 0;
							} else {
								try {
									if (wifiUtlis == null) {
										wifiUtlis = new WifiUtlis(
												MachineActivity.this);
									}
									detectionInfoFlag = true;
									new Thread(detectionInfoThread).start();
									wifiUtlis.sendMessage(Utlis
											.getseleteMessage(7));
									detection_item_left_power_iv
											.setVisibility(View.GONE);
									detection_item_left_info_power_iv
											.setVisibility(View.GONE);
									detection_item_left_sensor_iv
											.setVisibility(View.GONE);
									detection_item_left_info_sensor_iv
											.setVisibility(View.GONE);
									detection_item_left_heating_iv
											.setVisibility(View.GONE);
									detection_item_left_info_heating_iv
											.setVisibility(View.GONE);
									detection_item_left_shock_iv
											.setVisibility(View.GONE);
									detection_item_left_info_shock_iv
											.setVisibility(View.GONE);
									detection_item_left_magnet_iv
											.setVisibility(View.GONE);
									detection_item_left_info_magnet_iv
											.setVisibility(View.GONE);
									detection_item_left_level_iv
											.setVisibility(View.GONE);
									detection_item_left_info_level_iv
											.setVisibility(View.GONE);
									machine_detection_body_bottom_right_tv
											.setText("");
									machine_detection_bottom_btn_check
											.setText(getString(R.string.stop));
									machine_detection_body_bottom_left_info_iv
											.setVisibility(View.VISIBLE);
									machine_detection_body_bottom_left_tv
											.setVisibility(View.VISIBLE);
									detection_item_left_power_iv
											.setVisibility(View.VISIBLE);
									machine_detection_body_bottom_left_info_iv
											.startAnimation(rotateAnimation);
									animationDrawable.start();
									detectionNum = 1;
									checkNum = 0;
								} catch (Exception e) {
									Toast.makeText(MachineActivity.this,
											getString(R.string.wifi_error),
											Toast.LENGTH_SHORT).show();
								}

							}
						}
					});
			break;
		case 7:
			view = LayoutInflater.from(MachineActivity.this).inflate(
					R.layout.activity_machine_user_pass, null);
			userViewControNum = 6;
			machine_user_pass_body_original_et = (EditText) view
					.findViewById(R.id.machine_user_pass_body_original_et);
			machine_user_pass_body_new_et = (EditText) view
					.findViewById(R.id.machine_user_pass_body_new_et);
			machine_user_pass_body_confirm_et = (EditText) view
					.findViewById(R.id.machine_user_pass_body_confirm_et);
			break;
		default:
			break;
		}
		if (view != null) {
			if (id != 2) {
				machine_right_rl.addView(view);
			}
		}
	}

	private void defaultLogin() {
		machine_user_right_btn_default.setVisibility(View.GONE);
		machine_user_right_btn_pass_default.setVisibility(View.GONE);
		userViewControNum = 3;
		view = LayoutInflater.from(MachineActivity.this).inflate(
				R.layout.activity_machine_user_default, null);
		machine_user_body_default_btn = (Button) view
				.findViewById(R.id.machine_user_body_default_btn);

		if (user.getUdefault() == 1) {
			machine_user_body_default_btn
					.setBackgroundResource(R.drawable.setting_on);
			defaultControNum = 1;
		} else {
			machine_user_body_default_btn
					.setBackgroundResource(R.drawable.setting_off);
		}

		machine_user_body_default_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				defaultControNum++;
				if (defaultControNum % 2 == 0) {
					machine_user_body_default_btn
							.setBackgroundResource(R.drawable.setting_off);
					user.setUdefault(0);
				} else {
					machine_user_body_default_btn
							.setBackgroundResource(R.drawable.setting_on);
					user.setUdefault(1);
				}
			}
		});
		if (view != null) {
			machine_right_rl.addView(view);
		}
	}

	private void showAdamin() {

		machine_user_right_btn_pass.setVisibility(View.GONE);
		machine_user_right_btn_default.setVisibility(View.GONE);
		// machine_user_right_btn_pass_default.setVisibility(View.GONE);
		view = LayoutInflater.from(MachineActivity.this).inflate(
				R.layout.activity_machine_user_admin, null);
		machine_admin_body_lv = (ListView) view
				.findViewById(R.id.machine_admin_body_lv);
		machine_admin_body_head_name_tv = (TextView) view
				.findViewById(R.id.machine_admin_body_head_name_tv);
		machine_admin_body_head_admin_tv = (TextView) view
				.findViewById(R.id.machine_admin_body_head_admin_tv);
		machine_admin_body_head_login_tv = (TextView) view
				.findViewById(R.id.machine_admin_body_head_login_tv);
		machine_admin_body_head_modify_psw_tv = (TextView) view
				.findViewById(R.id.machine_admin_body_head_modify_psw_tv);

		machine_admin_body_head_name_tv
				.setText(getString(R.string.user_admin_name));
		machine_admin_body_head_admin_tv
				.setText(getString(R.string.user_admin_permissions));
		machine_admin_body_head_login_tv
				.setText(getString(R.string.user_admin_login));
		machine_admin_body_head_modify_psw_tv
				.setText(getString(R.string.user_admin_modify_psw));

		users = userDao.getAllUser(MachineActivity.this);
		machine_admin_body_lv.setAdapter(new MachineAdminAdapter(
				MachineActivity.this, users, modifyFlag));

		machine_admin_body_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (modifyFlag) {
					if (!(users.get(position).getUname().equals("admin"))
							&& !(users.get(position).getUname().equals("guest"))) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MachineActivity.this);
						builder.setTitle(getString(R.string.user_pass_default_sure));
						builder.setPositiveButton(getString(R.string.sure),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										users.get(position).setUpass("123456");
										userDao.updatePassword(
												users.get(position),
												MachineActivity.this);
										Toast.makeText(
												MachineActivity.this,
												getString(R.string.user_pass_default_modify_successful),
												Toast.LENGTH_LONG).show();
									}
								});
						builder.setNegativeButton(getString(R.string.cancle),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								});
						builder.show();
					}
				}

			}
		});
		if (view != null) {
			machine_right_rl.addView(view);
		}
	}

	private String getNum(int num) {
		String info = null;
		switch (num) {
		case 1:
			info = "15";
			break;
		case 2:
			info = "32";
			break;
		case 3:
			info = "48";
			break;
		case 4:
			info = "64";
			break;
		default:
			break;
		}
		return info;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MachineActivity.this);
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

	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (MachineActivity.this.getCurrentFocus() != null) {
				if (MachineActivity.this.getCurrentFocus().getWindowToken() != null) {
					imm.hideSoftInputFromWindow(MachineActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	// 电机位置校准线程
	class SelectInfoThread implements Runnable {
		public void run() {
			while (selectInfoFlag) {
				try {
					Message message = selectInfoHandler.obtainMessage();
					message.obj = wifiUtlis.getByteMessage();
					selectInfoHandler.sendMessage(message);
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	// 仪器自检线程
	class DetectionInfoThread implements Runnable {
		public void run() {
			while (detectionInfoFlag) {
				try {
					machine_detection_body_bottom_right_sv.scrollTo(0, 364);
					Message message = detectionInfoHandler.obtainMessage();
					message.obj = wifiUtlis.getByteMessage();
					detectionInfoHandler.sendMessage(message);
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String timeFormat(int hour, int min, int sec) {
		String hourStr, minStr, secStr;
		if (hour < 10) {
			hourStr = "0" + hour;
		} else {
			hourStr = "" + hour;
		}

		if (min < 10) {
			minStr = "0" + min;
		} else {
			minStr = "" + min;
		}
		sec = sec * 5;
		if (sec < 10) {
			secStr = "0" + sec;
		} else {
			secStr = "" + sec;
		}
		return hourStr + ":" + minStr + ":" + secStr;
	}

	public class WifiListAdapter extends BaseAdapter {

		LayoutInflater inflater;
		List<ScanResult> list;

		public WifiListAdapter(Context context, List<ScanResult> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			view = inflater.inflate(R.layout.item_wifi_list, null);
			wifi_SignalStrenth_tv = (TextView) view
					.findViewById(R.id.wifi_signal_strenth_tv);
			wifi_tv = (TextView) view.findViewById(R.id.wifi_tv);
			wifi_Iv = (ImageView) view.findViewById(R.id.wifi_ig);
			ScanResult scanResult = list.get(position);

			wifi_tv.setText(scanResult.SSID);
			wifi_SignalStrenth_tv.setText(String.valueOf(Math
					.abs(scanResult.level)) + "%");
			if (Math.abs(scanResult.level) > 100) {
				wifi_Iv.setImageDrawable(getResources().getDrawable(
						R.drawable.net_wifi_signal_0));
			} else if (Math.abs(scanResult.level) > 75) {
				wifi_Iv.setImageDrawable(getResources().getDrawable(
						R.drawable.net_wifi_signal_1));
			} else if (Math.abs(scanResult.level) > 50) {
				wifi_Iv.setImageDrawable(getResources().getDrawable(
						R.drawable.net_wifi_signal_2));
			} else if (Math.abs(scanResult.level) > 25) {
				wifi_Iv.setImageDrawable(getResources().getDrawable(
						R.drawable.net_wifi_signal_3));
			} else {
				wifi_Iv.setImageDrawable(getResources().getDrawable(
						R.drawable.net_wifi_signal_4));
			}
			return view;
		}

	}

	// 电机位置校准接受线程
	private Handler selectInfoHandler = new Handler() {
		public void handleMessage(Message msg) {
			byte[] info = (byte[]) msg.obj;
			if (info.length != 0) {
				receive = Utlis.getReceive(info);
				for (int i = 0; i < receive.size(); i++) {
					receiveMeg = receive.get(i);
					// 系统复位成功
					if (receiveMeg.equals("ff ff 0a d1 01 00 ff 01 da fe ")) {
						machine_instrument_body_run_parameter_hole_info_btn_rl
								.setVisibility(View.VISIBLE);
						machine_instrument_body_run_parameter_hole_info_et
								.setVisibility(View.VISIBLE);
						machine_instrument_body_run_parameter_hole_info_tv
								.setVisibility(View.GONE);
						machine_instrument_body_flux_info_tv.setClickable(true);
						machine_instrument_body_run_parameter_blend_btn_rl
								.setVisibility(View.VISIBLE);
						machine_instrument_body_run_parameter_magnetic_btn_rl
								.setVisibility(View.VISIBLE);
						machine_instrument_body_run_parameter_hol_btn_rl
								.setVisibility(View.VISIBLE);
					} else if (receiveMeg
							.equals("ff ff 0a d1 01 09 02 01 e6 fe ")) {
						Toast.makeText(MachineActivity.this,
								getString(R.string.instrument_hole_success),
								Toast.LENGTH_SHORT).show();
					} else if (receiveMeg
							.equals("ff ff 0a d1 01 09 01 01 e5 fe ")) {
						Toast.makeText(
								MachineActivity.this,
								getString(R.string.instrument_magnetic_success),
								Toast.LENGTH_SHORT).show();
					} else if (receiveMeg
							.equals("ff ff 0a d1 01 09 00 01 e4 fe ")) {
						Toast.makeText(MachineActivity.this,
								getString(R.string.instrument_blend_success),
								Toast.LENGTH_SHORT).show();
					} else if (receiveMeg.substring(15, 17).equals("09")) {
						try {
							hole = (Integer.parseInt(
									receiveMeg.substring(21, 23), 16) * 256 + Integer
									.parseInt(receiveMeg.substring(24, 26), 16)) / 10.0f;
							magnetic = (Integer.parseInt(
									receiveMeg.substring(27, 29), 16) * 256 + Integer
									.parseInt(receiveMeg.substring(30, 32), 16)) / 10.0f;
							blend = (Integer.parseInt(
									receiveMeg.substring(33, 35), 16) * 256 + Integer
									.parseInt(receiveMeg.substring(36, 38), 16)) / 10.0f;
							machine_instrument_body_run_parameter_blend_info_et
									.setText(blend + "");
							machine_instrument_body_run_parameter_magnetic_info_et
									.setText(magnetic + "");
							machine_instrument_body_run_parameter_hole_info_et
									.setText(hole + "");
							machine_instrument_body_run_parameter_blend_info_tv
									.setText(blend + "");
							machine_instrument_body_run_parameter_magnetic_info_tv
									.setText(magnetic + "");
							machine_instrument_body_run_parameter_hole_info_tv
									.setText(hole + "");
							wifiUtlis.sendMessage(Utlis
									.sendSystemResetMessage());
						} catch (Exception e) {
							Toast.makeText(MachineActivity.this,
									getString(R.string.wifi_error),
									Toast.LENGTH_SHORT).show();
						}
					} else if (receiveMeg.substring(15, 17).equals("07")) {
						Log.w("receiveMeg", fluxNum + "->>>>>" + receiveMeg);
						switch (Integer.parseInt(receiveMeg.substring(21, 23),
								16)) {
						case 15:
							fluxNum = 1;
							break;
						case 32:
							fluxNum = 2;
							break;
						case 48:
							fluxNum = 3;
							break;
						default:
							break;
						}
						machineDao.updateMachineFlux(machine,
								MachineActivity.this, fluxNum);
						Log.w("info", fluxNum + "--");
						machine_instrument_body_flux_info_tv
								.setText(getNum(fluxNum));
					}
				}
			}
		};
	};
	// 仪器自检接受线程
	private Handler detectionInfoHandler = new Handler() {
		public void handleMessage(Message msg) {
			byte[] info = (byte[]) msg.obj;
			if (info.length != 0) {
				machine_detection_body_bottom_right_sv.scrollTo(0, 364);
				receive = Utlis.getReceive(info);
				for (int i = 0; i < receive.size(); i++) {
					receiveMeg = receive.get(i);
					index = Integer.valueOf(receiveMeg.substring(15, 17));
					Log.w("Handler---", i + "=" + index);
					try {
						switch (index) {
						case 1:
							// 一起查询
							if (receiveMeg.substring(9, 11).equals("51")) {
								// 供电电压查询
								if (checkNum == 1) {
									if (Integer.parseInt(
											receiveMeg.substring(24, 26), 16) < 48
											|| Integer.parseInt(receiveMeg
													.substring(24, 26), 16) > 52) {
										machine_detection_body_bottom_right_tv
												.setText(getString(R.string.detection_five));
										detection_item_left_info_power_iv
												.setBackgroundResource(R.drawable.error);
									} else if (Integer.parseInt(
											receiveMeg.substring(30, 32), 16) < 115
											|| Integer.parseInt(receiveMeg
													.substring(30, 32), 16) > 125) {
										machine_detection_body_bottom_right_tv
												.setText(getString(R.string.detection_twelve));
										detection_item_left_info_power_iv
												.setBackgroundResource(R.drawable.error);
									} else if ((Integer.parseInt(
											receiveMeg.substring(33, 35), 16) * 256 + Integer
											.parseInt(receiveMeg.substring(36,
													38), 16)) < 265
											|| (Integer.parseInt(receiveMeg
													.substring(33, 35), 16) * 256 + Integer
													.parseInt(receiveMeg
															.substring(36, 38),
															16)) > 275) {
										machine_detection_body_bottom_right_tv
												.setText(getString(R.string.detection_thirty_three));
										detection_item_left_info_power_iv
												.setBackgroundResource(R.drawable.error);
									} else {
										machine_detection_body_bottom_right_tv
												.setText(getString(R.string.detection_power_sucess));
										detection_item_left_info_power_iv
												.setBackgroundResource(R.drawable.yes);
									}
									// 供电电压正常

									if (fluxInfoNum == 32) {
										detection_item_left_power_iv
												.setVisibility(View.GONE);
										detection_item_left_info_power_iv
												.setVisibility(View.VISIBLE);
										detection_item_left_sensor_iv
												.setVisibility(View.VISIBLE);
										animationDrawable.stop();
										animationDrawable = (AnimationDrawable) detection_item_left_sensor_iv
												.getBackground();
										animationDrawable.start();
										wifiUtlis.sendMessage(Utlis
												.sendDetectionMessage(2));

										Log.w("Test", "供电电压32");
										printHexString(Utlis
												.sendDetectionMessage(2));
										checkNum++;
									} else {
										detection_item_left_power_iv
												.setVisibility(View.GONE);
										detection_item_left_info_power_iv
												.setVisibility(View.VISIBLE);

										detection_item_left_shock_iv
												.setVisibility(View.VISIBLE);
										animationDrawable.stop();
										animationDrawable = (AnimationDrawable) detection_item_left_shock_iv
												.getBackground();
										animationDrawable.start();
										wifiUtlis.sendMessage(Utlis
												.sendDetectionMessage(4));

										Log.w("Test", "供电电压15 48");
										printHexString(Utlis
												.sendDetectionMessage(4));
										checkNum = checkNum + 3;
									}

								}
							} else if (receiveMeg.substring(9, 11).equals("d1")) {
								if (receiveMeg.substring(18, 20).equals("00")) {
									// 混合电机测试
									if (checkNum == 4) {
										if (receiveMeg.substring(21, 23)
												.equals("01")) {
											detection_item_left_info_shock_iv
													.setBackgroundResource(R.drawable.yes);
											machine_detection_body_bottom_right_tv
													.setText(machine_detection_body_bottom_right_tv
															.getText()
															.toString()
															+ "\n"
															+ getString(R.string.detection_shock_sucess));
										} else {
											detection_item_left_info_shock_iv
													.setBackgroundResource(R.drawable.error);
											machine_detection_body_bottom_right_tv
													.setText(machine_detection_body_bottom_right_tv
															.getText()
															.toString()
															+ "\n"
															+ getString(R.string.detection_shock_error));
										}

										detection_item_left_shock_iv
												.setVisibility(View.GONE);
										detection_item_left_info_shock_iv
												.setVisibility(View.VISIBLE);
										detection_item_left_magnet_iv
												.setVisibility(View.VISIBLE);
										animationDrawable.stop();
										animationDrawable = (AnimationDrawable) detection_item_left_magnet_iv
												.getBackground();
										animationDrawable.start();
										wifiUtlis.sendMessage(Utlis
												.sendDetectionMessage(5));

										Log.w("Test", "混合");
										printHexString(Utlis
												.sendDetectionMessage(5));
										checkNum++;
									}
								} else if (receiveMeg.substring(18, 20).equals(
										"01")) {
									// 磁吸电机测试
									if (checkNum == 5) {
										if (receiveMeg.substring(21, 23)
												.equals("01")) {
											detection_item_left_info_magnet_iv
													.setBackgroundResource(R.drawable.yes);
											machine_detection_body_bottom_right_tv
													.setText(machine_detection_body_bottom_right_tv
															.getText()
															.toString()
															+ "\n"
															+ getString(R.string.detection_magnet_sucess));
										} else {
											detection_item_left_info_magnet_iv
													.setBackgroundResource(R.drawable.error);
											machine_detection_body_bottom_right_tv
													.setText(machine_detection_body_bottom_right_tv
															.getText()
															.toString()
															+ "\n"
															+ getString(R.string.detection_magnet_error));
										}
										detection_item_left_magnet_iv
												.setVisibility(View.GONE);
										detection_item_left_info_magnet_iv
												.setVisibility(View.VISIBLE);
										detection_item_left_level_iv
												.setVisibility(View.VISIBLE);
										animationDrawable.stop();
										animationDrawable = (AnimationDrawable) detection_item_left_level_iv
												.getBackground();
										animationDrawable.start();
										wifiUtlis.sendMessage(Utlis
												.sendDetectionMessage(6));
										Log.w("Test", "磁吸");
										printHexString(Utlis
												.sendDetectionMessage(6));
										checkNum++;
									}
								} else if (receiveMeg.substring(18, 20).equals(
										"02")) {
									// 水平运动电机
									if (checkNum == 6) {
										if (receiveMeg.substring(21, 23)
												.equals("01")) {
											detection_item_left_info_level_iv
													.setBackgroundResource(R.drawable.yes);
											machine_detection_body_bottom_right_tv
													.setText(machine_detection_body_bottom_right_tv
															.getText()
															.toString()
															+ "\n"
															+ getString(R.string.detection_level_sucess));
										} else {
											detection_item_left_info_level_iv
													.setBackgroundResource(R.drawable.error);
											machine_detection_body_bottom_right_tv
													.setText(machine_detection_body_bottom_right_tv
															.getText()
															.toString()
															+ "\n"
															+ getString(R.string.detection_level_error));
										}
										detection_item_left_level_iv
												.setVisibility(View.GONE);
										detection_item_left_info_level_iv
												.setVisibility(View.VISIBLE);
										detection_item_left_level_iv
												.setVisibility(View.VISIBLE);
										animationDrawable.stop();
										detection_item_left_level_iv
												.setVisibility(View.GONE);
										detectionNum = 0;
										machine_detection_body_bottom_left_info_iv
												.setVisibility(View.GONE);
										machine_detection_body_bottom_left_tv
												.setVisibility(View.INVISIBLE);
										machine_detection_body_bottom_left_info_iv
												.clearAnimation();
										machine_detection_bottom_btn_check
												.setText(getString(R.string.detection_check));
										detectionCheckNum--;
										detectionInfoFlag = false;
										checkNum = 0;
										machine_detection_body_bottom_right_sv
												.scrollTo(0, 364);
										Log.w("Test", "水平");
									}
								}
							}
							break;
						case 5:
							// TODO 加热传感器
							if (checkNum == 2) {
								boolean t1 = false, t2 = false, t3 = false, t4 = false, t5 = false, t6 = false, t7 = false, t8 = false;
								if (receiveMeg.substring(21, 26).indexOf(
										"00 00") != -1) {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_sensor_t1));
									detection_item_left_info_sensor_iv
											.setBackgroundResource(R.drawable.error);
								} else {
									t1 = true;
								}
								if (receiveMeg.substring(27, 32).indexOf(
										"00 00") != -1) {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_sensor_t2));
									detection_item_left_info_sensor_iv
											.setBackgroundResource(R.drawable.error);
								} else {
									t2 = true;
								}
								if (receiveMeg.substring(33, 38).indexOf(
										"00 00") != -1) {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_sensor_t3));
									detection_item_left_info_sensor_iv
											.setBackgroundResource(R.drawable.error);
								} else {
									t3 = true;
								}
								if (receiveMeg.substring(39, 44).indexOf(
										"00 00") != -1) {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_sensor_t4));
									detection_item_left_info_sensor_iv
											.setBackgroundResource(R.drawable.error);
								} else {
									t4 = true;
								}
								if (receiveMeg.substring(45, 50).indexOf(
										"00 00") != -1) {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_sensor_t5));
									detection_item_left_info_sensor_iv
											.setBackgroundResource(R.drawable.error);
								} else {
									t5 = true;
								}
								if (receiveMeg.substring(51, 56).indexOf(
										"00 00") != -1) {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_sensor_t6));
									detection_item_left_info_sensor_iv
											.setBackgroundResource(R.drawable.error);
								} else {
									t6 = true;
								}
								if (receiveMeg.substring(57, 62).indexOf(
										"00 00") != -1) {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_sensor_t7));
									detection_item_left_info_sensor_iv
											.setBackgroundResource(R.drawable.error);
								} else {
									t7 = true;
								}
								if (receiveMeg.substring(63, 68).indexOf(
										"00 00") != -1) {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_sensor_t8));
									detection_item_left_info_sensor_iv
											.setBackgroundResource(R.drawable.error);
								} else {
									t8 = true;
								}
								if (t1 && t2 && t3 && t4 && t5 && t6 && t7
										&& t8) {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_sensor_sucess));
									detection_item_left_info_sensor_iv
											.setBackgroundResource(R.drawable.yes);
								}
								detection_item_left_sensor_iv
										.setVisibility(View.GONE);
								detection_item_left_info_sensor_iv
										.setVisibility(View.VISIBLE);
								detection_item_left_heating_iv
										.setVisibility(View.VISIBLE);
								animationDrawable.stop();
								animationDrawable = (AnimationDrawable) detection_item_left_heating_iv
										.getBackground();
								animationDrawable.start();
								wifiUtlis.sendMessage(Utlis
										.sendDetectionMessage(3));

								Log.w("Test", "温度");
								printHexString(Utlis.sendDetectionMessage(3));
								checkNum++;
							}
							break;
						case 6:
							if (checkNum == 0) {
								if (Integer.parseInt(
										receiveMeg.substring(21, 23), 16) != 3
										&& Integer.parseInt(
												receiveMeg.substring(21, 23),
												16) != 4) {
									wifiUtlis.sendMessage(Utlis
											.sendDetectionMessage(1));

									Log.w("Test", "checkNum =0");
									printHexString(Utlis
											.sendDetectionMessage(1));
									checkNum++;
								}
							}
							break;
						case 7:
							fluxInfoNum = Integer.parseInt(
									receiveMeg.substring(21, 23), 16);
							if (fluxInfoNum != 32) {
								detection_item_right_sensor_tv
										.setTextColor(getResources().getColor(
												R.color.black));
								detection_item_right_heating_tv
										.setTextColor(getResources().getColor(
												R.color.black));
							}
							wifiUtlis.sendMessage(Utlis.getseleteMessage(6));

							Log.w("Test", "fluxNum !=32");
							printHexString(Utlis.getseleteMessage(6));
							break;
						case 10:
							if (checkNum == 3) {
								if (!receiveMeg.substring(21, 23).equals("00")) {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_heating_error_one)
													+ receiveMeg
															.substring(
																	24,
																	receiveMeg
																			.length() - 6)
													+ getString(R.string.detection_heating_error_two));

									detection_item_left_info_heating_iv
											.setBackgroundResource(R.drawable.error);
								} else {
									machine_detection_body_bottom_right_tv
											.setText(machine_detection_body_bottom_right_tv
													.getText().toString()
													+ "\n"
													+ getString(R.string.detection_heating_sucess));
									detection_item_left_info_heating_iv
											.setBackgroundResource(R.drawable.yes);
								}
								detection_item_left_heating_iv
										.setVisibility(View.GONE);
								detection_item_left_info_heating_iv
										.setVisibility(View.VISIBLE);
								detection_item_left_shock_iv
										.setVisibility(View.VISIBLE);
								animationDrawable.stop();
								animationDrawable = (AnimationDrawable) detection_item_left_shock_iv
										.getBackground();
								animationDrawable.start();
								wifiUtlis.sendMessage(Utlis
										.sendDetectionMessage(4));

								Log.w("Test", "加热条");
								printHexString(Utlis.sendDetectionMessage(4));
								checkNum++;

							}
						default:
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					machine_detection_body_bottom_right_sv.scrollTo(0, 364);
				}
			}
		};
	};

	// 将指定byte数组以16进制的形式打印到控制台
	public static void printHexString(byte[] b) {
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
}
