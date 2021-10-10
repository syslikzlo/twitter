package nia.corewebapp.twitter.service;

import nia.corewebapp.twitter.dto.PostDto;
import nia.corewebapp.twitter.entity.Post;

import java.util.List;

public interface PostService {

    List<Post> findAll();

    List<Post> findByUsername(String username);

    List<Post> findByContent(String content);

    Post findById(long postId);

    long create(PostDto postDto);

    void update(PostDto postDto);

    void checkAuthorityOnPost(long postId);

    void delete(long postId);
}
