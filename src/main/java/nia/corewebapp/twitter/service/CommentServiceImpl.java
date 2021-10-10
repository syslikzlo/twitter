package nia.corewebapp.twitter.service;

import nia.corewebapp.twitter.entity.Comment;
import nia.corewebapp.twitter.entity.Post;
import nia.corewebapp.twitter.entity.User;
import nia.corewebapp.twitter.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nia.corewebapp.twitter.repository.CommentRepository;
import nia.corewebapp.twitter.repository.PostRepository;
import nia.corewebapp.twitter.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public long create(long postId, String content) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository
                .findByUsername(SecurityUtils.getCurrentUserDetails().getUsername())
                .orElseThrow();

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);
        comment.setDtCreated(LocalDateTime.now());

        commentRepository.save(comment);
        return comment.getCommentId();
    }
}
