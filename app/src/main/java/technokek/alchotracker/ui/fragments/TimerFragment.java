package technokek.alchotracker.ui.fragments;

import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import technokek.alchotracker.R;
import technokek.alchotracker.api.State;
import technokek.alchotracker.api.TimerInterface;
import technokek.alchotracker.ui.activity.MainActivity;

public class TimerFragment extends Fragment implements TimerInterface {
    private int notificationIdCh1 = 1;
    private int notificationIdCh2 = 1;
    private Resources res;
    private TextView tvTimer;
    private TextView tvTimeLeft;
    private Button interruptButton;
    private Button startButton;
    private long startHour, startMinute;
    private long tHour, tMinute;
    private Calendar calendar;
    private CountDownTimer countDownTimer;
    private State state = State.CHOOSING;

    public TimerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getActivity().getResources();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        interruptButton = view.findViewById(R.id.button_interrupt);
        interruptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInterruptPressed();
            }
        });
        startButton = view.findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == State.CHOOSING) {
                    onStartPressed();
                }
                else if (state == State.RUNNING) {
                    onPausePressed();
                }
                else if (state == State.PAUSED) {
                    onContinuePressed();
                }
                //TODO
            }
        });
        tvTimeLeft = view.findViewById(R.id.time_textView);
        tvTimer = view.findViewById(R.id.timer);
        tvTimer.setOnClickListener(new TvTimerOnClickListener());
        onChoose();
    }

    @Override
    public void onChoose() {
        tvTimer.setText(R.string.select_timer_hint);
        enableInterruptButton(false);
        interruptButton.setText(res.getString(R.string.button_interrupt_text));
        startButton.setEnabled(false);
        startButton.setText(res.getString(R.string.button_start_text));
        state = State.CHOOSING;
    }

    @Override
    public void onStartPressed() {
        enableInterruptButton(true);
        startButton.setText(res.getString(R.string.button_pause_text));
        startCountDown();
        state = State.RUNNING;
    }

    @Override
    public void onInterruptPressed() {
        countDownTimer.cancel();
        onFinish();
        onChoose();
    }

    @Override
    public void onPausePressed() {
        countDownTimer.cancel();
        startButton.setText(res.getString(R.string.button_continue_text));
        state = State.PAUSED;
    }

    @Override
    public void onContinuePressed() {
        startButton.setText(res.getString(R.string.button_pause_text));
        startCountDown();
        state = State.RUNNING;
    }

    @Override
    public void onFinish() {
        tvTimeLeft.setVisibility(View.INVISIBLE);
        //TODO save DATA in DB
        //TODO send notification to function
        sendFinishNotification();
        onChoose();
    }

    private void sendFinishNotification() {
        if (startHour != tHour || startMinute != tMinute) {
            String drinkingTime = (startHour - tHour) + ":" + (startMinute - tMinute);
            ((technokek.alchotracker.ui.activity.MainActivity)getActivity()).sendTimerFragmentNotificationChannel1(
                    notificationIdCh1++,
                    "AlkoTime's up!",
                    "You've been drinking for " + drinkingTime
            );
        }
    }

    private void sendReminderNotification() {
        if (((startMinute - tMinute) == 30 || (startMinute - tMinute) == 0) && (startHour != tHour ||
                startMinute != tMinute)) {
            String drinkingTime = (startHour - tHour) + ":" + (startMinute - tMinute);
            ((MainActivity)getActivity()).sendTimerFragmentNotificationChannel2(
                    notificationIdCh2++,
                    "Rest a bit!",
                    "You've been drinking for " + drinkingTime
            );
        }
    }

    private void startCountDown() {
        if (state != State.RUNNING) {
            Log.d("TimeInMillis", Long.toString(chosenTimeToMillis()));
            countDownTimer = new CountDownTimer(chosenTimeToMillis(), res.getInteger(R.integer.countdown_interval)) {

                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d("Hours from Millis", Long.toString(getHoursFromMillis(millisUntilFinished)));
                    tHour = getHoursFromMillis(millisUntilFinished);
                    Log.d("Minutes from Millis", Long.toString(getMinutesFromMillis(millisUntilFinished)));
                    tMinute = getMinutesFromMillis(millisUntilFinished);
                    sendReminderNotification();
                    setTvTimeLeft();
                }

                @Override
                public void onFinish() {
                    Toast.makeText(getActivity(), "Timer finished", Toast.LENGTH_LONG).show();
                    TimerFragment.this.onFinish();
                    onChoose();
                }
            }.start();
        }
    }

    private long chosenTimeToMillis() {
        return  ((tHour*res.getInteger(R.integer.seconds_in_hour) +
                tMinute*res.getInteger(R.integer.seconds_in_minute)) *
                res.getInteger(R.integer.millis_in_seconds));
    }

    private long getHoursFromMillis(long millis) {
        return  (millis / (res.getInteger(R.integer.seconds_in_hour) *
                res.getInteger(R.integer.millis_in_seconds)));
    }

    private long getMinutesFromMillis(long millis) {
        return  (long) Math.ceil((double)(millis - getHoursFromMillis(millis) * (res.getInteger(R.integer.seconds_in_hour) *
                res.getInteger(R.integer.millis_in_seconds))) / (res.getInteger(R.integer.seconds_in_minute)*
                res.getInteger(R.integer.millis_in_seconds)));
    }

    private void setTvTimeLeft() {
        setCurrentCalendarTime();
        tvTimeLeft.setText(DateFormat.format("HH:mm",calendar));
        Log.d("tHour", Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));
    }

    private void setCurrentCalendarTime() {
        calendar.set(0,0,0, (int) tHour, (int)tMinute);
    }

    private void enableInterruptButton(boolean enable) {
        if (enable) {
            interruptButton.setEnabled(true);
            interruptButton.setBackground(res.getDrawable(R.drawable.round_button_interrupt_active));
        }
        else {
            interruptButton.setEnabled(false);
            interruptButton.setBackground(res.getDrawable(R.drawable.round_button_interrupt_inactive));
        }
    }

    private class TvTimerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //Initialize time picker dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            //Initialize time
                            tHour = hourOfDay;
                            startHour = hourOfDay;
                            tMinute = minute;
                            startMinute = minute;
                            //Initialize calendar
                            calendar = Calendar.getInstance();
                            //Set hour and minute for finish time
                            calendar.set(0,0,0,
                                    calendar.get(Calendar.HOUR_OF_DAY) + (int)tHour,
                                    calendar.get(Calendar.MINUTE) + (int)tMinute);
                            //Set selected time on text view
                            tvTimer.setText(DateFormat.format("HH:mm",calendar));
                            //Set time for time to end
                            setTvTimeLeft();
                            tvTimeLeft.setVisibility(View.VISIBLE);
                        }
                    }, 0, 0, true
            );
            //Чтобы кнопка активировалась
            startButton.setEnabled(true);
            //Set transparent background
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //Display previous selected time
            timePickerDialog.updateTime((int)tHour, (int)tMinute);
            //Show dialog
            timePickerDialog.show();
        }
    }
}
