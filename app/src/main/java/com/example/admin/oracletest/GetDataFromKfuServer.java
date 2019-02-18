package com.example.admin.oracletest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Класс для асинхроного получения данных с сервера кфу
 */
public class GetDataFromKfuServer extends AsyncTask<AsyncTaskArguments, Void, AsyncTaskArguments> {

    private static final String TAG = "GetDataFromKfuServer";

    @Override
    protected AsyncTaskArguments doInBackground(AsyncTaskArguments... asyncTaskArguments) {
        String resultJson = "";

        try {
            //Создаем объект URL передавая в консруктор наш url
            URL url = new URL(asyncTaskArguments[0].Data.toString());
            //Создаем подключение
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //Делаем запрос при помощи GET method
            connection.setRequestMethod("GET");
            connection.connect();

            //Создаем поток для чтения
            InputStream inputStream = connection.getInputStream();
            //Создаем буффер
            StringBuffer stringBuffer = new StringBuffer();
            //Создаем reader, указывая кодировку cp1251, для поддержки кириллицы
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "cp1251"));

            //Читаем, пока можем
            String line;
            while((line=bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            resultJson = stringBuffer.toString();
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: error die to " + e.getMessage());
        }
        AsyncTaskArguments returnAsyncTaskArgument = new AsyncTaskArguments();
        returnAsyncTaskArgument.Data = resultJson;
        returnAsyncTaskArgument.Callback = asyncTaskArguments[0].Callback;
        return returnAsyncTaskArgument;
    }

    @Override
    protected void onPostExecute(AsyncTaskArguments asyncTaskArguments) {
        super.onPostExecute(asyncTaskArguments);
        asyncTaskArguments.Callback.execute(asyncTaskArguments.Data.toString());
    }
}
