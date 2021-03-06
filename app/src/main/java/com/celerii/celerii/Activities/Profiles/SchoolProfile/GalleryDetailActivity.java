package com.celerii.celerii.Activities.Profiles.SchoolProfile;

import android.content.Context;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.celerii.celerii.Activities.StudentPerformance.StudentPerformanceForParentsActivity;
import com.celerii.celerii.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.celerii.celerii.adapters.ClassStoryAdapter;
import com.celerii.celerii.adapters.GalleryAdapter;
import com.celerii.celerii.helperClasses.Analytics;
import com.celerii.celerii.helperClasses.Date;
import com.celerii.celerii.helperClasses.SharedPreferencesManager;
import com.celerii.celerii.helperClasses.UpdateDataFromFirebase;
import com.celerii.celerii.helperClasses.WrapContentViewPager;
import com.celerii.celerii.models.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class GalleryDetailActivity extends AppCompatActivity {
    SharedPreferencesManager sharedPreferencesManager;
    Context context;

    FirebaseAuth auth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseUser mFirebaseUser;

    Toolbar mtoolbar;
    ProgressBar progressBar;

    public WrapContentViewPager viewPager;
    public MyViewPagerAdapter myViewPagerAdapter;
    public LinearLayout dotsLayout;
    public TextView[] dots;
    private String[] imageArray;
    public String urls;

    String featureUseKey = "";
    String featureName = "Gallery Detail";
    long sessionStartTime = 0;
    String sessionDurationInSeconds = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        context = this;

        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mFirebaseUser = auth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        int currentImage = bundle.getInt("currentImage");
        urls = sharedPreferencesManager.getSchoolGallery();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> imageURLList = gson.fromJson(urls, type);
        imageArray = new String[imageURLList.size()];
        imageArray = imageURLList.toArray(imageArray);

        mtoolbar = (Toolbar) findViewById(R.id.gallerytoolbar);
        progressBar = (ProgressBar) findViewById(R.id.imageloadprogress);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        viewPager = (WrapContentViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        myViewPagerAdapter = new MyViewPagerAdapter(context, imageArray);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(currentImage);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int innerPosition) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private Context context;
        private String[] imageURLs;

        public MyViewPagerAdapter(Context context, String[] imageURLs) {
            this.context = context;
            this.imageURLs = imageURLs;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);

            Glide.with(context)
                    .load(imageURLs[position])
                    .placeholder(R.drawable.profileimageplaceholder)
                    .error(R.drawable.profileimageplaceholder)
                    .into(imageView);

//            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public int getCount() {
            return imageURLs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sharedPreferencesManager.getActiveAccount().equals("Parent")) {
            featureUseKey = Analytics.featureAnalytics("Parent", mFirebaseUser.getUid(), featureName);
        } else {
            featureUseKey = Analytics.featureAnalytics("Teacher", mFirebaseUser.getUid(), featureName);
        }
        sessionStartTime = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();

        sessionDurationInSeconds = String.valueOf((System.currentTimeMillis() - sessionStartTime) / 1000);
        Analytics.featureAnalyticsUpdateSessionDuration(featureName, featureUseKey, mFirebaseUser.getUid(), sessionDurationInSeconds);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateDataFromFirebase.populateEssentials(this);
    }
}
