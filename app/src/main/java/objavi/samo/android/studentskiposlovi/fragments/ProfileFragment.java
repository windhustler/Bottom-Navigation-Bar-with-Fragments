package objavi.samo.android.studentskiposlovi.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import objavi.samo.android.studentskiposlovi.R;
import objavi.samo.android.studentskiposlovi.viewHolder.UserJobsViewHolder;
import objavi.samo.android.studentskiposlovi.model.JobPosting;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    //**********************************Firebase**********************************************

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<JobPosting,UserJobsViewHolder> mFirebaseAdapter;

    //**********************************Firebase**********************************************

    public ProfileFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_home_profile,container,false);

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

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Query query = mDatabaseReference.child("user-jobs").child(userId);

            FirebaseRecyclerOptions options
                    = new FirebaseRecyclerOptions.Builder<JobPosting>()
                    .setQuery(query, JobPosting.class)
                    .build();


            mFirebaseAdapter = new FirebaseRecyclerAdapter<JobPosting, UserJobsViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull UserJobsViewHolder holder, int position, @NonNull JobPosting model) {
                    final DatabaseReference jobRef = getRef(position);
                    final String jobKey = jobRef.getKey();

                    holder.bindToPost(model, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference jobsRef
                                    = mDatabaseReference
                                    .child("jobs")
                                    .child(jobKey);
                            jobsRef.removeValue();
                            DatabaseReference userJobsRef
                                    = mDatabaseReference
                                    .child("user-jobs")
                                    .child(user.getUid())
                                    .child(jobKey);
                            userJobsRef.removeValue();
                        }
                    });
                }

                @NonNull
                @Override
                public UserJobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    return new UserJobsViewHolder(inflater.inflate(R.layout.item_job_posting_delete, parent, false));
                }
            };
            mRecyclerView.setAdapter(mFirebaseAdapter);
        }
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
