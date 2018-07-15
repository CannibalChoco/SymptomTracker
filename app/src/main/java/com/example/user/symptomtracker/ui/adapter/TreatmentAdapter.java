package com.example.user.symptomtracker.ui.adapter;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.ViewHolder> {

    private List<TreatmentEntity> treatments;

    public TreatmentAdapter(List<TreatmentEntity> treatments) {
        this.treatments = treatments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_treatment, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TreatmentEntity treatment = treatments.get(position);
        holder.name.setText(treatment.getName());
        holder.takesEffect.setText(String.valueOf(treatment.getTakesEffectIn()));
    }

    @Override
    public int getItemCount() {
        return treatments != null ? treatments.size() : 0;
    }

    public void replaceDataSet(List<TreatmentEntity> treatments){
        if (!this.treatments.isEmpty()){
            this.treatments.clear();
        }

        this.treatments.addAll(treatments);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.treatmentName)
        TextView name;
        @BindView(R.id.treatmentTakesEffect)
        TextView takesEffect;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}