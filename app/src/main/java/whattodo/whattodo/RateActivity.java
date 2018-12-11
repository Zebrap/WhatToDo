package whattodo.whattodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RateActivity extends AppCompatActivity {
    Button btnSandRate;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        btnSandRate = findViewById(R.id.btnSendRate);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        btnSandRate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int rate = (int)ratingBar.getRating();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String username = pref.getString("username", "");
                String attraction_id = pref.getString("attraction_id", "");
                String url ="https://whattodowebservice.azurewebsites.net/rate?login="+username+"&attractionid="+attraction_id+"&mark="+rate;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(RateActivity.this, "Wystawiona ocena: "+rate, Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("find","false");
                                editor.commit();
                                Intent intent = new Intent(RateActivity.this,Main2Activity.class);
                                RateActivity.this.startActivity(intent);
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RateActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue queue = Volley.newRequestQueue(RateActivity.this);
                queue.add(stringRequest);


            }
        });
    }
}
