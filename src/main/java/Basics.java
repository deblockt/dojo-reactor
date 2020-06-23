import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Basics {

    public static Mono<Void> empty() {
        return Mono.empty();

    }

    public static Mono<Integer> return1() {
        return Mono.just(1);
    }

    public static Flux<Integer> return1And2() {
//        return Flux.create(emiter -> {
//            emiter.next(1).next(2).complete();
//        });

        return Flux.just(1, 2);
    }

    public static Mono<Void> returnError() {
        return Mono.error(new Exception("error"));
    }
}
