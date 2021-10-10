package nia.corewebapp.twitter.repository;

import nia.corewebapp.twitter.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
