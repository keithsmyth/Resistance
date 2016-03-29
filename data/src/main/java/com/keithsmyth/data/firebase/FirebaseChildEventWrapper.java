package com.keithsmyth.data.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.keithsmyth.data.model.ModelActionWrapper;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public class FirebaseChildEventWrapper<T> {

    private final Firebase ref;
    private final Mapper<T> addMapper;
    private final Mapper<T> removeMapper;
    private final Observable<ModelActionWrapper<T>> observable;

    private ChildEventListener childEventListener;

    public FirebaseChildEventWrapper(Firebase ref, Mapper<T> addMapper, Mapper<T> removeMapper) {
        this.ref = ref;
        this.addMapper = addMapper;
        this.removeMapper = removeMapper;
        observable = createObservable();
    }

    public Observable<ModelActionWrapper<T>> getObservable() {
        return observable;
    }

    private Observable<ModelActionWrapper<T>> createObservable() {
        return Observable.create(new Observable.OnSubscribe<ModelActionWrapper<T>>() {
            @Override
            public void call(Subscriber<? super ModelActionWrapper<T>> subscriber) {
                initFirebaseChildListener(subscriber);
                addUnSubscribeAction(subscriber);
            }
        });
    }

    private void initFirebaseChildListener(final Subscriber<? super ModelActionWrapper<T>> subscriber) {
        if (childEventListener != null) {
            throw new UnsupportedOperationException("Wrapper designed for single subscriber");
        }
        childEventListener = new ChildEventListenerImpl() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                if (addMapper != null) {
                    final T model = addMapper.map(dataSnapshot);
                    subscriber.onNext(new ModelActionWrapper<>(model, true));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (removeMapper != null) {
                    final T model = removeMapper.map(dataSnapshot);
                    subscriber.onNext(new ModelActionWrapper<>(model, false));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                subscriber.onError(firebaseError.toException());
            }
        };
        ref.addChildEventListener(childEventListener);
    }

    private void addUnSubscribeAction(Subscriber<? super ModelActionWrapper<T>> subscriber) {
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                removeFirebaseChildListener();
            }
        }));
    }

    private void removeFirebaseChildListener() {
        if (childEventListener != null) {
            ref.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    public interface Mapper<T> {
        T map(DataSnapshot dataSnapshot);
    }
}
