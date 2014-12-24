package org.tianlong.rna.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tianlong.rna.adapter.UpAdapter;
import org.tianlong.rna.dao.ExperimentDao;
import org.tianlong.rna.dao.StepDao;
import org.tianlong.rna.dao.UserDao;
import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.pojo.Step;
import org.tianlong.rna.pojo.User;
import org.tianlong.rna.utlis.Utlis;
import org.tianlong.rna.utlis.WifiUtlis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "HandlerLeak", "DefaultLocale", "ShowToast" })
public class UpAndDownActivity extends Activity {

	private Button experiment_right_back_btn;
	private Button activity_upanddown_up_top_btn;
	private ListView activity_upanddown_up_top_lv;
	private Button activity_upanddown_down_top_btn;
	private ListView activity_upanddown_down_top_lv;
	private TextView activity_upanddown_top_uname_tv;
	private ImageView activity_upanddown_down_top_delete_btn;
	private TextView machine_wifi_name_tv;

	private boolean sameNameFlag = false;
	private List<Experiment> experiments;
	private ExperimentDao experimentDao;
	private StepDao stepDao;
	private boolean readListFlag = true, getInfoListFlag = true,
			sendFileFlag = true; // 读取列表线程控制，得到下位机文件线程控制，上位机发送文件线程控制
	private boolean deleteFlag = false; // 仪器实验删除
	// private String receiveMeg; //接收信息
	private WifiUtlis wifiUtlis; // wifi控制工具
	private List<String> receive; // 接受信息列表
	private List<String> infoList; // 转换后的文件列表
	private List<Map<String, Object>> experimentsList; // 下位机实验列表
	private List<Map<String, Object>> upViewList; // 上传点击列表
	private List<Map<String, Object>> downViewList; // 下载点击列表
	private Map<String, Object> map; // 点击保存列表
	private getExperimentInfoThread experimentInfoThread;
	private sendFileThread fileThread;
	private readListThread listThread;
	private int U_id, up_id = -1, down_id = -1;
	private String Uname;
	private int sendControlNum; // 发送文件控制数
	private int num;

	private ProgressDialog pDialog;
	private String newExpNameString = "";
	private EditText editNewName;
	
