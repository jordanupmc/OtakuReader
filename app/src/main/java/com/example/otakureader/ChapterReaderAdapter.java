package com.example.otakureader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ChapterReaderAdapter extends FragmentStatePagerAdapter {

    public List<Integer> imgUrl;

    public ChapterReaderAdapter(final FragmentManager fm, final List<Integer> imgUrl) {
        super(fm);
        this.imgUrl = imgUrl;
    }

    @Override
    public int getCount() {
        return imgUrl == null ? 0 : imgUrl.size();
    }

    @Override
    public Fragment getItem(final int position) {
        return ChapterPageFragment.newInstance(position, imgUrl.get(position));
    }


    public static class ChapterPageFragment extends Fragment {
        int mNum;
        int mPage;

        static ChapterPageFragment newInstance(final int num, final int pageId) {
            final ChapterPageFragment f = new ChapterPageFragment();
            final Bundle args = new Bundle();
            args.putInt("num", num);
            args.putInt("page", pageId);
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
            mPage = getArguments() != null ? getArguments().getInt("page") : -1; //TODO Placeholder if not found ?
        }


        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_pager_list, container, false);
            final TouchImageView imageView = view.findViewById(R.id.imgShow);
            imageView.setImageResource(mPage);
            return view;
        }


    }
}
