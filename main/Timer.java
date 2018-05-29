package com.example.testapp.main;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.testapp.R;

import java.util.Calendar;
import java.util.Locale;

public class Timer {
    private boolean timerWasLaunched = false, threadWasLaunched = false;
    private Handler handlerForTimer;
    private TextView tvTimer;
    private TextView tvUnderCaption;
    private CountDownTimer timer;
    private int formatTime = 1;
    private final int TIME_IN_SECONDS = 1, NORMAL_TIME = 2;
    private ImageButton ibChangeFormatLeft, ibChangeFormatRight;
    private Animation animHide, animVisible;

    public Timer(final Activity activity, View view) {
        initialize(activity, view);

        animHide = AnimationUtils.loadAnimation(activity, R.anim.opacityhide);
        animVisible = AnimationUtils.loadAnimation(activity, R.anim.opacityvisible);
        Animation.AnimationListener opacityHideListener = new Animation.AnimationListener() {
            boolean animWasPlayed = false;

            @Override
            public void onAnimationStart(Animation animation) {
                if (!animWasPlayed) {
                    Animation anim = AnimationUtils.loadAnimation(activity, R.anim.opacityhide);
                    animWasPlayed = true;
                    tvUnderCaption.startAnimation(anim);
                } else {
                    animWasPlayed = false;
                    Handler handlerForAnimCaption = new Handler();
                    handlerForAnimCaption.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Animation anim = AnimationUtils.loadAnimation(activity, R.anim.opacityvisible);
                            tvUnderCaption.startAnimation(anim);
                        }
                    }, 480);

                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvTimer.startAnimation(animVisible);
//                tvTimer2.startAnimation(animVisible);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        animHide.setAnimationListener(opacityHideListener);
//        rebuildTimer();
    }

    private void initialize(Activity activity, View view) {

        tvTimer = view.findViewById(R.id.textViewTimer);
//        tvTimer2 = view.findViewById(R.id.textViewTimer2);
        TextView tvCaption = view.findViewById(R.id.textViewCaption);
        tvUnderCaption = view.findViewById(R.id.textViewUnderCaption);
        ibChangeFormatLeft = view.findViewById(R.id.ibChangeFormatLeft);
        ibChangeFormatRight = view.findViewById(R.id.ibChangeFormatRight);

        ibChangeFormatLeft.setOnClickListener(imageButtonListener);
        ibChangeFormatRight.setOnClickListener(imageButtonListener);

        tvCaption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        tvTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
//        tvTimer2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        tvUnderCaption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        Typeface timerFont = Typeface.createFromAsset(activity.getAssets(), "fonts/TimerFont.ttf");
        Typeface captionFont = Typeface.createFromAsset(activity.getAssets(), "fonts/CaptionFont.ttf");
        tvTimer.setTypeface(timerFont);
//        tvTimer2.setTypeface(timerFont);
        tvCaption.setTypeface(captionFont);
        tvUnderCaption.setTypeface(captionFont);
    }


    public void rebuildTimer() {
        if (!threadWasLaunched) {
            handlerForTimer = new Handler();
            handlerForTimer.post(runUiThread);
            threadWasLaunched = true;
        } else {
            if (handlerForTimer != null) {
                handlerForTimer.removeCallbacks(runUiThread);
                handlerForTimer = new Handler();
                handlerForTimer.postDelayed(runUiThread, 500);
            }
        }
    }

    //Получение времени с устройства для таймера
    private long getTimeForTimer() {
        Calendar calendar = Calendar.getInstance();
        long time = (calendar.get(Calendar.HOUR_OF_DAY) * 3600 * 1000);
        time += (calendar.get(Calendar.MINUTE) * 60 * 1000) + (calendar.get(Calendar.SECOND) * 1000);
        time = 86400000 - time;
        return time;
    }

