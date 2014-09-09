package org.tianlong.rna.adapter;

import java.util.List;

import org.tianlong.rna.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter {
	
	//private Context context;
	private List<String> list;
	private LayoutInflater inflater;
	
	
	
	public SpinnerAdapter(Context context, List<String> list) {
		super();
		//this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	class 	ViewHolder{
		TextView sp_item_ls;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_experiment_spinner_item,null);
			holder.sp_item_ls = (TextView) convertView.findViewById(R.id.sp_item_tv);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.sp_item_ls.setText(list.get(position));
		return convertView;
	}

}
