package com.nikol.ciphernote.Network;

import com.google.gson.annotations.SerializedName;

public class NoteDto {
    public String id;
    public String username;
    
    @SerializedName("title_ciphertext")
    public String titleCiphertext;
    
    @SerializedName("content_ciphertext")
    public String contentCiphertext;
    
    @SerializedName("wrapped_note_key")
    public String wrappedNoteKey;
    
    @SerializedName("updated_at")
    public long updatedAt;
    
    public int deleted;
}
