package app.facebookdemoapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import app.bean.UserProfileBean;
import app.fb.FacebookController;
import app.fb.FacebookController.OnLogoutListener;
import app.helper.RoundedImageView;
import app.imageloader.ImageLoader;


public class ProfileActivity extends Activity{

	private UserProfileBean mUserProfileBean;
	private RoundedImageView ivProfileImage;
	private ProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		mProgressDialog=new ProgressDialog(ProfileActivity.this);
		mProgressDialog.setMessage("Please Wait...");

		ivProfileImage		=		(RoundedImageView)findViewById(R.id.ivProfileImage);
		mUserProfileBean=(UserProfileBean) getIntent().getExtras().getSerializable("profile_data");
		
//		setProfileImage();
		ImageLoader.getImageLoader(getApplicationContext()).DisplayImage(mUserProfileBean.getImageUrl(), ivProfileImage, R.drawable.profile);
		((TextView)findViewById(R.id.tvName)).setText(mUserProfileBean.getName());
		((TextView)findViewById(R.id.tvEmail)).setText(mUserProfileBean.getEmail());
		((TextView)findViewById(R.id.tvDob)).setText(mUserProfileBean.getDob());
		((TextView)findViewById(R.id.tvGender)).setText(mUserProfileBean.getGender());
		((TextView)findViewById(R.id.tvAddress)).setText(mUserProfileBean.getAddress());
		((TextView)findViewById(R.id.tvBio)).setText(mUserProfileBean.getBio());

		findViewById(R.id.btnLogout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				disableUserInteraction();
				FacebookController.getInstance(ProfileActivity.this).logout(mOnLogoutListener);
			}
		});
	}



	private OnLogoutListener mOnLogoutListener = new FacebookController.OnLogoutListener() {

		@Override
		public void onFail(String reason) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), reason+"", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onException(Throwable throwable) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onThinking() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLogout() {
			// TODO Auto-generated method stub
			enableUserInteraction();
			Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();
			finish();
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
	
	Bitmap mBitmap;
	private void setProfileImage() {
		
		new Thread() {
			public void run() {
				try {
					mBitmap=BitmapFactory.decodeStream(new URL(mUserProfileBean.getImageUrl()).openConnection().getInputStream());
				
					if(mBitmap!=null) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ivProfileImage.setImageBitmap(mBitmap);
							}
						});
					}
				
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
	}
}
