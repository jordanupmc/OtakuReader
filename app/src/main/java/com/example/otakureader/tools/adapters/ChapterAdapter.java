package com.example.otakureader.tools.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakureader.R;
import com.example.otakureader.tools.Chapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChapterAdapter extends ArrayAdapter<Chapter> {

    private final int layout;
    private String currentChapterId;

    public ChapterAdapter(@NonNull Context context, int resource, @NonNull List<Chapter> objects) {
        super(context, resource, objects);
        layout = resource;
    }

    // Pour éviter l'animation de décochage lors d'une suppresion de l'item précédent qui était coché
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return super.getItem(position).getLocalId();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            // Create new view
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(layout, null);
        } else {
            view = convertView;
        }

        final Chapter item = getItem(position);
        // Recycle view
        final TextView numberView = view.findViewById(R.id.chapNumber);
        numberView.setText(String.valueOf(item.getNumber()));

        final TextView titleView = view.findViewById(R.id.chapTitle);
        if (item.getTitle() == null || item.getNumber().equals(item.getTitle())) {
            titleView.setText("Chapter " + item.getNumber());
        } else {
            titleView.setText(item.getTitle());
        }

        final ImageView imageView = view.findViewById(R.id.readingStatus);

        if (item.isStatus() != null) {
            if (item.isStatus()) {
                //LECTURE FINI
                imageView.setBackground(view.getResources().getDrawable(R.drawable.book_outline));
            } else {
                //EN COURS DE LECTURE
                imageView.setBackground(view.getResources().getDrawable(R.drawable.book_open_page_variant));
            }
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
        final TextView dateView = view.findViewById(R.id.chapDate);
        dateView.setText(item.getDate());

        if (currentChapterId != null && currentChapterId.equals(item.getId())) {
            numberView.setTextColor(view.getResources().getColor(R.color.secondaryTextColor));
            titleView.setTextColor(view.getResources().getColor(R.color.secondaryTextColor));
            dateView.setTextColor(view.getResources().getColor(R.color.secondaryTextColor));
        } else {
            numberView.setTextColor(view.getResources().getColor(R.color.primaryTextColor));
            titleView.setTextColor(view.getResources().getColor(R.color.primaryTextColor));
            dateView.setTextColor(view.getResources().getColor(R.color.primaryTextColor));
        }

        return view;

    }

    public void setCurrentChapterId(String id) {
        currentChapterId = id;
    }
}