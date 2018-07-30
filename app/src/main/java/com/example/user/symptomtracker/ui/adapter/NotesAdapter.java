package com.example.user.symptomtracker.ui.adapter;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.NoteEntity;
import com.example.user.symptomtracker.ui.DetailActivity;
import com.example.user.symptomtracker.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<NoteEntity> notes;
    private Context context;
    private OnNoteLongClickListener listener;

    public interface OnNoteLongClickListener {
        void onNoteEdit(int id, String text);
    }

    public NotesAdapter(Context context, List<NoteEntity> notes, OnNoteLongClickListener listener) {
        this.notes = notes;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_note, parent, false);

        return new ViewHolder(rootView, listener);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.note)
        TextView note;

        private OnNoteLongClickListener listener;

        public ViewHolder(View itemView, OnNoteLongClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            NoteEntity note = notes.get(getAdapterPosition());
            listener.onNoteEdit(note.getId(), note.getContent());
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(DetailActivity.VIBRATE_MILLIS);
            return false;
        }
    }
}
