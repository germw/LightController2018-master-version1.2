/*	This Light Controller is making use of the sample program Bluetooth Chat in Eclipse.
 *  By keeping the original Bluetooth connection module, with new UI and functions.
 *  Function kept from Bluetooth:
 *  (1)	DeviceListActivity: 	used to find Bluetooth Device
 * 	(2)	BluetoothChatService:	used to do the data sending and receiving (Handlers)
 * 	These 2 files are unmodified.
 * 	http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/1113/8758.html
 */

package hk.hku.eee.lightcontroller2018;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;


/**
 * This is the main Activity that displays the current chat session.
 */
public class LightController extends AppCompatActivity {
    // Debugging	--predefined
    private static final String TAG = "MainActivity";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler	--predefined
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler	--predefined
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes	--predefined
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_SETTING = 3;			//for calling setting page
    private static final int REQUEST_RENAME = 4;			//for calling rename page
    private static final int REQUEST_PWM = 5;				//for calling pwm mode pae
    
    //waiting time constant
    private static final int SLEEP_TIME = 1000;

    // Layout Views	--predefined
    //private TextView mTitle;
	private TextView btStatus;

    // Name of the connected device	-predefined
    private String mConnectedDeviceName = null;
    
    // 	Define TEXTVIEW for the table of storing status of 3 bulbs
    //	Name of the Bulbs
    private TextView bulb_t = null;
	private TextView table_bulb1 = null;
	private TextView table_bulb2 = null;
	private TextView table_bulb3 = null;
	//	On/Off Status of Bulbs
	private TextView status_t =null;
	private TextView bulb1_status = null;
	private TextView bulb2_status = null;
	private TextView bulb3_status = null;
	//	Intensities of the Bulbs
	private TextView intensity_t;
    private TextView Int1;
    private TextView Int2;
    private TextView Int3;
    //	Color Temperatures of the Bulbs
    private TextView color_t = null;
    private TextView Color1;
    private TextView Color2;
    private TextView Color3;
	//  RGB of bulb 1
	private TextView color_R;
	private TextView color_G;
	private TextView color_B;
    
    //	TextView for the Target Light Intensity and Color Temperature
    private TextView intensity_t1 = null;
	private	TextView LightIntensity = null;
	private TextView color_t1 = null;
	private TextView ColorTemperature = null;
	
	//	ImageView to display the 'color' of current setting, a empty TextView with Background changing
	private ImageView bulb_display = null;
	
	private ImageView bulb1_icon, bulb2_icon, bulb3_icon;

	
	//	Define BUTTONS on the layouts
	//	Buttons for selecting bulbs
	private ImageButton bulb1 = null;
	private ImageButton bulb2 = null;
	private ImageButton bulb3 = null;
	//	Buttons for searching device and saving MAC address
	private Button search = null;
	private Button save_mac = null;
	//	Buttons for setting intensities and color temperatures
	private ImageButton inc_int = null;
	private ImageButton dec_int = null;
	private ImageButton inc_color = null;
	private ImageButton dec_color = null;
	//	Buttons for choosing Preferences setting (Mode)
	private ImageButton pre1 = null;
	private ImageButton pre2 = null;
	private ImageButton pre3 = null;
	//	Buttons for sending out data
    private ImageButton button_send = null;
    private ImageButton Off = null;
    private ImageButton Effect = null;
    private ImageButton Alarm = null;

    private TextView pre1_t;
    private TextView pre2_t;
    private TextView pre3_t;
    private TextView bulb1_t;
    private TextView bulb2_t;
    private TextView bulb3_t;
    
    
    //	Define SEEKBARS for setting intensities and color temperatures
	private SeekBar seekbar_intensity = null;
	private SeekBar seekbar_color = null;


	//Navigation drawer
	private DrawerLayout mDrawerLayout;

	//Currently connected bluetooth address
	private String current_MAC_address;



    //	internal variable
    //	Booleans to store if the bulb is selected, if selected, it is true	
//	boolean bulb1_select = false;
//	boolean bulb2_select = false;
//	boolean bulb3_select = false;
	//	Booleans to store if the bulb is on/off, if the bulb is on, it is true
	boolean bulb1_on = true;
	boolean bulb2_on = true;
	boolean bulb3_on = true;
	//boolean for enabling or disabling the bulbs
	boolean bulb1_en = false;
	boolean bulb2_en = false;
	boolean bulb3_en = false;
	//	Integers to store current target setting of intensity and color temperature
	int intensity = 300;
	int color= 3300;
	//	Integer to store what is the current name bulb doing connection (1-3)
	int bulb_number=1;
	//	Setting MIN, MAX ,and Interval of intensity and color temperatures for doing calculation
	final int min_int = 50;
	final int max_int = 600;
	final int step_int	= 25;
	final int min_color = 3300;
	final int max_color = 7500;
	final int step_color = 100;

	//ArrayList<bulb> bulbs;
	
	//	String to save the current MAC address if the device being connected
	public static String MAC_address=null;

	//	Context being used in getting shared preferences
	final Context context = this;
	//	Shared Preferences to store data, like MAC address, Status of Bulbs, Preferences
	SharedPreferences sharedpreferences;
	
	public static final String MyPreference = "LightController";	
			
    // String buffer for outgoing messages	--predefined
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter	--predefined
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services	--predefined
    private BluetoothService mChatService = null;
    
    // To store the effect currently selected for sending effect command
	int effect_no = 0;
	//private Spinner effect_list = null;
	// A flag to indicate where BlueTooth Adapter is in use
	private boolean bt_available = true;
	// Integers to store which bulbs are lastly connected and going to connect
	private int last_connect =0;
    private int current_connect = 0;
    // Global device, all threads use this device to connect bt
    BluetoothDevice device = null;
    
    //newly added variables
	int menu_no = 1;		//for user menu, controlling the page
	int textSize = 1;		//for setting the size of text, from 1 - 4
	private TableLayout table;	//the status table, for hiding the status table
	private LinearLayout gap1,gap2;
	private boolean hideTable = false;	//boolean for hide the table or not
	private TextView current_flux = null;	//TextView for display the current flux feedback from the bulb
	private TextView current_cct = null;	//TextView for display the current cct feedback from the bulb
	private Switch hideTableSwitch;

	private boolean bt_flag = false;
	int thread_counter = 0 ;				//thread_counter for auto sending command after update the setting
	int autoSend_time = 25;						//define the time for thread_counter
	int bt_off_time = 30;
	int thread_timeout_counter=15;			//not in use
	int hour = 0;				//for timer - turning off the bulbs
	int minute = 0;				//for timer - turning off the bulbs
	boolean alarm = false;		//for timer - indicating if there is already a alarm, should be 1 alarm at most
	int globalCounter = 0;
	boolean autoDisableBT = false;
	boolean autoSend = false;

	private String[] modeList = {"pre1","pre2","pre3"};
	private String[] bulbList = {"bulb1_name","bulb2_name","bulb3_name"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ArrayList<bulb> bulbs = new ArrayList<bulb>();

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
		actionbar.setDisplayHomeAsUpEnabled(true);

		initialization();
		navigationDrawerConfig();

        if(D) Log.e(TAG, "+++ ON CREATE +++");

		btStatus = (TextView) findViewById(R.id.bt_status);

        // Get local Bluetooth adapter	--predefined
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported	--predefined
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        //	Get the last setting of intensity and color temperature, hide/unhide status table, and bulbs enable
        sharedpreferences = getSharedPreferences(MyPreference,Context.MODE_PRIVATE);
        if (sharedpreferences.contains("intensity_memory")){
        	intensity = sharedpreferences.getInt("intensity_memory", 300);
        }
        if (sharedpreferences.contains("color_memory")){
        	color = sharedpreferences.getInt("color_memory", 3300);
        }
        if(sharedpreferences.contains("size")){
        	textSize = sharedpreferences.getInt("size", 1);
        }
        if(sharedpreferences.contains("hide")){
        	hideTable = sharedpreferences.getBoolean("hide",false);
        }
        //if(sharedpreferences.contains("bulb1_en")){
        	bulb1_en = sharedpreferences.getBoolean("bulb1_en",false);
        //}
        if(sharedpreferences.contains("bulb2_en")){
        	bulb2_en = sharedpreferences.getBoolean("bulb2_en",false);
        }
        if(sharedpreferences.contains("bulb3_en")){
        	bulb3_en = sharedpreferences.getBoolean("bulb3_en",false);
        }
        if(sharedpreferences.contains("autoDisableBT")){
        	autoDisableBT = sharedpreferences.getBoolean("autoDisableBT", false);
        }
        if(sharedpreferences.contains("bt_off_time")){
        	bt_off_time = sharedpreferences.getInt("bt_off_time",15);
        }
        if(sharedpreferences.contains("autoSend")){
        	autoSend = sharedpreferences.getBoolean("autoSend", false);
        }
        if(sharedpreferences.contains("autoSend_time")){
        	autoSend_time = sharedpreferences.getInt("autoSend_time", 25);
        }
        thread_timeout_counter = bt_off_time;
    }


    //hook into option menu framework and listen to open the drawer
    @Override
	public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    		case android.R.id.home:
    			mDrawerLayout.openDrawer((GravityCompat.START));
    			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initialization(){
    	pre1_t = (TextView)findViewById(R.id.mode_text1);
    	pre2_t = (TextView)findViewById(R.id.mode_text2);
    	pre3_t = (TextView)findViewById(R.id.mode_text3);
    	bulb1_t = (TextView)findViewById(R.id.bulb1_name);
    	bulb2_t = (TextView)findViewById(R.id.bulb2_name);
    	bulb3_t = (TextView)findViewById(R.id.bulb3_name);
	}


