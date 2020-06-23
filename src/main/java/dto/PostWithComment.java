package dto;

import dto.Comment;

import java.util.List;

public class PostWithComment {
    public Integer userId;
    public Integer id;
    public String title;
    public String body;
    public List<Comment> comments;
}
