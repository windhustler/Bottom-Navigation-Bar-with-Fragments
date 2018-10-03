package objavi.samo.android.studentskiposlovi.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import objavi.samo.android.studentskiposlovi.R;
import objavi.samo.android.studentskiposlovi.model.JobPosting;
import objavi.samo.android.studentskiposlovi.viewHolder.JobsViewHolder;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    //**********************************Firebase**********************************************

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<JobPosting, JobsViewHolder> mFirebaseAdapter;

    //**********************************Firebase**********************************************

    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_home_profile, container,false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mRecyclerView = rootView.findViewById(R.id.rv_home_profile);
        mRecyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Query query = mDatabaseReference.child("jobs");

        FirebaseRecyclerOptions options
                = new FirebaseRecyclerOptions.Builder<JobPosting>()
                .setQuery(query, JobPosting.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<JobPosting, JobsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final JobsViewHolder holder, int position, @NonNull final JobPosting model) {

                holder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDetailedAdd(model.getCategory(),
                                model.getCurrentTime(),
                                model.getJobDescription(),
                                model.getJobLocation(),
                                model.getPhoneNumber(),
                                model.getEmail());
                    }
                });
            }

            @NonNull
            @Override
            public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new JobsViewHolder(inflater.inflate(R.layout.item_job_posting, parent, false));
            }
        };
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }
    private void showDetailedAdd(String job_category,
                                 String job_posting_time,
                                 String job_description,
                                 String job_location,
                                 String phone_number,
                                 String email_text){

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_detailed);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        TextView jobCategory = dialog.findViewById(R.id.text_job_category);
        TextView jobPostingTime= dialog.findViewById(R.id.text_job_posting_time);
        TextView jobDescription= dialog.findViewById(R.id.text_job_description);
        TextView jobLocation= dialog.findViewById(R.id.text_job_location);
        Button phoneNumber = dialog.findViewById(R.id.btn_phone_number);
        Button email = dialog.findViewById(R.id.btn_email);

        jobCategory.setText(job_category);
        jobPostingTime.setText(job_posting_time);
        jobDescription.setText(job_description);
        jobLocation.setText(job_location);

        dialog.findViewById(R.id.btn_phone_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //add an intent to the user phone number
            }
        });
        dialog.findViewById(R.id.btn_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //add an intent to the user email
            }
        });
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirebaseAdapter != null){
            mFirebaseAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFirebaseAdapter != null){
            mFirebaseAdapter.stopListening();
        }
    }


}
