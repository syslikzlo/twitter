package nia.corewebapp.twitter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import nia.corewebapp.twitter.entity.Post;
import nia.corewebapp.twitter.entity.Tag;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {

    private long postId;

    private String title;

    private String content;

    private String tags;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dtCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dtUpdated;

    private String username;

    public PostDto() {
        this.dtCreated = LocalDateTime.now();
    }

    public PostDto(Post post) {
        this();
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.dtCreated = post.getDtCreated();
        this.dtUpdated = post.getDtUpdated();
        this.username = post.getUser().getUsername();
        this.tags = post.getTags()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.joining(" "));
    }

    public PostDto(String title, String content) {
        this();
        this.title = title;
        this.content = content;
    }

    public PostDto(long postId, String title) {
        this();
        this.postId = postId;
        this.title = title;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getDtCreated() {
        return dtCreated;
    }

    public void setDtCreated(LocalDateTime dtCreated) {
        this.dtCreated = dtCreated;
    }

    public LocalDateTime getDtUpdated() {
        return dtUpdated;
    }

    public void setDtUpdated(LocalDateTime dtUpdated) {
        this.dtUpdated = dtUpdated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
