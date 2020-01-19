package com.celerii.celerii.Activities.Settings;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.celerii.celerii.R;
import com.celerii.celerii.adapters.FAQAdapter;
import com.celerii.celerii.helperClasses.SharedPreferencesManager;
import com.celerii.celerii.models.FAQModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FAQRowActivity extends AppCompatActivity {

    SharedPreferencesManager sharedPreferencesManager;

    FirebaseAuth auth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseUser mFirebaseUser;

    SwipeRefreshLayout mySwipeRefreshLayout;
    LinearLayout errorLayout, progressLayout;

    Toolbar toolbar;
    private ArrayList<FAQModel> FAQList;
    public RecyclerView recyclerView;
    public FAQAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    String activeAccount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqrow);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        activeAccount = sharedPreferencesManager.getActiveAccount();

        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mFirebaseUser = auth.getCurrentUser();

        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        toolbar = (Toolbar) findViewById(R.id.hometoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAQ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        errorLayout = (LinearLayout) findViewById(R.id.errorlayout);
        progressLayout = (LinearLayout) findViewById(R.id.progresslayout);

        recyclerView.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);

        FAQList = new ArrayList<>();
        mAdapter = new FAQAdapter(FAQList, this);
        loadFromFirebase();
        recyclerView.setAdapter(mAdapter);

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadFromFirebase();
                    }
                }
        );
    }

    private void loadFromFirebase() {
        if (activeAccount.equals("Teacher")){
            mDatabaseReference = mFirebaseDatabase.getReference().child("Teacher FAQ");
        } else if (activeAccount.equals("Parent")){
            mDatabaseReference = mFirebaseDatabase.getReference().child("Parent FAQ");
        }
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    FAQList.clear();
                    mySwipeRefreshLayout.setRefreshing(false);
                    progressLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        FAQModel faqModel = postSnapshot.getValue(FAQModel.class);
                        FAQList.add(faqModel);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    mySwipeRefreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    void yeah(){
        FAQModel faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);

        faqModel = new FAQModel("How do I register to a class, do I register to a school too?", "Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt. Lorem ipsum dolor sit amet, consectet adipiscing elit, sed do eiusmod tempor incididunt.");
        FAQList.add(faqModel);
    }
}