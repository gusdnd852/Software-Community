package site.namsu.sweng.rx.publisher;

import site.namsu.sweng.rx.function.BiFunction;
import site.namsu.sweng.rx.function.Consumer;
import site.namsu.sweng.rx.function.Function;
import site.namsu.sweng.rx.function.Predicate;
import site.namsu.sweng.rx.operator.*;
import site.namsu.sweng.rx.operator.Error;
import site.namsu.sweng.rx.scheduler.Scheduler;
import site.namsu.sweng.rx.subscription.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;

/**
 * @Author : Hyunwoong
 * @When : 2018-11-26 오전 7:20
 * @Homepage : https://github.com/gusdnd852
 */
@SuppressWarnings("unchecked")
public class Publisher<T> implements Flow.Publisher<T> {
    private List<T> inputAsList = new ArrayList<>();
    private static Executor executor = Scheduler.main();

    protected Publisher(T[] inputs) {
        for (T input : inputs) {
            if (input instanceof Iterable)
                for (T once : (Iterable<T>) input)
                    inputAsList.add(once);
            else inputAsList.add(input);
        }
    }

    public Publisher() {
    }

    @SafeVarargs
    public static <T> Publisher<T> springThread(T... inputs) {
        Publisher.executor = Scheduler.main();
        return new Publisher<>(inputs);
    }

    @SafeVarargs
    public static <T> Publisher<T> backgroundThread(T... inputs) {
        Publisher.executor = Scheduler.background();
        return new Publisher<>(inputs);
    }

    public <R> Publisher<R> map(Function<T, R> function) {
        return new Mapper<>(this, function);
    }

    public <R> Publisher<R> reduce(R init, BiFunction<R, T, R> biFunction) {
        return new Reducer<>(this, init, biFunction);
    }

    public Publisher<T> filter(Predicate<T> predicate) {
        return new Filter<>(this, predicate);
    }

    public Publisher<T> next(Consumer<T> consumer) {
        return new Proceeder<>(this, consumer);
    }

    public <R> Publisher<Publisher<R>> flatMap(Function<T, Publisher<R>> flatMapper) {
        return new Flatter<>(this, flatMapper);
    }

    public <R> Publisher<R> switchIf(Function<T, Function<T, R>> function) {
        return new Switcher<>(this, function);
    }

    @Override public void subscribe(Flow.Subscriber<? super T> subscriber) {
        executor.execute(() -> subscriber.onSubscribe(new Subscription<>(subscriber, inputAsList)));
    }

    public Publisher<T> error(Consumer<Throwable> consumer) {
        return new Error<>(this, consumer);
    }

    public Publisher<T> complete(Runnable runnable) {
        return new Complete<>(this, runnable);
    }

    public T block() {
        return inputAsList.get(0);
    }
}