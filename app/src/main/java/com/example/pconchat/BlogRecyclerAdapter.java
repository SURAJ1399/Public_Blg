package com.example.pconchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blog_list;
    Context context;
    private FirebaseFirestore firebaseFirestore;
   public ImageView bloglike;

    TextView bloglikecount;


    public BlogRecyclerAdapter(List<BlogPost> blog_list) {
        this.blog_list = blog_list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloglistitem, parent, false);
        context = parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String blogpostid=blog_list.get(position).BlogPostid;
        final String currentussrid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String descdata = blog_list.get(position).getDesc();
        holder.setdesctext(descdata);
        String imageurl = blog_list.get(position).getImage_url();
        holder.setblogimage(imageurl);
        //Getlikescount

        firebaseFirestore.collection("Posts").document(blogpostid).collection("Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
             if(!queryDocumentSnapshots.isEmpty()){
                 int count=queryDocumentSnapshots.size();
                 holder.getlikescount(count);
             }
             else
             {
                 holder.getlikescount(0);
             }
            }
        });















//Getlikes
        firebaseFirestore.collection("Posts").document(blogpostid).collection("Likes").document(currentussrid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("NewApi")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
           if(documentSnapshot.exists())
           {
               holder.bloglike.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_red_24dp));
           }
           else {
               holder.bloglike.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_black_24dp));
           }
            }
        });

        //likefeatures
        holder.bloglike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Posts").document(blogpostid).collection("Likes").document(currentussrid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                     if(!task.getResult().exists())
                     {
                         Map<String,Object>likemap=new HashMap<>();
                         likemap.put("timestamp",FieldValue.serverTimestamp());
                         firebaseFirestore.collection("Posts").document(blogpostid).collection("Likes").document(currentussrid).set(likemap);

                     }
                     else
                     {
                         firebaseFirestore.collection("Posts").document(blogpostid).collection("Likes").document(currentussrid).delete();
                     }
                    }
                });

                          }
        });

        ///Commentsection

        holder.commentpagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,CommentActivity.class);
                i.putExtra("blogpostid",blogpostid);
                context.startActivity(i);
            }
        });













        //timefeatures
        long millisec = blog_list.get(position).getTimestamp().getTime();


        //timestamp to time convert
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(millisec);
        holder.setTime(formatter.format(cal.getTime()));

        final String userid = blog_list.get(position).getUser();
        firebaseFirestore.collection("Users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String Username = task.getResult().getString("Name");
                    holder.setuserdata(Username);
                   String Userimage = task.getResult().getString("Image");
                   holder.setuserimage(Userimage);


                } else {
                    //Error Handling;
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView bloglike;
        TextView descview;
        ImageView blogimageview;
        View mview;
        Button commentpagebtn;
        Context context1;
        private TextView blogdate;
        TextView blogusername;
        CircleImageView bloguserimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
            bloglike=mview.findViewById(R.id.bloglike);
            commentpagebtn=mview.findViewById(R.id.commentpagebtn);


        }

        public void setdesctext(String desctext) {
            descview = mview.findViewById(R.id.blogdesc);
            descview.setText(desctext);
        }

        public void setblogimage(String downloaduri) {
            blogimageview = mview.findViewById(R.id.blogimageview);
            Glide.with(context).load(downloaduri).into(blogimageview);

        }

        public void setTime(String date) {
            blogdate = mview.findViewById(R.id.blogdate);
            blogdate.setText(date);
        }


        public void setuserdata(String Name){


            blogusername=mview.findViewById(R.id.blogusername);
            blogusername.setText(Name);


        }
        public void setuserimage(String Image)
        {
            bloguserimage=mview.findViewById(R.id.bloguserimage);
            Glide.with(context).load(Image).into(bloguserimage);
        }
        public void getlikescount(int count)
        {
            bloglikecount=mview.findViewById(R.id.bloglikescount);
            bloglikecount.setText(count+" "+"Likes");
        }
    }

}