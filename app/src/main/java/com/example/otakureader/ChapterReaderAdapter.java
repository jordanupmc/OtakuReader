package com.example.otakureader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
        // TODO Ajouter un new fragment pour : if(position==imgUrl.size()+1)
        if (position == imgUrl.size()) {
            return ChapterPageFragment.newInstance(position, "");
        }
        return ChapterPageFragment.newInstance(position, imgUrl.get(position));
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


        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
            mPage = getArguments() != null ? getArguments().getString("page") : ""; //TODO Placeholder if not found ?
        }


        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
            final View view;
            if (!mPage.equals("")) {
                view = inflater.inflate(R.layout.fragment_pager_list, container, false);
                final PhotoView imageView = view.findViewById(R.id.imgShow);

                Glide.with(this).load(mPage).into(imageView);
            } else {
                //TODO WIP CHAPTER
                view = inflater.inflate(R.layout.last_element, container, false);
                final ListView listView = view.findViewById(R.id.fs_view_chapter_list);

                final ArrayList<String> data = new ArrayList<>(Arrays.asList("Chap1", "Chap2", "Chap3"));
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, data);


                listView.setAdapter(adapter);
            }

            return view;

        }


    }
}
