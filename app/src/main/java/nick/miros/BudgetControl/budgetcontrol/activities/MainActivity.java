package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

import nick.miros.BudgetControl.budgetcontrol.app.Balance;
import nick.miros.BudgetControl.budgetcontrol.app.Budget;
import nick.miros.BudgetControl.budgetcontrol.app.Currency;
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


    private TextView balanceView;
    private Button ExpenseDirectionButton;
    private Button DataDirectionButton;
    private Button BudgetDirectionButton;
    private ImageButton balanceOverdraftButton;
    private ImageButton balanceInfoButton;
    private ImageButton monthlyBudgetOverdraftButton;
    private MyProgressBar monthlyProgressBar;
    private MyProgressBar dailyProgressBar;
    private ExpensesDataSource datasource;
    private final String MY_PREFS_KEY = "myPrefsKey";
    private static final String CURRENT_MONTHLY_BUDGET_KEY = "currentMonthlyBudgetKey";
    private static final String CURRENCY_SYMBOL_KEY = "currencySymbolKey";
    private static final String MONTH_BUDGET_WAS_SET_KEY = "monthBudgetWasSetKey";
    private static final String YEAR_BUDGET_WAS_SET_KEY = "yearBudgetWasSetKey";
    private static final String MONTH_BUDGET_DECISION_WAS_MADE_KEY = "monthBudgetDecisionWasMadeKey";
    private SharedPreferences settings;
    private double monthlyBudget = 0;
    private double dailyBudget = 0;
    private double balance = 0;
    private Calendar c = Calendar.getInstance();
    private final Context context = this;
    private DecimalFormat nf = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);

        //if there was no currency chosen before - start this prompt first
        if (!settings.contains(CURRENCY_SYMBOL_KEY)) {

            Intent intent = new Intent(getApplicationContext(), CurrencyListActivity.class);
            startActivity(intent);
        }

        //if there was no budget before - start a normal budget prompt
        if (!settings.contains(CURRENT_MONTHLY_BUDGET_KEY)) {
            startNormalBudgetPrompt();

            //in case there was a budget before, but it was set in the previous month
            //start a prompt asking for a decision whether the user wants to
            //change it or keep the old one
        } else if ((settings.getInt(MONTH_BUDGET_WAS_SET_KEY, 1) < c.get(Calendar.MONTH)
                || settings.getInt(YEAR_BUDGET_WAS_SET_KEY, 1) < c.get(Calendar.YEAR))

                //check whether the user has already made a decision in this prompt
                && (settings.getInt(MONTH_BUDGET_DECISION_WAS_MADE_KEY, 1) != c.get(Calendar.MONTH))) {
            startDecisionPrompt();
        }

        //set the onClickListener to the balanceOverDraftButton
        balanceOverdraftButton = (ImageButton) findViewById(R.id.balanceOverdraftIcon);
        balanceOverdraftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startRecountBudgetPrompt();
            }
        });

        //set the onClickListener to the monthlyBudgetOverdraftButton
        monthlyBudgetOverdraftButton = (ImageButton) findViewById(R.id.monthlyBudgetOverdraftIcon);
        monthlyBudgetOverdraftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startRaiseMonthlyBudgetPrompt();
            }
        });
        balanceInfoButton = (ImageButton) findViewById(R.id.balanceInfoIcon);
        balanceInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startBalanceInfoAlert();

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
        dailyProgressBar = (MyProgressBar) findViewById(R.id.dailyProgressBar);
        monthlyProgressBar = (MyProgressBar) findViewById(R.id.monthlyProgressBar);
        monthlyBudgetOverdraftButton.setVisibility(View.GONE);

        double spentToday = datasource.getAllTodayExpenses();
        double spentThisMonth = datasource.getAllMonthlyExpenses();

        monthlyBudget = Budget.getCurrentMonthlyBudget(getApplicationContext());
        monthlyProgressBar.setMax(monthlyBudget);

        dailyBudget = Budget.getDailyBudget(getApplicationContext());
        dailyProgressBar.setMax(dailyBudget);

        if (spentThisMonth != 0) {
            monthlyProgressBar.updateProgress(spentThisMonth);

            //in case the user overdrafts their monthly budget
            //show them the monthlyBudgetOverdraftButton
            if (spentThisMonth > monthlyBudget) {
                monthlyBudgetOverdraftButton.setVisibility(View.VISIBLE);
            }
        }
        if (spentToday != 0) {
            dailyProgressBar.updateProgress(spentToday);
        }

    }

    public void updateBalance() {
        balanceView = (TextView) findViewById(R.id.balance);
        balanceOverdraftButton.setVisibility(View.GONE);

        balance = Balance.getBalance(getApplicationContext());
        balanceView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext())
                + nf.format(balance)
                + "");

        //don't show the button in case the balance is positive
        if (balance >= 0) {
            balanceOverdraftButton.setVisibility(View.GONE);
        }
        //show the overdraft button but only in case all of the monthly expenses
        //are not bigger than the total monthly budget
        else if (Budget.getCurrentMonthlyBudget(getApplicationContext()) > datasource.getAllMonthlyExpenses()) {
            balanceOverdraftButton.setVisibility(View.VISIBLE);
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
                        if (userInput.getText().toString().length() > 2) {
                            if (Double.parseDouble(userInput.getText().toString()) > 100) {
                                //save the monthly budget value
                                monthlyBudget = Double.parseDouble(userInput.getText().toString());
                                Budget.setCurrentMonthlyBudget(monthlyBudget, getApplicationContext());

                                alertDialog.dismiss();

                                updateBalance();
                                updateProgressBars();

                            }
                        }

                        userInput.setError("Your budget should be at least "
                                               + Currency.getCurrentCurrencyUsed(getApplicationContext())
                                               + "100");

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
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
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

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(MONTH_BUDGET_DECISION_WAS_MADE_KEY, c.get(Calendar.MONTH));
        editor.commit();
    }

    public void startBalanceInfoAlert() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.alert_balance_info, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(promptsView)
                .setPositiveButton("Ah! Now I get it!", null)
                .create();

        double dailyBudget = Budget.getDailyBudget(getApplicationContext());
        double monthlyExpenses = datasource.getAllMonthlyExpenses();
        double currentBalance = Balance.getBalance(getApplicationContext());

        TextView explanationTextView = (TextView) promptsView.findViewById(R.id.explanationText);
        explanationTextView.setText("The balance shows whether you're on track with your budget or not. Positive balance means you are saving money, negative means that you are losing money. The formula for the balance: " +
                                    + c.get(Calendar.DAY_OF_MONTH) + " (days passed since the start of the month) * "
                                    + Currency.getCurrentCurrencyUsed(getApplicationContext())
                                    + dailyBudget + " (your daily budget) - "
                                    + Currency.getCurrentCurrencyUsed(getApplicationContext())
                                    + monthlyExpenses + " (all your expenses for the month) = "
                                    + Currency.getCurrentCurrencyUsed(getApplicationContext())
                                    + nf.format(currentBalance));



        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
            }
        });
        alertDialog.show();
    }

    public void startRaiseMonthlyBudgetPrompt() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.alert_monthly_budget_overdraft, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(promptsView)
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .setCancelable(false)
                .create();

        TextView textView = (TextView) promptsView.findViewById(R.id.monthlyBudgetOverdraftText);
        textView.setText("Looks like your budget of "
                + Currency.getCurrentCurrencyUsed(getApplicationContext())
                + Budget.getCurrentMonthlyBudget(getApplicationContext())
                + " doesn't seem to fit your needs. Your budget should be at least "
                + datasource.getAllMonthlyExpenses()
                + " + the amount you need for the rest of the month. Do you wish to raise your monthly budget?");
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
