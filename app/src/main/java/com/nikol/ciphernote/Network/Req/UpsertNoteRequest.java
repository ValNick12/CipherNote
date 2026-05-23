package com.nikol.ciphernote.Network.Req;

public class UpsertNoteRequest {
    public String id;
    public String titleCiphertext;
    public String contentCiphertext;
    public String wrappedNoteKey;
    public long updatedAt;

    public UpsertNoteRequest(String id, String titleCiphertext, String contentCiphertext, String wrappedNoteKey, long updatedAt) {
        this.id = id;
        this.titleCiphertext = titleCiphertext;
        this.contentCiphertext = contentCiphertext;
        this.wrappedNoteKey = wrappedNoteKey;
        this.updatedAt = updatedAt;
    }
}
