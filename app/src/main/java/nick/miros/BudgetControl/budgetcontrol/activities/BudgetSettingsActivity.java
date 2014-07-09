

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
 * The Activity also contains a button that sends the user
 * to another activity where the user chooses their preferred
 * currency.
 */

public class BudgetSettingsActivity extends Activity {

    public static final String[] monthNames = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November",
            "December"};
    private final String currencyPrefs = "currenciesKey";
    private final String currencySymbolKey = "currencySymbolKey";
    private final String currencyNameKey = "currencyNameKey";
    private final String nameIdentifier = "currencyName";
    private final String symbolIdentifier = "currencySymbol";
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

        settings = getSharedPreferences(currencyPrefs, Context.MODE_PRIVATE);

        currentMonthView = (TextView) findViewById(R.id.currentMonth);
        dailyBudgetView = (TextView) findViewById(R.id.dailyBudgetAmount);
        monthlyBudgetView = (TextView) findViewById(R.id.currentMonthBudget);
        budgetEditButton = (ImageButton) findViewById(R.id.EditButton);

        Calendar cal = Calendar.getInstance();
        final int month = cal.get(Calendar.MONTH); //current calendar month
        final int amountOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //amount of days in the current month
        currentMonthView.setText("Budget for " + monthNames[month]);

        //check if shared preferences contain values for the current month
        if (settings.contains(monthNames[month])) {
            //set chosen currency and budget for the month
            monthlyBudgetView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext()) +
                    settings.getString(monthNames[month], ""));

            DecimalFormat numberFormat = new DecimalFormat("#.00");

            //calculate the daily budget and set it to the dailyBudgetView Textview
            dailyBudgetView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext())
                    + (numberFormat.format(Double.parseDouble(settings.getString(monthNames[month], ""))
                    / amountOfDays)));
        }

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
                                //check if the input has something
                                if (userInput.getText().toString().length() > 0) {
                                    //checks whether the input is just a dot
                                    if (DecimalDigits.isValidInput(userInput)) {

                                        //apply the monthly budget to the monthly view
                                        monthlyBudgetView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext())
                                                + userInput.getText());

                                        //save the monthly budget value
                                        monthlyBudget = Double.parseDouble(userInput.getText().toString());
                                        Budget.setCurrentMonthlyBudget(monthlyBudget, getApplicationContext());

                                        //formats and sets the text for the dailyBudgetView textview
                                        DecimalFormat numberFormat = new DecimalFormat("#.00");
                                        dailyBudgetView.setText(Currency.getCurrentCurrencyUsed(getApplicationContext())
                                                + (numberFormat.format(Double.parseDouble(userInput.getText().toString())
                                                / amountOfDays)));
                                        alertDialog.dismiss();
                                    }
                                }
                            }
                        });
                    }
                });

                alertDialog.show();

            }
        });

        currencyEditButton = (ImageButton) findViewById(R.id.EditCurrencyButton);
        currencyEditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //start the CurrencyListActivity
                Intent intent = new Intent(v.getContext(), CurrencyListActivity.class);
                startActivityForResult(intent, currencyRequestCode);
            }
        });


        currentCurrencyView = (TextView) findViewById(R.id.currentCurrency);

        //check whether the preferences contain the values for the
        //currency symbol and name. If so, apply them.
        if (settings.contains(currencySymbolKey) && settings.contains(currencyNameKey)) {

            currentCurrencyView.setText(settings.getString(currencyNameKey, "")
                    + " "
                    + settings.getString(currencySymbolKey, ""));
        }
    }

    // Call Back method  to get the currency from other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {

            //receive the info sent from the CurrencyListActivity
            if (requestCode == currencyRequestCode) {

                //get the key-value pairs passed
                String currencyName = data.getStringExtra(nameIdentifier);
                String currencySymbol = data.getStringExtra(symbolIdentifier);

                //save the values received into sharedPreferences
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(currencySymbolKey, currencySymbol);
                editor.putString(currencyNameKey, currencyName);
                editor.commit();

                currentCurrencyView.setText(currencyName + " " + currencySymbol);

                //call the onCreate method again in order to refresh the Activity
                //so that the chosen currency could be applied to other views
                Bundle tempBundle = new Bundle();
                onCreate(tempBundle);
            }

        }
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
