package org.tianlong.rna.adapter;

import java.util.List;
import java.util.Map;

import org.tianlong.rna.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainApplyAdapter extends BaseAdapter {
	//private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	
	public MainApplyAdapter(Context context, List<Map<String, Object>> list) {
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
		ImageView main_item_iv;
		TextView main_item_tv;
		TextView main_item_id_tv;
		RelativeLayout main_item_rl;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_maina_item, null);
			holder.main_item_rl = (RelativeLayout) convertView.findViewById(R.id.main_item_rl);
			holder.main_item_iv = (ImageView) convertView.findViewById(R.id.main_item_iv);
			holder.main_item_tv = (TextView) convertView.findViewById(R.id.main_item_tv);
			holder.main_item_id_tv = (TextView) convertView.findViewById(R.id.main_item_id_tv);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = list.get(position);
		holder.main_item_iv.setImageResource((Integer)map.get("image"));
		holder.main_item_tv.setText((String)map.get("info"));
		holder.main_item_id_tv.setText((Integer)map.get("id")+"");
		return convertView;
	}
}
