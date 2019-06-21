package com.test.admin.servicedesk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Service for retrieving notification
 */

public class NotificationsService extends Service {

    private static String LOG_TAG = "ServiceNotification";
    private Timer mTimer;
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.checkUserRequests();
            Log.d(LOG_TAG, "Timer update");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service create");
        this.mTimer = new Timer();
        /*
            delay - 10 sec
            period - 18 sec
        */
        mTimer.schedule(mTimerTask, 1000 * 10, 100 * 60 * 3);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Service start command");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        mTimerTask.cancel();
        Log.d(LOG_TAG, "Service destroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
