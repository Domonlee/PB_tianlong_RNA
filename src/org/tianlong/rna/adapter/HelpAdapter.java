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
import android.widget.TextView;

public class HelpAdapter extends BaseAdapter {

	private List<Map<String, Object>> lists;
	private Context context;
	private LayoutInflater inflater;

	public HelpAdapter(List<Map<String, Object>> lists, Context context) {
		super();
		this.lists = lists;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_help_left_item,
					null);
			holder.help_left_item_iv = (ImageView) convertView
					.findViewById(R.id.help_left_item_iv);
			holder.help_left_item_tv = (TextView) convertView
					.findViewById(R.id.help_left_item_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.help_left_item_iv.setBackgroundResource((Integer) lists.get(
				position).get("image"));
		holder.help_left_item_tv.setText(context.getString((Integer) lists.get(
				position).get("name")));
		if (position == 0) {
			convertView.setBackgroundResource(R.drawable.list_select);
		}
		convertView.setBackgroundResource(R.drawable.list_bg);
		return convertView;
	}

	class ViewHolder {
		ImageView help_left_item_iv;
		TextView help_left_item_tv;
	}
}
