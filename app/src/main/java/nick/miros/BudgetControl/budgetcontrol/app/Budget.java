package nick.miros.BudgetControl.budgetcontrol.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DecimalFormat;
import java.util.Calendar;

import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;

/**
 * Class for setting and getting the daily and monthly budgets.
 * The Class also contains a method for balancing the daily budget, so that the user
 * doesn't overdraft the monthly budget in the long run.
 * The Class has a method that resets the monthly budget setting date.
 *
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
    private static final String YEAR_DAILY_BUDGET_WAS_BALANCED_KEY = "yearDailyBudgetWasBalancedKey";
    private static final String MONTH_DAILY_BUDGET_WAS_BALANCED_KEY = "monthDailyBudgetWasBalancedKey";
    private static final String DAY_DAILY_BUDGET_WAS_BALANCED_KEY = "dayDailyBudgetWasBalancedKey";
    private static final String TIME_STAMP_DAILY_BUDGET_BALANCED_KEY = "timeStampDailyBudgetBalancedKey";
    private static final String TIME_STAMP_MONTHLY_BUDGET_SET_KEY = "timeStampMonthlyBudgetSetKey";
    private static DecimalFormat nf = new DecimalFormat("0.00");

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
        editor.putLong(TIME_STAMP_MONTHLY_BUDGET_SET_KEY, c.getTimeInMillis() / 1000);

        editor.commit();
    }

    /**
     * Returns the daily budget. The daily budget might be balanced or not depending on the values
     * stored in the keys.
     * @param context current Application context
     * @return current balance
     */
    public static double getDailyBudget(Context context) {
        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);

        Calendar c = Calendar.getInstance();
        double monthlyBudget = settings.getFloat(CURRENT_MONTHLY_BUDGET_KEY, 0);

        int monthBalanceUpdated = settings.getInt(MONTH_DAILY_BUDGET_WAS_BALANCED_KEY, 0);
        int yearBalanceUpdated = settings.getInt(YEAR_DAILY_BUDGET_WAS_BALANCED_KEY, 0);

        //ensure that the balanced daily budget from the previous month
        //doesn't get transferred to the next month
        if (monthBalanceUpdated == c.get(Calendar.MONTH) && (yearBalanceUpdated == c.get(Calendar.YEAR))
                && (settings.getLong(TIME_STAMP_DAILY_BUDGET_BALANCED_KEY, 0) > settings.getLong(TIME_STAMP_MONTHLY_BUDGET_SET_KEY, 0))
                ) {
            return Double.parseDouble(nf.format(settings.getFloat(BALANCED_DAILY_BUDGET_KEY, 0)));
        }
        //if there was no balancing of the budget this month
        //get the normal daily budget
        else {
            final int amountOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            double dailyBudget = Double.parseDouble(nf.format(monthlyBudget / amountOfDays));
            return dailyBudget;
        }
    }

    /**
     * Makes the daily budget equal to the sum that will not overdraft the monthly budget at the
     * end of the month if the user continues with such daily budget till the end of the month.
     */
    public static void balanceDailyBudget(Context context) {

        datasource = new ExpensesDataSource(context);
        datasource.open();

        Calendar c = Calendar.getInstance();

        int amountOfDaysLeft = c.getActualMaximum(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_MONTH);

        double balancedDailyBudget = Double.parseDouble(
                nf.format((Budget.getCurrentMonthlyBudget(context)
                        - datasource.getAllMonthlyExpenses())
                        / amountOfDaysLeft)
        );

        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(BALANCED_DAILY_BUDGET_KEY, (float) balancedDailyBudget);

        final int currentYear = c.get(Calendar.YEAR);
        final int currentMonth = c.get(Calendar.MONTH);
        final int currentDay = c.get(Calendar.DAY_OF_MONTH);
        final long currentSecond = c.getTimeInMillis() / 1000;
        editor.putInt(YEAR_DAILY_BUDGET_WAS_BALANCED_KEY, currentYear);
        editor.putInt(MONTH_DAILY_BUDGET_WAS_BALANCED_KEY, currentMonth);
        editor.putInt(DAY_DAILY_BUDGET_WAS_BALANCED_KEY, currentDay);
        editor.putLong(TIME_STAMP_DAILY_BUDGET_BALANCED_KEY, currentSecond);
        editor.commit();
    }

    /**
     * Makes the budget setting date the first day of the month by default
     */
    public static void resetBudgetSettingDate(Context context) {
        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(DATE_BUDGET_WAS_SET_KEY, 1);

        editor.commit();

    }
}
