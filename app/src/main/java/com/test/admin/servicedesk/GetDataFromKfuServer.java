package com.test.admin.servicedesk;

import android.os.AsyncTask;
import android.util.Log;

import com.test.admin.servicedesk.Data.AsyncTaskArguments;
import com.test.admin.servicedesk.Models.ServerKFU;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Класс для асинхроного получения данных с сервера кфу
 */

public class GetDataFromKfuServer extends AsyncTask<com.test.admin.servicedesk.Data.AsyncTaskArguments, Void, com.test.admin.servicedesk.Data.AsyncTaskArguments>{

    private static final String TAG = "GetDataFromKfuServer";

    /**
     * @param asyncTaskArguments Параметры передаваемые с {@link ServerKFU}
     *                           callback - который вернеться с JSON
     *                           url - это URL по которой происходит подключение к функциям/процедурам
     * @return Возвращает отбратно {@link com.test.admin.servicedesk.Data.AsyncTaskArguments}. Callback - вернется на UI Thread
     *                                                         Data - полученный JSON
     */

    @Override
    protected com.test.admin.servicedesk.Data.AsyncTaskArguments doInBackground(com.test.admin.servicedesk.Data.AsyncTaskArguments... asyncTaskArguments) {
        Callback callback = asyncTaskArguments[0].Callback;
        String url = asyncTaskArguments[0].Data.toString();
        Log.d(TAG, "doInBackground: " + url);
        String resultJson = "";
        try {
            //Создаем объект URL передавая в консруктор наш url
            URL urlObject = new URL(url);
            //Создаем подключение
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            //Делаем запрос при помощи GET method
            connection.setRequestMethod("GET");
            connection.connect();
            Log.d(TAG, "doInBackground: connected");
            //Создаем поток для чтения
            InputStream inputStream = connection.getInputStream();

            //Создаем буффер
            StringBuffer stringBuffer = new StringBuffer();
            //Создаем reader, указывая кодировку cp1251, для поддержки кириллицы
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "cp1251"));

            //Читаем, пока можем
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            resultJson = stringBuffer.toString();
            Log.d(TAG, "doInBackground: " + resultJson);
        }catch (Exception e){
            Log.d(TAG, "doInBackground: " + e.getMessage());
        }
        com.test.admin.servicedesk.Data.AsyncTaskArguments returnedAsyncTaskArguments = new com.test.admin.servicedesk.Data.AsyncTaskArguments();
        returnedAsyncTaskArguments.Callback = callback;
        returnedAsyncTaskArguments.Data = resultJson;
        return returnedAsyncTaskArguments;
    }

    @Override
    protected void onPostExecute(AsyncTaskArguments asyncTaskArguments) {
        super.onPostExecute(asyncTaskArguments);
        Callback callback = asyncTaskArguments.Callback;
        callback.execute(asyncTaskArguments.Data.toString());
        Log.d(TAG, "onPostExecute: " + asyncTaskArguments.Data.toString());
    }
}