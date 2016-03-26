package com.keithsmyth.resistance.data.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import rx.Single;
import rx.SingleSubscriber;

public class FirebaseSingleEventWrapper<T> {

    private final Firebase ref;
    private final Mapper<T> mapper; // TODO: 1 mapper to rule them all
    private final Single<T> single;

    public FirebaseSingleEventWrapper(Firebase ref, Mapper<T> mapper) {
        this.ref = ref;
        this.mapper = mapper;
        single = createObservable();
    }

    public Single<T> getSingle() {
        return single;
    }

    private Single<T> createObservable() {
        return Single.create(new Single.OnSubscribe<T>() {
            @Override
            public void call(SingleSubscriber<? super T> singleSubscriber) {
                initFirebaseSingleEventListener(singleSubscriber);
            }
        });
    }

    private void initFirebaseSingleEventListener(final SingleSubscriber<? super T> singleSubscriber) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final T model = mapper.map(dataSnapshot);
                singleSubscriber.onSuccess(model);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                singleSubscriber.onError(firebaseError.toException());
            }
        });
    }

    public interface Mapper<T> {
        T map(DataSnapshot dataSnapshot);
    }
}
