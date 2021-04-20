package com.example.btvn7;

import android.widget.Toast;

import static java.security.AccessController.getContext;


public class PresenterInfo {
    IPhoneBook iPhoneBook;

    public PresenterInfo(IPhoneBook iPhoneBook) {
        this.iPhoneBook = iPhoneBook;
    }
    public boolean onInfo(String name, String phone){
        if(name.equals("")||phone.equals("")){
            iPhoneBook.onMessenger("Tên hoặc số điện thoại không được để trống");
            return false;
        }else return true;
    }
}
