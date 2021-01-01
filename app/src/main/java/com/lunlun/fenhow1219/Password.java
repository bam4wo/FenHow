package com.lunlun.fenhow1219;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Intent intent = getIntent();
        String id = intent.getStringExtra("user");
        TextInputEditText old = findViewById(R.id.old);
        TextInputEditText newp = findViewById(R.id.password);
        TextInputEditText newp2 = findViewById(R.id.password2);

        String getold, newpass, newpass2;
        getold = String.valueOf(old.getText());
        newpass = String.valueOf(newp.getText());
        newpass2 = String.valueOf(newp2.getText());

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getold.equals("") && !newpass.equals("") && !newpass2.equals("")) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[1];
                            field[0] = "oldpassword";
                            //Creating array for data
                            String[] data = new String[1];
                            data[0] = getold;
                            PutData putData = new PutData("http://192.168.1.109/Hospital/oldPass.php", "POST", field, data); //網址要改成自己的php檔位置及自己的ip
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Oldpassword Correct")) {

                                    } else {
                                        Toast.makeText(getApplicationContext(), "舊密碼錯誤！", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }


}