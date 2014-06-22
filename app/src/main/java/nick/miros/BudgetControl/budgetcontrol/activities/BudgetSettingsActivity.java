/*
* This is an Activity where the user will be able to set
* their monthly budget and the daily budget will be recalculated
* appropriately.
 */

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

import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.helper.DecimalDigits;


public class BudgetSettingsActivity extends Activity {

    public static final String[] monthNames = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November",
            "December"};
    private final Context context = this;
    private TextView currentMonth;
    private ImageButton BudgetEditButton;
    private ImageButton CurrencyEditButton;
    private TextView MonthlyBudget;
    private TextView dailyBudget;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_settings);

        currentMonth = (TextView) findViewById(R.id.currentMonth);
        dailyBudget = (TextView) findViewById(R.id.dailyBudgetAmount);

        Calendar cal = Calendar.getInstance();
        final int month = cal.get(Calendar.MONTH); //current calendar month

        final int amountOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //amount of days in the current month

        currentMonth.setText("Budget for " + monthNames[month]);

        BudgetEditButton = (ImageButton) findViewById(R.id.EditButton);
        MonthlyBudget = (TextView) findViewById(R.id.currentMonthBudget);

        final SharedPreferences sharedPreferences = getSharedPreferences("Test", Context.MODE_PRIVATE);

        if (sharedPreferences.contains(monthNames[month])) {
            MonthlyBudget.setText(sharedPreferences.getString(monthNames[month], ""));

            DecimalFormat numberFormat = new DecimalFormat("#.00");

            //formats and sets the text for the dailyBudget textview
            dailyBudget.setText(" " + (numberFormat.format(Double.parseDouble(sharedPreferences.getString(monthNames[month], "")) / amountOfDays)));
        }

        BudgetEditButton.setOnClickListener(new View.OnClickListener() {

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

                                //checks whether the input is just a dot
                                if (DecimalDigits.isValidInput(userInput)) {


                                    MonthlyBudget.setText(userInput.getText());

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(monthNames[month], userInput.getText().toString());
                                    editor.commit();

                                    DecimalFormat numberFormat = new DecimalFormat("#.00");

                                    //formats and sets the text for the dailyBudget textview
                                    dailyBudget.setText(" " + (numberFormat.format(Double.parseDouble(userInput.getText().toString()) / amountOfDays)));
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                });

                alertDialog.show();

            }
        });

        CurrencyEditButton = (ImageButton) findViewById(R.id.EditCurrencyButton);
        CurrencyEditButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CurrencyListActivity.class));
            }
        });
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
