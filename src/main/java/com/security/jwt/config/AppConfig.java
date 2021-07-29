package com.security.jwt.config;

import com.security.jwt.model.Forum;
import com.security.jwt.model.Post;
import com.security.jwt.model.User;
import com.security.jwt.repository.ForumRepository;
import com.security.jwt.repository.PostRepository;
import com.security.jwt.repository.ReplyRepository;
import com.security.jwt.repository.UserRepository;
import com.security.jwt.service.MailService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.transaction.Transactional;
import java.util.Arrays;

@Configuration
@AllArgsConstructor
public class AppConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ForumRepository forumRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@gmail.com");
        user.setEnabled(true);

        Forum forum = new Forum();
        forum.setForumName("Anno 1800");
        forum.setUser(user);

        Post post = new Post();
        post.setUser(user);
        post.setForum(forum);
        post.setPostName("Season 7 update");
        post.setPostContent("New season with new victorian era buildings");
        post.setTopic("Building");

        forumRepository.save(forum);
        postRepository.save(post);
        userRepository.save(user);

    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
