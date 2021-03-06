package org.hvdw.xsofiatweaker;

import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
/*import android.content.SharedPreferences;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo; */
import android.content.ComponentName;
/*import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager; */
import android.app.AndroidAppHelper;
import android.widget.Toast;
/* shellExec and rootExec methods */
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
/* assets filecopy */
/*import android.content.res.AssetManager;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream; */

import de.robv.android.xposed.XposedBridge;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XposedHelpers;
//import de.robv.android.xposed.XC_MethodReplacement;
//import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class XSofiaTweaker implements IXposedHookZygoteInit, IXposedHookLoadPackage {
	public static final String TAG = "XSofiaTweaker";
	public static Context mContext;
	private static PackageManager pm;
	public static XSharedPreferences pref;

	private boolean noKillEnabled;
	private boolean skip_ch_four;
	private boolean disable_airhelper;
	private boolean disable_doorhelper;
	private boolean disable_btphonetop;

	private String band_call_option;
	private String band_call_entry;
	private String bt_phone_call_option;
	private String bt_phone_call_entry;
	private String dvd_call_option;
	private String dvd_call_entry;
	private String eject_call_option;
	private String eject_call_entry;
	private String eq_call_option;
	private String eq_call_entry;
	private String media_call_option;
	private String media_call_entry;
	private String mode_src_call_option;
	private String mode_src_call_entry;
	private String navi_call_option;
	private String navi_call_entry;

	private String acc_on_call_option;
	private String acc_on_call_entry;
	private String acc_off_call_option;
	private String acc_off_call_entry;
	private String resume_call_option;
	private String resume_call_entry;

	private boolean home_key_capture_enabled;
	private String home_call_option;
	private String home_call_entry;
	private boolean mute_key_capture_enabled;
	private String mute_call_option;
	private String mute_call_entry;

	private String test_call_option;
	private String test_entry;

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		Context mContext = (Context) AndroidAppHelper.currentApplication();
		XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
		sharedPreferences.makeWorldReadable();

		noKillEnabled = sharedPreferences.getBoolean(MySettings.PREF_NO_KILL, true);
		skip_ch_four = sharedPreferences.getBoolean(MySettings.PREF_SKIP_CH_FOUR, false);
		disable_airhelper = sharedPreferences.getBoolean(MySettings.PREF_DISABLE_AIRHELPER, false);
		disable_doorhelper = sharedPreferences.getBoolean(MySettings.PREF_DISABLE_DOORHELPER, false);
		disable_btphonetop = sharedPreferences.getBoolean(MySettings.PREF_DISABLE_BTPHONETOP, false);

		band_call_option = sharedPreferences.getString(MySettings.BAND_CALL_OPTION, "");
		band_call_entry = sharedPreferences.getString(MySettings.BAND_CALL_ENTRY, "");
		bt_phone_call_option = sharedPreferences.getString(MySettings.BT_PHONE_CALL_OPTION, "");
		bt_phone_call_entry = sharedPreferences.getString(MySettings.BT_PHONE_CALL_ENTRY, "");
		dvd_call_option = sharedPreferences.getString(MySettings.DVD_CALL_OPTION, "");
		dvd_call_entry = sharedPreferences.getString(MySettings.DVD_CALL_ENTRY, "");
		eject_call_option = sharedPreferences.getString(MySettings.EJECT_CALL_OPTION, "");
		eject_call_entry = sharedPreferences.getString(MySettings.EJECT_CALL_ENTRY, "");
		eq_call_option = sharedPreferences.getString(MySettings.EQ_CALL_OPTION, "");
		eq_call_entry = sharedPreferences.getString(MySettings.EQ_CALL_ENTRY, "");
		media_call_option = sharedPreferences.getString(MySettings.MEDIA_CALL_OPTION, "");
		media_call_entry = sharedPreferences.getString(MySettings.MEDIA_CALL_ENTRY, "");
		mode_src_call_option = sharedPreferences.getString(MySettings.MODE_SRC_CALL_OPTION, "");
		mode_src_call_entry = sharedPreferences.getString(MySettings.MODE_SRC_CALL_ENTRY, "");
		navi_call_option = sharedPreferences.getString(MySettings.NAVI_CALL_OPTION, "");
		navi_call_entry = sharedPreferences.getString(MySettings.NAVI_CALL_ENTRY, "");

		acc_on_call_option = sharedPreferences.getString(MySettings.ACC_ON_CALL_OPTION, "");
		acc_on_call_entry = sharedPreferences.getString(MySettings.ACC_ON_CALL_ENTRY, "");
		acc_off_call_option = sharedPreferences.getString(MySettings.ACC_OFF_CALL_OPTION, "");
		acc_off_call_entry = sharedPreferences.getString(MySettings.ACC_OFF_CALL_ENTRY, "");
		resume_call_option = sharedPreferences.getString(MySettings.RESUME_CALL_OPTION, "");
		resume_call_entry = sharedPreferences.getString(MySettings.RESUME_CALL_ENTRY, "");

		home_key_capture_enabled = sharedPreferences.getBoolean(MySettings.HOME_KEY_CAPTURE, true);
		home_call_option = sharedPreferences.getString(MySettings.HOME_CALL_OPTION, "");
		home_call_entry = sharedPreferences.getString(MySettings.HOME_CALL_ENTRY, "");
		mute_key_capture_enabled = sharedPreferences.getBoolean(MySettings.MUTE_KEY_CAPTURE, true);
		mute_call_option = sharedPreferences.getString(MySettings.MUTE_CALL_OPTION, "");
		mute_call_entry = sharedPreferences.getString(MySettings.MUTE_CALL_ENTRY, "");

		// check our assets file and copy to /sdcard/XSofiaTweaker if necessary
        /*Log.d(TAG, "copying navi_app.txt");
        UtilsActivity.CheckCopyAssetFile(mContext, "navi_app.txt");
        Log.d(TAG, "copying player_app.txt");
        UtilsActivity.CheckCopyAssetFile(mContext, "player_app.txt"); */

	}


	public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
	   XposedBridge.log(TAG + " Loaded app: " + lpparam.packageName);

