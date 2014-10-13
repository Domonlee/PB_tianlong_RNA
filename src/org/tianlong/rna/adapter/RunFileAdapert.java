package org.tianlong.rna.adapter;

import java.util.List;
import java.util.Map;

import org.tianlong.rna.activity.R;
import org.tianlong.rna.activity.RunFileActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RunFileAdapert extends BaseAdapter {

	private List<Map<String, Object>> strings;
	// private Context context;
	private LayoutInflater inflater;

	public RunFileAdapert(List<Map<String, Object>> strings, Context context) {
		super();
		this.strings = strings;
		// this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return strings.size();
	}

	@Override
	public Object getItem(int position) {
		return strings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.activity_runfile_item, null);
			holder.textView = (TextView) convertView
					.findViewById(R.id.activity_runfile_item_tv);
			holder.newItemImage = (ImageView) convertView
					.findViewById(R.id.activity_runfile_item_new_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView.setText((String) strings.get(position).get("info"));
		// 设置背景
		if ( position == RunFileActivity.listChooseId) {
			convertView.setBackgroundResource(R.drawable.list_select);
		} else {
			Log.w("RunAdapert", "else position=" + position);
			convertView.setBackgroundResource(R.drawable.list_bg);
		}
		// 设置标志位
		if (strings.get(position).get("id").equals(1)) {
			holder.newItemImage.setVisibility(View.VISIBLE);
		} else {
			holder.newItemImage.setVisibility(View.GONE);
		}

		return convertView;
	}

	class ViewHolder {
		TextView textView;
		ImageView newItemImage;
	}
}
