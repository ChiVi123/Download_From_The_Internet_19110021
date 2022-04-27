package com.android.s19110021;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static String getSource(Context context, String query, String protocol) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String sourceCode = null;

        String[] protocols = context.getResources().getStringArray(R.array.protocol_option);

        Log.d(LOG_TAG, "getSource: " + protocols[0]);
        Log.d(LOG_TAG, "getSource: " + protocol);

        try {
            Uri uriBuilder;
            if(protocol.equals(protocols)) {
                uriBuilder = UriFactory(query, "http");
            } else {
                uriBuilder = UriFactory(query, "https");
            }

            URL requestURL = new URL(uriBuilder.toString());

            httpURLConnection = (HttpURLConnection) requestURL.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            sourceCode = SourceToString(bufferedReader);

        } catch (IOException ioException) {
            ioException.printStackTrace();

        } finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sourceCode;
    }

    private static Uri UriFactory(String query, String protocol) {
        return Uri.parse(query).buildUpon().scheme(protocol).build();
    }

    private static String SourceToString(BufferedReader bufferedReader)
           throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }

        if (stringBuilder.length() == 0){
            return null;
        }
        return stringBuilder.toString();
    }
}
