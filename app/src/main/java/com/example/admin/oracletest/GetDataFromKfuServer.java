package com.example.admin.oracletest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.admin.oracletest.Data.AsyncTaskArguments;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Класс для асинхроного получения данных с сервера кфу
 */
public class GetDataFromKfuServer {

    // Logs
    private static final String TAG = "GetDataFromKfuServer";

    /**
     * Получить данные с Сервера КФУ
     * @param url       путь к функции
     * @return          JSON с данными
     */

    public static String getDataFromKfuServer(String url){
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
        return resultJson;
    }

}
