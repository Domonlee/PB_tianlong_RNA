package org.tianlong.rna.adapter;

import java.util.List;

import org.tianlong.rna.activity.MachineActivity;
import org.tianlong.rna.activity.R;
import org.tianlong.rna.pojo.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MachineAdminAdapter extends BaseAdapter {

	private Context context;
	private List<User> lists;
	private LayoutInflater inflater;
	private boolean modifyFlag;

	public MachineAdminAdapter(Context context, List<User> list) {
		super();
		this.context = context;
		this.lists = list;
		this.inflater = LayoutInflater.from(context);
	}

	public MachineAdminAdapter(Context context, List<User> list,boolean flag) {
		super();
		this.context = context;
		this.lists = list;
		this.modifyFlag = flag;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.activity_machine_user_admin_item,
					null);
			holder.user_admin_item_name_tv = (TextView) arg1
					.findViewById(R.id.user_admin_item_name_tv);
			holder.user_admin_item_admin_tv = (TextView) arg1
					.findViewById(R.id.user_admin_item_admin_tv);
			holder.user_admin_item_login_iv = (ImageView) arg1
					.findViewById(R.id.user_admin_item_login_iv);
			holder.user_admin_item_modify_psw_iv = (ImageView) arg1
					.findViewById(R.id.user_admin_item_modify_psw_iv);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		holder.user_admin_item_name_tv.setText(lists.get(arg0).getUname());

		if (modifyFlag) {
			holder.user_admin_item_modify_psw_iv
					.setBackgroundResource(R.drawable.user_admin_modify_icon);
		}else {
			holder.user_admin_item_modify_psw_iv.setVisibility(View.INVISIBLE);
		}

		if (lists.get(arg0).getUadmin() == 1) {
			holder.user_admin_item_admin_tv.setText(context
					.getString(R.string.user_admin_admin));
			holder.user_admin_item_modify_psw_iv.setVisibility(View.INVISIBLE);
		} else {
			holder.user_admin_item_admin_tv.setText(context
					.getString(R.string.user_admin_user));
		}

		if (lists.get(arg0).getUdefault() == 1) {
			holder.user_admin_item_login_iv
					.setBackgroundResource(R.drawable.yes);
		} else {
			holder.user_admin_item_login_iv
					.setBackgroundResource(R.drawable.error);
		}
		
		return arg1;
	}

	class ViewHolder {
		TextView user_admin_item_name_tv;
		TextView user_admin_item_admin_tv;
		ImageView user_admin_item_login_iv;
		ImageView user_admin_item_modify_psw_iv;
	}
}
