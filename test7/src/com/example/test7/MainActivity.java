package com.example.test7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView title = (TextView)findViewById(R.id.title);
        
        if (savedInstanceState == null) {
        	title.setText("Welcome to HelloAndroid!");
        } else {
        	title.setText("Welcome back.");
        }
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	final TextView title = (TextView)findViewById(R.id.title);
    	
    	AsyncTask<Void, Void, Void> task = new  AsyncTask<Void, Void, Void>() {

    		String res;
    		JSONObject json;
			@Override
			protected Void doInBackground(Void... params) {

				HttpSession session = new HttpSession();
				
			    res = getHttpToString("http://192.168.1.6:8080/pages/default/main.aspx?key=12456&shopid=1&theme=androidV", session);
			    Log.i("Http",res);		
			    res = getHttpToString("http://192.168.1.6:8080/pages/Login/SelectShops.ashx", session);
			    Log.i("Http",res);
			    String check = getHttpToString("http://192.168.1.6:8080/pages/Login/Authentication.ashx", session,
			    		new ArrayList<NameValuePair>(){{
			    			add(new BasicNameValuePair("ShopId", "1"));
			    			add(new BasicNameValuePair("UserId", "141"));
			    			add(new BasicNameValuePair("Password", "1113"));
			    		}});
			    Log.i("Http",check);
			    
				return null;
			}

			
			@Override 
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				try {
					
					JSONObject json = new JSONObject(res);
					JSONArray array = json.getJSONArray("Магазины");
					title.setText(array.getJSONObject(0).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onPostExecute(result);
			}
    		
    	};
    	task.execute();
    }

    private class HttpSession {
    	
    	private DefaultHttpClient httpClient;
    	
		public HttpSession() {
			// TODO Auto-generated constructor stub
		}

		public DefaultHttpClient getHttpClient() {
			return httpClient;
		}

		public void setHttpClient(DefaultHttpClient httpClient) {
			this.httpClient = httpClient;
		}
    }
    
	private String getHttpToString(final String url, HttpSession session) {
		
	    return  getHttpToString(url, session, new ArrayList<NameValuePair>());
	    
	}
	
	private String getHttpToString(final String url, HttpSession session, List<NameValuePair> post) {
		
		String result = "";
		
		if(session.getHttpClient() == null) {
			
			session.setHttpClient(new DefaultHttpClient());
		}
			
		DefaultHttpClient httpClient = session.getHttpClient(); 
		
	    try {
	    	
	    	HttpPost httpPost = new HttpPost(url);
	    	httpPost.setEntity(new UrlEncodedFormEntity(post));
	    	HttpResponse response = httpClient.execute(httpPost);

	        HttpEntity entity = response.getEntity();
	        
	        if (entity != null) {
	        	
	            InputStream instream = entity.getContent();
	            result = convertStreamToString(instream);
	            instream.close();
	        }

	    } catch (Exception e) {
	    	
	    }
	    
	    return result;
	}    
	
    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }     


}
