package org.tianlong.rna.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tianlong.rna.adapter.DownAdapter;
import org.tianlong.rna.adapter.UpAdapter;
import org.tianlong.rna.dao.ExperimentDao;
import org.tianlong.rna.dao.StepDao;
import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.pojo.Step;
import org.tianlong.rna.utlis.Utlis;
import org.tianlong.rna.utlis.WifiUtlis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
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
	
	private List<Experiment> experiments;
	private ExperimentDao experimentDao;
	private StepDao stepDao;
	private boolean readListFlag = true,getInfoListFlag = true, sendFileFlag = true;						//读取列表线程控制，得到下位机文件线程控制，上位机发送文件线程控制
	//private String receiveMeg;																			//接收信息
	private WifiUtlis wifiUtlis;																			//wifi控制工具
	private List<String> receive;																			//接受信息列表
	private List<String> infoList;																			//转换后的文件列表
	private List<Map<String, Object>> experimentsList;					 									//下位机实验列表
	private List<Map<String, Object>> upViewList;															//上传点击列表
	private List<Map<String, Object>> downViewList;															//下载点击列表
	private Map<String, Object> map;																		//点击保存列表
	private getExperimentInfoThread experimentInfoThread;
	private sendFileThread fileThread;
	private readListThread listThread;
	private int U_id,up_id = -1,down_id = -1;
	private String Uname;
	private int sendControlNum;																				//发送文件控制数
	
	//Pad发送文件handler
	private Handler sendFileHandler = new Handler(){
		public void handleMessage(Message msg) {
			String info = (String)msg.obj;
			if(info.length() != 0){
				sendControlNum++;
				if(sendControlNum >= 3 && sendControlNum <= (infoList.size()-3)){
					if(Utlis.checkReceive(info,1)){
						try {
							wifiUtlis.sendMessage(Utlis.getMessageByte(infoList.get(sendControlNum), infoList.get(sendControlNum+1),1));
							sendControlNum++;
						} catch (Exception e) {
							Toast.makeText(UpAndDownActivity.this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
						}
					}
					else{
						sendControlNum-=2;
						try {
							wifiUtlis.sendMessage(Utlis.getMessageByte(infoList.get(sendControlNum), infoList.get(sendControlNum+1),1));
							sendControlNum++;
						} catch (Exception e) {
							Toast.makeText(UpAndDownActivity.this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
						}
					}
				}
				else{
					if(Utlis.checkReceive(info,1)){
						if(sendControlNum<infoList.size()){
							try {
								wifiUtlis.sendMessage(Utlis.getbyteList(infoList.get(sendControlNum),1));
							} catch (Exception e) {
								Toast.makeText(UpAndDownActivity.this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
							}
						}
						else{
							//wifiUtlis.sendMessage(Utlis.getseleteMessage(6));
							//完成发送文件
							try {
								sendFileFlag = false;
								sendControlNum = 0;
								try {
									wifiUtlis = new WifiUtlis(UpAndDownActivity.this);
									wifiUtlis.sendMessage(Utlis.getseleteMessage(10));
									readListFlag = true;
									new Thread(listThread).start();
								} catch (Exception e) {
									Toast.makeText(UpAndDownActivity.this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								Toast.makeText(UpAndDownActivity.this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
							}
						}
					}
					else{
						try {
							sendControlNum-=2;
							wifiUtlis.sendMessage(Utlis.getbyteList(infoList.get(sendControlNum),1));
						} catch (Exception e) {
							Toast.makeText(UpAndDownActivity.this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		};
	};
	//Pad发送文件线程
	class sendFileThread implements Runnable{
		 public void run() {
			 while (sendFileFlag) {
				 try {
					 Message message = sendFileHandler.obtainMessage();
					 message.obj = wifiUtlis.getMessage();
					 if(((String)message.obj).length() != 0){
						 sendFileHandler.sendMessage(message);
					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		 };
	};
	
	//读取下位机实验列表接受handler
	private Handler readListHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			byte[] info = (byte[])msg.obj;
			if(info.length != 0){
				if(receive != null){
					receive.removeAll(receive);
				}
				receive = Utlis.getReceive(info);
				experimentsList = Utlis.getExperimentList(receive);
				activity_upanddown_down_top_lv.setAdapter(new DownAdapter(UpAndDownActivity.this, experimentsList));
				readListFlag = false;
			}
		};
	};
	//读取下位机实验列表接受线程
	class readListThread implements Runnable{
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
	};
	//将下位机实验数据转换成Pad实验数据handler
	private Handler getExperimentInfoHandler = new Handler(){
		public void handleMessage(Message msg) {
			byte[] info = (byte[])msg.obj;
			if(info.length != 0){
				if(receive != null){
					receive.removeAll(receive);
				}
				receive = Utlis.getReceive(info);
				infoList = Utlis.getExperimentInfoList(receive);
				Experiment experiment = new Experiment();
				if(infoList.size()!=0){
					if((infoList.get(infoList.size()-2).substring(0, 9)).indexOf("#END_FILE") != -1){
						experiment.setU_id(U_id);
						experiment.setEname(infoList.get(0).substring(infoList.get(0).indexOf(":")+1,infoList.get(0).length()));
						String date = Utlis.systemFormat.format(new Date());
						experiment.setCdate(date);
						experiment.setRdate(date);
						experiment.setEremark(infoList.get(2).substring(infoList.get(2).indexOf(":")+1,infoList.get(2).length()));
						experiment.setEDE_id(0);
						experiment.setEquick(0);
						experimentDao.insertExperiment(experiment, UpAndDownActivity.this);
						experiment = experimentDao.getExperimentByCdate(date, UpAndDownActivity.this, U_id);
						for (int i = 3; i < infoList.size(); i++) {
							if(infoList.get(i).indexOf("#END_FILE") != -1){
								break;
							}
							else{
								Step step = Utlis.getStepFromInfo(infoList.get(i), experiment.getE_id());
								stepDao.insertStep(step, UpAndDownActivity.this);
							}
						}
						experiments = experimentDao.getAllExperimentsByU_id(UpAndDownActivity.this, U_id);
						activity_upanddown_up_top_lv.setAdapter(new UpAdapter(UpAndDownActivity.this, experiments));
						getInfoListFlag = false;
						Toast.makeText(UpAndDownActivity.this, getString(R.string.up_success), Toast.LENGTH_SHORT);
					}
					else{
						Toast.makeText(UpAndDownActivity.this, getString(R.string.up_failure), Toast.LENGTH_SHORT);
						getInfoListFlag = false;
					}
				}
			}
		};
	};
	//读取下位机指定实验接受线程
	class getExperimentInfoThread implements Runnable{
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
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upanddown);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		Intent intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");
		experimentDao = new ExperimentDao();
		stepDao = new StepDao();
		experiments = experimentDao.getAllExperimentsByU_id(UpAndDownActivity.this, U_id);
		experimentsList = new ArrayList<Map<String,Object>>();
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
			Toast.makeText(UpAndDownActivity.this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
		}
		experiment_right_back_btn = (Button) findViewById(R.id.experiment_right_back_btn);
		activity_upanddown_up_top_btn = (Button) findViewById(R.id.activity_upanddown_up_top_btn);
		activity_upanddown_up_top_lv = (ListView) findViewById(R.id.activity_upanddown_up_top_lv);
		activity_upanddown_down_top_btn = (Button) findViewById(R.id.activity_upanddown_down_top_btn);
		activity_upanddown_down_top_lv = (ListView) findViewById(R.id.activity_upanddown_down_top_lv);
		activity_upanddown_top_uname_tv = (TextView) findViewById(R.id.activity_upanddown_top_uname_tv);
		
		activity_upanddown_top_uname_tv.setText(Uname);
		
		activity_upanddown_up_top_lv.setAdapter(new UpAdapter(UpAndDownActivity.this, experiments));
		
		activity_upanddown_up_top_lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(downViewList.size() != 0){
					((View)downViewList.get(0).get("view")).setBackgroundResource(R.drawable.up_down_select);
					down_id = -1;
					downViewList.removeAll(downViewList);
					map.clear();
				}
				if(upViewList.size() != 0){
					if(((Integer)upViewList.get(0).get("id")) == arg2){
						((View)upViewList.get(0).get("view")).setBackgroundResource(R.drawable.up_down_select);
						up_id = -1;
						upViewList.removeAll(upViewList);
						map.clear();
					}
					else{
						((View)upViewList.get(0).get("view")).setBackgroundResource(R.drawable.up_down_select);
						upViewList.removeAll(upViewList);
						map.clear();
						arg1.setBackgroundResource(R.drawable.list_btn_select);
						up_id = arg2;
						map.put("id", arg2);
						map.put("view", arg1);
						upViewList.add(map);
					}
				}
				else{
					arg1.setBackgroundResource(R.drawable.list_btn_select);
					up_id = arg2;
					map.put("id", arg2);
					map.put("view", arg1);
					upViewList.add(map);
				}
			}
		});
		
		activity_upanddown_down_top_lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(upViewList.size() != 0){
					((View)upViewList.get(0).get("view")).setBackgroundResource(R.drawable.up_down_select);
					up_id = -1;
					upViewList.removeAll(upViewList);
					map.clear();
				}
				if(downViewList.size() != 0){
					if(((Integer)downViewList.get(0).get("id")) == arg2){
						((View)downViewList.get(0).get("view")).setBackgroundResource(R.drawable.up_down_select);
						down_id = -1;
						downViewList.removeAll(downViewList);
						map.clear();
					}
					else{
						((View)downViewList.get(0).get("view")).setBackgroundResource(R.drawable.up_down_select);
						downViewList.removeAll(downViewList);
						map.clear();
						arg1.setBackgroundResource(R.drawable.list_btn_select);
						down_id = (Integer) experimentsList.get(arg2).get("id");
						map.put("id", arg2);
						map.put("view", arg1);
						downViewList.add(map);
					}
				}
				else{
					arg1.setBackgroundResource(R.drawable.list_btn_select);
					down_id = (Integer) experimentsList.get(arg2).get("id");
					map.put("id", arg2);
					map.put("view", arg1);
					downViewList.add(map);
				}
			}
		});
		activity_upanddown_down_top_lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(UpAndDownActivity.this);
				builder.setTitle(getString(R.string.delete_file));
				builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						if(!readListFlag){
							readListFlag = true;
						}
						try {
							wifiUtlis.sendMessage(Utlis.sendDeleteExperiment((Integer)experimentsList.get(arg2).get("id")));
							wifiUtlis.sendMessage(Utlis.getseleteMessage(10));
							new Thread(listThread).start();
						} catch (Exception e) {
							Toast.makeText(UpAndDownActivity.this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
						}
					}
				});
				builder.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.show();
				return false;
			}
		});
		
		activity_upanddown_up_top_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(up_id == -1){
					Toast.makeText(UpAndDownActivity.this, getString(R.string.no_select), Toast.LENGTH_SHORT).show();
				}
				else{
					if(experimentsList.size()>=15){
						Toast.makeText(UpAndDownActivity.this, getString(R.string.down_is_full), Toast.LENGTH_SHORT).show();
					}
					else{
						AlertDialog.Builder builder = new AlertDialog.Builder(UpAndDownActivity.this);
						builder.setTitle(getString(R.string.down_file));
						builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								try {
									if(infoList == null){
										infoList = Utlis.getPadExperimentInfoList(experiments.get(up_id), stepDao.getAllStep(UpAndDownActivity.this, experiments.get(up_id).getE_id()), Uname);
									}
									else{
										infoList.removeAll(infoList);
										infoList = Utlis.getPadExperimentInfoList(experiments.get(up_id), stepDao.getAllStep(UpAndDownActivity.this, experiments.get(up_id).getE_id()), Uname);
									}
									wifiUtlis.sendMessage(Utlis.getbyteList(infoList.get(0),1));
									if(!sendFileFlag){
										sendFileFlag = true;
									}
									new Thread(fileThread).start();
								} catch (Exception e) {
									Toast.makeText(UpAndDownActivity.this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
								}
							}
						});
						builder.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						builder.show();
					}
				}
			}
		});
		
		activity_upanddown_down_top_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(down_id == -1){
					Toast.makeText(UpAndDownActivity.this, getString(R.string.no_select), Toast.LENGTH_SHORT).show();
				}
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(UpAndDownActivity.this);
					builder.setTitle(getString(R.string.up_file));
					builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							try {
								wifiUtlis.sendMessage(Utlis.sendExperimentId(down_id));
								getInfoListFlag = true;
								new Thread(experimentInfoThread).start();
							} catch (Exception e) {
								Toast.makeText(UpAndDownActivity.this, getString(R.string.wifi_error), Toast.LENGTH_SHORT).show();
							}
						}
					});
					builder.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
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
				Intent intent = new Intent(UpAndDownActivity.this, MainActivity.class);
				intent.putExtra("U_id", U_id);
				intent.putExtra("Uname", Uname);
				startActivity(intent);
				finish();
			}
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		AlertDialog.Builder builder = new AlertDialog.Builder(UpAndDownActivity.this);
    		builder.setTitle(getString(R.string.sure_exit));
    		builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Process.killProcess(android.os.Process.myPid());
				}
			});
    		builder.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
    		builder.show();
    	}
		return super.onKeyDown(keyCode, event);
    }
}
