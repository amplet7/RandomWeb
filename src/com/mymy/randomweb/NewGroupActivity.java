package com.mymy.randomweb;

import java.io.File;
import java.io.FileOutputStream;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewGroupActivity extends ActionBarActivity {

	protected static final String TAG = "New Group";

	EditText edit_group;
	Intent intent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
		
		intent = getIntent();
		
		edit_group =(EditText) findViewById(R.id.edit_group_name1);
		
		findViewById(R.id.btn_submit).setOnClickListener(onClickListener1);
        findViewById(R.id.btn_cancel).setOnClickListener(onClickListener1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_group, menu);
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
	
	
		
	View.OnClickListener onClickListener1 = new View.OnClickListener() {
	        
	        @Override
	        public void onClick(View v) {
	            switch(v.getId()) {
	            case R.id.btn_submit: 	// 그룹 추가
	            	String groupName = edit_group.getText().toString();
	            	String dirPath = getFilesDir().getAbsolutePath();
	            	Log.v(TAG, "dirPath : " + dirPath);
	            	File file = new File(dirPath + "/" + groupName
	            					+ ".txt");
	            	Log.v(TAG, "dirPath, groupName : " + dirPath + " " + groupName);
	            	// 시작 시 어플폴더에 파일이 없으면 기본그룹 파일 생성 후 한줄 기록
	            	try{
	            		if( !file.exists() ) {
	            			Log.v(TAG, "im in !file.exists()");
	            			FileOutputStream fos1 = openFileOutput("group_" + groupName
	            					+ ".txt", Context.MODE_PRIVATE);
	            			String firstline = "URLs\n";
	            			fos1.write(firstline.getBytes());
	            			fos1.close();
	            			intent.putExtra("newgroup", groupName);
	            			setResult(RESULT_OK,intent); 
	                    	finish();
	                    	
	            		}else{
	            			Toast.makeText(NewGroupActivity.this, "이미 있는 그룹입니다.", 
	                				Toast.LENGTH_SHORT).show();
	            		}
	            	}catch(Exception e){
	            		System.err.println(e);
	            		System.exit(1);
	            	}
	            	
	            	
	            	
	            case R.id.btn_cancel:
	            	Intent intent = null;
					setResult(RESULT_CANCELED, intent); 
	            	finish();
	            }
	        }
	
	    };
    
    
    
	
	
	
	
	
}
