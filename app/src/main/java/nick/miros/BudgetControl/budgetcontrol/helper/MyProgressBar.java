package nick.miros.BudgetControl.budgetcontrol.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import nick.miros.BudgetControl.budgetcontrol.app.Currency;

/**
 * A custom view class for the monthly and daily progress bars
 */
public class MyProgressBar extends FrameLayout {

    Context context;
    public MyProgressBar(Context context) {
        super(context);
        this.context = context;
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    LinearLayout progressBarBackground;
    LinearLayout valueBar;
    TextView ratio;
    double max;

    {
        progressBarBackground = new LinearLayout(getContext());
        valueBar = new LinearLayout(getContext());
        ratio = new TextView(getContext());

        progressBarBackground.setWeightSum(1);
        progressBarBackground.setBackgroundColor(Color.GRAY);

        addView(progressBarBackground);
        progressBarBackground.addView(valueBar);

        addView(ratio);
        ratio.setTextColor(Color.BLACK);
        ratio.setGravity(Gravity.CENTER);
    }

    public void setMax(double max) {

        this.max = max;

        ratio.setText(Currency.getCurrentCurrencyUsed(getContext())
                      + max
                      + " / "
                      + Currency.getCurrentCurrencyUsed(getContext())
                      + max);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;

        valueBar.setLayoutParams(layoutParams);
        valueBar.setBackgroundColor(Color.GREEN);
    }

    public void updateProgress(double expenses) {
        double fullCoeff = 1 - (expenses / max);

        if (fullCoeff <= 0) {
            ratio.setText("Overdraft!");
            ratio.setTextColor(Color.RED);
            progressBarBackground.removeView(valueBar);
        }
        else {

            ratio.setText(Currency.getCurrentCurrencyUsed(getContext())
                    + (max - expenses)
                    + " / "
                    + Currency.getCurrentCurrencyUsed(getContext())
                    + max);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = (float) fullCoeff;

            valueBar.setLayoutParams(layoutParams);
            progressBarBackground.setBackgroundColor(Color.GRAY);

            if (fullCoeff >= 0.5) {
                valueBar.setBackgroundColor(Color.GREEN);
            } else if (fullCoeff >= 0.2) {
                valueBar.setBackgroundColor(Color.YELLOW);
            } else {
                valueBar.setBackgroundColor(Color.RED);
            }
        }
    }
}
