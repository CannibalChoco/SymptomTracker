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
import com.example.user.symptomtracker.utils.TimeUtils;
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
        void onSeverityInsert(int parentId, int severity);

        void onSeverityUpdate(int severityEntityId, int newSeverityValue);
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

        // set checked if added today
        if (lastSeverityAddedToday(symptom)) {
            List<SeverityEntity> severityEntityList = symptom.getSeverityList();
            SeverityEntity lastSeverityEntity = severityEntityList.get(severityEntityList.size() - 1);
            int viewId = holder.getViewForSeverity(lastSeverityEntity.getSeverity());
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
        int size = this.symptomList.size();
        if (size > 0) {
            this.symptomList.clear();
            notifyItemRangeRemoved(0, size);
        }

        // TODO: reset the selection group ?


        this.symptomList.addAll(symptomList);
        notifyDataSetChanged();
    }

    public void addAllToAdapter(List<Symptom> symptomList) {
        this.symptomList.addAll(symptomList);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        int size = this.symptomList.size();
        if (size > 0) {
            this.symptomList.clear();
            notifyItemRangeRemoved(0, size);
        }
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

                if (userHasChecked) {
                    // check if added today
                    int position = getAdapterPosition();

                    boolean lastSeverityAddedToday = lastSeverityAddedToday(symptomList.get(position));
                    if (lastSeverityAddedToday) {
                        // if added today- update, sending severityId, new severity value
                        List<SeverityEntity> severityEntityList = symptomList.get(getAdapterPosition())
                                .getSeverityList();
                        SeverityEntity lastSeverityEntity = severityEntityList
                                .get(severityEntityList.size() - 1);
                        listener.onSeverityUpdate(lastSeverityEntity.getId(), severity);

                    } else {
                        // insert, sending parentId, severityValue
                        int parentId = symptomList.get(getAdapterPosition()).getSymptom().getId();
                        listener.onSeverityInsert(parentId, severity);
                    }
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

    private boolean lastSeverityAddedToday(Symptom symptom) {
        List<SeverityEntity> severityEntityList = symptom.getSeverityList();

        int severityListSize = severityEntityList.size();
        if (severityListSize > 0) {
            SeverityEntity lastSeverityEntity = severityEntityList.get(severityListSize - 1);

            // set checked if added today
            return TimeUtils.severityAddedToday(lastSeverityEntity.getTimestamp());
        }

        return false;
    }
}
