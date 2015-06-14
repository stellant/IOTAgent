package com.wipro.iot.sensors;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/***
 * The IOT Agent is an android application used to read sensor data from android mobile and upload to the
 * Axeda Platform
 *
 * @author TH303898
 * @version 1.0
 * @since 2015-06-06
 */


public class MainActivity2 extends Activity implements SensorEventListener{

    //GPS and Network Boolean Variables
    private boolean gpsEnabled = false;
    private boolean networkEnabled = false;

    //Accelerometer and Magnetic Field Boolean Variables
    private boolean acc = false;
    private boolean mag = false;

    //Variables for Serial and Interval
    private String serialVariable = "";
    private int intervalVariable = 500;

    //Calendar and SimpleDateFormat
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    //ImageView
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView imageView6;
    private ImageView imageView7;
    private ImageView imageView8;

    //TextView for Labels
    private TextView location;
    private TextView accelerometer;
    private TextView gyroscope;
    private TextView magneticfield;
    private TextView orientation;
    private TextView proximity;
    private TextView datetime;
    private TextView wifinetwork;
    private TextView dateTime;

    //TextViews for Location
    private TextView locationLatitude1;
    private TextView locationLatitude2;
    private TextView locationLongitude1;
    private TextView locationLongitude2;
    private TextView locationAltitude1;
    private TextView locationAltitude2;
    private TextView locationAccuracy1;
    private TextView locationAccuracy2;
    private TextView locationProvider1;
    private TextView locationProvider2;

    //TextViews for Accelerometer
    private TextView accelerometerPower1;
    private TextView accelerometerPower2;
    private TextView accelerometerX1;
    private TextView accelerometerX2;
    private TextView accelerometerY1;
    private TextView accelerometerY2;
    private TextView accelerometerZ1;
    private TextView accelerometerZ2;
    private TextView accelerometerS1;
    private TextView accelerometerS2;

    //TextViews for MagneticField
    private TextView magneticfieldPower1;
    private TextView magneticfieldPower2;
    private TextView magneticfieldX1;
    private TextView magneticfieldX2;
    private TextView magneticfieldY1;
    private TextView magneticfieldY2;
    private TextView magneticfieldZ1;
    private TextView magneticfieldZ2;
    private TextView magneticfieldS1;
    private TextView magneticfieldS2;

    //TextView for Proximity
    private TextView proximityPower1;
    private TextView proximityPower2;
    private TextView proximityDistance1;
    private TextView proximityDistance2;

    //TextView for Gyroscope
    private TextView gyroscopePower1;
    private TextView gyroscopePower2;
    private TextView gyroscopeX1;
    private TextView gyroscopeX2;
    private TextView gyroscopeY1;
    private TextView gyroscopeY2;
    private TextView gyroscopeZ1;
    private TextView gyroscopeZ2;
    private TextView gyroscopeS1;
    private TextView gyroscopeS2;

    //TextView for Orientation
    private TextView orientationX1;
    private TextView orientationX2;
    private TextView orientationY1;
    private TextView orientationY2;
    private TextView orientationZ1;
    private TextView orientationZ2;

    //TextView for Wifi Network
    private TextView wifiNetworkName1;
    private TextView wifiNetworkName2;
    private TextView wifiNetworkStrength1;
    private TextView wifiNetworkStrength2;

    //Start and Stop Buttons
    private Button start,stop;

    //Autocomplete Textview
    private AutoCompleteTextView urlTextView;

    //Sensor Manager for Accelerometer, Gyroscope, Orientation, Proximity
    private SensorManager sensorManager;

    //ConnectivityManager and NetworkInfo for Network and Wifi State Change
    private ConnectivityManager connectivityManager;
    private NetworkInfo network;
    private IntentFilter wifiNetworkStateChangeFilter;
    private IntentFilter wifiSignalStrengthFilter;

    //WifiManager and WifiInfo for Wifi
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    //TelephonyManager for Network
    private TelephonyManager telephonyManager;

