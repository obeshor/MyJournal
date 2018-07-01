package com.obeshor.myjournal;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.enums.Animation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntrySingleFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private String mEntry_id;
    private EntryPost mEntryPost ;
    private Toolbar newPostToolbar;
    private EditText newPostDesc;
    private EditText newPostTitle;
    private Button newPostBtn;
    private Button newDelBtn;

    private ProgressBar newPostProgress;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    public EntrySingleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_entry_single, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        newPostToolbar = v.findViewById(R.id.new_entry_toolbar);

        newPostDesc = v.findViewById(R.id.new_entry_desc);
        newPostTitle = v.findViewById(R.id.new_entry_title);
        newPostBtn = v. findViewById(R.id.entry_btn);
        newDelBtn = v. findViewById(R.id.entry_btn_delete);
        newPostProgress = v.findViewById(R.id.new_entry_progress);

        newPostToolbar.setOnMenuItemClickListener(this);

        newPostToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.action_home));
        newPostToolbar.setTitle("Entry Details");
        // Add share button to toolbar
        newPostToolbar.inflateMenu(R.menu.menu_entry_single);

        newPostToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        // Download entry details if new instance, else restore from saved instance
        if (savedInstanceState == null || !(savedInstanceState.containsKey(MyJournal.ENTRY_ID)
                && savedInstanceState.containsKey(MyJournal.ENTRY_OBJECT))) {
            mEntry_id = getArguments().getString(MyJournal.ENTRY_ID);

            downloadEntryDetails(mEntry_id);

        } else {
            mEntry_id = savedInstanceState.getString(MyJournal.ENTRY_ID);
            mEntryPost = savedInstanceState.getParcelable(MyJournal.ENTRY_OBJECT);
            newPostTitle.setText(mEntryPost.title) ;
            newPostDesc.setText(mEntryPost.desc);
            newPostToolbar.setTitle("");

            // Add share button to toolbar
            newPostToolbar.inflateMenu(R.menu.menu_entry_single);


        }

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc = newPostDesc.getText().toString();
                final String title = newPostTitle.getText().toString();

                if(!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(title)){

                    newPostProgress.setVisibility(View.VISIBLE);
                    Map<String, Object> postMap = new HashMap<>();
                    postMap.put("title", title);
                    postMap.put("desc", desc);
                    postMap.put("timestamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("Posts").document(mEntry_id).update(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Saved Successful", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                startActivity(mainIntent);

                            } else {

                                Toast.makeText(getActivity(), "Failed to Save", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }

            }
        });

       newDelBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               new PanterDialog(getActivity())
                       .setHeaderBackground(R.drawable.ic_launcher_background)
                       .setHeaderLogo(R.drawable.ic_logo)
                       .setPositive("OK", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               firebaseFirestore.collection("Posts").document(mEntry_id).delete();
                               startActivity(new Intent(getActivity(), MainActivity.class));
                           }
                       })
                       .setNegative("DISMISS")
                       .setMessage(R.string.delete_msg)
                       .withAnimation(Animation.POP)
                       .isCancelable(false)
                       .show();

           }
       });


        return v;
    }

    private void downloadEntryDetails(final String postkey) {

        newPostProgress.setVisibility(View.VISIBLE);
        newPostBtn.setEnabled(false);

        //load ui data for editing
        firebaseFirestore.collection("Posts").document(postkey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String desc = task.getResult().getString("desc");
                        String title = task.getResult().getString("title");
                        String user_id =task.getResult().getString("user_id");
                        //String date =task.getResult().getString("date");

                        newPostDesc.setText(desc);
                        newPostTitle.setText(title);

                        mEntryPost = new EntryPost(user_id, title, desc);

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

                newPostProgress.setVisibility(View.INVISIBLE);
                newPostBtn.setEnabled(true);

            }
        });


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mEntryPost != null && mEntry_id != null) {
            outState.putString(MyJournal.ENTRY_ID, mEntry_id);
           // outState.putParcelable(MyJournal.ENTRY_OBJECT, mEntryPost);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    // Toolbar menu click
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            if (mEntryPost != null) {
                // Share the blog
                String shareText = getString(R.string.action_share_text, mEntryPost.title, mEntryPost.desc);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mEntryPost.title);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.action_share_using)));

            }
            return true;
        } else {
            return false;
        }
    }


}