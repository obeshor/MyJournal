package com.obeshor.myjournal;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    CircleImageView profileImage;

    TextView profileName;

    TextView profileYear;


    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        profileImage = (CircleImageView) view.findViewById(R.id.profile_image) ;
        profileName = (TextView) view.findViewById(R.id.profile_name) ;
        profileYear = (TextView) view.findViewById(R.id.profile_year) ;


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();

        if (user != null){
            profileName.setText(user.getDisplayName());

            Picasso.with(getContext()).load(user.getPhotoUrl()).networkPolicy(NetworkPolicy.OFFLINE).into(profileImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(getContext()).load(user.getPhotoUrl()).into(profileImage);
                }
            });

        }
    }


}
