package hk.hku.eee.lightcontroller2018;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class pwm extends AppCompatActivity{
	
	Button warm_10, warm_1, warm1, warm10, cool_10, cool_1, cool1, cool10, pwm_set, pwm_cancel;
	TextView PWM_warm, PWM_cool;
	
	private static int PWM_min = 1;
	private static int PWM_max = 255;
	
	int pwm_warm = 128;
	int pwm_cool = 128;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pwm);
		
		PWM_warm = (TextView) findViewById(R.id.PWM_warm);
		PWM_warm.setText(Integer.toString(pwm_warm));
		PWM_cool = (TextView) findViewById(R.id.PWM_cool);
		PWM_cool.setText(Integer.toString(pwm_cool));
		
		warm_10 = (Button) findViewById(R.id.warm_10);
		warm_10.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if((pwm_warm-10)>PWM_min){
					pwm_warm = pwm_warm-10;
				}else pwm_warm = PWM_min;
				update_pwm();
			}
		});
		
		warm_1 = (Button) findViewById(R.id.warm_1);
		warm_1.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if((pwm_warm-1)>PWM_min){
					pwm_warm = pwm_warm-1;
				}else pwm_warm = PWM_min;
				update_pwm();
			}
		});
		
		warm1 = (Button) findViewById(R.id.warm1);
		warm1.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if((pwm_warm+1)<PWM_max){
					pwm_warm = pwm_warm+1;
				}else pwm_warm = PWM_max;
				update_pwm();
			}
		});
		
		warm10 = (Button) findViewById(R.id.warm10);
		warm10.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if((pwm_warm+10)<PWM_max){
					pwm_warm = pwm_warm+10;
				}else pwm_warm = PWM_max;
				update_pwm();
			}
		});
		
		cool_10 = (Button) findViewById(R.id.cool_10);
		cool_10.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if((pwm_cool-10)>PWM_min){
					pwm_cool = pwm_cool-10;
				}else pwm_cool = PWM_min;
				update_pwm();
			}
		});
		
		cool_1 = (Button) findViewById(R.id.cool_1);
		cool_1.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if((pwm_cool-1)>PWM_min){
					pwm_cool = pwm_cool-1;
				}else pwm_cool = PWM_min;
				update_pwm();
			}
		});
		
		cool1 = (Button) findViewById(R.id.cool1);
		cool1.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if((pwm_cool+1)<PWM_max){
					pwm_cool = pwm_cool+1;
				}else pwm_cool = PWM_max;
				update_pwm();
			}
		});
		
		cool10 = (Button) findViewById(R.id.cool10);
		cool10.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if((pwm_cool+10)<PWM_max){
					pwm_cool = pwm_cool+10;
				}else pwm_cool = PWM_max;
				update_pwm();
			}
		});
		
		pwm_set = (Button) findViewById(R.id.pwm_set);
		pwm_set.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent();
				intent.putExtra("pwm_warm", pwm_warm);
				intent.putExtra("pwm_cool", pwm_cool);
				setResult(RESULT_OK,intent);
				finish();
				
			}
		});
		
		pwm_cancel = (Button) findViewById(R.id.pwm_cancel);
		pwm_cancel.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent();
				setResult(RESULT_CANCELED,intent);
				finish();
			}
		});
	}
	
	public void update_pwm(){
		PWM_warm.setText(Integer.toString(pwm_warm));
		PWM_cool.setText(Integer.toString(pwm_cool));
	}

}
