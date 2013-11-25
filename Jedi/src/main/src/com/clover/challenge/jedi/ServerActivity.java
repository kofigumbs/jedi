package com.clover.challenge.jedi;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;

// Activity that users sees if app is open and device is listening for commands
public class ServerActivity extends SuperActivity implements
		DialogInterface.OnClickListener, OnClickListener {

	private static final int ANIM_FREQ = 1000;
	private int ellipses = 1;
	private Runnable anim;
	private Intent i;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);

		tv = (TextView) findViewById(R.id.waiting);
		findViewById(R.id.cancel).setOnClickListener(this);

		// Begin listening service
		i = new Intent(this, BackgroundService.class);
		startService(i);

		// Define ellipses animation
		anim = new Runnable() {
			@Override
			public void run() {

				String s = getString(R.string.waiting);
				for (int i = 0; i < ellipses % 4; i++)
					s = s + ".";

				ellipses++;
				tv.setText(s);
				tv.postDelayed(this, ANIM_FREQ);
			}

		};
	}

	@Override
	public void onResume() {
		super.onResume();

		// If connection lost, finish activity
		if (!isServiceRunning()) {
			finish();
			Toast.makeText(this, "Connection lost!", Toast.LENGTH_SHORT).show();
			return;
		}

		// Set ellipses animation
		tv.postDelayed(anim, ANIM_FREQ);
	}

	@Override
	public void onPause() {
		super.onPause();
		// Remove ellipses animation
		tv.removeCallbacks(anim);
	}

	@Override
	public void onBackPressed() {

		// Confirmation dialogue
		new AlertDialog.Builder(this).setMessage(R.string.cancel_confirm)
				.setPositiveButton("Yes", this)
				.setNegativeButton("Cancel", null).create().show();

	}

	// Confirmation dialogue "Yes" listener
	@Override
	public void onClick(DialogInterface dialog, int which) {
		finish();
		stopService(i);
	}

	// Cancel button listener
	@Override
	public void onClick(View v) {
		finish();
		stopService(i);
	}

	// Iterates through all services and checks if the listener is still active
	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (BackgroundService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}