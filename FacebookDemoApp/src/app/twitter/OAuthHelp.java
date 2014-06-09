package app.twitter;


import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;




public class OAuthHelp {

	public static final String APPLICATION_PREFERENCES = "tweet_preferences";
	public static final String TWEET_AUTH_KEY = "auth_key";
	private static final String TWEET_AUTH_SEKRET_KEY = "auth_secret_key";;
	private SharedPreferences preferences;
	public AccessToken accessToken;
	private String consumerSecretKey;
	private String consumerKey;

	/**
	 * OAuthHelp class constructor loads the consumer keys
	 *
	 * @param context
	 */
	public OAuthHelp(Context context) {
		preferences = context.getSharedPreferences(APPLICATION_PREFERENCES,Context.MODE_PRIVATE);
		accessToken = loadAccessToken();
	}

	/**
	 * Depricated method has been used
	 *
	 * @param twitter
	 */
	public void configureOAuth(Twitter twitter) {
		twitter.setOAuthConsumer(consumerKey, consumerSecretKey);
		twitter.setOAuthAccessToken(accessToken);
	}

	/**
	 * true is accesstoken available false other wise
	 *
	 * @return boolean
	 *
	 */
	public boolean hasAccessToken() {
		return null != accessToken;
	}

	/**
	 * Stores access token in preferences
	 *
	 * @param accessToken
	 */
	public void storeAccessToken(AccessToken accessToken) {
		try{ Editor editor = preferences.edit();
		do{

		}while(accessToken.getToken()==null);
		editor.putString(TWEET_AUTH_KEY, accessToken.getToken());

		editor.putString(TWEET_AUTH_SEKRET_KEY, accessToken.getTokenSecret());
		editor.commit();
		this.accessToken = accessToken;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Loads acess token from SharedPreferences
	 * @return
	 */
	private AccessToken loadAccessToken() {
		String token = preferences.getString(TWEET_AUTH_KEY, null);
		String tokenSecret = preferences.getString(TWEET_AUTH_SEKRET_KEY, null);
		if (null != token && null != tokenSecret) {
			return new AccessToken(token, tokenSecret);
		} else {
			return null;
		}
	}

	
}
