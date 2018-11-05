package whattodo.whattodo;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin,btnRegister;
    EditText etUsername, etPassword;

  //  UserLocal userLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPasword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                final String username = etUsername.getText().toString();
                final String passowrd = etPassword.getText().toString();

           //     LoginRequest loginRequest = new LoginRequest(username,passowrd,responseListener);
                String url ="https://whattodowebservice.azurewebsites.net/login?login="+username+"&password="+passowrd;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("result");
                                    String preferences = jsonObject.getString("preferences");
                                    Log.i("array","array: "+preferences);
                                    if(success.equals("1")){
                                        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                                        intent.putExtra("username",username);

                                        MainActivity.this.startActivity(intent);
                                    }else{
                                        AlertDialog.Builder bulder = new AlertDialog.Builder(MainActivity.this);
                                        bulder.setMessage("Logowanie nieudane")
                                                .setNegativeButton("spróbuj ponownie: "+success,null)
                                                .create()
                                                .show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder bulder = new AlertDialog.Builder(MainActivity.this);
                        bulder.setMessage("Error")
                                .setNegativeButton("Error",null)
                                .create()
                                .show();
                    }
                });
            /*     prawdopodobnie źle dodaje parametry / inna konstrukcja url
            {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("login",username);
                        params.put("password",passowrd);
                        return params;
                    }
                };*/

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
              //  Log.i("url",stringRequest.toString(),null);
                queue.add(stringRequest);
                break;
            case R.id.btnRegister:
                startActivity(new Intent(this,Register.class));
                break;
        }
    }
}
