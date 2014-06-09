package app.twitter;



import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/*
 Copyright [2010] [Abhinava Srivastava]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

public class TwitterSession {
	private SharedPreferences sharedPref;
	private Editor editor;

	private static final String TWEET_AUTH_KEY = "auth_key";
	private static final String TWEET_AUTH_SECRET_KEY = "auth_secret_key";
	private static final String TWEET_USER_NAME = "user_name";
	private static final String TWEET_USER_ID = "user_id";
	private static final String SHARED = "Twitter_Preferences";

	public TwitterSession(Context context) {
		sharedPref 	  = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);

		editor 		  = sharedPref.edit();
	}

	public void storeAccessToken(AccessToken accessToken, String username,String userId) {
		Log.e("TT","AAA"+accessToken.getToken()+"...."+accessToken.getTokenSecret());
		editor.putString(TWEET_AUTH_KEY, accessToken.getToken());
		editor.putString(TWEET_AUTH_SECRET_KEY, accessToken.getTokenSecret());
		editor.putString(TWEET_USER_NAME, username);
		editor.putString(TWEET_USER_ID, userId);

		editor.commit();
	}
	public String getAuthToken(){
		return sharedPref.getString(TWEET_AUTH_KEY, "");
	}
	public String getAuthTokenSecret(){
		return sharedPref.getString(TWEET_AUTH_SECRET_KEY, "");
	}
	public void resetAccessToken() {
		editor.putString(TWEET_AUTH_KEY, null);
		editor.putString(TWEET_AUTH_SECRET_KEY, null);
		editor.putString(TWEET_USER_NAME, null);
		editor.putString(TWEET_USER_ID, null);
		editor.commit();
	}

	public String getUsername() {
		return sharedPref.getString(TWEET_USER_NAME, "");
	}

	public String getUserID() {
		return sharedPref.getString(TWEET_USER_ID, "");
	}

	public AccessToken getAccessToken() {
		String token 		= sharedPref.getString(TWEET_AUTH_KEY, null);
		String tokenSecret 	= sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);

		if (token != null && tokenSecret != null) 
			return new AccessToken(token, tokenSecret);
		else
			return null;
	}
}