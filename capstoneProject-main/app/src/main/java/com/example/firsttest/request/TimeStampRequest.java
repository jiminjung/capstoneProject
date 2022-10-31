package com.example.firsttest;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TimeStampRequest extends StringRequest {
    final static private String URL="http://210.117.128.200:8080/getTimeStamp.php";

    private Map<String,String> parameters;

    public TimeStampRequest(String id, Response.Listener<String>listener){
        super(Method.POST, URL, listener,null);

        parameters = new HashMap<>();
        parameters.put("id", id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
