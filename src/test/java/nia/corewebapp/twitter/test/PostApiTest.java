package nia.corewebapp.twitter.test;

import nia.corewebapp.twitter.config.JpaConfig;
import nia.corewebapp.twitter.dto.PostDto;
import nia.corewebapp.twitter.entity.Post;
import nia.corewebapp.twitter.repository.PostRepository;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestOperations;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaConfig.class)
@Sql(scripts = "classpath:Blog.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PostApiTest {

    private final PostRepository postRepository;
    private final RestOperations restOperations;

    private static final String LOGIN = "user1";
    private static final String PASSWORD = "user1";

    private static final String URL = "http://localhost:8080/twitter/api/post/";

    private static final String TEST_GET_ALL = """
            [
                {
                    "postId": 3,
                    "title": "Day 3",
                    "content": "It's all bad!",
                    "tags": "life day thoughts",
                    "username": "user2",
                    "dtCreated": "2021-04-03 14:16:10"
                },
                {
                    "postId": 2,
                    "title": "Day 2",
                    "content": "It's all ok!",
                    "tags": "news life day ideas",
                    "username": "user1",
                    "dtCreated": "2021-04-03 14:16:05"
                },
                {
                    "postId": 1,
                    "title": "Day 1",
                    "content": "It's all good!",
                    "tags": "news life",
                    "username": "user1",
                    "dtCreated": "2021-04-03 14:16:00"
                }
            ]""";

    private static final String TEST_GET_BY_ID = """
            {
                "postId": 1,
                "title": "Day 1",
                "content": "It's all good!",
                "tags": "news life",
                "username": "user1",
                "dtCreated": "2021-04-03 14:16:00"
            }""";

    @Autowired
    public PostApiTest(PostRepository postRepository,
                       RestOperations restOperations) {
        this.postRepository = postRepository;
        this.restOperations = restOperations;
    }

    @Test
    void getAll() throws JSONException {

/*        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/api/post/"))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());*/


        ResponseEntity<String> response = restOperations
                .getForEntity(URL, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONAssert.assertEquals(TEST_GET_ALL, response.getBody(), true);
    }


    @Test
    void getById() throws JSONException {
        ResponseEntity<String> response = restOperations
                .getForEntity(URL + "/1", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(TEST_GET_BY_ID, response.getBody(), true);
    }

    @Test
    void create() throws JSONException {
        ResponseEntity<String> response = restOperations
                .postForEntity(URL, new HttpEntity<>(
                        new PostDto("Day 4", "All is ok again"),
                        getHeaders()
                ), String.class);

        Assertions.assertEquals("Day 4", postRepository.findById(4L).get().getTitle());

        Set<String> titles = postRepository.findAll().stream()
                .map(Post::getTitle)
                .collect(Collectors.toSet());

        assertEquals(Set.of("Day 1", "Day 2", "Day 3", "Day 4"), titles);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("4", response.getBody());
    }

    @Test
    void update() throws JSONException {
        restOperations.put(URL + "/1",
                new HttpEntity<>(
                        new PostDto( "Day 5", "Anything"),
                        getHeaders()));

        Post updatedPost = postRepository.findById(1L).get();
        assertEquals("Day 5", updatedPost.getTitle());
    }

    @Test
    void delete() throws JSONException {
        restOperations.exchange(URL + "/1",
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders()),
                Void.class);

        assertEquals(2, postRepository.findAll().size());
    }

    private HttpHeaders getHeaders() {
        String auth = LOGIN + ":" + PASSWORD;
        byte[] encoded = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + new String(encoded, StandardCharsets.UTF_8));
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }

}
