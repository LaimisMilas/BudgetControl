package nick.miros.BudgetControl.budgetcontrol.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;

/**
 * Created by admin on 7/19/2014.
 */
public class Balance {

    private static double balance = 0;
    private static double expensesSoFar = 0;
    private static double accumulatedMoney = 0;
    private static ExpensesDataSource datasource;
    private static final String BALANCED_DAILY_BUDGET_KEY = "balancedDailyBudgetKey";
    private static final String BALANCED_DAILY_BUDGET_MONTH_KEY = "balancedDailyBudgetMonthKey";
    private static SharedPreferences settings;
    private static final String MY_PREFS_KEY = "myPrefsKey";


    public static double getBalance(Context context) {
        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);

        int monthUpdated = 0;

        if (settings.contains(BALANCED_DAILY_BUDGET_MONTH_KEY)) {
            monthUpdated = settings.getInt(BALANCED_DAILY_BUDGET_MONTH_KEY, 0);
        }

        datasource = new ExpensesDataSource(context);
        datasource.open();
        Calendar c = Calendar.getInstance();

        if (monthUpdated == c.get(Calendar.MONTH)) {
            return settings.getFloat(BALANCED_DAILY_BUDGET_KEY, 0);
        }
        else {
            int dateBudgetWasSet = Budget.getBudgetSettingDate(context);

            accumulatedMoney = (c.get(Calendar.DAY_OF_MONTH) + 1 - dateBudgetWasSet) * Budget.getDailyBudget(context);
            expensesSoFar = datasource.getAllMonthlyExpenses();
            balance = accumulatedMoney - expensesSoFar;
            return balance;
        }
    }
}
