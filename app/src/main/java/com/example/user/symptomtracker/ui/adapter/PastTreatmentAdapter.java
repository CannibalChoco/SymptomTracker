package com.example.user.symptomtracker.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;
import com.example.user.symptomtracker.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PastTreatmentAdapter extends RecyclerView.Adapter<PastTreatmentAdapter.ViewHolder> {

    private List<TreatmentEntity> treatments;
    private Context context;

    public PastTreatmentAdapter(Context context, List<TreatmentEntity> treatments) {
        this.treatments = treatments;
        this.context = context;
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

        if (treatment.getTakesEffectIn() != TreatmentEntity.TIME_NOT_SELECTED){
            String takesEffect = TimeUtils.getTimeUnitFromTimestamp(context,
                    treatment.getTakesEffectIn());
            holder.takesEffect.setText(takesEffect);
        } else {
            holder.takesEffect.setText(R.string.default_no_treatment_time_provided);
        }

        if (treatment.getWasSuccessful() == TreatmentEntity.WAS_SUCCESSFUL_NO) {
            holder.name.setTextColor(context.getResources().getColor(R.color.colorStatusAttention));
        } else if (treatment.getWasSuccessful() == TreatmentEntity.WAS_SUCCESSFUL_YES){
            holder.name.setTextColor(context.getResources().getColor(R.color.colorStatusGood));
        }

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
