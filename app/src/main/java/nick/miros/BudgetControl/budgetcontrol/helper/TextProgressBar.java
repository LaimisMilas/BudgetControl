package nick.miros.BudgetControl.budgetcontrol.helper;

/**
 * Created by mymi on 02-Jul-14.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.text.DecimalFormat;

import nick.miros.BudgetControl.budgetcontrol.app.Currency;

public class TextProgressBar extends ProgressBar {

    private String text = "";
    private int textColor = Color.BLACK;
    private float textSize = 40;

    public TextProgressBar(Context context) {
        super(context);
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //create an instance of class Paint, set color and font size
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        //Drawable d = this.getProgressDrawable();
        //d.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        //In order to show text in a middle, we need to know its size
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        //Now we store font size in bounds variable and can calculate it's position
        int x = getWidth() / 2 - bounds.centerX();
        int y = getHeight() / 2 - bounds.centerY();
        //drawing text with appropriate color and size in the center
        canvas.drawText(text, x, y, textPaint);
    }

    public String getText() {
        return text;
    }

    public synchronized void setText(float progress, float max) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.text = Currency.getCurrentCurrencyUsed(getContext()) + df.format(progress)
                    + " / " + Currency.getCurrentCurrencyUsed(getContext()) + df.format(max);
    }

    public int getTextColor() {
        return textColor;
    }

    public synchronized void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public synchronized void setTextSize(float textSize) {
        this.textSize = textSize;
    }

}
