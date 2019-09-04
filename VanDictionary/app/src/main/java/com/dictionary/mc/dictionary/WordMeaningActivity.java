package com.dictionary.mc.dictionary;

import android.Manifest;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.dictionary.mc.dictionary.fragments.FragmentAntonyms;
import com.dictionary.mc.dictionary.fragments.FragmentDefinition;
import com.dictionary.mc.dictionary.fragments.FragmentExample;
import com.dictionary.mc.dictionary.fragments.FragmentSynonyms;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordMeaningActivity extends AppCompatActivity {

    private ViewPager viewPager;

    String enWord;
    DatabaseHelper myDbHelper;
    Cursor c = null;

    public String enDefinition;
    public String example;
    public String synonyms;
    public String antonyms;

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        //received values
        Bundle bundle = getIntent().getExtras();
        enWord= bundle.getString("en_word");


        myDbHelper = new DatabaseHelper(this);

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }


        c = myDbHelper.getMeaning(enWord);

        if (c.moveToFirst()) {

            enDefinition= c.getString(c.getColumnIndex("en_definition"));
            example=c.getString(c.getColumnIndex("example"));
            synonyms=c.getString(c.getColumnIndex("synonyms"));
            antonyms=c.getString(c.getColumnIndex("antonyms"));

        }

        myDbHelper.insertHistory(enWord);


        ImageButton btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts = new TextToSpeech(WordMeaningActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        // TODO Auto-generated method stub
                        if(status == TextToSpeech.SUCCESS){
                            int result=tts.setLanguage(Locale.getDefault());
                            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                                Log.e("error", "This Language is not supported");
                            }
                            else{
                                tts.speak(enWord, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                        else
                            Log.e("error", "Initialization Failed!");
                    }
                });
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(enWord);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);

        if (viewPager != null){
            setupViewPager(viewPager);
        }

        //tabLayout

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });


    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }


    }


    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentDefinition(), "Definition");
        adapter.addFrag(new FragmentSynonyms(), "Synonyms");
        adapter.addFrag(new FragmentAntonyms(), "Antonyms");
        adapter.addFrag(new FragmentExample(), "Example");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}