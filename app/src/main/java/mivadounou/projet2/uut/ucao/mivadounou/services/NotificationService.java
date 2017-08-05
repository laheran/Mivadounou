package mivadounou.projet2.uut.ucao.mivadounou.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.models.NotificationMessage;
import mivadounou.projet2.uut.ucao.mivadounou.other.FirebaseRef;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Created by leinad on 8/4/17.
 */

public class NotificationService extends Service {
    
    private Looper mServiceLooper;
    
    private ServiceHandler mServiceHandler;
    
    private DatabaseReference mDatabaseReference;
    
    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        
        @Override
        public void handleMessage(Message msg) {
            
            if (msg.arg2 == 0) {
                mDatabaseReference
                        .child(FirebaseRef.USER_COMMANDE_NOTIFICATIONS)
                        .child(msg.obj.toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot notificationMessageDataSnapshot : dataSnapshot.getChildren()) {
                            
                            NotificationMessage notificationMessage = notificationMessageDataSnapshot.getValue(NotificationMessage.class);
                            
                            assert notificationMessage != null;
                            if (!notificationMessage.getIsSent()) {
                                sendNotification("Mivadounou", notificationMessage.getMessage());
                            }
                        }
                    }
                    
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    
                    }
                });
            } else {
                mDatabaseReference
                        .child(FirebaseRef.RESTAU_COMMANDE_NOTIFICATIONS)
                        .child(msg.obj.toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        
                        for (DataSnapshot notificationMessageDataSnapshot : dataSnapshot.getChildren()) {
                            
                            NotificationMessage notificationMessage = notificationMessageDataSnapshot.getValue(NotificationMessage.class);
                            
                            if (!notificationMessage.getIsSent()) {
                                sendNotification("Mivadounou", notificationMessage.getMessage());
                            }
                        }
                    }
                    
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    
                    }
                });
            }
            
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }
    
    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                THREAD_PRIORITY_BACKGROUND);
        thread.start();
        
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        
        assert intent != null;
        String userId = intent.getStringExtra(FirebaseRef.USER_COMMANDE_NOTIFICATIONS);
        
        String restauKey = intent.getStringExtra(FirebaseRef.RESTAU_COMMANDE_NOTIFICATIONS);

//        sendNotification("Mivadounou", userId + " Service started " + restauKey);
        
        if (userId != null) {
            
            // For each start request, send a message to start a job and deliver the
            // start ID so we know which request we're stopping when we finish the job
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            msg.arg2 = 0;
            msg.obj = userId;
            mServiceHandler.sendMessage(msg);
        }
        
        if (restauKey != null) {
            
            // For each start request, send a message to start a job and deliver the
            // start ID so we know which request we're stopping when we finish the job
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            msg.arg2 = 1;
            msg.obj = restauKey;
            mServiceHandler.sendMessage(msg);
        }
        
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }
    
    private void sendNotification(String title, String message) {
        
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message);
        
        Intent resultIntent = new Intent("android.intent.action.MAIN");
        
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        
        mBuilder.setContentIntent(resultPendingIntent);
        
        int notificationId = (int) System.currentTimeMillis();
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(notificationId, mBuilder.build());
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
