package edu.iastate.pal.network;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Volley utility methods class.
 *
 * @author Evan Lambert
 * @author Nathan Cool
 */
public class VolleyUtils {

    private static Uri.Builder buildBaseURL() {
        // Builds the url and passes the queries
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http");
        uri.encodedAuthority("proj-309-yt-02.cs.iastate.edu:8080");
        return uri;
    }

    private static String returnURL(Uri.Builder uri, HashMap<String, String> map){
        if(!map.containsKey("path")) {
            throw new IllegalArgumentException("Please enter a path");
        }

        uri.appendPath(map.get("path"));
        map.remove("path");

        Iterator iterator = map.entrySet().iterator();   //Iterator to move through hash map
        while(iterator.hasNext()){
            HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>)iterator.next();
            uri.appendQueryParameter(entry.getKey(), entry.getValue());
            iterator.remove();
        }

        return uri.build().toString();
    }

    public static void volleyCall(Context context, HashMap<String, String> data, final VolleyResponseListener listener) {
        Uri.Builder uri = buildBaseURL();
        String url = returnURL(uri, data);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.toString());
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
