import static org.junit.Assert.*;
import reactor.test.StepVerifier;

public class PostService_getPostWithCommentTest {

    @org.junit.Test
    public void shouldReturnThePostDetailIfExists() {
        final var postService = new PostService();

        final var postWithCommentMono = postService.getPostWithComments(1);

        StepVerifier.create(postWithCommentMono)
                .consumeNextWith(postWithComment -> {
                    assertEquals(5, postWithComment.comments.size());
                })
                .verifyComplete();
    }
}
