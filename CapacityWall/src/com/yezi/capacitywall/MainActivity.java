package com.yezi.capacitywall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;

import com.example.capacitywall.R;
import com.iflytek.speech.ErrorCode;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechRecognizer;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SpeechUnderstander;
import com.iflytek.speech.SpeechUnderstanderListener;
import com.iflytek.speech.SpeechUtility;
import com.iflytek.speech.SynthesizerListener;
import com.iflytek.speech.TextUnderstander;
import com.iflytek.speech.TextUnderstanderListener;
import com.iflytek.speech.UnderstanderResult;
import com.iflytek.speech.util.TtsSettings;
import com.iflytek.speech.util.UnderstanderSettings;
import com.iflytek.speech.util.XmlParser;
import com.yezi.capacitywall.base.ConstantValue;
import com.yezi.capacitywall.bean.Seccode;
import com.yezi.capacitywall.creatjson.WriteJson;
import com.yezi.capacitywall.intent.GetResult;
import com.yezi.capacitywall.intent.Parameter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected static final String TAG = "MainActivity";

	private Button bt_drective;

	private SpeechUnderstander mSpeechUnderstander;

	private TextUnderstander mTextUnderstander;
	
	private SpeechSynthesizer mTts1;

	private final int SUCESS = 1;

	private final int FALSE = 0;

	private final int UNDERSTAND = 999;

	private final int ERROR = 404;
	
	private final int SPEECH = 487;
	
	
	public static String SPEAKER = "speaker"; 

	private SharedPreferences mSharedPreferences;

	private SharedPreferences mSharedPreferences1;
	private Toast mToast;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SUCESS:
				Toast.makeText(getApplicationContext(), "成功", 0).show();
				break;
			case FALSE:
				Toast.makeText(getApplicationContext(), "失败", 0).show();
				break;

			case UNDERSTAND:
				String undersentence = (String) msg.obj;

				Toast.makeText(getApplicationContext(), undersentence, 0)
						.show();
				break;

			case ERROR:
				String error = (String) msg.obj;
				Toast.makeText(getApplicationContext(), error, 0).show();
				break;
			case SPEECH:
				String text = (String) msg.obj;
				int code = mTts1.startSpeaking(text, mTtsListener);
				if (code != 0) {
					showTip("start speak error : " + code);
				} else
					showTip("start speak success.");
				break;
				
				
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	InitListener listenr = new InitListener() {

		@Override
		public void onInit(ISpeechModule arg0, int arg1) {

			if (arg1 == ErrorCode.SUCCESS) {
				System.out.println("成功");
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去掉状态栏,全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		bt_drective = (Button) findViewById(R.id.bt_drective);
		
		initDetection();
		mSharedPreferences = getSharedPreferences(
				UnderstanderSettings.PREFER_NAME, Activity.MODE_PRIVATE);
		mSharedPreferences1 = getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
		init();
		initTTS();
		mSpeechUnderstander = new SpeechUnderstander(this,
				speechUnderstanderListener);

		mTextUnderstander = new TextUnderstander(this, textUnderstanderListener);
	}
	
	private void initTTS() {
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mTts1 = new SpeechSynthesizer(getApplicationContext(), mTtsInitListener);
	}

	private void initDetection() {

		if (SpeechUtility.getUtility(this).queryAvailableEngines() == null
				|| SpeechUtility.getUtility(this).queryAvailableEngines().length < 0) {
		}
		// 4d6774d0 53edd194
		SpeechUtility.getUtility(this).setAppid("4d6774d0");

	}

	private void init() {
		bt_drective.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// MyAyntask task = new MyAyntask();
				// task.execute(ConstantValue.NOTEPAD);

				setParam();
				// 启动语义理解
				int scode = mSpeechUnderstander
						.startUnderstanding(mRecognizerListener);
				if (scode != 0) {

				}

			}
		});


	}

	/**
	 * 初期化监听器（语音到语义）。
	 */
	private InitListener speechUnderstanderListener = new InitListener() {

		@Override
		public void onInit(ISpeechModule arg0, int code) {
			Log.d(TAG, "speechUnderstanderListener init() code = " + code);
			if (code == ErrorCode.SUCCESS) {
			}
		}
	};

	/**
	 * 初期化监听器（文本到语义）。
	 */
	private InitListener textUnderstanderListener = new InitListener() {

		@Override
		public void onInit(ISpeechModule arg0, int code) {
			Log.d(TAG, "textUnderstanderListener init() code = " + code);
			if (code == ErrorCode.SUCCESS) {

			}
		}
	};

	class MyAyntask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			if (params[0].contains("Direction")) {
				String text = "yes sir?";	
				System.out.println(text);
				showTip(text);
				setParam1();
				Message message = new Message();
				message.obj=text;
				message.what = SPEECH;
				handler.sendMessage(message);
				// 设置参数
				
				
				
			} else {
				String url = ConstantValue.EXECUTEURL;
				GetResult getres = new GetResult();
				WriteJson josn = new WriteJson();
				List<Seccode> codelist = new ArrayList<Seccode>();
				Seccode code = new Seccode();

				if (params[0].contains("Note")) {
					code.setCode("1");
				}
				else if(params[0].contains("Close")&&params[0].contains("computer")){
					code.setCode("2");
				}
				else if(params[0].contains("music")&&params[0].contains("Open")){
					code.setCode("4");
				}
				else if(params[0].contains("music")&&params[0].contains("Close")){
					code.setCode("5");
				}
				else {
					code.setCode("404");
				}
				// 大部分相同的代码
				commetCode(url, getres, josn, codelist, code);
			}
			return null;
		}

		private void commetCode(String url, GetResult getres, WriteJson josn,
				List<Seccode> codelist, Seccode code) {
			codelist.add(code);
			String codeInfo = josn.writeWifiInfo(codelist);
			System.out.println(codeInfo);
			// String result = getres.getResult(url);
			List<Parameter> param = new ArrayList<Parameter>();
			Parameter parameter = new Parameter("Code", codeInfo);
			param.add(parameter);
			String result = getres.getResult(url, param);
			if(result!=null){
				JSONObject object;
				try {
					object = new JSONObject(result);
					String string = object.getString("result");
					Message message = new Message();
					message.what = Integer.parseInt(string);
					handler.sendMessage(message);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				System.out.println(result);
			}
		}

	}

	/**
	 * 识别回调。
	 */
	private SpeechUnderstanderListener mRecognizerListener = new SpeechUnderstanderListener.Stub() {

		@Override
		public void onVolumeChanged(int v) throws RemoteException {
			showTip("onVolumeChanged：" + v);
		}

		@Override
		public void onError(int errorCode) throws RemoteException {
			showTip("onError Code：" + errorCode);
		}

		@Override
		public void onEndOfSpeech() throws RemoteException {
			showTip("onEndOfSpeech");
		}

		@Override
		public void onBeginOfSpeech() throws RemoteException {
			showTip("onBeginOfSpeech");
		}

		@Override
		public void onResult(final UnderstanderResult result)
				throws RemoteException {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (null != result) {
						// 显示
						Log.d(TAG,
								"understander result："
										+ result.getResultString());
						String text = XmlParser.parseNluResult(result
								.getResultString());
						Message meg = new Message();
						meg.obj = text;
						meg.what = UNDERSTAND;
						System.out.println(text);

						MyAyntask task = new MyAyntask();
						task.execute(text);

					} else {
						Log.d(TAG, "understander result:null");
						showTip("识别结果不正确。");
					}
				}
			});
		}
	};

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE,
				mSharedPreferences.getString(
						"understander_language_preference", "en_us"));
		mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("understander_vadbos_preference",
						"4000"));
	
		mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("understander_vadeos_preference",
						"1000"));
	

		String param = null;
		param = "asr_ptt="
				+ mSharedPreferences.getString("understander_punc_preference",
						"1");
		mSpeechUnderstander.setParameter(SpeechConstant.PARAMS, param
				+ ",asr_audio_path=/sdcard/iflytek/wavaudio.pcm");
	}

	private void showTip(final String str) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mToast.setText(str);
				mToast.show();
			}
		});
	}
	
	private TextUnderstanderListener textListener = new TextUnderstanderListener.Stub() {
		
		@Override
		public void onResult(final UnderstanderResult result) throws RemoteException {
	       	runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (null != result) {
			            	// 显示
							Log.d(TAG, "understander result：" + result.getResultString());
							String text = XmlParser.parseNluResult(result.getResultString());
							
			            } else {
			                Log.d(TAG, "understander result:null");
			                showTip("识别结果不正确。");
			            }
					}
				});
		}
		
		@Override
		public void onError(int errorCode) throws RemoteException {
			showTip("onError Code："	+ errorCode);
		}
	};
	
	/**
     * 初期化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {

		@Override
		public void onInit(ISpeechModule arg0, int code) {
			Log.d(TAG, "InitListener init() code = " + code);
        	if (code == ErrorCode.SUCCESS) {
        		
        	}
		}
    };
    
    
    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener.Stub() {
        @Override
        public void onBufferProgress(int progress) throws RemoteException {
        	 Log.d(TAG, "onBufferProgress :" + progress);
//        	 showTip("onBufferProgress :" + progress);
        }

        @Override
        public void onCompleted(int code) throws RemoteException {
        	Log.d(TAG, "onCompleted code =" + code);
            showTip("onCompleted code =" + code);
        }

        @Override
        public void onSpeakBegin() throws RemoteException {
            Log.d(TAG, "onSpeakBegin");
            showTip("onSpeakBegin");
        }

        @Override
        public void onSpeakPaused() throws RemoteException {
        	 Log.d(TAG, "onSpeakPaused.");
        	 showTip("onSpeakPaused.");
        }

        @Override
        public void onSpeakProgress(int progress) throws RemoteException {
        	Log.d(TAG, "onSpeakProgress :" + progress);
        	showTip("onSpeakProgress :" + progress);
        }

        @Override
        public void onSpeakResumed() throws RemoteException {
        	Log.d(TAG, "onSpeakResumed.");
        	showTip("onSpeakResumed");
        }
    };
    
    /**
	 * 参数设置
	 * @param param
	 * @return 
	 */
	private void setParam1(){
		mTts1.setParameter(SpeechConstant.ENGINE_TYPE,
				mSharedPreferences1.getString("engine_preference", "local"));
		
		if(mSharedPreferences1.getString("engine_preference", "local").equalsIgnoreCase("local")){
			mTts1.setParameter(SpeechSynthesizer.VOICE_NAME,
					mSharedPreferences1.getString("role_cn_preference", "xiaoyan"));
		}else{
			mTts1.setParameter(SpeechSynthesizer.VOICE_NAME,
					mSharedPreferences1.getString("role_cn_preference", "xiaoyan")); 
		}
		mTts1.setParameter(SpeechSynthesizer.SPEED,
				mSharedPreferences1.getString("speed_preference", "50"));
		
		mTts1.setParameter(SpeechSynthesizer.PITCH,
				mSharedPreferences1.getString("pitch_preference", "50"));
		
		mTts1.setParameter(SpeechSynthesizer.VOLUME,
				mSharedPreferences1.getString("volume_preference", "50"));
	}

}
