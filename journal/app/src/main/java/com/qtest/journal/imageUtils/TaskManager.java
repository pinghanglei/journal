package com.qtest.journal.imageUtils;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.qtest.journal.httpUtils.HttpUtil;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;


public class TaskManager {
	private static TaskManager taskManager = new TaskManager();
	private TaskManager() {} 
	//单例模式返回同一个对象
	public static TaskManager getInstence(){
		return taskManager;
	}

	private ExecutorService downLoadService = Executors.newFixedThreadPool(5);
	
	@SuppressLint("UseSparseArrays")
	private final HashMap<Integer,Task> tasks = new HashMap<Integer,Task>();
	
	class Task implements Runnable{
		String url;
		int viewID;
		Handler handler;
		int type;
		
		public Task(String url, int viewID, Handler handler, int type) {
			super();
			this.url = url;
			this.viewID = viewID;
			this.handler = handler;
			this.type = type;
		}

		@Override
		public void run() {
			removeTask(viewID);
			//TODO load image
			Bitmap bitmap = getBitmapFromNet(url, type);
			handler.sendMessage(handler.obtainMessage(1, bitmap));
		}
		
	}
	
	/**
	 * 从网络加载图片
	 * @param viewID
	 * @param url
	 * @param handler
	 * @param type
	 */
	public void loadImage(int viewID, String url, Handler handler, int type){
		if(containsTask(viewID)){
			if(updateTask(viewID, url, handler, type)){
				return;
			}
		}
		
		Task task = new Task(url, viewID, handler, type);
		addTask(viewID,task);
		downLoadService.execute(task);
	}
	
	/**
	 * 添加task的引用到队列中
	 * @param task
	 */
	private void addTask(int ViewID,Task task){
		synchronized (tasks) {
			tasks.put(ViewID, task);
		}
	}
	
	/**
	 * 从队列中移除task的引用
	 * @param task
	 */
	private void removeTask(int viewID){
		synchronized (tasks) {
			if(tasks.containsKey(viewID)){
				tasks.remove(viewID);
			}
		}
	}
	
	/**
	 * 队列中是否有 对应ID的task 如果有则重用task 
	 * @param viewID
	 * @return
	 */
	private boolean containsTask(int viewID){
		synchronized (tasks) {
			return tasks.containsKey(viewID);
		}
	}
	
	private boolean updateTask(int viewID, String url, Handler handler, int type){
		synchronized (tasks) {
			if(tasks.containsKey(viewID)){
				Task task = tasks.get(viewID);
				if(task!=null){
					task.viewID = viewID;
					task.url = url;
					task.handler = handler;
					task.type = type;
					return true;
				}
			}
		}
		return false;
	}
	
	/**
     * 从网络获取图片
     * @param imageUrl
     * @param type
     * @return Bitmap
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private Bitmap getBitmapFromNet(String imageUrl, int type){
        try{
			byte[] data = HttpUtil.getImage(imageUrl);
			if(data==null){
				return null;
			}
			BitmapFactory.Options opts = OptionsManager.getBitmapOptions(data, type);
			Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            if(b!=null){
            	ImageToSdcard.saveFile(imageUrl, data);
            	data = null;
            	return b;
            }
            data = null;
        } catch (Exception e) {
        	ImageLoad.getInstence().clearCache();
        	e.printStackTrace();
		}
        return null;
    }
}
