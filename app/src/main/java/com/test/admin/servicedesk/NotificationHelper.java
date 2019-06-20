package com.test.admin.servicedesk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.test.admin.servicedesk.dependencyinjection.app.SessionManager;
import com.test.admin.servicedesk.models.RequestsPage;
import com.test.admin.servicedesk.network.main.RequestsApi;
import com.test.admin.servicedesk.ui.main.MainActivity;
import com.test.admin.servicedesk.ui.main.my_requests.MyRequestsFragment;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationHelper {

    private static final String TAG = "NotificationHelper";

    // Injections
    @Inject
    static RequestsApi requestsApi;

    @Inject
    static SessionManager sessionManager;

    // Vars
    public static final String CHANNEL_ID = "Channel 1";

    public static int getUserRequestsAmount(){
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(BaseApplication.getContext().getString(R.string.settings), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(BaseApplication.getContext().getString(R.string.userRequestsAmount), 0);
    }

    public static void checkUserRequests(){
        Log.d(TAG, "checkUserRequests: " + requestsApi.getMyRequests(sessionManager.getAuthUser().getValue().data.getUserId(), 1, 1).request().url().toString());
        requestsApi.getMyRequests(sessionManager.getAuthUser().getValue().data.getUserId(), 1, 1)
                .enqueue(new Callback<RequestsPage>() {
                    @Override
                    public void onResponse(Call<RequestsPage> call, Response<RequestsPage> response) {
                        Log.d(TAG, "onResponse: called");
                        if(response.body().getRequests().length > getUserRequestsAmount()){
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(BaseApplication.getContext(), CHANNEL_ID)
                                    .setContentTitle("Новые заявки")
                                    .setContentText("На Вас начислены новые заявки")
                                    .setSmallIcon(R.drawable.ic_fiber_new_white)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH);

                            Intent intent = new Intent("android.intent.action.MainActivity");
                            PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            mBuilder.setContentIntent(pendingIntent);

                            Notification notification = mBuilder.build();
                            NotificationManager notificationManager = (NotificationManager) BaseApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(1, notification);
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestsPage> call, Throwable t) {
                        Log.d(TAG, "onFailure: called " + t.getMessage());
                    }
                });
    }

}
