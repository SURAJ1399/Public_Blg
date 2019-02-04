  package com.example.pconchat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    CircleImageView setupImage;
    Button namesavebtn;
    EditText nametext;
    Uri uriimage=null;
    String userid;
  //  Uri downloaduri;
    boolean ischanged=false;
    StorageReference imagepath;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    String downloadurl;
    private FirebaseFirestore firebaseFirestore;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        userid=firebaseAuth.getCurrentUser().getUid();
        setupImage = findViewById(R.id.circleimage_view);
        progressBar=findViewById(R.id.progressBar);
        namesavebtn=findViewById(R.id.namesave_btn);
        nametext=findViewById(R.id.name_save);
        progressBar.setVisibility(View.VISIBLE);
        namesavebtn.setEnabled(false);
        firebaseFirestore.collection("Users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()) {

                        String name=task.getResult().getString("Name");
                       String image=task.getResult().getString("Image");
                        Toast.makeText(SetupActivity.this, "Hello:"+name, Toast.LENGTH_LONG).show();

                        nametext.setText(name);
                          userid=firebaseAuth.getCurrentUser().getUid();

                      //  Glide.with(SetupActivity.this).load(userid+".jpg").into(setupImage);
                       Glide.with(SetupActivity.this).load(image).into(setupImage);
                        //Picasso.with(SetupActivity.this).load(image).into(setupImage);

                    }
                    else
                    {
                        Toast.makeText(SetupActivity.this, "Data Doesn't Exists:", Toast.LENGTH_LONG).show();

                    }


                }
                else
                { String error=task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Firestore Retrive Error:"+error, Toast.LENGTH_LONG).show();


                }
                namesavebtn.setEnabled(true);
            }
        });

        progressBar.setVisibility(View.INVISIBLE);
        namesavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = nametext.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (ischanged) {

                    Toast.makeText(SetupActivity.this, "Welcome!-:" + username, Toast.LENGTH_LONG).show();
                    if (!TextUtils.isEmpty(username)) {
                        userid = firebaseAuth.getCurrentUser().getUid();
                        progressBar.setVisibility(View.VISIBLE);
                     UploadTask imagepath = storageReference.child("Profile Images").child(userid + ".jpg").putFile(uriimage);
                     imagepath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                   task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           if(task.isSuccessful())
                                           {
                                               downloadurl=uri.toString();
                                               storeFireatore(task, username,downloadurl);
                                           }

                                       }
                                   });



                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(SetupActivity.this, "ERROR:" + error, Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }


                            }
                        });

                    }
                }
                else {
                    storeFireatore(null, username,downloadurl);
                }


            }
        });




        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SetupActivity.this, "PERMISSION DENIED", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        Intent intent =new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);


                    }
                }

            }
        });


    }

    public void storeFireatore(@NonNull Task<UploadTask.TaskSnapshot> task, String username ,String downloadurl) {

           /*  if(task==null)
        {   downloadurl=uriimage.toString();
        }*/
        Map<String,String>  usermap= new HashMap<>();
        usermap.put("Name", username);
        usermap.put("Image",downloadurl);
        firebaseFirestore.collection("Users").document(userid).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SetupActivity.this, "Settings Are Uploaded", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(SetupActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();


                }
                else
                {
                    String error=task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "FIRESTORE ERROR:"+error, Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 &&resultCode==RESULT_OK && data!=null &&data.getData()!=null)
        {
            uriimage=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriimage);
                setupImage.setImageBitmap(bitmap);
                ischanged=true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}


