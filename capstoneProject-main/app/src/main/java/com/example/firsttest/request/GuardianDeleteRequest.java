package com.example.firsttest.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GuardianDeleteRequest extends StringRequest {
    final static private String URL ="http://210.117.128.200:8080/deleteGuardian.php";

    private Map<String,String> parameters;

    public GuardianDeleteRequest(String id, Response.Listener<String>listener){
        super(Method.POST, URL, listener,null);

        parameters = new HashMap<>();
        parameters.put("id", id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
