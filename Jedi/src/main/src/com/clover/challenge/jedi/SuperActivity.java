package com.clover.challenge.jedi;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SuperActivity extends Activity {

	public final String IP = "IP_ADDR";
	public final int PORT = 8080;

	private static final int FONT_SIZE = 30;
	private static final int PAD = 10;

	public String getLocalIpAddress() {
		// try {
		// for (Enumeration<NetworkInterface> en = NetworkInterface
		// .getNetworkInterfaces(); en.hasMoreElements();) {
		// NetworkInterface intf = en.nextElement();
		// for (Enumeration<InetAddress> enumIpAddr = intf
		// .getInetAddresses(); enumIpAddr.hasMoreElements();) {
		// InetAddress inetAddress = enumIpAddr.nextElement();
		// if (!inetAddress.isLoopbackAddress()) {
		// return inetAddress.getHostAddress().toString();
		// }
		// }
		// }
		// } catch (SocketException ex) {
		// ex.printStackTrace();
		// }
		// return null;

		// WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
		// WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		// int ip = wifiInfo.getIpAddress();
		// String ipAddress = Formatter.formatIpAddress(ip);
		// return ipAddress;

		return "10.0.0.6";
	}

	public static class mTextView extends TextView {

		public mTextView(Context context, AttributeSet attrs) {
			super(context, attrs);
			initUI(false, this);
		}
	}

	public static class mButton extends Button {

		public mButton(Context context, AttributeSet attrs) {
			super(context, attrs);
			initUI(true, this);
		}

	}

	private static void initUI(boolean button, View v) {

		Typeface tf = Typeface.createFromAsset(v.getContext().getAssets(),
				"fonts/Roboto-Thin.ttf");
		((TextView) v).setTypeface(tf);

		((TextView) v).setTextSize(FONT_SIZE);
		((TextView) v).setGravity(Gravity.CENTER);
		((TextView) v).setPadding(PAD, PAD, PAD, PAD);

		if (button)
			((Button) v).setTextColor(Color.WHITE);

		else
			((TextView) v).setTextColor(Color.BLACK);

	}

}
