package com.keithsmyth.resistance.data.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import rx.Observable;
import rx.Subscriber;

public class FirebaseSingleEventWrapper<T> {

    private final Firebase ref;
    private final Mapper<T> mapper;
    private final Observable<T> observable;

    public FirebaseSingleEventWrapper(Firebase ref, Mapper<T> mapper) {
        this.ref = ref;
        this.mapper = mapper;
        observable = createObservable();
    }

    public Observable<T> getObservable() {
        return observable;
    }

    private Observable<T> createObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                initFirebaseSingleEventListener(subscriber);
            }
        });
    }

    private void initFirebaseSingleEventListener(final Subscriber<? super T> subscriber) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final T model = mapper.map(dataSnapshot);
                subscriber.onNext(model);
                subscriber.onCompleted();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                subscriber.onError(firebaseError.toException());
            }
        });
    }

    public interface Mapper<T> {
        T map(DataSnapshot dataSnapshot);
    }
}