	public void navigationDrawerConfig(){
		mDrawerLayout = findViewById(R.id.drawer_layout);

		final NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(@NonNull MenuItem item) {
						// set item as selected to persist highlight
						//onNavigationItemSelected(navigationView.getMenu().getItem(item.getItemId()));
						//navigationView.getMenu().getItem(item.getItemId()).setChecked(false);

						//operation of different selected items
						int id= item.getItemId();

						switch (id) {
							case R.id.menu:
								showUserMenu();
								break;
							case R.id.setting:
								Intent settingScreen = new Intent (LightController.this,setting.class);
								settingScreen.putExtra("size", textSize);
								settingScreen.putExtra("hide", hideTable);
								settingScreen.putExtra("bulb1_name", bulb1_t.getText().toString());
								settingScreen.putExtra("bulb2_name", bulb2_t.getText().toString());
								settingScreen.putExtra("bulb3_name", bulb3_t.getText().toString());
								settingScreen.putExtra("bulb1_en", bulb1_en);
								settingScreen.putExtra("bulb2_en", bulb2_en);
								settingScreen.putExtra("bulb3_en", bulb3_en);
								settingScreen.putExtra("autoDisableBT", autoDisableBT);
								settingScreen.putExtra("autoSend", autoSend);
								settingScreen.putExtra("autoSend_time", autoSend_time/5);
								settingScreen.putExtra("bt_off_time", bt_off_time/5);
								startActivityForResult(settingScreen,3);
								break;
							case R.id.rename:
								Intent renameScreen = new Intent (getApplicationContext(),rename.class);
								renameScreen.putExtra("bulb1", table_bulb1.getText());
								renameScreen.putExtra("bulb2", table_bulb2.getText());
								renameScreen.putExtra("bulb3", table_bulb3.getText());
								renameScreen.putExtra("pre1", pre1_t.getText().toString());
								renameScreen.putExtra("pre2", pre2_t.getText().toString());
								renameScreen.putExtra("pre3", pre3_t.getText().toString());
								startActivityForResult(renameScreen,4);
								break;
							case R.id.config:
								Intent serverIntent_1 = new Intent(getApplicationContext(), DeviceListActivity.class);
								startActivityForResult(serverIntent_1, REQUEST_CONNECT_DEVICE);
								break;
							case R.id.pwm:
								Intent PWM_mode = new Intent(getApplicationContext(), pwm.class);
								startActivityForResult(PWM_mode,5);
								break;
//							case R.id.connect:
//								// Launch the DeviceListActivity to see devices and do scan
//								Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
//								startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
//								break;
							case R.id.discover:
								// Ensure this device is discoverable by others
								ensureDiscoverable();
								break;
						}


						// close drawer when item is tapped
						mDrawerLayout.closeDrawers();
						return true;
					}
				}
		);
	}


    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        //	check if the BT is enabled, if not, enable BT automatically
        /*if(!mBluetoothAdapter.isEnabled()){
        	mBluetoothAdapter.enable();
        //	This thread is a waiting thread
        //	It check if the BT is enabled 0.3s once, MAX waiting time is 6s
        new Thread(new Runnable() {
        	  @Override
        	  public void run() {
        	    for(int i=1;i<20 && !mBluetoothAdapter.isEnabled();i++){
        	    	try {Thread.sleep(300);} catch (Exception e) {}
        	    }
        	  }
        }).run();
        }*/
        //checkBT();

        //	predefined method that pops out an intent to ask for enable BT
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);	}
        // Otherwise, setup the chat session
        else {	if (mChatService == null) setupChat();	}

    	}	//end for on Start


    //	Method onResume --predefined
    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");
        /*if(!mBluetoothAdapter.isEnabled()){
        	mBluetoothAdapter.enable();

        //	This thread is a waiting thread
        //	It check if the BT is enabled 0.3s once, MAX waiting time is 6s
        new Thread(new Runnable() {
        	  @Override
        	  public void run() {
        	    for(int i=1;i<20 && !mBluetoothAdapter.isEnabled();i++){
        	    	try {Thread.sleep(300);} catch (Exception e) {}
        	    }
        	  }
        }).run();

        last_connect = 0;
        }*/
        checkBT();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                mChatService.start();
            }// Start the Bluetooth chat services
        }
    }//end for onResume

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the send button with a listener that for click events
        /*	While button_send is clicked, it would create a new thread for sending the data
         * 	selected bulbs, the send button and off button are disabled until all data is send out
         *
         */

        button_send = (ImageButton) findViewById(R.id.button_send);
        button_send.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
               	button_send.setEnabled(false);	//	Disable required button

            	//	New Thread to send data
            	 new Thread(new Runnable(){
            		 @ Override
            		public void run(){
            			 // get the required setting at start of the thread
            			Boolean [] select = new Boolean[3];
            			select[0] = bulb1_en;
            			Log.v(TAG,"bulb1 enable: "+select[0]);
            			select[1] = bulb2_en;
            			select[2] = bulb3_en;
            			Log.v(TAG,"bulb2 enable: "+select[1]);
            			Log.v(TAG,"bulb3 enable: "+select[2]);

            			// integers to save data being out
            			int intensity_s = intensity;
            			int color_s = color;

            			//	message is the data being sent out
            			String message = ToCommand(intensity_s,color_s);

            			//thread testing loop, do until bt is available to use
            			while(!bt_available){
            				try {
            					Log.v(TAG,"Bt not available, wait until bt available");
            					Thread.sleep(1000);
            				}
            				catch (InterruptedException e)
							{
								e.printStackTrace();
							}
            			}

            			bt_available = false;	//disable the bt

            			//	initialize the Bulb being connected
//            			bulb_number=1;

            			checkBT();

            			//	this for loop is to connect different device and send data
						 for(int count=1;count<4;count++){
		                	bt_flag = true;
		                	if(select[count-1]){	//check if the bulb is selected
		                							//if yes, get the MAC address
								if(count==1){
									MAC_address=sharedpreferences.getString("bulb1_mac", "");
									current_connect = 1;
									Log.v(TAG, "address 1: "+MAC_address);
								}
								if(count==2){
									MAC_address=sharedpreferences.getString("bulb2_mac", "");
									current_connect = 2;
									Log.v(TAG, "address 2: "+MAC_address);
								}
								if(count==3){
									MAC_address=sharedpreferences.getString("bulb3_mac", "");
									current_connect = 3;
									Log.v(TAG, "address 3: "+MAC_address);
								}

								//	Do the connection if the MAC address is not empty
								if(MAC_address!=""){
									//	get the device if the target device is not connecting
									if(last_connect!=current_connect){
                                        Log.v(TAG,"Bluetooth is send to another device");
										String address = MAC_address;
										Log.v(TAG, "address: "+address);
										device = mBluetoothAdapter.getRemoteDevice(address);

										mChatService.connect(device);

										//	for loop to wait for connection
										//	device.getBondState() = 12 if the connection is success
										for(int i=1;i<15 || device.getBondState()!=device.BOND_BONDED;i++)
										{try {Thread.sleep(200);} catch (Exception e) {}}

										if(device.getBondState()!=device.BOND_BONDED){
											//String address = MAC_address;
											device = mBluetoothAdapter.getRemoteDevice(address);
											mChatService.connect(device);

											//	for loop to wait for connection
											//	device.getBondState() = 12 if the connection is success
											for(int i=1;i<15 || device.getBondState()!=device.BOND_BONDED;i++)
											{try {Thread.sleep(300);} catch (Exception e) {} }
										}

										try{
											//send out the String
											sendMessage(message);
											//Toast.makeText(getApplicationContext(), "Send to device "+count, Toast.LENGTH_LONG).show();
											//	This sendMessage function is predefined
											// 	A string is passed and would be sent through BT

                                            Log.v(TAG,"button send 2: "+message);
											if(count==1){	Setting(bulb1_status, Int1, Color1, intensity_s, color_s);
												last_connect = 1;}
											if(count==2){	Setting(bulb2_status, Int2, Color2, intensity_s, color_s);
												last_connect = 2;}
											if(count==3){	Setting(bulb3_status, Int3, Color3, intensity_s, color_s);
												last_connect = 3;}
										}catch (Exception e){}

										try {
											Thread.sleep(500);
										} catch (Exception e) {}
									}	//end for if(last_connect!=current_connect)
									else if(mChatService.getState() == BluetoothService.STATE_CONNECTED){
										Log.v(TAG,"Bluetooth is currently connected to an device");

										try{
											//send out the String
											sendMessage(message);
											Log.v(TAG,"button send 1: "+message);
											//Toast.makeText(getApplicationContext(), "Send to device "+count, Toast.LENGTH_LONG).show();
											//	This sendMessage function is predefined
											// 	A string is passed and would be sent through BT

											if(count==1){	Setting(bulb1_status, Int1, Color1, intensity_s, color_s);
												last_connect = 1;}
											if(count==2){	Setting(bulb2_status, Int2, Color2, intensity_s, color_s);
												last_connect = 2;}
											if(count==3){	Setting(bulb3_status, Int3, Color3, intensity_s, color_s);
												last_connect = 3;}
										}catch (Exception e){}

										//wait 0.5s to ensure data is sent out
										try {
											Thread.sleep(500);
										}
										catch (Exception e) {}
									}
								} else{
									Toast.makeText(getApplicationContext(), "No Saved MAC Address for Bulb "+Integer.toString(count), Toast.LENGTH_LONG).show();
								}
		                	}
						 }
//						 for(int count=1;count<4;count++){
//							 bt_flag = true;
//							 if(select[count-1]){	//check if the bulb is selected
//								 //if yes, get the MAC address
//								 if(count==1){	MAC_address=sharedpreferences.getString("bulb1_mac", "");
//									 current_connect = 1;}
//								 if(count==2){	MAC_address=sharedpreferences.getString("bulb2_mac", "");
//									 current_connect = 2;}
//								 if(count==3){	MAC_address=sharedpreferences.getString("bulb3_mac", "");
//									 current_connect = 3;}
//
//								 //	Do the connection if the MAC address is not empty
//								 if(MAC_address!=""){
//									 //	get the device if the target device is not connecting
//									 if(last_connect!=current_connect){
//										 String address = MAC_address;
//										 device = mBluetoothAdapter.getRemoteDevice(address);
//										 mChatService.connect(device);
//										 Log.v(TAG, "address: "+MAC_address);
//
//										 //	for loop to wait for connection
//										 //	device.getBondState() = 12 if the connection is success
//										 for(int i=1;i<15 || device.getBondState()!=device.BOND_BONDED;i++)
//										 {try {Thread.sleep(200);} catch (Exception e) {}}
//									 }	//end for if(last_connect!=current_connect)
//
//									 try{
//										 //send out the String
//										 sendMessage(message);
//										 Toast.makeText(getApplicationContext(), "Send to device "+count, Toast.LENGTH_LONG).show();
//										 /*	This sendMessage function is predefined
//										  * 	A string is passed and would be sent through BT
//										  */
//										 if(count==1){	Setting(bulb1_status, Int1, Color1, intensity_s, color_s);
//											 last_connect = 1;}
//										 if(count==2){	Setting(bulb2_status, Int2, Color2, intensity_s, color_s);
//											 last_connect = 2;}
//										 if(count==3){	Setting(bulb3_status, Int3, Color3, intensity_s, color_s);
//											 last_connect = 3;}
//									 }catch (Exception e){}
//
//									 //wait 0.5s to ensure data is sent out
//									 try {Thread.sleep(500);} catch (Exception e) {}

//									 if(device.getBondState()!=device.BOND_BONDED){
//										 String address = MAC_address;
//										 device = mBluetoothAdapter.getRemoteDevice(address);
//										 mChatService.connect(device);
//
//										 //	for loop to wait for connection
//										 //	device.getBondState() = 12 if the connection is success
//										 for(int i=1;i<15 || device.getBondState()!=device.BOND_BONDED;i++)
//										 {try {Thread.sleep(300);} catch (Exception e) {} }
//
//										 try{
//											 //send out the String
//											 sendMessage(message);
//											 Toast.makeText(getApplicationContext(), "Send to device "+count, Toast.LENGTH_LONG).show();
//											 /*	This sendMessage function is predefined
//											  * 	A string is passed and would be sent through BT
//											  */
//											 if(count==1){	Setting(bulb1_status, Int1, Color1, intensity_s, color_s);
//												 last_connect = 1;}
//											 if(count==2){	Setting(bulb2_status, Int2, Color2, intensity_s, color_s);
//												 last_connect = 2;}
//											 if(count==3){	Setting(bulb3_status, Int3, Color3, intensity_s, color_s);
//												 last_connect = 3;}
//										 }catch (Exception e){}
//
//										 //wait 0.5s to ensure data is sent out
//										 try {Thread.sleep(500);} catch (Exception e) {}
//									 }
//								 }else{
//									 Toast.makeText(getApplicationContext(), "No Saved MAC Address for Bulb "+Integer.toString(count), Toast.LENGTH_LONG).show();
//								 }
//							 }
//						 }

						 //	reset the condition of the button to be clickable
		                button_send.post(new Runnable(){
		                	public void run(){button_send.setEnabled(true);}
		                });

		                bt_available = true;	//release the bt for other threads
		            }
	            		}).start();	//end for the thread
            	}});//end for onClick Listener

        // setting a decrease button for intensity, similar for increase button
        dec_int = (ImageButton) findViewById(R.id.dec_intensity);
        dec_int.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		// Do a slightly vibration upon click
        		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        		vib.vibrate(30);
        		//	Set the new intensity and TextView
        		//	MIN intensity is 50, another clikc will bring it to MAX, 600
        		//	Interval of intensity is 50
        		if(intensity>min_int){
        			intensity = intensity - step_int;
        			String temp = Integer.toString(intensity);
        			LightIntensity.setText(temp);
        			//	Also set the progress for seekbar to make it consist
        			//	The formula is hard-coded,  => (current intensity - MIN)/Interval
        			seekbar_intensity.setProgress((intensity-min_int)/step_int);}
        		else{
        			// Similar to above
        			intensity = max_int;
        			String temp = Integer.toString(intensity);
        			LightIntensity.setText(temp);
        			seekbar_intensity.setProgress((intensity-min_int)/step_int);}
        		// This part is changing the color of display, MAX of RGB is 255
        		int figure = (intensity*255)/max_int;
        		//	Fixed Blue to 50 to make it change from black to yellow,
        		//	while Green is about 90% of Red
        		//bulb_display.setBackgroundColor(Color.rgb(figure, (int)(figure*0.9), 50));
        		thread_counter = autoSend_time;
        	}
        });

        //	Similar to Decrease Button, please check dec_int
        inc_int = (ImageButton) findViewById(R.id.inc_intensity);
        inc_int.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        		vib.vibrate(30);
        		if(intensity<max_int){
            		intensity = intensity + step_int;
            		String temp = Integer.toString(intensity);
            		LightIntensity.setText(temp);
            		seekbar_intensity.setProgress((intensity-min_int)/step_int);}
        		else{
        			intensity = min_int;
        			String temp = Integer.toString(intensity);
        			LightIntensity.setText(temp);
        			seekbar_intensity.setProgress((intensity-min_int)/step_int);}
        		int figure = (intensity*255)/max_int;
        		//bulb_display.setBackgroundColor(Color.rgb(figure, (int)(figure*0.9), 50));
        		thread_counter = autoSend_time;
        	}
        });

        //	Similar setting method with intensity control
        //	MIN = 3300,	MAX = 7500, Interval = 100
        dec_color = (ImageButton) findViewById(R.id.dec_color);
        dec_color.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        		vib.vibrate(30);
        		if(color>min_color){
        			color = color - step_color;
        			String temp = Integer.toString(color);
        			ColorTemperature.setText(temp);
        			seekbar_color.setProgress((color-min_color)/step_color);}
        		else{
        			color = max_color;
        			String temp = Integer.toString(color);
        			ColorTemperature.setText(temp);
        			seekbar_color.setProgress((color-min_color)/step_color);}
        		thread_counter = autoSend_time;
        	}
        });

        //	Similar setting method with intensity control
        inc_color = (ImageButton) findViewById(R.id.inc_color);
        inc_color.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        		vib.vibrate(30);
        		if(color<max_color){
        			color = color + step_color;
        			String temp = Integer.toString(color);
        			ColorTemperature.setText(temp);
        			seekbar_color.setProgress((color-min_color)/step_color);}
        		else{
        			color = min_color;
        			String temp = Integer.toString(color);
        			ColorTemperature.setText(temp);
        			seekbar_color.setProgress((color-max_color)/step_color);}
        		thread_counter = autoSend_time;
        	}
        });


        //	Seekbar for intensity control
        seekbar_intensity = (SeekBar) findViewById(R.id.seekbar_intensity);
        //	initialize the progresss
        seekbar_intensity.setProgress((intensity-min_int)/step_int);
        seekbar_intensity.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
        	@Override
        	public void onProgressChanged(SeekBar seekbar_intensity, int progressValue, boolean fromUser){
        		intensity = progressValue*step_int + min_int;
        		LightIntensity.setText(Integer.toString(intensity));
        		int figure = (intensity*255)/max_int;
        		//color_change.setBackgroundColor(Color.rgb(figure, (int)(figure*0.9), 50));
        		//bulb_display.setBackgroundColor(Color.rgb(figure, (int)(figure*0.9), 50));
        		thread_counter = autoSend_time;}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekbar_intensity){}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekbar_intensity){}

        });

        //	seekbar for color temperature control
        seekbar_color = (SeekBar) findViewById(R.id.seekbar_color);
        //	initialize
		seekbar_color.setMax(42);
        seekbar_color.setProgress((color-min_color)/step_color);
        seekbar_color.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
        	@Override
        	public void onProgressChanged(SeekBar seekbar_color, int progressValue, boolean fromUser){
        		color = progressValue*step_color + min_color;
        		ColorTemperature.setText(Integer.toString(color));
        		thread_counter = autoSend_time;}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekbar_color){}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekbar_color){}
        });

        //	Search Button for searching device
        /*search = (Button)findViewById(R.id.search);
        search.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		//	Same as the step calling in long press home key => find device
        		//	A connection will be made and used to save MAC address
        		Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        	}
        });		//not in use now, weight = 0 in xml file

        //	Save MAC Address Button
        save_mac = (Button)findViewById(R.id.save_mac);
        save_mac.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		if(MAC_address!=null){
        		//check how many bulbs are selected, and which bulb is selected
        		int number_selected=0;
        		if(bulb1_select){number_selected++;bulb_number=1;}
        		if(bulb2_select){number_selected++;bulb_number=2;}
        		if(bulb3_select){number_selected++;bulb_number=3;}
        		// if only 1 bulb is selected, can perform action saving MAC-address to shared preferences
        		if(number_selected==1){
        		Editor editor = sharedpreferences.edit();
        		if(bulb_number==1){
        				editor.putString("bulb1_mac", MAC_address);
        				editor.commit();
        				last_connect = 1;}
        		else{	if(bulb_number==2){
        				editor.putString("bulb2_mac", MAC_address);
        				editor.commit();
        				last_connect = 2;}
        		else{	editor.putString("bulb3_mac", MAC_address);
        				editor.commit();
        				last_connect = 3;} // end of if(bulb_number==2)
        		}// end of if (bulb_number==1)

        		// making toast to user that it is success
        		Toast.makeText(getApplicationContext(), "Saved Bulb "+Integer.toString(bulb_number)+
        				"'s Mac Address: " + MAC_address, Toast.LENGTH_LONG).show();}
        		else{	//else for if(number_selected==1)
        			//	ask the user to select only one device
        			Toast.makeText(getApplicationContext(), "PLEASE SELEC ONLY 1 BULB TO SAVE MAC ADDRESS", Toast.LENGTH_LONG).show();}}
        		//else for if(MAC_address!=null)
        		else{//	ask the user to connect first
        			Toast.makeText(getApplicationContext(), "PLEASE SEARCH A BULB AND CONNECT FIRST", Toast.LENGTH_LONG).show();}
        	}
        });*/

        bulb1_icon = (ImageView) findViewById(R.id.bulb1_icon);
        bulb2_icon = (ImageView) findViewById(R.id.bulb2_icon);
        bulb3_icon = (ImageView) findViewById(R.id.bulb3_icon);

        //	Selection button for bulb1, similar for bulb2 and bulb3
        bulb1 = (ImageButton)findViewById(R.id.bulb1);
		UpdateBulb(bulb1,bulb1_icon,bulb1_en);
        bulb1.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
				if(bulb1_en){
					bulb1.setImageResource(R.drawable.ic_bulb_off);
					bulb1_icon.setImageResource(R.drawable.cross);
				}
				else{
					bulb1.setImageResource(R.drawable.ic_bulb_on);
					bulb1_icon.setImageResource(R.drawable.tick);
				}

				bulb1_en = !bulb1_en;
				//Toast.makeText(getApplicationContext(), "Bulb 1:"+bulb1_en, Toast.LENGTH_LONG).show();
        	}
        });

