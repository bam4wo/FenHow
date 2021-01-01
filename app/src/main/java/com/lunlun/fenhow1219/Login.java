package com.lunlun.fenhow1219;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Login extends AppCompatActivity {

    private static final String TAG = Log.class.getSimpleName();
    private static final int REQUEST_CODE = 101;
    boolean isMail = false;
    private Switch sw;

    private String IMEINumber;
    private TextView imei;
    private TextInputEditText textInputEditTextIDorEmail, textInputEditTextPassword;
    private Button buttonLogin;
    private TextView textViewSignUp;
    private ProgressBar progressBar;
    private TextInputLayout textInputLayout;
    private boolean rememberme_checkBox_statue;
    private String userInput;
    private String password;
    private CheckBox rememberme_checkBox;
    private List<DeviceManage> deviceManages;
    DeviceManage dd;
    TouchID touch;
    private ImageView touchID;
    private ImageView faceID;
    private LinearLayout mlinearLayout;

    //  指紋辨識
    private KeyguardManager mKeyguardManager;
    private FingerprintManager mFingerprintManager;
    private CancellationSignal cancellationSignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handleSSLHandshake();
        findViews();
        getImei();
        deviceManages = new ArrayList<>();
        //deviceManages.add(new DeviceManage(1,"357798080499328",0,"null"));

        //checkDivicd();
        if (!IMEINumber.equals("")) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[1];
                    field[0] = "imei_p";
                    //Creating array for data
                    String[] data = new String[1];
                    data[0] = IMEINumber;
                    PutData putData = new PutData("http://192.168.1.109/Hospital/imeiPublic.php", "POST", field, data); //網址要改成自己的php檔位置及自己的ip
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (result.equals("Get IMEI Public Success")) {
                                imei.setText("近期登入");
                                touchID.setVisibility(View.INVISIBLE);
                                faceID.setVisibility(View.INVISIBLE);
                                lololo();
                            } else {
                                mlinearLayout.setVisibility(View.INVISIBLE);
                                imei.setText(IMEINumber);
                            }
                        }
                    }
                }
            });
        }
    }


