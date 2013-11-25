package com.clover.challenge.jedi;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Activity that allows users to send messages to server device
public class ClientActivity extends SuperActivity implements
		DialogInterface.OnClickListener, OnClickListener, TextWatcher {

	private static final String WR = "^(https?://)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([/\\w \\.-]*)*/?$";
	private static final String AR = "^(\\d+ )?[a-z]+( .*)?";
	private final Handler handler = new Handler();

	private boolean validCommand = false;
	private String serverIp;
	private String type;
	private EditText commandET;
	private Button send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);

		commandET = (EditText) findViewById(R.id.command);
		send = (Button) findViewById(R.id.send);

		commandET.addTextChangedListener(this);
		send.setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);

		serverIp = getIntent().getExtras().getString(IP);
		if (serverIp == null) {
			finish();
			Toast.makeText(this, "Connection error!\nTry re-pairing.",
					Toast.LENGTH_LONG).show();
		}
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
		new Thread(new ClientThread(SAFE_WORD)).start();
	}

	// Button listeners
	@Override
	public void onClick(View v) {

		// Cancel button pressed
		if (v.getId() == R.id.cancel) {
			onBackPressed();
		}

		else // Send button pressed
		if (validCommand) {
			String command = commandET.getText().toString().trim();
			Thread cThread = new Thread(new ClientThread(type + KEY + command));
			cThread.start(); // Send command

		} else {
			Toast.makeText(this, "Invalid command!", Toast.LENGTH_SHORT).show();
		}
	}

	// Sets validCommand and type fields dynamically as user types.
	// Also changes send button text
	@Override
	public void afterTextChanged(Editable e) {

		String s = e.toString().trim().toLowerCase(Locale.ENGLISH);

		if (PhoneNumberUtils.isGlobalPhoneNumber(s)) {
			validCommand = true;
			type = PHONE;

			send.setText("Send phone number");

		} else if (s.matches(WR)) {
			validCommand = true;
			type = URL;

			send.setText("Send as URL");

		} else if (s.matches(AR)) {
			validCommand = true;
			type = ADDRESS;

			send.setText("Send as address");
		}

		else {
			validCommand = false;
			type = null;

			send.setText(getString(R.string.send));
		}

	}

	// Unused method implementations
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	// Posts s as a Toast message and in Logcat
	private void handle(final String s) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(ClientActivity.this, s, Toast.LENGTH_SHORT)
						.show();
				Log.d("ClientActivity", s);
			}
		});
	}

	// Sends messages to server asynchronously via sockets.
	// Provides status updates via handle()
	private class ClientThread implements Runnable {
		private String command;

		public ClientThread(String s) {
			command = s;
		}

		public void run() {
			try {
				Log.d("ClientActivity", "Connecting...");

				InetAddress serverAddr = InetAddress.getByName(serverIp);
				Socket socket = new Socket(serverAddr, PORT);

				// Send command string to server device via socket
				handle("Sending...");
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				out.println(command);
				out.flush();
				handle("Sent!");

				socket.close();
				Log.d("ClientActivity", "Closed.");

			} catch (Exception e) {
				e.printStackTrace();
				handle("Error!");
			}
		}
	}
}