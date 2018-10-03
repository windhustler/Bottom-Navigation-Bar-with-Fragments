package objavi.samo.android.studentskiposlovi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import objavi.samo.android.studentskiposlovi.R;
import objavi.samo.android.studentskiposlovi.LoginActivity;

public class SettingsFragment extends Fragment {

    private Button signOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        signOut = view.findViewById(R.id.btn_sign_out);


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"Odjava uspješna!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                        //getActivity().finish();
                        //finishAffinity(); // skroz izlazi iz aktivnosti

                        // tu za lipsi ux staviti jos dialog koji cu postaviti na botun za odjavu koji ce pitati dali se zelim odjaviti
                    }
                });



            }
        });
    }
}
