package org.tianlong.rna.adapter;

import java.util.ArrayList;
import java.util.List;

import org.tianlong.rna.pojo.Experiment;

import org.tianlong.rna.activity.ExperimentActivity;
import org.tianlong.rna.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExperimentAdapter extends BaseAdapter {

	// private Context context;
	private List<Experiment> experiments;
	private LayoutInflater inflater;
	public static List<View> views = new ArrayList<View>();

	public ExperimentAdapter(Context context, List<Experiment> experiments) {
		super();
		// this.context = context;
		this.experiments = experiments;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return experiments.size();
	}

	@Override
	public Object getItem(int arg0) {
		return experiments.get(arg0);
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
			arg1 = inflater.inflate(R.layout.activity_left_listview_item, null);
			holder.left_listview_item_name = (TextView) arg1
					.findViewById(R.id.left_listview_item_name);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}

		holder.left_listview_item_name
				.setText(experiments.get(arg0).getEname());
		if (experiments.size() != 0 && arg0 == ExperimentActivity.listChooseId) {
			arg1.setBackgroundResource(R.drawable.list_select);
			views.add(arg1);
		} else {
			arg1.setBackgroundResource(R.drawable.list_bg);
		}
		return arg1;
	}

	class ViewHolder {
		TextView left_listview_item_name;
	}
}
