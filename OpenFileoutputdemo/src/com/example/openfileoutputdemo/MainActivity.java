package com.example.openfileoutputdemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button btn;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.btn);
		textView = (TextView) findViewById(R.id.textview);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				save();
				String text = load();
				textView.setText(text);
			}

		});
	}

	public void save() {
		// TODO Auto-generated method stub
		String data = "hello word,Äã´óÒ¯";
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			out = openFileOutput("String", MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(data);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	public String load() {
		FileInputStream in = null;
		BufferedReader reader = null;
		StringBuffer buffer = new StringBuffer();
		try {
			in = openFileInput("String");
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		return buffer.toString();

	}
	

}
