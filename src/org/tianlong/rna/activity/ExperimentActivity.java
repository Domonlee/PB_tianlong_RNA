package org.tianlong.rna.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tianlong.rna.adapter.ExperimentAdapter;
import org.tianlong.rna.adapter.SpinnerAdapter;
import org.tianlong.rna.dao.ExperimentDao;
import org.tianlong.rna.dao.StepDao;
import org.tianlong.rna.pojo.DefaultExperiment;
import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.pojo.Step;
import org.tianlong.rna.utlis.Utlis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExperimentActivity extends Activity {

	private TextView experiment_top_uname_tv;
	private RelativeLayout expetiment_left_new;
	private RelativeLayout experiment_left_delete;
	private ListView experiment_left_lv;
	private RelativeLayout experiment_right_rl;
	private TextView expetiment_left_new_btn;
	private Button experiment_right_back_btn;
	private ImageView expetiment_left_new_btn_iv;

	private int U_id;
	private String Uname;

	private ExperimentDao experimentDao;
	private StepDao stepDao;
	private List<Experiment> experiments;
	private List<Map<String, Object>> views;
	private Map<String, Object> map;
	private Dialog dialog;
	private View view;

	private int chooseE_id = -1;
	public static int listChooseId = 0;

	// 新建准备页面控件
	private EditText experiment_new_prepare_body_name_et;
	private CheckBox experiment_new_prepare_body_quick_cb;
	private EditText experiment_new_prepare_body_remark_et;
	private Button experiment_new_prepare_body_btn_next;
	private Spinner experiment_new_prepare_body_template_sp;
	private List<DefaultExperiment> defaultExperiments;
	private int newControlNum;
	private int DE_id;

	// 实验信息页面控件
	private Button experiment_info_body_save_btn;
	private TextView experiment_info_top_ename_tv;
	private TextView experiment_info_body_cdate_info_tv;
	private TextView experiment_info_body_rdate_info_tv;
	private CheckBox experiment_info_body_quick_cb;
	private EditText experiment_info_body_remark_info_et;
	private TextView experiment_info_body_remark_info_tv;
	private Button experiment_info_bottom_edit_btn;
	private Button experiment_info_bottom_run_btn;
	private Button experiment_info_body_remark_info_btn;
	private Button experiment_info_body_remark_info_cancle_btn;
	private int expNum = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		initView();

		final ExperimentAdapter experimentAdapter = new ExperimentAdapter(
				ExperimentActivity.this, experiments);
		experiment_left_lv.setAdapter(experimentAdapter);

		if (experiments.size() != 0) {
			chooseE_id = experiments.get(listChooseId).getE_id();
			showView();
		}

		/**
		 * 
		 * Bugs: Open
		 */
		// Bug:删除上一项，下一项自动选中
		experiment_left_lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listChooseId = arg2;
				if (ExperimentAdapter.views.size() != 0) {
					for (int i = 0; i < ExperimentAdapter.views.size(); i++) {
						ExperimentAdapter.views.get(i).setBackgroundResource(
								R.drawable.list_bg);
					}
					ExperimentAdapter.views.removeAll(ExperimentAdapter.views);
				}
				if (view != null) {
					experiment_right_rl.removeView(view);
				}
				if (views.size() != 0) {
					if (((Integer) views.get(0).get("id")) == arg2) {
						((View) views.get(0).get("view"))
								.setBackgroundResource(R.drawable.list_bg);
						chooseE_id = -1;
						views.remove(0);
						map.clear();
					} else {
						((View) views.get(0).get("view"))
								.setBackgroundResource(R.drawable.list_bg);
						views.remove(0);
						map.clear();
						map.put("id", arg2);
						map.put("view", arg1);
						views.add(map);
						arg1.setBackgroundResource(R.drawable.list_select);
						chooseE_id = experiments.get(listChooseId).getE_id();
						showView();
					}
				} else {
					map.put("id", arg2);
					map.put("view", arg1);
					views.add(map);
					arg1.setBackgroundResource(R.drawable.list_select);
					chooseE_id = experiments.get(listChooseId).getE_id();
					showView();
				}
			}
		});

		experiment_left_delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (chooseE_id != -1) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ExperimentActivity.this);
					builder.setTitle(getString(R.string.exp_sure_to_delete));
					builder.setPositiveButton(getString(R.string.sure),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									List<Step> steps = stepDao
											.getAllStep(
													ExperimentActivity.this,
													chooseE_id);
									for (int i = 0; i < steps.size(); i++) {
										stepDao.deleteStep(steps.get(i),
												ExperimentActivity.this);
									}
									experimentDao.deleteExperiment(chooseE_id,
											ExperimentActivity.this);

									experiments = experimentDao
											.getAllExperimentsByU_id(
													ExperimentActivity.this,
													U_id);
									experiment_left_lv
											.setAdapter(new ExperimentAdapter(
													ExperimentActivity.this,
													experiments));
									if (view != null) {
										experiment_right_rl.removeView(view);
									}
									chooseE_id = -1;
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
				} else {
					Toast.makeText(ExperimentActivity.this,
							getString(R.string.exp_no_select),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		expetiment_left_new.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				newControlNum++;
				chooseE_id = -1;
				if (view != null) {
					experiment_right_rl.removeView(view);
				}
				if (newControlNum % 2 == 0) {
					expetiment_left_new_btn
							.setText(getString(R.string.exp_new_exp));
					experiment_left_delete.setVisibility(View.VISIBLE);
					experiment_left_lv.setVisibility(View.VISIBLE);
					expetiment_left_new_btn_iv.setVisibility(View.VISIBLE);
				} else {
					expetiment_left_new_btn.setText(getString(R.string.cancle));
					experiment_left_delete.setVisibility(View.GONE);
					experiment_left_lv.setVisibility(View.GONE);
					expetiment_left_new_btn_iv.setVisibility(View.GONE);
					showNewView();
				}
			}
		});

		experiment_right_back_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ExperimentActivity.this,
						MainActivity.class);
				intent.putExtra("U_id", U_id);
				intent.putExtra("Uname", Uname);
				listChooseId = 0;
				startActivity(intent);
				finish();
			}
		});
	}

	public void initView() {
		experiment_right_back_btn = (Button) findViewById(R.id.experiment_right_back_btn);
		expetiment_left_new_btn = (TextView) findViewById(R.id.expetiment_left_new_btn);
		experiment_top_uname_tv = (TextView) findViewById(R.id.experiment_top_uname_tv);
		expetiment_left_new = (RelativeLayout) findViewById(R.id.expetiment_left_new);
		experiment_left_delete = (RelativeLayout) findViewById(R.id.experiment_left_delete);
		experiment_left_lv = (ListView) findViewById(R.id.experiment_left_lv);
		experiment_right_rl = (RelativeLayout) findViewById(R.id.experiment_right_rl);
		expetiment_left_new_btn_iv = (ImageView) findViewById(R.id.expetiment_left_new_btn_iv);

		stepDao = new StepDao();
		experimentDao = new ExperimentDao();
		views = new ArrayList<Map<String, Object>>();
		map = new HashMap<String, Object>();

		Intent intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");
		defaultExperiments = experimentDao
				.getAllDefaultExperiments(ExperimentActivity.this);

		experiment_top_uname_tv.setText(Uname);
		experiments = experimentDao.getAllExperimentsByU_id(
				ExperimentActivity.this, U_id);
	}

	private void showNewView() {
		view = LayoutInflater.from(ExperimentActivity.this).inflate(
				R.layout.activity_experiment_new_prepare, null);
		experiment_new_prepare_body_name_et = (EditText) view
				.findViewById(R.id.experiment_new_prepare_body_name_et);
		experiment_new_prepare_body_quick_cb = (CheckBox) view
				.findViewById(R.id.experiment_new_prepare_body_quick_cb);
		experiment_new_prepare_body_remark_et = (EditText) view
				.findViewById(R.id.experiment_new_prepare_body_remark_et);
		experiment_new_prepare_body_btn_next = (Button) view
				.findViewById(R.id.experiment_new_prepare_body_btn_next);
		experiment_new_prepare_body_template_sp = (Spinner) view
				.findViewById(R.id.experiment_new_prepare_body_template_sp);

		experiment_new_prepare_body_template_sp.setAdapter(new SpinnerAdapter(
				ExperimentActivity.this, Utlis
						.getTemplate(ExperimentActivity.this)));

		experiment_new_prepare_body_quick_cb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							Toast.makeText(ExperimentActivity.this,
									getString(R.string.exp_add_quick),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ExperimentActivity.this,
									getString(R.string.exp_cancle_quick),
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		experiment_new_prepare_body_template_sp
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						DE_id = defaultExperiments.get(arg2).getE_id();
					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		// --Next Btn实现同名检测  未实现默认命名
		experiment_new_prepare_body_btn_next
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (experiment_new_prepare_body_name_et.getText()
								.toString().equals("")) {
							Toast.makeText(ExperimentActivity.this,
									getString(R.string.exp_name_null),
									Toast.LENGTH_SHORT).show();
							// experiment_new_prepare_body_name_et.setText("Experiment 1");
						} else {
							if (experimentDao.hasEname(ExperimentActivity.this,experiment_new_prepare_body_name_et.getText().toString(),U_id)){
								Toast.makeText(ExperimentActivity.this, getString(R.string.exp_name_already_exist),
										Toast.LENGTH_SHORT).show();
							} else {

								AlertDialog.Builder builder = new AlertDialog.Builder(
										ExperimentActivity.this);
								builder.setMessage(getString(R.string.exp_creating));
								builder.setCancelable(false);
								dialog = builder.show();
								Intent intent = new Intent(
										ExperimentActivity.this,
										CreatExperimentActivity.class);

								if (experiment_new_prepare_body_quick_cb
										.isChecked()) {
									intent.putExtra("Equick", 1);
								} else {
									intent.putExtra("Equick", 0);
								}

								intent.putExtra("Ename",
										experiment_new_prepare_body_name_et
												.getText().toString());
								intent.putExtra("Enum", DE_id);
								intent.putExtra("Eremark",
										experiment_new_prepare_body_remark_et
												.getText().toString());
								intent.putExtra("U_id", U_id);
								intent.putExtra("Uname", Uname);
								intent.putExtra("DE_id", DE_id);
								startActivity(intent);
								finish();
							}
						}
					}
				});
		experiment_right_rl.addView(view);
	}

	private void showView() {
		final Experiment experiment = experiments.get(listChooseId);
		view = LayoutInflater.from(ExperimentActivity.this).inflate(
				R.layout.activity_experiment_right_info, null);
		experiment_info_top_ename_tv = (TextView) view
				.findViewById(R.id.experiment_info_top_ename_tv);
		experiment_info_body_save_btn = (Button) view
				.findViewById(R.id.experiment_info_body_save_btn);
		experiment_info_body_cdate_info_tv = (TextView) view
				.findViewById(R.id.experiment_info_body_cdate_info_tv);
		experiment_info_body_rdate_info_tv = (TextView) view
				.findViewById(R.id.experiment_info_body_rdate_info_tv);
		experiment_info_body_quick_cb = (CheckBox) view
				.findViewById(R.id.experiment_info_body_quick_cb);
		experiment_info_body_remark_info_et = (EditText) view
				.findViewById(R.id.experiment_info_body_remark_info_et);
		experiment_info_body_remark_info_tv = (TextView) view
				.findViewById(R.id.experiment_info_body_remark_info_tv);
		experiment_info_bottom_edit_btn = (Button) view
				.findViewById(R.id.experiment_info_bottom_edit_btn);
		experiment_info_bottom_run_btn = (Button) view
				.findViewById(R.id.experiment_info_bottom_run_btn);
		experiment_info_body_remark_info_btn = (Button) view
				.findViewById(R.id.experiment_info_body_remark_info_btn);
		experiment_info_body_remark_info_cancle_btn = (Button) view
				.findViewById(R.id.experiment_info_body_remark_info_cancle_btn);

		experiment_info_top_ename_tv.setText(experiment.getEname());
		experiment_info_body_cdate_info_tv.setText(experiment.getCdate());
		experiment_info_body_rdate_info_tv.setText(experiment.getRdate());
		if (experiment.getEquick() == 0) {
			experiment_info_body_quick_cb.setChecked(false);
		} else {
			experiment_info_body_quick_cb.setChecked(true);
		}

		experiment_info_body_quick_cb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							experiment.setEquick(1);
							Toast.makeText(ExperimentActivity.this,
									getString(R.string.exp_add_quick),
									Toast.LENGTH_SHORT).show();
						} else {
							experiment.setEquick(0);
							Toast.makeText(ExperimentActivity.this,
									getString(R.string.exp_cancle_quick),
									Toast.LENGTH_SHORT).show();
						}
						experimentDao.updateExperiment(experiment,
								ExperimentActivity.this);
					}
				});

		experiment_info_body_remark_info_tv.setText(experiment.getEremark());
		experiment_info_body_remark_info_et.setText(experiment.getEremark());

		experiment_info_body_remark_info_tv
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						experiment_info_body_remark_info_cancle_btn
								.setVisibility(View.VISIBLE);
						experiment_info_body_remark_info_btn
								.setVisibility(View.VISIBLE);
						experiment_info_body_remark_info_et
								.setVisibility(View.VISIBLE);
						experiment_info_body_remark_info_et.setFocusable(true);
						experiment_info_body_remark_info_et
								.setFocusableInTouchMode(true);
						experiment_info_body_remark_info_et.requestFocus();
						InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
						im.showSoftInput(experiment_info_body_remark_info_et, 0);
						experiment_info_body_remark_info_tv
								.setVisibility(View.GONE);

					}
				});

		experiment_info_body_remark_info_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						experiment
								.setEremark(experiment_info_body_remark_info_et
										.getText().toString());
						if (experimentDao.updateExperiment(experiment,
								ExperimentActivity.this)) {
							Toast.makeText(ExperimentActivity.this,
									getString(R.string.exp_edit_success),
									Toast.LENGTH_SHORT).show();
							experiment_info_body_remark_info_cancle_btn
									.setVisibility(View.GONE);
							experiment_info_body_remark_info_btn
									.setVisibility(View.GONE);
							experiment_info_body_remark_info_et
									.setVisibility(View.GONE);
							experiment_info_body_remark_info_tv
									.setText(experiment_info_body_remark_info_et
											.getText().toString());
							experiment_info_body_remark_info_tv
									.setVisibility(View.VISIBLE);
						} else {
							Toast.makeText(ExperimentActivity.this,
									getString(R.string.exp_edit_failure),
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		experiment_info_body_remark_info_cancle_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						experiment_info_body_remark_info_cancle_btn
								.setVisibility(View.GONE);
						experiment_info_body_remark_info_btn
								.setVisibility(View.GONE);
						experiment_info_body_remark_info_et
								.setVisibility(View.GONE);
						experiment_info_body_remark_info_tv
								.setVisibility(View.VISIBLE);
					}
				});

		experiment_info_body_save_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});

		experiment_info_bottom_run_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								ExperimentActivity.this);
						builder.setMessage(getString(R.string.exp_prepareing));
						builder.setCancelable(false);
						dialog = builder.show();
						Intent intent = new Intent(ExperimentActivity.this,
								RunExperimentActivity.class);
						intent.putExtra("U_id", U_id);
						intent.putExtra("Uname", Uname);
						intent.putExtra("jump", "experiment");
						intent.putExtra("E_id", experiment.getE_id());
						startActivity(intent);
						finish();
					}
				});

		experiment_info_bottom_edit_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								ExperimentActivity.this);
						builder.setMessage(getString(R.string.exp_creating));
						builder.setCancelable(false);
						dialog = builder.show();
						Intent intent = new Intent(ExperimentActivity.this,
								EditExperimentActivity.class);
						intent.putExtra("U_id", U_id);
						intent.putExtra("Uname", Uname);
						intent.putExtra("E_id", experiment.getE_id());
						startActivity(intent);
						finish();
					}
				});

		experiment_right_rl.addView(view);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ExperimentActivity.this);
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
			if (ExperimentActivity.this.getCurrentFocus() != null) {
				if (ExperimentActivity.this.getCurrentFocus().getWindowToken() != null) {
					imm.hideSoftInputFromWindow(ExperimentActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}
}
