package org.tianlong.rna.adapter;

import java.util.List;
import java.util.Map;

import org.tianlong.rna.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DownAdapter extends BaseAdapter {
	
	//private Context context;
	private List<Map<String, Object>> strings;
	private LayoutInflater inflater;
	
	public DownAdapter(Context context, List<Map<String, Object>> strings) {
		super();
		//this.context = context;
		this.strings = strings;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strings.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return strings.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.activity_upanddown_left_listview_item, null);
			holder.left_listview_item_name = (TextView) arg1.findViewById(R.id.left_listview_item_name);
			arg1.setTag(holder);
		}
		else{
			holder = (ViewHolder) arg1.getTag();
		}
		holder.left_listview_item_name.setText((String)strings.get(arg0).get("info")+" -- "+(String)strings.get(arg0).get("user"));
		return arg1;
	}

	class ViewHolder{
		TextView left_listview_item_name;
	}
}
