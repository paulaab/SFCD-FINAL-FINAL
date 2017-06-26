package de.vodafone.innogarage.sfcdmonitoring;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{
    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5, expandableLayout6;
    //Define socket variables (ports)
    public static boolean debugMode = true;
    public static int socketPortForBroadcast = 45555;
    public static int socketServerPortForSFCD = 45556;
    public int msgCounter = 0;
    //Define variables for connection
    ConnectionManager conMan = new ConnectionManager();
    List<Connection> cons;
    TimerTask timerTask;
    TimerTask timerTask2;
    Boolean stop;
    Timer timer2 = new Timer();
    Timer timer = new Timer();
    public Context globalContext;
    ListViewAdapter adapter;
    ListView listDevices;
    Button buttonState;

    //------------Map Var---------------------------------------
    private GoogleMap mMap;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;


    //---------------------------------------------------------




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        globalContext = this;
        cons = conMan.getConnections();

        listDevices = (ListView) findViewById(R.id.mSFCDList);
        buttonState = (Button) findViewById(R.id.buttonState);

        adapter = new ListViewAdapter(globalContext, conMan);
        listDevices.setAdapter(adapter);




        //new updateListTask().execute();
        //new ScannerTask().execute(conMan);

        timerTask2 = new TimerTask() {
            @Override
            public void run() {
                try {
                    new updateListTask().execute();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("TimerUpdateList","Error executing updateListTask");

                }


            }
        };
        timer2.schedule(timerTask2, 0, 5000);







        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    new ScannerTask().execute(conMan);

                }
                catch (Exception ex){
                    ex.printStackTrace();
                    Log.e("TimerScanner","Error executing ScannerTask");

                }

            }
        };
        timer.schedule(timerTask, 0, 200);




    }
    /*------------Methods for displaying the data on the list--------------*/

    public void clearView(){
        TextView temp;
        temp = (TextView) findViewById(R.id.ltebw);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.rsrprxdr);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.rsrprxm);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.grsrq);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.gsinr);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.gmode);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.ltecastate);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.cellid);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.currenttime);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.ltetxchan);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.gtac);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.emmstatereg);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.rrcstate);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.temperature);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.systemmode);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.psstate);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.emmstateserv);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.lteband);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.lterxchan);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.gtxpower);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.imsregstate);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.resetcounter);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.pccrxdrssi);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.pccrxrmrssi);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.searfcn);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.smcc);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.smnc);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.stac);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.scid);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.sbd);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.sd);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.su);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.ssnr);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.spci);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.srsrq);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.srsrp);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.srssi);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.srxlv);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.iteearfcn);
        temp.setText("N/A");

        temp = (TextView) findViewById(R.id.thresholdlow);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.thresholdhi);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.priority);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.itepci);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.itersrq);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.itersrp);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.iterssi);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.iterxlv);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.itapci1);
        temp.setText("N/A");

        temp = (TextView) findViewById(R.id.itarsrq1);
        temp.setText("N/A");

        temp = (TextView) findViewById(R.id.itarsrp1);
        temp.setText("N/A");

        temp = (TextView) findViewById(R.id.itarssi1);
        temp.setText("N/A");

        temp = (TextView) findViewById(R.id.itarxlv1);
        temp.setText("N/A");

        temp = (TextView) findViewById(R.id.itapci2);
        temp.setText("N/A");

        temp = (TextView) findViewById(R.id.itarsrq2);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.itarsrp2);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.itarssi2);
        temp.setText("N/A");

        temp = (TextView) findViewById(R.id.itarxlv2);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.altitude);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.ept);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.climb);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.eps);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.epv);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.epx);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.speed);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.track);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.longitude);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.latitude);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.satellites);
        temp.setText("N/A");
        temp = (TextView) findViewById(R.id.mode);
        temp.setText("N/A");

    }

    public void clearButton(){
        Button button = (Button) findViewById(R.id.buttonState);
        button.setVisibility(View.INVISIBLE);

    }



    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableButton2(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableButton3(View view) {
        expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        expandableLayout3.toggle(); // toggle expand and collapse
    }

    public void expandableButton4(View view) {
        expandableLayout4 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout4);
        expandableLayout4.toggle(); // toggle expand and collapse
    }

    public void expandableButton5(View view) {
        expandableLayout5 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout5);
        expandableLayout5.toggle(); // toggle expand and collapse
    }

    public void expandableButton6(View view) {
        expandableLayout6 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout6);
        expandableLayout6.toggle(); // toggle expand and collapse
    }






    //-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------Map Code------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Log.d("MapReady", "map is ready");
        // Add a marker in Sydney and move the camera
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }
    //----------------------------------------------------------------------------My current location-----------------------------------------------------------
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }



    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    //---------------------------------------------------------------------------- End my Location-----------------------------------------------------------


    //---------------------------------------------------------------------------- Display a Marker on the Map-----------------------------------------------
    public void displayMarker(final Double Lati, final Double Longi, int snr){
        final long TimeToLive = 2000;
        final BitmapDescriptor myicon;
        int id = 0;
        if (snr <= 10){
            id = getResources().getIdentifier("red", "drawable", getPackageName());
        }
        else if(snr >= 20){
            id = getResources().getIdentifier("yellow", "drawable", getPackageName());
        }
        else{
            id = getResources().getIdentifier("green", "drawable", getPackageName());
        }

        myicon = BitmapDescriptorFactory.fromResource(id);

        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                LatLng pos = new LatLng(Lati,Longi);
                Marker mar = mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .icon(myicon)
                );
                fadeTime(TimeToLive,mar);
            }

        });
    }

