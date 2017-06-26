package de.vodafone.innogarage.sfcdmonitoring;

import android.app.Activity;
import android.util.Log;
import android.util.LongSparseArray;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by paulabohorquez on 5/16/17.
 */

public class Connection {
    private Socket socket;
    private int errorCounter;
    private InputStream incomingStream;
    private List<JSONObject> incomingData;
    private List<JSONObject> outgoingData;
    private boolean close,focus,online;
    private String name;
    private String clientip;
    //private DataOutputStream outgoingStream;
    private Double longitude;
    private Double latitude;
    private int snr;




    public Connection(Socket socket) {
        clientip = socket.getInetAddress().toString();
        name = socket.getInetAddress().getHostName();
        this.socket = socket;
        incomingData = new CopyOnWriteArrayList<>();
        outgoingData = new CopyOnWriteArrayList<>();
        errorCounter = 0;
        close = false;
        focus = false;
        online=false;
        longitude = 0.0;
        latitude = 0.0;

//Get incoming stream and place it in an arraylist
        try {
            incomingStream = socket.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Start reading incoming stream above in a and convert it to
        // JSON Object that will be accessed after that.
        new InputStreamThread().start();

    }


    private class InputStreamThread extends Thread {
        BufferedReader breader = new BufferedReader(new InputStreamReader(incomingStream));

        public void run() {

            String inmsg;
            JSONObject jObj;
            String mode;

            while (!close) {
                try {

                    char[] b = new char[2048];
                    int count = breader.read(b, 0, 2048);
                    System.out.println("Count: "+count);
                    if (count>0){
                        inmsg = new String(b, 0, count);
                      inmsg = inmsg.substring(8);
                        System.out.println(socket.getInetAddress() + "     Incomming message stream received:  " + inmsg);
                        if (inmsg != null) {
                            try {
                                jObj = new JSONObject(inmsg);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("InputStreamThread: ", socket.getInetAddress() + "   Could not save Json Object with incoming stream");
                                jObj = new JSONObject();

                            }


                            incomingData.add(jObj);
                            Log.e("Connection: ", socket.getInetAddress() + " Message received: " + jObj.toString() + " => Placed in incomingData, parsed as JSON");
                            try {
                                mode = jObj.getJSONObject("gstatus").getString("mode");

                                if(mode.equalsIgnoreCase("ONLINE")){

                                   online=true;

                                }
                                else {

                                    online=false;


                                }




                            }catch (JSONException e){
                                e.printStackTrace();
                                System.out.print("Could not save Mode value");
                            }
/*
                            try {
                                latitude = jObj.getJSONObject("gstatus").getDouble("latitude");
                                longitude = jObj.getJSONObject("gstatus").getDouble("longitude");
                                snr = jObj.getJSONArray("serving").getJSONObject(0).getInt("SNR");






                            }
                            catch (JSONException e){
                                System.out.print("Could not get coordinates data:");
                                e.printStackTrace();


                            }*/

                        }



                    }
                    else {
                        errorCounter = errorCounter + 1;
                        System.out.print("Error counts:  "+errorCounter);
                        if (errorCounter>=30){
                            close=true;


                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }






        }
    }



    public String getName() {return name;}
    public String getIP(){return clientip;}
    public List<JSONObject> getIncomingData(){return incomingData;}
    public int getNumberOfErrors(){return errorCounter;}
    public boolean isFocus() {return focus;}
    public void setFocus(boolean focus) {this.focus = focus;}
    public Socket getSocket(){return socket;}
    public boolean isClose() {return close;}
    public void setClose(boolean close) {this.close = close;}
    public boolean isOnline() {return online;}
    public Double getlongitude(){return longitude;}
    public Double getlatitude(){return latitude;}
    public int getSNR(){return snr;}

}
