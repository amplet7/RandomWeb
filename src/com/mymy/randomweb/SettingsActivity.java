package com.mymy.randomweb;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity   {

	Preference clearCache;
	Preference clearCookie;
	Preference clearForm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.activity_settings);
		
		clearCache = (Preference) findPreference("clear_cache");
		clearCookie = (Preference) findPreference("clear_cookie");
		clearForm = (Preference) findPreference("clear_foam");
		
		clearCache.setOnPreferenceClickListener(onPreferenceClickListener);
		clearCookie.setOnPreferenceClickListener(onPreferenceClickListener);
		clearForm.setOnPreferenceClickListener(onPreferenceClickListener);
	}
	
	Preference.OnPreferenceClickListener onPreferenceClickListener = 
			new Preference.OnPreferenceClickListener(){
		@Override
		public boolean onPreferenceClick (Preference preference)
		{
		    if (preference.getKey().equals("clear_cache")){
		    	WebViewActivity.mWebView.clearCache(true);
		    	Toast.makeText(SettingsActivity.this, "캐쉬가 삭제되었습니다", 
						Toast.LENGTH_SHORT).show();
		    }else if(preference.getKey().equals("clear_cookie")){
		    	Toast.makeText(SettingsActivity.this, "쿠키가 삭제되었습니다", 
						Toast.LENGTH_SHORT).show();
		    }else if(preference.getKey().equals("clear_foam")){
		    	WebViewActivity.mWebView.clearFormData();
		    	Toast.makeText(SettingsActivity.this, "저장된 양식이 삭제되었습니다", 
						Toast.LENGTH_SHORT).show();
		    }
		    
		   
    	
    	
    		
		    return false;
		}
	
	};

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
