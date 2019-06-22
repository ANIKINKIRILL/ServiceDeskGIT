package com.test.admin.servicedesk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.admin.servicedesk.models.RequestsPage;
import com.test.admin.servicedesk.network.main.RequestsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationHelper {

    private static final String TAG = "NotificationHelper";

    // Vars
    public static final String CHANNEL_ID = "Channel 1";
    public static final String CHANNEL_NAME = "Заявки";
    public static final String CHANNEL_DESC = "Новые назначенные заявки";

    public static int getUserRequestsAmount(){
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(BaseApplication.getContext().getString(R.string.settings), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(BaseApplication.getContext().getString(R.string.userRequestsAmount), 0);
    }

    public static int getUserId(){
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(BaseApplication.getContext().getString(R.string.settings), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(BaseApplication.getContext().getString(R.string.user_id), 0);
    }

    public void checkUserRequests(){

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_IP)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RequestsApi requestsApi = retrofit.create(RequestsApi.class);

        Log.d(TAG, "checkUserRequests: " + requestsApi.getMyRequests(getUserId(), 1, 3).request().url().toString());
        requestsApi.getMyRequests(getUserId(), 1, 3)
                .enqueue(new Callback<RequestsPage>() {
                    @Override
                    public void onResponse(Call<RequestsPage> call, Response<RequestsPage> response) {
                        Log.d(TAG, "onResponse: called");
                        Log.d(TAG, "onResponse: " + response.body().getRequests_amount() + " / " + getUserRequestsAmount());
                        if (response.body().getRequests_amount() > getUserRequestsAmount()) {
                            Log.d(TAG, "onResponse: notification's been received");

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                                notificationChannel.setDescription(CHANNEL_DESC);
                                NotificationManager notificationManager = (NotificationManager) BaseApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.createNotificationChannel(notificationChannel);
                            }

                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(BaseApplication.getContext(), CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_fiber_new_white)
                                    .setContentTitle("Новые заявки")
                                    .setContentText("На Вас начислены новые заявки")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

//                            Intent intent = new Intent("android.intent.action.MainActivity");
//                            PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                            mBuilder.setContentIntent(pendingIntent);

                            Notification notification = mBuilder.build();
                            NotificationManager notificationManager = (NotificationManager) BaseApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(1, notification);
                        }else{
                            Log.d(TAG, "onResponse: notification's not been received");
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestsPage> call, Throwable t) {
                        Log.d(TAG, "onFailure: called " + t.getMessage());
                    }
                });
    }

}