//        //	Long press the button can rename the bulb
//        bulb1.setOnLongClickListener(new OnLongClickListener(){
//        	public boolean onLongClick(View v){
//        		// call a prompt out for user to input new name
//        		LayoutInflater li = LayoutInflater.from(context);
//        		View promptsView = li.inflate(R.layout.prompts,null);
//
//        		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        		alertDialogBuilder.setView(promptsView);
//        		final EditText Name_Input = (EditText) promptsView.findViewById(R.id.Name_Input);
//        		alertDialogBuilder.setCancelable(false)
//        		.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
//        			public void onClick(DialogInterface dialog, int id){
//        				// To save the new name and renew required TextView
//        				bulb1.setText(Name_Input.getText());
//        				Editor editor = sharedpreferences.edit();
//        				editor.putString("bulb1_name",bulb1.getText().toString());
//        				editor.commit();
//        				table_bulb1.setText(bulb1.getText().toString());
//        			}
//        		})//end for setPositive Btton
//        		.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//        			public void onClick(DialogInterface dialog,int id){
//        				dialog.cancel();
//        			}
//        		});//end for set Negative Button
//
//        		AlertDialog alertDialog = alertDialogBuilder.create();
//        		alertDialog.show();
//
//        		return true;
//        	}//end for onLongClick
//        });


        // Similar to above
        bulb2 = (ImageButton)findViewById(R.id.bulb2);
		UpdateBulb(bulb2,bulb2_icon,bulb2_en);
        bulb2.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
				if (bulb2_en) {
					bulb2.setImageResource(R.drawable.ic_bulb_off);
					bulb2_icon.setImageResource(R.drawable.cross);
					//Toast.makeText(getApplicationContext(), "Bulb 2:"+bulb2_en, Toast.LENGTH_LONG).show();
				} else {
					bulb2.setImageResource(R.drawable.ic_bulb_on);
					bulb2_icon.setImageResource(R.drawable.tick);
				}

				bulb2_en = !bulb2_en;
			}
        });

