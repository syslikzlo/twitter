package nia.corewebapp.twitter.repository;

import nia.corewebapp.twitter.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    String FIND_SORTED_BY_TAGS = """
            select
            	p.*
            from
            	post p
            inner join
            	post_tag pt
            	using (post_id)
            group by p.post_id
            order by count(*) desc;
            """;

    List<Post> findByTitle(String name);

    List<Post> findByDtCreatedBetween(LocalDateTime before, LocalDateTime after);

    //Метод, который находит все посты, имеющие в своем тексте(content) заданную подстроку
    //* Без учёта регистра текста
    //Написать тест на этот метод
    List<Post> findByContentContainingIgnoreCaseOrderByDtCreatedDesc(String pattern);

    //All posts by some user
    List<Post> findByUser_usernameOrderByDtCreatedDesc(String username);

    @Query(value = FIND_SORTED_BY_TAGS, nativeQuery = true)
    List<Post> findSortedByTags();

}
