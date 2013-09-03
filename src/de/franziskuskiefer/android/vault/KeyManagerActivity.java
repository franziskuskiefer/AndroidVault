package de.franziskuskiefer.android.vault;

import java.util.Locale;

import net.sourceforge.zbar.Symbol;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

public class KeyManagerActivity extends FragmentActivity {

	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private static String key = "blablub";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_key_manager);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
//		launchQRScanner();
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
	
	private void onUnlock(String key){
		System.out.println("fuck: "+key);
		this.key = key;
		UnlockApp ua = new UnlockApp();
		ua.InitializeSQLCipher(this.getApplicationContext());
		
		setContentView(R.layout.activity_key_manager);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.key_manager, menu);
		return true;
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new TestFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			
			return fragment;
		}
		
		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return getString(R.string.title_section1).toUpperCase(l);
				case 1:
					return getString(R.string.title_section2).toUpperCase(l);
				case 2:
					return getString(R.string.title_section3).toUpperCase(l);
				case 3:
					return "SSH";
			}
			return null;
		}
	}
	
	public static class TestFragment extends Fragment {
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	    	View view = inflater.inflate(R.layout.test_fragment, container, false);
	    	((TextView)view.findViewById(R.id.key_string)).setText(key);
	        return view;
	    }
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_key_manager_dummy, container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(INPUT_SERVICE, "Finished QR-Code scanning...");
		switch (requestCode) {
			case ZBAR_QR_SCANNER_REQUEST:
				if (resultCode == RESULT_OK) {
					String scannedString = "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT);
					onUnlock(scannedString);
//					Toast.makeText(this, scannedString , Toast.LENGTH_SHORT).show();
				} else if(resultCode == RESULT_CANCELED && data != null) {
					String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
					if(!TextUtils.isEmpty(error)) {
						Log.d(INPUT_SERVICE, "Scanning Error:\n"+error);
//						Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
					}
				}
				break;
		}
	}

	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

}