//	   if (!lpparam.packageName.equals("com.syu.ms")) return;
	   if (lpparam.packageName.equals("com.syu.ms")) {

/**********************************************************************************************************************************************/
		/* This is the No Kill function */
		if (noKillEnabled == true) {
			findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "killAppWhenSleep", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					noKillEnabled = sharedPreferences.getBoolean(MySettings.PREF_NO_KILL, true);
					XposedBridge.log(TAG + " nokill enabled");
					param.setResult(null);
				}
			});
		} else {
			XposedBridge.log(TAG + " nokill disabled");
		}


		/* This should prevent the mute of audio channel 4 (alarm) which is used by Google voice for voice feedback 
		*  This seems like a must-do switch on setting, but when no other channel is used it will give noise, although 
		*  you won't hear that with the engine on */
		if (skip_ch_four == true) {
			findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "setStreamVol", int.class, int.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					int stream = (int) param.args[0];
					if (stream == 4) {
						Context context = (Context) AndroidAppHelper.currentApplication();
						XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
						sharedPreferences.makeWorldReadable();
						skip_ch_four = sharedPreferences.getBoolean(MySettings.PREF_SKIP_CH_FOUR, false);
						XposedBridge.log(TAG + " skipping alarm channel 4 mute");
						Log.d(TAG, " skipping alarm channel 4 mute");

						param.setResult(null);
					}
				}
			});
		}

		if (disable_btphonetop == true) {
			Class<?> ProfileInfoClass = XposedHelpers.findClass("module.bt.HandlerBt$3", lpparam.classLoader);
			   XposedBridge.hookAllMethods(ProfileInfoClass, "topChanged", new XC_MethodHook() {
				   @Override
				   protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					   Context context = (Context) AndroidAppHelper.currentApplication();
					   XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					   sharedPreferences.makeWorldReadable();
					   disable_btphonetop = sharedPreferences.getBoolean(MySettings.PREF_DISABLE_BTPHONETOP, false);
					   XposedBridge.log(TAG + " prevent bt phone app from forcing always on top during call");
					   param.setResult(null);
				   }
			});
		}

		/* Correct the mediaKey function in app/ToolkitApp.java
		*  Gustdens modified code only sends media keys to the active media player
		*  The original code uses an intent and "every" media player listening will react. */
