package com.nikol.ciphernote;

import androidx.cardview.widget.CardView;

import com.nikol.ciphernote.Model.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
