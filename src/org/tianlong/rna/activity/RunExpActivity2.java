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
public class RunExpActivity2 extends Activity {

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

	// 温度显示
	private ImageView experiment_run_body_left_temp_body_bg;
	private RelativeLayout experiment_run_body_left_body_t1_rl;
	private RelativeLayout experiment_run_body_left_body_t2_rl;
	private RelativeLayout experiment_run_body_left_body_t3_rl;
	private RelativeLayout experiment_run_body_left_body_t4_rl;
	private RelativeLayout experiment_run_body_left_body_t5_rl;
	private RelativeLayout experiment_run_body_left_body_t6_rl;
	private RelativeLayout experiment_run_body_left_body_t7_rl;
	private RelativeLayout experiment_run_body_left_body_t8_rl;

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
	private List<RunExpViewHolder> holders;
	private Date date;
	private WifiUtlis wifiUtlis;
	private AlertDialog.Builder builder;
	private Dialog dialog;
	private Machine machine;
	private MachineDao machineDao;

	private List<String> infoList; // 转换后的文件列表
	private List<Experiment> experiments;
//	private GetExperimentInfoThread getExperimentInfoThread;

	private int fluxNum;
	private int hour;
	private int min;
	private int sec;
	private String time;

	private int exp_id;

	private boolean selectInfoFlag = true; // 接收查询线程控制
	private int startTimeControl, //
			waitTimeControl, // 等待时间控制
			blendTimeControl, // 混合时间控制
			magnetTimeControl, // 磁吸时间控制
			runBtnControl, // 运行按钮控制
			continueControl, // 继续按钮控制
			controlNum, // 总步骤索引
			stopBtn; // 停止控制

	private int E_id;
	private int U_id;
	private String Uname;
	private float t1, t2, t3, t4, t5, t6, t7, t8;
	private int control; // 电机位置控制
	private String receiveMeg; // 接收信息
	private int index; // 接收信息功能索引
	final IntentFilter homeFilter = new IntentFilter(
			Intent.ACTION_CLOSE_SYSTEM_DIALOGS);// home键监听

	private String TAGINFO = "INFO";

	// 将下位机实验数据转换成Pad实验数据handler
		private Handler getExperimentInfoHandler = new Handler() {
			public void handleMessage(Message msg) {
				Log.w("Handler", "handler");
				byte[] info = (byte[]) msg.obj;
				if (info.length != 0) {
					if (receive != null) {
						receive.removeAll(receive);
					}
					receive = Utlis.getReceive(info);
//					Log.w(TAG + "receive", receive + "");
					infoList = Utlis.getExperimentInfoList(receive);
					Log.w(TAG + "infoList", infoList + "");
					Experiment experiment = new Experiment();
					try {
						if (infoList.size() != 0) {
							Log.w("发送文件子串", infoList.get(infoList.size() - 2)
									.substring(0, 9) + "");
							Log.w("发送文件索引", infoList.get(infoList.size() - 2)
									.substring(0, 9).indexOf("#END_FILE")
									+ "");
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

								// TODO 得不到数据
								exp_id = experiment.getE_id();

								try {
									experimentDao.insertExperiment(experiment,
											RunExpActivity2.this);
								} catch (Exception e) {
									Log.w("插入异常", "插入异常");
								}

								experiment = experimentDao.getExperimentByCdate(
										date, RunExpActivity2.this, U_id);
								for (int i = 3; i < infoList.size(); i++) {
									if (infoList.get(i).indexOf("#END_FILE") != -1) {
										break;
									} else {
										Step step = Utlis.getStepFromInfo(
												infoList.get(i),
												experiment.getE_id());
										stepDao.insertStep(step,
												RunExpActivity2.this);
									}
								}
								experiments = experimentDao
										.getAllExperimentsByU_id(
												RunExpActivity2.this, U_id);
								Log.w("RunExp", "得到实验数据正常");

							} else {
								Log.w("RunExp", "得到实验数据失败");
							}
						}
					} catch (Exception e) {
						Log.w("异常", "异常----");
					}
				}
			};
		};
	
	// 读取下位机指定实验接受线程
