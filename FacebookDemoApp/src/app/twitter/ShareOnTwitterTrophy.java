package app.twitter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import app.constant.Constants;

public class ShareOnTwitterTrophy extends AsyncTask<String, Integer, Long> {
	private Activity mActivity;
	private Bitmap bitmap;
	private TwitterSession session;
	private ProgressDialog mProgressDlg;
	public ShareOnTwitterTrophy(Activity mActivity,Bitmap bitmap,TwitterSession session) {
		this.mActivity=mActivity;
		this.bitmap=bitmap;
		this.session=session;
	}
	
	protected void onPreExecute() {
		
		mProgressDlg = new ProgressDialog(mActivity);
		mProgressDlg.setCancelable(false);
		mProgressDlg.setMessage("Please Wait...");
		mProgressDlg.show();
	}

	@Override
	protected Long doInBackground(String... arg0) {

		long result = 0;
		// TwitterSession twitterSession = new TwitterSession(activity);
		// AccessToken accessToken = twitterSession.getAccessToken();
		AccessToken accessToken = session.getAccessToken();
		if (accessToken != null) {
			Configuration conf = new ConfigurationBuilder()
					.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY)
					.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET)
					.setOAuthAccessToken(accessToken.getToken())
					.setOAuthAccessTokenSecret(accessToken.getTokenSecret())
					.build();
			
			ImageUploadFactory factory = new ImageUploadFactory(conf);
			ImageUpload upload = factory.getInstance();
			
/*			String path = "file:///android_asset/login_logo.png";
			URL url = null;
			try {
				ImageUploadFactory factory = new ImageUploadFactory(conf);
				ImageUpload upload = factory.getInstance();
				
				url = new URL(path);
				File f = new File(url.toURI());
				
//				f.createNewFile();
// 				write the bytes in file
//				FileOutputStream fo = new FileOutputStream(f);
//				fo.write(bytes.toByteArray());
				// remember close de FileOutput
//				fo.close();
				upload.upload(f, arg0[0]);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			


			Log.d("", "Start sending image...");
			try {
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

				// you can create a new file name "test.jpg" in sdcard
				// folder.
				//Environment.getExternalStorageDirectory()+ File.separator+ "test.jpg";
//				String imagePath 
				File file = mActivity.getCacheDir();
				File f = new File(file,"test.jpg");
//				f.createNewFile();
				// write the bytes in file
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(bytes.toByteArray());

				// remember close de FileOutput
				fo.close();
				upload.upload(f, arg0[0]);
				Log.e("Image Uploaded", "yayeeeee");
				result = 1;
			} catch (Exception e) {
				Log.e("image upload failed", "awwwww :(");
				e.printStackTrace();
			}

			return result;
		}
		return result;
	}

	@Override
	protected void onPostExecute(Long result) {
		if(mProgressDlg!=null && mProgressDlg.isShowing()){
			mProgressDlg.dismiss();
		}
		if (result == 1){
			statusUpdateDialog();
		}else{
			Toast.makeText(mActivity, "Please try again.", Toast.LENGTH_SHORT).show();
		}
	}

	
	private void statusUpdateDialog() {
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
		alertDialog.setTitle("Message Shared");
		alertDialog.setMessage("Message has been shared successfully.");
		alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
			}
		});
		alertDialog.show();
	}
}
