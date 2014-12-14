package com.mymy.randomweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class SiteRegisterActivity extends ActionBarActivity {

	private static final String TAG = "SiteRegister";
	
	private String title = WebViewActivity.title;
	private String curUrl = WebViewActivity.curUrl;
	private EditText siteName;
	private EditText siteUrl;
	
	ArrayList<String> Groups;
	private int groupPosition = 0;
	private String curFileName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site_register);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 홈 액티비티로 가는 홈 버튼 활성화
		
    	siteName = (EditText)findViewById(R.id.edit_site_names);
    	siteUrl = (EditText)findViewById(R.id.edit_url);
    	
    	siteName.setText(title);
    	siteUrl.setText(curUrl); 
    	
    	//mEditText.setOnKeyListener(onKeyListener);
        findViewById(R.id.btn_submit).setOnClickListener(onClickListener1);
        findViewById(R.id.btn_cancel).setOnClickListener(onClickListener1);
        
        Groups = new ArrayList<String>();
        Spinner spin2 = (Spinner)findViewById(R.id.spinner2);
        spin2.setPrompt("그룹을 선택하세요");
        ContextWrapper cw = new ContextWrapper(this);
        File dir = cw.getFilesDir();
        
        final String[] files= dir.list(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				return filename.startsWith("group_");
			}
        });
        
        for(int i=0; i < files.length; i++){
        	files[i].subfiles[i].lastIndexOf(".txt");
        	Groups.add(files[i].replaceFirst("group_", ""));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, Groups);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setSelection(0);
        spin2.setAdapter(adapter);
        
        spin2.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				groupPosition = position;
				curFileName = files[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
        	
        });
    	
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
            case R.id.btn_submit: 	// 즐겨찾기 등록
            	try{
            		String sname = siteName.getText().toString();
            		String surl = siteUrl.getText().toString();
            		surl =  surl + "\n";
            		String saveLine = sname + " .1.1. .1.1. " + surl;
            		Log.v(TAG, "curFileName : " + curFileName);
            		FileInputStream fis = openFileInput(curFileName);
                	Log.v(TAG, "passed FileInputStream fis = openFileInput()");
                	byte[] data = new byte[fis.available()];
                	Log.v(TAG, "passed new byte[fis.available()]");
                	while (fis.read(data) != -1){;} //빈파일은 여기서 무한루프돔.
                	
                	String URLs = new String(data);
                	Log.v(TAG, "URLs : " + URLs);
                	fis.close();
                	
                	

                	
                	if(URLs.indexOf(surl) == -1){ // 파일 안에 현재 url이 없을 때만 저장
                		// surl의 줄바꿈 문자 때문에 부분주소만 겹쳐도 등록된 걸로 판별하는 오류 제거됨
                		FileOutputStream fos = openFileOutput(curFileName,Context.MODE_APPEND);
                		fos.write(saveLine.getBytes());
                		Log.v(TAG, "passed fos.write(surl.getBytes());");
                		fos.close();
                		Toast.makeText(SiteRegisterActivity.this, "추가완료", 
                				Toast.LENGTH_SHORT).show();
                	}
                	else{
                		Toast.makeText(SiteRegisterActivity.this, "이미 추가됨", 
                				Toast.LENGTH_SHORT).show();
                		
                	}
                        	
            	}catch(Exception e){
            		System.err.println(e);
            		System.exit(1);
            	}
            	//setResult(RESULT_OK,intent); 
            	finish();
            	
            case R.id.btn_cancel:
            	//setResult(RESULT_OK,intent); 
            	finish();
            }
        }

    };
	
	
    
}
