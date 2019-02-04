package com.example.pconchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private  EditText loginEmailText;
    private  EditText loginPassText;
    private Button loginBtn;
    private Button loginPageBtn;
    private FirebaseAuth mAuth;
  private   ProgressBar loginprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        loginEmailText=(EditText)findViewById(R.id.login_email);
        loginPassText=(EditText)findViewById(R.id.login_pass);
        loginBtn=(Button)findViewById(R.id.login_btn);
        loginPageBtn=(Button)findViewById(R.id.login_reg_btn);
        loginprogress=findViewById(R.id.login_progerss);
        loginprogress.setVisibility(View.INVISIBLE);
        loginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regIntent);
                finish();
            }
        });






        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail=loginEmailText.getText().toString();
                String loginPassword=loginPassText.getText().toString();
                if(!TextUtils.isEmpty(loginEmail)&&!TextUtils.isEmpty(loginPassword))
                {
                    loginprogress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendtoMain();
                            } else {
                                String errormessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "ERROR:" + errormessage, Toast.LENGTH_SHORT).show();
                            }
                            loginprogress.setVisibility(View.INVISIBLE);
                        }
                    });
                }


            }
        });

    }



    private void sendtoMain() {
        Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }



}
