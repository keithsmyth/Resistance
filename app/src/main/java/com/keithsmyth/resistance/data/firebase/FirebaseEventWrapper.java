package com.keithsmyth.resistance.data.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public class FirebaseEventWrapper<T> {

    private final Firebase ref;
    private final Mapper<T> mapper;
    private final Observable<T> observable;

    private ValueEventListener valueEventListener;

    public FirebaseEventWrapper(Firebase ref, Mapper<T> mapper) {
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
                initFirebaseListener(subscriber);
                addUnSubscribeAction(subscriber);
            }
        });
    }

    private void initFirebaseListener(final Subscriber<? super T> subscriber) {
        if (valueEventListener != null) {
            throw new UnsupportedOperationException("Wrapper designed for single subscriber");
        }
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final T model = mapper.map(dataSnapshot);
                subscriber.onNext(model);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                subscriber.onError(firebaseError.toException());
            }
        };
        ref.addValueEventListener(valueEventListener);
    }

    private void addUnSubscribeAction(Subscriber<? super T> subscriber) {
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                removeFirebaseListener();
            }
        }));
    }

    private void removeFirebaseListener() {
        ref.removeEventListener(valueEventListener);
    }

    public interface Mapper<T> {
        T map(DataSnapshot dataSnapshot);
    }
}