//		findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "mediaKey", int.class, new XC_MethodReplacement() {
		findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "mediaKey", int.class, new XC_MethodHook() {
			@Override
			//protected void replaceHookedMethod(XC_MethodHook.MethodHookParam param) {
			protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
				int onKey1 = (int) param.args[0];
				XposedBridge.log(TAG + " Do not call mediaKey(int, int) but call onKey with parameter 1");
				Log.d(TAG, "Do not call mediaKey(int, int) but call onKey with parameter 1");
				Class<?> classstart = XposedHelpers.findClass("app.ToolkitApp", lpparam.classLoader);
				Object class2Instance = XposedHelpers.newInstance(classstart, onKey1);
				XposedHelpers.callMethod(class2Instance, "onKey");
				XposedBridge.log(TAG + " onKey should be done instead of mediaKey" );

				param.setResult(null);
			}
		});

		findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "mediaKey", int.class, int.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
				int onKey2 = (int) param.args[1];
				XposedBridge.log(TAG + " Do not call mediaKey(int, int) but call onKey with parameter 2");
				Log.d(TAG, "Do not call mediaKey(int, int) but call onKey with parameter 2");
				Class<?> classstart = XposedHelpers.findClass("app.ToolkitApp", lpparam.classLoader);
				Object class2Instance = XposedHelpers.newInstance(classstart, onKey2);
				XposedHelpers.callMethod(class2Instance, "onKey");
				XposedBridge.log(TAG + " onKey should be done instead of mediaKey" );

				param.setResult(null);
			}
		});

