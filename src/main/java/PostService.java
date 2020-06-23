import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.*;
import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PostService {

    /**
     * methods to implements
     */
    public Mono<String> getPostTitle(Integer postId) {
        throw new RuntimeException("not implemented");
    }

    public Mono<PostWithComment> getPostWithComments(Integer postId) {
        throw new RuntimeException("not implemented");
    }

    public Mono<PostWithUser> getPostWithUser(int postId) { throw new RuntimeException("not implemented"); }

    /**
     * method to get data from API
     */
    public Mono<List<Comment>> getComments(Integer postId) {
        return HttpClient.create()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .get()
                .uri("/posts/" + postId + "/comments")
                .responseSingle(this::toString)
                .map(body -> PostService.convertToList(body, Comment.class));
    }

    public Mono<Post> getPost(Integer postId) {
        return HttpClient.create()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .get()
                .uri("/posts/" + postId)
                .responseSingle(this::toString)
                .map(body -> PostService.convertTo(body, Post.class));
    }

    public Mono<User> getUser(Integer userId) {
        return HttpClient.create()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .get()
                .uri("/users/" + userId)
                .responseSingle(this::toString)
                .map(body -> PostService.convertTo(body, User.class));
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
            return new ObjectMapper().readValue(body, new TypeReference<ArrayList<T>>(){});
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
