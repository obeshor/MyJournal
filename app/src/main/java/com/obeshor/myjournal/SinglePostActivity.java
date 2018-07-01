package com.obeshor.myjournal;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class SinglePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        if (savedInstanceState == null) {
            String entryId;
            Intent intent = getIntent();
            Uri data = intent.getData();
            if (data == null) {
                // Not loading from deep link
                entryId = getIntent().getStringExtra(MyJournal.ENTRY_ID);
                loadEntryDetailsOf(entryId);
            } else {
                // Loading from deep link
                entryId = getIntent().getStringExtra(MyJournal.ENTRY_ID);
                loadEntryDetailsOf(entryId);
            }

        }

    }

    private void loadEntryDetailsOf(String entryId) {
        Fragment fragment = new EntrySingleFragment() ;
        Bundle args = new Bundle();
        args.putString(MyJournal.ENTRY_ID, entryId);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.entry_single_container,fragment).commit();

    }

}
