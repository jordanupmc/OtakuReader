package com.example.otakureader.tools.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.otakureader.R;
import com.example.otakureader.api.pojo.MangaPOJO;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MangaGridAdapter extends RecyclerView.Adapter<MangaGridAdapter.MyViewHolder> {
    public interface OnItemGridClickListener {
        void onItemClick(MangaPOJO item);
    }

    private List<MangaPOJO> mDataset;
    private Context context;
    private final OnItemGridClickListener listener;

    public MangaGridAdapter(List<MangaPOJO> myDataset, OnItemGridClickListener listener) {
        this.mDataset = myDataset;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        context = parent.getContext();
        LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_manga_grid, parent, false);

        MyViewHolder vh = new MyViewHolder(ll);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MangaPOJO currentManga = mDataset.get(position);
        String imgUrl = "";
        ImageView imageView = holder.getLinearLayout().findViewById(R.id.mangaImg);
        if(currentManga.getImage() != null){
            imgUrl += context.getString(R.string.api_image_url) + currentManga.getImage();
        }else{
            imgUrl +=  context.getString(R.string.default_img_path)+ "default_image"+new Random().nextInt(10)+".jpg";
        }
        Glide.with(context).load(imgUrl).into(imageView);


        TextView textView =  holder.getLinearLayout().findViewById(R.id.mangaTitle);
        textView.setText(currentManga.getTitle());

        holder.getLinearLayout().removeAllViews();
        holder.getLinearLayout().addView(imageView);
        holder.getLinearLayout().addView(textView);

        holder.bind(currentManga, listener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;

        public MyViewHolder(LinearLayout v) {
            super(v);
            linearLayout = v;
        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }


        public void bind(final MangaPOJO item, final OnItemGridClickListener listener) {
            linearLayout.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
