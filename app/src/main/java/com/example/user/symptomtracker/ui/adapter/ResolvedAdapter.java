package com.example.user.symptomtracker.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.Symptom;
import com.example.user.symptomtracker.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResolvedAdapter extends RecyclerView.Adapter<ResolvedAdapter.ViewHolder> {

    private List<Symptom> symptomList;

    private OnSymptomClickListener listener;

    public interface OnSymptomClickListener{
        void onResolvedSymptomSelected(int id);
    }

    public ResolvedAdapter(List<Symptom> symptomList, OnSymptomClickListener listener) {
        this.symptomList = symptomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ResolvedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_resolved, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ResolvedAdapter.ViewHolder holder, int position) {
        Symptom symptom = symptomList.get(position);
        holder.name.setText(symptom.getSymptom().getName());

        long resolvedTimestamp = symptom.getSymptom().getResolvedTimeStamp();
        String timestampString = TimeUtils.getDateStringFromTimestamp(resolvedTimestamp);
        holder.resolvedSince.setText(timestampString);
    }

    @Override
    public int getItemCount() {
        return symptomList != null ? symptomList.size() : 0;
    }

    public void replaceDataSet(List<Symptom> symptomList){
        int size = getItemCount();
        if (!this.symptomList.isEmpty()){
            this.symptomList.clear();
            notifyItemRangeChanged(0, size);
        }

        this.symptomList.addAll(symptomList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.resolvedName)
        TextView name;
        @BindView(R.id.textResolvedSince)
        TextView resolvedSince;

        OnSymptomClickListener listener;

        public ViewHolder(View itemView, OnSymptomClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            int symptomId = symptomList.get(position).getSymptom().getId();
            listener.onResolvedSymptomSelected(symptomId);
        }
    }
}
