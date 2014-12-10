package org.tianlong.rna.activity;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.tianlong.rna.adapter.ArrayWheelAdapter;
import org.tianlong.rna.dao.ExperimentDao;
import org.tianlong.rna.dao.MachineDao;
import org.tianlong.rna.dao.StepDao;
import org.tianlong.rna.pojo.DefaultExperiment;
import org.tianlong.rna.pojo.DefaultStep;
import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.pojo.Machine;
import org.tianlong.rna.pojo.Step;
import org.tianlong.rna.utlis.TimeWheelView;
import org.tianlong.rna.utlis.Utlis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class CreatExperimentActivity extends Activity {

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

	private Button experiment_new_main_bottom_insert_btn;
	private Button experiment_new_main_bottom_delete_btn;
	private Button experiment_new_main_bottom_save_btn;
	private Button experiment_new_main_bottom_back_btn;
	private TextView experiment_new_main_bottom_ename_info_tv;
	private EditText experiment_new_main_bottom_ename_info_et;
	private TextView experiment_new_main_top_uname_tv;
	private TableLayout experiment_new_main_body_tl;
	private TableRow stepRow;

	private int U_id;
	private String Uname;
	private int Equick;
	private int Enum;
	private String Ename;
	private String Eremark;
	private int DE_id;
	private int check;
	private int speed;
	private int switchInfo;
	private int stepChooseNum;
	private int fluxNum;
	private int volMax = 1000;
	private boolean deleteFlag = false;
	private boolean saveFlag = false;

	private StepDao stepDao;
	private ExperimentDao experimentDao;
	private List<DefaultStep> defaultSteps;
	private List<Step> steps;
	private List<CViewHolder> holders;
	private List<TextView> textViews;
	private Experiment experiment;
	private Dialog dialog;
	private Machine machine;
	private MachineDao machineDao;
	private List<DefaultExperiment> defaultExperiments;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment_new_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				CreatExperimentActivity.this);
		builder.setTitle("dsada");
		Dialog dialog = builder.show();
		experimentDao = new ExperimentDao();

		machineDao = new MachineDao();
		machine = machineDao.getMachine(CreatExperimentActivity.this);
		fluxNum = machine.getMflux();

		stepDao = new StepDao();
		Intent intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");
		Equick = intent.getIntExtra("Equick", 9999);
		Enum = intent.getIntExtra("Enum", 9999);
		Ename = intent.getStringExtra("Ename");
		Eremark = intent.getStringExtra("Eremark");
		DE_id = intent.getIntExtra("DE_id", 9999);
		defaultSteps = stepDao.getAllDStep(CreatExperimentActivity.this, DE_id,fluxNum);
		experiment = new Experiment();
		steps = new ArrayList<Step>();
		holders = new ArrayList<CViewHolder>();
		textViews = new ArrayList<TextView>();

		experiment_new_main_bottom_ename_info_tv = (TextView) findViewById(R.id.experiment_new_main_bottom_ename_info_tv);
		experiment_new_main_body_tl = (TableLayout) findViewById(R.id.experiment_new_main_body_tl);
		experiment_new_main_top_uname_tv = (TextView) findViewById(R.id.experiment_new_main_top_uname_tv);
		experiment_new_main_bottom_back_btn = (Button) findViewById(R.id.experiment_new_main_bottom_back_btn);
		experiment_new_main_bottom_save_btn = (Button) findViewById(R.id.experiment_new_main_bottom_save_btn);
		experiment_new_main_bottom_insert_btn = (Button) findViewById(R.id.experiment_new_main_bottom_insert_btn);
		experiment_new_main_bottom_delete_btn = (Button) findViewById(R.id.experiment_new_main_bottom_delete_btn);
		experiment_new_main_bottom_ename_info_et = (EditText) findViewById(R.id.experiment_new_main_bottom_ename_info_et);

		experiment_new_main_top_uname_tv.setText(Uname);

		experiment_new_main_bottom_ename_info_tv.setVisibility(View.GONE);
		experiment_new_main_bottom_ename_info_et.setVisibility(View.VISIBLE);
		experiment_new_main_bottom_ename_info_et.setText(Ename);

		experiment_new_main_body_tl.setStretchAllColumns(true);

		/**
		 * Modified By Domon Added the 15&48
		 */
		experiment_new_main_bottom_insert_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {

						experiment_new_main_body_tl.removeView(stepRow);
						Step step = new Step();
						switch (fluxNum) {
						case 1:
							step.setSname("Step");
							step.setShole(1);
							step.setSspeed(1);
							step.setSvol(30);
							step.setSwait("00:00:00");
							step.setSblend("00:00:00");
							step.setSmagnet("00:00:00");
							break;
						case 2:
							step.setSname("Step");
							step.setShole(1);
							step.setSspeed(1);
							step.setSvol(30);
							step.setSwait("00:00:00");
							step.setSblend("00:00:00");
							step.setSmagnet("00:00:00");
							step.setSswitch(0);
							step.setStemp(0);
							break;
						case 3:
							step.setSname("Step");
							step.setShole(1);
							step.setSspeed(1);
							step.setSvol(30);
							step.setSwait("00:00:00");
							step.setSblend("00:00:00");
							step.setSmagnet("00:00:00");
							break;
						}

						steps.add(step);
						Toast.makeText(CreatExperimentActivity.this, getString(R.string.exp_inset_success),2000).show();
						deleteFlag = true;
						saveFlag = true;
						createTable();
					}
				});

		experiment_new_main_bottom_delete_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								CreatExperimentActivity.this);
						deleteFlag = hasTemplet();
						if (steps.size() == 0) {
							builder.setTitle("当前没有步骤!");
							builder.setPositiveButton(getString(R.string.sure),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									});
							builder.show();
						} else {
							if (stepChooseNum == -1 || !hasTemplet()) {
								Log.w("w", !hasTemplet()+ "");
								Log.w("w", stepChooseNum+ "");
								//步骤step
								Log.w("w", stepChooseNum+ "");
								builder.setTitle(getString(R.string.exp_sure_delete_all));
								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												experiment_new_main_body_tl
														.removeView(stepRow);
												steps.removeAll(steps);
												defaultSteps
														.removeAll(defaultSteps);
												stepChooseNum = -1;
												if (textViews.size() != 0) {
													textViews
															.get(0)
															.setBackgroundColor(
																	getResources()
																			.getColor(
																					R.color.blue));
													textViews.remove(0);
												}
												Toast.makeText(
														CreatExperimentActivity.this,
														getString(R.string.exp_delete_success),
														Toast.LENGTH_SHORT)
														.show();
												deleteFlag = false;
												createTable();
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
							} else {
								builder.setTitle(getString(R.string.exp_sure_delete_this));
								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												experiment_new_main_body_tl
														.removeView(stepRow);
												steps.remove(stepChooseNum);
												stepChooseNum = -1;
												if (textViews.size() != 0) {
													textViews
															.get(0)
															.setBackgroundColor(
																	getResources()
																			.getColor(
																					R.color.blue));
													textViews.remove(0);
												}
												Toast.makeText(
														CreatExperimentActivity.this,
														getString(R.string.exp_delete_success),
														Toast.LENGTH_SHORT)
														.show();
												createTable();
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
							}
							builder.show();
						}
					}
				});

		//TODO save btn
		experiment_new_main_bottom_save_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						saveFlag = hasTemplet();
						if (steps.size() >= 50) {
							Toast.makeText(CreatExperimentActivity.this,
									"步骤不能超过50步，请检查",
									Toast.LENGTH_SHORT).show();
						}
						if (!saveFlag || (steps.size() == 0)) {
							Toast.makeText(CreatExperimentActivity.this,
									getString(R.string.exp_save_failure),
									Toast.LENGTH_SHORT).show();
						}
						if (steps.size() >= 50) {
							Toast.makeText(CreatExperimentActivity.this,
									"步骤不能超过50步，请检查",
									Toast.LENGTH_SHORT).show();
						}
						else {
							if (experiment_new_main_bottom_ename_info_et
									.getText().toString().equals("")) {
								Toast.makeText(CreatExperimentActivity.this,
										getString(R.string.exp_name_null),
										Toast.LENGTH_SHORT).show();
							}if (!getEnameIsNull()) {
//								Toast.makeText(
//										CreatExperimentActivity.this,
//										getString(R.string.exp_save_failure) + "---------",
//										Toast.LENGTH_SHORT).show();
							} 
							else {

								String systemTime = Utlis.systemFormat
										.format(new Date(System
												.currentTimeMillis()));
								experiment.setCdate(systemTime);
								experiment.setRdate(systemTime);
								experiment
										.setEname(experiment_new_main_bottom_ename_info_et
												.getText().toString());
								experiment.setEDE_id(Enum);
								experiment.setEquick(Equick);
								experiment.setEremark(Eremark);
								experiment.setU_id(U_id);
								experimentDao.insertExperiment(experiment,
										CreatExperimentActivity.this);
								experiment = experimentDao
										.getExperimentByCdate(systemTime,
												CreatExperimentActivity.this,
												U_id);

								boolean flag = false;
								for (int i = 0; i < steps.size(); i++) {
									if (steps.get(i).getSname().equals("")) {
										flag = true;
										break;
									}
								}
								if (flag) {
									Toast.makeText(
											CreatExperimentActivity.this,
											getString(R.string.exp_step_name_null),
											Toast.LENGTH_SHORT).show();
								} else {
									if (getEnameIsNull()) {
									if (getVolIsNull()) {
										if (getVolIsBig()) {
											if (getTempIsNull()) {
												if (getTempIsBig()) {
													for (int i = 0; i < steps
															.size(); i++) {
														steps.get(i)
																.setE_id(
																		experiment
																				.getE_id());
														steps.get(i)
																.setSvol(
																		Integer.valueOf(holders
																				.get(i).experiment_new_main_item_body_vol_info_et
																				.getText()
																				.toString()));
														steps.get(i)
																.setStemp(
																		Integer.valueOf(holders
																				.get(i).experiment_new_main_item_body_temp_info_et
																				.getText()
																				.toString()));
														stepDao.insertStep(
																steps.get(i),
																CreatExperimentActivity.this);
													}
													Intent intent = new Intent(
															CreatExperimentActivity.this,
															ExperimentActivity.class);
													intent.putExtra("U_id",
															U_id);
													intent.putExtra("Uname",
															Uname);
													startActivity(intent);
													finish();
													Toast.makeText(
															CreatExperimentActivity.this,
															getString(R.string.exp_save_success),
															Toast.LENGTH_SHORT)
															.show();
												} else {
													Toast.makeText(
															CreatExperimentActivity.this,
															getString(R.string.exp_temp_big),
															Toast.LENGTH_SHORT)
															.show();
												}
											} else {
												Toast.makeText(
														CreatExperimentActivity.this,
														getString(R.string.exp_temp_empty),
														Toast.LENGTH_SHORT)
														.show();
											}
										} else {
											Toast.makeText(
													CreatExperimentActivity.this,
													getString(R.string.exp_vol_big),
													Toast.LENGTH_SHORT).show();
										}

									} else {
										Toast.makeText(
												CreatExperimentActivity.this,
												getString(R.string.exp_vol_empty),
												Toast.LENGTH_SHORT).show();
									}
									} else {
										Toast.makeText(
												CreatExperimentActivity.this,
												getString(R.string.exp_save_failure),
												Toast.LENGTH_SHORT).show();
									}
								}
							}
						}
					}
				});

		experiment_new_main_bottom_back_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (experiment != null) {
							experimentDao.deleteExperiment(
									experiment.getE_id(),
									CreatExperimentActivity.this);
						}
						Intent intent = new Intent(
								CreatExperimentActivity.this,
								ExperimentActivity.class);
						intent.putExtra("U_id", U_id);
						intent.putExtra("Uname", Uname);
						startActivity(intent);
						finish();
					}
				});
		createTable();
		dialog.cancel();
	};

	/**
	 * 
	 * Title: hasTemplet Description: Modified By： Domon Modified Date:
	 * 2014-9-18
	 * 
	 * @return
	 */
	public boolean hasTemplet() {
		ExperimentDao experimentDao = new ExperimentDao();
		defaultExperiments = experimentDao
				.getAllDefaultExperiments(CreatExperimentActivity.this);
		if (defaultExperiments.size() == 0) {
			return false;
		}
		else {
			
			return true;
		}
	}

	private void createTable() {
		stepRow = new TableRow(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				CreatExperimentActivity.this);
		builder.setTitle(getString(R.string.waitting));
		Dialog waitDialog = builder.show();
		if (holders.size() != 0) {
			holders.removeAll(holders);
		}

		if (steps.size() == 0) {
			for (int i = 0; i < defaultSteps.size(); i++) {
				final int p = i;
				final Step step = new Step();
				LayoutInflater inflater = LayoutInflater
						.from(CreatExperimentActivity.this);
				final CViewHolder holder = new CViewHolder();
				View convertView = inflater.inflate(
						R.layout.activity_experiment_new_main_item, null);
				holder.experiment_new_main_item_top_name_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_top_name_tv);
				holder.experiment_new_main_item_head_wait_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_head_wait_info_tv);
				holder.experiment_new_main_item_head_blend_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_head_blend_info_tv);
				holder.experiment_new_main_item_head_magnet_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_head_magnet_info_tv);
				holder.experiment_new_main_item_body_hole_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_body_hole_info_tv);
				holder.experiment_new_main_item_body_vol_info_et = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_body_vol_info_et);
				holder.experiment_new_main_item_body_speed_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_body_speed_info_tv);
				holder.experiment_new_main_item_body_temp_info_et = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_body_temp_info_et);
				holder.experiment_new_main_item_body_switch_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_body_switch_info_tv);
				holder.experiment_new_main_item_bottom_name_et = (EditText) convertView
						.findViewById(R.id.experiment_new_main_item_bottom_name_et);

				holder.experiment_new_main_item_body_temp_rl = (RelativeLayout) convertView
						.findViewById(R.id.experiment_new_main_item_body_temp_rl);
				holder.experiment_new_main_item_body_switch_rl = (RelativeLayout) convertView
						.findViewById(R.id.experiment_new_main_item_body_switch_rl);
				// 通量属性展示 1-->15 3-->48
				if (fluxNum == 1 || fluxNum == 3) {
					holder.experiment_new_main_item_body_temp_rl
							.setVisibility(View.GONE);
					holder.experiment_new_main_item_body_switch_rl
							.setVisibility(View.GONE);
				}

				if (defaultSteps.get(i).getDSwait().equals("00:00:00")) {
					holder.experiment_new_main_item_head_wait_info_tv
							.setBackgroundResource(R.drawable.zero);
				}
				if (defaultSteps.get(i).getDSblend().equals("00:00:00")) {
					holder.experiment_new_main_item_head_blend_info_tv
							.setBackgroundResource(R.drawable.zero);
				}
				if (defaultSteps.get(i).getDSmagnet().equals("00:00:00")) {
					holder.experiment_new_main_item_head_magnet_info_tv
							.setBackgroundResource(R.drawable.zero);
				}

				holder.experiment_new_main_item_top_name_tv.setText("No.  "
						+ (i + 1));
				// 通量选择 模板
				if (defaultSteps.get(i).getDStemplet() == fluxNum) {
					holder.experiment_new_main_item_head_wait_info_tv
							.setText(defaultSteps.get(i).getDSwait());
					holder.experiment_new_main_item_head_blend_info_tv
							.setText(defaultSteps.get(i).getDSblend());
					holder.experiment_new_main_item_head_magnet_info_tv
							.setText(defaultSteps.get(i).getDSmagnet());
					holder.experiment_new_main_item_body_hole_info_tv
							.setText(defaultSteps.get(i).getDShole() + "");
					holder.experiment_new_main_item_body_vol_info_et
							.setText(defaultSteps.get(i).getDSvol() + "");
					holder.experiment_new_main_item_body_speed_info_tv
							.setText(defaultSteps.get(i).getDSspeed()
									+ getString(R.string.exp_speed_unit));
					holder.experiment_new_main_item_body_temp_info_et
							.setText(defaultSteps.get(i).getDStemp() + "");
					holder.experiment_new_main_item_body_switch_info_tv
							.setText(getSwitch(defaultSteps.get(i)
									.getDSswitch()));
					holder.experiment_new_main_item_bottom_name_et
							.setText(defaultSteps.get(i).getDSname());
				}

				holder.experiment_new_main_item_body_temp_info_et
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								if (!holder.experiment_new_main_item_body_switch_info_tv
										.getText().toString()
										.equals(getString(R.string.exp_closs))) {
									AlertDialog.Builder builder = new AlertDialog.Builder(
											CreatExperimentActivity.this);
									builder.setTitle(getString(R.string.exp_temp_setting));
									View view = LayoutInflater.from(
											CreatExperimentActivity.this)
											.inflate(R.layout.activity_temp,
													null);
									builder.setView(view);
									builder.setCancelable(false);
									final TimeWheelView temp_thousand = (TimeWheelView) view
											.findViewById(R.id.temp_thousand);
									final TimeWheelView temp_hundred = (TimeWheelView) view
											.findViewById(R.id.temp_hundred);
									final TimeWheelView temp_ten = (TimeWheelView) view
											.findViewById(R.id.temp_ten);
									String[] thousand = getResources()
											.getStringArray(
													R.array.thousand_array);
									String[] num = getResources()
											.getStringArray(R.array.num_array);

									temp_thousand
											.setAdapter(new ArrayWheelAdapter<String>(
													thousand));
									temp_hundred
											.setAdapter(new ArrayWheelAdapter<String>(
													num));
									temp_ten.setAdapter(new ArrayWheelAdapter<String>(
											num));

									temp_thousand.setCyclic(true);
									temp_hundred.setCyclic(true);
									temp_ten.setCyclic(true);

									String temp = holder.experiment_new_main_item_body_temp_info_et
											.getText().toString();
									int a = 0, b = 0, c = 0;
									switch (temp.length()) {
									case 1:
										a = 0;
										b = 0;
										c = Integer.valueOf(temp
												.substring(0, 1));
										break;
									case 2:
										a = 0;
										b = Integer.valueOf(temp
												.substring(0, 1));
										c = Integer.valueOf(temp
												.substring(1, 2));
										break;
									case 3:
										a = Integer.valueOf(temp
												.substring(0, 1));
										b = Integer.valueOf(temp
												.substring(1, 2));
										c = Integer.valueOf(temp
												.substring(2, 3));
										break;
									default:
										break;
									}
									temp_thousand.setCurrentItem(a);
									temp_hundred.setCurrentItem(b);
									temp_ten.setCurrentItem(c);

									builder.setPositiveButton(
											getString(R.string.sure),
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													int tempTotal = temp_thousand
															.getCurrentItem()
															* 100
															+ temp_hundred
																	.getCurrentItem()
															* 10
															+ temp_ten
																	.getCurrentItem();
													if (tempTotal > 120) {
														Toast.makeText(
																CreatExperimentActivity.this,
																getString(R.string.exp_temp_big),
																Toast.LENGTH_SHORT)
																.show();
														try {
															Field field = dialog
																	.getClass()
																	.getSuperclass()
																	.getDeclaredField(
																			"mShowing");
															field.setAccessible(true);
															field.set(dialog,
																	false);
														} catch (Exception e) {
															e.printStackTrace();
														}
													} 
													else if (tempTotal < 30) {
														Toast.makeText(
																CreatExperimentActivity.this,
																getString(R.string.exp_temp_small),
																Toast.LENGTH_SHORT)
																.show();
														try {
															Field field = dialog
																	.getClass()
																	.getSuperclass()
																	.getDeclaredField(
																			"mShowing");
															field.setAccessible(true);
															field.set(dialog,
																	false);
														} catch (Exception e) {
															e.printStackTrace();
														}	
													}
													else {
														steps.get(p).setStemp(
																tempTotal);
														holder.experiment_new_main_item_body_temp_info_et
																.setText(tempTotal
																		+ "");
														try {
															Field field = dialog
																	.getClass()
																	.getSuperclass()
																	.getDeclaredField(
																			"mShowing");
															field.setAccessible(true);
															field.set(dialog,
																	true);
														} catch (Exception e) {
															e.printStackTrace();
														}
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
													try {
														Field field = dialog
																.getClass()
																.getSuperclass()
																.getDeclaredField(
																		"mShowing");
														field.setAccessible(true);
														field.set(dialog, true);
													} catch (Exception e) {
														e.printStackTrace();
													}
												}
											});
									builder.show();
								}
							}
						});

				holder.experiment_new_main_item_body_vol_info_et
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_vol_setting));
								View view = LayoutInflater.from(
										CreatExperimentActivity.this).inflate(
										R.layout.activity_vol, null);
								builder.setView(view);
								builder.setCancelable(false);
								final TimeWheelView vol_thousand = (TimeWheelView) view
										.findViewById(R.id.vol_thousand);
								final TimeWheelView vol_hundred = (TimeWheelView) view
										.findViewById(R.id.vol_hundred);
								final TimeWheelView vol_ten = (TimeWheelView) view
										.findViewById(R.id.vol_ten);
								final TimeWheelView vol_bits = (TimeWheelView) view
										.findViewById(R.id.vol_bits);
								String[] thousand = getResources()
										.getStringArray(R.array.thousand_array);
								String[] num = getResources().getStringArray(
										R.array.num_array);

								vol_thousand
										.setAdapter(new ArrayWheelAdapter<String>(
												thousand));
								vol_hundred
										.setAdapter(new ArrayWheelAdapter<String>(
												num));
								vol_ten.setAdapter(new ArrayWheelAdapter<String>(
										num));
								vol_bits.setAdapter(new ArrayWheelAdapter<String>(
										num));

								vol_thousand.setCyclic(true);
								vol_hundred.setCyclic(true);
								vol_ten.setCyclic(true);
								vol_bits.setCyclic(true);

								String temp = holder.experiment_new_main_item_body_vol_info_et
										.getText().toString();
								int a = 0, b = 0, c = 0, d = 0;
								switch (temp.length()) {
								case 1:
									a = 0;
									b = 0;
									c = 0;
									d = Integer.valueOf(temp.substring(0, 1));
									break;
								case 2:
									a = 0;
									b = 0;
									c = Integer.valueOf(temp.substring(0, 1));
									d = Integer.valueOf(temp.substring(1, 2));
									break;
								case 3:
									a = 0;
									b = Integer.valueOf(temp.substring(0, 1));
									c = Integer.valueOf(temp.substring(1, 2));
									d = Integer.valueOf(temp.substring(2, 3));
									break;
								case 4:
									a = Integer.valueOf(temp.substring(0, 1));
									b = Integer.valueOf(temp.substring(1, 2));
									c = Integer.valueOf(temp.substring(2, 3));
									d = Integer.valueOf(temp.substring(3, 4));
									break;
								default:
									break;
								}
								vol_thousand.setCurrentItem(a);
								vol_hundred.setCurrentItem(b);
								vol_ten.setCurrentItem(c);
								vol_bits.setCurrentItem(d);

								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												int volTotal = vol_thousand
														.getCurrentItem()
														* 1000
														+ vol_hundred
																.getCurrentItem()
														* 100
														+ vol_ten
																.getCurrentItem()
														* 10
														+ vol_bits
																.getCurrentItem();

												if (volTotal > getVolMax()) {
													Toast.makeText(
															CreatExperimentActivity.this,
															getString(R.string.exp_vol_big)
																	+ getVolMax()
																	+ "ul",
															Toast.LENGTH_SHORT)
															.show();
													try {
														Field field = dialog
																.getClass()
																.getSuperclass()
																.getDeclaredField(
																		"mShowing");
														field.setAccessible(true);
														field.set(dialog, false);
													} catch (Exception e) {
														e.printStackTrace();
													}
												} else if (volTotal < 30) {
													Toast.makeText(
															CreatExperimentActivity.this,
															getString(R.string.exp_vol_small)
																	+ "30ul",
															Toast.LENGTH_SHORT)
															.show();
													try {
														Field field = dialog
																.getClass()
																.getSuperclass()
																.getDeclaredField(
																		"mShowing");
														field.setAccessible(true);
														field.set(dialog, false);
													} catch (Exception e) {
														e.printStackTrace();
													}
												} else {
													steps.get(p).setSvol(
															volTotal);
													holder.experiment_new_main_item_body_vol_info_et
															.setText(volTotal
																	+ "");
													try {
														Field field = dialog
																.getClass()
																.getSuperclass()
																.getDeclaredField(
																		"mShowing");
														field.setAccessible(true);
														field.set(dialog, true);
													} catch (Exception e) {
														e.printStackTrace();
													}
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
												try {
													Field field = dialog
															.getClass()
															.getSuperclass()
															.getDeclaredField(
																	"mShowing");
													field.setAccessible(true);
													field.set(dialog, true);
												} catch (Exception e) {
													e.printStackTrace();
												}
											}
										});
								builder.show();
							}
						});

				step.setSname(defaultSteps.get(i).getDSname());
				step.setShole(defaultSteps.get(i).getDShole());
				step.setSspeed(defaultSteps.get(i).getDSspeed());
				step.setSvol(defaultSteps.get(i).getDSvol());
				step.setSwait(defaultSteps.get(i).getDSwait());
				step.setSblend(defaultSteps.get(i).getDSblend());
				step.setSmagnet(defaultSteps.get(i).getDSmagnet());
				step.setSswitch(defaultSteps.get(i).getDSswitch());
				step.setStemp(defaultSteps.get(i).getDStemp());

				holder.experiment_new_main_item_top_name_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								if (textViews.size() != 0) {
									if (stepChooseNum != p) {
										textViews.get(0).setBackgroundColor(
												getResources().getColor(
														R.color.blue));
										textViews.remove(0);
										holder.experiment_new_main_item_top_name_tv
												.setBackgroundColor(getResources()
														.getColor(
																R.color.purple));
										textViews
												.add(holder.experiment_new_main_item_top_name_tv);
									} else {
										textViews.get(0).setBackgroundColor(
												getResources().getColor(
														R.color.blue));
										textViews.remove(0);
										stepChooseNum = -1;
									}
								} else {
									holder.experiment_new_main_item_top_name_tv
											.setBackgroundColor(getResources()
													.getColor(R.color.purple));
									textViews
											.add(holder.experiment_new_main_item_top_name_tv);
								}
								stepChooseNum = p;
							}
						});

				holder.experiment_new_main_item_head_wait_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Date date = null;
								try {
									date = Utlis.timeFormat
											.parse(holder.experiment_new_main_item_head_wait_info_tv
													.getText().toString());
								} catch (ParseException e) {
									e.printStackTrace();
								}
								View view = LayoutInflater.from(
										CreatExperimentActivity.this).inflate(
										R.layout.activity_time_hour, null);
								final TimeWheelView experiment_new_main_item_time_hour_hour = (TimeWheelView) view
										.findViewById(R.id.time_hour_hour);
								final TimeWheelView experiment_new_main_item_time_hour_minutes = (TimeWheelView) view
										.findViewById(R.id.time_hour_minutes);
								final TimeWheelView experiment_new_main_item_time_hour_seconds = (TimeWheelView) view
										.findViewById(R.id.time_hour_seconds);
								String[] hour = getResources().getStringArray(
										R.array.hour_array);
								String[] min = getResources().getStringArray(
										R.array.minute_array);
								String[] sec = getResources().getStringArray(
										R.array.second_array);
								experiment_new_main_item_time_hour_hour
										.setAdapter(new ArrayWheelAdapter<String>(
												hour));
								experiment_new_main_item_time_hour_minutes
										.setAdapter(new ArrayWheelAdapter<String>(
												min));
								experiment_new_main_item_time_hour_seconds
										.setAdapter(new ArrayWheelAdapter<String>(
												sec));
								experiment_new_main_item_time_hour_hour
										.setCyclic(true);
								experiment_new_main_item_time_hour_minutes
										.setCyclic(true);
								experiment_new_main_item_time_hour_seconds
										.setCyclic(true);
								experiment_new_main_item_time_hour_hour
										.setCurrentItem(date.getHours());
								experiment_new_main_item_time_hour_minutes
										.setCurrentItem(date.getMinutes());
								experiment_new_main_item_time_hour_seconds
										.setCurrentItem(date.getSeconds() / 5);

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_wait_time_setting));
								builder.setView(view);
								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												String timeFromatStr = timeFormat(
														experiment_new_main_item_time_hour_hour
																.getCurrentItem(),
														experiment_new_main_item_time_hour_minutes
																.getCurrentItem(),
														experiment_new_main_item_time_hour_seconds
																.getCurrentItem());
												if (timeFromatStr
														.equals("00:00:00")) {
													holder.experiment_new_main_item_head_wait_info_tv
															.setBackgroundResource(R.drawable.zero);
												} else {
													holder.experiment_new_main_item_head_wait_info_tv
															.setBackgroundResource(R.drawable.wait);
												}
												step.setSwait(timeFromatStr);
												holder.experiment_new_main_item_head_wait_info_tv
														.setText(timeFromatStr);
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
				holder.experiment_new_main_item_head_blend_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Date date = null;
								try {
									date = Utlis.timeFormat
											.parse(holder.experiment_new_main_item_head_blend_info_tv
													.getText().toString());
								} catch (ParseException e) {
									e.printStackTrace();
								}
								View view = LayoutInflater.from(
										CreatExperimentActivity.this).inflate(
										R.layout.activity_time_hour, null);
								final TimeWheelView experiment_new_main_item_time_hour_hour = (TimeWheelView) view
										.findViewById(R.id.time_hour_hour);
								final TimeWheelView experiment_new_main_item_time_hour_minutes = (TimeWheelView) view
										.findViewById(R.id.time_hour_minutes);
								final TimeWheelView experiment_new_main_item_time_hour_seconds = (TimeWheelView) view
										.findViewById(R.id.time_hour_seconds);
								String[] hour = getResources().getStringArray(
										R.array.hour_array);
								String[] min = getResources().getStringArray(
										R.array.minute_array);
								String[] sec = getResources().getStringArray(
										R.array.second_array);
								experiment_new_main_item_time_hour_hour
										.setAdapter(new ArrayWheelAdapter<String>(
												hour));
								experiment_new_main_item_time_hour_minutes
										.setAdapter(new ArrayWheelAdapter<String>(
												min));
								experiment_new_main_item_time_hour_seconds
										.setAdapter(new ArrayWheelAdapter<String>(
												sec));
								experiment_new_main_item_time_hour_hour
										.setCyclic(true);
								experiment_new_main_item_time_hour_minutes
										.setCyclic(true);
								experiment_new_main_item_time_hour_seconds
										.setCyclic(true);
								experiment_new_main_item_time_hour_hour
										.setCurrentItem(date.getHours());
								experiment_new_main_item_time_hour_minutes
										.setCurrentItem(date.getMinutes());
								experiment_new_main_item_time_hour_seconds
										.setCurrentItem(date.getSeconds() / 5);

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_blend_time_setting));
								builder.setView(view);
								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												String timeFromatStr = timeFormat(
														experiment_new_main_item_time_hour_hour
																.getCurrentItem(),
														experiment_new_main_item_time_hour_minutes
																.getCurrentItem(),
														experiment_new_main_item_time_hour_seconds
																.getCurrentItem());

												if (timeFromatStr
														.equals("00:00:00")) {
													holder.experiment_new_main_item_head_blend_info_tv
															.setBackgroundResource(R.drawable.zero);
												} else {
													holder.experiment_new_main_item_head_blend_info_tv
															.setBackgroundResource(R.drawable.blend);
												}
												step.setSblend(timeFromatStr);
												holder.experiment_new_main_item_head_blend_info_tv
														.setText(timeFromatStr);
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
				holder.experiment_new_main_item_head_magnet_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Date date = null;
								try {
									date = Utlis.timeFormat
											.parse(holder.experiment_new_main_item_head_magnet_info_tv
													.getText().toString());
								} catch (ParseException e) {
									e.printStackTrace();
								}
								View view = LayoutInflater.from(
										CreatExperimentActivity.this).inflate(
										R.layout.activity_time_hour, null);
								final TimeWheelView experiment_new_main_item_time_hour_hour = (TimeWheelView) view
										.findViewById(R.id.time_hour_hour);
								final TimeWheelView experiment_new_main_item_time_hour_minutes = (TimeWheelView) view
										.findViewById(R.id.time_hour_minutes);
								final TimeWheelView experiment_new_main_item_time_hour_seconds = (TimeWheelView) view
										.findViewById(R.id.time_hour_seconds);
								String[] hour = getResources().getStringArray(
										R.array.hour_array_magnetic);
								String[] min = getResources().getStringArray(
										R.array.minute_array);
								String[] sec = getResources().getStringArray(
										R.array.second_array);
								experiment_new_main_item_time_hour_hour
										.setAdapter(new ArrayWheelAdapter<String>(
												hour));
								experiment_new_main_item_time_hour_minutes
										.setAdapter(new ArrayWheelAdapter<String>(
												min));
								experiment_new_main_item_time_hour_seconds
										.setAdapter(new ArrayWheelAdapter<String>(
												sec));
								experiment_new_main_item_time_hour_hour
										.setCyclic(true);
								experiment_new_main_item_time_hour_minutes
										.setCyclic(true);
								experiment_new_main_item_time_hour_seconds
										.setCyclic(true);
								experiment_new_main_item_time_hour_hour
										.setCurrentItem(date.getHours());
								experiment_new_main_item_time_hour_minutes
										.setCurrentItem(date.getMinutes());
								experiment_new_main_item_time_hour_seconds
										.setCurrentItem(date.getSeconds() / 5);

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_magnetic_time_setting));
								builder.setView(view);
								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												String timeFromatStr = timeFormat(
														experiment_new_main_item_time_hour_hour
																.getCurrentItem(),
														experiment_new_main_item_time_hour_minutes
																.getCurrentItem(),
														experiment_new_main_item_time_hour_seconds
																.getCurrentItem());
												if (timeFromatStr
														.equals("00:00:00")) {
													holder.experiment_new_main_item_head_magnet_info_tv
															.setBackgroundResource(R.drawable.zero);
												} else {
													holder.experiment_new_main_item_head_magnet_info_tv
															.setBackgroundResource(R.drawable.magnet);
												}
												step.setSmagnet(timeFromatStr);
												holder.experiment_new_main_item_head_magnet_info_tv
														.setText(timeFromatStr);
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

				holder.experiment_new_main_item_body_hole_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								View view = LayoutInflater
										.from(CreatExperimentActivity.this)
										.inflate(
												R.layout.activity_experiment_new_main_item_hole,
												null);
								RadioGroup experiment_new_main_item_hole_rg = (RadioGroup) view
										.findViewById(R.id.experiment_new_main_item_hole_rg);
								RadioButton experiment_new_main_item_hole_rb_one = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_one);
								RadioButton experiment_new_main_item_hole_rb_two = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_two);
								RadioButton experiment_new_main_item_hole_rb_there = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_there);
								RadioButton experiment_new_main_item_hole_rb_four = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_four);
								RadioButton experiment_new_main_item_hole_rb_five = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_five);
								RadioButton experiment_new_main_item_hole_rb_six = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_six);
								switch (Integer
										.valueOf(holder.experiment_new_main_item_body_hole_info_tv
												.getText().toString())) {
								case 1:
									experiment_new_main_item_hole_rb_one
											.setChecked(true);
									break;
								case 2:
									experiment_new_main_item_hole_rb_two
											.setChecked(true);
									break;
								case 3:
									experiment_new_main_item_hole_rb_there
											.setChecked(true);
									break;
								case 4:
									experiment_new_main_item_hole_rb_four
											.setChecked(true);
									break;
								case 5:
									experiment_new_main_item_hole_rb_five
											.setChecked(true);
									break;
								case 6:
									experiment_new_main_item_hole_rb_six
											.setChecked(true);
									break;
								default:
									break;
								}

								// 通量设置孔位属性 1-->15(1~5) 2-->48(1~4)
								switch (fluxNum) {
								case 1:
									experiment_new_main_item_hole_rb_six
											.setVisibility(View.GONE);
									break;
								case 3:
									experiment_new_main_item_hole_rb_six
											.setVisibility(View.GONE);
									experiment_new_main_item_hole_rb_five
											.setVisibility(View.GONE);
									break;
								}

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_hole_select));
								builder.setView(view);
								dialog = builder.show();
								experiment_new_main_item_hole_rg
										.setOnCheckedChangeListener(new OnCheckedChangeListener() {
											public void onCheckedChanged(
													RadioGroup group,
													int checkedId) {
												switch (checkedId) {
												case R.id.experiment_new_main_item_hole_rb_one:
													check = 1;
													break;
												case R.id.experiment_new_main_item_hole_rb_two:
													check = 2;
													break;
												case R.id.experiment_new_main_item_hole_rb_there:
													check = 3;
													break;
												case R.id.experiment_new_main_item_hole_rb_four:
													check = 4;
													break;
												case R.id.experiment_new_main_item_hole_rb_five:
													check = 5;
													break;
												case R.id.experiment_new_main_item_hole_rb_six:
													check = 6;
													break;
												}
												if (check == 1) {
													if (switchInfo == 2) {
														switchInfo = 1;
													}
												} else {
													if (switchInfo == 1) {
														switchInfo = 2;
													}
												}
												step.setShole(check);
												step.setSswitch(switchInfo);
												holder.experiment_new_main_item_body_switch_info_tv
														.setText(getSwitch(switchInfo));
												holder.experiment_new_main_item_body_hole_info_tv
														.setText(check + "");
												dialog.cancel();
											}
										});
							}
						});

				holder.experiment_new_main_item_body_speed_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								View view = LayoutInflater
										.from(CreatExperimentActivity.this)
										.inflate(
												R.layout.activity_experiment_new_main_item_vol,
												null);
								RadioGroup experiment_new_main_item_vol_rg = (RadioGroup) view
										.findViewById(R.id.experiment_new_main_item_vol_rg);
								RadioButton experiment_new_main_item_vol_rb_one = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_one);
								RadioButton experiment_new_main_item_vol_rb_two = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_two);
								RadioButton experiment_new_main_item_vol_rb_there = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_there);
								RadioButton experiment_new_main_item_vol_rb_four = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_four);
								RadioButton experiment_new_main_item_vol_rb_five = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_five);
								RadioButton experiment_new_main_item_vol_rb_six = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_six);
								RadioButton experiment_new_main_item_vol_rb_seven = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_seven);
								RadioButton experiment_new_main_item_vol_rb_eight = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_eight);
								switch (Integer
										.valueOf(holder.experiment_new_main_item_body_speed_info_tv
												.getText().toString()
												.substring(0, 1))) {
								case 1:
									experiment_new_main_item_vol_rb_one
											.setChecked(true);
									break;
								case 2:
									experiment_new_main_item_vol_rb_two
											.setChecked(true);
									break;
								case 3:
									experiment_new_main_item_vol_rb_there
											.setChecked(true);
									break;
								case 4:
									experiment_new_main_item_vol_rb_four
											.setChecked(true);
									break;
								case 5:
									experiment_new_main_item_vol_rb_five
											.setChecked(true);
									break;
								case 6:
									experiment_new_main_item_vol_rb_six
											.setChecked(true);
									break;
								case 7:
									experiment_new_main_item_vol_rb_seven
											.setChecked(true);
									break;
								case 8:
									experiment_new_main_item_vol_rb_eight
											.setChecked(true);
									break;
								default:
									break;
								}

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_speed_select));
								builder.setView(view);
								dialog = builder.show();
								experiment_new_main_item_vol_rg
										.setOnCheckedChangeListener(new OnCheckedChangeListener() {
											public void onCheckedChanged(
													RadioGroup group,
													int checkedId) {
												switch (checkedId) {
												case R.id.experiment_new_main_item_vol_rb_one:
													speed = 1;
													break;
												case R.id.experiment_new_main_item_vol_rb_two:
													speed = 2;
													break;
												case R.id.experiment_new_main_item_vol_rb_there:
													speed = 3;
													break;
												case R.id.experiment_new_main_item_vol_rb_four:
													speed = 4;
													break;
												case R.id.experiment_new_main_item_vol_rb_five:
													speed = 5;
													break;
												case R.id.experiment_new_main_item_vol_rb_six:
													speed = 6;
													break;
												case R.id.experiment_new_main_item_vol_rb_seven:
													speed = 7;
													break;
												case R.id.experiment_new_main_item_vol_rb_eight:
													speed = 8;
													break;
												}
												// TODO
												step.setSspeed(speed);
												holder.experiment_new_main_item_body_speed_info_tv
														.setText(speed
																+ getString(R.string.exp_speed_unit));
												dialog.cancel();
											}
										});
							}
						});

				holder.experiment_new_main_item_body_switch_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								switchInfo = step.getSswitch();
								if (switchInfo != 0) {
									switchInfo = 0;

								} else {
									if (holder.experiment_new_main_item_body_hole_info_tv
											.getText().toString().equals("1")) {
										switchInfo = 1;
									} else {
										switchInfo = 2;
									}
								}
								step.setSswitch(switchInfo);
								holder.experiment_new_main_item_body_switch_info_tv
										.setText(getSwitch(switchInfo));
							}
						});

				holder.experiment_new_main_item_bottom_name_et
						.addTextChangedListener(new TextWatcher() {
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
								step.setSname(holder.experiment_new_main_item_bottom_name_et
										.getText().toString());
							}

							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
							}

							public void afterTextChanged(Editable s) {
							}
						});
				steps.add(step);
				holders.add(holder);
				stepRow.addView(convertView);
			}
		} else {
			for (int i = 0; i < steps.size(); i++) {
				final int p = i;
				LayoutInflater inflater = LayoutInflater
						.from(CreatExperimentActivity.this);
				final CViewHolder holder = new CViewHolder();
				View convertView = inflater.inflate(
						R.layout.activity_experiment_new_main_item, null);
				holder.experiment_new_main_item_top_name_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_top_name_tv);
				holder.experiment_new_main_item_head_wait_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_head_wait_info_tv);
				holder.experiment_new_main_item_head_blend_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_head_blend_info_tv);
				holder.experiment_new_main_item_head_magnet_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_head_magnet_info_tv);
				holder.experiment_new_main_item_body_hole_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_body_hole_info_tv);
				holder.experiment_new_main_item_body_vol_info_et = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_body_vol_info_et);
				holder.experiment_new_main_item_body_speed_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_body_speed_info_tv);
				holder.experiment_new_main_item_body_temp_info_et = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_body_temp_info_et);
				holder.experiment_new_main_item_body_switch_info_tv = (TextView) convertView
						.findViewById(R.id.experiment_new_main_item_body_switch_info_tv);
				holder.experiment_new_main_item_bottom_name_et = (EditText) convertView
						.findViewById(R.id.experiment_new_main_item_bottom_name_et);

				holder.experiment_new_main_item_body_temp_rl = (RelativeLayout) convertView
						.findViewById(R.id.experiment_new_main_item_body_temp_rl);
				holder.experiment_new_main_item_body_switch_rl = (RelativeLayout) convertView
						.findViewById(R.id.experiment_new_main_item_body_switch_rl);

				// 通量属性展示 1-->15 3-->48
				if (fluxNum == 1 || fluxNum == 3) {
					holder.experiment_new_main_item_body_temp_rl
							.setVisibility(View.GONE);
					holder.experiment_new_main_item_body_switch_rl
							.setVisibility(View.GONE);
				}

				if (steps.get(i).getSwait().equals("00:00:00")) {
					holder.experiment_new_main_item_head_wait_info_tv
							.setBackgroundResource(R.drawable.zero);
				}
				if (steps.get(i).getSblend().equals("00:00:00")) {
					holder.experiment_new_main_item_head_blend_info_tv
							.setBackgroundResource(R.drawable.zero);
				}
				if (steps.get(i).getSmagnet().equals("00:00:00")) {
					holder.experiment_new_main_item_head_magnet_info_tv
							.setBackgroundResource(R.drawable.zero);
				}
				holder.experiment_new_main_item_top_name_tv.setText("No.  "
						+ (i + 1));
				holder.experiment_new_main_item_head_wait_info_tv.setText(steps
						.get(i).getSwait());
				holder.experiment_new_main_item_head_blend_info_tv
						.setText(steps.get(i).getSblend());
				holder.experiment_new_main_item_head_magnet_info_tv
						.setText(steps.get(i).getSmagnet());
				holder.experiment_new_main_item_body_hole_info_tv.setText(steps
						.get(i).getShole() + "");
				holder.experiment_new_main_item_body_vol_info_et.setText(steps
						.get(i).getSvol() + "");
				// speed = step.getSspeed();
				holder.experiment_new_main_item_body_speed_info_tv
						.setText(steps.get(i).getSspeed()
								+ getString(R.string.exp_speed_unit));
				holder.experiment_new_main_item_body_temp_info_et.setText(steps
						.get(i).getStemp() + "");
				// switchInfo = step.getSswitch();
				holder.experiment_new_main_item_body_switch_info_tv
						.setText(getSwitch(steps.get(i).getSswitch()));
				holder.experiment_new_main_item_bottom_name_et.setText(steps
						.get(i).getSname());

				holder.experiment_new_main_item_body_temp_info_et
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								if (!holder.experiment_new_main_item_body_switch_info_tv
										.getText().toString()
										.equals(getString(R.string.exp_closs))) {
									AlertDialog.Builder builder = new AlertDialog.Builder(
											CreatExperimentActivity.this);
									builder.setTitle(getString(R.string.exp_temp_setting));
									View view = LayoutInflater.from(
											CreatExperimentActivity.this)
											.inflate(R.layout.activity_temp,
													null);
									builder.setView(view);
									builder.setCancelable(false);
									final TimeWheelView temp_thousand = (TimeWheelView) view
											.findViewById(R.id.temp_thousand);
									final TimeWheelView temp_hundred = (TimeWheelView) view
											.findViewById(R.id.temp_hundred);
									final TimeWheelView temp_ten = (TimeWheelView) view
											.findViewById(R.id.temp_ten);
									String[] thousand = getResources()
											.getStringArray(
													R.array.thousand_array);
									String[] num = getResources()
											.getStringArray(R.array.num_array);

									temp_thousand
											.setAdapter(new ArrayWheelAdapter<String>(
													thousand));
									temp_hundred
											.setAdapter(new ArrayWheelAdapter<String>(
													num));
									temp_ten.setAdapter(new ArrayWheelAdapter<String>(
											num));

									temp_thousand.setCyclic(true);
									temp_hundred.setCyclic(true);
									temp_ten.setCyclic(true);

									String temp = holder.experiment_new_main_item_body_temp_info_et
											.getText().toString();
									int a = 0, b = 0, c = 0;
									switch (temp.length()) {
									case 1:
										a = 0;
										b = 0;
										c = Integer.valueOf(temp
												.substring(0, 1));
										break;
									case 2:
										a = 0;
										b = Integer.valueOf(temp
												.substring(0, 1));
										c = Integer.valueOf(temp
												.substring(1, 2));
										break;
									case 3:
										a = Integer.valueOf(temp
												.substring(0, 1));
										b = Integer.valueOf(temp
												.substring(1, 2));
										c = Integer.valueOf(temp
												.substring(2, 3));
										break;
									default:
										break;
									}
									temp_thousand.setCurrentItem(a);
									temp_hundred.setCurrentItem(b);
									temp_ten.setCurrentItem(c);

									builder.setPositiveButton(
											getString(R.string.sure),
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													int tempTotal = temp_thousand
															.getCurrentItem()
															* 100
															+ temp_hundred
																	.getCurrentItem()
															* 10
															+ temp_ten
																	.getCurrentItem();
													if (tempTotal > 120) {
														Toast.makeText(
																CreatExperimentActivity.this,
																getString(R.string.exp_temp_big),
																Toast.LENGTH_SHORT)
																.show();
														try {
															Field field = dialog
																	.getClass()
																	.getSuperclass()
																	.getDeclaredField(
																			"mShowing");
															field.setAccessible(true);
															field.set(dialog,
																	false);
														} catch (Exception e) {
															e.printStackTrace();
														}
													
													}
													else if (tempTotal < 30) {
														Toast.makeText(
																CreatExperimentActivity.this,
																getString(R.string.exp_temp_small),
																Toast.LENGTH_SHORT)
																.show();
														try {
															Field field = dialog
																	.getClass()
																	.getSuperclass()
																	.getDeclaredField(
																			"mShowing");
															field.setAccessible(true);
															field.set(dialog,
																	false);
														} catch (Exception e) {
															e.printStackTrace();
														}	
													}
													else {
														steps.get(p).setStemp(
																tempTotal);
														holder.experiment_new_main_item_body_temp_info_et
																.setText(tempTotal
																		+ "");
														try {
															Field field = dialog
																	.getClass()
																	.getSuperclass()
																	.getDeclaredField(
																			"mShowing");
															field.setAccessible(true);
															field.set(dialog,
																	true);
														} catch (Exception e) {
															e.printStackTrace();
														}
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
													try {
														Field field = dialog
																.getClass()
																.getSuperclass()
																.getDeclaredField(
																		"mShowing");
														field.setAccessible(true);
														field.set(dialog, true);
													} catch (Exception e) {
														e.printStackTrace();
													}
												}
											});
									builder.show();
								}
							}
						});

				holder.experiment_new_main_item_body_vol_info_et
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_vol_setting));
								View view = LayoutInflater.from(
										CreatExperimentActivity.this).inflate(
										R.layout.activity_vol, null);
								builder.setView(view);
								builder.setCancelable(false);
								final TimeWheelView vol_thousand = (TimeWheelView) view
										.findViewById(R.id.vol_thousand);
								final TimeWheelView vol_hundred = (TimeWheelView) view
										.findViewById(R.id.vol_hundred);
								final TimeWheelView vol_ten = (TimeWheelView) view
										.findViewById(R.id.vol_ten);
								final TimeWheelView vol_bits = (TimeWheelView) view
										.findViewById(R.id.vol_bits);
								String[] thousand = getResources()
										.getStringArray(R.array.thousand_array);
								String[] num = getResources().getStringArray(
										R.array.num_array);

								vol_thousand
										.setAdapter(new ArrayWheelAdapter<String>(
												thousand));
								vol_hundred
										.setAdapter(new ArrayWheelAdapter<String>(
												num));
								vol_ten.setAdapter(new ArrayWheelAdapter<String>(
										num));
								vol_bits.setAdapter(new ArrayWheelAdapter<String>(
										num));

								vol_thousand.setCyclic(true);
								vol_hundred.setCyclic(true);
								vol_ten.setCyclic(true);
								vol_bits.setCyclic(true);

								String temp = holder.experiment_new_main_item_body_vol_info_et
										.getText().toString();
								int a = 0, b = 0, c = 0, d = 0;
								switch (temp.length()) {
								case 1:
									a = 0;
									b = 0;
									c = 0;
									d = Integer.valueOf(temp.substring(0, 1));
									break;
								case 2:
									a = 0;
									b = 0;
									c = Integer.valueOf(temp.substring(0, 1));
									d = Integer.valueOf(temp.substring(1, 2));
									break;
								case 3:
									a = 0;
									b = Integer.valueOf(temp.substring(0, 1));
									c = Integer.valueOf(temp.substring(1, 2));
									d = Integer.valueOf(temp.substring(2, 3));
									break;
								case 4:
									a = Integer.valueOf(temp.substring(0, 1));
									b = Integer.valueOf(temp.substring(1, 2));
									c = Integer.valueOf(temp.substring(2, 3));
									d = Integer.valueOf(temp.substring(3, 4));
									break;
								default:
									break;
								}
								vol_thousand.setCurrentItem(a);
								vol_hundred.setCurrentItem(b);
								vol_ten.setCurrentItem(c);
								vol_bits.setCurrentItem(d);

								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												int volTotal = vol_thousand
														.getCurrentItem()
														* 1000
														+ vol_hundred
																.getCurrentItem()
														* 100
														+ vol_ten
																.getCurrentItem()
														* 10
														+ vol_bits
																.getCurrentItem();
												if (volTotal > getVolMax()) {
													Toast.makeText(
															CreatExperimentActivity.this,
															getString(R.string.exp_vol_big)
																	+ getVolMax()
																	+ "ul",
															Toast.LENGTH_SHORT)
															.show();
													try {
														Field field = dialog
																.getClass()
																.getSuperclass()
																.getDeclaredField(
																		"mShowing");
														field.setAccessible(true);
														field.set(dialog, false);
													} catch (Exception e) {
														e.printStackTrace();
													}
												} else if (volTotal < 30) {
													Toast.makeText(
															CreatExperimentActivity.this,
															getString(R.string.exp_vol_small)
																	+ "30ul",
															Toast.LENGTH_SHORT)
															.show();
													try {
														Field field = dialog
																.getClass()
																.getSuperclass()
																.getDeclaredField(
																		"mShowing");
														field.setAccessible(true);
														field.set(dialog, false);
													} catch (Exception e) {
														e.printStackTrace();
													}
												} else {
													steps.get(p).setSvol(
															volTotal);
													holder.experiment_new_main_item_body_vol_info_et
															.setText(volTotal
																	+ "");
													try {
														Field field = dialog
																.getClass()
																.getSuperclass()
																.getDeclaredField(
																		"mShowing");
														field.setAccessible(true);
														field.set(dialog, true);
													} catch (Exception e) {
														e.printStackTrace();
													}
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
												try {
													Field field = dialog
															.getClass()
															.getSuperclass()
															.getDeclaredField(
																	"mShowing");
													field.setAccessible(true);
													field.set(dialog, true);
												} catch (Exception e) {
													e.printStackTrace();
												}
											}
										});
								builder.show();
							}
						});

				steps.get(i).setSname(steps.get(i).getSname());
				steps.get(i).setShole(steps.get(i).getShole());
				steps.get(i).setSspeed(steps.get(i).getSspeed());
				steps.get(i).setSvol(steps.get(i).getSvol());
				steps.get(i).setSwait(steps.get(i).getSwait());
				steps.get(i).setSblend(steps.get(i).getSblend());
				steps.get(i).setSmagnet(steps.get(i).getSmagnet());
				steps.get(i).setSswitch(steps.get(i).getSswitch());
				steps.get(i).setStemp(steps.get(i).getStemp());

				holder.experiment_new_main_item_top_name_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								if (textViews.size() != 0) {
									if (stepChooseNum != p) {
										textViews.get(0).setBackgroundColor(
												getResources().getColor(
														R.color.blue));
										textViews.remove(0);
										holder.experiment_new_main_item_top_name_tv
												.setBackgroundColor(getResources()
														.getColor(
																R.color.purple));
										textViews
												.add(holder.experiment_new_main_item_top_name_tv);
										stepChooseNum = p;
									} else {
										textViews.get(0).setBackgroundColor(
												getResources().getColor(
														R.color.blue));
										textViews.remove(0);
										stepChooseNum = -1;
									}
								} else {
									holder.experiment_new_main_item_top_name_tv
											.setBackgroundColor(getResources()
													.getColor(R.color.purple));
									textViews
											.add(holder.experiment_new_main_item_top_name_tv);
									stepChooseNum = p;
								}
							}
						});

				holder.experiment_new_main_item_head_wait_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Date date = null;
								try {
									date = Utlis.timeFormat
											.parse(holder.experiment_new_main_item_head_wait_info_tv
													.getText().toString());
								} catch (ParseException e) {
									e.printStackTrace();
								}
								View view = LayoutInflater.from(
										CreatExperimentActivity.this).inflate(
										R.layout.activity_time_hour, null);
								final TimeWheelView experiment_new_main_item_time_hour_hour = (TimeWheelView) view
										.findViewById(R.id.time_hour_hour);
								final TimeWheelView experiment_new_main_item_time_hour_minutes = (TimeWheelView) view
										.findViewById(R.id.time_hour_minutes);
								final TimeWheelView experiment_new_main_item_time_hour_seconds = (TimeWheelView) view
										.findViewById(R.id.time_hour_seconds);
								String[] hour = getResources().getStringArray(
										R.array.hour_array);
								String[] min = getResources().getStringArray(
										R.array.minute_array);
								String[] sec = getResources().getStringArray(
										R.array.second_array);
								experiment_new_main_item_time_hour_hour
										.setAdapter(new ArrayWheelAdapter<String>(
												hour));
								experiment_new_main_item_time_hour_minutes
										.setAdapter(new ArrayWheelAdapter<String>(
												min));
								experiment_new_main_item_time_hour_seconds
										.setAdapter(new ArrayWheelAdapter<String>(
												sec));
								experiment_new_main_item_time_hour_hour
										.setCyclic(true);
								experiment_new_main_item_time_hour_minutes
										.setCyclic(true);
								experiment_new_main_item_time_hour_seconds
										.setCyclic(true);
								experiment_new_main_item_time_hour_hour
										.setCurrentItem(date.getHours());
								experiment_new_main_item_time_hour_minutes
										.setCurrentItem(date.getMinutes());
								experiment_new_main_item_time_hour_seconds
										.setCurrentItem(date.getSeconds() / 5);

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_wait_time_setting));
								builder.setView(view);
								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												String timeFromatStr = timeFormat(
														experiment_new_main_item_time_hour_hour
																.getCurrentItem(),
														experiment_new_main_item_time_hour_minutes
																.getCurrentItem(),
														experiment_new_main_item_time_hour_seconds
																.getCurrentItem());
												if (timeFromatStr
														.equals("00:00:00")) {
													holder.experiment_new_main_item_head_wait_info_tv
															.setBackgroundResource(R.drawable.zero);
												} else {
													holder.experiment_new_main_item_head_wait_info_tv
															.setBackgroundResource(R.drawable.wait);
												}
												steps.get(p).setSwait(
														timeFromatStr);
												holder.experiment_new_main_item_head_wait_info_tv
														.setText(timeFromatStr);
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
				holder.experiment_new_main_item_head_blend_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Date date = null;
								try {
									date = Utlis.timeFormat
											.parse(holder.experiment_new_main_item_head_blend_info_tv
													.getText().toString());
								} catch (ParseException e) {
									e.printStackTrace();
								}
								View view = LayoutInflater.from(
										CreatExperimentActivity.this).inflate(
										R.layout.activity_time_hour, null);
								final TimeWheelView experiment_new_main_item_time_hour_hour = (TimeWheelView) view
										.findViewById(R.id.time_hour_hour);
								final TimeWheelView experiment_new_main_item_time_hour_minutes = (TimeWheelView) view
										.findViewById(R.id.time_hour_minutes);
								final TimeWheelView experiment_new_main_item_time_hour_seconds = (TimeWheelView) view
										.findViewById(R.id.time_hour_seconds);
								String[] hour = getResources().getStringArray(
										R.array.hour_array);
								String[] min = getResources().getStringArray(
										R.array.minute_array);
								String[] sec = getResources().getStringArray(
										R.array.second_array);
								experiment_new_main_item_time_hour_hour
										.setAdapter(new ArrayWheelAdapter<String>(
												hour));
								experiment_new_main_item_time_hour_minutes
										.setAdapter(new ArrayWheelAdapter<String>(
												min));
								experiment_new_main_item_time_hour_seconds
										.setAdapter(new ArrayWheelAdapter<String>(
												sec));
								experiment_new_main_item_time_hour_hour
										.setCyclic(true);
								experiment_new_main_item_time_hour_minutes
										.setCyclic(true);
								experiment_new_main_item_time_hour_seconds
										.setCyclic(true);
								experiment_new_main_item_time_hour_hour
										.setCurrentItem(date.getHours());
								experiment_new_main_item_time_hour_minutes
										.setCurrentItem(date.getMinutes());
								experiment_new_main_item_time_hour_seconds
										.setCurrentItem(date.getSeconds() / 5);

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_blend_time_setting));
								builder.setView(view);
								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												String timeFromatStr = timeFormat(
														experiment_new_main_item_time_hour_hour
																.getCurrentItem(),
														experiment_new_main_item_time_hour_minutes
																.getCurrentItem(),
														experiment_new_main_item_time_hour_seconds
																.getCurrentItem());
												if (timeFromatStr
														.equals("00:00:00")) {
													holder.experiment_new_main_item_head_blend_info_tv
															.setBackgroundResource(R.drawable.zero);
												} else {
													holder.experiment_new_main_item_head_blend_info_tv
															.setBackgroundResource(R.drawable.blend);
												}
												steps.get(p).setSblend(
														timeFromatStr);
												holder.experiment_new_main_item_head_blend_info_tv
														.setText(timeFromatStr);
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

				// --磁吸时间 fixed
				holder.experiment_new_main_item_head_magnet_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Date date = null;
								try {
									date = Utlis.timeFormat
											.parse(holder.experiment_new_main_item_head_magnet_info_tv
													.getText().toString());
								} catch (ParseException e) {
									e.printStackTrace();
								}
								View view = LayoutInflater.from(
										CreatExperimentActivity.this).inflate(
										R.layout.activity_time_hour, null);
								final TimeWheelView experiment_new_main_item_time_hour_hour = (TimeWheelView) view
										.findViewById(R.id.time_hour_hour);
								final TimeWheelView experiment_new_main_item_time_hour_minutes = (TimeWheelView) view
										.findViewById(R.id.time_hour_minutes);
								final TimeWheelView experiment_new_main_item_time_hour_seconds = (TimeWheelView) view
										.findViewById(R.id.time_hour_seconds);
								String[] hour = getResources().getStringArray(
										R.array.hour_array_magnetic);
								String[] min = getResources().getStringArray(
										R.array.minute_array);
								String[] sec = getResources().getStringArray(
										R.array.second_array);
								experiment_new_main_item_time_hour_hour
										.setAdapter(new ArrayWheelAdapter<String>(
												hour));
								experiment_new_main_item_time_hour_minutes
										.setAdapter(new ArrayWheelAdapter<String>(
												min));
								experiment_new_main_item_time_hour_seconds
										.setAdapter(new ArrayWheelAdapter<String>(
												sec));
								experiment_new_main_item_time_hour_hour
										.setCyclic(true);
								experiment_new_main_item_time_hour_minutes
										.setCyclic(true);
								experiment_new_main_item_time_hour_seconds
										.setCyclic(true);
								experiment_new_main_item_time_hour_hour
										.setCurrentItem(date.getHours());
								experiment_new_main_item_time_hour_minutes
										.setCurrentItem(date.getMinutes());
								experiment_new_main_item_time_hour_seconds
										.setCurrentItem(date.getSeconds() / 5);

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_magnetic_time_setting));
								builder.setView(view);
								builder.setPositiveButton(
										getString(R.string.sure),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												String timeFromatStr = timeFormat(
														experiment_new_main_item_time_hour_hour
																.getCurrentItem(),
														experiment_new_main_item_time_hour_minutes
																.getCurrentItem(),
														experiment_new_main_item_time_hour_seconds
																.getCurrentItem());
												if (timeFromatStr
														.equals("00:00:00")) {
													holder.experiment_new_main_item_head_magnet_info_tv
															.setBackgroundResource(R.drawable.zero);
												} else {
													holder.experiment_new_main_item_head_magnet_info_tv
															.setBackgroundResource(R.drawable.magnet);
												}
												steps.get(p).setSmagnet(
														timeFromatStr);
												holder.experiment_new_main_item_head_magnet_info_tv
														.setText(timeFromatStr);
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

				holder.experiment_new_main_item_body_hole_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								View view = LayoutInflater
										.from(CreatExperimentActivity.this)
										.inflate(
												R.layout.activity_experiment_new_main_item_hole,
												null);
								RadioGroup experiment_new_main_item_hole_rg = (RadioGroup) view
										.findViewById(R.id.experiment_new_main_item_hole_rg);
								RadioButton experiment_new_main_item_hole_rb_one = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_one);
								RadioButton experiment_new_main_item_hole_rb_two = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_two);
								RadioButton experiment_new_main_item_hole_rb_there = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_there);
								RadioButton experiment_new_main_item_hole_rb_four = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_four);
								RadioButton experiment_new_main_item_hole_rb_five = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_five);
								RadioButton experiment_new_main_item_hole_rb_six = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_hole_rb_six);

								// 通量设置孔位属性 1-->15(1~5) 2-->48(1~4)
								switch (fluxNum) {
								case 1:
									experiment_new_main_item_hole_rb_six
											.setVisibility(View.GONE);
									break;
								case 3:
									experiment_new_main_item_hole_rb_six
											.setVisibility(View.GONE);
									experiment_new_main_item_hole_rb_five
											.setVisibility(View.GONE);
									break;
								}

								switch (Integer
										.valueOf(holder.experiment_new_main_item_body_hole_info_tv
												.getText().toString())) {
								case 1:
									experiment_new_main_item_hole_rb_one
											.setChecked(true);
									break;
								case 2:
									experiment_new_main_item_hole_rb_two
											.setChecked(true);
									break;
								case 3:
									experiment_new_main_item_hole_rb_there
											.setChecked(true);
									break;
								case 4:
									experiment_new_main_item_hole_rb_four
											.setChecked(true);
									break;
								case 5:
									experiment_new_main_item_hole_rb_five
											.setChecked(true);
									break;
								case 6:
									experiment_new_main_item_hole_rb_six
											.setChecked(true);
									break;
								default:
									break;
								}
								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_hole_select));
								builder.setView(view);
								dialog = builder.show();
								experiment_new_main_item_hole_rg
										.setOnCheckedChangeListener(new OnCheckedChangeListener() {
											public void onCheckedChanged(
													RadioGroup group,
													int checkedId) {
												switch (checkedId) {
												case R.id.experiment_new_main_item_hole_rb_one:
													check = 1;
													break;
												case R.id.experiment_new_main_item_hole_rb_two:
													check = 2;
													break;
												case R.id.experiment_new_main_item_hole_rb_there:
													check = 3;
													break;
												case R.id.experiment_new_main_item_hole_rb_four:
													check = 4;
													break;
												case R.id.experiment_new_main_item_hole_rb_five:
													check = 5;
													break;
												case R.id.experiment_new_main_item_hole_rb_six:
													check = 6;
													break;
												}
												if (check == 1) {
													if (switchInfo == 2) {
														switchInfo = 1;
													}
												} else {
													if (switchInfo == 1) {
														switchInfo = 2;
													}
												}
												steps.get(p).setShole(check);
												steps.get(p).setSswitch(
														switchInfo);
												holder.experiment_new_main_item_body_switch_info_tv
														.setText(getSwitch(switchInfo));
												holder.experiment_new_main_item_body_hole_info_tv
														.setText(check + "");
												dialog.cancel();
											}
										});
							}
						});

				holder.experiment_new_main_item_body_vol_info_et
						.addTextChangedListener(new TextWatcher() {
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
								if (s.length() > 0) {
									steps.get(p).setSvol(
											Integer.valueOf(s.toString()));
								} else {
									steps.get(p).setSvol(0);
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

				holder.experiment_new_main_item_body_speed_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								View view = LayoutInflater
										.from(CreatExperimentActivity.this)
										.inflate(
												R.layout.activity_experiment_new_main_item_vol,
												null);
								RadioGroup experiment_new_main_item_vol_rg = (RadioGroup) view
										.findViewById(R.id.experiment_new_main_item_vol_rg);
								RadioButton experiment_new_main_item_vol_rb_one = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_one);
								RadioButton experiment_new_main_item_vol_rb_two = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_two);
								RadioButton experiment_new_main_item_vol_rb_there = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_there);
								RadioButton experiment_new_main_item_vol_rb_four = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_four);
								RadioButton experiment_new_main_item_vol_rb_five = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_five);
								RadioButton experiment_new_main_item_vol_rb_six = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_six);
								RadioButton experiment_new_main_item_vol_rb_seven = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_seven);
								RadioButton experiment_new_main_item_vol_rb_eight = (RadioButton) view
										.findViewById(R.id.experiment_new_main_item_vol_rb_eight);
								switch (Integer
										.valueOf(holder.experiment_new_main_item_body_speed_info_tv
												.getText().toString()
												.substring(0, 1))) {
								case 1:
									experiment_new_main_item_vol_rb_one
											.setChecked(true);
									break;
								case 2:
									experiment_new_main_item_vol_rb_two
											.setChecked(true);
									break;
								case 3:
									experiment_new_main_item_vol_rb_there
											.setChecked(true);
									break;
								case 4:
									experiment_new_main_item_vol_rb_four
											.setChecked(true);
									break;
								case 5:
									experiment_new_main_item_vol_rb_five
											.setChecked(true);
									break;
								case 6:
									experiment_new_main_item_vol_rb_six
											.setChecked(true);
									break;
								case 7:
									experiment_new_main_item_vol_rb_seven
											.setChecked(true);
									break;
								case 8:
									experiment_new_main_item_vol_rb_eight
											.setChecked(true);
									break;
								default:
									break;
								}

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CreatExperimentActivity.this);
								builder.setTitle(getString(R.string.exp_speed_select));
								builder.setView(view);
								dialog = builder.show();
								experiment_new_main_item_vol_rg
										.setOnCheckedChangeListener(new OnCheckedChangeListener() {
											public void onCheckedChanged(
													RadioGroup group,
													int checkedId) {
												switch (checkedId) {
												case R.id.experiment_new_main_item_vol_rb_one:
													speed = 1;
													break;
												case R.id.experiment_new_main_item_vol_rb_two:
													speed = 2;
													break;
												case R.id.experiment_new_main_item_vol_rb_there:
													speed = 3;
													break;
												case R.id.experiment_new_main_item_vol_rb_four:
													speed = 4;
													break;
												case R.id.experiment_new_main_item_vol_rb_five:
													speed = 5;
													break;
												case R.id.experiment_new_main_item_vol_rb_six:
													speed = 6;
													break;
												case R.id.experiment_new_main_item_vol_rb_seven:
													speed = 7;
													break;
												case R.id.experiment_new_main_item_vol_rb_eight:
													speed = 8;
													break;
												}
												// TODO  频率设置
												steps.get(p).setSspeed(speed);
												holder.experiment_new_main_item_body_speed_info_tv
														.setText(speed
																+ getString(R.string.exp_speed_unit));
												dialog.cancel();
											}
										});
							}
						});
				if (holder.experiment_new_main_item_body_switch_info_tv
						.getText().toString()
						.equals(getString(R.string.exp_closs))) {
					holder.experiment_new_main_item_body_temp_info_et
							.setEnabled(false);
					holder.experiment_new_main_item_body_temp_info_et
							.setText("0");
				}

				holder.experiment_new_main_item_body_switch_info_tv
						.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								switchInfo = steps.get(p).getSswitch();
								if (switchInfo != 0) {
									switchInfo = 0;
									holder.experiment_new_main_item_body_temp_info_et
											.setEnabled(false);
									holder.experiment_new_main_item_body_temp_info_et
											.setText("0");
								} else {
									holder.experiment_new_main_item_body_temp_info_et
											.setEnabled(true);
									holder.experiment_new_main_item_body_temp_info_et
											.setText("30");
									if (holder.experiment_new_main_item_body_hole_info_tv
											.getText().toString().equals("1")) {
										switchInfo = 1;
									} else {
										switchInfo = 2;
									}
								}
								steps.get(p).setSswitch(switchInfo);
								holder.experiment_new_main_item_body_switch_info_tv
										.setText(getSwitch(switchInfo));
							}
						});
				holder.experiment_new_main_item_bottom_name_et
						.addTextChangedListener(new TextWatcher() {
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
								steps.get(p)
										.setSname(
												holder.experiment_new_main_item_bottom_name_et
														.getText().toString());
							}

							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
							}

							public void afterTextChanged(Editable s) {
							}
						});
				holders.add(holder);
				stepRow.addView(convertView);
			}
		}
		experiment_new_main_body_tl.addView(stepRow,
				new TableLayout.LayoutParams(FP, WC));
		waitDialog.cancel();
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

	/**
	 * Title: getVolMax Description: 1 for 15 flux,2 for 32 flux,3 for 48 flux
	 * Created By： Domon Modified Date: 2014-9-12
	 * 
	 * @return
	 */
	private int getVolMax() {
		switch (fluxNum) {
		case 1:
			volMax = 1500;
			break;
		case 2:
			volMax = 1000;
			break;
		case 3:
			volMax = 1000;
			break;
		}
		return volMax;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					CreatExperimentActivity.this);
			builder.setTitle(getString(R.string.exp_new_not_exit));
			builder.setNegativeButton(getString(R.string.sure),
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
			if (CreatExperimentActivity.this.getCurrentFocus() != null) {
				if (CreatExperimentActivity.this.getCurrentFocus()
						.getWindowToken() != null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							CreatExperimentActivity.this);
					builder.setTitle(getString(R.string.waitting));
					Dialog waitDialog = builder.show();
					imm.hideSoftInputFromWindow(CreatExperimentActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					waitDialog.cancel();
				}
			}
		}
		return super.onTouchEvent(event);
	}

	private boolean getTempIsNull() {
		boolean flag = false;
		if (holders.size() != 0) {
			for (int i = 0; i < holders.size(); i++) {
				if (!holders.get(i).experiment_new_main_item_body_temp_info_et
						.getText().toString().equals("")) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	private boolean getVolIsNull() {
		boolean flag = false;
		if (holders.size() != 0) {
			for (int i = 0; i < holders.size(); i++) {
				if (!holders.get(i).experiment_new_main_item_body_vol_info_et
						.getText().toString().equals("")) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	
	private boolean getEnameIsNull() {
		boolean flag = false;
		if (holders.size() != 0) {
			for (int i = 0; i < holders.size(); i++) {
				if (!experiment_new_main_bottom_ename_info_et.getText()
						.toString().equals("")) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	private boolean getVolIsBig() {
		boolean flag = false;
		if (holders.size() != 0) {
			for (int i = 0; i < holders.size(); i++) {
				if (Integer
						.valueOf(holders.get(i).experiment_new_main_item_body_vol_info_et
								.getText().toString()) <= getVolMax()) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	private boolean getTempIsBig() {
		boolean flag = false;
		if (holders.size() != 0) {
			for (int i = 0; i < holders.size(); i++) {
				if (Integer
						.valueOf(holders.get(i).experiment_new_main_item_body_temp_info_et
								.getText().toString()) <= 120) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * Title: timeFormat Description: Modified By： Domon Modified Date:
	 * 2014-9-12
	 * 
	 * @param hour
	 * @param min
	 * @param sec
	 * @return
	 */
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
}

class CViewHolder {
	TextView experiment_new_main_item_top_name_tv;
	TextView experiment_new_main_item_head_wait_info_tv;
	TextView experiment_new_main_item_head_blend_info_tv;
	TextView experiment_new_main_item_head_magnet_info_tv;
	TextView experiment_new_main_item_body_hole_info_tv;
	TextView experiment_new_main_item_body_vol_info_et;
	TextView experiment_new_main_item_body_speed_info_tv;
	TextView experiment_new_main_item_body_temp_info_et;
	TextView experiment_new_main_item_body_switch_info_tv;
	TextView experiment_new_main_item_bottom_name_et;

	RelativeLayout experiment_new_main_item_body_switch_rl;
	RelativeLayout experiment_new_main_item_body_temp_rl;
}
