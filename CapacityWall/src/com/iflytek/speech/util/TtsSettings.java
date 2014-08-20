package com.iflytek.speech.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Window;

import com.example.capacitywall.R;
import com.iflytek.speech.util.SettingTextWatcher;
import com.yezi.capacitywall.MainActivity;


/**
 * ���û��档
 *
 */
public class TtsSettings extends PreferenceActivity implements OnPreferenceChangeListener {
	
	public static final String PREFER_NAME = "com.iflytek.setting";
	private String TAG = "TtsSettings";
	private ListPreference enginePreference = null;
	private ListPreference roleCnPreference = null;
	private SharedPreferences mSharedPreferences;
	private CharSequence[] speakerEntries;
	private CharSequence[] speakerValues;
	private EditTextPreference mSpeedPreference;
	private EditTextPreference mPitchPreference;
	private EditTextPreference mVolumePreference;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// ָ�������ļ�����
		getPreferenceManager().setSharedPreferencesName(PREFER_NAME);
		addPreferencesFromResource(R.xml.tts_setting);
		
		parseSpeaker();
		
		enginePreference = (ListPreference)findPreference("engine_preference"); 
		enginePreference.setOnPreferenceChangeListener(this);
		roleCnPreference = (ListPreference)findPreference("role_cn_preference"); 
		roleCnPreference.setOnPreferenceChangeListener(this);
		mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
		
		mSpeedPreference = (EditTextPreference)findPreference("speed_preference");
		mSpeedPreference.getEditText().addTextChangedListener(new SettingTextWatcher(TtsSettings.this,mSpeedPreference,5));
		
		mPitchPreference = (EditTextPreference)findPreference("pitch_preference");
		mPitchPreference.getEditText().addTextChangedListener(new SettingTextWatcher(TtsSettings.this,mPitchPreference,5));
		
		mVolumePreference = (EditTextPreference)findPreference("volume_preference");
		mVolumePreference.getEditText().addTextChangedListener(new SettingTextWatcher(TtsSettings.this,mVolumePreference,5));
		
		
		if(mSharedPreferences.getString("engine_preference", "local").equalsIgnoreCase("cloud")) {
			roleCnPreference.setTitle("���ߺϳɷ�����");
			roleCnPreference.setDialogTitle("���ߺϳɷ�����");
			roleCnPreference.setEntries(R.array.role_msc_entries);
			roleCnPreference.setEntryValues(R.array.role_msc_values);
			
			if(mSharedPreferences.getString("role_cn_preference", "xiaoyan").equalsIgnoreCase("xiaoyan"))
				roleCnPreference.setValue("xiaoyan");
			else
				roleCnPreference.setValue(mSharedPreferences.getString("role_cn_preference", "xiaoyan"));
		}else{
			roleCnPreference.setTitle("���غϳɷ�����");
			roleCnPreference.setDialogTitle("���غϳɷ�����");
			roleCnPreference.setEntries(speakerEntries);
			roleCnPreference.setEntryValues(speakerValues);
			
			if(mSharedPreferences.getString("role_cn_preference", "xiaoyan").equalsIgnoreCase("xiaoyan"))
				roleCnPreference.setValue("xiaoyan");
			else
				roleCnPreference.setValue(mSharedPreferences.getString("role_cn_preference", "xiaoyan"));
		}
	}
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(enginePreference == preference){
			Log.d(TAG, "onPreferenceChange" + newValue);
			if(newValue.equals("local")){
				roleCnPreference.setTitle("���غϳɷ�����");
				roleCnPreference.setDialogTitle("���غϳɷ�����");
				roleCnPreference.setEntries(speakerEntries);
				roleCnPreference.setEntryValues(speakerValues);
				roleCnPreference.setValue("xiaoyan");
			}else{
				roleCnPreference.setTitle("���ߺϳɷ�����");
				roleCnPreference.setDialogTitle("���ߺϳɷ�����");
				roleCnPreference.setEntries(R.array.role_msc_entries);
				roleCnPreference.setEntryValues(R.array.role_msc_values);
				roleCnPreference.setValue("xiaoyan");
			}			
		}
		return true;
	}		
	
	/**
	 * ��ȡ�Ѿ����صķ����ˣ����أ�
	 */
	private void parseSpeaker() {
		Intent intent = getIntent();
		String speaker = intent.getStringExtra(MainActivity.SPEAKER);
		String[] str = speaker.split(";");
		speakerEntries = new CharSequence[str.length];
		speakerValues  = new CharSequence[str.length];
		for(int i = 0; i < str.length; i++){
			speakerEntries[i] = str[i].split(":")[1];
			speakerValues[i] = str[i].split(":")[0];			
		}
	}
}