package com.example.myapplication.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class NotesListAdapter {

}

class NotesViewHolder extends RecyclerView.ViewHolder {
    CardView notes_container;
    TextView textView_title, textView_notes;
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_container = itemView.findViewById(R.id.notes_container);
        textView_notes = itemView.findViewById(R.id.textView_notes);
        textView_title = itemView.findViewById(R.id.textView_title);
    }
}