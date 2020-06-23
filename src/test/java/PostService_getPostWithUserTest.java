import static org.junit.Assert.assertEquals;

import reactor.test.StepVerifier;

public class PostService_getPostWithUserTest {

    @org.junit.Test
    public void shouldReturnThePostWithUser() {
        final var postService = new PostService();

        final var postWithUserMono = postService.getPostWithUser(1);

        StepVerifier.create(postWithUserMono)
                .consumeNextWith(postWithUser -> {
                    assertEquals("Bret", postWithUser.user.username);
                })
                .verifyComplete();
    }
}
