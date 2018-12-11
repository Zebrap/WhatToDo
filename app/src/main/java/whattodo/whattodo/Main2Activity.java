package whattodo.whattodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.http.GET;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener, LocationEngineListener, PermissionsListener {
    private Button btnLogout, btnFind, btnRate;
    private CheckBox checkFood, checkAlcohol, checkActive, checkTourism, checkGroup;
    private SeekBar seekBar, seekTime;
    private EditText etPrice;
    private TextView TextUsername, textDistance, textTime;
    private PermissionsManager permissionsManager;
    private Location originLocation;
    private LocationEngine locationEngine;
    private String username;
    private String activeRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnLogout =  findViewById(R.id.btnLogout);
        btnFind =  findViewById(R.id.btnFind);
        btnRate = findViewById(R.id.btnRate);
        btnLogout.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnRate.setOnClickListener(this);
        btnRate.setEnabled(false);
        TextUsername = findViewById(R.id.etUsername);
        seekBar = findViewById(R.id.seekBar);
        textDistance = findViewById(R.id.textViewDistance);
        etPrice = findViewById(R.id.etPrice);
        checkFood = findViewById(R.id.checkFood);
        checkAlcohol = findViewById(R.id.checkAlcohol);
        checkActive = findViewById(R.id.checkActive);
        checkTourism = findViewById(R.id.checkTourism);
        checkGroup = findViewById(R.id.checkGroup);
        seekTime = findViewById(R.id.seekBarTime);
        textTime = findViewById(R.id.textViewTime);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        if (pref.contains("username")) {
            TextUsername.setText(pref.getString("username", ""));
            username = pref.getString("username", "");
            etPrice.setText(pref.getString("price",""));
            boolean check = Boolean.parseBoolean(pref.getString("food",""));
            checkFood.setChecked(check);
            check = Boolean.parseBoolean(pref.getString("alcohol",""));
            checkAlcohol.setChecked(check);
            check = Boolean.parseBoolean(pref.getString("group",""));
            checkGroup.setChecked(check);
            check = Boolean.parseBoolean(pref.getString("tourism",""));
            checkTourism.setChecked(check);
            check = Boolean.parseBoolean(pref.getString("active",""));
            checkActive.setChecked(check);
            int progress = Integer.parseInt(pref.getString("distance",""));
            textDistance.setText(pref.getString("distance","")+" km");
            seekBar.setProgress(progress-1);
            progress = Integer.parseInt(pref.getString("time",""));
            textTime.setText(pref.getString("time","")+" h");
            seekTime.setProgress(progress-1);
            activeRate = pref.getString("find","");
            if(activeRate.equals("1")){
                btnRate.setEnabled(true);
            }else{
                btnRate.setEnabled(false);
            }
        }

    /*    String massage = "Witaj "+username;
        TextUsername.setText(massage);*/

        enableLocation();
        seekbarr();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnLogout:

                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.btnFind:
                btnFind.setEnabled(false);
                SetPref();
        //       Toast.makeText(Main2Activity.this, "long: "+originLocation.getLongitude()+" lat:"+originLocation.getLatitude(), Toast.LENGTH_SHORT).show();

           //     startActivity(new Intent(this,MapsActivity.class));
            //    Toast.makeText(Main2Activity.this, "Check box:"+checkFood.isChecked(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnRate:
                startActivity(new Intent(this,RateActivity.class));
                break;
        }
    }


    public void seekbarr(){
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progres_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progres_value = progress+1;
                        textDistance.setText(progres_value+" Km");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        textDistance.setText(progres_value+" Km");
                    }
                }
        );

        seekTime.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progres_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progres_value = progress+1;
                        textTime.setText(progres_value+" h");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        textTime.setText(progres_value+" h");
                    }
                }
        );
    }
    @SuppressWarnings("MissingPermission")
    public void SetPref(){
        String url = "https://whattodowebservice.azurewebsites.net/set_pref";

        int price = 0;
        if(etPrice.getText().toString().isEmpty()){

        }else{
            price = Integer.parseInt(etPrice.getText().toString().trim());
        }
        Boolean food = checkFood.isChecked();
        Boolean alcohol = checkAlcohol.isChecked();
        Boolean group = checkGroup.isChecked();
        Boolean tourism = checkTourism.isChecked();
        Boolean active = checkActive.isChecked();
        int distance = seekBar.getProgress()+1;
        int time = seekTime.getProgress()+1;
    //   String location = ""+originLocation.getLongitude()+" "+originLocation.getLatitude();
        Location lastLocation = locationEngine.getLastLocation();
        locationEngine.activate();
        locationEngine.addLocationEngineListener(this);
        String location = "";
        if (lastLocation != null) {
             location = ""+originLocation.getLatitude()+" "+originLocation.getLongitude();
        }


/*      Map<String, String> params = new HashMap<String, String>();
        params.put("Login", "admin");
        params.put("Cena", "20");
        params.put("Odleglosc", "5");
        params.put("Jedzenie", "false");
        params.put("Alkohol", "false");
        params.put("Grupowe", "false");
        params.put("Zabytki", "false");
        params.put("Aktywne", "false");
        params.put("Lokalizacja", "54.372496 18.618783");
        params.put("CzasMin", "0");
        params.put("CzasMax", "5");*/

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("Login", username);
            jsonObj.put("Cena", price);
            jsonObj.put("Odleglosc", distance);
            jsonObj.put("Jedzenie", food);
            jsonObj.put("Alkohol", alcohol);
            jsonObj.put("Grupowe", group);
            jsonObj.put("Zabytki", tourism);
            jsonObj.put("Aktywne", active);
            jsonObj.put("Lokalizacja", location);
            jsonObj.put("CzasMin", 0);
            jsonObj.put("CzasMax", time);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Response ", jsonObj.toString()+" loc:"+location);

        RequestQueue queue = Volley.newRequestQueue(Main2Activity.this);

        JsonObjectRequest getRequest = new JsonObjectRequest(url, jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        try {
                            String result = response.getString("result");
                            if(result.equals("true")){
                                Toast.makeText(Main2Activity.this, "Znalenziono atrakcje", Toast.LENGTH_SHORT).show();
                                String localization = response.getString("Lokalizacja");
                                String Latitude = localization.substring(0,8) ;
                                String Longitude = localization.substring(10,18);
                                Log.d("Response", "localization, long: "+Longitude+" Lat: "+Latitude);

                                Intent intent = new Intent(Main2Activity.this,MapsActivity.class);
                                intent.putExtra("Longitude",Longitude);
                                intent.putExtra("Latitude",Latitude);

                                btnFind.setEnabled(true);
                                Main2Activity.this.startActivity(intent);

                            }else{
                                btnFind.setEnabled(true);
                                Toast.makeText(Main2Activity.this, "Nie udało się", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            btnFind.setEnabled(true);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnFind.setEnabled(true);
                Log.e("Response", error.getMessage(), error);
            }
        }) { //no semicolon or coma
            @Override
            public String getBodyContentType(){
                return "application/json";
            }
        };

        queue.add(getRequest);

    }
    @SuppressWarnings("MissingPermission")
    private void enableLocation(){
        if(permissionsManager.areLocationPermissionsGranted(this)){
            initializedLocationEngine();
        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializedLocationEngine(){
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if(lastLocation!=null){
            originLocation = lastLocation;
        }else{
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            originLocation = location;
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        if (pref.contains("username")) {
            activeRate = pref.getString("find","");
            if(activeRate.equals("1")){
                btnRate.setEnabled(true);
            }else{
                btnRate.setEnabled(false);
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}
