package com.mymy.randomweb;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class FontActivity extends ActionBarActivity {

	private static final String TAG = "FontActiity";
	SeekBar sBar1;
	TextView seekvalue1;
	Button btn_clearcache;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_font);
		
		seekvalue1 = (TextView)findViewById(R.id.tv_seekvalue1);
		sBar1 = (SeekBar)findViewById(R.id.seekbar1);
		sBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	        	seekvalue1.setText(" 현재 값 : " + progress +1);
	        }

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		btn_clearcache = (Button)findViewById(R.id.btn_delete_cache);
		btn_clearcache.setOnClickListener(onClickListener);
		
		
		
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
        	switch(v.getId()){
        	case R.id.btn_delete_cache:
            	//clearApplicationCache(null);
        		WebViewActivity.mWebView.clearCache(true);
        		break;
        	
        	case R.id.btn_clear_foam:
        		WebViewActivity.mWebView.clearFormData();
        		break;
        	}
        }
    };
    
	        
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.font, menu);
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
	
	
	
	private void clearApplicationCache(java.io.File dir){
        if(dir==null)
            dir = getCacheDir();
        else;
        if(dir==null)
            return;
        else;
        java.io.File[] children = dir.listFiles();
        try{
        	Log.v(TAG, "dir : " + dir.getName());
        	Log.v(TAG, "dir : " + children.length);
            for(int i=0;i<children.length;i++)
                if(children[i].isDirectory()){
                	Log.v(TAG, "디렉토리 : " + children[i].getName() + " +++ " + children[i].getPath());
                    clearApplicationCache(children[i]);
                }
                	
                else {
                	Log.v(TAG, "파일 : " + children[i].getName()  + " +++ " + children[i].getPath());
                	children[i].delete();
                }
        }
        catch(Exception e){}
    }

    
    
}
