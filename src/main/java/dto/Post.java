package dto;

public class Post {
    public Integer userId;
    public Integer id;
    public String title;
    public String body;

    @Override
    public String toString() {
        return "dto.Post{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