    //LocationManager and LocationListener and LocationProviderChangeIntentFilter for Location
    private LocationManager locationManager;
    private GPSNetworkLocation gpsNetworkLocation;

    //WifiNetworkStateChange BroadcastReceiver
    private WifiNetworkStateChange wifiNetworkStateChange;

    //WifiSignalStrength BroadcastReceiver
    private WifiSignalStrength wifiSignalStrength;

    //NetworkSignalStrength PhoneStateListener
    private NetworkSignalStrength networkSignalStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        //Create Calendar and SimpleDateFormat Objects
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

        //Creating ImageView Objects
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView7 = (ImageView) findViewById(R.id.imageView7);
        imageView8 = (ImageView) findViewById(R.id.imageView8);

        //Creating Objects for Sensors Titles
        location=(TextView)findViewById(R.id.textView1);
        accelerometer=(TextView)findViewById(R.id.textView2);
        gyroscope=(TextView)findViewById(R.id.textView3);
        magneticfield=(TextView)findViewById(R.id.textView4);
        orientation=(TextView)findViewById(R.id.textView5);
        proximity=(TextView)findViewById(R.id.textView6);
        datetime=(TextView)findViewById(R.id.textView7);
        wifinetwork=(TextView)findViewById(R.id.textView8);
        dateTime = (TextView) findViewById(R.id.textView71);

        //Location TextViews
        locationLatitude1 = (TextView)findViewById(R.id.textView111);
        locationLatitude2 = (TextView)findViewById(R.id.textView112);
        locationLongitude1 = (TextView)findViewById(R.id.textView121);
        locationLongitude2 = (TextView)findViewById(R.id.textView122);
        locationAltitude1 = (TextView)findViewById(R.id.textView131);
        locationAltitude2 = (TextView)findViewById(R.id.textView132);
        locationAccuracy1 = (TextView)findViewById(R.id.textView141);
        locationAccuracy2 = (TextView)findViewById(R.id.textView142);
        locationProvider1 = (TextView)findViewById(R.id.textView151);
        locationProvider2 = (TextView)findViewById(R.id.textView152);

        //accelerometer TextViews
        accelerometerPower1 = (TextView)findViewById(R.id.textView211);
        accelerometerPower2 = (TextView)findViewById(R.id.textView212);
        accelerometerX1 = (TextView)findViewById(R.id.textView221);
        accelerometerX2 = (TextView)findViewById(R.id.textView222);
        accelerometerY1 = (TextView)findViewById(R.id.textView231);
        accelerometerY2 = (TextView)findViewById(R.id.textView232);
        accelerometerZ1 = (TextView)findViewById(R.id.textView241);
        accelerometerZ2 = (TextView)findViewById(R.id.textView242);
        accelerometerS1 = (TextView)findViewById(R.id.textView251);
        accelerometerS2 = (TextView)findViewById(R.id.textView252);

        //Magnetic Field TextViews
        magneticfieldPower1 = (TextView)findViewById(R.id.textView411);
        magneticfieldPower2 = (TextView)findViewById(R.id.textView412);
        magneticfieldX1 = (TextView)findViewById(R.id.textView421);
        magneticfieldX2 = (TextView)findViewById(R.id.textView422);
        magneticfieldY1 = (TextView)findViewById(R.id.textView431);
        magneticfieldY2 = (TextView)findViewById(R.id.textView432);
        magneticfieldZ1 = (TextView)findViewById(R.id.textView441);
        magneticfieldZ2 = (TextView)findViewById(R.id.textView442);
        magneticfieldS1 = (TextView)findViewById(R.id.textView451);
        magneticfieldS2 = (TextView)findViewById(R.id.textView452);

        //Proximity TextViews
        proximityPower1 = (TextView)findViewById(R.id.textView611);
        proximityPower2 = (TextView)findViewById(R.id.textView612);
        proximityDistance1 = (TextView)findViewById(R.id.textView621);
        proximityDistance2 = (TextView)findViewById(R.id.textView622);

