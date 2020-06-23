import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import dto.Comment;
import dto.Post;
import dto.PostWithComment;
import dto.PostWithUser;
import dto.User;
import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;

public class PostService {

    /**
     * methods to implements
     */
    public Mono<String> getPostTitle(Integer postId) {
        return getPost(postId).onErrorResume(NotFoundError.class, error -> Mono.empty()).map(post -> post.title);
    }

    public Mono<PostWithComment> getPostWithComments(Integer postId) {
        Mono<Post> post = getPost(postId).onErrorResume(NotFoundError.class, error -> Mono.empty());
        Mono<List<Comment>> comments = getComments(postId);

        if (false)
            return Mono.zip(post, comments).map(tuple -> {
                PostWithComment postWithComment = new PostWithComment();
                postWithComment.id = tuple.getT1().id;
                postWithComment.comments = tuple.getT2();
                return postWithComment;
            });
        else
            return post.zipWith(comments).map(tuple -> {
                PostWithComment postWithComment = new PostWithComment();
                postWithComment.id = tuple.getT1().id;
                postWithComment.comments = tuple.getT2();
                return postWithComment;
            });
    }

    public Mono<PostWithUser> getPostWithUser(int postId) {
        Mono<Post> post = getPost(postId).onErrorResume(NotFoundError.class, error -> Mono.empty());
        if (false)
            return post.zipWhen(p -> getUser(p.userId)).map(tuple -> {
                var pu = new PostWithUser();
                pu.id = tuple.getT1().id;
                pu.user = tuple.getT2();
                return pu;
            });
        else
            return post.flatMap(p -> {
                // action asynchrone
                return getUser(p.id).map(u -> {
                    // action synchrone
                    var pu = new PostWithUser();
                    pu.id = p.id;
                    pu.user = u;
                    return pu;
                });
            });
    }

    /**
     * method to get data from API
     */
    public Mono<List<Comment>> getComments(Integer postId) {
        return HttpClient.create().baseUrl("https://jsonplaceholder.typicode.com/").get()
                .uri("/posts/" + postId + "/comments")
                .responseSingle(this::toString)
                .map(body -> PostService.convertToList(body, Comment.class));
    }

    public Mono<Post> getPost(Integer postId) {
        return HttpClient.create().baseUrl("https://jsonplaceholder.typicode.com/").get().uri("/posts/" + postId)
                .responseSingle(this::toString).map(body -> PostService.convertTo(body, Post.class));
    }

    public Mono<User> getUser(Integer userId) {
        return HttpClient.create().baseUrl("https://jsonplaceholder.typicode.com/").get().uri("/users/" + userId)
                .responseSingle(this::toString).map(body -> PostService.convertTo(body, User.class));
    }

    /**
     * Helpers
     */
    private Mono<String> toString(HttpClientResponse rep, ByteBufMono value) {
        if (rep.status() == HttpResponseStatus.OK) {
            return value.map(buffer -> buffer.toString(Charset.defaultCharset()));
        } else {
            return Mono.error(new NotFoundError());
        }
    }

    private static <T> T convertTo(String body, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(body, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> convertToList(String body, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(body, new TypeReference<ArrayList<T>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static class NotFoundError extends RuntimeException {

        public NotFoundError() {
            super("the resource doesn't exists");
        }
    }
}
