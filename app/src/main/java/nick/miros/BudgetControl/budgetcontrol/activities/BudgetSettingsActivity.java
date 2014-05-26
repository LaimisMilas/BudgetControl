package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.helper.DecimalDigits;


public class BudgetSettingsActivity extends ActionBarActivity {

    TextView currentMonth;
    final Context context = this;
    private ImageButton BudgetEditButton;
    private TextView MonthlyBudget;
    private TextView dailyBudget;

    String[] monthNames = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November",
            "December"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_settings);

        currentMonth = (TextView) findViewById(R.id.currentMonth);
        dailyBudget = (TextView) findViewById(R.id.dailyBudgetAmount);

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        final int amountOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        currentMonth.setText("Budget for " + monthNames[month]);


        BudgetEditButton = (ImageButton) findViewById(R.id.EditButton);
        MonthlyBudget = (TextView) findViewById(R.id.currentMonthBudget);


        // add button listener
        BudgetEditButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // get prompts.xml view
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

                        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                if (new DecimalDigits().isValidInput(userInput)) {

                                    String input = userInput.getText().toString();

                                    if (input.substring(input.length()-1, input.length()).equals(".")) {
                                        MonthlyBudget.setText(input.substring(0, input.length() - 1));
                                    }
                                    else {
                                        MonthlyBudget.setText(userInput.getText());
                                    }

                                    DecimalFormat numberFormat = new DecimalFormat("#.00");
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
