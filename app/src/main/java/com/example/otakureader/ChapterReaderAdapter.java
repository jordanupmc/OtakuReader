package com.example.otakureader;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import static com.example.otakureader.FullscreenView.CHAPTER_ID;

public class ChapterReaderAdapter extends FragmentStatePagerAdapter {

    public List<String> imgUrl;

    public ChapterReaderAdapter(final FragmentManager fm, final List<String> imgUrl) {
        super(fm);
        this.imgUrl = imgUrl;
    }

    @Override
    public int getCount() {
        return imgUrl == null ? 1 : imgUrl.size() + 1;
    }

    @Override
    public Fragment getItem(final int position) {
        if (position == imgUrl.size()) {
            return LastPageFragment.newInstance();
        }
        return ChapterPageFragment.newInstance(position, imgUrl.get(position));
    }

    public static class LastPageFragment extends Fragment {
        static LastPageFragment newInstance() {
            return new LastPageFragment();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.last_element, container, false);
            final ListView listView = view.findViewById(R.id.fs_view_chapter_list);

            final ArrayList<String> data = new ArrayList<>(Arrays.asList("Chap1", "Chap2", "Chap3"));
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, data);
            listView.setAdapter(adapter);
            final Button next = view.findViewById(R.id.nextChapterBtn);
            next.setOnClickListener(v -> {
                final Intent intent = new Intent(getActivity(), FullscreenView.class);
                //TODO
                intent.putExtra(CHAPTER_ID, "4e711cb0c09225616d037cc2");

                startActivity(intent);
                getActivity().finish();
            });
            return view;
        }
    }


    public static class ChapterPageFragment extends Fragment {
        int mNum;
        String mPage;

        static ChapterPageFragment newInstance(final int num, final String pageUrl) {
            final ChapterPageFragment f = new ChapterPageFragment();
            final Bundle args = new Bundle();
            args.putInt("num", num);
            args.putString("page", pageUrl);
            f.setArguments(args);
            return f;
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
            mPage = getArguments() != null ? getArguments().getString("page") : ""; //TODO Placeholder if not found ?
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_pager_list, container, false);
            final PhotoView imageView = view.findViewById(R.id.imgShow);
            final ProgressBar progressBar = view.findViewById(R.id.pbChapterAdapter);
            final Button btnRefresh = view.findViewById(R.id.btnRefresh);

            progressBar.setVisibility(View.VISIBLE);

            btnRefresh.setOnClickListener(v -> {
                downloadImg(imageView, progressBar, btnRefresh);
            });

            downloadImg(imageView, progressBar, btnRefresh);
            return view;

        }

        private void downloadImg(PhotoView imageView, ProgressBar progressBar, Button btnRefresh) {
            Glide.with(this).load(mPage).listener(new RequestListener<Drawable>() {

                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    btnRefresh.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    btnRefresh.setVisibility(View.GONE);
                    return false;
                }
            }).into(imageView);
        }


    }
}
