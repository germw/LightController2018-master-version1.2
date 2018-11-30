package hk.hku.eee.lightcontroller2018;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class rename extends AppCompatActivity{
	
	EditText bulb1, bulb2, bulb3, pre1, pre2, pre3;
	Button OK, CANCEL;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rename);	
		
		bulb1 = (EditText) findViewById(R.id.name1);
		bulb2 = (EditText) findViewById(R.id.name2);
		bulb3 = (EditText) findViewById(R.id.name3);
		pre1 = (EditText) findViewById(R.id.rename_pre1);
		pre2 = (EditText) findViewById(R.id.rename_pre2);
		pre3 = (EditText) findViewById(R.id.rename_pre3);
		
		OK = (Button) findViewById(R.id.rename_ok);
		CANCEL = (Button) findViewById(R.id.rename_cancel);
		
		Intent renameScreen = getIntent();
		bulb1.setText(renameScreen.getStringExtra("bulb1"));
		bulb2.setText(renameScreen.getStringExtra("bulb2"));
		bulb3.setText(renameScreen.getStringExtra("bulb3"));
		pre1.setText(renameScreen.getStringExtra("pre1"));
		pre2.setText(renameScreen.getStringExtra("pre2"));
		pre3.setText(renameScreen.getStringExtra("pre3"));
		
		OK.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent();
				intent.putExtra("bulb1", bulb1.getText().toString());
				intent.putExtra("bulb2", bulb2.getText().toString());
				intent.putExtra("bulb3", bulb3.getText().toString());
				intent.putExtra("pre1", pre1.getText().toString());
				intent.putExtra("pre2", pre2.getText().toString());
				intent.putExtra("pre3", pre3.getText().toString());
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		
		CANCEL.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});
		
	}

}