/**********************************************************************************************************************************************/
		/* Below are the captured key functions */
		findAndHookMethod("app.HandlerApp", lpparam.classLoader, "wakeup", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
				Context context = (Context) AndroidAppHelper.currentApplication();
				XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
				sharedPreferences.makeWorldReadable();
				resume_call_option = sharedPreferences.getString(MySettings.RESUME_CALL_OPTION, "");
				resume_call_entry = sharedPreferences.getString(MySettings.RESUME_CALL_ENTRY, "");
				XposedBridge.log(TAG + " Execute the RESUME action using specific call method");
				Log.d(TAG, "Execute the RESUME action using specific call method");
			}
		});


		if ((bt_phone_call_option != "") && (bt_phone_call_entry != "")) {
//			findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyBtPhone", new XC_MethodHook() {  Is not correct one ??
			findAndHookMethod("util.JumpPage", lpparam.classLoader, "btPageDialByKey", new XC_MethodHook() { 
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					bt_phone_call_option = sharedPreferences.getString(MySettings.BT_PHONE_CALL_OPTION, "");
					bt_phone_call_entry = sharedPreferences.getString(MySettings.BT_PHONE_CALL_ENTRY, "");
					XposedBridge.log(TAG + " mcuKeyBtPhone pressed; bt_phone_call_option: " + bt_phone_call_option + " bt_phone_call_entry : " + bt_phone_call_entry);
					whichActionToPerform(context, bt_phone_call_option, bt_phone_call_entry);

					param.setResult(null);
				}
			});
		}

		if ((navi_call_option != "") && (navi_call_entry != "")) {
			findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyNavi", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					navi_call_option = sharedPreferences.getString(MySettings.NAVI_CALL_OPTION, "");
					navi_call_entry = sharedPreferences.getString(MySettings.NAVI_CALL_ENTRY, "");
					XposedBridge.log(TAG + " mcuKeyNavi  pressed; navi_call_option: " + navi_call_option + " navi_call_entry : " + navi_call_entry);
					whichActionToPerform(context, navi_call_option, navi_call_entry);

					param.setResult(null);
				}
			});
		}

		if ((band_call_option != "") && (band_call_entry != "")) {
			// band button = radio
			findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyBand", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					band_call_option = sharedPreferences.getString(MySettings.BAND_CALL_OPTION, "");
					band_call_entry = sharedPreferences.getString(MySettings.BAND_CALL_ENTRY, "");
					XposedBridge.log(TAG + " mcuKeyBand (Radio) pressed; forward action to specific call method");
					Log.d(TAG, "mcuKeyBand (Radio) pressed; forward action to specific call method");
					XposedBridge.log(TAG + " band_call_option: " + band_call_option + " band_call_entry : " + band_call_entry);
					whichActionToPerform(context, band_call_option, band_call_entry);

					param.setResult(null);
				}
			});
		}

		if ((mode_src_call_option != "") && (mode_src_call_entry != "")) {
			findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyMode", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					mode_src_call_option = sharedPreferences.getString(MySettings.MODE_SRC_CALL_OPTION, "");
					mode_src_call_entry = sharedPreferences.getString(MySettings.MODE_SRC_CALL_ENTRY, "");
					XposedBridge.log(TAG + " Source/Mode pressed; forward action  to specific call method");
					Log.d(TAG, "Source/Mode pressed; forward action  to specific call method");
					whichActionToPerform(context, mode_src_call_option, mode_src_call_entry);

					param.setResult(null);
				}
			});
		}

		if ((media_call_option != "") && (media_call_entry != "")) {
			findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyPlayer", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					media_call_option = sharedPreferences.getString(MySettings.MEDIA_CALL_OPTION, "");
					media_call_entry = sharedPreferences.getString(MySettings.MEDIA_CALL_ENTRY, "");
					XposedBridge.log(TAG + " MEDIA button pressed; forward action to specific call method");
					Log.d(TAG, "MEDIA button pressed; forward action to specific call method");
					whichActionToPerform(context, media_call_option, media_call_entry);

					param.setResult(null);
				}
			});
		}

		if ((eq_call_option != "") && (eq_call_entry != "")) {
			findAndHookMethod("util.JumpPage", lpparam.classLoader, "eq", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					eq_call_option = sharedPreferences.getString(MySettings.EQ_CALL_OPTION, "");
					eq_call_entry = sharedPreferences.getString(MySettings.EQ_CALL_ENTRY, "");
					XposedBridge.log(TAG + " EQ button pressed; forward action  to specific call method");
					Log.d(TAG, "EQ button pressed; forward action  to specific call method");
					whichActionToPerform(context, eq_call_option, eq_call_entry);

					param.setResult(null);
				}
			});
		}

		findAndHookMethod("dev.ReceiverMcu", lpparam.classLoader, "onHandle", byte[].class, int.class, int.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
				Context context = (Context) AndroidAppHelper.currentApplication();
				//byte[] data  = getByteField(param.thisObject, "byte[].class");
				byte[] data =  (byte[]) param.args[0];
				/* int start = getIntField(param.thisObject, "start");
				int length = getIntField(param.thisObject, "length"); */
				int start = (int) param.args[1];
				int length = (int) param.args[2];
				byte b = data[start];

				XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
				sharedPreferences.makeWorldReadable();

				//Log.d(TAG, "DVD or eject button; Executed the Media action to the launcher.sh");
				if ((b & 255) == 1 && (data[start + 1] & 255) == 0 && (data[start + 2] & 255) == 16 && (data[start + 3] & 255) == 80) {
					dvd_call_option = sharedPreferences.getString(MySettings.DVD_CALL_OPTION, "");
					dvd_call_entry = sharedPreferences.getString(MySettings.DVD_CALL_ENTRY, "");
					XposedBridge.log(TAG + " DVD button pressed; forward action to specific call method");
					Log.d(TAG, "DVD button pressed; forward action to specific call method");
					whichActionToPerform(context, dvd_call_option, dvd_call_entry);
				}
				if ((b & 255) == 1 && (data[start + 1] & 255) == 161 && (data[start + 2] & 255) == 2 && (data[start + 3] & 255) == 91) {
					eject_call_option = sharedPreferences.getString(MySettings.EJECT_CALL_OPTION, "");
					eject_call_entry = sharedPreferences.getString(MySettings.EJECT_CALL_ENTRY, "");
					XposedBridge.log(TAG + " EJECT button pressed; forward action to specific call method");
					Log.d(TAG, "EJECT button pressed; forward action to specific call method");
					whichActionToPerform(context, eject_call_option, eject_call_entry);
				}
			}
		});

		findAndHookMethod("util.JumpPage", lpparam.classLoader, "broadcastByIntentName", String.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
				String actionName = (String) param.args[0];
				Context context = (Context) AndroidAppHelper.currentApplication();
				XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
				sharedPreferences.makeWorldReadable();
				XposedBridge.log(TAG + " broadcastByIntentName in util.JumpPage beforeHooked " + actionName);
				Log.d(TAG, "broadcastByIntentName in util.JumpPage afterHooked " + actionName);
				if (actionName == "com.glsx.boot.ACCON") {
					acc_on_call_option = sharedPreferences.getString(MySettings.ACC_ON_CALL_OPTION, "");
					acc_on_call_entry = sharedPreferences.getString(MySettings.ACC_ON_CALL_ENTRY, "");
					Log.d(TAG, "ACC_ON command received");
					XposedBridge.log(TAG + " ACC_ON command received");
					whichActionToPerform(context, acc_on_call_option, acc_on_call_entry);
				}
				if (actionName == "com.glsx.boot.ACCOFF") {
					acc_off_call_option = sharedPreferences.getString(MySettings.ACC_OFF_CALL_OPTION, "");
					acc_off_call_entry = sharedPreferences.getString(MySettings.ACC_OFF_CALL_ENTRY, "");
					Log.d(TAG, "ACC_OFF command received");
					XposedBridge.log(TAG + " ACC_OFF command received");
					whichActionToPerform(context, acc_off_call_option, acc_off_call_entry);
				}
			}
		});

		if (home_key_capture_enabled == true) {
			findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "keyHome", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					home_call_option = sharedPreferences.getString(MySettings.HOME_CALL_OPTION, "");
					home_call_entry = sharedPreferences.getString(MySettings.HOME_CALL_ENTRY, "");
					XposedBridge.log(TAG + " HOME button pressed; forward action to specific call method");
					Log.d(TAG, "HOME button pressed; forward action to specific call method");
					//executeSystemCall("am start -a android.intent.action.MAIN -c android.intent.category.HOME");
					whichActionToPerform(context, home_call_option, home_call_entry);
					param.setResult(null);
				}
			});
		}

