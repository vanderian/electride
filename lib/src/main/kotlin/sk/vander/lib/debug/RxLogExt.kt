package sk.vander.lib.debug

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author marian on 23.2.2017.
 */
fun <T> Flowable<T>.log(tag: String): Flowable<T> =
    this.compose {
      it.doOnSubscribe { timber.log.Timber.d(tag + " #onSubscribe on %s", Thread.currentThread()) }
          .doOnNext { value -> timber.log.Timber.d(tag + " #onNext:%s on %s", value, Thread.currentThread()) }
          .doOnComplete { timber.log.Timber.d(tag + " #onSuccess on %s", Thread.currentThread()) }
          .doOnError { error -> timber.log.Timber.d(tag + " #onError:%s on %s", error, Thread.currentThread()) }
          .doOnCancel { timber.log.Timber.d(tag + " #onCancel on %s", Thread.currentThread()) }
          .doOnRequest { value -> timber.log.Timber.d(tag + " #onRequest:%s on %s", value, Thread.currentThread()) }
    }

fun <T> Observable<T>.log(tag: String): Observable<T> =
    this.compose {
      it.doOnSubscribe { timber.log.Timber.d(tag + " #onSubscribe on %s", Thread.currentThread()) }
          .doOnNext { value -> timber.log.Timber.d(tag + " #onNext:%s on %s", value, Thread.currentThread()) }
          .doOnComplete { timber.log.Timber.d(tag + " #onSuccess on %s", Thread.currentThread()) }
          .doOnError { error -> timber.log.Timber.d(tag + " #onError:%s on %s", error, Thread.currentThread()) }
          .doOnDispose { timber.log.Timber.d(tag + " #onDispose on %s", Thread.currentThread()) }
    }

fun <T> Maybe<T>.log(tag: String): Maybe<T> =
    this.compose {
      it.doOnSubscribe { timber.log.Timber.d(tag + " #onSubscribe on %s", Thread.currentThread()) }
          .doOnSuccess { value -> timber.log.Timber.d(tag + " #onSuccess:%s on %s", value, Thread.currentThread()) }
          .doOnComplete { timber.log.Timber.d(tag + " #onComplete on %s", Thread.currentThread()) }
          .doOnError { error -> timber.log.Timber.d(tag + " #onError:%s on %s", error, Thread.currentThread()) }
          .doOnDispose { timber.log.Timber.d(tag + " #onDispose on %s", Thread.currentThread()) }
    }

fun <T> Single<T>.log(tag: String): Single<T> =
    this.compose {
      it.doOnSubscribe { timber.log.Timber.d(tag + " #onSubscribe on %s", Thread.currentThread()) }
          .doOnSuccess { value -> timber.log.Timber.d(tag + " #onSuccess:%s on %s", value, Thread.currentThread()) }
          .doOnError { error -> timber.log.Timber.d(tag + " #onError:%s on %s", error, Thread.currentThread()) }
          .doOnDispose { timber.log.Timber.d(tag + " #onDispose on %s", Thread.currentThread()) }
    }