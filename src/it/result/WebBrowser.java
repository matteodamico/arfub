package it.result;

import it.arFub.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

public class WebBrowser extends Activity
{

	private WebView webView;
	private EditText url;
	String urlString;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webbrowser);
		webView = (WebView) findViewById(R.id.webview);
		url = (EditText) findViewById(R.id.url);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		webView.getSettings().setJavaScriptEnabled(true);
		urlString =null; 
		urlString=getIntent().getExtras().getString("url");
		Log.i("Browser","ok");
		if(urlString==null){
			webView.loadUrl(url.getText().toString());

		}else{
			webView.loadUrl(urlString);
			url.setText(urlString);
		}
	}

	public void go(View v){
		webView.loadUrl(url.getText().toString());
	}

	public void reload(View v)
	{
		webView.reload();
	}

	public void stop(View v)
	{
		webView.stopLoading();
	}

	public void goBack(View v)
	{
		webView.goBack();
	}

	public void goForward(View v)
	{
		webView.goForward();
	}
}