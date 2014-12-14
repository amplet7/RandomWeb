package com.mymy.randomweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
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
	
	private ArrayList<String> Groups;
	private int groupPosition = 0;
	private String curFileName;
	private Spinner spin;
	private ArrayAdapter<String> adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site_register);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Ȩ ��Ƽ��Ƽ�� ���� Ȩ ��ư Ȱ��ȭ
		
		
		
    	siteName = (EditText)findViewById(R.id.edit_site_names);
    	siteUrl = (EditText)findViewById(R.id.edit_url);
    	
    	siteName.setText(title);
    	siteUrl.setText(curUrl); 
    	
    	//mEditText.setOnKeyListener(onKeyListener);
    	findViewById(R.id.btn_newgroup).setOnClickListener(onClickListener1);
        findViewById(R.id.btn_submit).setOnClickListener(onClickListener1);
        findViewById(R.id.btn_cancel).setOnClickListener(onClickListener1);
        
        Groups = new ArrayList<String>();
        spin = (Spinner)findViewById(R.id.spinner2);
        spin.setPrompt("�׷��� �����ϼ���");
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
        	Groups.add(files[i].replaceFirst("group_", "").replaceFirst("(.txt)$", ""));
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, Groups);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setSelection(0);
        spin.setAdapter(adapter);
        
        spin.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				groupPosition = position;
				curFileName = "group_" + Groups.get(position) + ".txt";
						
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
            
            case R.id.btn_newgroup: // �� �׷�
            	Intent intent = new Intent(SiteRegisterActivity.this, NewGroupActivity.class); 
    			//intent.putExtra("data_name",name_input.getText().toString());  
    		    
            	startActivityForResult(intent, 0);
            	break;
            	
            case R.id.btn_submit: 	// ���ã�� ���
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
                	while (fis.read(data) != -1){;} //�������� ���⼭ ���ѷ�����.
                	
                	String URLs = new String(data);
                	Log.v(TAG, "URLs : " + URLs);
                	fis.close();
                	
                	

                	
                	if(URLs.indexOf(surl) == -1){ // ���� �ȿ� ���� url�� ���� ���� ����
                		// surl�� �ٹٲ� ���� ������ �κ��ּҸ� ���ĵ� ��ϵ� �ɷ� �Ǻ��ϴ� ���� ���ŵ�
                		FileOutputStream fos = openFileOutput(curFileName,Context.MODE_APPEND);
                		fos.write(saveLine.getBytes());
                		Log.v(TAG, "passed fos.write(surl.getBytes());");
                		fos.close();
                		Toast.makeText(SiteRegisterActivity.this, "�߰��Ϸ�", 
                				Toast.LENGTH_SHORT).show();
                	}
                	else{
                		Toast.makeText(SiteRegisterActivity.this, "�̹� �߰��� �ּ��Դϴ�.", 
                				Toast.LENGTH_SHORT).show();
                		
                	}
                        	
            	}catch(Exception e){
            		System.err.println(e);
            		System.exit(1);
            	}
            	finish();
            	
            case R.id.btn_cancel:
            	finish();
            }
        }

    };
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	Log.v(TAG, "onActivityResult");
		switch (requestCode){
		case 0:
			if (resultCode == RESULT_OK){
				Log.v(TAG, "RESULT_OK");
				String groupName = data.getStringExtra("newgroup");
				Log.v(TAG, "groupName : " + groupName);
				//Log.v(TAG, "SiteReg-> onActivityRes : " + adapter.getPosition(groupName));
				//
				
				Groups.add(Groups.size(), groupName);
				
				adapter.notifyDataSetChanged();
				
				spin.setSelection(Groups.size()-1);
			} else if(resultCode == RESULT_CANCELED){
				Log.v(TAG, "RESULT_CANCELED");
			}
				
		}
	}
    
    
	
	
}
