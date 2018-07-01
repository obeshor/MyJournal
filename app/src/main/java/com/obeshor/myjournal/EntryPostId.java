package com.obeshor.myjournal;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class EntryPostId {
    @Exclude
    public String EntryPostId;

    public <T extends EntryPostId> T withId(@NonNull final String id) {
        this.EntryPostId = id;
        return (T) this;
    }
}
