package com.obeshor.myjournal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EntryRecyclerAdapter extends RecyclerView.Adapter<EntryRecyclerAdapter.ViewHolder> {

    public List<EntryPost> entry_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public EntryRecyclerAdapter(List<EntryPost> entry_list){

        this.entry_list = entry_list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);


        final String entryPostId = entry_list.get(position).EntryPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String desc_data = entry_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String title_data = entry_list.get(position).getTitle();
        holder.setTitleText(title_data);

        String user_id = entry_list.get(position).getUser_id();
        //User Data will be retrieved here...
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    holder.setUserData(userName, userImage);


                } else {

                    //Firebase Exception

                }

            }
        });

        try {
            long millisecond = entry_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        // Attendre le click sur la vue
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent detailIntent = new Intent(context,SinglePostActivity.class);
                detailIntent.putExtra(MyJournal.ENTRY_ID, entryPostId);
                context.startActivity(detailIntent);

            }
        });



    }


    @Override
    public int getItemCount() {
        return entry_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView titleView;
        private TextView descView;
        private TextView entryDate;

        private TextView entryUserName;
        private CircleImageView entryUserImage;



        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


        }

        public void setDescText(String descText){

            descView = mView.findViewById(R.id.entry_desc);
            descView.setText(descText);

        }
        public void setTitleText(String titleText){

            titleView = mView.findViewById(R.id.entry_title);
            titleView.setText(titleText);

        }



        public void setTime(String date) {

            entryDate = mView.findViewById(R.id.entry_date);
            entryDate.setText(date);

        }

        public void setUserData(String name, final String image){

            entryUserImage = mView.findViewById(R.id.entry_user_image);
            entryUserName = mView.findViewById(R.id.entry_user_name);

            entryUserName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);

           // Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(entryUserImage);

            Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(entryUserImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(image).into(entryUserImage);
                }
            });


        }


    }

}
