package com.example.pconchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText reg_email_field;
    private  EditText reg_Pass_field;
    private  EditText reg_confirm_Pass_field;
    private Button reg_Btn;
    private Button reg_login_btn;
    private FirebaseAuth mAuth;
    private ProgressBar reg_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance() ;
        reg_email_field=findViewById(R.id.reg_email);
        reg_Pass_field=findViewById(R.id.reg_pass);
        reg_confirm_Pass_field=findViewById(R.id.reg_confirm_pass);
        reg_Btn=findViewById(R.id.reg_btn);
        reg_login_btn=findViewById(R.id.reg_login_btn);
        reg_progress=findViewById(R.id.reg_progerss);
        reg_progress.setVisibility(View.INVISIBLE);
        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginintent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(loginintent);
                finish();
            }
        });
        reg_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=reg_email_field.getText().toString();
                String pass=reg_Pass_field.getText().toString();
                String confirmpass=reg_confirm_Pass_field.getText().toString();
                if( !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(confirmpass)){
                    if(pass.equals(confirmpass)){
                        reg_progress.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent setupintent=new Intent(RegisterActivity.this,SetupActivity.class);
                                    startActivity(setupintent);
                                    finish();
                                }
                                else{
                                    String errormessage=task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this,"Error:"+errormessage,Toast.LENGTH_LONG).show();
                                }
                                reg_progress.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Confirm Password And Password Field Doesn't Match.",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            sendtoMain();
        }
    }

    private void sendtoMain() {
        Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
