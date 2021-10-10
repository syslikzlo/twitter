package nia.corewebapp.twitter.controller;

import nia.corewebapp.twitter.dto.PostDto;
import nia.corewebapp.twitter.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import nia.corewebapp.twitter.repository.UserRepository;

import javax.servlet.ServletContext;

@Controller
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final ServletContext servletContext;

    @Autowired
    public PostController(PostService postService,
                          UserRepository userRepository,
                          ServletContext servletContext) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.servletContext = servletContext;
    }

    @GetMapping
    public String blog(@RequestParam(required = false) String search,
                       ModelMap model) {

        if (StringUtils.hasText(search)) {
            model.put("posts", postService.findByContent(search));
            model.put("title", "Search by");
            model.put("subtitle", search.length() < 20
                    ? search
                    : search.substring(20) + "...");
        } else {
            model.put("posts", postService.findAll());
            model.put("title", "All posts");
        }

        setCommonParams(model);
        return "blog";
    }

    @GetMapping("/user/{username}")
    public String user(@PathVariable String username,
                       ModelMap model) {
        model.put("posts", postService.findByUsername(username));
        model.put("title", "Posts by");
        model.put("subtitle", username);

        setCommonParams(model);
        return "blog";
    }

    @GetMapping("/post/{postId}")
    public String post(@PathVariable long postId, ModelMap model) {
        model.put("post", postService.findById(postId));
        setCommonParams(model);
        return "post";
    }

    @GetMapping("/post/new")
    @PreAuthorize("hasRole('USER')")
    public String post(ModelMap model) {
        setCommonParams(model);
        return "post-new";
    }

    @PostMapping("/post/new")
    public String post(PostDto postDto) {
        long postId = postService.create(postDto);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/post/{postId}/edit")
    @PreAuthorize("hasRole('USER')")
    public String postEdit(@PathVariable long postId, ModelMap model) {
        postService.checkAuthorityOnPost(postId);
        model.put("post", new PostDto(postService.findById(postId)));
        setCommonParams(model);
        return "post-edit";
    }

    @PostMapping("/post/edit")
    public String postEdit(PostDto postDto) {
        postService.update(postDto);
        return "redirect:/post/" + postDto.getPostId();
    }

    @PostMapping("/post/{postId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void postEdit(@PathVariable long postId) {
        postService.delete(postId);
    }

    private void setCommonParams(ModelMap model) {
        model.put("users", userRepository.findAll());
        model.put("contextPath", servletContext.getContextPath());
    }

}
