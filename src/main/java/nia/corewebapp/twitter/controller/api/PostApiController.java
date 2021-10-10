package nia.corewebapp.twitter.controller.api;


import nia.corewebapp.twitter.dto.PostDto;
import nia.corewebapp.twitter.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import nia.corewebapp.twitter.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
public class PostApiController {

    private final PostRepository postRepository;
    private final PostService postService;

    @Autowired
    public PostApiController(PostRepository postRepository,
                             PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> findAll(){
        return ResponseEntity.ok(postService.findAll()
                .stream()
                .map(PostDto::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> findById(@PathVariable long postId){
        return ResponseEntity.ok(new PostDto(postService.findById(postId)));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody PostDto postDto){
        return ResponseEntity.ok(postService.create(postDto));
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable long postId,
                       @RequestBody PostDto postDto){
        postDto.setPostId(postId);
        postService.update(postDto);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long postId){
        postService.delete(postId);
    }

}
