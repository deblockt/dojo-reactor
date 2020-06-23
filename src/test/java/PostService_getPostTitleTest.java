import reactor.test.StepVerifier;

public class PostService_getPostTitleTest {

    @org.junit.Test
    public void shouldReturnThePostDetailIfExists() {
        final var postService = new PostService();

        final var post1Title = postService.getPostTitle(1);

        StepVerifier.create(post1Title)
                .expectNext("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
                .verifyComplete();
    }

    @org.junit.Test
    public void shouldReturnTheEmptyMonoWhenIgDoesNotExists() {
        final var postService = new PostService();

        final var post1Title = postService.getPostTitle(9999);

        StepVerifier.create(post1Title)
                .verifyComplete();
    }
}
