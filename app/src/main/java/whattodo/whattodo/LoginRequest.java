package whattodo.whattodo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest  extends StringRequest {
    private static String LOGIN_URL = "https://whattodowebservice.azurewebsites.net/login";

    private Map<String,String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener){
        super(Method.GET, LOGIN_URL, listener, null);
        params = new HashMap<>();
        params.put("login",username);
        params.put("password",password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
