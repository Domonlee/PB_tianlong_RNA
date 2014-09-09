package org.tianlong.rna.activity;

import java.util.List;
import java.util.Map;

import org.tianlong.rna.activity.R;
import org.tianlong.rna.adapter.MainApplyAdapter;
import org.tianlong.rna.dao.ExperimentDao;
import org.tianlong.rna.pojo.Experiment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class QuickExperimentActivity extends Activity {
	
	private GridView mb_bottom_gv;
	private List<Map<String, Object>> list;
	private String Uname = null;
	private int U_id;
	private ExperimentDao experimentDao;
	private Dialog dialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainb);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		experimentDao = new ExperimentDao();
		Intent intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");
		list = experimentDao.getExperimentByQuick(QuickExperimentActivity.this,U_id);
		mb_bottom_gv = (GridView) findViewById(R.id.mb_bottom_gv);
		
		mb_bottom_gv.setAdapter(new MainApplyAdapter(QuickExperimentActivity.this,list));
		
		mb_bottom_gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Map<String, Object> map = list.get(arg2);
				AlertDialog.Builder builder = new AlertDialog.Builder(QuickExperimentActivity.this);
				builder.setMessage(getString(R.string.exp_prepareing));
				builder.setCancelable(false);
				dialog = builder.show();
				Intent intent = new Intent(QuickExperimentActivity.this, RunExperimentActivity.class);
				intent.putExtra("E_id", (Integer)map.get("id"));
				intent.putExtra("Uname", Uname);
				intent.putExtra("U_id", U_id);
				intent.putExtra("jump", "quick");
				finish();
				startActivity(intent);
			}
		});
		
		mb_bottom_gv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(QuickExperimentActivity.this);
				builder.setTitle(getString(R.string.quick_cancle));
				String[] item = new String[]{getString(R.string.quick_now_cancle),getString(R.string.quick_all_cancle)};
				builder.setItems(item, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							changQuick(arg2);
							break;
						case 1:
							for (int i = 0; i < list.size(); i++) {
								changQuick(i);
							}
							break;
						default:
							break;
						}
						list = experimentDao.getExperimentByQuick(QuickExperimentActivity.this,U_id);
						mb_bottom_gv.setAdapter(new MainApplyAdapter(QuickExperimentActivity.this,list));
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
	}
	private void changQuick(int num){
		Map<String, Object> map = list.get(num);
		int E_id = (Integer) map.get("id");
		Experiment experiment = experimentDao.getExperimentById(E_id, QuickExperimentActivity.this,U_id);
		experiment.setEquick(0);
		experimentDao.updateExperiment(experiment, QuickExperimentActivity.this);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		AlertDialog.Builder builder = new AlertDialog.Builder(QuickExperimentActivity.this);
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(dialog != null){
			dialog.dismiss();
		}
	}
}
