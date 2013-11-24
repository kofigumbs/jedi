package com.clover.challenge.jedi;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;

public class ServerActivity extends SuperActivity implements
		DialogInterface.OnClickListener, OnClickListener {

	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);

		findViewById(R.id.cancel).setOnClickListener(this);

		i = new Intent(this, BackgroundService.class);
		startService(i);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (!isServiceRunning()) {
			finish();
			Toast.makeText(this, "Connection lost!", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onBackPressed() {

		new AlertDialog.Builder(this).setMessage(R.string.cancel_confirm)
				.setPositiveButton("Yes", this)
				.setNegativeButton("Cancel", null).create().show();

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		finish();
		stopService(i);
	}

	@Override
	public void onClick(View v) {
		finish();
		stopService(i);
	}

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