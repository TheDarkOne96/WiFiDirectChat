package com.example.android.wifidirect;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;


public class WiFiClientIPTransferService extends IntentService {

public WiFiClientIPTransferService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
public WiFiClientIPTransferService() {
    super("WiFiClientIPTransferService");
}



    /*
     * (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(FileTransferService.ACTION_SEND_FILE)) {
            String host = intent.getExtras().getString(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS);
            String emptyMsg =  intent.getExtras().getString(FileTransferService.emptyMsg);

            Socket socket = new Socket();
            int port = intent.getExtras().getInt(FileTransferService.EXTRAS_GROUP_OWNER_PORT);

            try {
            	
                Log.d(WiFiDirectActivity.TAG, "Opening client socket for First tiime- ");
                socket.bind(null);
                socket.connect((new InetSocketAddress(host, port)), 5000);
                Log.d(WiFiDirectActivity.TAG, "Client socket - " + socket.isConnected());
                OutputStream stream = socket.getOutputStream();

               /*
                * Object that is used to send file name with extension and recieved on other side.
                */
                ObjectOutputStream oos = new ObjectOutputStream(stream);

                oos.writeObject(emptyMsg);
                System.out.println("Sending request to Socket Server");
                
                oos.close();	//close the ObjectOutputStream after sending data.
            } catch (IOException e) {
                Log.e(WiFiDirectActivity.TAG, e.getMessage());
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    if (socket.isConnected()) {
                        try {
                            socket.close();
                        } catch (Exception e) {
                            // Give up
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
