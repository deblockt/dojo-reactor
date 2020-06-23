import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Basics {
    public static Mono<Void> empty() {
        throw new RuntimeException("not implemented");
    }

    public static Mono<Integer> return1() {
        throw new RuntimeException("not implemented");
    }

    public static Flux<Integer> return1And2() {
        throw new RuntimeException("not implemented");
    }

    public static Mono<Void> returnError() {
        throw new RuntimeException("not implemented");
    }
}
