package hk.hku.eee.lightcontroller2018;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class setting extends AppCompatActivity {

		private int SIZE, time;
		private boolean hideTable = false;
		private boolean bulb1_enB, bulb2_enB, bulb3_enB, bt_auto_offB, bt_auto_sendB;
		private CheckBox checkbox, bulb1_en ,bulb2_en, bulb3_en, bt_auto_off, bt_auto_send;
		private TextView bulb1_name, bulb2_name, bulb3_name, bt_time_display, bt_auto_display;
		private SeekBar bt_time, bt_send_time;
		private Button setting_ok, setting_cancel;
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.setting);	
			
			
			RadioGroup radiogroup_textSize = (RadioGroup) findViewById(R.id.radiogroup_textSize);
			RadioButton textSize_small,textSize_medium, textSize_large, textSize_extraLarge;
			textSize_small = (RadioButton) findViewById(R.id.textSize1);
			textSize_medium = (RadioButton) findViewById(R.id.textSize2);
			textSize_large = (RadioButton) findViewById(R.id.textSize3);
			textSize_extraLarge = (RadioButton) findViewById(R.id.textSize4);
			checkbox = (CheckBox) findViewById(R.id.hideChecked);
			bulb1_en = (CheckBox) findViewById(R.id.bulb1_en);
			bulb2_en = (CheckBox) findViewById(R.id.bulb2_en);
			bulb3_en = (CheckBox) findViewById(R.id.bulb3_en);
			bt_auto_off = (CheckBox) findViewById(R.id.bt_auto_off);
			bt_auto_send = (CheckBox) findViewById(R.id.bt_auto_send);
			bulb1_name = (TextView) findViewById(R.id.bulb1_name);
			bulb2_name = (TextView) findViewById(R.id.bulb2_name);
			bulb3_name = (TextView) findViewById(R.id.bulb3_name);
			bt_time_display = (TextView) findViewById(R.id.bt_time_display);
			bt_auto_display = (TextView) findViewById(R.id.bt_auto_display);
			bt_time = (SeekBar) findViewById(R.id.bt_time);
			bt_send_time = (SeekBar) findViewById(R.id.bt_send_time);
			
			
			Intent settingScreen = getIntent();
			SIZE = settingScreen.getIntExtra("size", 1);
			hideTable = settingScreen.getBooleanExtra("hide", false);
			bulb1_enB = settingScreen.getBooleanExtra("bulb1_en", true);
			bulb2_enB = settingScreen.getBooleanExtra("bulb2_en", true);
			bulb3_enB = settingScreen.getBooleanExtra("bulb3_en", true);
			bt_auto_offB = settingScreen.getBooleanExtra("autoDisableBT", false);
			bt_auto_sendB = settingScreen.getBooleanExtra("autoSend", false);
			bulb1_name.setText(settingScreen.getStringExtra("bulb1_name"));
			bulb2_name.setText(settingScreen.getStringExtra("bulb2_name"));
			bulb3_name.setText(settingScreen.getStringExtra("bulb3_name"));
			bt_time.setProgress(settingScreen.getIntExtra("bt_off_time", 15) -1);
			bt_time_display.setText(Integer.toString((bt_time.getProgress()+1)*5)+" minutes");
			bt_send_time.setProgress(settingScreen.getIntExtra("autoSend_time", 5)-1);
			bt_auto_display.setText(Integer.toString(bt_send_time.getProgress()+1) + " seconds");
			
			
			checkbox.setChecked(hideTable);
			bulb1_en.setChecked(bulb1_enB);
			bulb2_en.setChecked(bulb2_enB);
			bulb3_en.setChecked(bulb3_enB);
			bt_auto_off.setChecked(bt_auto_offB);
			bt_auto_send.setChecked(bt_auto_sendB);
			
			switch(SIZE){
			case 1:
				textSize_small.setChecked(true);
				break;
			case 2:
				textSize_medium.setChecked(true);
				break;
			case 3:
				textSize_large.setChecked(true);
				break;
			case 4:
				textSize_extraLarge.setChecked(true);
				break;
			}
			
			
			radiogroup_textSize.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId){
					switch(checkedId){
					case R.id.textSize1:
						SIZE = 1;
						break;
					case R.id.textSize2:
						SIZE = 2;
						break;
					case R.id.textSize3:
						SIZE = 3;
						break;
					case R.id.textSize4:
						SIZE = 4;
						break;						
					}

				}
			});
			
			bt_time.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				@Override
	        	public void onProgressChanged(SeekBar bt_time, int progressValue, boolean fromUser){
	        		bt_time_display.setText(Integer.toString((progressValue+1)*5) + " minutes");}
	        	
	        	@Override
	        	public void onStartTrackingTouch(SeekBar bt_time){}
	        	
	        	@Override
	        	public void onStopTrackingTouch(SeekBar bt_time){}
			});
			
			bt_send_time.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				@Override
	        	public void onProgressChanged(SeekBar bt_send_time, int progressValue, boolean fromUser){
	        		bt_auto_display.setText(Integer.toString(progressValue+1) + " seconds");}
	        	
	        	@Override
	        	public void onStartTrackingTouch(SeekBar bt_send_time){}
	        	
	        	@Override
	        	public void onStopTrackingTouch(SeekBar bt_send_time){}
			});
			
			setting_ok = (Button) findViewById(R.id.setting_ok);
			setting_ok.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					Intent intent = new Intent();
					intent.putExtra("size", SIZE);
					hideTable = checkbox.isChecked();
					intent.putExtra("hide", hideTable);
					bulb1_enB = bulb1_en.isChecked();
					intent.putExtra("bulb1_en", bulb1_enB);
					bulb2_enB = bulb2_en.isChecked();
					intent.putExtra("bulb2_en", bulb2_enB);
					bulb3_enB = bulb3_en.isChecked();
					intent.putExtra("bulb3_en", bulb3_enB);
					intent.putExtra("bt_auto_off", bt_auto_off.isChecked());
					intent.putExtra("bt_auto_send", bt_auto_send.isChecked());
					intent.putExtra("bt_send_time", bt_send_time.getProgress()+1);
					intent.putExtra("bt_off_time", bt_time.getProgress()+1);
					setResult(RESULT_OK, intent);
			    	finish();	
				}
			});
			
			setting_cancel = (Button) findViewById(R.id.setting_cancel);
			setting_cancel.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					Intent intent = new Intent();
					setResult(RESULT_CANCELED, intent);
			    	finish();	
				}
			});
		}
		
		@Override
	    public void onBackPressed(){
			Intent intent = new Intent();
			intent.putExtra("size", SIZE);
			hideTable = checkbox.isChecked();
			intent.putExtra("hide", hideTable);
			intent.putExtra("bt_auto_off", bt_auto_off.isChecked());
			intent.putExtra("bt_auto_send", bt_auto_send.isChecked());
			setResult(RESULT_OK, intent);
	    	finish();
	    }
}
