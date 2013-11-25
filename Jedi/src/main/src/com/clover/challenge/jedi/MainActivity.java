package com.clover.challenge.jedi;

import java.nio.charset.Charset;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

// Home screen when user first opens app
public class MainActivity extends SuperActivity implements
		CreateNdefMessageCallback, OnNdefPushCompleteCallback {

	private NfcAdapter mNfcAdapter;
	private String ip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Check for available NFC Adapter
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}

		// Register callbacks
		mNfcAdapter.setNdefPushMessageCallback(this, this);
		mNfcAdapter.setOnNdefPushCompleteCallback(this, this);

		// Get ip address (async)
		getIpAddress();
	}

	@Override
	public void onResume() {
		super.onResume();
		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent().setAction(""));
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	// Creates a custom MIME type encapsulated in an NDEF record
	public NdefRecord createMime(String mimeType, byte[] payload) {
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
		NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				mimeBytes, new byte[0], payload);
		return mimeRecord;
	}

	// Returns message containing "server" IP address and wifi network name to
	// send to client on NFC
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		String text = getWifiName() + KEY + ip;
		NdefMessage msg = new NdefMessage(new NdefRecord[] { createMime(
				"text/plain", text.getBytes()) });

		return msg;
	}

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		// Launch server activity
		Intent i = new Intent(this, ServerActivity.class);
		startActivity(i);
	}

	// Server processes message received via NFC
	private void processIntent(Intent intent) {

		// Get beamed message
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		String s = new String(msg.getRecords()[0].getPayload());
		String[] data = s.split(KEY);

		// If phones are on the same network
		if (getWifiName().equals(data[0])) {
			// Launch client activity
			Intent i = new Intent(this, ClientActivity.class);
			i.putExtra(IP, data[1]);
			startActivity(i);

		} else { // Different networks -- not allowed!
			Toast.makeText(this, "Must be connected on same wifi network!",
					Toast.LENGTH_LONG).show();

		}
	}

	// Sets ip field and throws error if wifi is off or unavailable
	private void getIpAddress() {

		// Async to avoid potentially long operation on UI thread
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String ipString = null;

				ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				NetworkInfo mWifi = connManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				if (mWifi.isConnected()) {
					WifiManager wifiManager = (WifiManager) MainActivity.this
							.getSystemService(Context.WIFI_SERVICE);
					WifiInfo wifiInfo = wifiManager.getConnectionInfo();
					int ipNum = wifiInfo.getIpAddress();
					ipString = String.format(Locale.getDefault(),
							"%d.%d.%d.%d", (ipNum & 0xff), (ipNum >> 8 & 0xff),
							(ipNum >> 16 & 0xff), (ipNum >> 24 & 0xff));

				}

				return ipString;
			}

			@Override
			public void onPostExecute(String s) {

				// Error case
				if (s == null) {
					Toast.makeText(MainActivity.this, "Wifi Error",
							Toast.LENGTH_SHORT).show();
					finish();

				} else { // Set activity field
					ip = s;
				}
			}

		}.execute();

	}

}