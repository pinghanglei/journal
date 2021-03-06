package com.qtest.journal.widget;

import com.qtest.journal.R;
import com.qtest.journal.util.ConfigUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SlideMenuListFragment extends ListFragment {

	private ConfigUtil configUtil;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_menu, null);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		configUtil = new ConfigUtil(getActivity());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SettingAdapter adapter = new SettingAdapter(getActivity());
		if (configUtil.getIsAnonimous() == 0) {
			adapter.add(new SampleItem(configUtil.getUserName(),
					R.drawable.image_touxiang, 0));
		} else {
			adapter.add(new SampleItem("匿名使用", R.drawable.image_touxiang, 1));
		}
		adapter.add(new SampleItem("检查更新", R.drawable.image_touxiang, 2));
		
		setListAdapter(adapter);

	}

	private class SampleItem {
		public String tag;
		public int iconResource;
		public int type;

		public SampleItem(String tag, int iconResource, int type) {
			this.tag = tag;
			this.iconResource = iconResource;
			this.type = type;
		}
	}

	private class SettingAdapter extends ArrayAdapter<SampleItem> {
		private Context mContext;

		public SettingAdapter(Context context) {
			super(context, 0);
			this.mContext = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final int type = getItem(position).type;
			if (0 == type || 1 == type) {
				if (convertView == null) {
					convertView = LayoutInflater.from(getContext()).inflate(
							R.layout.list_row_accout, null);
				}
				ImageView icon = (ImageView) convertView
						.findViewById(R.id.id_slidemenu_account_touxiang_imageview);
//				if(icon!=null){
					icon.setImageResource(getItem(position).iconResource);
//				}
				

				TextView title = (TextView) convertView
						.findViewById(R.id.id_slidemenu_username_textview);
//				if(title!=null){
					title.setText(getItem(position).tag);
//				}
				
			} else {
				if (convertView == null) {
					convertView = LayoutInflater.from(getContext()).inflate(
							R.layout.list_row, null);
				}
				ImageView icon = (ImageView) convertView
						.findViewById(R.id.row_icon);
//				if(icon!=null){
					icon.setImageResource(getItem(position).iconResource);
//				}
				
				TextView title = (TextView) convertView
						.findViewById(R.id.row_title);
//				if(title!=null){
					title.setText(getItem(position).tag);
//				}
				
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction("SLIDEMENU.ITEM.CLICK");
					intent.putExtra("type", type);
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(
							intent);
				}
			});

			return convertView;
		}

	}

}
