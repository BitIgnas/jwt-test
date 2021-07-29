package com.security.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Forum {
    @Id
    @GeneratedValue
    private Long id;
    private String forumName;
    private String pictureUrl;
    @OneToMany(mappedBy = "forum")
    private List<Post> forumPosts;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