/*-----Customize characteristics of the markers: Size and time to fade--------*/


    public void fadeTime(long duration, Marker marker) {

        final Marker myMarker = marker;
        ValueAnimator myAnim = ValueAnimator.ofFloat(1, 0);
        myAnim.setDuration(duration);
        myAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                myMarker.setAlpha((float) animation.getAnimatedValue());
            }
        });
        myAnim.start();
    }


//-------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------End Map Code-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_invite:
                conMan.clearConnections();
                conMan.sendInvitation();

                Toast.makeText(MainActivity.this,"Invitation sent",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



/*================================== UPDATE LIST AND REFRESH VIEW ====================================*/


    class updateListTask extends AsyncTask<Object, Object, Void> {
        Boolean changeList = false;



        //ListViewAdapter adapter;
        @Override
        protected Void doInBackground(Object... voids) {
            if(cons != conMan.getConnections()){
                changeList = true;
                cons = conMan.getConnections();

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if (changeList){
               // adapter = new ListViewAdapter(globalContext, conMan);
                adapter.notifyDataSetChanged();
              //  listDevices.setAdapter(adapter);
               // adapter.notifyDataSetChanged();
                //listDevices.invalidateViews();

            }


        }
    }



/*

    class updateListTask extends AsyncTask<Void,boolean,Void>{
        boolean stop;


        @Override
        protected boolean doInBackground(Void... voids) {
            stop = true;
            //Timer timer = new Timer();
            //TimerTask myTimerTask = new TimerTask() {
                //@Override
                //public void run() {

                    //while(!stop){
                        if(!cons.equals(conMan.getConnections())){
                            cons = conMan.getConnections();
                        }
                        else {

                        }
                    //}

                //}
            //};

//            timer.schedule(myTimerTask,0,5000);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            listDevices.setAdapter(new ListViewAdapter(globalContext, conMan));


        }
    }*/



    public class ListViewAdapter extends BaseAdapter {

        ConnectionManager conMan;
        private Context context;

        public ListViewAdapter(Context c,ConnectionManager conMan) {
            this.conMan = conMan;
            context = c;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.clients_list, parent , false);
            Button button = (Button) row.findViewById(R.id.detailsButton);
            TextView tv_hostname = (TextView) row.findViewById(R.id.client_name);
            TextView tv_clientIP = (TextView) row.findViewById(R.id.client_ip);
            //Button buttonState= (Button) findViewById(R.id.buttonState);



            final Connection con = conMan.getConnections().get(position);


            button.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){

                    for(Connection con : conMan.getConnections()){

                        con.setFocus(false);

                    }
                    con.setFocus(true);
                    //clearView();
                }
            });

            tv_hostname.setText(con.getName());
            tv_clientIP.setText(con.getIP());


            if (con.isFocus()) {
                button.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                button.setText("Selected");

                if(con.isOnline()){

                    buttonState.setVisibility(View.VISIBLE);
                    buttonState.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                    buttonState.setText("ONLINE: "+con.getName());
                    //imgtemp = (ImageView) findViewById(R.id.stateLTE);
                    // imgtemp.setImageResource(R.drawable.green);

                }else{

                    buttonState.setVisibility(View.VISIBLE);
                    buttonState.setBackgroundColor(getResources().getColor(R.color.red));
                    buttonState.setText("OFFLINE: "+con.getName());
                }
            } else {

                button.setBackgroundColor(Color.GRAY);
                button.setText("--");

            }
            notifyDataSetChanged();
            return row;
        }



        @Override
        public int getCount() {
            return cons.size();
        }

        @Override
        public Object getItem(int position) {
            return cons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

/*=================== GET DATA FROM DEVICES ========================*/

    public class ScannerTask extends AsyncTask<ConnectionManager, Void, JSONObject> {

        Double latitude;
        Double longitude;


        @Override
        protected JSONObject doInBackground(ConnectionManager... params) {
            ConnectionManager conMan = null;
            if(params.length == 1)
                conMan = params[0];

            Connection con = null;
            List<JSONObject> msgList = null;
            JSONObject actMsg = null;
            List<Connection> cons = null;

            if(!conMan.getConnections().isEmpty()){

                cons = conMan.getConnections();

                for(Connection temp : cons){

                    if(temp.isFocus()){
                        con = temp;
                    }
                }
                if(con == null){
                    con = cons.get(0); //Default connection
                }

                msgList = con.getIncomingData();

                if(!msgList.isEmpty()) {

                    System.out.print("Received msglist");
                    actMsg = msgList.get(0);
                    msgList.remove(0);
                    if (actMsg == null || con.getNumberOfErrors()>28){
                        try {
                            actMsg.put("Error","Value");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return actMsg;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            double lat = 0;
            double longi = 0;
            int snr = 0;
            int mid;

            if (!cons.isEmpty()) {
                if(result!=null){

                    if (result.has("Error")){
                        clearView();

                    }

                    else{
                        clearView();

                        Iterator<?> keys = result.keys();
                        TextView temp;
                        while (keys.hasNext()) {
                            String mKey = (String) keys.next();
                            switch (mKey) {
                                case "gstatus":
                                    try {
                                        JSONObject gobj = result.getJSONObject("gstatus");
                                        if(gobj!=null){
                                            temp = (TextView) findViewById(R.id.ltebw);
                                            temp.setText(gobj.getString("ltebw(mhz)"));
                                            temp = (TextView) findViewById(R.id.rsrprxdr);
                                            temp.setText(gobj.getString("rsrp(dbm)pccrxdrssi"));
                                            temp = (TextView) findViewById(R.id.rsrprxm);
                                            temp.setText(gobj.getString("rsrp(dbm)pccrxmrssi"));
                                            temp = (TextView) findViewById(R.id.grsrq);
                                            temp.setText(gobj.getString("rsrq(db)"));
                                            temp = (TextView) findViewById(R.id.gsinr);
                                            temp.setText(gobj.getString("sinr(db)"));
                                            temp = (TextView) findViewById(R.id.gmode);
                                            temp.setText(gobj.getString("mode"));
                                            temp = (TextView) findViewById(R.id.ltecastate);
                                            temp.setText(gobj.getString("ltecastate"));
                                            temp = (TextView) findViewById(R.id.cellid);
                                            temp.setText(gobj.getString("cellid"));
                                            temp = (TextView) findViewById(R.id.currenttime);
                                            temp.setText(gobj.getString("currenttime"));
                                            temp = (TextView) findViewById(R.id.ltetxchan);
                                            temp.setText(gobj.getString("ltetxchan"));
                                            temp = (TextView) findViewById(R.id.gtac);
                                            temp.setText(gobj.getString("tac"));
                                            temp = (TextView) findViewById(R.id.emmstatereg);
                                            temp.setText(gobj.getString("emmstatereg"));
                                            temp = (TextView) findViewById(R.id.rrcstate);
                                            temp.setText(gobj.getString("rrcstate"));
                                            temp = (TextView) findViewById(R.id.temperature);
                                            temp.setText(gobj.getString("temperature"));
                                            temp = (TextView) findViewById(R.id.systemmode);
                                            temp.setText(gobj.getString("systemmode"));
                                            temp = (TextView) findViewById(R.id.psstate);
                                            temp.setText(gobj.getString("psstate"));
                                            temp = (TextView) findViewById(R.id.emmstateserv);
                                            temp.setText(gobj.getString("emmstateserv"));
                                            temp = (TextView) findViewById(R.id.lteband);
                                            temp.setText(gobj.getString("lteband"));
                                            temp = (TextView) findViewById(R.id.lterxchan);
                                            temp.setText(gobj.getString("lterxchan"));
                                            temp = (TextView) findViewById(R.id.gtxpower);
                                            temp.setText(gobj.getString("txpower"));
                                            temp = (TextView) findViewById(R.id.imsregstate);
                                            temp.setText(gobj.getString("imsregstate"));
                                            temp = (TextView) findViewById(R.id.resetcounter);
                                            temp.setText(gobj.getString("resetcounter"));
                                            temp = (TextView) findViewById(R.id.pccrxdrssi);
                                            temp.setText(gobj.getString("pccrxdrssi"));
                                            temp = (TextView) findViewById(R.id.pccrxrmrssi);
                                            temp.setText(gobj.getString("pccrxmrssi"));


                                        }

                                    } catch (JSONException e) {
                                        System.out.println("Could not extract gstatus object -->");
                                        e.printStackTrace();

                                    }
                                    break;


                                case "serving":
                                    try {
                                        JSONArray sarr = result.getJSONArray("serving");
                                        if (sarr != null) {
                                            JSONObject sobj = sarr.getJSONObject(0);


                                            temp = (TextView) findViewById(R.id.searfcn);
                                            temp.setText(sobj.getString("EARFCN"));

                                            temp = (TextView) findViewById(R.id.smcc);
                                            temp.setText(sobj.getString("MCC"));

                                            temp = (TextView) findViewById(R.id.smnc);
                                            temp.setText(sobj.getString("MNC"));

                                            temp = (TextView) findViewById(R.id.stac);
                                            temp.setText(sobj.getString("TAC"));

                                            temp = (TextView) findViewById(R.id.scid);
                                            temp.setText(sobj.getString("CID"));

                                            temp = (TextView) findViewById(R.id.sbd);
                                            temp.setText(sobj.getString("Bd"));

                                            temp = (TextView) findViewById(R.id.sd);
                                            temp.setText(sobj.getString("D"));

                                            temp = (TextView) findViewById(R.id.su);
                                            temp.setText(sobj.getString("U"));

                                            temp = (TextView) findViewById(R.id.ssnr);
                                            temp.setText(sobj.getString("SNR"));
                                            //snr = sobj.getInt("SNR");

                                            temp = (TextView) findViewById(R.id.spci);
                                            temp.setText(sobj.getString("PCI"));

                                            temp = (TextView) findViewById(R.id.srsrq);
                                            temp.setText(sobj.getString("RSRQ"));

                                            temp = (TextView) findViewById(R.id.srsrp);
                                            temp.setText(sobj.getString("RSRP"));

                                            temp = (TextView) findViewById(R.id.srssi);
                                            temp.setText(sobj.getString("RSSI"));

                                            temp = (TextView) findViewById(R.id.srxlv);
                                            temp.setText(sobj.getString("RXLV"));
                                        }

                                    } catch (JSONException e) {
                                        System.out.println("Could not extract serving object -->");
                                        e.printStackTrace();
                                    }
                                    break;


                                case "interfreq":
                                    try {
                                        JSONArray itearr = result.getJSONArray("interfreq");
                                        if(itearr!=null){
                                            JSONObject iteobj= itearr.getJSONObject(0);

                                            temp = (TextView) findViewById(R.id.iteearfcn);
                                            temp.setText(iteobj.getString("EARFCN"));

                                            temp = (TextView) findViewById(R.id.thresholdlow);
                                            temp.setText(iteobj.getString("ThresholdLow"));

                                            temp = (TextView) findViewById(R.id.thresholdhi);
                                            temp.setText(iteobj.getString("ThresholdHi"));

                                            temp = (TextView) findViewById(R.id.priority);
                                            temp.setText(iteobj.getString("Priority"));

                                            temp = (TextView) findViewById(R.id.itepci);
                                            temp.setText(iteobj.getString("PCI"));

                                            temp = (TextView) findViewById(R.id.itersrq);
                                            temp.setText(iteobj.getString("RSRQ"));

                                            temp = (TextView) findViewById(R.id.itersrp);
                                            temp.setText(iteobj.getString("RSRP"));

                                            temp = (TextView) findViewById(R.id.iterssi);
                                            temp.setText(iteobj.getString("RSSI"));

                                            temp = (TextView) findViewById(R.id.iterxlv);
                                            temp.setText(iteobj.getString("RXLV"));


                                        }

                                    }catch (JSONException e){
                                        System.out.println("Could not extract interfreq object -->");
                                     //   e.printStackTrace();
                                    }
                                    break;
                                case "intrafreq":
                                    try {
                                        JSONArray itaarr = result.getJSONArray("intrafreq");
                                        if(itaarr!=null){
                                            JSONObject itaobj1= itaarr.getJSONObject(0);



                                            temp = (TextView) findViewById(R.id.itapci1);
                                            temp.setText(itaobj1.getString("PCI"));

                                            temp = (TextView) findViewById(R.id.itarsrq1);
                                            temp.setText(itaobj1.getString("RSRQ"));

                                            temp = (TextView) findViewById(R.id.itarsrp1);
                                            temp.setText(itaobj1.getString("RSRP"));

                                            temp = (TextView) findViewById(R.id.itarssi1);
                                            temp.setText(itaobj1.getString("RSSI"));

                                            temp = (TextView) findViewById(R.id.itarxlv1);
                                            temp.setText(itaobj1.getString("RXLV"));

                                            JSONObject itaobj2= itaarr.getJSONObject(1);
                                            temp = (TextView) findViewById(R.id.itapci2);
                                            temp.setText(itaobj2.getString("PCI"));

                                            temp = (TextView) findViewById(R.id.itarsrq2);
                                            temp.setText(itaobj2.getString("RSRQ"));

                                            temp = (TextView) findViewById(R.id.itarsrp2);
                                            temp.setText(itaobj2.getString("RSRP"));

                                            temp = (TextView) findViewById(R.id.itarssi2);
                                            temp.setText(itaobj2.getString("RSSI"));

                                            temp = (TextView) findViewById(R.id.itarxlv2);
                                            temp.setText(itaobj2.getString("RXLV"));



                                        }

                                    }catch (JSONException e){
                                        System.out.println("Could not extract intrafreq object -->");
                                        e.printStackTrace();
                                    }
                                    break;

                                case "location":
                                    try{
                                        JSONObject lobj = result.getJSONObject("location");
                                        if (lobj != null){

                                            temp = (TextView) findViewById(R.id.altitude);
                                            temp.setText(lobj.getString("altitude"));
                                            temp = (TextView) findViewById(R.id.ept);
                                            temp.setText(lobj.getString("ept"));
                                            temp = (TextView) findViewById(R.id.climb);
                                            temp.setText(lobj.getString("climb"));
                                            temp = (TextView) findViewById(R.id.eps);
                                            temp.setText(lobj.getString("eps"));
                                            temp = (TextView) findViewById(R.id.epv);
                                            temp.setText(lobj.getString("epv"));
                                            temp = (TextView) findViewById(R.id.epx);
                                            temp.setText(lobj.getString("epx"));
                                            temp = (TextView) findViewById(R.id.speed);
                                            temp.setText(lobj.getString("speed"));
                                            temp = (TextView) findViewById(R.id.track);
                                            temp.setText(lobj.getString("track"));
                                            temp = (TextView) findViewById(R.id.longitude);
                                            temp.setText(lobj.getString("longitude"));
                                            //longi = lobj.getDouble("longitude");
                                            temp = (TextView) findViewById(R.id.latitude);
                                            temp.setText(lobj.getString("latitude"));
                                            // lat = lobj.getDouble("latitude");
                                            temp = (TextView) findViewById(R.id.satellites);
                                            temp.setText(lobj.getString("satellites"));
                                            temp = (TextView) findViewById(R.id.mode);
                                            temp.setText(lobj.getString("mode"));



                                        }
                                    }

                                    catch (JSONException e){
                                        System.out.println("Could not extract location object -->");
                                        e.printStackTrace();
                                        break;
                                    }
                                    break;
                            }
                        }
                        //Displaying markers
                        //displayMarker(lat, longi, snr);
                    }

                }
            }
            else {
                clearView();
                clearButton();
            }
        }

    }

}
