

package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import nick.miros.BudgetControl.budgetcontrol.app.Budget;
import nick.miros.BudgetControl.budgetcontrol.app.Currency;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.helper.DecimalDigits;

/**
 * This is an Activity where the user will be able to set
 * their monthly budget via an AlertDialog and the daily
 * budget will be recalculated appropriately.
 * <p/>
 * The Activity also has a control for setting the currency
 * that will be used throughout the whole application
 * <p/>
 * The Activity also contains a button that sends the user
 * to another activity where the user chooses their preferred
 * currency.
 */

public class BudgetSettingsActivity extends Activity {

    public static final String[] monthNames = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November",
            "December"};
    private final String MY_PREFS_KEY = "myPrefsKey";
    private final String CURRENCY_SYMBOL_KEY = "currencySymbolKey";
    private final String CURRENCY_NAME_KEY = "currencyNameKey";
    private static final String CURRENT_MONTHLY_BUDGET_KEY = "currentMonthlyBudgetKey";
    private final String NAME_ID = "currencyName";
    private final String SYMBOL_ID = "currencySymbol";
    private final Context context = this;
    private TextView currentMonthView;
    private ImageButton budgetEditButton;
    private ImageButton currencyEditButton;
    private TextView monthlyBudgetView;
    private TextView dailyBudgetView;
    private TextView currentCurrencyView;
    private double monthlyBudget;
    private SharedPreferences settings;
    private final int currencyRequestCode = 1;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_settings);

        settings = getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);

        currentMonthView = (TextView) findViewById(R.id.currentMonth);
        dailyBudgetView = (TextView) findViewById(R.id.dailyBudgetAmount);
        monthlyBudgetView = (TextView) findViewById(R.id.currentMonthBudget);
        budgetEditButton = (ImageButton) findViewById(R.id.EditButton);

        Calendar cal = Calendar.getInstance();
        final int month = cal.get(Calendar.MONTH); //current calendar month
        final int amountOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //amount of days in the current month
        currentMonthView.setText("Budget for " + monthNames[month]);

        //set chosen currency and budget for the month
        monthlyBudgetView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext()) +
                settings.getFloat(CURRENT_MONTHLY_BUDGET_KEY, 0));

        //calculate the daily budget and set it to the dailyBudgetView Textview
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        dailyBudgetView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext())
                + (numberFormat.format(settings.getFloat(CURRENT_MONTHLY_BUDGET_KEY, 0)
                / amountOfDays)));

        budgetEditButton.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View arg0) {

                                                    LayoutInflater li = LayoutInflater.from(context);
                                                    View promptsView = li.inflate(R.layout.alert_budget_prompt, null);

                                                    final AlertDialog alertDialog = new AlertDialog.Builder(context)
                                                            .setView(promptsView)
                                                            .setPositiveButton("OK", null)
                                                            .setNegativeButton("Cancel", null)
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
                                                                                                                                    if (userInput.getText().toString().length() > 2) {
                                                                                                                                        if (Double.parseDouble(userInput.getText().toString()) > 100) {
                                                                                                                                            //apply the monthly budget to the monthly view
                                                                                                                                            monthlyBudgetView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext())
                                                                                                                                                    + userInput.getText());

                                                                                                                                            //save the monthly budget value
                                                                                                                                            monthlyBudget = Double.parseDouble(userInput.getText().toString());
                                                                                                                                            Budget.setCurrentMonthlyBudget(monthlyBudget, getApplicationContext());

                                                                                                                                            //sets the text for the dailyBudgetView textview
                                                                                                                                            dailyBudgetView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext())
                                                                                                                                                    + Budget.getDailyBudget(getApplicationContext()) + "");
                                                                                                                                            alertDialog.dismiss();
                                                                                                                                        }
                                                                                                                                    }

                                                                                                                                    userInput.setError("Your budget should be at least "
                                                                                                                                            + Currency.getCurrentCurrencyUsed(getApplicationContext())
                                                                                                                                            + "100");
                                                                                                                                }
                                                                                                                            }
                                                                                          );
                                                                                      }
                                                                                  }
                                                    );

                                                    alertDialog.show();

                                                }
                                            }
        );

        currencyEditButton = (ImageButton)

                findViewById(R.id.EditCurrencyButton);

        currencyEditButton.setOnClickListener(new View.OnClickListener()

                                              {
                                                  public void onClick(View v) {

                                                      //start the CurrencyListActivity
                                                      Intent intent = new Intent(v.getContext(), CurrencyListActivity.class);
                                                      //startActivityForResult(intent, currencyRequestCode);
                                                      startActivity(intent);
                                                  }
                                              }
        );

        currentCurrencyView = (TextView)

                findViewById(R.id.currentCurrency);

        currentCurrencyView.setText(settings.getString(CURRENCY_NAME_KEY, "")
                + " "
                + settings.getString(CURRENCY_SYMBOL_KEY, ""));
    }

    @Override
    public void onResume() {
        super.onResume();

        currentCurrencyView.setText(settings.getString(CURRENCY_NAME_KEY, "")
                + " "
                + settings.getString(CURRENCY_SYMBOL_KEY, ""));

        //apply the monthly budget to the monthly view
        monthlyBudgetView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext())
                + Budget.getCurrentMonthlyBudget(getApplicationContext()));

        //sets the text for the dailyBudgetView textview
        dailyBudgetView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext())
                + Budget.getDailyBudget(getApplicationContext()) + "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.budget_settings, menu);
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