//	class GetExperimentInfoThread implements Runnable {
//		public void run() {
//			while (true) {
//				try {
//					Log.w("Thread", "thread");
//					Message message = getExperimentInfoHandler.obtainMessage();
//					Log.w("Thread", "1"+ message);
//					message.obj = wifiUtlis.getByteMessage();
//					Log.w("Thread", "2"+ message.obj);
//					getExperimentInfoHandler.sendMessage(message);
//					Log.w("Thread", "3"+ getExperimentInfoHandler.sendMessage(message));
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		};
//	}
		
		private Thread getExperimentInfoThread = new Thread() {
			public void run() {
				while (true) {

					try {
						Log.w("Thread", "thread");
						Message message = getExperimentInfoHandler.obtainMessage();
						Log.w("Thread", "1"+ message);
						message.obj = wifiUtlis.getByteMessage();
						Log.w("Thread", "2"+ message.obj);
						getExperimentInfoHandler.sendMessage(message);
						Log.w("Thread", "3"+ getExperimentInfoHandler.sendMessage(message));
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		};

	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment_run);
		
		try {
			wifiUtlis = new WifiUtlis(RunExpActivity2.this);
			wifiUtlis.sendMessage(Utlis.sendExperimentId(3));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// TODO 查询数据得到
//		getExperimentInfoThread = new GetExperimentInfoThread();
		new Thread(getExperimentInfoThread).start();
//		new GetExperimentInfoThread().run();

		MainActivity.uvFlag = false;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		machineDao = new MachineDao();
		machine = machineDao.getMachine(RunExpActivity2.this);
		fluxNum = machine.getMflux();

		stepDao = new StepDao();
		experimentDao = new ExperimentDao();

		Log.w("实验id-----", exp_id + "");

		// TODO step为空
		steps = stepDao.getAllStep(RunExpActivity2.this, exp_id);

		views = new ArrayList<View>();
		changeBg = new ArrayList<Map<String, Object>>();
		holders = new ArrayList<RunExpViewHolder>();

		waitTimeList = new ArrayList<WaitTimeCount>();
		blendTimeList = new ArrayList<BlendTimeCount>();
		magnetTimeList = new ArrayList<MagnetTimeCount>();

		builder = new AlertDialog.Builder(RunExpActivity2.this);
		stepRow = new TableRow(this);

		experiment = experimentDao.getExperimentById(exp_id,
				RunExpActivity2.this, U_id);

		AlertDialog.Builder sbuilder = new AlertDialog.Builder(
				RunExpActivity2.this);
		sbuilder.setTitle(getString(R.string.waitting));
		Dialog waitDialog = sbuilder.show();
		waitDialog.dismiss();

		initView();
		experiment_run_body_right_bottom_back_btn = (Button) findViewById(R.id.experiment_run_body_righr_bottom_back_btn);
		experiment_run_body_right_body_tl.setStretchAllColumns(true);
		createTable();
		
		Log.w("on create end", "");

		// experiment_run_top_uname_tv.setText(Uname);
		// experiment_run_body_left_top_name_tv.setText("!!!");
	}

	public void initView() {

		// sendInfo = Utlis.getPadExperimentInfoList(experiment, steps, "");

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

		experiment_run_body_left_temp_body_bg = (ImageView) findViewById(R.id.experiment_run_body_left_temp_body_bg);
		experiment_run_body_left_body_t1_rl = (RelativeLayout) findViewById(R.id.experiment_run_body_left_body_t1_rl);
		experiment_run_body_left_body_t2_rl = (RelativeLayout) findViewById(R.id.experiment_run_body_left_body_t2_rl);
		experiment_run_body_left_body_t3_rl = (RelativeLayout) findViewById(R.id.experiment_run_body_left_body_t3_rl);
		experiment_run_body_left_body_t4_rl = (RelativeLayout) findViewById(R.id.experiment_run_body_left_body_t4_rl);
		experiment_run_body_left_body_t5_rl = (RelativeLayout) findViewById(R.id.experiment_run_body_left_body_t5_rl);
		experiment_run_body_left_body_t6_rl = (RelativeLayout) findViewById(R.id.experiment_run_body_left_body_t6_rl);
		experiment_run_body_left_body_t7_rl = (RelativeLayout) findViewById(R.id.experiment_run_body_left_body_t7_rl);
		experiment_run_body_left_body_t8_rl = (RelativeLayout) findViewById(R.id.experiment_run_body_left_body_t8_rl);

		experiment_run_top_mstate_tv.setVisibility(View.GONE);
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
			final RunExpViewHolder holder = new RunExpViewHolder();
			View view = LayoutInflater.from(RunExpActivity2.this).inflate(
					R.layout.activity_experiment_run_item, null);
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
							// 控制位置 获取
							try {
								controlNum = Integer
										.valueOf(holder.experiment_run_item_top_name_tv
												.getText()
												.toString()
												.substring(
														5,
														holder.experiment_run_item_top_name_tv
																.getText()
																.toString()
																.length())) - 1;
								Toast.makeText(getApplicationContext(),
										"步骤：" + (controlNum + 1), 1000).show();
								getCurrentTime(controlNum);
							} catch (Exception e) {
								e.printStackTrace();
							}
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
			String dtime1 = Utlis.timeFormat.format(date);

			blendTimeCount = new BlendTimeCount(date.getHours() * 3600000
					+ date.getMinutes() * 60000 + date.getSeconds() * 1000,
					1000);
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
			String dtime = Utlis.timeFormat.format(date);
			holders.get(controlNum).experiment_run_item_head_wait_info_tv
					.setText(dtime);
			holders.get(controlNum).experiment_run_item_head_wait_pb
					.setProgress(date.getHours() * 3600 + date.getMinutes()
							* 60 + date.getSeconds());
			// Log.w("WaitTime", date.getHours() * 3600 + date.getMinutes()
			// * 60 + date.getSeconds() + "");
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
			Log.w("BlendTime", date.getHours() * 3600 + date.getMinutes() * 60
					+ date.getSeconds() + "");
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
						RunExpActivity2.this);
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
						RunExpActivity2.this);
				builder.setTitle(getString(R.string.sure_exit));
				builder.setPositiveButton(getString(R.string.sure),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Process.killProcess(android.os.Process.myPid());
								finish();
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
					RunExpActivity2.this);
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
		// Toast.makeText(RunExperimentActivity.this, time, 2000).show();
		experiment_run_body_left_body_time_info_tv.setText(time);
	}

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

}

class RunExpViewHolder2 {
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
