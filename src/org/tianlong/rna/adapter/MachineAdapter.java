package org.tianlong.rna.adapter;

import java.util.ArrayList;
import java.util.List;

import org.tianlong.rna.activity.ExperimentActivity;
import org.tianlong.rna.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MachineAdapter extends BaseAdapter {
	
	//private Context context;
	private List<String> lists;
	private LayoutInflater inflater;
	public static List<View> views = new ArrayList<View>();
	
	public MachineAdapter(Context context, List<String> list) {
		super();
		//this.context = context;
		this.lists = list;
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
		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.activity_left_listview_item, null);
			holder.left_listview_item_name = (TextView) arg1.findViewById(R.id.left_listview_item_name);
			arg1.setTag(holder);
		}
		else{
			holder = (ViewHolder) arg1.getTag();
		}
		holder.left_listview_item_name.setText(lists.get(arg0));
		if(lists.size() != 0 && arg0 == ExperimentActivity.listChooseId){
			arg1.setBackgroundResource(R.drawable.list_select);
			views.add(arg1);
		}
		else{
			arg1.setBackgroundResource(R.drawable.list_bg);
		}
		return arg1;
	}

	class ViewHolder{
		TextView left_listview_item_name;
	}
}
