package com.mymy.randomweb;

import com.mymy.randomweb.ObservableWebView;
import com.mymy.randomweb.ObservableWebView.OnScrollChangedCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Random;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public class WebViewActivity extends ActionBarActivity{
	private static final String TAG = "WebViewActi";
	
    private String mInputUrl = "http://m.naver.com";
    private EditText et_InputUrl;
    static ObservableWebView mWebView;
    private WebSettings mWebSettings;
    private ProgressBar mProgressBar;
    private InputMethodManager mInputMethodManager;
    
    private static final int MENU_BACKWARD = 0;
    private static final int MENU_FORWARD = 1;
    private static final int MENU_DELETE = 2;
    private static final int MENU_TXTSHOW = 3;
    private static final int MENU_RANDOM = 4;
    private static final int MENU_MANAGE = 5;
    private static final int MENU_FAVOR = 6;
    private static final int MENU_SETTINGS = 7;
    
    static final int SITE_LIMIT = 100;
    
    private int r1, r2; //random ��ư Ŭ�� ó���� ���(���� ����)
    protected static String title; //���� �������� title
    protected static String curUrl; // ���� �������� �ּ�
    
    // onTouch���� ���̴� �͵�
    private boolean dragFlag = false;   //���� ��ġ�� �巡�� ���� Ȯ��
    private float startYPosition = 0;       //��ġ�̺�Ʈ�� �������� Y(����)��ġ
    private float endYPosition = 0;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getSupportActionBar().hide();
        et_InputUrl = (EditText)findViewById(R.id.edit_Url);
        mWebView = (ObservableWebView)findViewById(R.id.webview);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
         
        mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        
        mWebView.setWebChromeClient(new webViewChrome());
        mWebView.setWebViewClient(new webViewClient());
        mWebSettings = mWebView.getSettings();
        mWebSettings.setBuiltInZoomControls(true); // �̺κ��� true�� �ؾ� ��Ȱ��ȭ ������, ���� ���̾ƿ� ���� ����. ��ũ�Ѻ�...
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setSupportZoom(true); 
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebView.loadUrl(mInputUrl);
        mWebView.setBackgroundResource(color.background_light);
        Log.v(TAG, "" + mWebView.zoomIn());
        mWebView.zoomIn ();
        mWebView.setOnTouchListener(onTouchListener);
        /*
        mWebView.setOnScrollChangedCallback ( new OnScrollChangedCallback(){
            public void onScroll(int l, int t){
                //Do stuff
                Log.d(TAG,"We Scrolled etc...");
              }
           });
        
        */
        et_InputUrl.setOnKeyListener(onKeyListener);
        findViewById(R.id.btn_go).setOnClickListener(onClickListener);

        et_InputUrl.setSelectAllOnFocus(true); // �ּ��Է�â Ŭ�� �� ��ü���
        // To get favicon
        WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
        
        r1 = 0; // ���������� �ε���
        r2 = -1;
        String dirPath = getFilesDir().getAbsolutePath();
		Log.v(TAG, "dirPath : " + dirPath);
		File file = new File(dirPath + "/group_�⺻�׷�.txt");
		
		// ���� �� ���������� ������ ������ �⺻�׷� ���� ���� �� ���� ���
		try{
			if( !file.exists() ) {
				Log.v(TAG, "im in MENU_FAVOR - !file.exists()");
				FileOutputStream fos1 = openFileOutput("group_�⺻�׷�.txt",Context.MODE_PRIVATE);
				String firstline = "URLs\n";
				fos1.write(firstline.getBytes());
				fos1.close();
			}
		}catch(Exception e){
			System.err.println(e);
			System.exit(1);
		}
		
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_BACKWARD, 0, "�ڷ�");
        menu.add(0, MENU_FORWARD, 0, "������");
        menu.add(0, MENU_DELETE, 0, "����");
        menu.add(0, MENU_TXTSHOW, 0, "txt���뺸��");
        menu.add(0, MENU_RANDOM, 0, "����");
        menu.add(0, MENU_MANAGE, 0, "���ã�� ����");
        menu.add(0, MENU_FAVOR, 0, "���ã��");
        menu.add(0, MENU_SETTINGS, 0, "����");
 
        
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case MENU_BACKWARD:
    		if (mWebView.canGoBack())
    			mWebView.goBack();
    		else{
    			Toast.makeText(WebViewActivity.this, "ù �������Դϴ�.", 
    					Toast.LENGTH_SHORT).show();
    		}
    		et_InputUrl.setText(curUrl);
    		
    		return true;
    		
    	case MENU_FORWARD:
    		if (mWebView.canGoForward())
    			mWebView.goForward();
    		et_InputUrl.setText(curUrl);

			return true;
			
    	case MENU_DELETE:
    		String dirPath = getFilesDir().getAbsolutePath();
			Log.v(TAG, "dirPath : " + dirPath);
			File file = new File(dirPath + "/URLs.txt");
			if (file.exists()){file.delete();}
			
			
			java.io.File[] children = getFilesDir().listFiles();
	        try{
	        	Log.v(TAG, "dir : " + children.length);
	            for(int i=0;i<children.length;i++){
                	Log.v(TAG, "���� : " + children[i].getName() + " +++ " + children[i].getPath());
                	if( children[i].getName().startsWith("group_")){
                		children[i].delete();
                	}
                	
	            }
	        }catch(Exception e){}
			
    		return true;
    		
    	case MENU_TXTSHOW:
    		try{
				
    			children = getFilesDir().listFiles();
    			String str1 = "";
    			
    	        try{
    	        	
    	            for(int i=0;i<children.length;i++){
                    	Log.v(TAG, "���� : " + children[i].getName() + " +++ " + children[i].getPath());
                    	String name = children[i].getName();
                    	if( name.startsWith("group_")){
                    		str1 = str1 + "=========start==========\n" + name + "\n" ;
                    		
                    		FileInputStream fis = openFileInput(name);
            				byte[] data = new byte[fis.available()];
            				while (fis.read(data) != -1){;}
            				fis.close();
            				String content = new String(data);
            				str1 = str1 + content + "\n=======end============\n";
                    	}
                    	
    	            }
    	        }catch(Exception e){}
				
				new AlertDialog.Builder(this).setTitle("���ϳ���")
	    		.setMessage(str1)
	    		.setPositiveButton("��", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton){
	    				;
	    			}
	    		}).show();
				
			}
			catch(Exception e){
				System.err.println(e);
				System.exit(1);
			}
    		return true;
    		
    	case MENU_RANDOM:
    		String[] str1 = new String[SITE_LIMIT]; // SITE_LIMIT�������� ���� ����Ʈ ����
    		int i = 0;
    		try{
    			FileInputStream fis = openFileInput("URLs.txt");
    			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
    			String line = reader.readLine();
    			Log.v(TAG, "file ù��° �� : " + line);
    			
    			while((str1[i] = reader.readLine()) != null){
    				
    				Log.v(TAG, str1[i]);
    				i++; 
    		    }
    			// ������ �ι�° ���� index 0
    			reader.close();
    			fis.close();
    		}
    		catch(Exception e){
				System.err.println(e);
				System.exit(1);
			}
    		
    		if(i<=1){
    			Toast.makeText(WebViewActivity.this, "�ΰ� �̻��� ����Ʈ�� ���ã�� �ϼ���", 
    					Toast.LENGTH_SHORT).show();
    			return true;
    		}
    		
    		Random rand = new Random(System.currentTimeMillis());
    		
    		//r1 = oldNum // r2 = presentNumber
    		r2 = rand.nextInt(i); // nextInt(n) => 0~n-1
    		Log.v(TAG, "i : " + i);
    		
    		
    		while(r1 == r2){
    			r2 = rand.nextInt(i);
    		}
    		Log.v(TAG, "���� : " + r2);
    		Log.v(TAG, "���� ���� : " + str1[r2]);
    		
    		String[] splstr = str1[r2].split(" .1.1. .1.1. ");
    		Log.v(TAG, "splstr : " + splstr[1]);
    		mWebView.loadUrl(splstr[1]);                          
    		r1 = r2;

    		return true;
    		
    		
    	case MENU_MANAGE:
    		Intent intent1 = new Intent(WebViewActivity.this, SiteManagerActivity.class); 
			//intent.putExtra("data_name",name_input.getText().toString());  
    		
			startActivity(intent1); 
    		
    		return true;
    		
    	case MENU_FAVOR:
    		// ���� : ���������� �ּҸ� �����ͼ� ���Ͽ� �ֳ� �˻� ��, ������ ���
			//curUrl = et_InputUrl.getText().toString();
			Log.v(TAG, "passed fos.write(curURL.getBytes());" + curUrl);
			
			Intent intent2 = new Intent(WebViewActivity.this, SiteRegisterActivity.class); 
			//intent.putExtra("data_name",name_input.getText().toString());  
		    
			startActivity(intent2); 
    		
    		return true;
    		
    	
    	case MENU_SETTINGS:
    		Intent intent3 = new Intent(WebViewActivity.this, SettingsActivity.class); 
			//intent.putExtra("data_name",name_input.getText().toString());  
		    
			startActivity(intent3);
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			boolean usejavascript = sharedPref.getBoolean("use_javascript", true);
			Toast.makeText(WebViewActivity.this, Boolean.toString(usejavascript), 
					Toast.LENGTH_SHORT).show();
    		
    		return true;
    	}
    	return false;
    	
    }
    
    /*
    // ��ȭ����
    new AlertDialog.Builder(this).setTitle("Question")
	.setMessage("���ã�� �߰��Ͻðڽ�?")
	.setPositiveButton("��", new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int whichButton){
			

				
		}
				

	})
	.setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int whichButton){
			;
		}
	}).show();
    
    
    */
  
    View.OnKeyListener onKeyListener = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			
			if (keyCode == KeyEvent.KEYCODE_ENTER && KeyEvent.ACTION_DOWN == event.getAction()){
	    		Log.v(TAG, "in onKey, KEYCODE_ENTER");
	    		mInputMethodManager.hideSoftInputFromWindow(et_InputUrl.getWindowToken(), 0);
	    		Log.v(TAG, "Passed hideSoftInputFromWindow");
	            
	    		String inputUrl = et_InputUrl.getText().toString();
	            if (!inputUrl.contains("http://") && !inputUrl.contains("https://"))
	                inputUrl = "http://" + inputUrl;
	            
	            
	            if(inputUrl == null){
	            	return false;}
	            et_InputUrl.setText(inputUrl);
	            
	            //�������� �ҷ��´�
	            mWebView.loadUrl(inputUrl);
	    		return true;
	    	}
	    	return false;  

		}
	};
    
    
    
    View.OnClickListener onClickListener = new View.OnClickListener() {
         
        @Override
        public void onClick(View v) {
        }
    };
    
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
    	
    	
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			
			switch(v.getId()){
			case R.id.webview:
				Log.v(TAG, "im case R.id.webview:");
				LinearLayout linear1 = (LinearLayout)findViewById(R.id.linear1);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.v(TAG, "im ACTION_DOWN");
					startYPosition = event.getY(); //ù��° ��ġ�� Y(����)�� ����
		        case MotionEvent.ACTION_MOVE: //��ġ�� �� �� �����̰� ������
		        	Log.v(TAG, "im ACTION_MOVE scrollY : "+ mWebView.getScrollY());
		        	
		            if (event.getY() < startYPosition){
		            	linear1.setVisibility(View.INVISIBLE);
		            }
		            dragFlag = true;
		            break;
		 
		        case MotionEvent.ACTION_UP : 
		            endYPosition = event.getY();
		 
		            if(dragFlag) {  //�巡�׸� �ϴٰ� ��ġ�� ����
		                // ����Y�� �� Y���� ũ�ٸ� ��ġ�� �Ʒ��� ���� �̷�����ٴ� ���̰�, ��ũ���� �Ʒ��γ������ٴ� ���̴�.
		                // (startYPosition - endYPosition) > 10 �� ��ġ�� �̵��� �Ÿ��� 10�ȼ� �̻��� �̵��ؾ� ��ũ�� �̵����� �����ϰڴٴ� �������� �ʿ����� ������ �����ص� �ȴ�.
		                if((startYPosition > endYPosition) && (startYPosition - endYPosition) > 10) {
		                    //TODO ��ũ�� �ٿ� �� �۾�
		                	linear1.setVisibility(View.INVISIBLE);
		                } 
		                //���� Y�� �� ���� �۴ٸ� ��ġ�� ������ �Ʒ��� �̷������ٴ� ���̰�, ��ũ���� �ö󰬴ٴ� ���̴�.
		                else if((startYPosition < endYPosition) && (endYPosition - startYPosition) > 10) {
		                    //TODO ��ũ�� �� �� �۾�
		                	
		                	Log.v(TAG, "im getVisibility() : "+ linear1.getVisibility());
		                	if (mWebView.getScrollY() == 0){
		                		if(linear1.getVisibility() == View.VISIBLE){
				        		linear1.setVisibility(View.INVISIBLE);
		                		}else if(linear1.getVisibility() == View.INVISIBLE){
		                			linear1.setVisibility(View.VISIBLE);
		                		}
		                	}else{
				        		linear1.setVisibility(View.VISIBLE);
		                	}
		                }
		            }
		 
		            startYPosition = 0.0f;
		            endYPosition = 0.0f;
		            dragFlag = false;
		            break;
		        }
		        return false;
			}
			v.performClick();
			return false;
		}
	};
    
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        switch(keyCode){
	        case KeyEvent.KEYCODE_BACK:
		        if (mWebView.canGoBack()) {
		            mWebView.goBack();
		            et_InputUrl.setText(curUrl);
		            return false;
		        } else{ finish();}
		        
	        case KeyEvent.KEYCODE_MENU:
	        	LinearLayout linear1 = (LinearLayout)findViewById(R.id.linear1);
	        	if (linear1.getVisibility() == View.VISIBLE){
	        		linear1.setVisibility(View.INVISIBLE);
		        	return false; // false�� Ű ó�� ����, true�� ���⼭ ó���ϰ� �� = �޴� �ȿ���
	        	}else if(linear1.getVisibility() == View.INVISIBLE){
	        		linear1.setVisibility(View.VISIBLE);
		        	return false;
	        	}
	        	
	        	
	        	
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
	
	
	
    
    // ȭ���� ���ư��� �ʱ�ȭ�Ǵ� ������ �ذ�
    // manifest�� android:configChanges="keyboardHidden|orientation|screenSize"/> �߰�
    @Override
    public void onConfigurationChanged(Configuration newConfig){
    	super.onConfigurationChanged(newConfig);
    }
     
    
    
    class webViewChrome extends WebChromeClient {
         
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // ������ �ε� ���α׷�����
            if(newProgress < 100) {
                mProgressBar.setProgress(newProgress);
            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
                mProgressBar.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
        }
        
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            // do whatever with the arguments passed in
            
            BitmapDrawable iconDrawable = 
            	    new BitmapDrawable(getResources(), icon);
            iconDrawable.setAntiAlias(true);
            ImageView fav1 = (ImageView) findViewById(R.id.favicon1);
            fav1.setImageBitmap(icon);
            
            //LinearLayout linearlayout1 = (LinearLayout) findViewById(R.id.linear1);
            //linearlayout1.setBackground(iconDrawable);
        }
        
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)) {
                //WebViewActivity.this.setTitle(title);
            	WebViewActivity.title = title;
            }
        }

    }
     
    class webViewClient extends WebViewClient {
         
        //Loading�� ���۵Ǹ� ProgressBaró���� �Ѵ�.
        
        // M. �� �޼ҵ带 ���� ���ϸ�, ��ũ ��ġ �� ��â�� ������ ����Ʈ ����â�� �� 
    	@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 15));
            view.loadUrl(url);
            return true;
        }
    	
    	@Override
    	public void onPageStarted(WebView view, String url, Bitmap favicon){
    		super.onPageStarted(view, url, favicon);
    		// view�� �̵��ϱ� ���� view��, url�� �̵��ϴ� ����Ʈ��.
    		et_InputUrl.setText(url); 
    		curUrl = url;
    		Log.v(TAG, "onPageStarted : " + curUrl);
    	}
    		
        @Override
        public void onPageFinished(WebView view, String url){
            super.onPageFinished(view, url);
            
        }
    }
    
    
    
    
    
    
    
    /*
    ContextWrapper cw = new ContextWrapper(this);
    private void clearApplicationCache(java.io.File dir){
        if(dir==null)
            dir = cw.getFilesDir();
        else;
        if(dir==null)
            return;
        else;
        java.io.File[] children = dir.listFiles();
        try{
        	Log.v(TAG, "dir : " + dir.getPath());
        	Log.v(TAG, "dir : " + children.length);
            for(int i=0;i<children.length;i++)
                if(children[i].isDirectory()){
                	Log.v(TAG, "���丮 : " + children[i].getName() + " +++ " + children[i].getPath());
                    clearApplicationCache(children[i]);
                }
                	
                else {
                	Log.v(TAG, "���� : " + children[i].getName() + " +++ " + children[i].getPath());
                	children[i].delete();
                }
        }
        catch(Exception e){}
    }
   
    	*/
    	

    
}