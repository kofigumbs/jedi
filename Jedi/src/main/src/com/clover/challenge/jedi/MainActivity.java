package com.clover.challenge.jedi;

import java.nio.charset.Charset;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

public class MainActivity extends SuperActivity implements
		CreateNdefMessageCallback, OnNdefPushCompleteCallback {

	private NfcAdapter mNfcAdapter;
	private boolean isServer = false;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // Register callbacks
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
    }

	@Override
	public void onResume() {
		super.onResume();
		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
			isServer = false;
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	/**
	 * Parses the NDEF Message from the intent and prints to the TextView
	 */
	void processIntent(Intent intent) {

		// get beamed message
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		String s = new String(msg.getRecords()[0].getPayload());

		// launch client activity
		Intent i = new Intent(this, ClientActivity.class);
		i.putExtra(IP, s);
		startActivity(i);
	}

	/**
	 * Creates a custom MIME type encapsulated in an NDEF record
	 * 
	 * @param mimeType
	 */
	public NdefRecord createMime(String mimeType, byte[] payload) {
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
		NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				mimeBytes, new byte[0], payload);
		return mimeRecord;
	}

	/**
	 * Returns "server" IP address to send to client on NFC
	 */
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		String text = getLocalIpAddress();
		NdefMessage msg = new NdefMessage(new NdefRecord[] { createMime(
				"text/plain", text.getBytes()) });

		return msg;
	}

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		// TODO Auto-generated method stub
		// launch client activity
		Intent i = new Intent(this, ServerActivity.class);
		i.putExtra(IP, getLocalIpAddress());
		startActivity(i);
	}

}