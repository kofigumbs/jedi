package com.clover.challenge.jedi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ClientActivity extends SuperActivity {

	private String serverIpAddress;
	Thread cThread;
	private String command = "";
	private boolean connected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		serverIpAddress = getIntent().getExtras().getString(IP);

	}

	@Override
	public void onStop() {
		connected = false;
		super.onStop();
	}

	// BUTTON LISTENERs
	public void youtube(View v) {

		// Start connection
		if (!connected && !serverIpAddress.equals("")) {
			command = "youtube";
			cThread = new Thread(new ClientThread());
			cThread.start();
		}
	}

	@Override
	public void onBackPressed() {
		connected = false;
		super.onBackPressed();
	}

	public class ClientThread implements Runnable {

		public void run() {
			try {
				InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
				Log.d("ClientActivity", "C: Connecting...");
				Socket socket = new Socket(serverAddr, PORT);
				connected = true;
				while (connected) {
					try {
						Log.d("ClientActivity", "C: Sending command.");
						PrintWriter out = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(
										socket.getOutputStream())), true);
						// where you issue the commands
						out.println(command);
						Log.d("ClientActivity", "C: Sent. " + command);
					} catch (Exception e) {
						Log.e("ClientActivity", "S: Error", e);
					}
				}
				socket.close();
				Log.d("ClientActivity", "C: Closed.");
			} catch (Exception e) {
				Log.e("ClientActivity", "C: Error", e);
				connected = false;
			}
		}
	}
}