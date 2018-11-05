package whattodo.whattodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogout;
    EditText etUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        etUsername = (EditText) findViewById(R.id.etUsername);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        String massage = "Witaj "+username;
        etUsername.setText(massage);

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
        }
    }
}
