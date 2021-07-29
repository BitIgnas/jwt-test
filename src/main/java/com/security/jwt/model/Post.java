package com.security.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    private String postName;
    private String topic;
    @Lob
    private String postContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id")
    private Forum forum;
}
