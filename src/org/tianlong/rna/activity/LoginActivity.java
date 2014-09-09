package org.tianlong.rna.activity;

import java.util.List;

import org.tianlong.rna.adapter.LoginAdapter;
import org.tianlong.rna.dao.ExperimentDao;
import org.tianlong.rna.dao.MachineDao;
import org.tianlong.rna.dao.StepDao;
import org.tianlong.rna.dao.UserDao;
import org.tianlong.rna.pojo.DefaultExperiment;
import org.tianlong.rna.pojo.DefaultStep;
import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.pojo.Machine;
import org.tianlong.rna.pojo.Step;
import org.tianlong.rna.pojo.User;
import org.tianlong.rna.utlis.Utlis;

import org.tianlong.rna.activity.R;

import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class LoginActivity extends Activity {

	private EditText login_name_et;
	private EditText login_pass_et;

	private Button login_name_btn;
	private Button login_button_login;
	private Button login_button_register;

	private ListView login_name_lv;

	private User user;
	private UserDao userDao;
	private ExperimentDao experimentDao;
	private MachineDao machineDao;
	private StepDao stepDao;
	private List<User> userList;
	private List<DefaultExperiment> defaultExperiments;
	// 点击快捷选择用户按钮计数器
	private int num;
	private int logout  = 0;
	private int U_id;

	private Intent intent;

	public static int language = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userDao = new UserDao();
		experimentDao = new ExperimentDao();
		stepDao = new StepDao();
		machineDao = new MachineDao();

		defaultExperiments = experimentDao
				.getAllDefaultExperiments(LoginActivity.this);
		intent = getIntent();

		if (defaultExperiments.size() == 0) {
			initial();
			Experiment experiment = new Experiment();
			experiment.setEname("Whole blood DNA");
			experiment.setEquick(0);
			experiment.setEremark("");
			String time = Utlis.getSystemTime();
			experiment.setCdate(time);
			experiment.setRdate(time);
			experiment.setEDE_id(0);
			experiment.setU_id(userDao.getUserByRUname("guest",
					LoginActivity.this).getU_id());

			experimentDao.insertExperiment(experiment, LoginActivity.this);

			experiment = experimentDao.getExperimentByCdate(time,
					LoginActivity.this,
					userDao.getUserByRUname("guest", LoginActivity.this)
							.getU_id());
			for (int i = 0; i < 7; i++) {
				Step step = new Step();
				switch (i) {
				case 0:
					step.setSname("Lysis");
					step.setShole(1);
					step.setSspeed(6);
					step.setSvol(750);
					step.setSwait("00:00:00");
					step.setSblend("00:20:00");
					step.setSmagnet("00:01:30");
					step.setSswitch(1);
					step.setStemp(65);
					step.setE_id(experiment.getE_id());
					break;
				case 1:
					step.setSname("Washing1");
					step.setShole(2);
					step.setSspeed(5);
					step.setSvol(600);
					step.setSwait("00:00:00");
					step.setSblend("00:03:00");
					step.setSmagnet("00:01:30");
					step.setSswitch(0);
					step.setStemp(0);
					step.setE_id(experiment.getE_id());
					break;
				case 2:
					step.setSname("Washing2");
					step.setShole(3);
					step.setSspeed(5);
					step.setSvol(600);
					step.setSwait("00:00:00");
					step.setSblend("00:03:00");
					step.setSmagnet("00:01:30");
					step.setSswitch(2);
					step.setStemp(65);
					step.setE_id(experiment.getE_id());
					break;
				case 3:
					step.setSname("Washing3");
					step.setShole(4);
					step.setSspeed(5);
					step.setSvol(600);
					step.setSwait("00:00:00");
					step.setSblend("00:02:00");
					step.setSmagnet("00:01:30");
					step.setSswitch(2);
					step.setStemp(65);
					step.setE_id(experiment.getE_id());
					break;
				case 4:
					step.setSname("Washing4");
					step.setShole(5);
					step.setSspeed(5);
					step.setSvol(600);
					step.setSwait("00:00:00");
					step.setSblend("00:00:00");
					step.setSmagnet("00:00:30");
					step.setSswitch(2);
					step.setStemp(65);
					step.setE_id(experiment.getE_id());
					break;
				case 5:
					step.setSname("Elution");
					step.setShole(6);
					step.setSspeed(6);
					step.setSvol(100);
					step.setSwait("00:00:00");
					step.setSblend("00:05:00");
					step.setSmagnet("00:01:30");
					step.setSswitch(2);
					step.setStemp(65);
					step.setE_id(experiment.getE_id());
					break;
				case 6:
					step.setSname("Move the magnet");
					step.setShole(2);
					step.setSspeed(5);
					step.setSvol(600);
					step.setSwait("00:00:00");
					step.setSblend("00:01:00");
					step.setSmagnet("00:00:00");
					step.setSswitch(0);
					step.setStemp(0);
					step.setE_id(experiment.getE_id());
					break;
				default:
					break;
				}
				stepDao.insertStep(step, LoginActivity.this);
			}
		}
		language = machineDao.getMachine(LoginActivity.this).getMlanguage();
		Utlis.setLanguage(LoginActivity.this, language);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_login);
		if (logout == 9999) {
			user = userDao.getDefaultUser(this);
			if (user != null) {
				intent = new Intent(LoginActivity.this, MainActivity.class);
				intent.putExtra("U_id", user.getU_id());
				intent.putExtra("Uname", user.getUname());
				startActivity(intent);
				finish();
			}
		} else {
			user = userDao.getUserById(U_id, LoginActivity.this);
		}

		login_name_et = (EditText) findViewById(R.id.login_name_et);
		login_pass_et = (EditText) findViewById(R.id.login_pass_et);
		login_name_btn = (Button) findViewById(R.id.login_name_btn);
		login_button_login = (Button) findViewById(R.id.login_button_login);
		login_button_register = (Button) findViewById(R.id.login_button_register);
		login_name_lv = (ListView) findViewById(R.id.login_name_lv);
		if (logout != 2) {
			if (user != null) {
				login_name_et.setText(user.getUname());
			}
		}
		userList = userDao.getAllUserByAdmin(LoginActivity.this);
		login_name_lv.setAdapter(new LoginAdapter(LoginActivity.this, userList,
				login_name_et));

		login_name_lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				login_name_et.setText(userList.get(arg2).getUname());
				login_name_btn
						.setBackgroundResource(R.drawable.input_name_btn_down);
				login_name_lv.setVisibility(View.GONE);
				login_pass_et.setText("");
			}
		});

		// 点击快捷选择用户按钮监听
		login_name_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				num++;
				if (num % 2 == 0) {
					login_name_btn
							.setBackgroundResource(R.drawable.input_name_btn_down);
					login_name_lv.setVisibility(View.GONE);
				} else {
					login_name_btn
							.setBackgroundResource(R.drawable.input_name_btn_up);
					login_name_lv.setVisibility(View.VISIBLE);
				}
			}
		});
		// 点击登录按钮监听
		login_button_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (login_name_et.getText().toString().equals("")) {
					Toast.makeText(LoginActivity.this,
							getString(R.string.user_name_null),
							Toast.LENGTH_SHORT).show();
				} else if (login_pass_et.getText().toString().equals("")
						&& !login_name_et.getText().toString().equals("guest")) {
					Toast.makeText(LoginActivity.this,
							getString(R.string.user_pass_null),
							Toast.LENGTH_SHORT).show();
				} else {
					user = userDao.getUserByUnameAndUpass(login_name_et
							.getText().toString(), login_pass_et.getText()
							.toString(), LoginActivity.this);
					if (user != null) {
						intent = new Intent(LoginActivity.this,
								MainActivity.class);
						intent.putExtra("U_id", user.getU_id());
						intent.putExtra("Uname", user.getUname());
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(LoginActivity.this,
								getString(R.string.user_error),
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		// 点击注册按钮监听
		login_button_register.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initial() {
		List<DefaultExperiment> defaultExperiments = experimentDao
				.getAllDefaultExperiments(LoginActivity.this);
		if (defaultExperiments.size() == 0) {
			List<String> list = Utlis.getTemplate(LoginActivity.this);
			for (int i = 0; i < list.size(); i++) {
				DefaultExperiment defaultExperiment = new DefaultExperiment();
				defaultExperiment.setEname(list.get(i));
				experimentDao.insertDefaultExperiment(defaultExperiment,
						LoginActivity.this);
			}
		}

		defaultExperiments = experimentDao
				.getAllDefaultExperiments(LoginActivity.this);
		if (defaultExperiments.size() != 0) {
			for (int i = 0; i < defaultExperiments.size(); i++) {
				if (i != 0) {
					for (int j = 0; j < 8; j++) {
						DefaultStep defaultStep = new DefaultStep();
						switch (j) {
						case 0:
							defaultStep.setDSname("Step");
							defaultStep.setDShole(2);
							defaultStep.setDSspeed(3);
							defaultStep.setDSvol(500);
							defaultStep.setDSwait("00:00:00");
							defaultStep.setDSblend("00:01:00");
							defaultStep.setDSmagnet("00:00:30");
							defaultStep.setDSswitch(0);
							defaultStep.setDStemp(0);
							defaultStep.setDE_id(defaultExperiments.get(i)
									.getE_id());
							break;
						case 1:
							defaultStep.setDSname("Step");
							defaultStep.setDShole(1);
							defaultStep.setDSspeed(5);
							defaultStep.setDSvol(550);
							defaultStep.setDSwait("00:00:30");
							defaultStep.setDSblend("00:03:00");
							defaultStep.setDSmagnet("00:00:30");
							defaultStep.setDSswitch(1);
							defaultStep.setDStemp(80);
							defaultStep.setDE_id(defaultExperiments.get(i)
									.getE_id());
							break;
						case 2:
							defaultStep.setDSname("Step");
							defaultStep.setDShole(2);
							defaultStep.setDSspeed(5);
							defaultStep.setDSvol(500);
							defaultStep.setDSwait("00:00:00");
							defaultStep.setDSblend("00:02:00");
							defaultStep.setDSmagnet("00:00:30");
							defaultStep.setDSswitch(0);
							defaultStep.setDStemp(0);
							defaultStep.setDE_id(defaultExperiments.get(i)
									.getE_id());
							break;
						case 3:
							defaultStep.setDSname("Step");
							defaultStep.setDShole(3);
							defaultStep.setDSspeed(3);
							defaultStep.setDSvol(400);
							defaultStep.setDSwait("00:00:00");
							defaultStep.setDSblend("00:02:00");
							defaultStep.setDSmagnet("00:00:30");
							defaultStep.setDSswitch(0);
							defaultStep.setDStemp(0);
							defaultStep.setDE_id(defaultExperiments.get(i)
									.getE_id());
							break;
						case 4:
							defaultStep.setDSname("Step");
							defaultStep.setDShole(4);
							defaultStep.setDSspeed(4);
							defaultStep.setDSvol(300);
							defaultStep.setDSwait("00:00:00");
							defaultStep.setDSblend("00:02:00");
							defaultStep.setDSmagnet("00:00:30");
							defaultStep.setDSswitch(0);
							defaultStep.setDStemp(0);
							defaultStep.setDE_id(defaultExperiments.get(i)
									.getE_id());
							break;
						case 5:
							defaultStep.setDSname("Step");
							defaultStep.setDShole(5);
							defaultStep.setDSspeed(2);
							defaultStep.setDSvol(550);
							defaultStep.setDSwait("00:00:00");
							defaultStep.setDSblend("00:00:40");
							defaultStep.setDSmagnet("00:00:30");
							defaultStep.setDSswitch(2);
							defaultStep.setDStemp(90);
							defaultStep.setDE_id(defaultExperiments.get(i)
									.getE_id());
							break;
						case 6:
							defaultStep.setDSname("Step");
							defaultStep.setDShole(6);
							defaultStep.setDSspeed(3);
							defaultStep.setDSvol(100);
							defaultStep.setDSwait("00:00:30");
							defaultStep.setDSblend("00:03:00");
							defaultStep.setDSmagnet("00:01:30");
							defaultStep.setDSswitch(2);
							defaultStep.setDStemp(90);
							defaultStep.setDE_id(defaultExperiments.get(i)
									.getE_id());
							break;
						case 7:
							defaultStep.setDSname("Step");
							defaultStep.setDShole(2);
							defaultStep.setDSspeed(3);
							defaultStep.setDSvol(500);
							defaultStep.setDSwait("00:00:00");
							defaultStep.setDSblend("00:00:20");
							defaultStep.setDSmagnet("00:00:30");
							defaultStep.setDSswitch(0);
							defaultStep.setDStemp(0);
							defaultStep.setDE_id(defaultExperiments.get(i)
									.getE_id());
							break;
						default:
							break;
						}
						stepDao.insertDStep(defaultStep, LoginActivity.this);
					}
				}
			}
		}

		List<User> userlist = userDao.getAllUser(LoginActivity.this);
		if (userlist.size() == 0) {
			User user = new User();
			user.setUdefault(0);
			user.setUname("admin");
			user.setUpass("admin");
			user.setUadmin(1);
			userDao.insertUser(user, LoginActivity.this);
			user.setUdefault(1);
			user.setUname("guest");
			user.setUpass("");
			user.setUadmin(0);
			userDao.insertUser(user, LoginActivity.this);
		}

		Machine machine = machineDao.getMachine(LoginActivity.this);
		if (machine == null) {
			machine = new Machine();
			machine.setMlanguage(0);
			machine.setMflux(2);
			machine.setMblend(0);
			machine.setMmagnet(0);
			machine.setMhole(0);
			machine.setMname("NP968-IV");
			machine.setMip("192.168.1.200");
			machine.setMmac("00:00:00:00:00:00");
			machine.setMgateway("192.168.1.2");
			machine.setMmask("255.255.255.0");
			machine.setMDtime("01:00:00");
			machineDao.insertMachine(LoginActivity.this, machine);
		}
	}

	//Show Dialog For Close Activity
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LoginActivity.this);
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
			if (LoginActivity.this.getCurrentFocus() != null) {
				if (LoginActivity.this.getCurrentFocus().getWindowToken() != null) {
					imm.hideSoftInputFromWindow(LoginActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}
}
