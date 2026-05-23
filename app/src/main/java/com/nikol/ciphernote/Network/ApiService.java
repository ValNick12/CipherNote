package com.nikol.ciphernote.Network;

import com.nikol.ciphernote.Network.Req.DeleteNoteRequest;
import com.nikol.ciphernote.Network.Req.LoginRequest;
import com.nikol.ciphernote.Network.Req.RegisterRequest;
import com.nikol.ciphernote.Network.Req.UpsertNoteRequest;
import com.nikol.ciphernote.Network.Res.AuthResponse;
import com.nikol.ciphernote.Network.Res.NotesResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("notes")
    Call<NotesResponse> getNotes();

    @POST("notes/upsert")
    Call<Void> upsertNote(@Body UpsertNoteRequest request);

    @POST("notes/delete")
    Call<Void> deleteNote(@Body DeleteNoteRequest request);
}
