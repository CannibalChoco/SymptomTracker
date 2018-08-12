package com.example.user.symptomtracker.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.database.entity.Symptom;
import com.example.user.symptomtracker.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * responsible for displaying all active symptoms in Todays View for allowing the user to log data
 */
public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    private List<Symptom> symptomList;
    private OnSeverityClickListener clickListener;

    public interface OnSeverityClickListener {
        void onSeverityInsert(int parentId, int severity);

        void onSeverityUpdate(int severityEntityId, int newSeverityValue);

        void onSymptomSelected(int id);
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
            holder.setPressedForSeverity(lastSeverityEntity.getSeverity());
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

        this.symptomList.addAll(symptomList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.todaySymptom)
        TextView name;

        @BindView(R.id.severity0)
        Button button0;
        @BindView(R.id.severity1)
        Button button1;
        @BindView(R.id.severity2)
        Button button2;
        @BindView(R.id.severity3)
        Button button3;
        @BindView(R.id.severity4)
        Button button4;
        @BindView(R.id.severity5)
        Button button5;
        @BindView(R.id.severity6)
        Button button6;
        @BindView(R.id.severity7)
        Button button7;
        @BindView(R.id.severity8)
        Button button8;
        @BindView(R.id.severity9)
        Button button9;
        @BindView(R.id.severity10)
        Button button10;

        List<Button> buttonList;

        OnSeverityClickListener listener;

        public ViewHolder(View itemView, OnSeverityClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;

            buttonList = new ArrayList<>();
            buttonList.add(button0);
            buttonList.add(button1);
            buttonList.add(button2);
            buttonList.add(button3);
            buttonList.add(button4);
            buttonList.add(button5);
            buttonList.add(button6);
            buttonList.add(button7);
            buttonList.add(button8);
            buttonList.add(button9);
            buttonList.add(button10);
        }

        /**
         * Handle logic for listeners
         *
         * @param view clicked view
         */
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
            // get severity for view
            int severity = getSeverityForView(view.getId());
            int position = getAdapterPosition();
            // check if added today
            boolean lastSeverityAddedToday = lastSeverityAddedToday(symptomList.get(position));
            if (lastSeverityAddedToday) {
                sendUpdateData(severity);
            } else {
                sendInsertData(severity);
            }
        }

        @OnClick(R.id.todaySymptom)
        public void symptomSelected(View view){
            int position = getAdapterPosition();
            Symptom symptom = symptomList.get(position);
            listener.onSymptomSelected(symptom.getSymptom().getId());
        }

        /**
         * Sends parentId and severity value through listener for new SeverityEntity creation
         * for insertion in db
         * @param severity value of severity in range 0 - 10
         */
        private void sendInsertData(int severity) {
            int parentId = symptomList.get(getAdapterPosition()).getSymptom().getId();
            listener.onSeverityInsert(parentId, severity);
        }

        /**
         * Sends SeverityEntity Id and new severity value through listener for existing
         * SeverityEntity updating.
         * @param severity new value of severity in range 0 - 10
         */
        private void sendUpdateData(int severity) {
            List<SeverityEntity> severityEntityList = symptomList.get(getAdapterPosition())
                    .getSeverityList();
            SeverityEntity lastSeverityEntity = severityEntityList
                    .get(severityEntityList.size() - 1);
            listener.onSeverityUpdate(lastSeverityEntity.getId(), severity);
        }

        /**
         * Matches the pressed button to a severity value
         * @param checkedId view Id of the checked view Id
         * @return
         */
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

        /**
         * Matches severity value to a button. Calls setButtonListState, passing the button to set
         * pressed
         * @param severity int severity value
         */
        private void setPressedForSeverity(int severity) {
            Button button;
            switch (severity) {
                case 0:
                    button = button0;
                    break;
                case 1:
                    button = button1;
                    break;
                case 2:
                    button = button2;
                    break;
                case 3:
                    button = button3;
                    break;
                case 4:
                    button = button4;
                    break;
                case 5:
                    button = button5;
                    break;
                case 6:
                    button = button6;
                    break;
                case 7:
                    button = button7;
                    break;
                case 8:
                    button = button8;
                    break;
                case 9:
                    button = button9;
                    break;
                case 10:
                    button = button10;
                    break;
                default:
                    button = null;
                    break;
            }

            setButtonListState(button);
        }

        /**
         * Goes through the button list and sets button pressed state either to true or false
         * @param pressedButton the button that will be set pressed
         */
        private void setButtonListState(Button pressedButton){
            for (Button button : buttonList){
                if (button == pressedButton){
                    button.setPressed(true);
                } else {
                    button.setPressed(false);
                }
            }
        }
    }

    /**
     * Checks if the lase entered severity for a SymptomEntity was added today
     * @param symptom Symptom to check for last severity insertion time
     * @return true if added today, else false
     */
    private boolean lastSeverityAddedToday(Symptom symptom) {
        List<SeverityEntity> severityEntityList = symptom.getSeverityList();

        int severityListSize = severityEntityList.size();
        if (severityListSize > 0) {
            SeverityEntity lastSeverityEntity = severityEntityList.get(severityListSize - 1);

            return TimeUtils.severityAddedToday(lastSeverityEntity.getTimestamp());
        }

        return false;
    }
}
