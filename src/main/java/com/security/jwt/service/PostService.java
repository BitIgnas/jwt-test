package com.security.jwt.service;

import com.security.jwt.dto.PostDto;
import com.security.jwt.model.Post;
import com.security.jwt.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final PostRepository postRepository;

    public void savePost(PostDto postDto) {
        Post post = mapToPost(postDto);
        post.setUser(authService.getCurrentUser());

        postRepository.save(post);
    }

    public List<PostDto> findAllPost() {
        return postRepository.findAll().stream()
                .map(this::mapToPostDto)
                .collect(Collectors.toList());
    }

    public Post mapToPost(PostDto postDto) {
        return modelMapper.map(postDto, Post.class);
    }

    public PostDto mapToPostDto(Post post) {
        return modelMapper.map(post, PostDto.class);
    }
}