/*		findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "keyBack", new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
				XposedBridge.log(TAG + " BACK button pressed; forward action  to the launcher.sh");
				Log.d(TAG, "BACK button pressed; forward action  to the launcher.sh");
				onItemSelectedp(4);
				param.setResult(null);
			}
		}); */

		if (mute_key_capture_enabled == true) {
			findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyVolMute", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					mute_call_option = sharedPreferences.getString(MySettings.MUTE_CALL_OPTION, "");
					mute_call_entry = sharedPreferences.getString(MySettings.MUTE_CALL_ENTRY, "");
					XposedBridge.log(TAG + " MUTE button pressed; forward action to specific call method");
					Log.d(TAG, "MUTE button pressed; forward action to specific call method");
					whichActionToPerform(context, mute_call_option, mute_call_entry);
					param.setResult(null);
				}
			});
		}

	   /* End of the part where the SofiaServer hooks are taking place
	   *  Now starts the part where the keys of the Canbus apk are captured
	   */ 
	   } else if (lpparam.packageName.equals("com.syu.canbus")) {
		if (disable_airhelper == true) {
			findAndHookMethod("com.syu.ui.air.AirHelper", lpparam.classLoader, "showAndRefresh", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					noKillEnabled = sharedPreferences.getBoolean(MySettings.PREF_DISABLE_AIRHELPER, true);
					XposedBridge.log(TAG + " prevent canbus airconditiong change popup");
					param.setResult(null);
				}
			});
		}


		if (disable_doorhelper == true) {
			findAndHookMethod("com.syu.ui.door.DoorHelper", lpparam.classLoader, "showAndRefresh", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					Context context = (Context) AndroidAppHelper.currentApplication();
					XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xsofiatweaker");
					sharedPreferences.makeWorldReadable();
					noKillEnabled = sharedPreferences.getBoolean(MySettings.PREF_DISABLE_DOORHELPER, true);
					XposedBridge.log(TAG + " prevent canbus door open popup");
					param.setResult(null);
				}
			});
		}

	   /* End of the part where the SofiaServer hooks are taking place
	   *  simply return out of the module if no sofiaserver or vanbus are detected
	   */
	   } else return;
	}
	/* End of the handleLoadPackage function doing the capture key functions */
