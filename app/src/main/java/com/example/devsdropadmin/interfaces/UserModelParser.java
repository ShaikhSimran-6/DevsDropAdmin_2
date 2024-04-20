package com.example.devsdropadmin.interfaces;

import com.example.devsdropadmin.model.UserModel;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;

public class UserModelParser implements SnapshotParser<UserModel> {
    @Override
    public UserModel parseSnapshot(DataSnapshot snapshot) {
        UserModel userModel = snapshot.getValue(UserModel.class);
        if (userModel != null) {
            userModel.setUserId(snapshot.getKey()); // Assuming id is stored as key in the database
        }
        return userModel;
    }
}
