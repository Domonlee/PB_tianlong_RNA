package org.tianlong.rna.activity;

import org.tianlong.rna.activity.R;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {
	
	private RelativeLayout main_top_logout_rl;
	private RelativeLayout main_body_rl;
	private TextView main_top_uname_tv;
	private Intent intent;
	private int U_id;
	private String Uname;
	private GestureDetector gd;
	public static int id = 1;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		intent = getIntent();
		U_id = intent.getIntExtra("U_id", 9999);
		Uname = intent.getStringExtra("Uname");
		
		gd = new GestureDetector(handListener);
		
		
		main_top_uname_tv = (TextView) findViewById(R.id.main_top_uname_tv);
		main_top_logout_rl = (RelativeLayout) findViewById(R.id.main_top_logout_rl);
		main_body_rl = (RelativeLayout) findViewById(R.id.main_body_rl);
		SwitchActivity(id);
		
		main_top_uname_tv.setText(Uname);
		
		main_top_logout_rl.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				intent.putExtra("logout", 1);
				intent.putExtra("U_id", U_id);
				startActivity(intent);
				finish();
			}
		});
	}
	
	private void SwitchActivity(int tid){
		main_body_rl.removeAllViews();
    	Intent intent = null;
    	switch (tid) {
		case 1:
			intent = new Intent(MainActivity.this, MainApplyActivity.class);
			intent.putExtra("Uname", Uname);
			intent.putExtra("U_id", U_id);
			id = 1;
			break;
		case 2:
			intent = new Intent(MainActivity.this, QuickExperimentActivity.class);
			intent.putExtra("Uname", Uname);
			intent.putExtra("U_id", U_id);
			id = 2;
			break;
		default:
			break;
		}
    	Window subActivity = getLocalActivityManager().startActivity("subActivity", intent);
    	main_body_rl.addView(subActivity.getDecorView(),LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
    }
	private OnGestureListener handListener = new OnGestureListener() {
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
		public void onShowPress(MotionEvent e) {}
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			return false;
		}
		
		public void onLongPress(MotionEvent e) {}
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if((e1.getX()-e2.getX())>150&&Math.abs(velocityX)>150){
				id++;
				if(id>2){
					id--;
				}
			}
			else if((e2.getX()-e1.getX())>150&&Math.abs(velocityX)>150){
				id--;
				if(id<1){
					id++;
				}
			}
			SwitchActivity(id);
			return false;
		}
		public boolean onDown(MotionEvent e) {
			return false;
		}
	};
	public boolean dispatchTouchEvent(MotionEvent ev) {
		gd.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
