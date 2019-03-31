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
import com.example.otakureader.tools.Chapter;
import com.example.otakureader.tools.adapters.ChapterAdapter;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import static com.example.otakureader.FullscreenView.CHAPTER_ID;
import static com.example.otakureader.FullscreenView.CHAPTER_LIST;

public class ChapterReaderPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> imgUrl;
    public static ArrayList<Chapter> chapters;
    public static String chapterId;

    public ChapterReaderPagerAdapter(final FragmentManager fm, final List<String> imgUrl, ArrayList<Chapter> chapters, String chapterId) {
        super(fm);
        this.imgUrl = imgUrl;
        ChapterReaderPagerAdapter.chapters = chapters;
        ChapterReaderPagerAdapter.chapterId = chapterId;
    }

    @Override
    public int getCount() {
        return imgUrl == null ? 1 : imgUrl.size() + 1;
    }

    @Override
    public Fragment getItem(final int position) {
        if (position == 0) {
            return LastPageFragment.newInstance();
        }
        return ChapterPageFragment.newInstance(position, imgUrl.get(position - 1));
    }

    public static class LastPageFragment extends Fragment {

        static LastPageFragment newInstance() {
            return new LastPageFragment();
        }

        private int getIndNextChapter() {
            for (int i = chapters.size() - 1; i >= 0; i--) {
                if (chapters.get(i).getId().equals(chapterId)) {
                    return i - 1 >= 0 ? i - 1 : -1;
                }
            }
            return -1;
        }


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.last_element, container, false);
            final ListView listView = view.findViewById(R.id.fs_view_chapter_list);

            final ArrayAdapter<Chapter> adapter = new ChapterAdapter(view.getContext(), R.layout.content_chapter, ChapterReaderPagerAdapter.chapters);


            listView.setAdapter(adapter);

            final Button next = view.findViewById(R.id.nextChapterBtn);
            final int nextChapterInd = getIndNextChapter();

            next.setVisibility(View.VISIBLE);
            if (nextChapterInd < 0) {
                next.setVisibility(View.GONE);
            }
            if (nextChapterInd > 0) {
                listView.setSelection(nextChapterInd - 1);
            }

            next.setOnClickListener(v -> {
                final Intent intent = new Intent(getActivity(), FullscreenView.class);
                intent.putExtra(CHAPTER_ID, chapters.get(nextChapterInd).getId());
                intent.putExtra(CHAPTER_LIST, chapters);

                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            });

            listView.setOnItemClickListener((adapterView, v, position, l) -> {
                final Intent intent = new Intent(v.getContext(), FullscreenView.class);
                intent.putExtra(CHAPTER_ID, chapters.get(position).getId());
                intent.putExtra(CHAPTER_LIST, chapters);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
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
            mPage = getArguments() != null ? getArguments().getString("page") : "";
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
