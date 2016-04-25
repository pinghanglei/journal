package com.qtest.journal.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qtest.journal.R;

import com.qtest.journal.imageUtils.ImageCallback;
import com.qtest.journal.imageUtils.ImageLoad;
import com.qtest.journal.imageUtils.OptionsManager;
import com.qtest.journal.model.Share;

import com.qtest.journal.util.BaseUtil;

public class ShareAdapter extends BaseAdapter {
	private Activity activity;
	private List<Share> sharelist;
	private ImageLoad imageLoad = ImageLoad.getInstence();
	int digok;
	Drawable left;
	int err = 1;

	public ShareAdapter(Activity activity, List<Share> sharelist) {
		super();
		this.activity = activity;
		this.sharelist = sharelist;
	}

	@Override
	public int getCount() {
		if (sharelist != null) {
			return sharelist.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return sharelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		Share share = sharelist.get(position);
		if (!TextUtils.isEmpty(share.getImgurl())) {
			return 1;
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(activity);
			convertView = inflater.inflate(R.layout.share_list_item, null);

			holder.item_view = convertView.findViewById(R.id.item_view);
			//
			holder.share_img = (ImageView) convertView
					.findViewById(R.id.share_img);
			holder.share_title = (TextView) convertView
					.findViewById(R.id.share_title);
			holder.share_read = (TextView) convertView
					.findViewById(R.id.share_read);
			holder.share_name = (TextView) convertView
					.findViewById(R.id.share_name);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Share share = sharelist.get(position);
		String imgUrl = share.getImgurl();
		setData(position, convertView, holder, share, imgUrl);

		return convertView;
	}

	private void setData(int position, View convertView, ViewHolder holder,
			Share share, String imgUrl) {

		if (!TextUtils.isEmpty(imgUrl)) {
			holder.share_img.setTag(imgUrl);
			holder.share_img.setImageBitmap(null);
			imageLoad.loadBitmap(activity, imgUrl, new ImageCallback(
					holder.share_img), OptionsManager.BIG_IMAGE, false);
			holder.share_img.setVisibility(View.VISIBLE);
		} else {
			// holder.share_img.setVisibility(View.GONE);
		}
		String title = share.getTitle();
		holder.share_title.setText(title);
		String read_num = share.getReadNum();
		holder.share_read.setText(read_num);
		String name = share.getName();
		holder.share_name.setText(name);
		holder.item_view.setOnClickListener(new MyOnClickListener(position));

	}

	static class ViewHolder {
		View item_view;
		ImageView share_img;
		TextView share_name;
		TextView share_read;
		TextView share_title;

	}

	class MyOnClickListener implements OnClickListener {
		private int position;

		public MyOnClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			int id = v.getId();
			Share share = sharelist.get(position);
			switch (id) {
			case R.id.item_view:

				break;

			}
		}

	}

	public void addData(List<Share> tucao) {

		if (sharelist == null) {
			sharelist = tucao;
		}
		if (tucao != null) {
			sharelist.addAll(tucao);
		}
	}

	public List<Share> getShareList() {
		return sharelist;
	}

	public void setShareList(List<Share> sharelist) {
		this.sharelist = sharelist;

	}

}
