package com.example.myapplication.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.myapplication.Model.Notes;
import com.example.myapplication.Model.Profiles;

import java.util.List;

@Dao
public interface MainDAO {
    @Insert(onConflict = REPLACE)
    void insert(Notes notes);

    @Insert(onConflict = REPLACE)
    void insert_profile(Profiles profile);

    @Query("SELECT * FROM notes WHERE user = :username ORDER BY id DESC")
    List<Notes> getAll(String username);

    @Query("SELECT * FROM notes WHERE id = :id")
    Notes getNoteById(int id);

    @Query("SELECT password_salt FROM profiles WHERE username = :username")
    byte[] getSalt(String username);

    @Query("SELECT username FROM profiles WHERE username = :username")
    String getUsername(String username);

    @Query("SELECT password_hash FROM profiles WHERE username = :username")
    String getPasswordHash(String username);

    @Query("SELECT * FROM profiles WHERE username = :username")
    Profiles getProfile(String username);

    @Query("UPDATE notes SET title = :title, note = :note WHERE ID = :id")
    void update(int id, String title, String note);

    @Delete
    void delete(Notes notes);
}
