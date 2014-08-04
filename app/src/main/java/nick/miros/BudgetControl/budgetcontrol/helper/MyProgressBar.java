package nick.miros.BudgetControl.budgetcontrol.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

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
    DecimalFormat numberFormat = new DecimalFormat("0.00");

    //Initializes all of the needed objects and variables for the ProgressBar
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

    /**
     * Sets the upper limit of the progress bar.
     * @param max the new maximum value the upper limit of the progress bar should be set to.
     */
    public void setMax(double max) {

        this.max = max;

        ratio.setText(Currency.getCurrentCurrencyUsed(getContext())
                      + numberFormat.format(max)
                      + " / "
                      + Currency.getCurrentCurrencyUsed(getContext())
                      + numberFormat.format(max));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;

        valueBar.setLayoutParams(layoutParams);
        valueBar.setBackgroundColor(Color.GREEN);
    }

    /**
     * Empties or fills (rarely) the progress bar
     * @param expenses the total amount of expenses to be taken into account
     */
    public void updateProgress(double expenses) {

        //shows the coeff of how full the progress bar should be
        double fullCoeff = 1 - (expenses / max);

        //in case the value is less than or equals to zero - just remove the green progress
        //and set zero as the bottom range
        if (fullCoeff <= 0) {
            ratio.setText(Currency.getCurrentCurrencyUsed(getContext()) +
                          "0.00 / " +
                          Currency.getCurrentCurrencyUsed(getContext()) +
                          max);
            //make the text red
            ratio.setTextColor(Color.RED);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 0;
            valueBar.setLayoutParams(layoutParams);
        }
        // else update the progress bar by the value of the fullCoeff
        else {
            ratio.setTextColor(Color.BLACK);
            ratio.setText(Currency.getCurrentCurrencyUsed(getContext())
                    + numberFormat.format(max - expenses)
                    + " / "
                    + Currency.getCurrentCurrencyUsed(getContext())
                    + numberFormat.format(max));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = (float) fullCoeff;

            valueBar.setLayoutParams(layoutParams);
            progressBarBackground.setBackgroundColor(Color.GRAY);

            //change colors depending on how full the progress bar is
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
