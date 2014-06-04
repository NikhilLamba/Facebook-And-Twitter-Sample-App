package app.constant;

import app.fb.Permissions;


public class Constants {


	public static final String FACEBOOK_APP_ID = "522205301165876";
	public static final String FACEBOOK_NAMESPACE = "app_demo_android";
	public static final Permissions[] PERMISSIONS = new Permissions[] {
		Permissions.USER_ABOUT_ME, Permissions.USER_BIRTHDAY,
		Permissions.USER_LOCATION, Permissions.EMAIL,
		Permissions.PUBLISH_ACTION };
}
