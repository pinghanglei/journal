package com.qtest.journal.update;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateInfoTool {

	// 更新信息存储类
	public static class UpdateInfo {
		private int mVersion;
		private String mUpdateUrl;
		private String mDescription;
		private boolean mForced;
		private String mMD5;

		public int getVersion() {
			return mVersion;
		}

		public void setVersion(int mVersion) {
			this.mVersion = mVersion;
		}

		public String getUpdateUrl() {
			return mUpdateUrl;
		}

		public void setUpdateUrl(String mUpdateUrl) {
			this.mUpdateUrl = mUpdateUrl;
		}

		public String getDescription() {
			return mDescription;
		}

		public void setDescription(String mDescription) {
			this.mDescription = mDescription;
		}

		public boolean isForced() {
			return mForced;
		}

		public void setForced(boolean mForced) {
			this.mForced = mForced;
		}

		public String getMD5() {
			return mMD5;
		}

		public void setMD5(String mMD5) {
			this.mMD5 = mMD5;
		}


	}

	// 解析xml文件信息
	public static UpdateInfo praseXML(InputStream inStream) {
		UpdateInfo resInfo = new UpdateInfo();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);

			Element root = dom.getDocumentElement();
			NodeList items = root.getChildNodes();

			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i);
				String tagName = item.getNodeName();

				if (item.getNodeType() == Document.ELEMENT_NODE) {
					String value = item.getTextContent();
					if (tagName.equals("mVersion")) {
						resInfo.setVersion(Integer.parseInt(value));
					} else if (tagName.equals("mUpdateUrl")) {
						resInfo.setUpdateUrl(value);
					} else if (tagName.equals("mDescription")) {
						resInfo.setDescription(value);
					} else if (tagName.equals("mForced")) {
						resInfo.setForced(Boolean.parseBoolean(value));
					} else if (tagName.equals("mMD5")) {
						resInfo.setMD5(value);
					}
				}
			}
			// inStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resInfo;
	}

	/**
	 * 从远程下载一个文件
	 * 
	 * @param Url
	 * @return
	 */
	public static UpdateInfo readXMLFromUrl(String xmlUrl) {
		URL url = null;
		InputStream inputStream = null;
		HttpURLConnection conn;

		UpdateInfo resInfo = null;
		try {
			url = new URL(xmlUrl);
			conn = (HttpURLConnection) url.openConnection();

			conn.setDoInput(true);

			conn.setConnectTimeout(7000);

			conn.connect();

			if (conn.getResponseCode() != 200) {
				return null;
			}

			inputStream = conn.getInputStream();

			resInfo = praseXML(inputStream);
			inputStream.close();
		} catch (IOException e) {
			return null;
		}

		return resInfo;
	}
}
