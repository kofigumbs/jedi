package com.clover.challenge.jedi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class BackgroundService extends IntentService {

	private Handler handler = new Handler();
	private Socket client;
	private ServerSocket server;

	// private Thread fst;

	public BackgroundService() {
		super("BackgroundService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		try {
			server = new ServerSocket(SuperActivity.PORT);

			while (!listen().equals(SuperActivity.SAFE_WORD))
				;

			server.close();
		} catch (Exception e) {

			e.printStackTrace();
			handle("Error");

		}
	}

	private String listen() {
		String line = SuperActivity.SAFE_WORD;

		try {
			client = server.accept();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			line = in.readLine();
			Log.d("ServerActivity", line);

			if (!line.equals(SuperActivity.SAFE_WORD))
				handle(line);

			client.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return line;

	}

	public void onDestroy() {
		super.onDestroy();

		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handle(final String s) {
		handler.post(new Runnable() {
			@Override
			public void run() {

				String[] data = s.split(SuperActivity.KEY);
				Intent i = null;

				if (data[0].equals(SuperActivity.PHONE)) {
					i = new Intent(Intent.ACTION_DIAL);
					i.setData(Uri.parse("tel:" + data[1]));

				} else if (data[0].equals(SuperActivity.URL)) {
					if (!data[1].startsWith("http"))
							data[1] = "http://".concat(data[1]);
					i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(data[1]));

				} else if (data[0].equals(SuperActivity.ADDRESS)) {
					String map = "http://maps.google.co.in/maps?q=" + data[1];
					i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(map));
				}

				if (i != null)
					startActivity(i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

				// Toast.makeText(BackgroundService.this, s, Toast.LENGTH_SHORT)
				// .show();
			}
		});
	}

}
