package edu.iastate.pal.network;

/**
 * Volley interface
 *
 * @author Evan Lambert
 */
public interface VolleyResponseListener {
    void onResponse(Object response);
    void onError(String message);
}
