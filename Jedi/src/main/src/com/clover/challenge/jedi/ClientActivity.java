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

public class ClientActivity extends SuperActivity implements
		DialogInterface.OnClickListener, OnClickListener, TextWatcher {

	private static String wr = "^(https?://)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([/\\w \\.-]*)*/?$";
	private static String ar = "^(\\d+ )?[a-z]+( .*)?";
	private String serverIpAddress;
	private String type;
	private boolean validCommand = false;
	private Handler handler = new Handler();
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

		serverIpAddress = getIntent().getExtras().getString(IP);

		// VERIFY IP
		if (serverIpAddress == null) {
			finish();
			Toast.makeText(this, "Connection error!\nTry re-pairing.",
					Toast.LENGTH_LONG).show();
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
		new Thread(new ClientThread(SAFE_WORD)).start();
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.cancel) {
			new Thread(new ClientThread(SAFE_WORD)).start();
			finish();
		}

		else if (validCommand) {
			String command = commandET.getText().toString().trim();
			Thread cThread = new Thread(new ClientThread(type + KEY + command));
			cThread.start();

		} else {
			Toast.makeText(this, "Invalid command!", Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	public void afterTextChanged(Editable e) {

		String s = e.toString().trim().toLowerCase(Locale.ENGLISH);

		if (PhoneNumberUtils.isGlobalPhoneNumber(s)) {
			validCommand = true;
			type = PHONE;

			send.setText("Send phone number");

		} else if (s.matches(wr)) {
			validCommand = true;
			type = URL;

			send.setText("Send as url");

		} else if (s.matches(ar)) {
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

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

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

	private class ClientThread implements Runnable {
		private String command;

		public ClientThread(String s) {
			command = s;
		}

		public void run() {
			try {
				InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
				Log.d("ClientActivity", "Connecting");
				Socket socket = new Socket(serverAddr, PORT);
				try {
					handle("Sending...");
					PrintWriter out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream())),
							true);

					out.println(command);
					out.flush();
					handle("Sent!");
				} catch (Exception e) {
					handle("Error!");
				}

				socket.close();

				Log.d("ClientActivity", "Closed.");
			} catch (Exception e) {
				handle("Error!");
			}
		}
	}
}