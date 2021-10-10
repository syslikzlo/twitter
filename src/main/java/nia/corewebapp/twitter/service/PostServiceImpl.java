package nia.corewebapp.twitter.service;

import nia.corewebapp.twitter.dto.PostDto;
import nia.corewebapp.twitter.entity.Post;
import nia.corewebapp.twitter.entity.Tag;
import nia.corewebapp.twitter.entity.User;
import nia.corewebapp.twitter.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import nia.corewebapp.twitter.repository.PostRepository;
import nia.corewebapp.twitter.repository.TagRepository;
import nia.corewebapp.twitter.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           TagRepository tagRepository,
                           UserRepository userRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findAll() {
        List<Post> posts = postRepository.findAll(Sort.by("dtCreated").descending());
        posts.forEach(p -> p.getTags().size());
        return posts;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findByUsername(String username) {
        return postRepository.findByUser_usernameOrderByDtCreatedDesc(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findByContent(String content) {
        return postRepository.findByContentContainingIgnoreCaseOrderByDtCreatedDesc(content);
    }

    @Override
    @Transactional(readOnly = true)
    public Post findById(long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.getTags().size();
        post.getComments().size();
        return post;
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public long create(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setTags(parseTags(postDto.getTags()));
        post.setDtCreated(LocalDateTime.now());

        post.setUser(userRepository.findByUsername(SecurityUtils.getCurrentUserDetails()
                .getUsername()).orElseThrow());

        postRepository.save(post);
        return post.getPostId();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void update(PostDto postDto) {
        User user = postRepository.findById(postDto.getPostId()).orElseThrow().getUser();
        SecurityUtils.checkCurrentUser(user.getUsername());

        Post post = postRepository.findById(postDto.getPostId()).orElseThrow();
        if (postDto.getTitle() != null) {
            post.setTitle(StringUtils.hasText(postDto.getTitle())
                    ? postDto.getTitle() : "");
        }

        if (postDto.getContent() != null) {
            post.setContent(StringUtils.hasText(postDto.getContent())
                    ? postDto.getContent() : "");
        }

        if (postDto.getTags() != null) {
            post.setTags(StringUtils.hasText(postDto.getTags())
                    ? parseTags(postDto.getTags())
                    : Collections.emptyList());
        }

        post.setDtUpdated(LocalDateTime.now());
        postRepository.save(post);
    }

    @Override
    public void checkAuthorityOnPost(long postId) {
        SecurityUtils.checkCurrentUser(findById(postId).getUser().getUsername());
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void delete(long postId) {
        User user = postRepository.findById(postId).orElseThrow().getUser();
        SecurityUtils.checkCurrentUserIsAdminOr(user.getUsername());
        postRepository.deleteById(postId);
    }

    private List<Tag> parseTags(String tags) {
        if (tags == null)
            return Collections.emptyList();
        return Arrays.stream(tags.split(" "))
                .map(tagName -> tagRepository
                        .findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName))))
                .collect(Collectors.toList());
    }
}
