package whattodo.whattodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogout, btnFind;
    private CheckBox checkFood, checkAlcohol, checkActive, checkTourism, checkGroup;
    private SeekBar seekBar;
    private EditText etPrice;
    private TextView TextUsername, textDistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnLogout =  findViewById(R.id.btnLogout);
        btnFind =  findViewById(R.id.btnFind);
        btnLogout.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        TextUsername = findViewById(R.id.etUsername);
        seekBar = findViewById(R.id.seekBar);
        textDistance = findViewById(R.id.textViewDistance);
        etPrice = findViewById(R.id.etPrice);
        checkFood = findViewById(R.id.checkFood);
        checkAlcohol = findViewById(R.id.checkAlcohol);
        checkActive = findViewById(R.id.checkActive);
        checkTourism = findViewById(R.id.checkTourism);
        checkGroup = findViewById(R.id.checkGroup);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        String massage = "Witaj "+username;
        TextUsername.setText(massage);

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
                startActivity(new Intent(this,MapsActivity.class));
            //    Toast.makeText(Main2Activity.this, "Check box:"+checkFood.isChecked(), Toast.LENGTH_SHORT).show();
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
    }

}
