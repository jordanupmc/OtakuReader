package com.example.otakureader;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    FavorisFragment fav;
    SearchFilterFragment searchList;
    BottomNavigationView navigation;
    MangaListFragment mangaList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);

        navigation.setSelectedItemId(R.id.action_favorite);
        fav = new FavorisFragment();
        searchList=new SearchFilterFragment();
        showFragment(fav);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //TODO revoir efficacité
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_favorite:
                    if (fav == null) {
                        fav = new FavorisFragment();
                    }
                    showFragment(fav);
                    return true;
                case R.id.action_trending:
                    if (mangaList == null) {
                        mangaList = new MangaListFragment();
                    }
                    showFragment(mangaList);
                    return true;

                case R.id.action_search:
                    showFragment(searchList);
                    return true;
            }
            return false;
        });

        //getApplicationContext().deleteDatabase("user-database");

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