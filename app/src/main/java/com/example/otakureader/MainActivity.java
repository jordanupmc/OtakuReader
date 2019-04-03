package com.example.otakureader;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.otakureader.tools.Chapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.example.otakureader.ChapterSelectActivity.MANGA_ID;

public class MainActivity extends AppCompatActivity {

    FavorisFragment fav;
    SearchFilterFragment searchList;
    BottomNavigationView navigation;
    MangaListFragment mangaList;
    Fragment currentFragment = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);

        navigation.setSelectedItemId(R.id.action_favorite);
        fav = new FavorisFragment();
        searchList=new SearchFilterFragment();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        if (savedInstanceState == null) {
            showFragment(fav);
        } else {
            int frag = savedInstanceState.getInt("frag");
            restaureFragment(frag);
        }


        //TODO revoir efficacité
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_favorite:
                    if (fav == null) {
                        fav = new FavorisFragment();
                    }
                    currentFragment = fav;
                    showFragment(fav);
                    return true;
                case R.id.action_trending:
                    if (mangaList == null) {
                        mangaList = new MangaListFragment();
                    }
                    currentFragment = mangaList;
                    showFragment(mangaList);
                    return true;

                case R.id.action_search:
                    currentFragment=searchList;
                    showFragment(searchList);
                    return true;
            }
            return false;
        });

    }

    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        icicle.putInt("frag", currentFragment == null ? fav.getId() : currentFragment.getId());
    }

    private void restaureFragment(int frag){
        switch (frag) {
            case R.id.frag_favoris:
                showFragment(fav);
                break;
            case R.id.frag_list:
                showFragment(mangaList);
                break;

            case R.id.frag_search:
                showFragment(searchList);
                break;
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);

        SearchView searchView =
                (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()<3) {
                    return false;
                } else {
                    searchList = SearchFilterFragment.newInstance(query);
                    showFragment(searchList);
                    searchView.clearFocus();
                    searchItem.collapseActionView();
                    navigation.setSelectedItemId(R.id.action_search);
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>=3){
                    searchView.setSubmitButtonEnabled(true);
                }else{
                    searchView.setSubmitButtonEnabled(false);
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}