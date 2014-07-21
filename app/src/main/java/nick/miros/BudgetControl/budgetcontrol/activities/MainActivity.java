package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import nick.miros.BudgetControl.budgetcontrol.app.Balance;
import nick.miros.BudgetControl.budgetcontrol.app.Budget;
import nick.miros.BudgetControl.budgetcontrol.app.Currency;
import nick.miros.BudgetControl.budgetcontrol.app.Expense;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;
import nick.miros.BudgetControl.budgetcontrol.helper.DecimalDigits;
import nick.miros.BudgetControl.budgetcontrol.helper.MyProgressBar;

public class MainActivity extends ActionBarActivity {
    View.OnClickListener mainActivityListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ExpenseDirectionButton:
                    startActivity(new Intent(v.getContext(), AddExpenseActivity.class));
                    break;
                case R.id.DataDirectionButton:
                    startActivity(new Intent(v.getContext(), ExpandableExpenseActivity.class));
                    break;
                case R.id.BudgetDirectionButton:
                    startActivity(new Intent(v.getContext(), BudgetSettingsActivity.class));
                    break;
            }
        }
    };

    private double balance = 0;
    private double expensesSoFar = 0;
    private double accumulatedMoney = 0;
    private TextView balanceTextView;
    private TextView balanceView;
    private Button ExpenseDirectionButton;
    private Button DataDirectionButton;
    private Button BudgetDirectionButton;
    private ImageButton overdraftWarningButton;
    private MyProgressBar monthlyProgress;
    private MyProgressBar dailyProgress;
    private ExpensesDataSource datasource;
    private final String MY_PREFS_KEY = "myPrefsKey";
    private static final String CURRENT_MONTHLY_BUDGET_KEY = "currentMonthlyBudgetKey";
    private static final String INITIAL_BUDGET_SET_DATE_KEY = "initialBudgetSetDateKey";
    private static final String CURRENCY_SYMBOL_KEY = "currencySymbolKey";
    private static final String DATE_BUDGET_WAS_SET_KEY = "dateBudgetWasSetKey";
    private static final String MONTH_BUDGET_WAS_SET_KEY = "monthBudgetWasSetKey";
    private static final String YEAR_BUDGET_WAS_SET_KEY = "yearBudgetWasSetKey";
    private static final String BALANCED_DAILY_BUDGET_KEY = "balancedDailyBudgetKey";
    private static final String BALANCED_DAILY_BUDGET_MONTH_KEY = "balancedDailyBudgetMonthKey";
    private SharedPreferences settings;
    private double monthlyBudget = 0;
    private double dailyBudget = 0;
    private Calendar c = Calendar.getInstance();
    private final Context context = this;
    DecimalFormat nf = new DecimalFormat("#.00");

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<Expense> allExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);

        //if there was no budget before - start a normal budget prompt
        if (!settings.contains(CURRENT_MONTHLY_BUDGET_KEY)) {
            startNormalBudgetPrompt();

        //in case there was a budget before, but it was set in the previous month
        //start a prompt asking for a decision whether the user wants to
        //change it or keep the old one
        } else if ((settings.getInt(MONTH_BUDGET_WAS_SET_KEY, 1) > c.get(Calendar.MONTH)
                || settings.getInt(YEAR_BUDGET_WAS_SET_KEY, 1) > c.get(Calendar.YEAR))) {
            startDecisionPrompt();
        }

        overdraftWarningButton = (ImageButton) findViewById(R.id.overdraftWarningIcon);
        overdraftWarningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startRecountBudgetPrompt();

            }
        });


        ExpenseDirectionButton = (Button) findViewById(R.id.ExpenseDirectionButton);
        DataDirectionButton = (Button) findViewById(R.id.DataDirectionButton);
        BudgetDirectionButton = (Button) findViewById(R.id.BudgetDirectionButton);

        ExpenseDirectionButton.setOnClickListener(mainActivityListener);
        DataDirectionButton.setOnClickListener(mainActivityListener);
        BudgetDirectionButton.setOnClickListener(mainActivityListener);

        datasource = new ExpensesDataSource(this);
        datasource.open();

        updateBalance();
        updateProgressBars();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateBalance();
        updateProgressBars();

    }

    public void updateProgressBars() {
        dailyProgress = (MyProgressBar) findViewById(R.id.dailyProgressBar);
        monthlyProgress = (MyProgressBar) findViewById(R.id.monthlyProgressBar);

        double spentToday = datasource.getAllTodayExpenses();
        double spentThisMonth = datasource.getAllMonthlyExpenses();

        monthlyBudget = Budget.getCurrentMonthlyBudget(getApplicationContext());
        monthlyProgress.setMax(monthlyBudget);

        dailyBudget = Budget.getDailyBudget(getApplicationContext());
        dailyProgress.setMax(dailyBudget);

        if (spentThisMonth != 0) {

            monthlyProgress.updateProgress(spentThisMonth);
        }

        if (spentToday != 0) {
            dailyProgress.updateProgress(spentToday);
        }

    }

    public void updateBalance() {

        balanceTextView = (TextView) findViewById(R.id.balanceText);
        balanceView = (TextView) findViewById(R.id.balance);

        if (datasource.getAllMonthlyExpenses() != 0) {

            balanceTextView.setVisibility(View.VISIBLE);
            balanceView.setVisibility(View.VISIBLE);

            balance = Balance.getBalance(getApplicationContext());

            balanceView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext())
                    + nf.format(Math.abs(balance))
                    + "");

            if (balance >= 0) {
                balanceTextView.setText("Saved this month:");
                overdraftWarningButton.setVisibility(View.GONE);
            } else {
                balanceTextView.setText("Overspent this month:");
                overdraftWarningButton.setVisibility(View.VISIBLE);
            }
        } else {
            balanceTextView.setVisibility(View.GONE);
            balanceView.setVisibility(View.GONE);
            overdraftWarningButton.setVisibility(View.GONE);
        }


    }

    public void startRecountBudgetPrompt() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.alert_recount_budget_prompt, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(promptsView)
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {

                        Budget.balanceDailyBudget(getApplicationContext());
                        alertDialog.dismiss();
                        updateBalance();
                        updateProgressBars();


                    }
                });

                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                negativeButton.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();

                    }
                });
            }
        });
        alertDialog.show();

    }

    public void startNormalBudgetPrompt() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.alert_budget_prompt, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(promptsView)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .create();

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        userInput.setFilters(new InputFilter[]{new DecimalDigits()});

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //check if the input has something
                        if (userInput.getText().toString().length() > 0) {
                            //checks whether the input is just a dot
                            if (DecimalDigits.isValidInput(userInput)) {

                                //save the monthly budget value
                                monthlyBudget = Double.parseDouble(userInput.getText().toString());
                                Budget.setCurrentMonthlyBudget(monthlyBudget, getApplicationContext());

                                SharedPreferences.Editor editor = settings.edit();
                                /*
                                editor.putString(INITIAL_BUDGET_SET_DATE_KEY, c.get(Calendar.YEAR)
                                        + c.get(Calendar.MONTH)
                                        + "");
                                        */

                                alertDialog.dismiss();

                                updateBalance();
                                updateProgressBars();

                                if (!settings.contains(CURRENCY_SYMBOL_KEY)) {

                                    Intent intent = new Intent(getApplicationContext(), CurrencyListActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });
            }
        });
        alertDialog.show();

    }

    public void startDecisionPrompt() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.alert_decision_prompt, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(promptsView)
                .setPositiveButton("Change the budget", null)
                .setNegativeButton("Keep the old budget", null)
                .setCancelable(false)
                .create();

        TextView textView = (TextView) promptsView.findViewById(R.id.budgetText);
        textView.setText("Your current monthly budget is: "
                + Currency.getCurrentCurrencyUsed(getApplicationContext())
                + Budget.getCurrentMonthlyBudget(getApplicationContext())
                + ". Do you wish to change it?");
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        startNormalBudgetPrompt();
                    }
                });

                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                negativeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Budget.resetBudgetSettingDate(getApplicationContext());
                    }
                });
            }
        });
        alertDialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