/**********************************************************************************************************************************************/

	public void whichActionToPerform (Context context, String callMethod, String actionString) {
		XposedBridge.log(TAG + " WhichActionToPerform: Call method: " + callMethod + " actionString: " + actionString);
		if (callMethod.equals("pkgname")) {
			//Log.d(TAG, " the callmethond is indeed pkgname");
			startActivityByPackageName(context, actionString);
		}
		if (callMethod.equals("pkg_intent")) {
			startActivityByIntentName(context, actionString);
		}
		if (callMethod.equals("sys_call")) {
			//executeSystemCall(actionString);
			String[] cmd = actionString.split(";");
			shellExec(cmd);
		}
	};


/*	private static void executeSystemCall(String input) {
		String cmd = input;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			Log.d(TAG, cmd);
			XposedBridge.log(TAG + ": " + cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
*/

// Simple versions: a normal shell version and an su version
/*	public static void shellExec(String...strings) {
		try{
			Process sh = Runtime.getRuntime().exec("sh");
			DataOutputStream outputStream = new DataOutputStream(sh.getOutputStream());

			for (String s : strings) {
				s = s.trim();
				outputStream.writeBytes(s+"\n");
				outputStream.flush();
			}

			outputStream.writeBytes("exit\n");
			outputStream.flush();
			try {
				sh.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			outputStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void rootExec(String...strings) {
		try{
			Process su = Runtime.getRuntime().exec("su");
			DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

			for (String s : strings) {
				s = s.trim();
				outputStream.writeBytes(s+"\n");
				outputStream.flush();
			}

			outputStream.writeBytes("exit\n");
			outputStream.flush();
			try {
				su.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			outputStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
*/

/*  More complicated versions of above shell and su call. As I want to run multiple commands I also need to look at that. 
    copied from https://stackoverflow.com/questions/20932102/execute-shell-command-from-android/26654728
    from the code of CarloCannas
*/
	public static String shellExec(String... strings) {
		String res = "";
		DataOutputStream outputStream = null;
		InputStream response = null;
		try {
			Process sh = Runtime.getRuntime().exec("sh");
			outputStream = new DataOutputStream(sh.getOutputStream());
			response = sh.getInputStream();

			for (String s : strings) {
				s = s.trim();
				outputStream.writeBytes(s + "\n");
				outputStream.flush();
			}

			outputStream.writeBytes("exit\n");
			outputStream.flush();
			try {
				sh.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			res = readFully(response);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Closer.closeSilently(outputStream, response);
		}
		return res;
	}


	public static String rootExec(String... strings) {
		String res = "";
		DataOutputStream outputStream = null;
		InputStream response = null;
		try {
			Process su = Runtime.getRuntime().exec("su");
			outputStream = new DataOutputStream(su.getOutputStream());
			response = su.getInputStream();

			for (String s : strings) {
				s = s.trim();
				outputStream.writeBytes(s + "\n");
				outputStream.flush();
			}

			outputStream.writeBytes("exit\n");
			outputStream.flush();
			try {
				su.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			res = readFully(response);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Closer.closeSilently(outputStream, response);
		}
		return res;
	}

	public static String readFully(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = is.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toString("UTF-8");
	}


	private static void executeScript(String input) {
		String cmd = input;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			Log.d(TAG, cmd);
			XposedBridge.log(TAG + ": " + cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	private static void executeBroadcast(String input) {
		StringBuffer output = new StringBuffer();
		String cmd = "am broadcast -a " + input;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			Log.d(TAG, cmd);
			XposedBridge.log(TAG + ": " + cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	public void startActivityByIntentName(Context context, String component) {
		Intent sIntent = new Intent(Intent.ACTION_MAIN);
		sIntent.setComponent(ComponentName.unflattenFromString(component));
		sIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(sIntent);
	}


	public void startActivityByPackageName(Context context, String packageName) {
		PackageManager pManager = context.getPackageManager();
		Intent intent = pManager.getLaunchIntentForPackage(packageName);
		XposedBridge.log(TAG + " startActivityByPackageName: " + packageName);
		if (intent != null) {
			context.startActivity(intent);
		}
	}
}