//        //	Similar to above
//        bulb2.setOnLongClickListener(new OnLongClickListener(){
//        	public boolean onLongClick(View v){
//        		LayoutInflater li = LayoutInflater.from(context);
//        		View promptsView = li.inflate(R.layout.prompts,null);
//
//        		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        		alertDialogBuilder.setView(promptsView);
//        		final EditText Name_Input = (EditText) promptsView.findViewById(R.id.Name_Input);
//        		alertDialogBuilder.setCancelable(false)
//        		.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
//        			public void onClick(DialogInterface dialog, int id){
//        				bulb2.setText(Name_Input.getText());
//        				Editor editor = sharedpreferences.edit();
//        				editor.putString("bulb2_name",bulb2.getText().toString());
//        				editor.commit();
//        				table_bulb2.setText(bulb2.getText().toString());
//        			}
//        		})	//end for setPositiveButton
//        		.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//        			public void onClick(DialogInterface dialog,int id){
//        				dialog.cancel();
//        			}
//        		});	//end for setNegativeButton
//
//        		AlertDialog alertDialog = alertDialogBuilder.create();
//        		alertDialog.show();
//        		return true;
//        	}
//        });

        //	same as above
        bulb3 = (ImageButton)findViewById(R.id.bulb3);
		UpdateBulb(bulb3,bulb3_icon,bulb3_en);
        bulb3.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
				if(bulb3_en){
					bulb3.setImageResource(R.drawable.ic_bulb_off);
					bulb3_icon.setImageResource(R.drawable.cross);
				}
				else{
					bulb3.setImageResource(R.drawable.ic_bulb_on);
					bulb3_icon.setImageResource(R.drawable.tick);
				}

				bulb3_en = !bulb3_en;
        	}
        });


        //	same as above
