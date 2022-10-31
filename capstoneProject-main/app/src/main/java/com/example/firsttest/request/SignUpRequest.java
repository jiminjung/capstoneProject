package com.example.firsttest.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignUpRequest extends StringRequest {

    final static private String URL = "http://210.117.128.200:8080/Register.php";
    final static private String URL2 = "http://210.117.128.200:8080/AddRegister.php";
    private Map<String, String> parameters;

    public SignUpRequest(String id, String password, String token, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("password", password);
        parameters.put("token", token);

    }
    public SignUpRequest(String id,String name, String age, String phone_number, Response.Listener<String> listener){
        super(Method.POST, URL2, listener, null);

        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("name", name);
        parameters.put("age", age);
        parameters.put("phone_number", phone_number);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