    //Перезапуск таймера, когда счётчик доходит до 0, т.е. onFinish()
    private CountDownTimer restartTimer(int formatTime) {
        this.formatTime = formatTime;

        if (formatTime == TIME_IN_SECONDS) {
            if (tvUnderCaption.getVisibility() != View.VISIBLE) {
                tvUnderCaption.setVisibility(View.VISIBLE);
            }
            return new CountDownTimer(getTimeForTimer(), 1000) {
                @Override
                public void onTick(long timeMillisecond) {
                    if (!timerWasLaunched) {
                        timerWasLaunched = true;
                    }
                    String result = String.format(Locale.ENGLISH, "%d", timeMillisecond / 1000);
                    tvTimer.setText(result);
//                    tvTimer2.setText(result);

                    if (((timeMillisecond / 1000) % 10) == 1) {
                        tvUnderCaption.setText("Секунда");
                    } else if ((((timeMillisecond / 1000) % 10) >= 2) && (((timeMillisecond / 1000) % 10) <= 4)) {
                        tvUnderCaption.setText("Секунды");
                    } else if ((((timeMillisecond / 1000) % 10) >= 5) &&
                            (((timeMillisecond / 1000) % 10) <= 9) ||
                            (((timeMillisecond / 1000) % 10) == 0)) {
                        tvUnderCaption.setText("Секунд");
                    } else tvUnderCaption.setText("Секунд");
                }

                @Override
                public void onFinish() {
                    timer.cancel();
                    timer = restartTimer(1);
                    timer.start();
                }
            };
        } else {
            if (tvUnderCaption.getVisibility() != View.INVISIBLE) {
                tvUnderCaption.setVisibility(View.INVISIBLE);
            }
            return new CountDownTimer(getTimeForTimer(), 1000) {
                @Override
                public void onTick(long timeMillisecond) {
                    if (!timerWasLaunched) {
                        timerWasLaunched = true;
                    }
                    int hours = (int) (timeMillisecond / 1000 / 3600);
                    int minutes = (int) (timeMillisecond / 1000 - hours * 3600) / 60;
                    int seconds = (int) (timeMillisecond / 1000 - hours * 3600 - minutes * 60);
                    String sHours = String.valueOf(hours), sMinutes = String.valueOf(minutes), sSeconds = String.valueOf(seconds);
                    if (hours < 10) {
                        sHours = "0" + sHours;
                    }
                    if (minutes < 10) {
                        sMinutes = "0" + sMinutes;
                    }
                    if (seconds < 10) {
                        sSeconds = "0" + sSeconds;
                    }
                    String result = sHours + ":" + sMinutes + ":" + sSeconds;
                    tvTimer.setText(result);
//                    tvTimer2.setText(result);
                }

                @Override
                public void onFinish() {
                    timer.cancel();
                    timer = restartTimer(NORMAL_TIME);
                    timer.start();
                }
            };
        }
    }

    //Поток работы таймера
    private Runnable runUiThread = new Runnable() {
        @Override
        public void run() {
            if (timerWasLaunched) {
                timer.cancel();
            }
            if (formatTime == TIME_IN_SECONDS) {
                timer = restartTimer(TIME_IN_SECONDS);
            } else if (formatTime == NORMAL_TIME) {
                timer = restartTimer(NORMAL_TIME);
            } else {
                timer = restartTimer(TIME_IN_SECONDS);
            }
            ibChangeFormatLeft.setEnabled(true);
            ibChangeFormatRight.setEnabled(true);
            timer.start();
        }
    };

    private View.OnClickListener imageButtonListener = new View.OnClickListener() {
        boolean imgChangeFormatWasClicked = false;

        @Override
        public void onClick(View v) {
            tvTimer.startAnimation(animHide);
//            tvTimer2.startAnimation(animHide);
            if (timerWasLaunched) {
                timer.cancel();
            }
            if (!imgChangeFormatWasClicked) {
                formatTime = NORMAL_TIME;
                imgChangeFormatWasClicked = true;
            } else {
                formatTime = TIME_IN_SECONDS;
                imgChangeFormatWasClicked = false;
            }
            ibChangeFormatLeft.setEnabled(false);
            ibChangeFormatRight.setEnabled(false);
            rebuildTimer();
        }
    };

}