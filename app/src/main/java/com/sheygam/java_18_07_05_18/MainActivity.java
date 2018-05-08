package com.sheygam.java_18_07_05_18;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MY_TAG";
    private TextView resultTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTxt = findViewById(R.id.resultTxt);
//        creation();
//        disposableObserver();
//        types();
//        multiThreding();
        unsubscribe();
    }

    private void creation(){
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe() called with: d = [" + d + "]");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext() called with: s = [" + s + "]");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete() called");
            }
        };

        Observable<String> observable = Observable.just("Name 1", "Name 2", "Name 3");
        observable.subscribe(observer);

        String[] arr = {"Name 1", "Name 2", "Name 3"};
        List<String> list = Arrays.asList(arr);

        observable = Observable.fromArray(arr);
        observable.subscribe(observer);

        observable = Observable.fromIterable(list);
        observable.subscribe(observer);

        observable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "My string from callable!";
            }
        });

        observable.subscribe(observer);


        ObservableOnSubscribe<String> onSubscribe = new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("Nam1");
                emitter.onNext("name 2");
                emitter.onNext("Name 3");
                emitter.onComplete();
//                emitter.onError(new Throwable());
            }
        };

        observable = Observable.create(onSubscribe);
        observable.subscribe(observer);
    }

    private void disposableObserver(){
        DisposableObserver<String> observer = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext() called with: s = [" + s + "]");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete() called");
            }
        };

        Observable<String> observable = Observable.just("Name 1", "Name 2", "Name 3");
        observable.subscribe(observer);
    }

    private void types(){
        Single<String> single = Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Single";
            }
        });

        DisposableSingleObserver<String> singleObserver = new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "onSuccess() called with: s = [" + s + "]");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }
        };

        single.subscribe(singleObserver);

        Completable completable = Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run() called");
            }
        });

        DisposableCompletableObserver completableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete() called");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }
        };

        completable.subscribe(completableObserver);

        Maybe<String> maybe = Maybe.just("Maybe");

        DisposableMaybeObserver<String> maybeObserver = new DisposableMaybeObserver<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "onSuccess() called with: s = [" + s + "]");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };

        maybe.subscribe(maybeObserver);
    }

    private void multiThreding(){
        DisposableObserver<String> observer = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext() called with: s = [" + s + "]");
                resultTxt.setText(s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete() called");
            }
        };

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Thread.sleep(5000);
                emitter.onNext("Done");
                emitter.onComplete();
            }
        });

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void unsubscribe(){
        Observable<String> observable = Observable.just("Name 1", "Name 2", "Name 3");
        DisposableObserver<String> observer = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext() called with: s = [" + s + "]");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete() called");
            }
        };

        Disposable disposable = observable.subscribeWith(observer);
        disposable.dispose();
    }

    private void lambda(){
        Observable<String> observable = Observable.just("Item 1", "Item 2");
        Disposable disposable = observable.subscribe(
                s -> Log.d(TAG, "accept() called with: s = [" + s + "]"),
                throwable -> Log.d(TAG, "accept() called with: throwable = [" + throwable + "]"),
                () -> Log.d(TAG, "run() called")
        );

        disposable.dispose();
    }
}
