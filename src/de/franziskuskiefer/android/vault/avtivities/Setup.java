package de.franziskuskiefer.android.vault.avtivities;

import net.sourceforge.zbar.Symbol;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import de.franziskuskiefer.android.vault.R;
import de.franziskuskiefer.android.vault.controller.BasicActivity;
import de.franziskuskiefer.android.vault.controller.DatabaseController;

public class Setup extends BasicActivity {

	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);

		final Button button = (Button) findViewById(R.id.BtnSetupOk);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				launchQRScanner();
			}
		});
		
		final Button cancel = (Button) findViewById(R.id.BtnSetupCancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
			    intent.addCategory(Intent.CATEGORY_HOME);
			    startActivity(intent);
			}
		});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.setup_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) {
			case R.id.menuReset:
				DatabaseController.resetDB(getApplicationContext());
				break;
	
			default:
				break;
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(INPUT_SERVICE, "Finished QR-Code scanning...");
		switch (requestCode) {
			case ZBAR_QR_SCANNER_REQUEST:
				if (resultCode == RESULT_OK) {
					onUnlock(data.getStringExtra(ZBarConstants.SCAN_RESULT));
				} else if(resultCode == RESULT_CANCELED && data != null) {
					String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
					if(!TextUtils.isEmpty(error)) {
						Log.d(INPUT_SERVICE, "Scanning Error:\n"+error);
						Toast.makeText(this, "Sorry, something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
					}
				}
				break;
		}
	}
	
	private void onUnlock(String key){
		Intent myIntent = new Intent(getBaseContext(), Vault.class);
		myIntent.putExtra("key", key);
		startActivity(myIntent);
	}
	
	public void launchQRScanner() {
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
			startActivityForResult(intent, ZBAR_QR_SCANNER_REQUEST);
			Log.d(INPUT_SERVICE, "Started QR-Code scanning...");
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
			Log.e(INPUT_METHOD_SERVICE, "Rear Facing Camera Unavailable");
		}
	}

	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}
}