/*
    private void checkDivicd() {
        Log.d(TAG,"DeviceManage : "+IMEINumber);
        int i;
        for(i=0;i<deviceManages.size();i++){
            if(deviceManages.get(i).getDeviceImei().contains(IMEINumber)){
                imei.setText("公雞公雞呱呱呱");
                touchID.setVisibility(View.INVISIBLE);
                faceID.setVisibility(View.INVISIBLE);
                lololo();
            }else {
                mlinearLayout.setVisibility(View.INVISIBLE);
                imei.setText(IMEINumber);
            }
        }
    }*/

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void lololo() {
        List<HotUserModel> hotUserList = new ArrayList<>();
        hotUserList.add(new HotUserModel(1,"45478",null,"ChiaW","000000"));
        hotUserList.add(new HotUserModel(2,"59487",null,"LanLan","000000"));
        hotUserList.add(new HotUserModel(3,"94520",null,"Jolin","000000"));
        hotUserList.add(new HotUserModel(4,"00708",null,"TomsTost","000000"));
        hotUserList.add(new HotUserModel(5,"56720",null,"菇腦絲","000000"));

        for (int i = 0; i < hotUserList.size(); i++) {
            View inflate2 = LayoutInflater.from(getBaseContext()).inflate(R.layout.login_public_device, mlinearLayout,false);
            CardView cardView = (CardView) inflate2.findViewById(R.id.cd_hot_article);
            ConstraintLayout constraintLayout = (ConstraintLayout) inflate2.findViewById(R.id.constraintLayout);
            String userName = hotUserList.get(i).getUserName();
            String userID = "員編:" + hotUserList.get(i).getUserID();
            ((TextView) inflate2.findViewById(R.id.tv_hot_article_title)).setText(userName);
            ((TextView) inflate2.findViewById(R.id.tv_hot_article_from)).setText(userID);

            int i2 = i % 4;
            if (i2 == 1) {
                constraintLayout.setBackground(getBaseContext().getDrawable(R.drawable.img_card_blue));
            } else if (i2 == 2) {
                constraintLayout.setBackground(getBaseContext().getDrawable(R.drawable.img_card_pink));
            } else if (i2 != 3) {
                constraintLayout.setBackground(getBaseContext().getDrawable(R.drawable.img_card_org));
            } else {
                constraintLayout.setBackground(getBaseContext().getDrawable(R.drawable.img_card_green));
            }
            mlinearLayout.addView(inflate2);
        }

    }

    public void runrun(String userType,String urlType){
        userInput = String.valueOf(textInputEditTextIDorEmail.getText());
        password = String.valueOf(textInputEditTextPassword.getText());

        if (!userInput.equals("") && !password.equals("")) {
            progressBar.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[2];
                    field[0] = userType;
                    field[1] = "password";
                    String[] data = new String[2];
                    data[0] = userInput;
                    data[1] = password;
                    PutData putData = new PutData(urlType, "POST", field, data);
                    if (putData.startPut()) {
                        Log.d(TAG,"userType: "+userType+" / "+ userInput);
                        Log.d(TAG,"urlType: "+urlType);
                        Log.d(TAG,"password: "+ password);
                        if (putData.onComplete()) {
                            progressBar.setVisibility(View.GONE);
                            String result = putData.getResult();
                            Log.d(TAG,"result: " + result);
                            if (result.equals("Login Success")) {
                                Toast.makeText(getApplicationContext(), "登入成功", Toast.LENGTH_LONG).show();
//                                loginSuccess();
                                getIntent().putExtra("USER_NAME", textInputEditTextIDorEmail.getText().toString());
                                setResult(Activity.RESULT_OK, getIntent());
                                Intent intent = new Intent(Login.this,Password.class);
                                intent.putExtra("user",userInput);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "帳號或密碼錯誤", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "All fields require", Toast.LENGTH_LONG).show();
        }
    };

    private void loginSuccess(){
        if (rememberme_checkBox_statue) {
            Log.d(TAG, "rememberme_checkBox_statue is :" + rememberme_checkBox_statue);
            SharedPreferences settingpref = getSharedPreferences("test", MODE_PRIVATE);
            settingpref.edit()
                    .putBoolean("RREF_REMEMBER", rememberme_checkBox_statue)
                    .putString("PREF_IMEI", IMEINumber)
                    .putString("PREF_USERID", userInput)
                    .putString("PREF_PASSWROD", password)
                    .commit();
            Log.d(TAG, "settingpref is :" + rememberme_checkBox_statue + " " + IMEINumber + " " + userInput + " " + password);
        }
    }


    private void findViews() {
        textInputEditTextIDorEmail = findViewById(R.id.old);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.button);
        textViewSignUp = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);
        textInputLayout = findViewById(R.id.textInputLayoutEmployeeId);
        textInputEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        touchID = findViewById(R.id.touchidimageButton);
        faceID = findViewById(R.id.faceidimageButton);
        mlinearLayout = (LinearLayout) findViewById(R.id.linear);

        //切換登入方式
        sw = findViewById(R.id.sw);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isMail = isChecked;
                textInputLayout.setHint(isMail ? getString(R.string.employeeid ): getString(R.string.email));
            }
        });

        CheckBox cb = findViewById(R.id.seepw);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //顯示密碼
                    textInputEditTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //隱藏密碼
                    textInputEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        rememberme_checkBox = findViewById(R.id.rememberme);
//        rememberme_checkBox_statue = getSharedPreferences("test", MODE_PRIVATE).getBoolean("RREF_REMEMBER", false);
//        if (rememberme_checkBox_statue) {
//            SharedPreferences setting = getSharedPreferences("test", MODE_PRIVATE);
//            textInputEditTextIDorEmail.setText(setting.getString("PREF_USERID", ""));
//        }

        //點此註冊
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMail) {
                    runrun("employee_id",getString(R.string.idlogin_php));
                } else {
                    runrun("email",getString(R.string.login_php));
                }
            }
        });

//        textInputEditTextIDorEmail.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                // ignore
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                // ignore
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if(userInput.contains("@")){
//                    Toast.makeText(getApplicationContext(), "@@@@@@@@@@@@2", Toast.LENGTH_LONG).show();
//                    Log.d(TAG,"@@@@@@@@@@@@@@@@2");
//                }
//            }
//        });

        findViewById(R.id.touchidimageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this,"使用指紋辨識",Toast.LENGTH_SHORT).show();
                touch.startFingerprintListening();//開始掃描
            }
        });

        findViewById(R.id.faceidimageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"目前不支援臉部辨識",Snackbar.LENGTH_LONG).show();
            }
        });
    }

    //忽略https的證書校驗
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }


    //取得imei
    public void getImei(){
        imei = findViewById(R.id.ed_imei);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            return;
        }
        IMEINumber = telephonyManager.getDeviceId();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}