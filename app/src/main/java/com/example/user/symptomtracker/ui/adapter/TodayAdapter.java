package com.example.user.symptomtracker.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.database.entity.Symptom;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * responsible for displaying all active symptoms in Todays View for allowing the user to log data
 */
public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    private static final int VIEW_NOT_FOUND = -1;
    private boolean userHasChecked = false;

    private List<Symptom> symptomList;
    private OnSeverityClickListener clickListener;

    public interface OnSeverityClickListener {
        void onSeverityClicked(int parentId, int severity);
    }

    public TodayAdapter(List<Symptom> symptomList,
                        OnSeverityClickListener clickListener) {
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
        Symptom symptom = symptomList.get(position);
        holder.name.setText(symptom.getSymptom().getName());

        List<SeverityEntity> severityList = symptom.getSeverityList();

        int severityListSize = severityList.size();
        if (severityList.size() > 0) {
            SeverityEntity severity = severityList.get(severityListSize - 1);
            int viewId = holder.getViewForSeverity(severity.getSeverity());
            if (viewId != VIEW_NOT_FOUND) {
                holder.selectionGroup.check(viewId);
            }
        }

    }

    @Override
    public int getItemCount() {
        return symptomList != null ? symptomList.size() : 0;
    }

    public void replaceSymptomData(List<Symptom> symptomList) {
        if (!this.symptomList.isEmpty()) {
            this.symptomList.clear();
        }

        this.symptomList.addAll(symptomList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.todaySymptom)
        TextView name;
        @BindView(R.id.selectionGroup)
        SingleSelectToggleGroup selectionGroup;
        View view;

        OnSeverityClickListener listener;

        public ViewHolder(View itemView, OnSeverityClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
            this.listener = listener;

            setSelectionListener();
        }

        private void setSelectionListener() {
            selectionGroup.setOnCheckedChangeListener((group, checkedId) -> {
                int severity = getSeverityForView(checkedId);
                int parentId = symptomList.get(getAdapterPosition()).getSymptom().getId();
                if (userHasChecked){
                    listener.onSeverityClicked(parentId, severity);
                }

            });
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
            userHasChecked = true;
        }

        private int getSeverityForView(int checkedId) {
            int severity;
            switch (checkedId) {
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
            return severity;
        }

        private int getViewForSeverity(int severity) {
            int viewId;
            switch (severity) {
                case 0:
                    viewId = R.id.severity0;
                    break;
                case 1:
                    viewId = R.id.severity1;
                    break;
                case 2:
                    viewId = R.id.severity2;
                    break;
                case 3:
                    viewId = R.id.severity3;
                    break;
                case 4:
                    viewId = R.id.severity4;
                    break;
                case 5:
                    viewId = R.id.severity5;
                    break;
                case 6:
                    viewId = R.id.severity6;
                    break;
                case 7:
                    viewId = R.id.severity7;
                    break;
                case 8:
                    viewId = R.id.severity8;
                    break;
                case 9:
                    viewId = R.id.severity9;
                    break;
                case 10:
                    viewId = R.id.severity10;
                    break;
                default:
                    viewId = VIEW_NOT_FOUND;
                    break;
            }
            return viewId;
        }
    }
}