//        bulb3.setOnLongClickListener(new OnLongClickListener(){
//        	public boolean onLongClick(View v){
//        		LayoutInflater li = LayoutInflater.from(context);
//        		View promptsView = li.inflate(R.layout.prompts,null);
//
//        		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        		alertDialogBuilder.setView(promptsView);
//        		final EditText Name_Input = (EditText) promptsView.findViewById(R.id.Name_Input);
//        		alertDialogBuilder.setCancelable(false)
//        		.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
//        			public void onClick(DialogInterface dialog, int id){
//        				bulb3.setText(Name_Input.getText());
//        				Editor editor = sharedpreferences.edit();
//        				editor.putString("bulb3_name",bulb3.getText().toString());
//        				editor.commit();
//        				table_bulb3.setText(bulb3.getText().toString());
//        			}
//        		})	//end for setPositiveButton
//        		.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//        			public void onClick(DialogInterface dialog,int id){
//        				dialog.cancel();
//        			}
//        		});	//end for setNegativeButton
//
//        		AlertDialog alertDialog = alertDialogBuilder.create();
//        		alertDialog.show();
//        		return true;
//        	}
//        });

        //	Similar to Send Button
        Off = (	ImageButton) findViewById(R.id.turn_off);
        Off.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Off.setEnabled(false);
            	 new Thread(new Runnable(){
            		 @ Override
            		public void run(){
                // Send a message using content of the edit text widget
            			Boolean [] select = new Boolean[3];
            			select[0] = bulb1_en;
            			select[1] = bulb2_en;
            			select[2] = bulb3_en;
            			String message;
            			//	fixed format to turn the bulb off
		            	message = "$010#1000";
		            	//testing thread for bt availability
		                while(!bt_available){try {Thread.sleep(10000);} catch (InterruptedException e) {e.printStackTrace();}}
		                bt_available = false;
		                bulb_number=1;
		                checkBT();
		                for(int count=1;count<4;count++){
		                	bt_flag = true;
		                	if(select[count-1]){
		                	if(count==1){MAC_address=sharedpreferences.getString("bulb1_mac", "");
		                				current_connect = 1;}
		                	if(count==2){MAC_address=sharedpreferences.getString("bulb2_mac", "");
		                				current_connect = 2;}
		                	if(count==3){MAC_address=sharedpreferences.getString("bulb3_mac", "");
		                				current_connect = 3;}
		            		if(MAC_address!=""){
		            			if(current_connect!=last_connect){
		            			String address = MAC_address;
		            			device = mBluetoothAdapter.getRemoteDevice(address);
		            			mChatService.connect(device);
		            			for(int i=1;i<15 || device.getBondState()!=device.BOND_BONDED;i++)
		            			{try {Thread.sleep(200);} catch (Exception e) {}}
		            			}	//end for if(current_connect!=last_connect)
		            			if(device.getBondState()!=device.BOND_BONDED){
		            				String address = MAC_address;
			            			device = mBluetoothAdapter.getRemoteDevice(address);
			            			mChatService.connect(device);
			            		for(int i=1;i<15 || device.getBondState()!=12;i++)
			            		{try {Thread.sleep(200);} catch (Exception e) {}}
		            			}	//end for if(device.getBondState()!=device.BOND_BONDED
		            		try{
		            			sendMessage(message);
		            			if(count==1){Set_off(bulb1_status);last_connect = 1;}
		            			if(count==2){Set_off(bulb2_status);last_connect = 2;}
		            			if(count==3){Set_off(bulb3_status);last_connect = 3;}
		            		}catch (Exception e){}
		            		try {Thread.sleep(500);} catch (Exception e) {}
		            		if(!bt_flag){
		            			try{
			            			sendMessage(message);
			            			if(count==1){Set_off(bulb1_status);last_connect = 1;}
			            			if(count==2){Set_off(bulb2_status);last_connect = 2;}
			            			if(count==3){Set_off(bulb3_status);last_connect = 3;}
			            		}catch (Exception e){}
			            		try {Thread.sleep(500);} catch (Exception e) {}
		            		}//end for if(!bt_flag)
		            		}else{
		            			Toast.makeText(getApplicationContext(), "No Saves MAC Address for Bulb "+Integer.toString(count), Toast.LENGTH_LONG).show();
		            		}
		                }
		                }
		                Off.post(new Runnable(){
		                	public void run(){Off.setEnabled(true);}
		                });
		                bt_available = true;
		            }
		            		}).start();
            }});

        Off.setOnLongClickListener(new OnLongClickListener(){
        	public boolean onLongClick(View v){
				finishAndRemoveTask();
        		return true;
        	}
        });


        //	preference button is to set the setting to user's preference
        pre1 = (ImageButton)findViewById(R.id.mode1);
        pre1.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		intensity = sharedpreferences.getInt("int1", 300);
        		color = sharedpreferences.getInt("color1", 3300);
        		LightIntensity.setText(Integer.toString(intensity));
        		ColorTemperature.setText(Integer.toString(color));
        		seekbar_intensity.setProgress((intensity-min_int)/step_int);
        		seekbar_color.setProgress((color-min_int)/step_int);
        		int figure = (intensity*255)/max_int;
        		thread_counter = autoSend_time;
        	}
        });
        pre1_t.setText(sharedpreferences.getString("pre1", "Mode 1"));

        //	Long Pressed can save the current setting to preference
        pre1.setOnLongClickListener(new OnLongClickListener(){
        	public boolean onLongClick(View v){
        		Mode_setting(1);
        		return true;
        	}
        });

        //	Similar as above
        pre2 = (ImageButton)findViewById(R.id.mode2);
        pre2.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		intensity = sharedpreferences.getInt("int2", 300);
        		color = sharedpreferences.getInt("color2", 3300);
        		LightIntensity.setText(Integer.toString(intensity));
        		ColorTemperature.setText(Integer.toString(color));
        		seekbar_intensity.setProgress((intensity-min_int)/step_int);
        		seekbar_color.setProgress((color-min_color)/step_color);
        		int figure = (intensity*255)/max_int;
        		thread_counter = autoSend_time;
        	}
        });
        pre2_t.setText(sharedpreferences.getString("pre2", "Mode 2"));

        //	Similar as above
        pre2.setOnLongClickListener(new OnLongClickListener(){
        	public boolean onLongClick(View v){
        		Mode_setting(2);
        		return true;
        	}
        });

        //	Similar as above
        pre3 = (ImageButton)findViewById(R.id.mode3);
        pre3.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		intensity = sharedpreferences.getInt("int3", 300);
        		color = sharedpreferences.getInt("color3", 3300);
        		LightIntensity.setText(Integer.toString(intensity));
        		ColorTemperature.setText(Integer.toString(color));
        		seekbar_intensity.setProgress((intensity-min_int)/step_int);
        		seekbar_color.setProgress((color-min_color)/step_color);
        		int figure = (intensity*255)/max_int;
        		thread_counter = autoSend_time;
        	}
        });
        pre3_t.setText(sharedpreferences.getString("pre3", "Mode 3"));

        //	Similar as above
        pre3.setOnLongClickListener(new OnLongClickListener(){
        	public boolean onLongClick(View v){
        		Mode_setting(3);
        		return true;
        	}
        });

        Effect = (ImageButton)findViewById(R.id.effect);
        Effect.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){

        		TextView title = new TextView(context);
        		title.setText(R.string.effect);
        		title.setTextColor(getResources().getColor(R.color.white));
        		title.setBackgroundColor(getResources().getColor(R.color.background));
        		title.setTextSize(25);
        		title.setPadding(0,10,0,10);
        		title.setGravity(Gravity.CENTER);

        		AlertDialog.Builder effectDialogBuilder = new AlertDialog.Builder(context);
        		effectDialogBuilder.setCustomTitle(title)
						.setItems(R.array.effects, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Toast.makeText(getApplicationContext(), "Effect selected:"+Integer.toString(which), Toast.LENGTH_LONG).show();
								effect_no = which +1;
								//                    	//	New Thread to send data
                    	 		new Thread(new Runnable(){
								 @ Override
								public void run(){
									// get the required setting at start of the thread
									Boolean [] select = new Boolean[3];
									select[0] = bulb1_en;
									select[1] = bulb2_en;
									select[2] = bulb3_en;

									int v=0;
									if(select[0])v++;
									if(select[1])v++;
									if(select[2])v++;

									//	message is the data being sent out
									String message = "$990#000" + Integer.toString(effect_no);

									while(!bt_available){try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}}

									bt_available = false;

									//	initialize the Bulb being connected
									bulb_number=1;

									checkBT();

									//	this for loop is to connect different device and send data
									for(int count=1;count<4;count++){
										bt_flag = true;
										if(select[count-1]){	//check if the bulb is selected
																//if yes, get the MAC address
										if(count==1){MAC_address=sharedpreferences.getString("bulb1_mac", "");
													current_connect = 1;}
										if(count==2){MAC_address=sharedpreferences.getString("bulb2_mac", "");
													current_connect = 2;}
										if(count==3){MAC_address=sharedpreferences.getString("bulb3_mac", "");
													current_connect = 3;}
										//	Do the connection if the MAC address is not empty
										if(MAC_address!=""){
											if(current_connect!=last_connect){
												//	get the device
												String address = MAC_address;
												device = mBluetoothAdapter.getRemoteDevice(address);
												mChatService.connect(device);

												//	for loop to wait for connection
												//	device.getBondState() = 12 if the connection is success
												for(int i=1;i<15 || device.getBondState()!=device.BOND_BONDED;i++)
												{try {Thread.sleep(200);} catch (Exception e) {}}
											}	// end for if(current_connect!=last_connect)

											//if same as the last one but have not connected so try to connect again
											if(device.getBondState()!=device.BOND_BONDED){
												//get the device
												String address = MAC_address;
												device = mBluetoothAdapter.getRemoteDevice(address);
												mChatService.connect(device);

												//	for loop to wait for connection
												//	device.getBondState() = 12 if the connection is success
													for(int i=1;i<15 || device.getBondState()!=12;i++)



													{try {Thread.sleep(200);} catch (Exception e) {}}
											}//ebd for if(device.getBondState()!=device.BOND_BONDED)

											try{
											//send out the String
											String time_delay = Integer.toString(v*4000);
											sendMessage(message+"?"+time_delay);
											if(count==1){last_connect = 1;}
											if(count==2){last_connect = 2;}
											if(count==3){last_connect = 3;}
											v--;
											/*	This sendMessage function is predefined
											 * 	A string is passed and would be sent through BT
											 */
											}catch (Exception e){}
											//wait 1s to ensure data is sent out
											try {Thread.sleep(500);} catch (Exception e) {}

											if(device.getBondState()!=device.BOND_BONDED){
												try{
													//send out the String
													String time_delay = Integer.toString(v*4000);
													sendMessage(message+"?"+time_delay);
													if(count==1){last_connect = 1;}
													if(count==2){last_connect = 2;}
													if(count==3){last_connect = 3;}
													v--;
												}catch (Exception e){}
												//wait 0.5s to ensure data is sent out
												try {Thread.sleep(500);} catch (Exception e) {}}
											}
											else{
											Toast.makeText(getApplicationContext(), "No Saved MAC Address for Bulb "+Integer.toString(count), Toast.LENGTH_LONG).show();
											}
										}
									}
									Effect.post(new Runnable(){
										public void run(){Effect.setEnabled(true);
										}
									});
									bt_available = true;
								}
									}).start();
								}
						});

				AlertDialog alertDialog = effectDialogBuilder.create();
        		alertDialog.show();
        	}
        });

        Alarm = (ImageButton)findViewById(R.id.alarm);
        Alarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//call method at last call_alarm
				call_alarm();
			}
		});

        //	Initialize the TextView
        intensity_t1 = (TextView) findViewById(R.id.intensity_t1);
        LightIntensity = (TextView) findViewById(R.id.Display_Intensity);
        LightIntensity.setText(Integer.toString(intensity));
        color_t1 = (TextView) findViewById(R.id.color_t1);
        ColorTemperature = (TextView) findViewById(R.id.Display_ColorTemperature);
        ColorTemperature.setText(Integer.toString(color));

        sharedpreferences = getSharedPreferences(MyPreference,Context.MODE_PRIVATE);

        //	Initialize the TextView and get back last time data
        intensity_t = (TextView) findViewById(R.id.intensity_t);
        Int1 = (TextView) findViewById(R.id.Int1);
        Int1.setText(Integer.toString(sharedpreferences.getInt("Int1", 300)));
        Int2 = (TextView) findViewById(R.id.Int2);
        Int2.setText(Integer.toString(sharedpreferences.getInt("Int2", 300)));
        Int3 = (TextView) findViewById(R.id.Int3);
        Int3.setText(Integer.toString(sharedpreferences.getInt("Int3", 300)));
        color_t = (TextView) findViewById(R.id.color_t);
        Color1 = (TextView) findViewById(R.id.Color1);
        Color1.setText(Integer.toString(sharedpreferences.getInt("Color1", 3300)));
        Color2 = (TextView) findViewById(R.id.Color2);
        Color2.setText(Integer.toString(sharedpreferences.getInt("Color2", 3300)));
        Color3 = (TextView) findViewById(R.id.Color3);
        Color3.setText(Integer.toString(sharedpreferences.getInt("Color3", 3300)));
		color_R = (TextView) findViewById(R.id.rgb_r);
		color_R.setText(Integer.toString(sharedpreferences.getInt("R", 0)));
		color_G = (TextView) findViewById(R.id.rgb_g);
		color_G.setText(Integer.toString(sharedpreferences.getInt("G", 0)));
		color_B = (TextView) findViewById(R.id.rgb_b);
		color_B.setText(Integer.toString(sharedpreferences.getInt("B", 0)));

		//	Set the status upon the record
        status_t = (TextView) findViewById(R.id.status_t);
        bulb1_status = (TextView)findViewById(R.id.bulb1_on);
        if(sharedpreferences.getBoolean("bulb1_status", false)) {
        	bulb1_status.setText("On");
        	bulb1_status.setTextColor(Color.rgb(221, 221, 221));
        }
        bulb2_status = (TextView)findViewById(R.id.bulb2_on);
        if(sharedpreferences.getBoolean("bulb2_status", false)){
        	bulb2_status.setText("On");
        	bulb2_status.setTextColor(Color.rgb(221, 221, 221));
        }
        bulb3_status = (TextView)findViewById(R.id.bulb3_on);
        if(sharedpreferences.getBoolean("bulb3_status", false)){
        	bulb3_status.setText("On");
        	bulb3_status.setTextColor(Color.rgb(221, 221, 221));
        }
        //	Set back the user defined names for bulbs
        bulb1_t.setText(sharedpreferences.getString("bulb1_name", "bulb1"));
        bulb2_t.setText(sharedpreferences.getString("bulb2_name", "bulb2"));
        bulb3_t.setText(sharedpreferences.getString("bulb3_name", "bulb3"));
        bulb_t = (TextView) findViewById(R.id.bulb_t);
        table_bulb1 = (TextView)findViewById(R.id.table_bulb1);
        table_bulb2 = (TextView)findViewById(R.id.table_bulb2);
        table_bulb3 = (TextView)findViewById(R.id.table_bulb3);
        table_bulb1.setText(sharedpreferences.getString("bulb1_name", "bulb1"));
        table_bulb2.setText(sharedpreferences.getString("bulb2_name", "bulb2"));
        table_bulb3.setText(sharedpreferences.getString("bulb3_name", "bulb3"));
        //bulb_display = (ImageView) findViewById(R.id.bulb_display);

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothService(this, mHandler);

        // Initialize the buffer for outgoing messages	--predefined, not used
        mOutStringBuffer = new StringBuffer("");

        int figure = (intensity*255)/max_int;
		//bulb_display.setBackgroundColor(Color.rgb(figure, (int)(figure*0.9), 50));


		gap1 = (LinearLayout) findViewById(R.id.gap1);
		gap2 = (LinearLayout) findViewById(R.id.gap2);

		table = (TableLayout) findViewById(R.id.table);

		hideTableSwitch = (Switch) findViewById(R.id.mode_debug);
		if(!hideTable){
			table.setVisibility(View.GONE);
			gap1.setVisibility(View.VISIBLE);
			gap2.setVisibility(View.VISIBLE);
		}
		hideTableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					hideTable = false;
					table.setVisibility(View.VISIBLE);
					gap1.setVisibility(View.GONE);
					gap2.setVisibility(View.GONE);
				}
				else{
					hideTable = true;
					table.setVisibility(View.GONE);
					gap1.setVisibility(View.VISIBLE);
					gap2.setVisibility(View.VISIBLE);
				}
			}
		});


		current_cct = (TextView) findViewById(R.id.current_cct);
		current_flux = (TextView) findViewById(R.id.current_flux);

