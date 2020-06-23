import org.junit.Test;
import reactor.test.StepVerifier;

public class BasicsTest {

    @Test
    public void shouldReturnEmpty() {
        StepVerifier.create(Basics.empty())
            .verifyComplete();
    }

    @Test
    public void shouldReturn1() {
        StepVerifier.create(Basics.return1())
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    public void shouldReturnFluxWith1And2() {
        StepVerifier.create(Basics.return1And2())
                .expectNext(1)
                .expectNext(2)
                .verifyComplete();
    }

    @Test
    public void shouldReturnAnError() {
        StepVerifier.create(Basics.returnError())
            .expectErrorMatches(error -> error.getMessage().equals("error"))
            .verify();
    }
}
