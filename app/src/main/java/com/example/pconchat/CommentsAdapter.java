package com.example.pconchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    List<Comments> commentList;
    Context context;





    public CommentsAdapter(List<Comments>commentsList)
    {
        this.commentList=commentsList;
    }
    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.commentlistitem,parent,false);
                context=parent.getContext();
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        String commentmessage=commentList.get(position).getMessage();
        holder.setcomment(commentmessage);
    }

    @Override
    public int getItemCount() {
        if(commentList!=null)
        {
            return commentList.size();
        }
        else
        {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mview;
      public  TextView commentmessage;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mview=itemView;
        }

        public void setcomment (String message)
        {
            commentmessage=mview.findViewById(R.id.commentmessage);
            commentmessage.setText(message);
        }
    }
}
