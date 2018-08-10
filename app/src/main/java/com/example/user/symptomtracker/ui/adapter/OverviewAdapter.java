package com.example.user.symptomtracker.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.Symptom;
import com.example.user.symptomtracker.utils.GraphUtils;
import com.github.mikephil.charting.charts.BarChart;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {

    private List<Symptom> symptomList;
    private OnSymptomClickListener clickListener;
    private Context context;

    public interface OnSymptomClickListener{
        void onSymptomSelected(int id);
    }

    public OverviewAdapter(Context context, List<Symptom> symptomList,
                           OnSymptomClickListener listener) {
        this.symptomList = symptomList;
        this.clickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_overview, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Symptom symptom = symptomList.get(position);
        holder.name.setText(symptom.getSymptom().getName());
        GraphUtils.initBarChart(context, holder.graph, symptom.getSeverityList());
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

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{

        private OnSymptomClickListener listener;

        @BindView(R.id.symptomName)
        TextView name;
        @BindView(R.id.list_item_graph)
        BarChart graph;

        public ViewHolder(View itemView, OnSymptomClickListener listener) {
            super(itemView);

            this.listener = listener;

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            graph.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // the item id in db
            int position = getAdapterPosition();
            Symptom symptom = symptomList.get(position);
            int id = symptom.getSymptom().getId();
            listener.onSymptomSelected(id);
        }
    }
}
