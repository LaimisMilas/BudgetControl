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
    private static final String currencyPrefs = "currenciesKey";
    private static final String currentBudgetKey = "currentBudgetKey";
    Context context;

    {
        settings = context.getSharedPreferences(currencyPrefs, Context.MODE_PRIVATE);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static double getCurrentMonthlyBudget() {

        return settings.getFloat(currentBudgetKey, 0);
    }

    public static void setCurrentMonthlyBudget(double currentMonthlyBudget) {

        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(currentBudgetKey, (float) currentMonthlyBudget);
        editor.commit();

        Budget.currentMonthlyBudget = currentMonthlyBudget;
    }
}
