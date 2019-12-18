package com.example.test_quiz.Chat_Section;


public class ChatMessage {
    private String username;
    private String message;
    private String comment_time;
    private String user_image_path;

    ChatMessage(String username, String message,String comment_time,String user_image_path) {

        this.username = username;
        this.message = message;
        this.comment_time = comment_time;
        this.user_image_path = user_image_path;
    }

    // you need an empty constructor to get your object from the DataSnapshot (cf. MainActivity)
    public ChatMessage() {

    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getComment_time() { return comment_time; }

    public String getUser_image_path() { return user_image_path; }
}