	private static int downSelectId = 999;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upanddown);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		MainActivity.uvFlag = false;
		pDialog = new ProgressDialog(UpAndDownActivity.this);
		pDialog.setMessage("数据加载中请稍后");
		pDialog.show();

		Intent intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");
		experimentDao = new ExperimentDao();
		stepDao = new StepDao();

		// uid 数值不对
		Log.w("up&down uid", U_id + "");
		experiments = experimentDao.getAllExperimentsByU_id(
				UpAndDownActivity.this, U_id);

		experimentsList = new ArrayList<Map<String, Object>>();
		upViewList = new ArrayList<Map<String, Object>>();
		downViewList = new ArrayList<Map<String, Object>>();
		map = new HashMap<String, Object>();
		experimentInfoThread = new getExperimentInfoThread();
		fileThread = new sendFileThread();
		listThread = new readListThread();
		try {
			wifiUtlis = new WifiUtlis(UpAndDownActivity.this);
			wifiUtlis.sendMessage(Utlis.getseleteMessage(10));
			new Thread(listThread).start();
		} catch (Exception e) {
			Toast.makeText(UpAndDownActivity.this,
					getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
		}
		experiment_right_back_btn = (Button) findViewById(R.id.experiment_right_back_btn);
		activity_upanddown_up_top_btn = (Button) findViewById(R.id.activity_upanddown_up_top_btn);
		activity_upanddown_up_top_lv = (ListView) findViewById(R.id.activity_upanddown_up_top_lv);
		activity_upanddown_down_top_btn = (Button) findViewById(R.id.activity_upanddown_down_top_btn);
		activity_upanddown_down_top_lv = (ListView) findViewById(R.id.activity_upanddown_down_top_lv);
		activity_upanddown_top_uname_tv = (TextView) findViewById(R.id.activity_upanddown_top_uname_tv);
		activity_upanddown_down_top_delete_btn = (ImageView) findViewById(R.id.activity_upanddown_down_top_delete_btn);
		machine_wifi_name_tv = (TextView) findViewById(R.id.machine_wifi_name_tv);
		machine_wifi_name_tv.setText(MainActivity.machine_wifi_name);

		activity_upanddown_top_uname_tv.setText(Uname);

		activity_upanddown_up_top_lv.setAdapter(new UpAdapter(
				UpAndDownActivity.this, experiments));

		activity_upanddown_up_top_lv
				.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (downViewList.size() != 0) {
							((View) downViewList.get(0).get("view"))
									.setBackgroundResource(R.drawable.up_down_select);
							down_id = -1;
							downViewList.removeAll(downViewList);
							map.clear();
						}
						if (upViewList.size() != 0) {
							if (((Integer) upViewList.get(0).get("id")) == arg2) {
								((View) upViewList.get(0).get("view"))
										.setBackgroundResource(R.drawable.up_down_select);
								up_id = -1;
								upViewList.removeAll(upViewList);
								map.clear();
							} else {
								((View) upViewList.get(0).get("view"))
										.setBackgroundResource(R.drawable.up_down_select);
								upViewList.removeAll(upViewList);
								map.clear();
								arg1.setBackgroundResource(R.drawable.list_btn_select);
								up_id = arg2;
								map.put("id", arg2);
								map.put("view", arg1);
								upViewList.add(map);
							}
						} else {
							arg1.setBackgroundResource(R.drawable.list_btn_select);
							up_id = arg2;
							map.put("id", arg2);
							map.put("view", arg1);
							upViewList.add(map);
						}
					}
				});

		// --长按事件
		activity_upanddown_up_top_lv
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						Intent gotoInfoIntent = new Intent(
								UpAndDownActivity.this,
								ExperimentActivity.class);
						gotoInfoIntent.putExtra("U_id", U_id);
						gotoInfoIntent.putExtra("Uname", Uname);
						startActivity(gotoInfoIntent);
						// 11.12改动 修改返回终止bug
						finish();
						return false;
					}
				});

		// --仪器文件 lv
		activity_upanddown_down_top_lv
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (upViewList.size() != 0) {
							((View) upViewList.get(0).get("view"))
									.setBackgroundResource(R.drawable.up_down_select);
							up_id = -1;
							upViewList.removeAll(upViewList);
							map.clear();
						}
						if (downViewList.size() != 0) {
							if (((Integer) downViewList.get(0).get("id")) == arg2) {
								((View) downViewList.get(0).get("view"))
										.setBackgroundResource(R.drawable.up_down_select);
								down_id = -1;
								downViewList.removeAll(downViewList);
								map.clear();
							} else {
								((View) downViewList.get(0).get("view"))
										.setBackgroundResource(R.drawable.up_down_select);
								// TODO仪器实验列表
								downViewList.removeAll(downViewList);
								map.clear();
								arg1.setBackgroundResource(R.drawable.list_btn_select);
								down_id = (Integer) experimentsList.get(arg2)
										.get("id");
								map.put("id", arg2);
								map.put("view", arg1);
								downViewList.add(map);
								
								downSelectId = arg2;
							}
						} else {
							arg1.setBackgroundResource(R.drawable.list_btn_select);
							down_id = (Integer) experimentsList.get(arg2).get(
									"id");
							map.put("id", arg2);
							map.put("view", arg1);
							downViewList.add(map);
								downSelectId = arg2;
						}
					}
				});
		// 删除按钮展示
		activity_upanddown_down_top_delete_btn
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						num++;
						if (num % 2 == 0) {
							deleteFlag = false;
							activity_upanddown_down_top_delete_btn
									.setBackgroundResource(R.drawable.upanddown_delete_ctrl_icon);
						} else {
							deleteFlag = true;
							activity_upanddown_down_top_delete_btn
									.setBackgroundResource(R.drawable.upanddown_delete_ctrl_cancel_icon);
						}
