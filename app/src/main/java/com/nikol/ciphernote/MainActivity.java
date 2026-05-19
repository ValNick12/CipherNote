package com.nikol.ciphernote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.nikol.ciphernote.Adapters.NotesListAdapter;
import com.nikol.ciphernote.Database.RoomDB;
import com.nikol.ciphernote.Model.Notes;
import com.nikol.ciphernote.Model.Profiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikol.ciphernote.cryptography.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private NotesListAdapter notesListAdapter;
    private List<Notes> notes = new ArrayList<>();
    private RoomDB database;
    private Notes selectedNote;
    private Profiles profile;

    private final ActivityResultLauncher<Intent> noteActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Notes new_note = (Notes) result.getData().getSerializableExtra("note");
                    if (new_note != null) {
                        database.mainDAO().insert(new_note);
                        notes = database.mainDAO().getAll(profile.getUsername());
                        notesListAdapter.filterList(notes);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_home);
        FloatingActionButton fab_add = findViewById(R.id.fab_home);
        SearchView searchView_Main = findViewById(R.id.searchView_Main);
        ImageView imageView_logout = findViewById(R.id.imageView_logout);
        profile = (Profiles) getIntent().getSerializableExtra("user");

        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll(profile.getUsername());

        updateRecycler(recyclerView, notes);

        fab_add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotesTakingActivity.class);
            intent.putExtra("user", profile);
            noteActivityResultLauncher.launch(intent);
        });

        imageView_logout.setOnClickListener(v -> {
            SessionManager.getInstance().clear();
            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logout);
            finish();
        });



        searchView_Main.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();
        for (Notes singleNote : notes){
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNote.getNote().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
    }

    private void updateRecycler(RecyclerView recyclerView, List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakingActivity.class);
            intent.putExtra("old_note", notes);
            intent.putExtra("user", profile);
            noteActivityResultLauncher.launch(intent);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.delete) {
            database.mainDAO().delete(selectedNote);
            int index = notes.indexOf(selectedNote);
            if (index != -1) {
                notes.remove(index);
                notesListAdapter.notifyItemRemoved(index);
            }
            Toast.makeText(MainActivity.this, "The note was deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            return false;
        }
    }
}
