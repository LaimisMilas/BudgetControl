package nick.miros.BudgetControl.budgetcontrol.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode;

import java.text.DecimalFormat;
import java.util.Calendar;

import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;

/**
 * Created by admin on 7/5/2014.
 */
public class Budget {

    private static SharedPreferences settings;
    private static ExpensesDataSource datasource;
    private static final String MY_PREFS_KEY = "myPrefsKey";
    private static final String CURRENT_MONTHLY_BUDGET_KEY = "currentMonthlyBudgetKey";
    private static final String DATE_BUDGET_WAS_SET_KEY = "dateBudgetWasSetKey";
    private static final String MONTH_BUDGET_WAS_SET_KEY = "monthBudgetWasSetKey";
    private static final String YEAR_BUDGET_WAS_SET_KEY = "yearBudgetWasSetKey";
    private static final String BALANCED_DAILY_BUDGET_KEY = "balancedDailyBudgetKey";
    private static final String BALANCED_DAILY_BUDGET_MONTH_KEY = "balancedDailyBudgetMonthKey";
    private static DecimalFormat nf = new DecimalFormat("#.00");

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
        editor.putInt(DATE_BUDGET_WAS_SET_KEY, c.get(Calendar.DAY_OF_MONTH));
        editor.putInt(MONTH_BUDGET_WAS_SET_KEY, c.get(Calendar.MONTH));
        editor.putInt(YEAR_BUDGET_WAS_SET_KEY, c.get(Calendar.YEAR));

        editor.commit();
    }

    public static double getDailyBudget(Context context) {
        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);

        Calendar c = Calendar.getInstance();
        double monthlyBudget = settings.getFloat(CURRENT_MONTHLY_BUDGET_KEY, 0);

        int monthUpdated = settings.getInt(BALANCED_DAILY_BUDGET_MONTH_KEY, 0);

        if (monthUpdated == c.get(Calendar.MONTH)) {
            return Double.parseDouble(nf.format(settings.getFloat(BALANCED_DAILY_BUDGET_KEY, 0)));
        }
        else {
            final int amountOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            double dailyBudget = Double.parseDouble(nf.format(monthlyBudget / amountOfDays));
            return dailyBudget;
        }
    }

    public static void balanceDailyBudget(Context context) {

        datasource = new ExpensesDataSource(context);
        datasource.open();

        Calendar c = Calendar.getInstance();

        int amountOfDaysLeft = c.getActualMaximum(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_MONTH);

        double balancedDailyBudget = Double.parseDouble(
                                     nf.format((Budget.getCurrentMonthlyBudget(context)
                                     - datasource.getAllMonthlyExpenses())
                                     / amountOfDaysLeft));

        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(BALANCED_DAILY_BUDGET_KEY, (float) balancedDailyBudget);

        final int currentMonth = c.get(Calendar.MONTH);
        editor.putInt(BALANCED_DAILY_BUDGET_MONTH_KEY, currentMonth);
        editor.commit();

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
