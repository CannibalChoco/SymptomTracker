package com.example.user.symptomtracker.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.NoteEntity;
import com.example.user.symptomtracker.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<NoteEntity> notes;

    public NotesAdapter(List<NoteEntity> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_note, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteEntity note = notes.get(position);

        String date = TimeUtils.getDateStringFromTimestamp(note.getTimestamp());
        holder.date.setText(date);
        holder.note.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    public void replaceDataSet(List<NoteEntity> notes){
        if (!this.notes.isEmpty()){
            this.notes.clear();
        }

        this.notes.addAll(notes);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.note)
        TextView note;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
