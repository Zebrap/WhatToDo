package whattodo.whattodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity implements View.OnClickListener {
    Button btnRegister;
    EditText etUsername, etPassword, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPasword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
          /*      String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String passowrd = etPassword.getText().toString();
                User registerUser = new User(email,username,passowrd);*/
                final String email = etEmail.getText().toString();
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
/*
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AlertDialog.Builder bulder = new AlertDialog.Builder(Register.this);
                        bulder.setMessage("Rejestrcja")
                                .setNegativeButton(response,null)
                                .create()
                                .show();
                        /*
                        try {
                            JSONObject jesonResponse = new JSONObject(response);
                            boolean success = jesonResponse.getBoolean("result");
                            if (success) {
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                Register.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder bulder = new AlertDialog.Builder(Register.this);
                                bulder.setMessage("Rejestacja nieudana")
                                        .setNegativeButton("spróbuj ponownie", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(email,username,password,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(registerRequest);*/
                String url ="https://whattodowebservice.azurewebsites.net/register?login="+username+"&password="+password+"&email="+email;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("result");

                                    if(success.equals("true")){
                                        AlertDialog.Builder bulder = new AlertDialog.Builder(Register.this);
                                        bulder.setMessage("Pomyślna rejestracja")
                                                .setNegativeButton("Zaloguj się",null)
                                                .create()
                                                .show();
                                    }else{
                                        AlertDialog.Builder bulder = new AlertDialog.Builder(Register.this);
                                        bulder.setMessage("Rejestracja nieudana")
                                                .setNegativeButton("spróbuj ponownie: ",null)
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
                        AlertDialog.Builder bulder = new AlertDialog.Builder(Register.this);
                        bulder.setMessage("Error")
                                .setNegativeButton("Error",null)
                                .create()
                                .show();
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(Register.this);
           //     Log.e("url",stringRequest.toString(),null);
                queue.add(stringRequest);
                break;
        }
    }
}
