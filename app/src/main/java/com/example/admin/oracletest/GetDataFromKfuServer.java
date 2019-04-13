package com.example.admin.oracletest;

import android.os.AsyncTask;

import com.example.admin.oracletest.Data.AsyncTaskArguments;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Класс для асинхроного получения данных с сервера кфу
 */

public class GetDataFromKfuServer extends AsyncTask<AsyncTaskArguments, Void, AsyncTaskArguments>{

    /**
     * @param asyncTaskArguments Параметры передаваемые с {@link com.example.admin.oracletest.Models.ServerKFU}
     *                           callback - который вернеться с JSON
     *                           url - это URL по которой происходит подключение к функциям/процедурам
     * @return Возвращает отбратно {@link AsyncTaskArguments}. Callback - вернется на UI Thread
     *                                                         Data - полученный JSON
     */

    @Override
    protected AsyncTaskArguments doInBackground(AsyncTaskArguments... asyncTaskArguments) {
        Callback callback = asyncTaskArguments[0].Callback;
        String url = asyncTaskArguments[0].Data.toString();
        String resultJson = "";
        try {
            //Создаем объект URL передавая в консруктор наш url
            URL urlObject = new URL(url);
            //Создаем подключение
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
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
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            resultJson = stringBuffer.toString();
        }catch (Exception e){
            e.getMessage();
        }
        AsyncTaskArguments returnedAsyncTaskArguments = new AsyncTaskArguments();
        returnedAsyncTaskArguments.Callback = callback;
        returnedAsyncTaskArguments.Data = resultJson;
        return returnedAsyncTaskArguments;
    }

    @Override
    protected void onPostExecute(AsyncTaskArguments asyncTaskArguments) {
        super.onPostExecute(asyncTaskArguments);
        Callback callback = asyncTaskArguments.Callback;
        callback.execute(asyncTaskArguments.Data.toString());
    }
}