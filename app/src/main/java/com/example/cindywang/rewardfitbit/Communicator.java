package com.example.cindywang.rewardfitbit;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by cindywang on 1/31/16.
 */
public class Communicator {
    public JSONArray executeHttpGet(String URL) throws Exception
    {
        BufferedReader in = null;
        try
        {
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
            HttpGet request = new HttpGet();
            request.setHeader("Content-Type", "text/plain; charset=utf-8");
            request.setURI(new URI(URL));
            HttpResponse response = client.execute(request);

            Log.d("communicator", response.getEntity().toString());
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";

            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null)
            {
                sb.append(line + NL);
            }
            in.close();
            String page = sb.toString();
            Log.d("json:", page);

            return new JSONArray(page);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    Log.d("BBB", e.toString());
                }
            }
        }
    }
}