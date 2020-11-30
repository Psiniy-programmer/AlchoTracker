package technokek.alchotracker.api;

public interface TimerInterface {
    void onChoose();
    void onStartPressed();
    void onInterruptPressed();
    void onPausePressed();
    void onContinuePressed();
    void onFinish();
}
