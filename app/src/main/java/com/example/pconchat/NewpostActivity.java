package com.example.pconchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import io.grpc.Compressor;

public class NewpostActivity extends AppCompatActivity {

    Button newpostbtn;
EditText newdesctext;
ImageView addimage;
Uri postimageuri=null;
ProgressBar progressBar2;
FirebaseFirestore firebaseFirestore;
StorageReference storagerefrence;
FirebaseAuth mAuth;
String current_user;
    Compressor compressorimage;
    String downloadurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);
        addimage=findViewById(R.id.new_post_image);
        mAuth=FirebaseAuth.getInstance();
        current_user=mAuth.getCurrentUser().getUid();
        storagerefrence=FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        newdesctext=findViewById(R.id.new_post_desc);
        newpostbtn=findViewById(R.id.postbtn);
        progressBar2=findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.INVISIBLE);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);

            }
        });


        newpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc=newdesctext.getText().toString();
                if(!TextUtils.isEmpty(desc)&& postimageuri!=null)
                {
                    progressBar2.setVisibility(View.VISIBLE);
                    final String  randomName= UUID.randomUUID().toString();
                    UploadTask filepath=storagerefrence.child("Post Image").child(randomName+".jpg").putFile(postimageuri);
                    filepath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if(task.isSuccessful()){

                                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                            downloadurl=uri.toString();

                                    }
                                });

                                UploadTask uploadTask =storagerefrence.child("Post_Images/Thumbs").child(randomName+".jpg").putFile(postimageuri);
                          uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                              @Override
                              public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                                  Map<String,Object>postmap=new HashMap<>();
                                  postmap.put("image_url",downloadurl);
                                  postmap.put("desc",desc);
                                  postmap.put("user",current_user);
                                  postmap.put("timestamp",FieldValue.serverTimestamp());
                                  firebaseFirestore.collection("Posts").add(postmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                      @Override
                                      public void onComplete(@NonNull Task<DocumentReference> task) {

                                          if(task.isSuccessful())
                                          {
                                              Toast.makeText(NewpostActivity.this, " Post Is Added",Toast.LENGTH_LONG).show();
                                              Intent i=new Intent(NewpostActivity.this,MainActivity.class);
                                              startActivity(i);
                                              finish();

                                          }
                                          else
                                          { String error=task.getException().getMessage();
                                              Toast.makeText(NewpostActivity.this, " Error:"+error, Toast.LENGTH_LONG).show();

                                          }
                                          progressBar2.setVisibility(View.INVISIBLE);
                                      }
                                  });

                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {

                              }
                          });

                            }
                            else
                            {
                             progressBar2.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 &&resultCode==RESULT_OK && data!=null &&data.getData()!=null)
        {
           postimageuri=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),postimageuri);
               addimage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
