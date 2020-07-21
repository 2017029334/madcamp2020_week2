package com.example.youtube.gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youtube.R;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<Image> images;
    public List<String> imageNames = new ArrayList<>();;

    PostAdapter(Context context){
        this.context = context;
    }

////    PostAdapter(Context context, List<String> imageNames) {
////        this.context = context;
////        this.imageNames = imageNames;
////    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item, parent, false);
        PostViewHolder viewHolder = new PostViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        final String imageName = imageNames.get(position);
//        holder.postTitle.setText(image.getTitle());
//        holder.postDescription.setText(image.getContent());

        Glide.with(holder.itemView.getContext())
                .load("http://192.249.19.243:8980/api/uploads/"+ imageName)
                .into(holder.postImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FullImageActivity.class);
                intent.putExtra("String", imageName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageNames.size();
    }

    public void setItem(List<String> imageNames){
        this.imageNames = imageNames;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView postImage;

        public PostViewHolder (View itemView){
            super(itemView);
            postImage = itemView.findViewById(R.id.image_below_card);
        }
    }
}
