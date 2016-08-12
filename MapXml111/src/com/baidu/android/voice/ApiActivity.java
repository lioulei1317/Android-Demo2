package com.baidu.android.voice;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.baidu.speech.VoiceRecognitionService;
import com.example.mapxml.R;
import com.example.mapxml.Second_Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ApiActivity extends Activity implements RecognitionListener {
	private static final int REQUEST_UI = 1;
	private ImageView yuyin_tubiao;

	public static final int STATUS_None = 0;
	public static final int STATUS_WaitingReady = 2;
	public static final int STATUS_Ready = 3;
	public static final int STATUS_Speaking = 4;
	public static final int STATUS_Recognition = 5;
	private SpeechRecognizer speechRecognizer;
	private int status = STATUS_None;
	private TextView txtResult;
	private long speechEndTime = -1;
	public static String speakings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yuyinshibie);
		txtResult = (TextView) findViewById(R.id.txtResult);
		yuyin_tubiao = (ImageView) findViewById(R.id.yuyin_tubiao);
		// 创建识别器
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this,
				new ComponentName(this, VoiceRecognitionService.class));
		// 注册监听器
		speechRecognizer.setRecognitionListener(this);
		yuyin_tubiao.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(ApiActivity.this);
				boolean api = sp.getBoolean("api", false);
				if (api) {
					switch (status) {
					case STATUS_None:
						start();
						status = STATUS_WaitingReady;
						break;
					case STATUS_WaitingReady:
						cancel();
						status = STATUS_None;
						break;
					case STATUS_Ready:
						cancel();
						status = STATUS_None;
						break;
					case STATUS_Speaking:
						stop();
						status = STATUS_Recognition;
						break;
					case STATUS_Recognition:
						cancel();
						status = STATUS_None;
						break;
					}
				} else {
					start();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		speechRecognizer.destroy();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			onResults(data.getExtras());
		}
	}

	/**
	 * 
	 * 设置识别参数
	 */
	public void bindParams(Intent intent) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (sp.getBoolean("tips_sound", true)) {
			intent.putExtra(Constant.EXTRA_SOUND_START,
					R.raw.bdspeech_recognition_start);
			intent.putExtra(Constant.EXTRA_SOUND_END, R.raw.bdspeech_speech_end);
			intent.putExtra(Constant.EXTRA_SOUND_SUCCESS,
					R.raw.bdspeech_recognition_success);
			intent.putExtra(Constant.EXTRA_SOUND_ERROR,
					R.raw.bdspeech_recognition_error);
			intent.putExtra(Constant.EXTRA_SOUND_CANCEL,
					R.raw.bdspeech_recognition_cancel);
		}
		if (sp.contains(Constant.EXTRA_INFILE)) {
			String tmp = sp.getString(Constant.EXTRA_INFILE, "")
					.replaceAll(",.*", "").trim();
			intent.putExtra(Constant.EXTRA_INFILE, tmp);
		}
		if (sp.getBoolean(Constant.EXTRA_OUTFILE, false)) {
			intent.putExtra(Constant.EXTRA_OUTFILE, "sdcard/outfile.pcm");
		}
		if (sp.contains(Constant.EXTRA_SAMPLE)) {
			String tmp = sp.getString(Constant.EXTRA_SAMPLE, "")
					.replaceAll(",.*", "").trim();
			if (null != tmp && !"".equals(tmp)) {
				intent.putExtra(Constant.EXTRA_SAMPLE, Integer.parseInt(tmp));
			}
		}
		if (sp.contains(Constant.EXTRA_LANGUAGE)) {
			String tmp = sp.getString(Constant.EXTRA_LANGUAGE, "")
					.replaceAll(",.*", "").trim();
			if (null != tmp && !"".equals(tmp)) {
				intent.putExtra(Constant.EXTRA_LANGUAGE, tmp);
			}
		}
		if (sp.contains(Constant.EXTRA_NLU)) {
			String tmp = sp.getString(Constant.EXTRA_NLU, "")
					.replaceAll(",.*", "").trim();
			if (null != tmp && !"".equals(tmp)) {
				intent.putExtra(Constant.EXTRA_NLU, tmp);
			}
		}

		if (sp.contains(Constant.EXTRA_VAD)) {
			String tmp = sp.getString(Constant.EXTRA_VAD, "")
					.replaceAll(",.*", "").trim();
			if (null != tmp && !"".equals(tmp)) {
				intent.putExtra(Constant.EXTRA_VAD, tmp);
			}
		}
		if (sp.contains(Constant.EXTRA_PROP)) {
			String tmp = sp.getString(Constant.EXTRA_PROP, "")
					.replaceAll(",.*", "").trim();
			if (null != tmp && !"".equals(tmp)) {
				intent.putExtra(Constant.EXTRA_PROP, Integer.parseInt(tmp));
			}
		}

		{
			intent.putExtra(Constant.EXTRA_OFFLINE_SLOT_DATA,
					buildTestSlotData());
		}
	}

	private String buildTestSlotData() {
		JSONObject slotData = new JSONObject();
		JSONArray name = new JSONArray().put("李涌泉").put("郭下纶");
		JSONArray song = new JSONArray().put("七里香").put("发如雪");
		JSONArray artist = new JSONArray().put("周杰伦").put("李世龙");
		JSONArray app = new JSONArray().put("手机百度").put("百度地图");
		JSONArray usercommand = new JSONArray().put("关灯").put("开门");
		try {
			slotData.put(Constant.EXTRA_OFFLINE_SLOT_NAME, name);
			slotData.put(Constant.EXTRA_OFFLINE_SLOT_SONG, song);
			slotData.put(Constant.EXTRA_OFFLINE_SLOT_ARTIST, artist);
			slotData.put(Constant.EXTRA_OFFLINE_SLOT_APP, app);
			slotData.put(Constant.EXTRA_OFFLINE_SLOT_USERCOMMAND, usercommand);
		} catch (JSONException e) {

		}
		return slotData.toString();
	}

	/**
	 * 开始识别
	 */
	private void start() {
		Intent intent = new Intent();
		bindParams(intent);
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		{

			String args = sp.getString("args", "");
			if (null != args) {
				intent.putExtra("args", args);
			}
		}
		boolean api = sp.getBoolean("api", false);
		if (api) {
			speechEndTime = -1;
			speechRecognizer.startListening(intent);
		} else {
			intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
			startActivityForResult(intent, REQUEST_UI);
		}

		txtResult.setText("");
	}

	private void stop() {
		speechRecognizer.stopListening();
	}

	private void cancel() {
		speechRecognizer.cancel();
		status = STATUS_None;
	}

	/**
	 * 准备就绪
	 */
	@Override
	public void onReadyForSpeech(Bundle params) {
		status = STATUS_Ready;
	}

	/**
	 * 开始说话处理
	 */
	@Override
	public void onBeginningOfSpeech() {
		status = STATUS_Speaking;
	}

	/**
	 * 音量变化处理
	 */
	@Override
	public void onRmsChanged(float rmsdB) {

	}

	/**
	 * 录音数据传出处理
	 */
	@Override
	public void onBufferReceived(byte[] buffer) {

	}

	/**
	 * 说话结束处理
	 */
	@Override
	public void onEndOfSpeech() {
		speechEndTime = System.currentTimeMillis();
		status = STATUS_Recognition;
	}

	/**
	 * 出错处理
	 */
	@Override
	public void onError(int error) {
		status = STATUS_None;
		StringBuilder sb = new StringBuilder();
		switch (error) {
		case SpeechRecognizer.ERROR_AUDIO:
			sb.append("音频问题");
			break;
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			sb.append("没有语音输入");
			break;
		case SpeechRecognizer.ERROR_CLIENT:
			sb.append("其它客户端错误");
			break;
		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
			sb.append("权限不足");
			break;
		case SpeechRecognizer.ERROR_NETWORK:
			sb.append("网络问题");
			break;
		case SpeechRecognizer.ERROR_NO_MATCH:
			sb.append("没有匹配的识别结果");
			break;
		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
			sb.append("引擎忙");
			break;
		case SpeechRecognizer.ERROR_SERVER:
			sb.append("服务端错误");
			break;
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
			sb.append("连接超时");
			break;
		}
		sb.append(":" + error);
	}

	/**
	 * 最终结果处理
	 */
	@Override
	public void onResults(Bundle results) {
		long end2finish = System.currentTimeMillis() - speechEndTime;
		status = STATUS_None;
		ArrayList<String> nbest = results
				.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		String strEnd2Finish = "";
		if (end2finish < 60 * 1000) {
			strEnd2Finish = "(waited " + end2finish + "ms)";
		}
		txtResult.setText(nbest.get(0) + strEnd2Finish);
		if((nbest.get(0) + strEnd2Finish)!=null){
			 speakings=nbest.get(0) + strEnd2Finish;
			Intent intent=new Intent(ApiActivity.this,Second_Activity.class);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * 临时结果处理
	 */
	@Override
	public void onPartialResults(Bundle partialResults) {
		ArrayList<String> nbest = partialResults
				.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		if (nbest.size() > 0) {
			txtResult.setText(nbest.get(0));
		}
	}

	/**
	 * 处理事件回调
	 */
	@Override
	public void onEvent(int eventType, Bundle params) {

	}
}
