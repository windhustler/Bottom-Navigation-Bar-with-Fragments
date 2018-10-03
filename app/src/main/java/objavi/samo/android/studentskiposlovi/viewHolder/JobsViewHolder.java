package objavi.samo.android.studentskiposlovi.viewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import objavi.samo.android.studentskiposlovi.R;
import objavi.samo.android.studentskiposlovi.model.JobPosting;

public class JobsViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "JobsViewHolder";

    public TextView currentTime, category, jobDescription, phoneNumber, jobLocation, hourlyPay, email;
    public CardView jobPostingItem;

    public JobsViewHolder(final View itemView) {
        super(itemView);
        currentTime = itemView.findViewById(R.id.text_current_time);
        category = itemView.findViewById(R.id.text_category);
        jobDescription = itemView.findViewById(R.id.text_job_description);
        phoneNumber = itemView.findViewById(R.id.text_phone_number);
        jobLocation = itemView.findViewById(R.id.text_job_location);
        hourlyPay = itemView.findViewById(R.id.text_hourly_pay);
        email = itemView.findViewById(R.id.text_email);

        jobPostingItem = itemView.findViewById(R.id.job_posting_item);
    }
    public void bindToPost(JobPosting jobPosting, View.OnClickListener detailItemListener){

        currentTime.setText(jobPosting.getCurrentTime());
        category.setText(jobPosting.getCategory());
        jobDescription.setText(jobPosting.getJobDescription());
        phoneNumber.setText(jobPosting.getPhoneNumber());
        jobLocation.setText(jobPosting.getJobLocation());
        hourlyPay.setText(jobPosting.getHourlyPay());
        email.setText(jobPosting.getEmail());

        jobPostingItem.setOnClickListener(detailItemListener);

    }
}