        //Gyroscope TextViews
        gyroscopePower1 = (TextView) findViewById(R.id.textView311);
        gyroscopePower2 = (TextView) findViewById(R.id.textView312);
        gyroscopeX1 = (TextView) findViewById(R.id.textView321);
        gyroscopeX2 = (TextView) findViewById(R.id.textView322);
        gyroscopeY1 = (TextView) findViewById(R.id.textView331);
        gyroscopeY2 = (TextView) findViewById(R.id.textView332);
        gyroscopeZ1 = (TextView) findViewById(R.id.textView341);
        gyroscopeZ2 = (TextView) findViewById(R.id.textView342);
        gyroscopeS1 = (TextView) findViewById(R.id.textView351);
        gyroscopeS2 = (TextView) findViewById(R.id.textView352);

        //Orientation TextViews
        orientationX1 = (TextView) findViewById(R.id.textView511);
        orientationX2 = (TextView) findViewById(R.id.textView512);
        orientationY1 = (TextView) findViewById(R.id.textView521);
        orientationY2 = (TextView) findViewById(R.id.textView522);
        orientationZ1 = (TextView) findViewById(R.id.textView531);
        orientationZ2 = (TextView) findViewById(R.id.textView532);


        //Wifi Network TextViews
        wifiNetworkName1 = (TextView)findViewById(R.id.textView811);
        wifiNetworkName2 = (TextView)findViewById(R.id.textView812);
        wifiNetworkStrength1 = (TextView)findViewById(R.id.textView821);
        wifiNetworkStrength2 = (TextView)findViewById(R.id.textView822);

        //Creating SensorManager Object
        sensorManager=(SensorManager)this.getSystemService(SENSOR_SERVICE);

