package whattodo.whattodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

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
                Toast.makeText(RateActivity.this, "Wystawiona ocena: "+rate, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
