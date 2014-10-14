package org.tianlong.rna.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tianlong.rna.dao.ExperimentDao;
import org.tianlong.rna.dao.MachineDao;
import org.tianlong.rna.dao.StepDao;
import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.pojo.Machine;
import org.tianlong.rna.pojo.Step;
import org.tianlong.rna.utlis.AdvancedCountdownTimer;
import org.tianlong.rna.utlis.MachineStateInfo;
import org.tianlong.rna.utlis.Utlis;
import org.tianlong.rna.utlis.WifiUtlis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class RunExperimentActivity extends Activity {

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

	private final String TAG = "TimeInfo";

	private TextView experiment_run_body_left_top_name_tv;
	private TextView experiment_run_top_mstate_tv;
	private TableLayout experiment_run_body_right_body_tl;
	private Button experiment_run_body_right_bottom_back_btn;
	private TextView experiment_run_top_uname_tv;
	private TextView experiment_run_body_left_body_time_info_tv;
	private Button experiment_run_body_right_bottom_run_btn;
	private Button experiment_run_body_right_bottom_stop_btn;
	private HorizontalScrollView experiment_run_body_right_body_hsv;
	private TextView experiment_run_body_left_body_t1_info_tv;
	private TextView experiment_run_body_left_body_t2_info_tv;
	private TextView experiment_run_body_left_body_t3_info_tv;
	private TextView experiment_run_body_left_body_t4_info_tv;
	private TextView experiment_run_body_left_body_t5_info_tv;
	private TextView experiment_run_body_left_body_t6_info_tv;
	private TextView experiment_run_body_left_body_t7_info_tv;
	private TextView experiment_run_body_left_body_t8_info_tv;
	private LinearLayout experiment_run_body_left_temp_body;
	
	//温度显示
	private ImageView experiment_run_body_left_temp_body_bg;
	private RelativeLayout experiment_run_body_left_body_t1_rl;
	private RelativeLayout experiment_run_body_left_body_t2_rl;
	private RelativeLayout experiment_run_body_left_body_t3_rl;
	private RelativeLayout experiment_run_body_left_body_t4_rl;
	private RelativeLayout experiment_run_body_left_body_t5_rl;
	private RelativeLayout experiment_run_body_left_body_t6_rl;
	private RelativeLayout experiment_run_body_left_body_t7_rl;
	private RelativeLayout experiment_run_body_left_body_t8_rl;

	private MachineStateInfo machineStateInfo;
	private TableRow stepRow;
	private StepDao stepDao;
	private WaitTimeCount waitTimeCount;
	private BlendTimeCount blendTimeCount;
	private MagnetTimeCount magnetTimeCount;
	private AnimationDrawable viewDrawable;
	private Experiment experiment;
	private ExperimentDao experimentDao;
	private List<String> receive;
	private List<Step> steps;
	private List<View> views;
	private List<Map<String, Object>> changeBg;
	private List<String> sendInfo;
	private List<WaitTimeCount> waitTimeList;
	private List<BlendTimeCount> blendTimeList;
	private List<MagnetTimeCount> magnetTimeList;
	private int sendControlNum;
	private List<RunViewHolder> holders;
	private Date date;
	private WifiUtlis wifiUtlis;
	private AlertDialog.Builder builder;
	private Dialog dialog;
	private SelectInfoThread selectInfoThread;
	private Machine machine;
	private MachineDao machineDao;

	private int fluxNum;
	private int hour;
	private int min;
	private int sec;
	private String time;

	private boolean sendFileFlag = true, // 接收发送数据线程控制
			selectInfoFlag = true; // 接收查询线程控制
	private int startTimeControl, //
			waitTimeControl, // 等待时间控制
			blendTimeControl, // 混合时间控制
			magnetTimeControl, // 磁吸时间控制
			runBtnControl, // 运行按钮控制
			runNum, // 运行或继续控制
			continueControl, // 继续按钮控制
			controlNum, // 总步骤索引
			stopBtn; // 停止控制

	private int E_id;
	private int U_id;
	private String Uname;
	private String jump;
	private float t1, t2, t3, t4, t5, t6, t7, t8;
	private int control; // 电机位置控制
	private String receiveMeg; // 接收信息
	private int index; // 接收信息功能索引
	final IntentFilter homeFilter = new IntentFilter(
			Intent.ACTION_CLOSE_SYSTEM_DIALOGS);// home键监听

	// log Tag
	private String TAGINFO = "INFO";

	private Handler sendFileHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i("info", "发文件");
			String info = (String) msg.obj;
			if (info.length() != 0) {
				Log.i("info", "发送数据回复:" + info);
				
				
				//
				sendControlNum++;
				Log.i("info", "发送数据控制NUM:" + sendControlNum);
				if (sendControlNum >= 3 && sendControlNum <= (sendInfo.size() - 3)) {
					if (Utlis.checkReceive(info, 0)) {
						try {
							wifiUtlis.sendMessage(Utlis.getMessageByte(
									sendInfo.get(sendControlNum),
									sendInfo.get(sendControlNum + 1), 0));
							sendControlNum++;

						} catch (Exception e) {
							Toast.makeText(RunExperimentActivity.this,
									getString(R.string.wifi_error),
									Toast.LENGTH_SHORT).show();
							inn();
							runNum = 0;
						}
					} else {
						sendControlNum--;
						try {
							wifiUtlis.sendMessage(Utlis.getMessageByte(
									sendInfo.get(sendControlNum),
									sendInfo.get(sendControlNum + 1), 0));
							sendControlNum++;

						} catch (Exception e) {
							Toast.makeText(RunExperimentActivity.this,
									getString(R.string.wifi_error),
									Toast.LENGTH_SHORT).show();
							inn();
							runNum = 0;
						}
					}
				} else {
					if (Utlis.checkReceive(info, 0)) {
						if (sendControlNum < sendInfo.size()) {
							try {
								wifiUtlis.sendMessage(Utlis.getbyteList(
										sendInfo.get(sendControlNum), 0));
							} catch (Exception e) {
								Toast.makeText(RunExperimentActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
								inn();
								runNum = 0;
							}
						} else {
							// wifiUtlis.sendMessage(Utlis.getseleteMessage(6));
							try {
								sendFileFlag = false;
								wifiUtlis
										.sendMessage(Utlis.getseleteMessage(5));
								wifiUtlis
										.sendMessage(Utlis.getseleteMessage(2));
								new Thread(selectInfoThread).start();
							} catch (Exception e) {
								Toast.makeText(RunExperimentActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
								inn();
								runNum = 0;
							}
						}
					} else {
						try {
							sendControlNum--;
							wifiUtlis.sendMessage(Utlis.getbyteList(
									sendInfo.get(sendControlNum), 0));
						} catch (Exception e) {
							Toast.makeText(RunExperimentActivity.this,
									getString(R.string.wifi_error),
									Toast.LENGTH_SHORT).show();
							inn();
							runNum = 0;
						}
					}
				}
				// TODO 发送文件线程
				//error
//				 machineStateInfo.pauseflag =true;
			}
		};
	};

	private Thread sendFileThread = new Thread() {
		public void run() {
			while (sendFileFlag) {

				Message message = sendFileHandler.obtainMessage();
				try {
					receiveMeg = wifiUtlis.getMessage();
//					receiveMeg = MachineStateInfo.sendMessageSyn();

					Log.w(TAGINFO, "receiveMes="+receiveMeg);
					if (receiveMeg.length() != 0) {
						message.obj = receiveMeg;
						sendFileHandler.sendMessage(message);
						Thread.sleep(1000);
					}
					else {
						sendFileFlag = false;
						if (dialog != null) {
							dialog.cancel();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	};

	private Handler selectInfoHandler = new Handler() {
		public void handleMessage(Message msg) {
			byte[] info = (byte[]) msg.obj;
			if (info.length != 0) {
				// Log.i("info", "不为空");
				receive = Utlis.getReceive(info);
				for (int i = 0; i < receive.size(); i++) {
					receiveMeg = receive.get(i);
					// Log.i("info", "命令:"+receiveMeg);
					index = Integer.parseInt(receiveMeg.substring(15, 17), 16);
					// 判断停止成功方法
					if (receiveMeg.equals("ff ff 0a d1 01 0d ff 01 e7 fe ")) {
						selectInfoFlag = false;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						inn();
					}
					// 判断开始成功方法
					else if (receiveMeg
							.equals("ff ff 0a d1 01 0a ff 01 e4 fe ")) {
						if (startTimeControl == 0) {
							if (viewDrawable == null) {
								try {
									if (dialog != null) {
										dialog.cancel();
									}
									viewDrawable = (AnimationDrawable) views
											.get(controlNum).getBackground();
									viewDrawable.start();
									date = Utlis.timeFormat
											.parse(experiment_run_body_left_body_time_info_tv
													.getText().toString());
									hour = date.getHours();
									min = date.getMinutes();
									sec = date.getSeconds();
									timeHandler.post(timeRunnable);
									waitTimeCount = waitTimeList
											.get(controlNum);
									waitTimeCount.start();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							startTimeControl = 1;
						}
						break;
					}
					// 判断暂停成功方法
					else if (receiveMeg
							.equals("ff ff 0a d1 01 0b ff 01 e5 fe ")) {
						// Log.i("info", "暂停成功");
						break;
					}
					// 判断继续成功方法
					else if (receiveMeg
							.equals("ff ff 0a d1 01 0c ff 01 e6 fe ")) {
						// Log.i("info", "继续成功");
						break;
					}
					// 如果都不是就是查询信息
					else {
						if (stopBtn != 1) {
							// 根据查询信息索引处理查询到的数据
							switch (index) {
							// 如果是2号查询就是电机位置查询
							case 2:
								// Log.i("info", "查询电机位置回复："+receiveMeg);
								control = Integer.parseInt(
										receiveMeg.substring(21, 23), 16);
								// Log.i("info", "电机位置:"+control);
								// 根据电机位置索引处理相应方法
								switch (control) {
								// 如果是0,电机在停止状态
								case 0:
									if (startTimeControl == 0) {
										try {
											wifiUtlis
													.sendMessage(Utlis
															.sendRunMessage(controlNum));
											if (viewDrawable == null) {
												try {
													if (dialog != null) {
														dialog.cancel();
													}
													viewDrawable = (AnimationDrawable) views
															.get(controlNum)
															.getBackground();
													viewDrawable.start();
													date = Utlis.timeFormat
															.parse(experiment_run_body_left_body_time_info_tv
																	.getText()
																	.toString());
													hour = date.getHours();
													min = date.getMinutes();
													sec = date.getSeconds();
													timeHandler
															.post(timeRunnable);
													date = Utlis.timeFormat
															.parse(holders
																	.get(controlNum).experiment_run_item_head_wait_info_tv
																	.getText()
																	.toString());
													waitTimeCount = new WaitTimeCount(
															date.getHours()
																	* 3600000
																	+ date.getMinutes()
																	* 60000
																	+ (date.getSeconds() - 2)
																	* 1000,
															1000);

													// waitTimeCount.start();
													// Log.i("info",
													// "电机停止状态启动等待");
												} catch (Exception e) {
													e.printStackTrace();
												}
											}
											startTimeControl = 1;
										} catch (Exception e1) {
											Toast.makeText(
													RunExperimentActivity.this,
													getString(R.string.wifi_error),
													Toast.LENGTH_SHORT).show();
											inn();
										}
									}
									break;
								// 如果是1,电机在等待状态
								case 1:
									Log.i("info", "等待中:" + waitTimeControl
											+ " 继续控制:" + continueControl);
									if (controlNum < holders.size()) {
										if (waitTimeControl == 0) {
											waitTimeControl = 1;
											if (continueControl != 1) {
												waitTimeCount.start();
												// Log.i("info", "电机等待状态启动等待");
											} else {
												continueControl = 0;
											}
											blendTimeControl = 0;
											magnetTimeControl = 0;
										}
									} else {
										selectInfoFlag = false;
									}
									break;
								// 如果是2,电机在混合状态
								case 2:
									Log.i("info", "混合中:" + blendTimeControl
											+ " 继续控制:" + continueControl);
									if (blendTimeControl == 0) {
										blendTimeControl = 1;
										if (continueControl != 1) {
											blendTimeCount.start();
										} else {
											continueControl = 0;
										}
										waitTimeControl = 0;
										magnetTimeControl = 0;
									}
									break;
								// 如果是3,电机在磁吸状态
								case 3:
									Log.i("info", "磁吸中:" + magnetTimeControl
											+ " 继续控制:" + continueControl);
									if (magnetTimeControl == 0) {
										magnetTimeControl = 1;
										if (continueControl != 1) {
											magnetTimeCount.start();
											// Log.i("info", "磁吸启动了");
										} else {
											continueControl = 0;
										}
										waitTimeControl = 0;
										blendTimeControl = 0;
									}
									break;
								default:
									break;
								}
								break;
							// 如果是5号查询就是温度查询
							case 5:
								// Log.i("info", "查询温度回复："+receiveMeg);
								t1 = (Integer.parseInt(
										receiveMeg.substring(21, 23)
												+ receiveMeg.substring(24, 26),
										16) + 5) / 10;
								t2 = (Integer.parseInt(
										receiveMeg.substring(27, 29)
												+ receiveMeg.substring(30, 32),
										16) + 5) / 10;
								t3 = (Integer.parseInt(
										receiveMeg.substring(33, 35)
												+ receiveMeg.substring(36, 38),
										16) + 5) / 10;
								t4 = (Integer.parseInt(
										receiveMeg.substring(39, 41)
												+ receiveMeg.substring(42, 44),
										16) + 5) / 10;
								t5 = (Integer.parseInt(
										receiveMeg.substring(45, 47)
												+ receiveMeg.substring(48, 50),
										16) + 5) / 10;
								t6 = (Integer.parseInt(
										receiveMeg.substring(51, 53)
												+ receiveMeg.substring(54, 56),
										16) + 5) / 10;
								t7 = (Integer.parseInt(
										receiveMeg.substring(57, 59)
												+ receiveMeg.substring(60, 62),
										16) + 5) / 10;
								t8 = (Integer.parseInt(
										receiveMeg.substring(63, 65)
												+ receiveMeg.substring(66, 68),
										16) + 5) / 10;
								experiment_run_body_left_body_t1_info_tv
										.setText(t1 + "°C");
								experiment_run_body_left_body_t2_info_tv
										.setText(t2 + "°C");
								experiment_run_body_left_body_t3_info_tv
										.setText(t3 + "°C");
								experiment_run_body_left_body_t4_info_tv
										.setText(t4 + "°C");
								experiment_run_body_left_body_t5_info_tv
										.setText(t5 + "°C");
								experiment_run_body_left_body_t6_info_tv
										.setText(t6 + "°C");
								experiment_run_body_left_body_t7_info_tv
										.setText(t7 + "°C");
								experiment_run_body_left_body_t8_info_tv
										.setText(t8 + "°C");
								break;
							// 如果是6号查询就是运行位置查询
							// TODO 跳转下一个步骤
							case 6:
								if (Integer.parseInt(
										receiveMeg.substring(24, 26), 16) != 5) {
									// 如果下位机当前运行步骤不等于总步骤索引并且总索引不为0时跳转到下一个步骤
									if ((controlNum + 1) != Integer.parseInt(
											receiveMeg.substring(21, 23), 16)) {
										Log.i("info", controlNum + "控制列");
										Log.i("info", "该跳了");
										continueControl = 0;
										if (viewDrawable != null) {
											viewDrawable.stop();
										}
										views.get(controlNum)
												.setBackgroundResource(
														R.anim.anim_view);
										controlNum = Integer.parseInt(
												receiveMeg.substring(21, 23),
												16) - 1;
										// Log.i("info",
										// "查询运行位置回复："+receiveMeg);
										// Log.i("info",
										// "位置代码："+receiveMeg.substring(21,
										// 23));
										// Log.i("info", "运行位置："+controlNum);
										getCurrentTime(controlNum);
										// 当总步骤索引小于总步骤数时开始下一步
										if (controlNum < holders.size()) {
											experiment_run_body_right_body_hsv
													.scrollTo(392 * controlNum,
															0);
											if (holders.get(controlNum).experiment_run_item_head_wait_info_tv
													.getText().toString()
													.equals("00:00:00")) {
												waitTimeCount = waitTimeList
														.get(controlNum);
												waitTimeCount.start();
												// Log.i("info", "下一步启动等待");
											} else {
												waitTimeCount = waitTimeList
														.get(controlNum);
											}
											viewDrawable = (AnimationDrawable) views
													.get(controlNum)
													.getBackground();
											viewDrawable.start();
										}
									}

								}
								// 当总步骤索引等于或大于总步骤数时实验结束
								else {
									selectInfoFlag = false;
									timeHandler.removeCallbacks(timeRunnable);
									viewDrawable.stop();
									builder = new AlertDialog.Builder(
											RunExperimentActivity.this);
									builder.setTitle(getString(R.string.run_exp_finsh));
									builder.setCancelable(false);
									builder.setPositiveButton(
											getString(R.string.sure),
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													wifiUtlis.getByteMessage();
													experiment_run_body_right_bottom_run_btn
															.setText(getString(R.string.run));
													startTimeControl = 0;
													waitTimeControl = 0;
													blendTimeControl = 0;
													magnetTimeControl = 0;
													runBtnControl = 0;
													continueControl = 0;
													controlNum = 0;
													runNum = 2;
													if (views.size() != 0) {
														views.removeAll(views);
													}
													if (holders.size() != 0) {
														holders.removeAll(holders);
													}
													if (waitTimeList.size() != 0) {
														waitTimeList
																.removeAll(waitTimeList);
													}
													if (blendTimeList.size() != 0) {
														blendTimeList
																.removeAll(blendTimeList);
													}
													if (magnetTimeList.size() != 0) {
														magnetTimeList
																.removeAll(magnetTimeList);
													}
													stepRow.removeAllViews();
													views.removeAll(views);
													holders.removeAll(holders);
													timeHandler
															.removeCallbacks(timeRunnable);
													getTime(0);
													experiment_run_body_right_body_tl
															.removeAllViews();
													viewDrawable = null;
													createTable();
												}
											});
									builder.show();
								}
								break;
							default:
								break;
							}
						}
					}
				}
				if (startTimeControl == 1) {
					try {
						Thread.sleep(100);
						wifiUtlis.sendMessage(Utlis.getseleteMessage(6));
						Thread.sleep(100);
						// 查询温度
						wifiUtlis.sendMessage(Utlis.getseleteMessage(5));
						Thread.sleep(100);
						wifiUtlis.sendMessage(Utlis.getseleteMessage(2));
					} catch (Exception e1) {
						Toast.makeText(RunExperimentActivity.this,
								getString(R.string.wifi_error),
								Toast.LENGTH_SHORT).show();
						inn();
					}
				}
			} else {
			}
		};
	};

	private Handler timeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment_run);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		machineDao = new MachineDao();
		machine = machineDao.getMachine(RunExperimentActivity.this);
		fluxNum = machine.getMflux();

		AlertDialog.Builder sbuilder = new AlertDialog.Builder(
				RunExperimentActivity.this);
		sbuilder.setTitle(getString(R.string.waitting));
		Dialog waitDialog = sbuilder.show();

		initView();

		selectMachineStateInfo();

		experiment_run_body_right_body_tl.setStretchAllColumns(true);
		createTable();

		experiment_run_top_uname_tv.setText(Uname);
		experiment_run_body_left_top_name_tv.setText(experiment.getEname());

		// TODO runBtn
		experiment_run_body_right_bottom_run_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (wifiUtlis == null) {
							Toast.makeText(RunExperimentActivity.this,
									getString(R.string.wifi_error_run),
									Toast.LENGTH_SHORT).show();
						} else {
							if (experiment_run_top_mstate_tv.getText()
									.toString().equals("仪器当前状态：Run")
									|| experiment_run_top_mstate_tv.getText()
											.toString().equals("仪器当前状态：Pause")) {
								Toast.makeText(RunExperimentActivity.this,
										"当前仪器有实验正在运行", 2000).show();
							} else {
								machineStateInfo.pauseflag = false;
								if (changeBg.size() != 0) {
									((View) changeBg.get(0).get("view"))
											.setBackgroundResource(R.anim.anim_view);
									changeBg.removeAll(changeBg);
								}
								runBtnControl++;
								if (runBtnControl % 2 == 0) {
									try {

										machineStateInfo.pauseflag = true;
										wifiUtlis.sendMessage(Utlis
												.sendPauseMessage());
										experiment_run_body_right_bottom_run_btn
												.setText(getString(R.string.run));
										viewDrawable.stop();
										selectInfoFlag = false;
										if (waitTimeControl == 1) {
											waitTimeCount.pause();
										}
										if (blendTimeControl == 1) {
											blendTimeCount.pause();
										}
										if (magnetTimeControl == 1) {
											magnetTimeCount.pause();
										}
										timeHandler
												.removeCallbacks(timeRunnable);
//										machineStateInfo.pauseflag = false;
									} catch (Exception e) {
										Toast.makeText(
												RunExperimentActivity.this,
												getString(R.string.wifi_error),
												Toast.LENGTH_SHORT).show();
									}
								} else {
									experiment_run_body_right_bottom_run_btn
											.setText(getString(R.string.pause));
									if (wifiUtlis != null) {
										// 控制流程
										// TODO
										switch (runNum) {
										case 0:
											try {
												wifiUtlis.sendMessage(Utlis.getbyteList(
														sendInfo.get(0), 0));
												new Thread(sendFileThread)
														.start();
												// sendFileThread.start();
												builder.setMessage(getString(R.string.run_exp_send_info));
												builder.setCancelable(false);
												dialog = builder.show();
												runNum = 1;
											} catch (Exception e1) {
												Toast.makeText(
														RunExperimentActivity.this,
														getString(R.string.wifi_error),
														Toast.LENGTH_SHORT)
														.show();
											}
											break;
										case 1:
											try {
												selectInfoFlag = true;
												Log.i("info",
														"waitTimeControl:"
																+ waitTimeControl);
												Log.i("info",
														"blendTimeControl:"
																+ blendTimeControl);
												Log.i("info",
														"magnetTimeControl:"
																+ magnetTimeControl);
												if (waitTimeControl != 0
														|| blendTimeControl != 0
														|| magnetTimeControl != 0) {
													continueControl = 1;
												} else {
													continueControl = 0;
												}
												if (waitTimeControl == 1) {
													waitTimeCount.resume();
													waitTimeControl = 0;
												}
												if (blendTimeControl == 1) {
													blendTimeCount.resume();
													blendTimeControl = 0;
												}
												if (magnetTimeControl == 1) {
													magnetTimeCount.resume();
													magnetTimeControl = 0;
												}
												new Thread(selectInfoThread) .start();
												viewDrawable.start();
												try {
													Thread.sleep(50);
												} catch (InterruptedException e) {
													// block
													e.printStackTrace();
												}

												timeHandler.post(timeRunnable);
//												machineStateInfo.pauseflag = false;
												wifiUtlis.sendMessage(Utlis
														.sendContinueMessage());

											} catch (Exception e) {
												Toast.makeText(
														RunExperimentActivity.this,
														getString(R.string.wifi_error),
														Toast.LENGTH_SHORT)
														.show();
												// inn();
												selectInfoFlag = false;
											}
											break;
										case 2:
											try {
//												machineStateInfo.pauseflag = true;
												selectInfoFlag = true;
												new Thread(selectInfoThread)
														.start();
												wifiUtlis.sendMessage(Utlis
														.sendRunMessage(controlNum));
//												machineStateInfo.pauseflag = false;
											} catch (Exception e) {
												Toast.makeText(
														RunExperimentActivity.this,
														getString(R.string.wifi_error),
														Toast.LENGTH_SHORT)
														.show();
												// inn();
												selectInfoFlag = false;
											}
											runNum = 1;
											break;
										default:
											break;
										}
									} else {
										Toast.makeText(
												RunExperimentActivity.this,
												getString(R.string.wifi_error),
												Toast.LENGTH_SHORT).show();
									}
								}
												machineStateInfo.pauseflag = false;
							}
						}
					}
				});

		// TODO stopBtn
		experiment_run_body_right_bottom_stop_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (runBtnControl == 0) {
							Toast.makeText(RunExperimentActivity.this,
									getString(R.string.run_exp_not_run),
									Toast.LENGTH_SHORT).show();
						} else {
							try {
								stopBtn = 1;
								startTimeControl = 0;
								if (selectInfoFlag == false) {
									selectInfoFlag = true;
									new Thread(selectInfoThread).start();
								}
								wifiUtlis.sendMessage(Utlis.sendStopMessage());
								builder.setMessage(getString(R.string.run_exp_stopping));
								builder.setCancelable(false);
								dialog = builder.show();
							} catch (Exception e) {
								Toast.makeText(RunExperimentActivity.this,
										getString(R.string.wifi_error),
										Toast.LENGTH_SHORT).show();
								// inn();
								selectInfoFlag = false;
							}
						}
					}
				});

		experiment_run_body_right_bottom_back_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (runBtnControl == 0) {
							if (wifiUtlis != null) {
								wifiUtlis.close();
								selectInfoFlag = false;
								sendFileFlag = false;
							}
							Intent intent = null;
							if (jump.equals("quick")) {
								intent = new Intent(RunExperimentActivity.this,
										MainActivity.class);
								MainActivity.id = 2;
							} else {
								intent = new Intent(RunExperimentActivity.this,
										ExperimentActivity.class);
							}
							intent.putExtra("U_id", U_id);
							intent.putExtra("Uname", Uname);
							startActivity(intent);
							finish();
						} else {
							Toast.makeText(RunExperimentActivity.this,
									getString(R.string.run_exp_not_exit),
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		waitDialog.cancel();
		try {
			wifiUtlis = new WifiUtlis(RunExperimentActivity.this);
		} catch (Exception e2) {
			Toast.makeText(RunExperimentActivity.this,
					getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
		}
	}

	public void initView() {

		selectInfoThread = new SelectInfoThread();
		stepDao = new StepDao();
		experimentDao = new ExperimentDao();

		Intent intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");
		jump = intent.getStringExtra("jump");
		E_id = intent.getIntExtra("E_id", 9999);

		steps = stepDao.getAllStep(RunExperimentActivity.this, E_id);

		views = new ArrayList<View>();
		changeBg = new ArrayList<Map<String, Object>>();
		holders = new ArrayList<RunViewHolder>();

		waitTimeList = new ArrayList<WaitTimeCount>();
		blendTimeList = new ArrayList<BlendTimeCount>();
		magnetTimeList = new ArrayList<MagnetTimeCount>();

		builder = new AlertDialog.Builder(RunExperimentActivity.this);
		stepRow = new TableRow(this);

		experiment = experimentDao.getExperimentById(E_id,
				RunExperimentActivity.this, U_id);

		sendInfo = Utlis.getPadExperimentInfoList(experiment, steps, "");

		experiment_run_body_left_body_time_info_tv = (TextView) findViewById(R.id.experiment_run_body_left_body_time_info_tv);
		experiment_run_top_uname_tv = (TextView) findViewById(R.id.experiment_run_top_uname_tv);
		experiment_run_body_left_top_name_tv = (TextView) findViewById(R.id.experiment_run_body_left_top_name_tv);
		experiment_run_top_mstate_tv = (TextView) findViewById(R.id.experiment_run_top_mstate_tv);
		experiment_run_body_right_body_tl = (TableLayout) findViewById(R.id.experiment_run_body_righr_body_tl);
		experiment_run_body_right_bottom_back_btn = (Button) findViewById(R.id.experiment_run_body_righr_bottom_back_btn);
		experiment_run_body_right_bottom_run_btn = (Button) findViewById(R.id.experiment_run_body_righr_bottom_run_btn);
		experiment_run_body_right_bottom_stop_btn = (Button) findViewById(R.id.experiment_run_body_righr_bottom_reset_btn);
		experiment_run_body_right_body_hsv = (HorizontalScrollView) findViewById(R.id.experiment_run_body_righr_body_hsv);
		experiment_run_body_left_body_t1_info_tv = (TextView) findViewById(R.id.experiment_run_body_left_body_t1_info_tv);
		experiment_run_body_left_body_t2_info_tv = (TextView) findViewById(R.id.experiment_run_body_left_body_t2_info_tv);
		experiment_run_body_left_body_t3_info_tv = (TextView) findViewById(R.id.experiment_run_body_left_body_t3_info_tv);
		experiment_run_body_left_body_t4_info_tv = (TextView) findViewById(R.id.experiment_run_body_left_body_t4_info_tv);
		experiment_run_body_left_body_t5_info_tv = (TextView) findViewById(R.id.experiment_run_body_left_body_t5_info_tv);
		experiment_run_body_left_body_t6_info_tv = (TextView) findViewById(R.id.experiment_run_body_left_body_t6_info_tv);
		experiment_run_body_left_body_t7_info_tv = (TextView) findViewById(R.id.experiment_run_body_left_body_t7_info_tv);
		experiment_run_body_left_body_t8_info_tv = (TextView) findViewById(R.id.experiment_run_body_left_body_t8_info_tv);

		experiment_run_body_left_temp_body = (LinearLayout) findViewById(R.id.experiment_run_body_left_temp_body);
		
		experiment_run_body_left_temp_body_bg = (ImageView)findViewById(R.id.experiment_run_body_left_temp_body_bg);
		experiment_run_body_left_body_t1_rl = (RelativeLayout)findViewById(R.id.experiment_run_body_left_body_t1_rl);
		experiment_run_body_left_body_t2_rl = (RelativeLayout)findViewById(R.id.experiment_run_body_left_body_t2_rl);
		experiment_run_body_left_body_t3_rl = (RelativeLayout)findViewById(R.id.experiment_run_body_left_body_t3_rl);
		experiment_run_body_left_body_t4_rl = (RelativeLayout)findViewById(R.id.experiment_run_body_left_body_t4_rl);
		experiment_run_body_left_body_t5_rl = (RelativeLayout)findViewById(R.id.experiment_run_body_left_body_t5_rl);
		experiment_run_body_left_body_t6_rl = (RelativeLayout)findViewById(R.id.experiment_run_body_left_body_t6_rl);
		experiment_run_body_left_body_t7_rl = (RelativeLayout)findViewById(R.id.experiment_run_body_left_body_t7_rl);
		experiment_run_body_left_body_t8_rl = (RelativeLayout)findViewById(R.id.experiment_run_body_left_body_t8_rl);
		hideTempBody();
		getTime(0);
	}

	/**
	 * 
	 * Title: hideTempBody Description: Modified By： Domon Modified Date:
	 * 2014-9-18
	 */
	private void hideTempBody() {
		if (fluxNum == 1 || fluxNum == 3) {
			experiment_run_body_left_body_t1_rl.setVisibility(View.GONE);
			experiment_run_body_left_body_t2_rl.setVisibility(View.GONE);
			experiment_run_body_left_body_t3_rl.setVisibility(View.GONE);
			experiment_run_body_left_body_t4_rl.setVisibility(View.GONE);
			experiment_run_body_left_body_t5_rl.setVisibility(View.GONE);
			experiment_run_body_left_body_t6_rl.setVisibility(View.GONE);
			experiment_run_body_left_body_t7_rl.setVisibility(View.GONE);
			experiment_run_body_left_body_t8_rl.setVisibility(View.GONE);
			experiment_run_body_left_temp_body_bg.setVisibility(View.VISIBLE);
		}
	}

	private void createTable() {
		experiment_run_body_right_body_hsv.scrollTo(0, 0);
		for (int i = 0; i < steps.size(); i++) {
			final RunViewHolder holder = new RunViewHolder();
			View view = LayoutInflater.from(RunExperimentActivity.this)
					.inflate(R.layout.activity_experiment_run_item, null);
			holder.experiment_run_item_rl = (RelativeLayout) view
					.findViewById(R.id.experiment_run_item_rl);
			holder.experiment_run_item_top_name_tv = (TextView) view
					.findViewById(R.id.experiment_run_item_top_name_tv);
			holder.experiment_run_item_head_wait_pb = (ProgressBar) view
					.findViewById(R.id.experiment_run_item_head_wait_pb);
			holder.experiment_run_item_head_wait_info_tv = (TextView) view
					.findViewById(R.id.experiment_run_item_head_wait_info_tv);
			holder.experiment_run_item_head_blend_pb = (ProgressBar) view
					.findViewById(R.id.experiment_run_item_head_blend_pb);
			holder.experiment_run_item_head_blend_info_tv = (TextView) view
					.findViewById(R.id.experiment_run_item_head_blend_info_tv);
			holder.experiment_run_item_head_magnet_pb = (ProgressBar) view
					.findViewById(R.id.experiment_run_item_head_magnet_pb);
			holder.experiment_run_item_head_magnet_info_tv = (TextView) view
					.findViewById(R.id.experiment_run_item_head_magnet_info_tv);
			holder.experiment_run_item_body_hole_info_tv = (TextView) view
					.findViewById(R.id.experiment_run_item_body_hole_info_tv);
			holder.experiment_run_item_body_vol_info_et = (TextView) view
					.findViewById(R.id.experiment_run_item_body_vol_info_et);
			holder.experiment_run_item_body_speed_info_tv = (TextView) view
					.findViewById(R.id.experiment_run_item_body_speed_info_tv);
			holder.experiment_run_item_body_temp_info_et = (TextView) view
					.findViewById(R.id.experiment_run_item_body_temp_info_et);
			holder.experiment_run_item_body_switch_info_tv = (TextView) view
					.findViewById(R.id.experiment_run_item_body_switch_info_tv);
			holder.experiment_run_item_bottom_name_et = (TextView) view
					.findViewById(R.id.experiment_run_item_bottom_name_et);

			holder.experiment_run_item_body_temp_rl = (RelativeLayout) view
					.findViewById(R.id.experiment_run_item_body_temp_rl);
			holder.experiment_run_item_body_switch_rl = (RelativeLayout) view
					.findViewById(R.id.experiment_run_item_body_switch_rl);

			// 通量属性展示 1-->15 3-->48
			if (fluxNum == 1 || fluxNum == 3) {
				holder.experiment_run_item_body_temp_rl
						.setVisibility(View.GONE);
				holder.experiment_run_item_body_switch_rl
						.setVisibility(View.GONE);
			}

			holder.experiment_run_item_top_name_tv.setText("No.  " + (i + 1));

			holder.experiment_run_item_rl
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							controlNum = Integer
									.valueOf(holder.experiment_run_item_top_name_tv
											.getText()
											.toString()
											.substring(
													6,
													holder.experiment_run_item_top_name_tv
															.getText()
															.toString()
															.length())) - 1;
							if (changeBg.size() == 0) {
								Map<String, Object> map = new HashMap<String, Object>();
								holder.experiment_run_item_rl
										.setBackgroundResource(R.drawable.run_bg_zi);
								map.put("view", holder.experiment_run_item_rl);
								map.put("id", controlNum);
								changeBg.add(map);
							} else {
								if ((Integer) changeBg.get(0).get("id") == controlNum) {
									((View) changeBg.get(0).get("view"))
											.setBackgroundResource(R.anim.anim_view);
									controlNum = 0;
									changeBg.removeAll(changeBg);
								} else {
									((View) changeBg.get(0).get("view"))
											.setBackgroundResource(R.anim.anim_view);
									changeBg.removeAll(changeBg);
									Map<String, Object> map = new HashMap<String, Object>();
									holder.experiment_run_item_rl
											.setBackgroundResource(R.drawable.run_bg_zi);
									map.put("view",
											holder.experiment_run_item_rl);
									map.put("id", controlNum);
									changeBg.add(map);
								}
							}
						}
					});
			if (steps.get(i).getSwait().equals("00:00:00")) {
				holder.experiment_run_item_head_wait_pb.setProgress(0);
			} else {
				try {
					// TODO 设置进度条时间
					// Log.w(TAG,
					// "Stotal:"
					// + Utlis.timeFormat.parse(
					// steps.get(i).getSwait()).getHours()
					// * 3600
					// + Utlis.timeFormat.parse(
					// steps.get(i).getSwait())
					// .getMinutes()
					// * 60
					// + Utlis.timeFormat.parse(
					// steps.get(i).getSwait())
					// .getSeconds());
					holder.experiment_run_item_head_wait_pb
							.setMax(Utlis.timeFormat.parse(
									steps.get(i).getSwait()).getHours()
									* 3600
									+ Utlis.timeFormat.parse(
											steps.get(i).getSwait())
											.getMinutes()
									* 60
									+ Utlis.timeFormat.parse(
											steps.get(i).getSwait())
											.getSeconds());
					holder.experiment_run_item_head_wait_pb
							.setProgress(Utlis.timeFormat.parse(
									steps.get(i).getSwait()).getHours()
									* 3600
									+ Utlis.timeFormat.parse(
											steps.get(i).getSwait())
											.getMinutes()
									* 60
									+ Utlis.timeFormat.parse(
											steps.get(i).getSwait())
											.getSeconds());
					// Log.w(TAG,
					// "Sprogress:"
					// + Utlis.timeFormat.parse(
					// steps.get(i).getSwait()).getHours()
					// * 3600
					// + Utlis.timeFormat.parse(
					// steps.get(i).getSwait())
					// .getMinutes()
					// * 60
					// + Utlis.timeFormat.parse(
					// steps.get(i).getSwait())
					// .getSeconds());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			holder.experiment_run_item_head_wait_info_tv.setText(steps.get(i)
					.getSwait());

			if (steps.get(i).getSblend().equals("00:00:00")) {
				holder.experiment_run_item_head_blend_pb.setProgress(0);
			} else {
				try {
					// TODO
					// Log.w(TAG,
					// "Btotal:"
					// + Utlis.timeFormat.parse(
					// steps.get(i).getSblend())
					// .getHours()
					// * 3600
					// + Utlis.timeFormat.parse(
					// steps.get(i).getSblend())
					// .getMinutes()
					// * 60
					// + Utlis.timeFormat.parse(
					// steps.get(i).getSblend())
					// .getSeconds());
					holder.experiment_run_item_head_blend_pb
							.setMax(Utlis.timeFormat.parse(
									steps.get(i).getSblend()).getHours()
									* 3600
									+ Utlis.timeFormat.parse(
											steps.get(i).getSblend())
											.getMinutes()
									* 60
									+ Utlis.timeFormat.parse(
											steps.get(i).getSblend())
											.getSeconds());
					holder.experiment_run_item_head_blend_pb
							.setProgress(Utlis.timeFormat.parse(
									steps.get(i).getSblend()).getHours()
									* 3600
									+ Utlis.timeFormat.parse(
											steps.get(i).getSblend())
											.getMinutes()
									* 60
									+ Utlis.timeFormat.parse(
											steps.get(i).getSblend())
											.getSeconds());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			holder.experiment_run_item_head_blend_info_tv.setText(steps.get(i)
					.getSblend());

			if ((i + 1) == steps.size()) {
				holder.experiment_run_item_head_magnet_pb.setProgress(0);
				holder.experiment_run_item_head_magnet_info_tv
						.setText("00:00:00");
			} else {
				if (steps.get(i).getSmagnet().equals("00:00:00")) {
					holder.experiment_run_item_head_magnet_pb.setProgress(0);
				} else {
					// TODO 磁吸进度条
					try {
						holder.experiment_run_item_head_magnet_pb
								.setMax(Utlis.timeFormat.parse(
										steps.get(i).getSmagnet()).getHours()
										* 3600
										+ Utlis.timeFormat.parse(
												steps.get(i).getSmagnet())
												.getMinutes()
										* 60
										+ Utlis.timeFormat.parse(
												steps.get(i).getSmagnet())
												.getSeconds());
						holder.experiment_run_item_head_magnet_pb
								.setProgress(Utlis.timeFormat.parse(
										steps.get(i).getSmagnet()).getHours()
										* 3600
										+ Utlis.timeFormat.parse(
												steps.get(i).getSmagnet())
												.getMinutes()
										* 60
										+ Utlis.timeFormat.parse(
												steps.get(i).getSmagnet())
												.getSeconds());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				holder.experiment_run_item_head_magnet_info_tv.setText(steps
						.get(i).getSmagnet());
			}
			holder.experiment_run_item_body_hole_info_tv.setText(steps.get(i)
					.getShole() + "");
			holder.experiment_run_item_body_vol_info_et.setText(steps.get(i)
					.getSvol() + "ul");
			holder.experiment_run_item_body_speed_info_tv.setText(steps.get(i)
					.getSspeed() + getString(R.string.exp_speed_unit));
			holder.experiment_run_item_body_temp_info_et.setText(steps.get(i)
					.getStemp() + getString(R.string.exp_temp_unit));
			holder.experiment_run_item_body_switch_info_tv
					.setText(getSwitch(steps.get(i).getSswitch()));
			holder.experiment_run_item_bottom_name_et.setText(steps.get(i)
					.getSname());
			views.add(holder.experiment_run_item_rl);
			holders.add(holder);
			stepRow.addView(view);
		}

		experiment_run_body_right_body_tl.addView(stepRow,
				new TableLayout.LayoutParams(FP, WC));
		for (int i = 0; i < holders.size(); i++) {
			// 设置混合时间
			try {
				date = Utlis.timeFormat
						.parse(holders.get(i).experiment_run_item_head_blend_info_tv
								.getText().toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			blendTimeCount = new BlendTimeCount(date.getHours() * 3600000
					+ date.getMinutes() * 60000 + (date.getSeconds() - 2)
					* 1000, 1000);

			// 设置磁吸时间
			try {
				date = Utlis.timeFormat
						.parse(holders.get(i).experiment_run_item_head_magnet_info_tv
								.getText().toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			magnetTimeCount = new MagnetTimeCount(date.getHours() * 3600000
					+ date.getMinutes() * 60000 + (date.getSeconds() - 2)
					* 1000, 1000);
			// 设置等待时间
			try {
				date = Utlis.timeFormat
						.parse(holders.get(i).experiment_run_item_head_wait_info_tv
								.getText().toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			waitTimeCount = new WaitTimeCount(date.getHours() * 3600000
					+ date.getMinutes() * 60000 + (date.getSeconds() - 2)
					* 1000, 1000);

			waitTimeList.add(waitTimeCount);
			blendTimeList.add(blendTimeCount);
			magnetTimeList.add(magnetTimeCount);
		}
	}

	private String getSwitch(int id) {
		String info = null;
		switch (id) {
		case 0:
			info = getString(R.string.exp_closs);
			break;
		case 1:
			info = getString(R.string.exp_crack);
			break;
		case 2:
			info = getString(R.string.exp_elution);
			break;
		default:
			break;
		}
		return info;
	}

	// 倒计时
	Runnable timeRunnable = new Runnable() {
		public void run() {
			sec--;
			if (sec < 0) {
				min--;
				if (min < 0) {
					hour--;
					if (hour < 0) {
						hour = 0;
					}
					min = 59;
				}
				sec = 59;
			}
			if (hour <= 9) {
				time = "0" + hour + ":";
			} else {
				time = hour + ":";
			}
			if (min <= 9) {
				time = time + "0" + min + ":";
			} else {
				time = time + min + ":";
			}
			if (sec <= 9) {
				time = time + "0" + sec;
			} else {
				time = time + sec;
			}
			experiment_run_body_left_body_time_info_tv.setText(time);
			timeHandler.postDelayed(timeRunnable, 1000);
			if (time.equals("00:00:00")) {
				timeHandler.removeCallbacks(timeRunnable);
			}
		}
	};

	class WaitTimeCount extends AdvancedCountdownTimer {
		public WaitTimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onTick(long millisUntilFinished, int percent) {
			if (continueControl == 1) {
				waitTimeControl = 0;
			} else {
				waitTimeControl = 1;
			}
			magnetTimeControl = 0;
			blendTimeControl = 0;
			Date date = new Date(millisUntilFinished);
			date.setHours(date.getHours() - 8);
			// 用时间格式对象转换成显示需要的String对象
			String dtime = Utlis.timeFormat.format(date);
			// 将转换后的信息传给step运行时间的textview
			holders.get(controlNum).experiment_run_item_head_wait_info_tv
					.setText(dtime);
			holders.get(controlNum).experiment_run_item_head_wait_pb
					.setProgress(date.getHours() * 3600 + date.getMinutes()
							* 60 + date.getSeconds());
		}

		public void onFinish() {
			waitTimeCount.cancel();
			waitTimeControl = 0;
			holders.get(controlNum).experiment_run_item_head_wait_info_tv
					.setText("00:00:00");
			holders.get(controlNum).experiment_run_item_head_wait_pb
					.setProgress(0);
			if (holders.get(controlNum).experiment_run_item_head_blend_info_tv
					.getText().toString().equals("00:00:00")) {
				blendTimeCount = blendTimeList.get(controlNum);
				blendTimeCount.start();
				// Log.i("info", "等待完成启动混合");
			} else {
				blendTimeCount = blendTimeList.get(controlNum);
			}
		}
	}

	class BlendTimeCount extends AdvancedCountdownTimer {
		public BlendTimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onTick(long millisUntilFinished, int percent) {
			if (continueControl == 1) {
				blendTimeControl = 0;
			} else {
				blendTimeControl = 1;
			}
			magnetTimeControl = 0;
			waitTimeControl = 0;
			Date date = new Date(millisUntilFinished);
			date.setHours(date.getHours() - 8);
			String dtime = Utlis.timeFormat.format(date);
			holders.get(controlNum).experiment_run_item_head_blend_info_tv
					.setText(dtime);
			holders.get(controlNum).experiment_run_item_head_blend_pb
					.setProgress(date.getHours() * 3600 + date.getMinutes()
							* 60 + date.getSeconds());
		}

		public void onFinish() {
			blendTimeCount.cancel();
			blendTimeControl = 0;
			holders.get(controlNum).experiment_run_item_head_blend_info_tv
					.setText("00:00:00");
			holders.get(controlNum).experiment_run_item_head_blend_pb
					.setProgress(0);
			if (holders.get(controlNum).experiment_run_item_head_magnet_info_tv
					.getText().toString().equals("00:00:00")) {
				magnetTimeCount = magnetTimeList.get(controlNum);
				magnetTimeCount.start();
			} else {
				magnetTimeCount = magnetTimeList.get(controlNum);
			}
		}
	}

	class MagnetTimeCount extends AdvancedCountdownTimer {
		public MagnetTimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onTick(long millisUntilFinished, int percent) {
			if (continueControl == 1) {
				magnetTimeControl = 0;
			} else {
				magnetTimeControl = 1;
			}
			blendTimeControl = 0;
			waitTimeControl = 0;
			Date date = new Date(millisUntilFinished);
			date.setHours(date.getHours() - 8);
			String dtime = Utlis.timeFormat.format(date);
			// 将转换后的信息传给step运行时间的textview
			holders.get(controlNum).experiment_run_item_head_magnet_info_tv
					.setText(dtime);
			holders.get(controlNum).experiment_run_item_head_magnet_pb
					.setProgress(date.getHours() * 3600 + date.getMinutes()
							* 60 + date.getSeconds());
		}

		public void onFinish() {
			magnetTimeCount.cancel();
			magnetTimeControl = 0;
			holders.get(controlNum).experiment_run_item_head_magnet_info_tv
					.setText("00:00:00");
			holders.get(controlNum).experiment_run_item_head_magnet_pb
					.setProgress(0);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (runBtnControl != 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						RunExperimentActivity.this);
				builder.setTitle(getString(R.string.run_exp_not_exit));
				builder.setPositiveButton(getString(R.string.sure),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						RunExperimentActivity.this);
				builder.setTitle(getString(R.string.sure_exit));
				builder.setPositiveButton(getString(R.string.sure),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Process.killProcess(android.os.Process.myPid());
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
		if (keyCode == KeyEvent.KEYCODE_POWER) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					RunExperimentActivity.this);
			builder.setTitle(getString(R.string.run_exp_not_exit));
			builder.setPositiveButton(getString(R.string.sure),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (wifiUtlis != null) {
			wifiUtlis.close();
			selectInfoFlag = false;
			sendFileFlag = false;
		}
		// if(homePressReceiver != null){
		// try {
		// unregisterReceiver(homePressReceiver);
		// }
		// catch (Exception e) {
		// //Log.e(TAG,
		// "unregisterReceiver homePressReceiver failure :"+e.getCause());
		// }
		// }
	}

	class SelectInfoThread implements Runnable {
		public void run() {
			// Log.i("info", "selectInfoFlag"+selectInfoFlag);
			while (selectInfoFlag) {
				// Log.i("info", Thread.currentThread().getName());
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

	private void inn() {
		experiment_run_body_left_body_t1_info_tv.setText("");
		experiment_run_body_left_body_t2_info_tv.setText("");
		experiment_run_body_left_body_t3_info_tv.setText("");
		experiment_run_body_left_body_t4_info_tv.setText("");
		experiment_run_body_left_body_t5_info_tv.setText("");
		experiment_run_body_left_body_t6_info_tv.setText("");
		experiment_run_body_left_body_t7_info_tv.setText("");
		experiment_run_body_left_body_t8_info_tv.setText("");
		selectInfoFlag = false;
		experiment_run_body_right_bottom_run_btn
				.setText(getString(R.string.run));
		sendControlNum = 0;
		startTimeControl = 0;
		waitTimeControl = 0;
		blendTimeControl = 0;
		magnetTimeControl = 0;
		runBtnControl = 0;
		continueControl = 0;
		runNum = 2;
		stopBtn = 0;
		for (int i = 0; i < waitTimeList.size(); i++) {
			waitTimeList.get(i).cancel();
		}
		for (int i = 0; i < blendTimeList.size(); i++) {
			blendTimeList.get(i).cancel();
		}
		for (int i = 0; i < magnetTimeList.size(); i++) {
			magnetTimeList.get(i).cancel();
		}
		if (waitTimeCount != null) {
			waitTimeCount.cancel();
			waitTimeCount = null;
		}
		if (blendTimeCount != null) {
			blendTimeCount.cancel();
			blendTimeCount = null;
		}
		if (magnetTimeCount != null) {
			magnetTimeCount.cancel();
			magnetTimeCount = null;
		}
		if (waitTimeList.size() != 0) {
			waitTimeList.removeAll(waitTimeList);
		}
		if (blendTimeList.size() != 0) {
			blendTimeList.removeAll(blendTimeList);
		}
		if (magnetTimeList.size() != 0) {
			magnetTimeList.removeAll(magnetTimeList);
		}
		if (views.size() != 0) {
			views.removeAll(views);
		}
		if (holders.size() != 0) {
			holders.removeAll(holders);
		}
		if (changeBg.size() != 0) {
			changeBg.removeAll(changeBg);
		}
		stepRow.removeAllViews();
		views.removeAll(views);
		holders.removeAll(holders);
		timeHandler.removeCallbacks(timeRunnable);
		getTime(0);
		experiment_run_body_right_body_tl.removeAllViews();
		viewDrawable = null;
		controlNum = 0;
		createTable();

		if (viewDrawable != null) {
			if (viewDrawable.isRunning()) {
				viewDrawable.stop();
			}
		}
		if (dialog != null) {
			dialog.cancel();
			Toast.makeText(RunExperimentActivity.this,
					getString(R.string.run_exp_stop_success),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void getTime(int colNum) {
		hour = 0;
		min = 0;
		sec = 0;
		for (int j = colNum; j < (steps.size() - colNum); j++) {
			try {
				sec = sec
						+ Utlis.timeFormat.parse(steps.get(j).getSwait())
								.getSeconds();
				if (sec >= 60) {
					min++;
					if (min >= 60) {
						hour++;
						min = 0;
					}
					sec = sec - 60;
				}
				min = min
						+ Utlis.timeFormat.parse(steps.get(j).getSwait())
								.getMinutes();
				if (min >= 60) {
					hour++;
					min = min - 60;
				}
				hour = hour
						+ Utlis.timeFormat.parse(steps.get(j).getSwait())
								.getHours();
				sec = sec
						+ Utlis.timeFormat.parse(steps.get(j).getSblend())
								.getSeconds();
				if (sec >= 60) {
					min++;
					if (min >= 60) {
						hour++;
						min = 0;
					}
					sec = sec - 60;
				}
				min = min
						+ Utlis.timeFormat.parse(steps.get(j).getSblend())
								.getMinutes();
				if (min >= 60) {
					hour++;
					min = min - 60;
				}
				hour = hour
						+ Utlis.timeFormat.parse(steps.get(j).getSblend())
								.getHours();

				if ((j + 1) < (steps.size() - colNum)) {
					sec = sec
							+ Utlis.timeFormat.parse(steps.get(j).getSmagnet())
									.getSeconds();
					if (sec >= 60) {
						min++;
						if (min >= 60) {
							hour++;
							min = 0;
						}
						sec = sec - 60;
					}
					min = min
							+ Utlis.timeFormat.parse(steps.get(j).getSmagnet())
									.getMinutes();
					if (min >= 60) {
						hour++;
						min = min - 60;
					}
					hour = hour
							+ Utlis.timeFormat.parse(steps.get(j).getSmagnet())
									.getHours();
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (hour <= 9) {
			time = "0" + hour + ":";
		} else {
			time = hour + ":";
		}
		if (min <= 9) {
			time = time + "0" + min + ":";
		} else {
			time = time + min + ":";
		}
		if (sec <= 9) {
			time = time + "0" + sec;
		} else {
			time = time + sec;
		}
		experiment_run_body_left_body_time_info_tv.setText(time);
	}

	private void getCurrentTime(int colNum) {
		hour = 0;
		min = 0;
		sec = 0;
		for (int j = colNum; j < steps.size(); j++) {
			try {
				sec = sec
						+ Utlis.timeFormat.parse(steps.get(j).getSwait())
								.getSeconds();
				if (sec >= 60) {
					min++;
					if (min >= 60) {
						hour++;
						min = 0;
					}
					sec = sec - 60;
				}
				min = min
						+ Utlis.timeFormat.parse(steps.get(j).getSwait())
								.getMinutes();
				if (min >= 60) {
					hour++;
					min = min - 60;
				}
				hour = hour
						+ Utlis.timeFormat.parse(steps.get(j).getSwait())
								.getHours();
				sec = sec
						+ Utlis.timeFormat.parse(steps.get(j).getSblend())
								.getSeconds();
				if (sec >= 60) {
					min++;
					if (min >= 60) {
						hour++;
						min = 0;
					}
					sec = sec - 60;
				}
				min = min
						+ Utlis.timeFormat.parse(steps.get(j).getSblend())
								.getMinutes();
				if (min >= 60) {
					hour++;
					min = min - 60;
				}
				hour = hour
						+ Utlis.timeFormat.parse(steps.get(j).getSblend())
								.getHours();

				sec = sec
						+ Utlis.timeFormat.parse(steps.get(j).getSmagnet())
								.getSeconds();
				if (sec >= 60) {
					min++;
					if (min >= 60) {
						hour++;
						min = 0;
					}
					sec = sec - 60;
				}
				min = min
						+ Utlis.timeFormat.parse(steps.get(j).getSmagnet())
								.getMinutes();
				if (min >= 60) {
					hour++;
					min = min - 60;
				}
				hour = hour
						+ Utlis.timeFormat.parse(steps.get(j).getSmagnet())
								.getHours();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (hour <= 9) {
			time = "0" + hour + ":";
		} else {
			time = hour + ":";
		}
		if (min <= 9) {
			time = time + "0" + min + ":";
		} else {
			time = time + min + ":";
		}
		if (sec <= 9) {
			time = time + "0" + sec;
		} else {
			time = time + sec;
		}
		Toast.makeText(RunExperimentActivity.this, time, 2000).show();
		experiment_run_body_left_body_time_info_tv.setText(time);
	}

	// TODO
	public void selectMachineStateInfo() {
		if (machineStateInfo == null) {
			machineStateInfo = new MachineStateInfo(RunExperimentActivity.this,
					experiment_run_top_mstate_tv);
		}
		// MachineStateInfo machineStateInfo = new MachineStateInfo(
		// ExperimentActivity.this, experiment_run_top_mstate_tv);
		machineStateInfo.queryState();
	}

}

class RunViewHolder {
	RelativeLayout experiment_run_item_rl;
	TextView experiment_run_item_top_name_tv;
	ProgressBar experiment_run_item_head_wait_pb;
	TextView experiment_run_item_head_wait_info_tv;
	ProgressBar experiment_run_item_head_blend_pb;
	TextView experiment_run_item_head_blend_info_tv;
	ProgressBar experiment_run_item_head_magnet_pb;
	TextView experiment_run_item_head_magnet_info_tv;
	TextView experiment_run_item_body_hole_info_tv;
	TextView experiment_run_item_body_vol_info_et;
	TextView experiment_run_item_body_speed_info_tv;
	TextView experiment_run_item_body_temp_info_et;
	TextView experiment_run_item_body_switch_info_tv;
	TextView experiment_run_item_bottom_name_et;

	RelativeLayout experiment_run_item_body_temp_rl;
	RelativeLayout experiment_run_item_body_switch_rl;
}
