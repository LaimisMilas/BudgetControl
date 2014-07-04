package nick.miros.BudgetControl.budgetcontrol.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mymi on 04-Jul-14.
 */
public class MyProgressBar extends LinearLayout {


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

    LinearLayout valueBar;
    TextView textView;

    {
        setWeightSum(1);
        valueBar = new LinearLayout(getContext());
        textView = new TextView(getContext());
        valueBar.addView(textView);
        addView(valueBar);
    }


    public void updateProgress(double percentage) {

        textView.setText("" + percentage * 100 + "%");
        textView.setTextColor(Color.BLACK);

        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = (float) percentage;

        valueBar.setLayoutParams(layoutParams);
        setBackgroundColor(Color.GRAY);

        if (percentage >= 0.5) {
            valueBar.setBackgroundColor(Color.GREEN);
        }
        else if(percentage >= 0.2) {
            valueBar.setBackgroundColor(Color.YELLOW);
        }
        else {
            valueBar.setBackgroundColor(Color.RED);
        }

    }


}