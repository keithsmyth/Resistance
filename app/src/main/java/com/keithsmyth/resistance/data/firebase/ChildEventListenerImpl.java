package com.keithsmyth.resistance.data.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

public class ChildEventListenerImpl implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
        // override to handle
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
        // override to handle
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        // override to handle
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
        // override to handle
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        // override to handle
    }
}
