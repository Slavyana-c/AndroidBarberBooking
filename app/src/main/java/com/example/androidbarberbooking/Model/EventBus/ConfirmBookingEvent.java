package com.example.androidbarberbooking.Model.EventBus;

public class ConfirmBookingEvent {
    private boolean isConfirm;

    public boolean isConfirm() {
        return isConfirm;
    }

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }

    public ConfirmBookingEvent(boolean isConfirm) {
        this.isConfirm = isConfirm;


    }
}