//		if(sharedpreferences.getBoolean("bulb1_select", false)){bulb1.performClick();}
//		if(sharedpreferences.getBoolean("bulb2_select", false)){bulb2.performClick();}
//		if(sharedpreferences.getBoolean("bulb3_select", false)){bulb3.performClick();}

		setTextSize(textSize);

		renameListener();

		//this thread is a non-stop counter
		//thread_counter value will be update in every listener controlling flux and cct
		new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){	if(thread_counter>0){thread_counter--;}
								if(thread_counter==1){if(autoSend)button_send.post(new Runnable(){
														public void run(){button_send.performClick();}
														});}
								try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		}).start();

		//this thread is not in use in current version
		new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){	if(thread_timeout_counter>0){thread_timeout_counter--;}
								else if(thread_timeout_counter==0){	thread_timeout_counter=-1;
																	if(autoDisableBT)mBluetoothAdapter.disable();}
								try {Thread.sleep(60000);} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		}).start();

		//this is for globalCounter to disable BT
		new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){	if(globalCounter>=0)globalCounter--;
								if(globalCounter==0)mBluetoothAdapter.disable();
								try {Thread.sleep(600);} catch (InterruptedException e) {}
				}
			}
		});
    }//end for setUpChat

	@Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }	//end for onPause

    @Override
    public void onStop() {
		bulb1_on = on_off_condition(bulb1_status);
		bulb2_on = on_off_condition(bulb2_status);
		bulb3_on = on_off_condition(bulb3_status);
		//	save down the bulb status on destroy
		Editor editor = sharedpreferences.edit();
		editor.putBoolean("bulb1_status", bulb1_on);
		editor.putBoolean("bulb2_status", bulb2_on);
		editor.putBoolean("bulb3_status", bulb3_on);
//    	editor.putBoolean("bulb1_select", bulb1_select);
//    	editor.putBoolean("bulb2_select", bulb2_select);
//    	editor.putBoolean("bulb3_select", bulb3_select);
		editor.putInt("intensity_memory", intensity);
		editor.putInt("color_memory", color);
		editor.putInt("size", textSize);
		editor.putInt("bt_off_time", bt_off_time);
		editor.putInt("autoSend_time", autoSend_time);
		editor.putBoolean("hide", hideTable);
		editor.putBoolean("bulb1_en",bulb1_en);
		editor.putBoolean("bulb2_en",bulb2_en);
		editor.putBoolean("bulb3_en",bulb3_en);
		editor.putBoolean("autoDisableBT", autoDisableBT);
		editor.putBoolean("autoSend", autoSend);
		editor.commit();
        super.onStop();
        Log.e(TAG, "-- ON STOP --");
    }	//end for onStop

    @Override
    public void onDestroy() {
    	//	to get the boolean of on/off
    	bulb1_on = on_off_condition(bulb1_status);
    	bulb2_on = on_off_condition(bulb2_status);
    	bulb3_on = on_off_condition(bulb3_status);
    	//	save down the bulb status on destroy
    	Editor editor = sharedpreferences.edit();
    	editor.putBoolean("bulb1_status", bulb1_on);
    	editor.putBoolean("bulb2_status", bulb2_on);
    	editor.putBoolean("bulb3_status", bulb3_on);
//    	editor.putBoolean("bulb1_select", bulb1_select);
//    	editor.putBoolean("bulb2_select", bulb2_select);
//    	editor.putBoolean("bulb3_select", bulb3_select);
    	editor.putInt("intensity_memory", intensity);
    	editor.putInt("color_memory", color);
    	editor.putInt("size", textSize);
    	editor.putInt("bt_off_time", bt_off_time);
    	editor.putInt("autoSend_time", autoSend_time);
    	editor.putBoolean("hide", hideTable);
    	editor.putBoolean("bulb1_en",bulb1_en);
    	editor.putBoolean("bulb2_en",bulb2_en);
    	editor.putBoolean("bulb3_en",bulb3_en);
    	editor.putBoolean("autoDisableBT", autoDisableBT);
    	editor.putBoolean("autoSend", autoSend);
    	editor.commit();
        super.onDestroy();
        if (mChatService != null) mChatService.stop();
        Log.e(TAG, "--- ON DESTROY ---");
    }	//end for onDestroy

    //	predefined method
    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    @Override
    public void onBackPressed(){
    	Ask_Exit();
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    //	predefined method
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //mOutEditText.setText(mOutStringBuffer);
        }
    }


    //	predefined -- not used
    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };


    //	predefined
    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:
                    btStatus.setText(R.string.title_connected_to);
					btStatus.append(mConnectedDeviceName);
                    bt_flag = true;
                    //mConversationArrayAdapter.clear();
                    break;
                case BluetoothService.STATE_CONNECTING:
					btStatus.setText(R.string.title_connecting);
                    break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:
					btStatus.setText(R.string.title_not_connected);
                    last_connect = 0;
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                thread_timeout_counter = bt_off_time;
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                Log.v(TAG,readMessage);
                //if(readMessage.length()>8){current_flux.setText(readMessage);}
				//Toast.makeText(getApplicationContext(), "Receiving Message", Toast.LENGTH_LONG).show();
				if(readMessage.length()>4){
					if(readMessage.charAt(0)=='R') {
						color_R.setText(readMessage.substring(1,5));
					}
					if(readMessage.charAt(0)=='G'){
						color_G.setText(readMessage.substring(1,5));
					}
					if(readMessage.charAt(0)=='B'){
						color_B.setText(readMessage.substring(1,5));
					}
				}
                if(readMessage.length()>8){
                	if(readMessage.charAt(0)=='#'){
                		current_flux.setText(readMessage.substring(1,4));
                	}
                	if(readMessage.charAt(4)=='$'){
                		current_cct.setText(readMessage.substring(5,9));
                	}
                	if(last_connect==1){
                		Int1.setText(current_flux.getText().toString());
                		Color1.setText(current_cct.getText().toString());
                	}
                	if(last_connect==2){
                		Int2.setText(current_flux.getText().toString());
                		Color2.setText(current_cct.getText().toString());
                	}
                	if(last_connect==3){
                		Int3.setText(current_flux.getText().toString());
                		Color3.setText(current_cct.getText().toString());
                	}
                }
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

	private TextView content;

    	//predefined	-	not used as this function never called in normal practice
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     	Log.d(TAG, "onActivityResult " + resultCode);


        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == RESULT_OK) {
            	//	This part is taken as reference for BT connection
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                MAC_address=data.getExtras()
                        .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                mChatService.connect(device);
                callConfiguration();
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable BlueTooth returns
            if (resultCode == RESULT_OK) {
                // BlueTooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable BlueTooth or an error occured
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
        case REQUEST_SETTING:
        	// When the request to setting returns
        	if (resultCode == RESULT_OK){
        		textSize = data.getIntExtra("size", 1);
        		setTextSize(textSize);
        		hideTable = data.getBooleanExtra("hide", false);
        		if(hideTable){
        			table.setVisibility(View.GONE);
        			hideTableSwitch.setChecked(false);
        		}
        		else{
        			table.setVisibility(View.VISIBLE);
        			hideTableSwitch.setChecked(true);
        		}
        		bulb1_en = data.getBooleanExtra("bulb1_en", true);
        		UpdateBulb(bulb1,bulb1_icon,bulb1_en);
        		bulb2_en = data.getBooleanExtra("bulb2_en", true);
        		UpdateBulb(bulb2,bulb2_icon,bulb2_en);
        		bulb3_en = data.getBooleanExtra("bulb3_en", true);
        		UpdateBulb(bulb3,bulb3_icon,bulb3_en);
        		autoDisableBT = data.getBooleanExtra("bt_auto_off", false);
        		bt_off_time = data.getIntExtra("bt_off_time", 3)*5;			//convert progress value to multiple of 5 minutes
        		thread_timeout_counter = bt_off_time;
        		autoSend = data.getBooleanExtra("bt_auto_send", false);
        		autoSend_time = data.getIntExtra("bt_send_time", 5) * 5;	//change to multiple of 0.2 seconds

        	}else if(resultCode == RESULT_CANCELED){
        		//do nothing
        	}
        	break;
        case REQUEST_RENAME:
        	if(resultCode == RESULT_OK){
        		table_bulb1.setText(data.getStringExtra("bulb1"));
        		table_bulb2.setText(data.getStringExtra("bulb2"));
        		table_bulb3.setText(data.getStringExtra("bulb3"));
        		bulb1_t.setText(table_bulb1.getText());
        		bulb2_t.setText(table_bulb2.getText());
        		bulb3_t.setText(table_bulb3.getText());
        		pre1_t.setText(data.getStringExtra("pre1"));
        		pre2_t.setText(data.getStringExtra("pre2"));
        		pre3_t.setText(data.getStringExtra("pre3"));
        		Editor editor = sharedpreferences.edit();
				editor.putString("bulb1_name",bulb1_t.getText().toString());
				editor.putString("bulb2_name",bulb2_t.getText().toString());
				editor.putString("bulb3_name",bulb3_t.getText().toString());
				editor.putString("pre1",pre1_t.getText().toString());
				editor.putString("pre2",pre2_t.getText().toString());
				editor.putString("pre3",pre3_t.getText().toString());
				editor.commit();
        	}
        	if(resultCode == RESULT_CANCELED){

        	}
        	break;
        case REQUEST_PWM:
        	if(resultCode == RESULT_OK){
        		final int pwm_warm = data.getIntExtra("pwm_warm", 128);
        		final int pwm_cool = data.getIntExtra("pwm_cool", 128);
        		button_send.setEnabled(false);
        		new Thread(new Runnable(){
           		 @ Override
           		public void run(){
           			 // get the required setting at start of the thread
           			Boolean [] select = new Boolean[3];
           			select[0] = bulb1_en;
           			select[1] = bulb2_en;
           			select[2] = bulb3_en;

           			// integers to save data being out
           			int intensity_s = pwm_warm;
           			int color_s = pwm_cool;
           			String warm_s;
           			String cool_s;

           			//convert all setting to 3 digits for PWM warm and 4 digits for PWM cool
           			if(intensity_s<10){warm_s = "00" + Integer.toString(intensity_s);}
           			else if (intensity_s<100){warm_s = '0' + Integer.toString(intensity_s);}
           			else {warm_s = Integer.toString(intensity_s);}

           			if(color_s<10){cool_s = "000" + Integer.toString(color_s);}
           			else if (color_s<100){cool_s = "00" + Integer.toString(color_s);}
           			else {cool_s = '0'+Integer.toString(color_s);}

           			//	message is the data being sent out
           			String message = 'P'+warm_s+'W'+cool_s;

           			while(!bt_available){try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}}

           			bt_available = false;

           			//	initialize the Bulb being connected
           			bulb_number=1;

           			//	this for loop is to connect different device and send data
					 for(int count=1;count<4;count++){
						 bt_flag = true;
						 if(select[count-1]){	//check if the bulb is selected
							 //if yes, get the MAC address
							 if(count==1){
								 MAC_address=sharedpreferences.getString("bulb1_mac", "");
								 current_connect = 1;
								 Log.v(TAG, "address: "+MAC_address);
							 }
							 if(count==2){	MAC_address=sharedpreferences.getString("bulb2_mac", "");
								 current_connect = 2;}
							 if(count==3){	MAC_address=sharedpreferences.getString("bulb3_mac", "");
								 current_connect = 3;}

							 //	Do the connection if the MAC address is not empty
							 if(MAC_address!=""){
								 //	get the device if the target device is not connecting
								 if(last_connect!=current_connect){
									 String address = MAC_address;
									 Log.v(TAG, "address: "+address);
									 device = mBluetoothAdapter.getRemoteDevice(address);

									 mChatService.connect(device);

									 //	for loop to wait for connection
									 //	device.getBondState() = 12 if the connection is success
									 for(int i=1;i<15 || device.getBondState()!=device.BOND_BONDED;i++)
									 {try {Thread.sleep(200);} catch (Exception e) {}}

//									 if(device.getBondState()!=device.BOND_BONDED){
//										 String address = MAC_address;
//										 device = mBluetoothAdapter.getRemoteDevice(address);
//										 mChatService.connect(device);
//
//										 //	for loop to wait for connection
//										 //	device.getBondState() = 12 if the connection is success
//										 for(int i=1;i<15 || device.getBondState()!=device.BOND_BONDED;i++)
//										 {try {Thread.sleep(300);} catch (Exception e) {} }
//									 }
								 }	//end for if(last_connect!=current_connect)

								 if(mChatService.getState() == BluetoothService.STATE_CONNECTED){
									 Log.v(TAG,"Bluetooth is currently connected to an device");

									 try{
										 //send out the String
										 sendMessage(message);
										 Log.v(TAG,"pwm send: "+message);
										 Toast.makeText(getApplicationContext(), "Send to device "+count, Toast.LENGTH_LONG).show();
										 /*	This sendMessage function is predefined
										  * 	A string is passed and would be sent through BT
										  */
										 if(count==1){	Setting(bulb1_status, Int1, Color1, intensity_s, color_s);
											 last_connect = 1;}
										 if(count==2){	Setting(bulb2_status, Int2, Color2, intensity_s, color_s);
											 last_connect = 2;}
										 if(count==3){	Setting(bulb3_status, Int3, Color3, intensity_s, color_s);
											 last_connect = 3;}
									 }catch (Exception e){}

									 //wait 0.5s to ensure data is sent out
									 try {
										 Thread.sleep(500);
									 }
									 catch (Exception e) {}
								 }
							 } else{
								 Toast.makeText(getApplicationContext(), "No Saved MAC Address for Bulb "+Integer.toString(count), Toast.LENGTH_LONG).show();
							 }

						 }

					 }

		                //	reset the condition of the button to be clickable
		                button_send.post(new Runnable(){
		                	public void run(){button_send.setEnabled(true);}
		                });

		                bt_available = true;
		            }
	            		}).start();
        	}
        }
    }


    	//This part is to set a new layout upon orientation change
   // unuse now, can be reuse if u have landscape layout
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            Log.d("Change as Portrait ","PPPPPPPPPPPPPPPPP");
            setContentView(R.layout.main);
            setupChat();
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Change as LandScape ","LLLLLLLLLLLLLLLLLLLL");
            setContentView(R.layout.main);
            setupChat();
        }

    }

    //	Function to change the setting to a String command for BT
    public String ToCommand(int mintensity, int mcolor){
    	String message;
    	if(mintensity<100){
        	message = "$0" + Integer.toString(mintensity) + "#" + Integer.toString(mcolor);
    	} else{
            message = "$" + Integer.toString(mintensity) + "#" + Integer.toString(mcolor);
    	}
    	return message;
    }

    //	Function to set  required TextView after send out data
    public void Setting(final TextView bulb_state, final TextView int_t, final TextView color_t,
    		final int int_s, final int color_s){
    	bulb_state.post(new Runnable(){
			public void run(){bulb_state.setText("On");
								bulb_state.setTextColor(Color.rgb(221, 221, 221));}});
		int_t.post(new Runnable(){
			public void run(){int_t.setText(Integer.toString(int_s));}});
		color_t.post(new Runnable(){
			public void run(){color_t.setText(Integer.toString(color_s));}});
    }

    //	Function to set required TextView after setting bulbs off
    public void Set_off(final TextView bulb_state){
    	bulb_state.post(new Runnable(){
			public void run(){	bulb_state.setText("Off");
								bulb_state.setTextColor(Color.rgb(41, 219, 21));}});
    }

    //	Function to check if the bulb is on/off
    public boolean on_off_condition(final TextView bulb_state){
    	if(bulb_state.getText().toString()=="On"){
    		return true;
    	}else{
    		return false;
    	}
    }

    public void Ask_Exit(){
    	LayoutInflater li = LayoutInflater.from(context);
		View offView = li.inflate(R.layout.exit,null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(offView);
		alertDialogBuilder.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				// To save the new name and renew required TextView
				Ask_off_BT();
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int id){
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
    }

    public void Ask_off_BT(){
    	LayoutInflater li = LayoutInflater.from(context);
		View offView = li.inflate(R.layout.off_bt,null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(offView);
		alertDialogBuilder.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				// To save the new name and renew required TextView
				new Thread(new Runnable(){
					public void run(){
						while(!bt_available){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).run();;
				mBluetoothAdapter.disable();
				finish();
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int id){
				finish();
			}
		});


		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
    }

    public void showUserMenu(){
    	LayoutInflater li = LayoutInflater.from(context);
		View menuView = li.inflate(R.layout.user_menu,null);

		menu_no = 1;

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(menuView);
		alertDialogBuilder.setCancelable(false)
		.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK &&event.getAction() == KeyEvent.ACTION_UP &&
		                !event.isCanceled()){
			dialog.cancel();
		}
				return false;
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();

		final TextView menu_no_dis = (TextView) menuView.findViewById(R.id.menu_no);
		ImageButton to_left = (ImageButton) menuView.findViewById(R.id.to_left);
		to_left.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(menu_no>1){menu_no--;}
				else menu_no = 6;
				menu_no_dis.setText(Integer.toString(menu_no));
				setContent(menu_no,content);
			}
		});

		ImageButton to_right = (ImageButton) menuView.findViewById(R.id.to_right);
		to_right.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(menu_no<6){menu_no++;}
				else menu_no = 1;
				menu_no_dis.setText(Integer.toString(menu_no));
				setContent(menu_no,content);
			}
		});

		TextView menuDismiss = (TextView) menuView.findViewById(R.id.dismiss);
		menuDismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.dismiss();
			}
		});

		content = (TextView) menuView.findViewById(R.id.content);
		content.setText("To configure a bulb:\n"
				+ "(1) search and connect a bulb\n"
				+ "(2) choose only 1 bulb\n"
				+ "(3) press 'save'");

		alertDialogBuilder.setTitle("How to use");
		alertDialogBuilder.setIcon(android.R.drawable.ic_menu_info_details);


		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
    }

	public void setContent(int no1,TextView con1){
		switch(no1){
		case 1:
			con1.setText("To configure a bulb:\n"
					+ "(1) Choose configuration in option menu\n"
					+ "(2) Search and connect the target light bulb\n"
					+ "(3) Choose which bulb it is");
			break;
		case 2:
			con1.setText("To control a bulb:\n"
					+ "(1) Select a bulb in user interface\n"
					+ "(2) Set the desire setting for color temperature and light intensity\n"
					+ "(3) Press 'Send'\n"
					+ "(4) Or wait for a while for auto-control.");
			break;
		case 3:
			con1.setText("To turn off a bulb:\n"
					+ "(1) Select a bulb in user interface\n"
					+ "(2) Press 'Off'\n"
					+ "(3) Or LONG PRESS 'Off' to set a alarm for turning off\n"
					+ "(4) Make sure your phone is in the area for auto turn-off");
			break;
		case 4:
			con1.setText("To make an effect:\n"
					+ "(1) Select a bulb in user interface\n"
					+ "(2) Press 'Effect'\n"
					+ "(3) Choose the desired effect\n"
					+ "(4) Stop the effect by interrupt");
			break;
		case 5:
			con1.setText("To modify setting:\n"
					+ "(1) Select 'Setting' in option-menu\n"
					+ "(2) Modify the setting for text sizes or bulbs-enabling\n"
					+ "(3) Press 'Set' to make changes");
			break;
		case 6:
			con1.setText("To rename bulbs or preferences:\n"
					+ "(1) Selec 'Rename' in option-menu\n"
					+ "(2) Change the name of the bulbs or preferences\n"
					+ "(3) Press 'OK' to make changes");
			break;
		}
	};

    public void callConfiguration(){
    	LayoutInflater li = LayoutInflater.from(context);
		View searchView = li.inflate(R.layout.device_search,null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(searchView);
		alertDialogBuilder.setCancelable(false)
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int id){
				dialog.cancel();
			}
		});

		final TextView MAC_dis = (TextView) searchView.findViewById(R.id.MAC_address);
		MAC_dis.setText(MAC_address);
		Button bulb1_connect = (Button) searchView.findViewById(R.id.bulb1_connect);
		bulb1_connect.setText(bulb1_t.getText().toString());
		Button bulb2_connect = (Button) searchView.findViewById(R.id.bulb2_connect);
		bulb2_connect.setText(bulb2_t.getText().toString());
		Button bulb3_connect = (Button) searchView.findViewById(R.id.bulb3_connect);
		bulb3_connect.setText(bulb3_t.getText().toString());

		alertDialogBuilder.setTitle("Configuration");
		alertDialogBuilder.setIcon(android.R.drawable.ic_menu_edit);

		final AlertDialog alertDialog = alertDialogBuilder.create();
		bulb1_connect.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Editor editor = sharedpreferences.edit();
        		editor.putString("bulb1_mac", MAC_address);
        		editor.commit();
        		last_connect = 1;
        		alertDialog.dismiss();
			}
		});

		bulb2_connect.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Editor editor = sharedpreferences.edit();
				editor.putString("bulb2_mac", MAC_address);
				editor.commit();
				last_connect = 2;
				alertDialog.dismiss();
			}
		});

		bulb3_connect.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Editor editor = sharedpreferences.edit();
				editor.putString("bulb3_mac", MAC_address);
				editor.commit();
				last_connect = 3;
				alertDialog.dismiss();
			}
		});

		alertDialog.show();


    }


    public void UpdateBulb(ImageButton bulb, ImageView icon, boolean en){
    	if(en){
			bulb.setImageResource(R.drawable.ic_bulb_on);
			icon.setImageResource(R.drawable.tick);
    	}else{
    		bulb.setImageResource(R.drawable.ic_bulb_off);
    		icon.setImageResource(R.drawable.cross);
    	}
    }

    public void Mode_setting(final int mode){
    	LayoutInflater li = LayoutInflater.from(context);
		View modeView = li.inflate(R.layout.mode_setting,null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(modeView);
		alertDialogBuilder.setCancelable(false);
//		.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//			public void onClick(DialogInterface dialog,int id){
//				dialog.cancel();
//			}
//		});

		Button mode_rename = (Button) modeView.findViewById(R.id.mode_rename);
		Button mode_set = (Button) modeView.findViewById(R.id.mode_set);
		Button mode_cancel = (Button) modeView.findViewById(R.id.mode_cancel);

		alertDialogBuilder.setTitle("Mode Setting");
		alertDialogBuilder.setIcon(android.R.drawable.ic_menu_edit);

		final AlertDialog alertDialog = alertDialogBuilder.create();
		mode_rename.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent renameScreen = new Intent (getApplicationContext(),rename.class);
	        	renameScreen.putExtra("bulb1", table_bulb1.getText());
	        	renameScreen.putExtra("bulb2", table_bulb2.getText());
	        	renameScreen.putExtra("bulb3", table_bulb3.getText());
	        	renameScreen.putExtra("pre1", pre1_t.getText().toString());
	        	renameScreen.putExtra("pre2", pre2_t.getText().toString());
	        	renameScreen.putExtra("pre3", pre3_t.getText().toString());
	        	startActivityForResult(renameScreen,4);
        		alertDialog.dismiss();
			}
		});

		mode_set.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Editor editor = sharedpreferences.edit();
				switch(mode){
				case 1:
                editor.putInt("int1",intensity);
                editor.putInt("color1", color);
                break;
				case 2:
				editor.putInt("int2",intensity);
	            editor.putInt("color2", color);
	            break;
				case 3:
				editor.putInt("int3",intensity);
	            editor.putInt("color3", color);
	            break;
				}
                editor.commit();
                Toast.makeText(getApplicationContext(), "Saved Preference", Toast.LENGTH_LONG).show();
				alertDialog.dismiss();
			}
		});

		mode_cancel.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				alertDialog.dismiss();
			}
		});

		alertDialog.show();
    }


    public void call_alarm(){
    	LayoutInflater li = LayoutInflater.from(context);
		View alarmView = li.inflate(R.layout.alarm,null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(alarmView);
		alertDialogBuilder.setCancelable(false);
//		.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//			public void onClick(DialogInterface dialog,int id){
//				dialog.cancel();
//			}
//		});

		final AlertDialog alertDialog = alertDialogBuilder.create();

		Button hour_add = (Button) alarmView.findViewById(R.id.hour_add);
		Button hour_minus = (Button) alarmView.findViewById(R.id.hour_minus);
		Button minute_add = (Button) alarmView.findViewById(R.id.minute_add);
		Button minute_minus = (Button) alarmView.findViewById(R.id.minute_minus);
		Button alarm_set = (Button) alarmView.findViewById(R.id.alarm_set);
		Button alarm_cancel = (Button) alarmView.findViewById(R.id.alarm_cancel);
		final TextView HOUR = (TextView) alarmView.findViewById(R.id.hour);
		final TextView MINUTE = (TextView) alarmView.findViewById(R.id.minute);
		final CheckBox alarm1 = (CheckBox) alarmView.findViewById(R.id.alarm1);
		final CheckBox alarm2 = (CheckBox) alarmView.findViewById(R.id.alarm2);
		final CheckBox alarm3 = (CheckBox) alarmView.findViewById(R.id.alarm3);
		alarm1.setText(bulb1_t.getText().toString());
		alarm2.setText(bulb2_t.getText().toString());
		alarm3.setText(bulb3_t.getText().toString());

		hour = 0;
		minute = 0;

		hour_add.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(hour!= 23)hour++;
				else hour = 0;
				HOUR.setText(Integer.toString(hour));
			}
		});

		hour_minus.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(hour==0)hour=23;
				else hour--;
				HOUR.setText(Integer.toString(hour));
			}
		});

		minute_add.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(minute!=59)minute++;
				else minute = 0;
				MINUTE.setText(Integer.toString(minute));
			}
		});

		minute_minus.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(minute==0)minute=59;
				else minute--;
				MINUTE.setText(Integer.toString(minute));
			}
		});

		alarm_set.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				alertDialog.dismiss();
			if(!alarm){
				final boolean alarm1_select = alarm1.isChecked();
				final boolean alarm2_select = alarm2.isChecked();
				final boolean alarm3_select = alarm3.isChecked();
				alarm = true;
			new Thread (new Runnable(){
				public void run(){
					int counter = hour*60 + minute;
					while(true){
						if(counter>0)counter--;
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(counter==0)break;
					}
					Off.post(new Runnable(){
						public void run(){
						Off.setEnabled(false);
						}
					});
					new Thread(new Runnable(){
	            		 @ Override
	            		public void run(){
	                // Send a message using content of the edit text widget
	            			Boolean [] select = new Boolean[3];
	            			select[0] = alarm1_select;
	            			select[1] = alarm2_select;
	            			select[2] = alarm3_select;
	            			String message;
	            			//	fixed format to turn the bulb off
			            	message = "$010#1000";
			            	/*if(!mBluetoothAdapter.isEnabled()){
			                	mBluetoothAdapter.enable();
			                //	This thread is a waiting thread
			                //	It check if the BT is enabled 0.5s once, MAX waiting time is 10s
			                	for(int i=1;i<20 && !mBluetoothAdapter.isEnabled();i++){
		                	    	try {
		                	      Thread.sleep(300);
		                	    } catch (Exception e) {}

		                	    														}
			                last_connect = 0;
			                }*/
			            	checkBT();
			            	if (mChatService != null) {
			                    // Only if the state is STATE_NONE, do we know that we haven't started already
			                    if (mChatService.getState() == BluetoothService.STATE_NONE) {
			                      // Start the Bluetooth chat services
			                      mChatService.start();
			                      try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			                    }
			                }
			                while(!bt_available){
			                	try {
									Thread.sleep(10000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			                }
			                bt_available = false;
			                bulb_number=1;
			                //BluetoothDevice device = null;
			                for(int count=1;count<4;count++){
			                	bt_flag = true;
			                	if(select[count-1]){
			                	if(count==1){MAC_address=sharedpreferences.getString("bulb1_mac", "");
			                				current_connect = 1;}
			                	if(count==2){MAC_address=sharedpreferences.getString("bulb2_mac", "");
			                				current_connect = 2;}
			                	if(count==3){MAC_address=sharedpreferences.getString("bulb3_mac", "");
			                				current_connect = 3;}
			            		if(MAC_address!=""){
			            			if(current_connect!=last_connect){
			            			String address = MAC_address;
			            			device = mBluetoothAdapter.getRemoteDevice(address);
			            			mChatService.connect(device);
			            		for(int i=1;i<15 || device.getBondState()!=device.BOND_BONDED;i++){
			            		try {
			            		      Thread.sleep(300);
			            		      // Do nothing
			            		    } catch (Exception e) {
			            		    }
			            		}
			            			}
			            			if(device.getBondState()!=device.BOND_BONDED){
			            				String address = MAC_address;
				            			device = mBluetoothAdapter.getRemoteDevice(address);
				            			mChatService.connect(device);
				            		for(int i=1;i<15 || device.getBondState()!=12;i++){
				            		try {
				            		      Thread.sleep(200);
				            		      // Do nothing
				            		    } catch (Exception e) {
				            		    }
				            		}
			            			}
			            		try{
			            			sendMessage(message);
			            			if(count==1){	Set_off(bulb1_status);
			            							last_connect = 1;}
			            			if(count==2){	Set_off(bulb2_status);
			            							last_connect = 2;}
			            			if(count==3){	Set_off(bulb3_status);
			            							last_connect = 3;}
			            		}catch (Exception e){
			            		}
			            		try {
			          		      Thread.sleep(1000);
			          		      // Do nothing
			          		    } catch (Exception e) {}
			            		if(!bt_flag){
			            			try{
				            			sendMessage(message);
				            			if(count==1){	Set_off(bulb1_status);
				            							last_connect = 1;}
				            			if(count==2){	Set_off(bulb2_status);
				            							last_connect = 2;}
				            			if(count==3){	Set_off(bulb3_status);
				            							last_connect = 3;}
				            		}catch (Exception e){
				            		}
				            		try {
				          		      Thread.sleep(1000);
				          		      // Do nothing
				          		    } catch (Exception e) {}
			            		}
			            		}else{
			            			Toast.makeText(getApplicationContext(), "No Saves MAC Address for Bulb "+Integer.toString(count), Toast.LENGTH_LONG).show();
			            		}
			                }
			                }
			                Off.post(new Runnable(){
			                	public void run(){
			                		Off.setEnabled(true);
			                	}
			                });
			                bt_available = true;
			                alarm = false;
			            }
			            		}).start();
				}
			}).start();
			}else{
				Toast.makeText(getApplicationContext(), "There is already an alarm running", Toast.LENGTH_LONG).show();
			}


			}
		});

		alarm_cancel.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				alertDialog.dismiss();
			}
		});

		alertDialog.show();

    }

    //checkBT is used for checking the status of BT, and enabling it if necessary
    public void checkBT(){
    	if(!mBluetoothAdapter.isEnabled()){
    		mBluetoothAdapter.enable();
    		 new Thread(new Runnable() {
           	  @Override
           	  public void run() {
           	    for(int i=1;i<20 && !mBluetoothAdapter.isEnabled();i++){
           	    	try {Thread.sleep(300);} catch (Exception e) {}
           	    }
           	  }
           }).run();

           last_connect = 0;
    	}
    }// end of check BT

    public void setTextSize(int textSize){
    	bulb_t.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	table_bulb1.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	table_bulb2.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	table_bulb3.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	status_t.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	bulb1_status.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	bulb2_status.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	bulb3_status.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	intensity_t.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	Int1.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	Int2.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	Int3.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	color_t.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	Color1.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	Color2.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	Color3.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	intensity_t1.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	LightIntensity.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	color_t1.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);
    	ColorTemperature.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize*2+5);

    }

    //Rename of bulbs and mode
    public void renameListener(){
		pre1_t.setOnLongClickListener(
				new OnLongClickListener(){
					@Override
					public boolean onLongClick(View v){
						renameText(pre1_t,modeList[0],false,null);
						return false;
					}
				}
		);

		pre2_t.setOnLongClickListener(
				new OnLongClickListener(){
					@Override
					public boolean onLongClick(View v){
						renameText(pre2_t,modeList[1],false,null);
						return false;
					}
				}
		);

		pre3_t.setOnLongClickListener(
				new OnLongClickListener(){
					@Override
					public boolean onLongClick(View v){
						renameText(pre3_t,modeList[2],false,null);
						return false;
					}
				}
		);

		bulb1_t.setOnLongClickListener(
				new OnLongClickListener(){
					@Override
					public boolean onLongClick(View v){
						renameText(bulb1_t,bulbList[0],true,table_bulb1);
						return false;
					}
				}
		);

		bulb2_t.setOnLongClickListener(
				new OnLongClickListener(){
					@Override
					public boolean onLongClick(View v){
						renameText(bulb2_t,bulbList[1],true,table_bulb2);
						return false;
					}
				}
		);

		bulb3_t.setOnLongClickListener(
				new OnLongClickListener(){
					@Override
					public boolean onLongClick(View v){
						renameText(bulb3_t,bulbList[2],true,table_bulb3);
						return false;
					}
				}
		);

	}

    public void renameText(final TextView oldNameText, final String systemName, final Boolean isBulb, final TextView table_name){
    	AlertDialog.Builder renameBuilder = new AlertDialog.Builder(context);

    	LayoutInflater inflater = LayoutInflater.from(context);
    	View prompts = inflater.inflate(R.layout.rename_dialog,null);
    	renameBuilder.setView(prompts);
		final EditText newName = (EditText) prompts.findViewById(R.id.new_name);
		TextView oldName = (TextView) prompts.findViewById(R.id.old_name);
		oldName.setText(oldNameText.getText().toString());

		renameBuilder.setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if("".equals(newName.getText().toString().trim())){
							//check if empty, if it is empty, do nothing
						}else{
							String name = newName.getText().toString().trim();
							oldNameText.setText(name);
							Editor editor = sharedpreferences.edit();
							editor.putString(systemName,name);
							editor.commit();
							if(isBulb){
								table_name.setText(name);
							}
						}
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = renameBuilder.create();
		alertDialog.show();
	}
}