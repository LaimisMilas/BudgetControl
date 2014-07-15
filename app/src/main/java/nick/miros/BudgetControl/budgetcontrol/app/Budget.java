package nick.miros.BudgetControl.budgetcontrol.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by admin on 7/5/2014.
 */
public class Budget {

    private static SharedPreferences settings;
    private static final String MY_PREFS_KEY = "myPrefsKey";
    private static final String CURRENT_MONTHLY_BUDGET_KEY = "currentMonthlyBudgetKey";
    private static final String DATE_BUDGET_WAS_SET_KEY = "dateBudgetWasSetKey";

    public static double getCurrentMonthlyBudget(Context context) {
        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);
        return settings.getFloat(CURRENT_MONTHLY_BUDGET_KEY, 0);
    }

    /**
     * Saves the monthly budget value passed
     *
     * @param currentMonthlyBudget value to be set as a monthly budget
     * @param context              context of the application that is calling the method
     */
    public static void setCurrentMonthlyBudget(double currentMonthlyBudget, Context context) {
        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(CURRENT_MONTHLY_BUDGET_KEY, (float) currentMonthlyBudget);

        Calendar c = Calendar.getInstance();
        String budgetSetDate = c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + "";
        //editor.putInt(DATE_BUDGET_WAS_SET_KEY, c.get(Calendar.DAY_OF_MONTH));
        editor.putString(DATE_BUDGET_WAS_SET_KEY, budgetSetDate);

        editor.commit();
    }

    public static double getDailyBudget(Context context) {
        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);
        double monthlyBudget = settings.getFloat(CURRENT_MONTHLY_BUDGET_KEY, 0);

        DecimalFormat nf = new DecimalFormat("#.00");

        Calendar c = Calendar.getInstance();
        final int amountOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        double dailyBudget = Double.parseDouble(nf.format(monthlyBudget / amountOfDays));

        return dailyBudget;
    }

    public static int getBudgetSettingDate(Context context) {

        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);
        return settings.getInt(DATE_BUDGET_WAS_SET_KEY, 1);

    }

    public static void resetBudgetSettingDate(Context context) {
        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(DATE_BUDGET_WAS_SET_KEY, 1);

        editor.commit();

    }
}
