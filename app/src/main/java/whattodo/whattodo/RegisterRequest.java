package whattodo.whattodo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest{
    private static String REGISTER_URL = "https://whattodowebservice.azurewebsites.net/register";

    private Map<String,String> params;

    public RegisterRequest(String email, String username, String password, Response.Listener<String> listener){
        super(Request.Method.GET, REGISTER_URL,listener,null);
        params = new HashMap<>();
        params.put("login",username);
        params.put("password",password);
        params.put("email",email);
    }

    public Map<String,String> getParams(){
        return params;
    }


}
