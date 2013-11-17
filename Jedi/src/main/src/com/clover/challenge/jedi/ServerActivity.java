package com.clover.challenge.jedi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import com.clover.challenge.jedi.ClientActivity.ClientThread;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ServerActivity extends SuperActivity {

	// default ip
	public static String SERVERIP;
	private Handler handler = new Handler();
	private ServerSocket serverSocket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);

		SERVERIP = getIntent().getExtras().getString(IP);

		Thread fst = new Thread(new ServerThread());
		fst.start();
	}

	public class ServerThread implements Runnable {

		public void run() {
			try {
				if (SERVERIP != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ServerActivity.this,
									"Listening on IP: " + SERVERIP,
									Toast.LENGTH_SHORT).show();
						}
					});
					serverSocket = new ServerSocket(PORT);
					while (true) {
						// listen for incoming clients
						Socket client = serverSocket.accept();
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(ServerActivity.this,
										"Connected" + SERVERIP,
										Toast.LENGTH_SHORT).show();
							}
						});

						try {
							BufferedReader in = new BufferedReader(
									new InputStreamReader(
											client.getInputStream()));
							String line = null;
							while ((line = in.readLine()) != null) {
								Log.d("ServerActivity", line);

								if (line == "youtube")
									break;
								// handler.post(new Runnable() {
								// @Override
								// public void run() {
								// // do whatever you want to the front
								// // end
								// // this is where you can be creative
								// Toast.makeText(
								// ServerActivity.this,
								// "Launch YouTube" + SERVERIP,
								// Toast.LENGTH_SHORT).show();
								//
								// }
								// });
							}
							break;
						} catch (Exception e) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(
											ServerActivity.this,
											"Oops. Connection interrupted. Please reconnect your phones."
													+ SERVERIP,
											Toast.LENGTH_SHORT).show();
									;
								}
							});
							e.printStackTrace();
						}
					}
				} else {
					handler.post(new Runnable() {
						@Override
						public void run() {

							Toast.makeText(
									ServerActivity.this,
									"Couldn't detect internet connection."
											+ SERVERIP, Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
			} catch (Exception e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ServerActivity.this, "Error" + SERVERIP,
								Toast.LENGTH_SHORT).show();
					}
				});
				e.printStackTrace();
			}
			// Start connection
			closeSocket();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		closeSocket();
	}

	private void closeSocket() {
		try {
			// make sure you close the socket upon exiting
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}