package com.qtest.journal.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.slavesdk.MessageManager;
import com.qtest.journal.R;
import com.qtest.journal.adapter.ShareAdapter;
import com.qtest.journal.httpUtils.UriUtil.OnNetRequestListener;
import com.qtest.journal.imageUtils.ImageLoad;
import com.qtest.journal.model.Result;
import com.qtest.journal.model.Share;
import com.qtest.journal.slidingmenu.SlidingFragmentActivity;
import com.qtest.journal.slidingmenu.SlidingMenu;
import com.qtest.journal.storge.MessageListDataStorge;
import com.qtest.journal.task.GetShareListTask;
import com.qtest.journal.update.AppTool;
import com.qtest.journal.update.UpdateInfoTool;
import com.qtest.journal.update.UpdateInfoTool.UpdateInfo;
import com.qtest.journal.util.ConfigUtil;
import com.qtest.journal.util.Log;
import com.qtest.journal.widget.PullRefreshView;
import com.qtest.journal.widget.SlideMenuListFragment;
import com.qtest.journal.widget.PullRefreshView.OnRefreshListener;

@SuppressLint({ "ShowToast", "HandlerLeak" })
public class MainActivity extends SlidingFragmentActivity implements
		OnRefreshListener, OnClickListener, OnScrollListener {

	private SlidingMenu sm;
	private PullRefreshView pullRefreshView;
	private ListView listView;
	private LocalBroadcastManager localBroadCastManager;
	private ShareAdapter adapter;
	private ImageButton slideButton;
	private View footer;
	private Button more_bar;
	private LinearLayout loading_state_bar;
	private ConfigUtil configUtil;
	private int start = 0;
	private RelativeLayout title_rl;

	private List<Share> data;
	private View no_data_view;
	private View net_error_view;

	// 是否可以滑动获得更多数据
	private boolean isMoreAble = true;
	// 上次时间差
	private long lastDeltaTime = Long.MAX_VALUE;
	private TextView pop_text;

	private UpdateInfo info;
	private Dialog alertdialog_update;
	private int end;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		MessageManager.getInstance().initialize(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();

		initSlidingMenu();

		adapter = new ShareAdapter(this, null);
		listView.setAdapter(adapter);

		obtainShareFromDB();
		refresh();

		new UpdateTask().execute("");

	}

	private OnNetRequestListener<Result<List<Share>>> onNetRequestListener = new OnNetRequestListener<Result<List<Share>>>() {
		@Override
		public void onNetRequest(Result<List<Share>> result) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					pullRefreshView.completeRefresh();
				}
			}, 250);
			boolean isNetAble = false;
			if (result != null) {
				isNetAble = true;
				int errcode = result.getErr();
				List<Share> sharelist;
				sharelist = result.getData();

				ConfigUtil.saveRefreshTime(getApplicationContext(),
						System.currentTimeMillis());
				if (sharelist != null && sharelist.size() > 0) {

					// int size = tucaolist.size();
					// Tucao tucao = tucaolist.get(0);
					// start = Integer.valueOf(tucao.getId());
					// configUtil.setMsgId(start);
					end = Integer.valueOf(sharelist.get(sharelist.size() - 1)
							.getId());
					Log.e("start=" + start);

				}
				if (result.getMore().equals("null")) {
					isMoreAble = false;
				} else {
					isMoreAble = true;
				}

				new MessageListDataStorge(MainActivity.this).insert(sharelist);
				if (errcode == 0 && sharelist != null) {
					if (adapter != null) {
						adapter.setShareList(sharelist);
						adapter.notifyDataSetChanged();
					}

				} else {
//					Toast.makeText(MainActivity.this, "服务器返回数据失败", 0).show();
					// 服务器返回数据失败
				}
			} else {
				net_error_view.setVisibility(View.GONE);
				no_data_view.setVisibility(View.GONE);
				isNetAble = false;
//				Toast.makeText(MainActivity.this, "网络请求失败", 0).show();
				// 网络请求失败
			}
			if (isDataEmpty()) {
				// 缓存中有数据
				if (isNetAble) {
					net_error_view.setVisibility(View.GONE);
					no_data_view.setVisibility(View.VISIBLE);
				} else {
					net_error_view.setVisibility(View.VISIBLE);
					no_data_view.setVisibility(View.GONE);
				}
			} else {
				// 缓存无数据
				net_error_view.setVisibility(View.GONE);
				no_data_view.setVisibility(View.GONE);
			}
		}
	};
	private OnNetRequestListener<Result<List<Share>>> onMoreNetRequestListener = new OnNetRequestListener<Result<List<Share>>>() {

		@Override
		public void onNetRequest(Result<List<Share>> result) {
			if (result != null) {
				int err = result.getErr();
				if (result.getMore().equals("0")) {
					isMoreAble = false;
				} else {
					isMoreAble = true;
				}
				if (err == 0) {
					List<Share> sharelist = result.getData();
					if (sharelist == null || sharelist.size() == 0) {
						Toast.makeText(MainActivity.this, "已经没有更多数据了",
								Toast.LENGTH_SHORT).show();
						return;
					}
					Share share = sharelist.get(sharelist.size() - 1);
					end = Integer.valueOf(share.getId());
					if (adapter != null) {
						adapter.addData(sharelist);
						adapter.notifyDataSetChanged();
					} else {

						adapter = new ShareAdapter(MainActivity.this, sharelist);
						listView.setAdapter(adapter);
					}
				} else {
					Toast.makeText(MainActivity.this, "服务器返回数据失败",
							Toast.LENGTH_SHORT).show();
					// 服务器返回数据失败
				}
			} else {
				Toast.makeText(MainActivity.this, "网络请求失败", Toast.LENGTH_SHORT)
						.show();
				// 网络请求失败
			}
		}
	};

	public boolean isDataEmpty() {
		int count = 0;
		if (adapter != null) {
			count = adapter.getCount();
		}
		if (count != 0) {
			return false;
		}
		return true;
	}

	private void initViews() {

		slideButton = (ImageButton) findViewById(R.id.titlebar_manage_btn);
		slideButton.setOnClickListener(this);
		footer = LayoutInflater.from(this).inflate(R.layout.more_info_footer,
				null);
		footer.setVisibility(View.GONE);
		more_bar = (Button) footer.findViewById(R.id.more_bar);
		more_bar.setOnClickListener(this);
		loading_state_bar = (LinearLayout) footer
				.findViewById(R.id.loading_state_bar);
		listView = (ListView) findViewById(R.id.listview);

		title_rl = (RelativeLayout) findViewById(R.id.titlebar_main);

		listView.setOnScrollListener(this);
		listView.addFooterView(footer);
		pullRefreshView = (PullRefreshView) findViewById(R.id.pull_refresh_view);
		pullRefreshView.setRefreshListener(this);
		no_data_view = findViewById(R.id.no_data_view);
		net_error_view = findViewById(R.id.net_error_view);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.titlebar_manage_btn:
			toggle();
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		refresh();
	}

	private void refresh() {
		long lastTime = ConfigUtil.getRefreshTime(getApplicationContext());
		long currentTime = System.currentTimeMillis();
		long deltaTime = currentTime - lastTime;
		// 如果连续两次时间差都是 1s以内
		if (lastDeltaTime < 3 * 1000 && deltaTime < 3 * 1000) {
			startPopText("亲，刷得好快，休息一下吧", 2500);
			lastDeltaTime = deltaTime;

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					pullRefreshView.completeRefresh();
				}
			}, 250);
			return;
		}
		listView.setSelection(0);
		start = 0;
		new GetShareListTask(MainActivity.this, start, onNetRequestListener,
				false).exe();

		
		
		
	}

	public void startPopText(String text, long delayedTime) {
		if (pop_text == null) {
			return;
		}
		pop_text.setText(text);
		if (pop_text.getVisibility() == View.VISIBLE) {
			return;
		}
		pop_text.setVisibility(View.VISIBLE);
		Animation animation = new AlphaAnimation(0, 1);
		animation.setDuration(300);
		pop_text.startAnimation(animation);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				pop_text.setVisibility(View.GONE);
				Animation animation = new AlphaAnimation(1, 0);
				animation.setDuration(300);
				pop_text.startAnimation(animation);
				pop_text.setClickable(false);
			}
		}, delayedTime);
	}

	private void initSlidingMenu() {
		sm = getSlidingMenu();

		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new SlideMenuListFragment()).commit();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.7f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setBehindScrollScale(0.0f);
		localBroadCastManager = LocalBroadcastManager
				.getInstance(getApplicationContext());
		localBroadCastManager.registerReceiver(receiver, new IntentFilter(
				"SLIDEMENU.ITEM.CLICK"));
	}

	private long lastExitTime;

	private void exit() {
		long currTime = System.currentTimeMillis();
		long dTime = currTime - lastExitTime;
		lastExitTime = currTime;
		if (dTime < 2 * 1000) {
			localBroadCastManager.unregisterReceiver(receiver);
			finish();
		} else {
			Toast.makeText(this, R.string.exit_text, Toast.LENGTH_SHORT).show();
		}
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("SLIDEMENU.ITEM.CLICK")) {
				int type = intent.getExtras().getInt("type");
				toggle();
				if (1 == type) {
					Toast.makeText(getApplicationContext(), "登录功能暂未开放，尽请期待！",
							Toast.LENGTH_SHORT).show();
				}
				if (0 == type) {
					Toast.makeText(getApplicationContext(), "退出登录吗？",
							Toast.LENGTH_SHORT).show();
				}

			}
		}
	};

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int lastPostition = view.getLastVisiblePosition();
		int count = view.getCount();
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			if (lastPostition == count - 1) {
				if (isMoreAble) {
					getMoreData();
				}
			}
			ImageLoad.getInstence().setScrolling(false);
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			ImageLoad.getInstence().setScrolling(true);
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			break;
		}
	}

	private void getMoreData() {
		if (isMoreAble) {
			loading_state_bar.setVisibility(View.VISIBLE);
			more_bar.setVisibility(View.VISIBLE);
		} else {
			loading_state_bar.setVisibility(View.GONE);
			more_bar.setVisibility(View.GONE);
		}
		if (adapter != null) {
			// start = adapter.getCount();
			Log.e("" + start);
		}
		new GetShareListTask(MainActivity.this, end, onMoreNetRequestListener,
				true).exe();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	// 读取本地数据库
	private void obtainShareFromDB() {
		data = new MessageListDataStorge(getApplicationContext())
				.getMessageList();
		adapter.setShareList(data);
		adapter.notifyDataSetChanged();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	class UpdateTask extends AsyncTask {

		@Override
		protected Object doInBackground(Object... params) {
			info = UpdateInfoTool.readXMLFromUrl("");
			if (info != null) {
				UpdataOperation();
			}
			return null;
		}
	}

	private void UpdataOperation() {
		// 更新处理
		if (info.getVersion() > com.qtest.journal.update.GetMyVersion
				.getAppVersionName(this)) {
			Message msg = new Message();
			msg.what = UPDATE;
			h.sendMessage(msg);
		} else {
			// configutil.setUpdate(false);
		}
	}

	private final int UPDATE = 1;

	private Handler h = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UPDATE:
				alertdialog_update = new AlertDialog.Builder(MainActivity.this)
						.setTitle("更新")
						.setMessage(
								"有新的更新" + "\n" + "更新详情" + "\n"
										+ info.getDescription())
						.setPositiveButton("更新",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										AppTool.openUrl(MainActivity.this,
												info.getUpdateUrl());
									}
								})
						.setNegativeButton(
								getResources().getString(R.string.cancel),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										return;
									}
								}).create();

				if (alertdialog_update != null) {
					if (alertdialog_update.isShowing()) {
						return;
					} else {
						alertdialog_update.show();
					}
				} else {
					alertdialog_update.show();
				}
				break;

			default:
				break;
			}

		};

	};

}
