package org.tianlong.rna.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.tianlong.rna.activity.R;
import org.tianlong.rna.dao.ExperimentDao;
import org.tianlong.rna.dao.StepDao;
import org.tianlong.rna.dao.UserDao;
import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.pojo.Step;
import org.tianlong.rna.pojo.User;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginAdapter extends BaseAdapter {
	private Context context;
	private List<User> list;
	private LayoutInflater inflater;
	private EditText editText;
	private UserDao dao = new UserDao();
	private ExperimentDao experimentDao = new ExperimentDao();
	private StepDao stepDao = new StepDao();
	private List<Experiment> experiments = new ArrayList<Experiment>();
	private List<Step> steps = new ArrayList<Step>();
	private EditText login_delete_et;

	public LoginAdapter(Context context, List<User> list, EditText editText) {
		super();
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		this.editText = editText;
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

	class ViewHolder {
		TextView loading_layout_item_tv;
		Button loading_layout_item_btn;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_login_item, null);
			holder.loading_layout_item_tv = (TextView) convertView
					.findViewById(R.id.login_item_tv);
			holder.loading_layout_item_btn = (Button) convertView
					.findViewById(R.id.login_item_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.loading_layout_item_tv.setText(list.get(position).getUname());
		if (list.get(position).getUname().equals("guest")
				&& list.get(position).getUpass().equals("")) {
			holder.loading_layout_item_btn.setVisibility(View.GONE);
		}

		holder.loading_layout_item_btn
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						View view = inflater.inflate(
								R.layout.activity_login_delete_item, null);
						login_delete_et = (EditText) view
								.findViewById(R.id.login_delete_et);
						final AlertDialog.Builder builder = new AlertDialog.Builder(
								context);

						builder.setTitle(context
								.getString(R.string.user_pass_delete));
						builder.setView(view);
						builder.setPositiveButton(
								context.getString(R.string.sure),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										/*
										 * Use reflect to make the dialog show
										 */
										try {
											Field field = dialog
													.getClass()
													.getSuperclass()
													.getDeclaredField(
															"mShowing");
											field.setAccessible(true);
											field.set(dialog, false);
										} catch (Exception e) {
											e.printStackTrace();
										}
										if (login_delete_et
												.getText()
												.toString()
												.equals(list.get(position)
														.getUpass())) {
											experiments = experimentDao
													.getAllExperimentsByU_id(
															context,
															list.get(position)
																	.getU_id());
											for (int i = 0; i < experiments
													.size(); i++) {
												steps = stepDao.getAllStep(
														context, experiments
																.get(i)
																.getE_id());
												for (int j = 0; j < steps
														.size(); j++) {
													stepDao.deleteStep(
															steps.get(j),
															context);
												}
												experimentDao.deleteExperiment(
														experiments.get(i)
																.getE_id(),
														context);
											}
											dao.deleteUser(list.get(position),
													context);
											list.remove(position);
											notifyDataSetChanged();
											editText.setText("");
											Toast.makeText(
													context,
													context.getString(R.string.user_delete_successful),
													Toast.LENGTH_SHORT).show();
											try {
												Field field = dialog
														.getClass()
														.getSuperclass()
														.getDeclaredField(
																"mShowing");
												field.setAccessible(true);
												field.set(dialog, true);
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {

											Toast.makeText(
													context,
													context.getString(R.string.user_pass_error),
													Toast.LENGTH_SHORT).show();
										}
									}
								});
						builder.setNegativeButton(
								context.getString(R.string.cancle),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											Field field = dialog
													.getClass()
													.getSuperclass()
													.getDeclaredField(
															"mShowing");
											field.setAccessible(true);
											field.set(dialog, true);
										} catch (Exception e) {
											e.printStackTrace();
										}
										dialog.cancel();
									}
								});
						builder.show();
					}
				});
		return convertView;
	}
}
