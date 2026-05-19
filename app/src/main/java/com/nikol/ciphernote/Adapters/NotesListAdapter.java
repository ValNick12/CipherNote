package com.nikol.ciphernote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nikol.ciphernote.Model.Notes;
import com.nikol.ciphernote.NotesClickListener;
import com.nikol.ciphernote.R;

import java.util.List;
import java.util.Objects;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder>{
    Context context;
    List<Notes> list;
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Notes note = list.get(position);
        holder.textView_title.setText(note.getTitle());
        holder.textView_title.setSelected(true);

        holder.textView_notes.setText(note.getNote());

        holder.notes_container.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onClick(list.get(pos));
            }
        });

        holder.notes_container.setOnLongClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onLongClick(list.get(pos), holder.notes_container);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Notes> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return list.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return list.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Notes oldNote = list.get(oldItemPosition);
                Notes newNote = newList.get(newItemPosition);
                return Objects.equals(oldNote.getTitle(), newNote.getTitle()) &&
                        Objects.equals(oldNote.getNote(), newNote.getNote());
            }
        });
        this.list = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        CardView notes_container;
        TextView textView_title, textView_notes;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notes_container = itemView.findViewById(R.id.notes_container);
            textView_notes = itemView.findViewById(R.id.textView_notes);
            textView_title = itemView.findViewById(R.id.textView_title);
        }
    }
}
