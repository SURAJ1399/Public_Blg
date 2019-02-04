package com.example.pconchat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class CommentActivity extends AppCompatActivity {

    Button commentsendbtn;
    EditText commenttext;
    List<Comments>commentList;
    String blogpostid;
    ProgressBar progressBar3;
    FirebaseAuth mAuth;
    CommentsAdapter commentsRecylerAdapter;
    FirebaseFirestore firebaseFirestore;
    String currentuserid;
    RecyclerView commentlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        blogpostid=getIntent().getStringExtra("blogpostid");
       mAuth=FirebaseAuth.getInstance();
       firebaseFirestore=FirebaseFirestore.getInstance();

        commentsendbtn=findViewById(R.id.commentsendbtn);
        commentlist=findViewById(R.id.commentlist);
        ///Recylerview firebase list
        commentList=new ArrayList<>();
        commentsRecylerAdapter=new CommentsAdapter(commentList);
         commentlist.setHasFixedSize(true);
         commentlist.setLayoutManager(new LinearLayoutManager(this));
          commentlist.setAdapter(commentsRecylerAdapter);




       currentuserid=mAuth.getCurrentUser().getUid();
        commenttext=findViewById(R.id.commenttext);
        //comment retrive

        firebaseFirestore.collection("Posts/"+blogpostid+"/Comments").addSnapshotListener(CommentActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
              if(!queryDocumentSnapshots.isEmpty())
               {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String commentid = doc.getDocument().getId();
                        Comments comments= doc.getDocument().toObject(Comments.class);
                        commentList.add(comments);
                        commentsRecylerAdapter.notifyDataSetChanged();

                    }
                    }
                }
            }
        });















        commentsendbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
                String commnetmsg=commenttext.getText().toString();

        if(!commnetmsg.isEmpty())
        {
            Map<String,Object> commentmap=new HashMap<>();
           commentmap.put("message",commnetmsg);
           commentmap.put("currentuserid",currentuserid);
           commentmap.put("timestamp", FieldValue.serverTimestamp());
            firebaseFirestore.collection("Posts/"+blogpostid+"/Comments").add(commentmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(CommentActivity.this, "Error Posting Comment"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {  Toast.makeText(CommentActivity.this, "Comment Posted", Toast.LENGTH_SHORT).show();

                        commenttext.setText("");
                    }
                }
            });
        }
    }
});


    }
}
