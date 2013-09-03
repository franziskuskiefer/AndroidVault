package de.franziskuskiefer.android.vault.view;

import java.util.Vector;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import de.franziskuskiefer.android.vault.R;
import de.franziskuskiefer.android.vault.controller.DatabaseController;

/**
 * The main applicatoin.
 * 
 * @author Franziskus Kiefer
 *
 */
public class Vault extends FragmentActivity implements NoticeDialogListener {

	private DatabaseController dbController;
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * load DB first
		 */
		String key = getIntent().getStringExtra("key");
		try{
			openDB(key);

			setContentView(R.layout.activity_vault);
			
			// Create the adapter that will return a fragment for each of the three
			// primary sections of the app.
			mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
		} catch (Exception e){
			Log.d("Vault", "Someone tried to login with a wrong key ... Show error and quit!");
			AlertDialog.Builder builder = new AlertDialog.Builder( this );
			builder.setMessage("Sorry, I don't know that key :(")
			.setCancelable(false)
			.setNeutralButton("Ok.",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
			});
			AlertDialog error = builder.create();
			error.show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.vault_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) {
			case R.id.menuNewEntry:
				new EntryClassChooser().show(getFragmentManager(), "CategoryChooser");
				break;
	
			default:
				break;
		}
		
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private Vector<String> categories = new Vector<String>();
		private TableFragment fragment;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			
			readCategories();
		}
		
		private void readCategories(){
			categories = dbController.getTableNames();
		}
		
		@Override
		public Fragment getItem(int position) {
			if (categories.size() == 0){
				Fragment fragment = new EmptyFragment();
				return fragment;
			} else {
				Log.d("Vault", "SectionsPagerAdapter getItem Position: "+position);
				fragment = new TableFragment();
				
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				args.putString("category", categories.get(position));
				fragment.setArguments(args);
				
				return fragment;
			}
		}

		@Override
		public int getCount() {
			int size = categories.size();
			if (size > 0)
				return size;
			else
				return 1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (categories.size() > 0)
				return categories.get(position);
			else
				return "Empty Database";
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			
			fragment.notifyDataSetChanged();
			
			Log.d("Vault", "notifyDataSetChanged(SectionsPagerAdapter)");
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		String dialogTag = dialog.getTag();
		
		if (dialogTag.equals("CategoryChooser")){ // Category Chooser Dialog
			int selected = ((EntryClassChooser)dialog).getSelected();

			// FIXME: make nicer ...
			switch (selected) {
				case 0: // password
					new NewPasswordEntry().show(getFragmentManager(), "NewPwdEntry");
					break;
				case 1: // PGP
					Toast.makeText(getApplicationContext(), "Sorry, not implemented yet!", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		} else if (dialogTag.equals("NewPwdEntry")) {
			String pwd = ((NewPasswordEntry)dialog).getPwd();
			String user = ((NewPasswordEntry)dialog).getUsername();
			Log.d("Vault", "pwd: "+pwd);
			Log.d("Vault", "username: "+user);
			
			// store passwords
			dbController.addPasswordEntry(pwd, user); 
			
			// XXX: refresh view with new content (actually, we get a new cursor)
			mSectionsPagerAdapter.notifyDataSetChanged();
			
		} else {
			Toast.makeText(getApplicationContext(), "Sorry, not implemented yet!", Toast.LENGTH_SHORT).show();
			// nothing else implemented yet ...
		}
	}
	
	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		
		Log.d("Vault", "onResumeFragments");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Log.d("Vault", "onResume");
	}
	
	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// Nothing to do here yet ...
	}
	
	/**
	 * Open Database
	 * @param key
	 */
	private void openDB(String key){
		dbController = new DatabaseController(getApplicationContext(), key);
	}

	public DatabaseController getDBController() {
		return this.dbController;
	}

}