//						DownAdapter downAdapter = new DownAdapter(
//								UpAndDownActivity.this, experimentsList,
//								deleteFlag, downViewList);
						
						DownAdapter downAdapter = new DownAdapter(
								UpAndDownActivity.this, experimentsList,
								deleteFlag );
						activity_upanddown_down_top_lv.setAdapter(downAdapter);
					}
				});

		// --下载按键
		activity_upanddown_up_top_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (up_id == -1) {
					Toast.makeText(UpAndDownActivity.this,
							getString(R.string.no_select), Toast.LENGTH_SHORT)
							.show();
				} else {
					if (experimentsList.size() >= 15) {
						Toast.makeText(UpAndDownActivity.this,
								getString(R.string.down_is_full),
								Toast.LENGTH_SHORT).show();
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								UpAndDownActivity.this);
						builder.setTitle(getString(R.string.down_file));
						builder.setPositiveButton(getString(R.string.sure),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											if (infoList == null) {
												infoList = Utlis
														.getPadExperimentInfoList(
																experiments
																		.get(up_id),
																stepDao.getAllStep(
																		UpAndDownActivity.this,
																		experiments
																				.get(up_id)
																				.getE_id()),
																Uname);
											} else {
												infoList.removeAll(infoList);
												infoList = Utlis
														.getPadExperimentInfoList(
																experiments
																		.get(up_id),
																stepDao.getAllStep(
																		UpAndDownActivity.this,
																		experiments
																				.get(up_id)
																				.getE_id()),
																Uname);
											}
											wifiUtlis.sendMessage(Utlis
													.getbyteList(
															infoList.get(0), 1));
											if (!sendFileFlag) {
												sendFileFlag = true;
											}
											new Thread(fileThread).start();
										} catch (Exception e) {
											Toast.makeText(
													UpAndDownActivity.this,
													getString(R.string.wifi_error),
													Toast.LENGTH_SHORT).show();
										}
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

		// --上传按键 实现同命名检测
		activity_upanddown_down_top_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (down_id == -1) {
							Toast.makeText(UpAndDownActivity.this,
									getString(R.string.no_select),
									Toast.LENGTH_SHORT).show();
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									UpAndDownActivity.this);
							builder.setTitle(getString(R.string.up_file));
							builder.setPositiveButton(getString(R.string.sure),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											try {
												// Log.w("上传id", down_id+"");
												wifiUtlis.sendMessage(Utlis
														.sendExperimentId(down_id));
												getInfoListFlag = true;
												new Thread(experimentInfoThread)
														.start();
											} catch (Exception e) {
												Toast.makeText(
														UpAndDownActivity.this,
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
					}
				});

		experiment_right_back_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				readListFlag = false;
				getInfoListFlag = false;
				Intent intent = new Intent(UpAndDownActivity.this,
						MainActivity.class);
				intent.putExtra("U_id", U_id);
				intent.putExtra("Uname", Uname);
				startActivity(intent);
				finish();
			}
		});
	}

	// Pad发送文件handler
	private Handler sendFileHandler = new Handler() {
		public void handleMessage(Message msg) {
			String info = (String) msg.obj;
			if (info.length() != 0) {
				sendControlNum++;
				if (sendControlNum >= 3
						&& sendControlNum <= (infoList.size() - 3)) {
					if (Utlis.checkReceive(info, 1)) {
						try {
							wifiUtlis.sendMessage(Utlis.getMessageByte(
									infoList.get(sendControlNum),
									infoList.get(sendControlNum + 1), 1));
							sendControlNum++;
						} catch (Exception e) {
							Toast.makeText(UpAndDownActivity.this,
									getString(R.string.wifi_error),
									Toast.LENGTH_SHORT).show();
						}
					} else {
						sendControlNum -= 2;
						try {
							wifiUtlis.sendMessage(Utlis.getMessageByte(
									infoList.get(sendControlNum),
									infoList.get(sendControlNum + 1), 1));
							sendControlNum++;
						} catch (Exception e) {
							Toast.makeText(UpAndDownActivity.this,
									getString(R.string.wifi_error),
									Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					if (Utlis.checkReceive(info, 1)) {
						if (sendControlNum < infoList.size()) {
							try {
								wifiUtlis.sendMessage(Utlis.getbyteList(
										infoList.get(sendControlNum), 1));
							} catch (Exception e) {
								Toast.makeText(UpAndDownActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
							}
						} else {
							// wifiUtlis.sendMessage(Utlis.getseleteMessage(6));
							// 完成发送文件
							try {
								sendFileFlag = false;
								sendControlNum = 0;
								try {
									wifiUtlis = new WifiUtlis(
											UpAndDownActivity.this);
									wifiUtlis.sendMessage(Utlis
											.getseleteMessage(10));
									readListFlag = true;
									new Thread(listThread).start();
								} catch (Exception e) {
									Toast.makeText(UpAndDownActivity.this,
											getString(R.string.wifi_error),
											Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								Toast.makeText(UpAndDownActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
							}
						}
					} else {
						try {
							sendControlNum -= 2;
							wifiUtlis.sendMessage(Utlis.getbyteList(
									infoList.get(sendControlNum), 1));
						} catch (Exception e) {
							Toast.makeText(UpAndDownActivity.this,
									getString(R.string.wifi_error),
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		};
	};

	// Pad发送文件线程
	class sendFileThread implements Runnable {
		public void run() {
			while (sendFileFlag) {
				try {
					Message message = sendFileHandler.obtainMessage();
					message.obj = wifiUtlis.getMessage();
					if (((String) message.obj).length() != 0) {
						sendFileHandler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	// 读取下位机实验列表接受handler
	private Handler readListHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			byte[] info = (byte[]) msg.obj;
			if (info.length != 0) {
				if (receive != null) {
					receive.removeAll(receive);
				}
				receive = Utlis.getReceive(info);
				Log.w("读取下位机实验列表", receive.toString());

				// FIXME 1223加入重发机制，但是不起作用
				if (receive.size() == 1) {
					try {
						wifiUtlis.sendMessage(Utlis.getseleteMessage(10));
						Log.w("UpAndDownActivity", "重发");
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

					for (int i = 0; i < receive.size(); i++) {
						if (!(receive.get(i)
								.equals("[ff ff 0b 51 02 0d ff 05 00 6d fe ]"))) {
							experimentsList = Utlis.getExperimentList(receive);
							activity_upanddown_down_top_lv
									.setAdapter(new DownAdapter(
											UpAndDownActivity.this,
											experimentsList, deleteFlag));
							pDialog.dismiss();
							readListFlag = false;
							// XXX 加入判断，若adapter有数据的话就跳出循环
							break;
						} else {
							try {
								wifiUtlis.sendMessage(Utlis
										.getseleteMessage(10));
								Log.w("UpAndDownActivity", "仪器下位机列表获取失败");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		};
	};

	// 读取下位机实验列表接受线程
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

	// 将下位机实验数据转换成Pad实验数据handler
	private Handler getExperimentInfoHandler = new Handler() {
		public void handleMessage(Message msg) {
			byte[] info = (byte[]) msg.obj;
			if (info != null) {
				if (info.length != 0) {
					if (receive != null) {
						receive.removeAll(receive);
					}
					receive = Utlis.getReceive(info);
					infoList = Utlis.getExperimentInfoList(receive);
					Log.w("发送文件列表", infoList + "");
					for (int i = 0; i < experiments.size(); i++) {
						if (experiments
								.get(i)
								.getEname()
								.equals(infoList.get(0).substring(
										infoList.get(0).indexOf(":") + 1,
										infoList.get(0).length()))) {
							// 重复实验名修复框
							editNewName = new EditText(UpAndDownActivity.this);
							editNewName.setText("");
							sameNameFlag = true;
							new AlertDialog.Builder(UpAndDownActivity.this)
									.setTitle(
											getString(R.string.has_same_exp_name))
									.setIcon(android.R.drawable.ic_dialog_info)
									.setView(editNewName)
									.setPositiveButton(
											getString(R.string.sure),
											new android.content.DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													if (editNewName.getText()
															.toString()
															.equals("")) {
														Toast.makeText(
																UpAndDownActivity.this,
																getString(R.string.exp_name_null),
																Toast.LENGTH_SHORT)
																.show();
													} else {
														newExpNameString = editNewName
																.getText()
																.toString();
														Log.w("输入新的实验名称",
																newExpNameString);
														// sameNameFlag = false;
														Experiment experiment = new Experiment();
														if (infoList.size() != 0
																&& !sameNameFlag) {
															if ((infoList
																	.get(infoList
																			.size() - 2)
																	.substring(
																			0,
																			9))
																	.indexOf("#END_FILE") != -1) {

																experiment
																		.setU_id(U_id);
																if (!newExpNameString
																		.equals("")) {
																	experiment
																			.setEname(newExpNameString);
																	Log.w("setNewName",
																			"set new Name"
																					+ newExpNameString);
																} else {
																	experiment
																			.setEname(infoList
																					.get(0)
																					.substring(
																							infoList.get(
																									0)
																									.indexOf(
																											":") + 1,
																							infoList.get(
																									0)
																									.length()));
																	Log.w("setOldName",
																			"set old Name");
																}
																String date = Utlis.systemFormat
																		.format(new Date());
																experiment
																		.setCdate(date);
																experiment
																		.setRdate(date);
																experiment
																		.setEremark(infoList
																				.get(2)
																				.substring(
																						infoList.get(
																								2)
																								.indexOf(
																										":") + 1,
																						infoList.get(
																								2)
																								.length()));
																experiment
																		.setEDE_id(0);
																experiment
																		.setEquick(0);
																experimentDao
																		.insertExperiment(
																				experiment,
																				UpAndDownActivity.this);
																experiment = experimentDao
																		.getExperimentByCdate(
																				date,
																				UpAndDownActivity.this,
																				U_id);
																for (int i = 3; i < infoList
																		.size(); i++) {
																	if (infoList
																			.get(i)
																			.indexOf(
																					"#END_FILE") != -1) {
																		break;
																	} else {
																		Step step = Utlis
																				.getStepFromInfo(
																						infoList.get(i),
																						experiment
																								.getE_id());
																		stepDao.insertStep(
																				step,
																				UpAndDownActivity.this);
																	}
																}
																experiments = experimentDao
																		.getAllExperimentsByU_id(
																				UpAndDownActivity.this,
																				U_id);
																activity_upanddown_up_top_lv
																		.setAdapter(new UpAdapter(
																				UpAndDownActivity.this,
																				experiments));
																getInfoListFlag = false;
																Toast.makeText(
																		UpAndDownActivity.this,
																		getString(R.string.up_success),
																		Toast.LENGTH_SHORT);
															} else {
																Toast.makeText(
																		UpAndDownActivity.this,
																		getString(R.string.up_failure),
																		Toast.LENGTH_SHORT);
																getInfoListFlag = false;
															}
														}
														// 同名检测标志
														// sameNameFlag = false;
													}
												}
											})
									.setNegativeButton(
											getString(R.string.cancle),
											new android.content.DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													sameNameFlag = true;
												}
											}).show();
						}
					}

					if (infoList.size() != 0 && !sameNameFlag) {
						Experiment experiment = new Experiment();
						if ((infoList.get(infoList.size() - 2).substring(0, 9))
								.indexOf("#END_FILE") != -1) {
							experiment.setU_id(U_id);
							experiment.setEname(infoList.get(0).substring(
									infoList.get(0).indexOf(":") + 1,
									infoList.get(0).length()));
							String date = Utlis.systemFormat.format(new Date());
							experiment.setCdate(date);
							experiment.setRdate(date);
							experiment.setEremark(infoList.get(2).substring(
									infoList.get(2).indexOf(":") + 1,
									infoList.get(2).length()));
							experiment.setEDE_id(0);
							experiment.setEquick(0);
							experimentDao.insertExperiment(experiment,
									UpAndDownActivity.this);
							experiment = experimentDao.getExperimentByCdate(
									date, UpAndDownActivity.this, U_id);
							for (int j = 3; j < infoList.size(); j++) {
								if (infoList.get(j).indexOf("#END_FILE") != -1) {
									break;
								} else {
									Step step = Utlis.getStepFromInfo(
											infoList.get(j),
											experiment.getE_id());
									stepDao.insertStep(step,
											UpAndDownActivity.this);
								}
							}
							experiments = experimentDao
									.getAllExperimentsByU_id(
											UpAndDownActivity.this, U_id);
							activity_upanddown_up_top_lv
									.setAdapter(new UpAdapter(
											UpAndDownActivity.this, experiments));
							getInfoListFlag = false;
							Toast.makeText(UpAndDownActivity.this,
									getString(R.string.up_success),
									Toast.LENGTH_SHORT);
						} else {
							Toast.makeText(UpAndDownActivity.this,
									getString(R.string.up_failure),
									Toast.LENGTH_SHORT);
							getInfoListFlag = false;
						}
					}
					// 同名检测标志
					sameNameFlag = false;
				}
			}
		};
	};

	// 读取下位机指定实验接受线程
	class getExperimentInfoThread implements Runnable {
		public void run() {
			while (getInfoListFlag) {
				try {
					Message message = getExperimentInfoHandler.obtainMessage();
					message.obj = wifiUtlis.getByteMessage();
					getExperimentInfoHandler.sendMessage(message);
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
					UpAndDownActivity.this);
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

	public class DownAdapter extends BaseAdapter {

		private List<Map<String, Object>> strings;
		private List<Map<String, Object>> downlists;
		private LayoutInflater inflater;
		private boolean deleteFlag = false;

		public DownAdapter(Context context, List<Map<String, Object>> strings) {
			super();
			this.strings = strings;
			this.inflater = LayoutInflater.from(context);
		}

		public DownAdapter(Context context, List<Map<String, Object>> strings,
				boolean deleteFlag, List<Map<String, Object>> downlists) {
			super();
			this.strings = strings;
			this.inflater = LayoutInflater.from(context);
			this.deleteFlag = deleteFlag;
			this.downlists = downlists;
		}

		public DownAdapter(Context context, List<Map<String, Object>> strings,
				boolean deleteFlag) {
			super();
			this.strings = strings;
			this.inflater = LayoutInflater.from(context);
			this.deleteFlag = deleteFlag;
		}

		@Override
		public int getCount() {
			return strings.size();
		}

		@Override
		public Object getItem(int arg0) {
			return strings.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				holder = new ViewHolder();
				arg1 = inflater.inflate(
						R.layout.activity_upanddown_left_listview_item, null);
				holder.left_listview_item_name = (TextView) arg1
						.findViewById(R.id.left_listview_item_name);
				holder.left_listview_item_delete_icon = (ImageView) arg1
						.findViewById(R.id.left_listview_item_delete_icon);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			holder.left_listview_item_name.setText((String) strings.get(arg0)
					.get("info")
					+ " -- "
					+ (String) strings.get(arg0).get("user"));
			//TODO
			// // 获取当前item位置 并更换背景
//			if (downlists != null && (downlists.size() < strings.size())) {
			if (downlists != null ) {
//				if (downlists.get(arg0).get("id").equals(arg0)) {
//				if (map.get("id").equals(arg0)) {
				if (downSelectId == arg0) {
					arg1.setBackgroundResource(R.drawable.list_btn_select);
					downSelectId = 998;
				}
			}
			if (deleteFlag == true) {
				holder.left_listview_item_delete_icon
						.setVisibility(View.VISIBLE);
				holder.left_listview_item_delete_icon
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										UpAndDownActivity.this);
								builder.setTitle(getString(R.string.delete_file));
								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												if (!readListFlag) {
													readListFlag = true;
												}
												try {
													if (arg0 == 0) {
														Toast.makeText(
																UpAndDownActivity.this,
																getString(R.string.up_delete_forbid),
																Toast.LENGTH_SHORT)
																.show();
													} else {
														wifiUtlis
																.sendMessage(Utlis
																		.sendDeleteExperiment((Integer) experimentsList
																				.get(arg0)
																				.get("id")));
														wifiUtlis
																.sendMessage(Utlis
																		.getseleteMessage(10));
														strings.remove(arg0);
														Toast.makeText(
																UpAndDownActivity.this,
																getString(R.string.up_delete_successful),
																Toast.LENGTH_SHORT)
																.show();
														new Thread(listThread)
																.start();
													}
												} catch (Exception e) {
													Toast.makeText(
															UpAndDownActivity.this,
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

								DownAdapter.this.notifyDataSetChanged();
							}
						});
			} else {
				holder.left_listview_item_delete_icon.setVisibility(View.GONE);
			}

			return arg1;

		}

		class ViewHolder {
			TextView left_listview_item_name;
			ImageView left_listview_item_delete_icon;
		}

	}
}