        //Creating wifiNetworkStateChangeIntentFilter
        wifiNetworkStateChangeFilter = new IntentFilter();
        wifiNetworkStateChangeFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        //Creating WifiSignalStrength IntentFilter
        wifiSignalStrengthFilter = new IntentFilter();
        wifiSignalStrengthFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);

        //Creating WifiNetworkStateChange BroadcastReceiver
        wifiNetworkStateChange = new WifiNetworkStateChange();

        //Creating WifiSignalStrength BroadcastReceiver
        wifiSignalStrength = new WifiSignalStrength();

        //Creating NetworkSignalStrength PhoneStateListener
        networkSignalStrength = new NetworkSignalStrength();

        //Creating WifiManager Object
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        //Creating TelephonyManager Object
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);


        //Creating ConnectivityManager and NetworkInfo Object
        connectivityManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network=connectivityManager.getActiveNetworkInfo();
        if( network!=null && network.getType()==ConnectivityManager.TYPE_WIFI && network.isConnected() ) {
            wifinetwork.setText("WIFI ON");
            imageView8.setImageResource(R.drawable.wifion);
            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                wifiNetworkName1.setText("  SSID");
                wifiNetworkStrength1.setText("  Strength (dBm)");
                wifiNetworkName2.setText(wifiInfo.getSSID().replace("\"","").toUpperCase()+"  ");
                wifiNetworkStrength2.setText(wifiInfo.getRssi()+"  ");
            }
        }
        else if(network!=null && network.getType()==ConnectivityManager.TYPE_MOBILE && network.isConnected())
        {
            wifinetwork.setText("NETWORK ON");
            imageView8.setImageResource(R.drawable.antennaon);
            if(telephonyManager!=null)
            {
                wifiNetworkName1.setText("  Network");
                wifiNetworkStrength1.setText("  Strength (dBm)");
                wifiNetworkName2.setText(telephonyManager.getNetworkOperatorName()+"  ");
            }
        }
        else
        {
            if(network!=null)
                network=null;
            wifinetwork.setText("NO WIFI AND NETWORK");
            imageView8.setImageResource(R.drawable.nonetwork);
            wifiNetworkName1.setText("");
            wifiNetworkStrength1.setText("");
            wifiNetworkName2.setText("");
            wifiNetworkStrength2.setText("");
        }

        //Creating LocationManager Object
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        registerLocation();
        dateTime.setText(simpleDateFormat.format(calendar.getTime()));
        registerListeners();

        //Autocomplete TextView
        urlTextView = (AutoCompleteTextView)findViewById(R.id.Autocomplete1);
        urlTextView.setHint("Enter Axeda Server Url");
        //Start and Stop Buttons
        start = (Button)findViewById(R.id.Button01);
        stop = (Button)findViewById(R.id.Button02);
        stop.setEnabled(false);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String url = urlTextView.getText().toString();
                if(!((url.compareTo("") != 0) && Patterns.WEB_URL.matcher(url).matches()))
                {
                    return;
                }
                initiatePopup();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //  To show Popup
    private void initiatePopup()
    {
            LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popUp = layoutInflater.inflate(R.layout.popup_main,null);
            final PopupWindow popupWindow = new PopupWindow(popUp, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,true);
            popupWindow.showAtLocation(popUp, Gravity.CENTER, 0, 0);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            final EditText serial = (EditText)popUp.findViewById(R.id.popupEditText1);
            final EditText interval = (EditText)popUp.findViewById(R.id.popupEditText2);
            final Button ok = (Button) popUp.findViewById(R.id.popupButton1);
            ok.setOnClickListener(new View.OnClickListener()
                                  {
                                      @Override
                                      public void onClick(View v)
                                      {
                                          serialVariable = serial.getText().toString();
                                          String intervalTemp = interval.getText().toString();
                                          if(serialVariable.trim().length() > 0)
                                          {
                                              if(intervalTemp.trim().length() > 0)
                                              {
                                                  intervalVariable = Integer.parseInt(intervalTemp);
                                              }
                                              else
                                              {
                                                  intervalVariable = 500;
                                              }
                                              popupWindow.dismiss();
                                              start.setEnabled(false);
                                              stop.setEnabled(true);
                                          }
                                      }
                                  }
            );
            final Button cancel = (Button) popUp.findViewById(R.id.popupButton2);
            cancel.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v) {

                    popupWindow.dismiss();
                }
            });

    }

    public void registerLocation()
    {
        gpsNetworkLocation = new GPSNetworkLocation();
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(!gpsEnabled && !networkEnabled)
        {
            location.setText("LOCATION PROVIDERS OFF");
            imageView1.setImageResource(R.drawable.locationoff);
            locationLatitude1.setText("");
            locationLatitude2.setText("");
            locationLongitude1.setText("");
            locationLongitude2.setText("");
            locationAltitude1.setText("");
            locationAltitude2.setText("");
            locationAccuracy1.setText("");
            locationAccuracy2.setText("");
            locationProvider1.setText("");
            locationProvider2.setText("");
        }
        else
        {
            if(connectivityManager==null)
            {
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            }
            network = connectivityManager.getActiveNetworkInfo();
            locationLatitude1.setText("  Latitude");
            locationLongitude1.setText("  Longitude");
            locationAltitude1.setText("  Altitude");
            locationAccuracy1.setText("  Accuracy");
            locationProvider1.setText("  Provider");
            if(networkEnabled/* && network.isConnected()*/)
            {
                location.setText("LOCATION");
                imageView1.setImageResource(R.drawable.locationon);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10,10,gpsNetworkLocation);
            }
            /*else if(networkEnabled && !network.isConnected())
            {
                location.setText("LOCATION");
                Toast.makeText(this,"LOCATION NETWORK PROVIDER NEEDS WIFI OR NETWORK",Toast.LENGTH_LONG).show();

            }*/
            if(gpsEnabled)
            {
                location.setText("LOCATION");
                imageView1.setImageResource(R.drawable.locationon);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10,gpsNetworkLocation);
            }
        }
    }

    public void registerListeners()
    {
        registerReceiver(wifiNetworkStateChange,wifiNetworkStateChangeFilter);
        accelerometer.setText("ACCELEROMETER NOT FOUND");
        gyroscope.setText("GYROSCOPE NOT FOUND");
        magneticfield.setText("MAGNETIC FIELD NOT FOUND");
        orientation.setText("ORIENTATION NOT FOUND");
        proximity.setText("PROXIMITY NOT FOUND");
        accelerometerPower1.setText("");
        accelerometerPower2.setText("");
        accelerometerX1.setText("");
        accelerometerX2.setText("");
        accelerometerY1.setText("");
        accelerometerY2.setText("");
        accelerometerZ1.setText("");
        accelerometerZ2.setText("");
        accelerometerS1.setText("");
        accelerometerS2.setText("");
        magneticfieldPower1.setText("");
        magneticfieldPower2.setText("");
        magneticfieldX1.setText("");
        magneticfieldX2.setText("");
        magneticfieldY1.setText("");
        magneticfieldY2.setText("");
        magneticfieldZ1.setText("");
        magneticfieldZ2.setText("");
        magneticfieldS1.setText("");
        magneticfieldS2.setText("");
        proximityPower1.setText("");
        proximityPower2.setText("");
        proximityDistance1.setText("");
        proximityDistance2.setText("");
        gyroscopePower1.setText("");
        gyroscopePower2.setText("");
        gyroscopeX1.setText("");
        gyroscopeX2.setText("");
        gyroscopeY1.setText("");
        gyroscopeY2.setText("");
        gyroscopeZ1.setText("");
        gyroscopeZ2.setText("");
        gyroscopeS1.setText("");
        gyroscopeS2.setText("");
        orientationX1.setText("");
        orientationX2.setText("");
        orientationY1.setText("");
        orientationY2.setText("");
        orientationZ1.setText("");
        orientationZ2.setText("");
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(int i = 0;i < sensors.size(); i++)
        {
            if(sensors.get(i).getType()==Sensor.TYPE_ACCELEROMETER)
            {
                sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
                accelerometer.setText("ACCELEROMETER");
                accelerometerPower1.setText("  Power (mA)");
                accelerometerX1.setText("  X (m/s2)");
                accelerometerY1.setText("  Y (m/s2)");
                accelerometerZ1.setText("  Z (m/s2)");
                accelerometerS1.setText("  S (m/s2)");
                imageView2.setImageResource(R.drawable.accelerometeron);
                acc = true;
            }
            if(sensors.get(i).getType()==Sensor.TYPE_GYROSCOPE)
            {
                sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_NORMAL);
                gyroscope.setText("GYROSCOPE");
                gyroscopePower1.setText("  Power (mA)");
                gyroscopeX1.setText("  X (rad/s)");
                gyroscopeY1.setText("  Y (rad/s)");
                gyroscopeZ1.setText("  Z (rad/s)");
                gyroscopeS1.setText("  S (rad/s)");
                imageView3.setImageResource(R.drawable.gyroscopeon);
            }
            if(sensors.get(i).getType()==Sensor.TYPE_MAGNETIC_FIELD)
            {
                sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
                magneticfield.setText("MAGNETIC FIELD");
                magneticfieldPower1.setText("  Power (mA)");
                magneticfieldX1.setText("  X (?T)");
                magneticfieldY1.setText("  Y (?T)");
                magneticfieldZ1.setText("  Z (?T)");
                magneticfieldS1.setText("  S (?T)");
                imageView4.setImageResource(R.drawable.magneton);
                mag = true;
            }

            //Deprecated in API Level 20
            /*if(sensors.get(i).getType()==Sensor.TYPE_ORIENTATION)
            {
                sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_NORMAL);
                orientation.setText("ORIENTATION");
                imageView5.setImageResource(R.drawable.orientationon);
            }
            */
            if(sensors.get(i).getType()==Sensor.TYPE_PROXIMITY)
            {
                sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),SensorManager.SENSOR_DELAY_NORMAL);
                proximity.setText("PROXIMITY");
                proximityPower1.setText("  Power (mA)");
                proximityDistance1.setText("  Dist. (cm)");
                imageView6.setImageResource(R.drawable.proximityon);
            }

        }
        if(acc && mag)
        {
            orientation.setText("ORIENTATION");
            orientationX1.setText("  X (Deg.)");
            orientationY1.setText("  Y (Deg.)");
            orientationZ1.setText("  Z (Deg.)");
            imageView5.setImageResource(R.drawable.orientationon);
        }

    }

    //Array Variables to hold Accelerometer and Magnetic Field Values for Orientation
    float[] acce;
    float[] magn;

    //Variables used for getRotationMatrix
    float[] R1 = new float[9]; //Rotation
    float[] I1 = new float[9]; //Inclination

    //Variables to save Orientation Values
    float[] O = new float[3];

    @Override
    public void onSensorChanged(SensorEvent event) {


            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            {
                acce = event.values;
                calendar = Calendar.getInstance();
                dateTime.setText(simpleDateFormat.format(calendar.getTime()));
                setAccelerometer(event,event.sensor.getPower());
            }
            if(event.sensor.getType()==Sensor.TYPE_PROXIMITY)
            {
                setProximity(event, event.sensor.getPower());
            }
            if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
            {
                magn = event.values;
                setMagneticField(event, event.sensor.getPower());
            }
            if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE)
            {
                setGyroscope(event,event.sensor.getPower());
            }
            if(acce != null && magn != null)
            {
                if(SensorManager.getRotationMatrix(R1,I1,acce,magn))
                {
                    SensorManager.getOrientation(R1,O);
                    orientationZ2.setText(O[0]+"  ");
                    orientationX2.setText(O[1]+"  ");
                    orientationY2.setText(O[2]+"  ");
                }
            }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        acc = false;
        mag = false;
        registerListeners();
        registerLocation();
    }
    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
        try {
            unregisterReceiver(wifiNetworkStateChange);
        }
        catch(Exception e)
        {
            Log.d("Registration Error", e.getMessage());
        }
        locationManager.removeUpdates(gpsNetworkLocation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        try {
            unregisterReceiver(wifiNetworkStateChange);
        }
        catch(Exception e)
        {
            Log.d("Registration Error", e.getMessage());
        }
        locationManager.removeUpdates(gpsNetworkLocation);
        try
        {
            telephonyManager.listen(networkSignalStrength, PhoneStateListener.LISTEN_NONE);
            unregisterReceiver(wifiSignalStrength);
        }
        catch (Exception ex)
        {
            Log.d("Already Registered",ex.getMessage());
        }
    }
    private void setAccelerometer(SensorEvent event, float power)
    {
        accelerometerPower2.setText(power+"  ");
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        accelerometerX2.setText(x + "  ");
        accelerometerY2.setText(y + "  ");
        accelerometerZ2.setText(z + "  ");
        accelerometerS2.setText(Math.sqrt(x * x + y * y + z * z) + "  ");
    }
    private void setMagneticField(SensorEvent event, float power)
    {
        magneticfieldPower2.setText(power+"  ");
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        magneticfieldX2.setText(x+"  ");
        magneticfieldY2.setText(y+"  ");
        magneticfieldZ2.setText(z + "  ");
        magneticfieldS2.setText(Math.sqrt(x * x + y * y + z * z) + "  ");
    }
    private void setProximity(SensorEvent event, float power)
    {
        proximityPower2.setText(power+"  ");
        proximityDistance2.setText(event.values[0]+"  ");
    }
    private void setGyroscope(SensorEvent event, float power)
    {
        gyroscopePower2.setText(power+"  ");
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        gyroscopeX2.setText(x + "  ");
        gyroscopeY2.setText(y + "  ");
        gyroscopeZ2.setText(z + "  ");
        gyroscopeS2.setText(Math.sqrt(x * x + y * y + z * z) + "  ");
    }

    //Class for Wifi or Network State Change
    private class WifiNetworkStateChange extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            network=connectivityManager.getActiveNetworkInfo();
            if( network!=null && network.getType()==ConnectivityManager.TYPE_WIFI && network.isConnected() )
            {
                try
                {
                        telephonyManager.listen(networkSignalStrength,PhoneStateListener.LISTEN_NONE);
                }
                catch (Exception ex)
                {
                    Log.d("Already UnListened",ex.getMessage());
                }
                wifinetwork.setText("WIFI ON");
                imageView8.setImageResource(R.drawable.wifion);
                wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    wifiNetworkName1.setText("  SSID");
                    wifiNetworkStrength1.setText("  Strength (dBm)");
                    wifiNetworkName2.setText(wifiInfo.getSSID().replace("\"","").toUpperCase()+"  ");
                    wifiNetworkStrength2.setText(wifiInfo.getRssi()+"  ");
                }
                registerReceiver(wifiSignalStrength, wifiSignalStrengthFilter);
            }
            else if(network!=null && network.getType()==ConnectivityManager.TYPE_MOBILE && network.isConnected())
            {
                try
                {
                        unregisterReceiver(wifiSignalStrength);
                }
                catch (Exception ex)
                {
                    Log.d("Already Registered",ex.getMessage());
                }
                wifinetwork.setText("NETWORK ON");
                if(telephonyManager!=null)
                {
                    wifiNetworkName1.setText("  Network");
                    imageView8.setImageResource(R.drawable.antennaon);
                    wifiNetworkStrength1.setText("  Strength (dBm)");
                    wifiNetworkName2.setText(telephonyManager.getNetworkOperatorName().toUpperCase()+"  ");
                    telephonyManager.listen(networkSignalStrength,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                }
            }
            else
            {
                wifinetwork.setText("NO WIFI AND NETWORK");
                imageView8.setImageResource(R.drawable.nonetwork);
                wifiNetworkName1.setText("");
                wifiNetworkStrength1.setText("");
                wifiNetworkName2.setText("");
                wifiNetworkStrength2.setText("");
                try
                {
                        telephonyManager.listen(networkSignalStrength,PhoneStateListener.LISTEN_NONE);
                        unregisterReceiver(wifiSignalStrength);
                }
                catch (Exception ex)
                {
                    Log.d("Already Registered",ex.getMessage());
                }
            }
        }
    }
    private class WifiSignalStrength extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED)
            {
                List<ScanResult> wifiList = wifiManager.getScanResults();
                for(ScanResult scanResult : wifiList)
                {
                    if(scanResult.BSSID.equals(wifiManager.getConnectionInfo().getBSSID()))
                    {
                        wifiNetworkName1.setText("  SSID");
                        wifiNetworkStrength1.setText("  Strength (dBm)");
                        wifiNetworkName2.setText(wifiManager.getConnectionInfo().getSSID().replace("\"", "").toUpperCase()+"  ");
                        wifiNetworkStrength2.setText(wifiManager.getConnectionInfo().getRssi()+"  ");
                        break;
                    }
                }
            }
        }
    }
    private class NetworkSignalStrength extends PhoneStateListener
    {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if( signalStrength!=null )
            {
                wifiNetworkStrength2.setText(signalStrength.getGsmSignalStrength()+"  ");
            }
        }
    }


    private class GPSNetworkLocation implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location)
        {
            locationLatitude2.setText(location.getLatitude()+"  ");
            locationLongitude2.setText(location.getLongitude()+"  ");
            locationAltitude2.setText(location.getAltitude()+"  ");
            locationAccuracy2.setText(location.getAltitude()+"  ");
            locationProvider2.setText(location.getProvider().toUpperCase()+"  ");
            if(location.getProvider().trim().equals("GPS"))
            {
                imageView1.setImageResource(R.drawable.gpson);
            }
            else
            {
                imageView1.setImageResource(R.drawable.locationon);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {


        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
