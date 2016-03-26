package com.keithsmyth.resistance.data.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import rx.Single;
import rx.SingleSubscriber;

public class FirebaseCompletionWrapper<T> {

    private final Firebase ref;
    private final T returnValue;

    public FirebaseCompletionWrapper(@NonNull Firebase ref, @Nullable T returnValue) {
        this.ref = ref;
        this.returnValue = returnValue;
    }

    public Single<T> updateChildren(final Map<String, Object> childrenMap) {
        return Single.create(new Single.OnSubscribe<T>() {
            @Override
            public void call(final SingleSubscriber<? super T> singleSubscriber) {
                ref.updateChildren(childrenMap, new CompletionListener<>(singleSubscriber, returnValue));
            }
        });
    }

    public Single<T> setValue(final Object value) {
        return Single.create(new Single.OnSubscribe<T>() {
            @Override
            public void call(SingleSubscriber<? super T> singleSubscriber) {
                ref.setValue(value, new CompletionListener<>(singleSubscriber, returnValue));
            }
        });
    }

    private static class CompletionListener<T> implements Firebase.CompletionListener {

        private final SingleSubscriber<? super T> singleSubscriber;
        private final T returnValue;

        public CompletionListener(SingleSubscriber<? super T> singleSubscriber, T returnValue) {
            this.singleSubscriber = singleSubscriber;
            this.returnValue = returnValue;
        }

        @Override
        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
            if (firebaseError == null) {
                singleSubscriber.onSuccess(returnValue);
            } else {
                singleSubscriber.onError(firebaseError.toException());
            }
        }
    }
}
