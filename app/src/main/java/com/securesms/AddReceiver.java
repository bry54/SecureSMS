package com.securesms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.securesms.items.ReceiverItem;

public class AddReceiver extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_receiver);
	}

	public void add_code(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		// Get the layout inflater
		LayoutInflater inflater = getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.dialog_add_receiver, null))
				// Add action buttons
				.setPositiveButton(R.string.add,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Dialog dialogView = (Dialog) dialog;
								EditText et_nick = (EditText) dialogView.findViewById(R.id.dialog_nick);
								EditText et_number = (EditText) dialogView.findViewById(R.id.dialog_number);
								EditText et_code = (EditText) dialogView.findViewById(R.id.dialog_code);
								DbAdapter dbHelper = new DbAdapter(
										getApplicationContext());
								dbHelper.open();
								ReceiverItem receiver = new ReceiverItem();
								receiver.name = et_nick.getText().toString();
								receiver.number = Integer.parseInt(et_number
										.getText().toString());
								receiver.code = et_code.getText().toString();
								dbHelper.createRowReceiver(receiver);
								dbHelper.close();
								Toast.makeText(getApplicationContext(),
										"Dodano kontakt!", Toast.LENGTH_SHORT)
										.show();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.show();
	}

	public void add_nfc(View v) {
		Toast.makeText(getApplicationContext(), "add nfc", Toast.LENGTH_LONG)
				.show();
	}

	public void add_bluetooth(View v) {
		Toast.makeText(getApplicationContext(), "add bluetooth",
				Toast.LENGTH_LONG).show();
	}
}
