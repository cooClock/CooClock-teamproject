package com.example.cooclock;

import java.util.ArrayList;
import java.util.Map;

/**
 * 사용자 계정 정보 모델 클래스
 */
public class UserAccount {
    private String idToken;     // Firebase Uid (고유 토큰 정보)
    private String emailId;     // 이메일 아이디
    private String password;    // 비밀번호괴
    private String username;    // 비밀번호
    private ArrayList<knowHowItem> myRecipes; // 레시피 목록


    public UserAccount() {}

    public String getIdToken(){return idToken;}

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
    public ArrayList<knowHowItem> getMyRecipes() {
        return myRecipes;
    }

    public void setMyRecipes(ArrayList<knowHowItem> myRecipes) {
        this.myRecipes = myRecipes;
    }

    public String getEmailId(){
        return emailId;
    }

    public void setEmailId(String emailId){
        this.emailId = emailId;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }


}