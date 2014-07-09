package nick.miros.BudgetControl.budgetcontrol.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 7/5/2014.
 */
public class Budget {


    public static double currentMonthlyBudget;
    private double amount;
    private static SharedPreferences settings;
    private static final String MY_PREFS_KEY = "myPrefsKey";
    private static final String CURRENT_BUDGET_KEY = "currentBudgetKey";

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static double getCurrentMonthlyBudget() {

        return settings.getFloat(CURRENT_BUDGET_KEY, 0);
    }

    /**
     * Saves the monthly budget value passed
     *
     * @param currentMonthlyBudget value to be set as a monthly budget
     * @param context context of the application that is calling the method
     */
    public static void setCurrentMonthlyBudget(double currentMonthlyBudget, Context context) {
        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(CURRENT_BUDGET_KEY, (float) currentMonthlyBudget);
        editor.commit();

        Budget.currentMonthlyBudget = currentMonthlyBudget;
    }
}
