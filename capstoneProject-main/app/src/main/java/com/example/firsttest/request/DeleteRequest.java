package com.example.firsttest.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest {
    final static private String SENIOR_URL ="http://210.117.128.200:8080/deleteSenior.php";

    private Map<String,String> parameters;

    public DeleteRequest(String ip, Response.Listener<String>listener){
        super(Method.POST, SENIOR_URL, listener,null);

        parameters = new HashMap<>();
        parameters.put("userIP", ip);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
