import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;

public class PostService_getPostWithUserTest {

    @org.junit.Test
    public void shouldReturnThePostDetailIfExists() {
        final var postService = new PostService();

        final var postWithUserMono = postService.getPostWithUser(1);

        StepVerifier.create(postWithUserMono)
                .consumeNextWith(postWithUser -> {
                    assertEquals("Bret", postWithUser.user.username);
                })
                .verifyComplete();
    }
}
