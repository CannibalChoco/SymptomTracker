package com.example.user.symptomtracker.ui.adapter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.utils.GraphUtils;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * responsible for displaying all active symptoms in Todays View for allowing the user to log data
 */
public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {


    private List<SymptomEntity> symptomList;
    private OnSeverityClickListener clickListener;

    public interface OnSeverityClickListener {
        void onSeverityClicked(int parentId, int severity);
    }

    public TodayAdapter(List<SymptomEntity> symptomList, OnSeverityClickListener clickListener) {
        this.symptomList = symptomList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_today, parent, false);

        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SymptomEntity symptom = symptomList.get(position);
        holder.name.setText(symptom.getName());
    }

    @Override
    public int getItemCount() {
        return symptomList != null ? symptomList.size() : 0;
    }

    public void replaceDataSet(List<SymptomEntity> symptomList){
        if (!this.symptomList.isEmpty()){
            this.symptomList.clear();
        }

        this.symptomList.addAll(symptomList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.todaySymptom)
        TextView name;
        View view;

        OnSeverityClickListener listener;

        public ViewHolder(View itemView, OnSeverityClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
            this.listener = listener;
        }

        @OnClick({R.id.severity0,
                R.id.severity1,
                R.id.severity2,
                R.id.severity3,
                R.id.severity4,
                R.id.severity5,
                R.id.severity6,
                R.id.severity7,
                R.id.severity8,
                R.id.severity9,
                R.id.severity10})
        public void severitySet(View view) {
            int severity;

            int id = view.getId();

            switch (id) {
                case R.id.severity0:
                    severity = 0;
                    break;
                case R.id.severity1:
                    severity = 1;
                    break;
                case R.id.severity2:
                    severity = 2;
                    break;
                case R.id.severity3:
                    severity = 3;
                    break;
                case R.id.severity4:
                    severity = 4;
                    break;
                case R.id.severity5:
                    severity = 5;
                    break;
                case R.id.severity6:
                    severity = 6;
                    break;
                case R.id.severity7:
                    severity = 7;
                    break;
                case R.id.severity8:
                    severity = 8;
                    break;
                case R.id.severity9:
                    severity = 9;
                    break;
                case R.id.severity10:
                    severity = 10;
                    break;
                default:
                    severity = 0;
                    break;
            }

            int parentId = symptomList.get(getAdapterPosition()).getId();
            listener.onSeverityClicked(parentId, severity);
        }

    }

}
