package org.tianlong.rna.activity;

import org.tianlong.rna.dao.UserDao;
import org.tianlong.rna.pojo.User;

import org.tianlong.rna.activity.R;

import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class RegisterActivity extends Activity {
	
	private EditText register_name_et;
	private EditText register_pass_et;
	private EditText register_rpass_et;
	
	private Button register_register_button;
	private Button register_cancle_button;
	
	private User user;
	private UserDao dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		dao = new UserDao();
		
		register_name_et = (EditText) findViewById(R.id.register_name_et);
		register_pass_et = (EditText) findViewById(R.id.register_pass_et);
		register_rpass_et = (EditText) findViewById(R.id.register_rpass_et);
		register_register_button = (Button) findViewById(R.id.register_register_button);
		register_cancle_button = (Button) findViewById(R.id.register_cancle_button);
		
		register_register_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(register_name_et.getText().toString().equals("")){
					Toast.makeText(RegisterActivity.this, getString(R.string.user_name_input), Toast.LENGTH_SHORT).show();
				}
				else if(register_pass_et.getText().toString().equals("")){
					Toast.makeText(RegisterActivity.this, getString(R.string.user_pass_input), Toast.LENGTH_SHORT).show();
				}
				else if(register_rpass_et.getText().toString().equals("")){
					Toast.makeText(RegisterActivity.this, getString(R.string.user_rpass_null), Toast.LENGTH_SHORT).show();
				}
				else{
					if(dao.getUserByUname(register_name_et.getText().toString(), RegisterActivity.this)){
						Toast.makeText(RegisterActivity.this, getString(R.string.user_name_ture), Toast.LENGTH_SHORT).show();
					}
					else{
						if(register_rpass_et.getText().toString().equals(register_pass_et.getText().toString())){
							user = new User();
							user.setUname(register_name_et.getText().toString());
							user.setUpass(register_pass_et.getText().toString());
							user.setUadmin(0);
							user.setUdefault(0);
							if(dao.insertUser(user, RegisterActivity.this)){
								Toast.makeText(RegisterActivity.this, getString(R.string.user_register_success), Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
								intent.putExtra("logout", 2);
								startActivity(intent);
								finish();
							}
						}
						else{
							Toast.makeText(RegisterActivity.this, getString(R.string.user_pass_rpass), Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
		
		register_cancle_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
				intent.putExtra("logout", 2);
				startActivity(intent);
				finish();
			}
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
	public boolean onTouchEvent(MotionEvent event) {  
	    InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	    // TODO Auto-generated method stub  
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        if (RegisterActivity.this.getCurrentFocus() != null) {  
	            if (RegisterActivity.this.getCurrentFocus().getWindowToken() != null) {  
	                imm.hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus().getWindowToken(),  
	                        InputMethodManager.HIDE_NOT_ALWAYS);  
	            }  
	        }  
	    }  
	    return super.onTouchEvent(event);  
	}
}
