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
    private static SharedPreferences settings;
    private static final String MY_PREFS_KEY = "myPrefsKey";
    private static final String YEAR_DAILY_BUDGET_WAS_BALANCED_KEY = "yearDailyBudgetWasBalancedKey";
    private static final String MONTH_DAILY_BUDGET_WAS_BALANCED_KEY = "monthDailyBudgetWasBalancedKey";
    private static final String DAY_DAILY_BUDGET_WAS_BALANCED_KEY = "dayDailyBudgetWasBalancedKey";
    private static final String TIME_STAMP_DAILY_BUDGET_BALANCED_KEY = "timeStampDailyBudgetBalancedKey";


    public static double getBalance(Context context) {
        settings = context.getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);

        datasource = new ExpensesDataSource(context);
        datasource.open();
        Calendar c = Calendar.getInstance();


        //check whether the the daily budget was balanced this month
        if ((settings.getInt(MONTH_DAILY_BUDGET_WAS_BALANCED_KEY, 0) == c.get(Calendar.MONTH)) &&
                (settings.getInt(YEAR_DAILY_BUDGET_WAS_BALANCED_KEY, 0) == c.get(Calendar.YEAR))) {

            accumulatedMoney = (c.get(Calendar.DAY_OF_MONTH)
                    - settings.getInt(DAY_DAILY_BUDGET_WAS_BALANCED_KEY, 0))
                    * Budget.getDailyBudget(context);
            expensesSoFar = datasource.getExpensesFromTimeStamp(settings.getLong(TIME_STAMP_DAILY_BUDGET_BALANCED_KEY, 0));
            balance = accumulatedMoney - expensesSoFar;
            return balance;
        } else {

            accumulatedMoney = c.get(Calendar.DAY_OF_MONTH) * Budget.getDailyBudget(context);
            expensesSoFar = datasource.getAllMonthlyExpenses();
            balance = accumulatedMoney - expensesSoFar;
            return balance;
        }
    }
}
