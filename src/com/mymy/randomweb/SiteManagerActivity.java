package com.mymy.randomweb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class SiteManagerActivity extends ActionBarActivity {

	private static final String TAG = "SiteManager";
	
	ArrayList<String> Groups;
	ArrayList<String> Items;
	ArrayAdapter<String> Adapter;
	ListView list;
	
	// in onContextItemSelected
	private String topStr = new String(); // 현재 position 기준으로 파일의 위쪽 내용
    private String curStr = new String(); // 기준
    private String bottomStr = new String(); // 파일의 아래쪽 내용
    int pos = 0; // 리스트뷰에서 컨텍스트 메뉴 롱클릭 된 항목 위치값
    
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site_manager);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 홈 액티비티로 가는 홈 버튼 활성화
		
		ContextWrapper cw = new ContextWrapper(this);
        File dir = cw.getFilesDir();
        /*
		try{
			
			Log.v(TAG, "im in MENU_FAVOR - !file.exists()");
			FileOutputStream fos1 = openFileOutput("group_재미로.txt",Context.MODE_APPEND);
			FileOutputStream fos2 = openFileOutput("group_예아.txt",Context.MODE_APPEND);
			
			String firstline = "group_재미로.txt\n";
			fos1.write(firstline.getBytes());
			fos1.close();
			fos2.close();
			
		}catch(Exception e){
			System.err.println(e);
			System.exit(1);
		}
		*/
		
		// group_ 으로 시작하는 파일목록을 스피너에 추가
		Groups = new ArrayList<String>();
		
		Spinner spin1 = (Spinner)findViewById(R.id.spinner1);
		spin1.setPrompt("그룹을 선택하세요");
	
        final String[] files= dir.list(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				return filename.startsWith("group_");
			}
        });
        
        for(int i=0; i < files.length; i++){
        	Groups.add(files[i]);
        	
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, Groups);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setSelection(0);
        spin1.setAdapter(adapter);
        
        spin1.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view, 
        			int position, long id){
        		Items = new ArrayList<String>();
        		String[] str1 = new String[WebViewActivity.SITE_LIMIT]; // SITE_LIMIT개까지만 랜덤 사이트 받음
        		
                int i = 0;
        		try{
        			FileInputStream fis = openFileInput(files[position]);
        			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        			String line = reader.readLine();
        			Log.v(TAG, "file 첫번째 줄 : " + line);
        			String temp1;
        			while((str1[i] = reader.readLine()) != null){
        				Log.v(TAG, "onCreate, while : " + str1[i]);
        				String[] splstr = str1[i].split(" .1.1. .1.1. ");
        				if (splstr[0].length() > 50){
        					temp1 = splstr[0].substring(0, 50) + " . . . ";
        				}else{
        					temp1 = splstr[0];
        				}
        				Items.add(temp1);
        				i++; 
        		    }
        			// 파일의 두번째 줄이 index 0
        			reader.close();
        			fis.close();
        		}
        		catch(Exception e){
        			System.err.println(e);
        			System.exit(1);
        		}

        		Adapter = new ArrayAdapter<String>(SiteManagerActivity.this, android.R.layout.simple_list_item_1, Items);
        		list = (ListView)findViewById(R.id.list1);
        		list.setAdapter(Adapter);
        		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        		
        		list.setOnItemClickListener(mItemClickListener);

        		registerForContextMenu(list);
        	}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
        });
        
        
        
        
	}
	
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		
		if(v == list){
			menu.setHeaderTitle("작업을 선택하세요");
			menu.add(0, 1,0,"편집/수정");
			menu.add(0, 2,0,"삭제");
			
		}
	}
	
	public boolean onContextItemSelected(MenuItem item){
		
		AdapterView.AdapterContextMenuInfo info = 
				(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	    pos = info.position;
	    Log.v(TAG, " pos : " + pos);
	    
	    topStr = "";
	    curStr = "";
	    bottomStr = "";
	    
	    
	    try{
	    	
		    int listCount = list.getCount();
			FileInputStream fis = openFileInput("URLs.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			String line = reader.readLine(); //첫 줄
			topStr = topStr + line + "\n";
			int i=0; // 두번째 줄부터
			while((line = reader.readLine()) != null){
				if (i >= listCount){break;}
				Log.v(TAG, "pass1");
				
				if (i == pos){
					curStr = curStr + line  + "\n";
					Log.v(TAG, "pass3");
					i++; 
					break;
				}
				topStr = topStr + line + "\n";
				i++; 
		    }
			while((line = reader.readLine()) != null){
				if (i >= listCount){break;}
				Log.v(TAG, "pass4");
				
				bottomStr = bottomStr + line + "\n";
			}
			
			Log.v(TAG, "top : " + topStr + "/// cur : " + 
			curStr + "/// bottom : " + bottomStr);
			
			reader.close();
			fis.close();
			
			
	
		}
		catch(Exception e){
			System.err.println(e);
			System.exit(1);
		}
	
	
	    
	    
		switch(item.getItemId()){
		case 1: //edit
			
			String[] splstr = curStr.split(" .1.1. .1.1. ");
    		Log.v(TAG, "splstr : " + splstr[0] + " " + splstr[1]);

			Intent intent1 = new Intent(SiteManagerActivity.this, SiteEditActivity.class); 
			intent1.putExtra("title", splstr[0]);
			intent1.putExtra("curUrl", splstr[1].replace("\n", "")); // without "\n"
    		Log.v(TAG, "162줄 splstr[1] : " + splstr[1].replace("\n", ""));
			startActivityForResult(intent1, 0); 
			// 이후 리턴값은 onActivityReslut 에서 처리
			return true;
		case 2: //delete
			
			try{
				FileOutputStream fos = openFileOutput("URLs.txt",Context.MODE_PRIVATE);
				fos.write(topStr.getBytes()); // 현재 pos 줄을 제외하고 쓰기
				fos.write(bottomStr.getBytes());
				fos.close();
			} catch(Exception e){
				System.err.println(e);
				System.exit(1);
			}
			Items.remove(pos);
	    	Adapter.notifyDataSetChanged();
			
			
			
			return true;
			
		}
		
		return true;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.site_manager, menu);
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
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch (requestCode){
		case 0:
			if (resultCode == RESULT_OK){
				String retTitle = data.getStringExtra("retTitle");
				String retUrl = data.getStringExtra("retUrl") + "\n";
				String modifiedStr = retTitle + " .1.1. .1.1. " + retUrl;
				
			    
			    if(topStr.indexOf(retUrl) == -1 && bottomStr.indexOf(retUrl) == -1){ 
			    	// 파일 안에 현재 url이 없을 때만 저장
			    	
			    	Log.v(TAG, "onActivityResult top : " + topStr + "/// cur : " + 
			    			curStr + "/// bottom : " + bottomStr);
			    	try{
			    		FileOutputStream fos = openFileOutput("URLs.txt",Context.MODE_PRIVATE);
	            		fos.write(topStr.getBytes());
	            		fos.write(modifiedStr.getBytes());
	            		fos.write(bottomStr.getBytes());
	            		
	            		Log.v(TAG, "passed fos.write(surl.getBytes());");
	            		fos.close();
			    	} catch(Exception e){
						System.err.println(e);
						System.exit(1);
					}
			    	
			    	
            		Toast.makeText(SiteManagerActivity.this, "수정완료", 
            				Toast.LENGTH_SHORT).show();
            		Items.remove(pos);
            		Items.add(pos, retTitle);
            		Adapter.notifyDataSetChanged();
            		
			    }
            		
            	else{
            		Toast.makeText(SiteManagerActivity.this, "이미 추가된 주소입니다", 
            				Toast.LENGTH_SHORT).show();
            		
            	}
			}
		}
	}
	
	/*

	public void mOnClick(View v){
		switch(v.getId()){
		case R.id.btn_del:

			new AlertDialog.Builder(this).setTitle("Question")
			.setMessage("선택한 사이트를 삭제 하시겠습니까?")
			.setPositiveButton("네", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton){
					SparseBooleanArray sb = list.getCheckedItemPositions();
					String bufferStr = new String();

					try{
						int listCount = list.getCount();
		    			FileInputStream fis = openFileInput("URLs.txt");
		    			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		    			String line = reader.readLine(); //첫 줄
		    			bufferStr = bufferStr + line + "\n";
		    			int i=0; // 두번째 줄부터
		    			int j=i;
		    			while((line = reader.readLine()) != null){
		    				Log.v(TAG,  i + " :: " + line + " :: " +sb.size() + " :: " + bufferStr);
		    				if (i >= listCount){break;}
		    				Log.v(TAG, "pass1");
		    				if(sb.get(i)){
		    					Log.v(TAG, "pass2 : " + Items.size());
		    					Items.remove(j);
		    					Log.v(TAG, "pass3");
		    					i++; // 삭제했으면 i만 증가.  ArrayList는 중간의 원소가 삭제되면, 한칸씩 왼쪽으로 밀리므로.
		    					continue;
		    				}
		    				bufferStr = bufferStr + line + "\n";
		    				i++; j++; // 삭제하지 않았다면 i, j 둘다 증가.
		    		    }
		    			
		    			// 파일의 두번째 줄이 index 0
		    			reader.close();
		    			fis.close();
		    			
		    			FileOutputStream fos = openFileOutput("URLs.txt",Context.MODE_PRIVATE);
		        		fos.write(bufferStr.getBytes());
		        		fos.close();

		    		}
		    		catch(Exception e){
						System.err.println(e);
						System.exit(1);
					}
					
					list.clearChoices();
					Adapter.notifyDataSetChanged();
					}

						
				}
						

			)
			.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton){
					;
				}
			}).show();
			
			
			
			
			
		}
	}
	*/
	
	
	
	
	AdapterView.OnItemClickListener mItemClickListener = 
			new AdapterView.OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				String mes;
				mes = "Select Item = " + Items.get(position);
				Toast.makeText(SiteManagerActivity.this, mes, Toast.LENGTH_SHORT).show();
				}

			
			};
}
