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

public class UpanFlieAdapter extends BaseAdapter {
	//private Context context;
	private List<Map<String,Object>> list;
	private LayoutInflater inflater;
	
	
	public UpanFlieAdapter(Context context, List<Map<String, Object>> list) {
		super();
		//this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	class ViewHolder{
		TextView getflie_item_textview;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_getflie_item, null);
			holder.getflie_item_textview = (TextView) convertView.findViewById(R.id.getflie_item_textview);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.getflie_item_textview.setText((String)list.get(position).get("title"));
		return convertView;
	}

}
