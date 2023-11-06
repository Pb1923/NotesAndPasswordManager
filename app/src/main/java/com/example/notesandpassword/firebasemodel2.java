package com.example.notesandpassword;

public class firebasemodel2 {

    private String title;
    private String password;

    public firebasemodel2()
    {

    }

    public firebasemodel2(String title,String password)
    {
        this.title=title;
        this.password=password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
