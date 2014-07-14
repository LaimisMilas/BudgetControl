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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

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
                    Toast.makeText(getApplicationContext(), "dataButton", Toast.LENGTH_SHORT).show();
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
    private MyProgressBar monthlyProgress;
    private MyProgressBar dailyProgress;
    private ExpensesDataSource datasource;
    private final String MY_PREFS_KEY = "myPrefsKey";
    private static final String CURRENT_MONTHLY_BUDGET_KEY = "currentMonthlyBudgetKey";
    private SharedPreferences settings;
    private double monthlyBudget = 0;
    private double dailyBudget = 0;
    private Calendar c = Calendar.getInstance();
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);

        if (!settings.contains(CURRENT_MONTHLY_BUDGET_KEY)) {
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

                                    alertDialog.dismiss();

                                    updateBalance();
                                    updateProgressBars();

                                    Intent intent = new Intent(getApplicationContext(), CurrencyListActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                }
            });
            alertDialog.show();

        }

        if (c.get(Calendar.DAY_OF_MONTH) == 1) {
            Budget.resetBudgetSettingDate(getApplicationContext());
        }

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
        updateProgressBars();
        updateBalance();


    }

    public void updateProgressBars() {
        dailyProgress = (MyProgressBar) findViewById(R.id.dailyProgressBar);
        monthlyProgress = (MyProgressBar) findViewById(R.id.monthlyProgressBar);

        double spentToday = datasource.getAllTodayExpenses();
        double spentThisMonth = datasource.getAllMonthlyExpenses();

        if (settings.contains(CURRENT_MONTHLY_BUDGET_KEY)) {

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

    }

    public void updateBalance() {

        balanceTextView = (TextView) findViewById(R.id.balanceText);
        balanceView = (TextView) findViewById(R.id.balance);

        if (settings.contains(CURRENT_MONTHLY_BUDGET_KEY) && datasource.getAllMonthlyExpenses() != 0) {

            int dateBudgetWasSet = Budget.getBudgetSettingDate(getApplicationContext());

            balanceTextView.setVisibility(View.VISIBLE);
            balanceView.setVisibility(View.VISIBLE);
            Calendar cal = Calendar.getInstance();
            accumulatedMoney = (cal.get(Calendar.DAY_OF_MONTH) + 1 - dateBudgetWasSet) * Budget.getDailyBudget(getApplicationContext());
            expensesSoFar = datasource.getExpensesStartingFrom(dateBudgetWasSet);
            balance = accumulatedMoney - expensesSoFar;

            balanceView.setText(balance + "");

            if (balance >= 0) {
                balanceTextView.setText("Money saved this month:");
            } else {
                balanceTextView.setText("Overdraft size:");
            }
        } else {
            balanceTextView.setVisibility(View.GONE);
            balanceView.setVisibility(View.GONE);
        }


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
