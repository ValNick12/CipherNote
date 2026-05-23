package com.nikol.ciphernote.Network.Req;

public class DeleteNoteRequest {
    public String id;
    public long updatedAt;

    public DeleteNoteRequest(String id, long updatedAt) {
        this.id = id;
        this.updatedAt = updatedAt;
    }
}
