package app.facebookdemoapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import app.bean.UserProfileBean;
import app.constant.Constants;
import app.fb.SimpleFacebook;
import app.fb.SimpleFacebookConfiguration;
import app.fb.SimpleFacebook.OnLoginListener;
import app.fb.SimpleFacebook.OnProfileRequestListener;
import app.fb.entities.Profile;

import com.facebook.Session;
import com.facebook.model.GraphUser;

public class MainActivity extends Activity {
	private ProgressDialog mProgressDialog;
	private SimpleFacebook mSimpleFacebook;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mProgressDialog=new ProgressDialog(MainActivity.this);
		mProgressDialog.setMessage("Please Wait...");
		handleFacebook();
		findViewById(R.id.btnFacebookLogin).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onFacebookClick();
			}
		});
		printHashKey();
	}


	/**
	 * Show the facebook keyhash
	 */
	private void printHashKey() {

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"app.facebookdemoapp", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("TEMPTAGHASH KEY:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

	/**
	 * Handle for the Facebook button login
	 */
	private void onFacebookClick() {
		if (mSimpleFacebook != null) {
			disableUserInteraction();
			mSimpleFacebook.login(mOnLoginListener);
		}

	}
	@Override
	public void onResume() {
		mSimpleFacebook = SimpleFacebook.getInstance(MainActivity.this);
		super.onResume();
	}
	/**
	 * Set the facebook configuration
	 */
	private void handleFacebook() {
		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
		.setAppId(Constants.FACEBOOK_APP_ID)
		.setNamespace(Constants.FACEBOOK_NAMESPACE)
		.setPermissions(Constants.PERMISSIONS).build();
		SimpleFacebook.setConfiguration(configuration);
	}
	/**
	 * Handle the Facebook Callbacks
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().onActivityResult(MainActivity.this,
					requestCode, resultCode, data);
		}
	}

	/**
	 * Listener for after the Facebook Login
	 */
	private OnLoginListener mOnLoginListener = new SimpleFacebook.OnLoginListener() {

		@Override
		public void onFail(String reason) {
			Toast.makeText(MainActivity.this, reason, Toast.LENGTH_LONG).show();
			enableUserInteraction();
		}

		@Override
		public void onException(Throwable throwable) {
			enableUserInteraction();
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while login is
			// happening
			disableUserInteraction();
		}

		@Override
		public void onLogin() {
			// change the state of the button or do whatever you want
			mSimpleFacebook.getProfile(mOnProfileRequestAdapter);

		}

		@Override
		public void onNotAcceptingPermissions() {
			enableUserInteraction();
		}

	};

	/**
	 * Callback of the facebook user profile
	 */
	private OnProfileRequestListener mOnProfileRequestAdapter = new OnProfileRequestListener() {
		@Override
		public void onComplete(Profile profile) {
			enableUserInteraction();
			if (profile != null) {

				try {
					GraphUser  mGraphUser=profile.getGraphUser();
					JSONObject mJsonObject=mGraphUser.getInnerJSONObject();
					Log.e("sdsds",mJsonObject.toString());
					if(mJsonObject!=null) {
						UserProfileBean mUserProfileBean=new UserProfileBean();
						if(mJsonObject.has("gender")) {
							mUserProfileBean.setGender(mJsonObject.getString("gender"));
						}
						if(mJsonObject.has("email")) {
							mUserProfileBean.setEmail(mJsonObject.getString("email"));
						}
						if(mJsonObject.has("bio")) {
							mUserProfileBean.setBio(mJsonObject.getString("bio"));
						}
						mUserProfileBean.setName(mJsonObject.getString("name"));
						if(mJsonObject.has("birthday")) {
							mUserProfileBean.setDob(mJsonObject.getString("birthday"));
						}
						mUserProfileBean.setImageUrl(mJsonObject.getJSONObject("picture").getJSONObject("data").getString("url"));
						if(mJsonObject.has("location")) {
							JSONObject mJsObject=mJsonObject.getJSONObject("location");
							mUserProfileBean.setAddress(mJsObject.getString("name"));

						}

						Intent mIntent=new Intent(MainActivity.this,ProfileActivity.class);
						mIntent.putExtra("profile_data", mUserProfileBean);
						startActivity(mIntent);
						finish();
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				//http://graph.facebook.com/"+id+"/picture?type=large
				//				Log.e("UserInfo","Facebook Image:-" + mGraphUser.getInnerJSONObject().toString() + "  Name:-"
				//						+ profile.getName() + "Birthday "+mGraphUser.getBirthday());
				//				Log.e("UserInfo","Facebook Id:-" + profile.getId() + "  Name:-"
				//						+ profile.getName() + "  AccessToken:-"
				//						+ mSimpleFacebook.getAccessToken()+ "Birthday "+profile.getBirthday()+ "Email  "+profile.getEmail());
				// TODO open the new fragment for login/Signup


			} else {
				Toast.makeText(MainActivity.this, R.string.error_string,
						Toast.LENGTH_LONG).show();
				enableUserInteraction();
			}
		}

		@Override
		public void onThinking() {
			disableUserInteraction();
		}

		@Override
		public void onException(Throwable throwable) {
			Toast.makeText(MainActivity.this,
					R.string.error_string, Toast.LENGTH_LONG).show();
			enableUserInteraction();
		}

		@Override
		public void onFail(String reason) {
			Toast.makeText(MainActivity.this, reason, Toast.LENGTH_LONG).show();
			enableUserInteraction();

		}
	};


	/**
	 * Show the progress dialog on the screen
	 */
	private void disableUserInteraction() {
		mProgressDialog.show();
	}

	/**
	 * Hide the progress dialog from the screen
	 */
	private void enableUserInteraction() {
		if (mProgressDialog != null &&mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

}
