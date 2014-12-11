package com.mymy.randomweb;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SiteEditActivity extends ActionBarActivity {

	private static final String TAG = "SiteRegister";
	
	private String title;
	private String curUrl;
	private EditText et_title;
	private EditText et_url;
	
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site_register);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 홈 액티비티로 가는 홈 버튼 활성화
		
		intent = getIntent();
		title = intent.getStringExtra("title");
		curUrl = intent.getStringExtra("curUrl"); //without "\n"
		
    	et_title = (EditText)findViewById(R.id.edit_site_names);
    	et_url = (EditText)findViewById(R.id.edit_url);
    	
    	et_title.setText(title);
    	et_url.setText(curUrl); 
    	
    	Button btn_submit1 = (Button) findViewById(R.id.btn_submit);
    	btn_submit1.setText("수정");
    	Button btn_cancel1 = (Button) findViewById(R.id.btn_cancel);
    

    	//mEditText.setOnKeyListener(onKeyListener);
    	btn_submit1.setOnClickListener(onClickListener1);
    	btn_cancel1.setOnClickListener(onClickListener1);
 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.site_register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//int id = item.getItemId();
		//if (id == R.id.action_settings) {
		//	return true;
		//}
		return super.onOptionsItemSelected(item);
	}
	
	View.OnClickListener onClickListener1 = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
            case R.id.btn_submit: 	// 수정
            	
        		String stitle = et_title.getText().toString();
        		String surl = et_url.getText().toString();

            	intent.putExtra("retTitle", stitle);
            	intent.putExtra("retUrl", surl); //without "\n"
            	setResult(RESULT_OK,intent); 
            	finish();
            	
            case R.id.btn_cancel:
            	setResult(RESULT_CANCELED,intent); 
            	finish();
            }
        }

    };
	
	
    
}
