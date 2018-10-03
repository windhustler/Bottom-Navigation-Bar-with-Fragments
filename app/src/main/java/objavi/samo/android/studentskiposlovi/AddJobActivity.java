package objavi.samo.android.studentskiposlovi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import objavi.samo.android.studentskiposlovi.model.JobPosting;
import objavi.samo.android.studentskiposlovi.model.User;
import objavi.samo.android.studentskiposlovi.utils.BaseActivity;
import objavi.samo.android.studentskiposlovi.utils.DateAndTime;

public class AddJobActivity extends BaseActivity {
    private static final String TAG = "AddJobActivity";
    private static final String REQUIRED = "Obavezno polje";
    private static final int JOB_POSTED = 12;

    private Context mContext = AddJobActivity.this;

    private String[] array_categories; // to be used inside the dialog for categories

    //******************************************widgets**********************************************

    private EditText mPhoneNumber, mHourlyPay, mJobLocation;
    private EditText mJobDescription;
    private ImageView mCloseSign;
    private Button mCategories;
    private FloatingActionButton mPostJob;

    //******************************************widgets**********************************************

    //*****************************************Firebase variables*******************************************

    private FirebaseDatabase mFirebaseDatabase; // entry point for app to access the database
    private DatabaseReference mJobsDatabaseReference; // a class that references a specific part of the database
    private DatabaseReference mUsersDatabaseReference;

    //*****************************************Firebase variables*******************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        array_categories = getResources().getStringArray(R.array.categories);

        // ********************************Initialize widgets*************************************************
        mJobDescription = findViewById(R.id.text_job_description);
        mPhoneNumber = findViewById(R.id.text_phone_number);
        mHourlyPay = findViewById(R.id.text_hourly_pay);
        mJobLocation = findViewById(R.id.text_job_location);
        mCloseSign = findViewById(R.id.image_close);
        mCategories = findViewById(R.id.btn_categories);
        mPostJob = findViewById(R.id.fab_post_job);
        // ********************************Initialize widgets**************************************************

        initStateChoiceDialog(); // Intialize the dialog for the categories button

        mCloseSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // alternative is to navigate to a specific activity e.g. HomeActivity
            }
        });

        //********************************Firebase stuff********************************************************

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");
        mJobsDatabaseReference = mFirebaseDatabase.getReference().child("jobs");

        //********************************Firebase stuff********************************************************


        //**********************************enableJobPostingButton******************************************
        enableSubmit();
        //**********************************enableJobPostingButton******************************************


        //*********************Push the data to the database and return to the HomeActivity***************
        mPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicking on addJob button");
                submitJob();
                //add a fancy dialog to note success of posting a job
                finish();

            }
        });
        //*********************Push the data to the database and return to the HomeActivity***************

    }
    //*************************************Categories dialog***************************************
    private void initStateChoiceDialog() {
        findViewById(R.id.btn_categories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStateChoiceDialog((Button) v);
            }
        });
    }

    private void showStateChoiceDialog(final Button bt) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setSingleChoiceItems(array_categories,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                bt.setTextColor(Color.BLACK);
                bt.setText(array_categories[which]);
            }
        });
        builder.show();
    }
    //*************************************Categories dialog***************************************


    //************************************** Push jobPosting object to the database***********************************
    private void addJob(){
        Log.d(TAG, "addJob: pushing jobPosting object to the database");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){

            String key = mJobsDatabaseReference.push().getKey(); // get push key generated for each job

            String category = mCategories.getText().toString().trim();
            String description = mJobDescription.getText().toString().trim();
            String phone = String.valueOf(mPhoneNumber.getText().toString()).trim();
            String location = mJobLocation.getText().toString().trim();
            String pay = String.valueOf(mHourlyPay.getText().toString()).trim();
            String userId = user.getUid();
            String email  = user.getEmail();
            String date = DateAndTime.currentTime();


            JobPosting jobPosting = new JobPosting(category, description, phone, location, pay, userId, email, date);
            Log.d(TAG, "addJob: Job added" + jobPosting.toString());

            Map<String, Object> jobValues = jobPosting.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/jobs/" + key, jobValues);
            childUpdates.put("/user-jobs/" + userId + "/" + key, jobValues);

            mFirebaseDatabase.getReference().updateChildren(childUpdates);
        }
    }
    //**************************************Push jobPosting object to the database***********************************
    public void submitJob(){
        final String description = mJobDescription.getText().toString().trim();
        final String category = mCategories.getText().toString().trim();
        if (TextUtils.isEmpty(description)){
            mCategories.setError(REQUIRED);
            return;
        }
        if(TextUtils.isEmpty(category)) {
            mCategories.setError(REQUIRED);
            return;
        }
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUsersDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    Toast.makeText(mContext, "Error: could not fetch user.",Toast.LENGTH_SHORT).show();
                } else {
                    addJob();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    public void enableSubmit(){
        mPostJob.setEnabled(false);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(mJobDescription.getText().toString().trim()) &&
                        !TextUtils.isEmpty(mCategories.getText().toString().trim())){
                    mPostJob.setEnabled(true);
                } else {
                    mPostJob.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mCategories.addTextChangedListener(watcher);
        mJobDescription.addTextChangedListener(watcher);
    }

}
