package com.clover.challenge.jedi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SuperActivity extends Activity {

	public static final String IP = "IP_ADDR";
	public static final String SAFE_WORD = "CLOSE";
	public static final String PHONE = "PHONE";
	public static final String URL = "URL";
	public static final String ADDRESS = "ADDRESS";
	public static final String KEY = "_UNIQUE_KEY_";
	public static final int PORT = 8080;

	private static final int FONT_SIZE = 30;
	private static final int PAD = 10;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	public void help(MenuItem item) {
		new AlertDialog.Builder(this).setTitle("Help").setMessage(R.string.help).create()
				.show();
	}

	public String getWifiName() {
		WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

		String s = wifiInfo.getSSID();
		if (s.charAt(0) == '"')
			s = s.substring(1, s.length() - 1);

		return s;
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

	public static class mEditText extends EditText {

		public mEditText(Context context, AttributeSet attrs) {
			super(context, attrs);
			initUI(false, this);
		}
	}

	private static void initUI(boolean white, TextView v) {

		Typeface tf = Typeface.createFromAsset(v.getContext().getAssets(),
				"fonts/Roboto-Thin.ttf");
		v.setTypeface(tf);
		
		v.setTextSize(FONT_SIZE);
		v.setGravity(Gravity.CENTER);
		v.setPadding(PAD, PAD, PAD, PAD);

		v.setTextColor(white ? Color.WHITE : Color.BLACK);

	}

}
