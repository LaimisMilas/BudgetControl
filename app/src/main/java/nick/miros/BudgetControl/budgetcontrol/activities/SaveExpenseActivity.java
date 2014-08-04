package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;
import nick.miros.BudgetControl.budgetcontrol.helper.DecimalDigits;

import java.util.Calendar;

/**
 * An Activity for saving expenses. The Expense can be freshly created or it can be an Expense that
 * was passed to be edited.
 * <p/>
 * In case the user comes from the MainActivity this Activity will create a new Expense. In case the
 * user comes from the ShowExpenseActivity this Activity will modify the passed Expense.
 */
public class SaveExpenseActivity extends Activity {

    private String description;
    private double amount;
    private TextView dateView;
    private TextView amountEntered;
    private TextView descriptionView;
    private ImageButton dateButton;
    private Button saveExpenseButton;
    private EditText amountView;
    private int activityComingFrom;
    private Expense chosenExpense;
    ExpensesDataSource dataSource;

    private static int chosenDay;
    private static int chosenMonth;
    private static int chosenYear;
    private static final String ACTIVITY_COMING_FROM_KEY = "activityComingFromKey";
    private static final int MAIN_ACTIVITY_KEY = 1;
    private static final int SHOW_EXPENSE_ACTIVITY_KEY = 2;
    private static final String EXPENSE_ID_KEY = "expenseIdKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_expense);

        dataSource = new ExpensesDataSource(this);
        dataSource.open();

        Intent receivingIntent = getIntent();
        //get the constant that stands for the Activity that the user is coming from
        activityComingFrom = receivingIntent.getIntExtra(ACTIVITY_COMING_FROM_KEY, 0);

        dateView = (TextView) findViewById(R.id.dateView);
        descriptionView = (TextView) findViewById(R.id.expense_description);
        amountView = (EditText) findViewById(R.id.expense_amount);

        dateButton = (ImageButton) findViewById(R.id.chooseDateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        amountEntered = (TextView) findViewById(R.id.expense_amount);
        amountEntered.setFilters(new InputFilter[]{new DecimalDigits()});

        saveExpenseButton = (Button) findViewById(R.id.saveExpense);
        saveExpenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveExpense(v);
            }
        });

        //in case the user enters this Activity from the MainActivity
        //and this is a fresh expense
        if (activityComingFrom == MAIN_ACTIVITY_KEY) {

            Calendar c = Calendar.getInstance();

            //give the current date by default
            chosenDay = c.get(Calendar.DATE);
            chosenMonth = c.get(Calendar.MONTH);
            chosenYear = c.get(Calendar.YEAR);

            //the default date that is set once the Activity is started
            dateView.setText((chosenMonth + 1) + " / " + chosenDay + " / " + chosenYear);

        }
        //in case the user enters the Activity from the ShowExpenseActivity
        //and the user chose an Expense beforehand that they want to edit
        else if (activityComingFrom == SHOW_EXPENSE_ACTIVITY_KEY) {

            //get the Expense the user chose
            chosenExpense = dataSource.getExpenseBasedOnId(receivingIntent.getLongExtra(EXPENSE_ID_KEY, 0));

            //set the date, amount and description to the values of the Expense that was passed
            dateView.setText((chosenExpense.getMonth() + 1) + "/" + chosenExpense.getDay() + "/" + chosenExpense.getYear());
            amountView.setText(chosenExpense.getAmount() + "");
            descriptionView.setText(chosenExpense.getDescription() + "");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //modifies or creates a new Expense
    public void saveExpense(View v) {

        //check for correct inputs in editTexts
        if (checkForEmpty(amountView, descriptionView) && DecimalDigits.isValidInput(amountView)) {

            //in case the user comes from MainActivity create a new Expense
            if (activityComingFrom == MAIN_ACTIVITY_KEY) {

                amount = Double.parseDouble(amountView.getText().toString());
                description = descriptionView.getText().toString();

                dataSource.createExpense(chosenDay, chosenMonth, chosenYear, amount, description);
                startActivity(new Intent(this, ExpenseListActivity.class));

            //in case the user comes from ShowExpenseActivity modify the chosen Expense
            } else if (activityComingFrom == SHOW_EXPENSE_ACTIVITY_KEY) {
                amount = Double.parseDouble(amountView.getText().toString());
                description = descriptionView.getText().toString();
                dataSource.modifyExpense(chosenDay, chosenMonth, chosenYear, amount, description, chosenExpense.getId());
                finish();

            }
        }
    }

    /**
     * Checks whether the user entered or not
     * the information to the given textview.
     * If not - set an error to an appropriate View.
     *
     * @param amountView      amount field to be checked
     * @param descriptionView description field to be checked
     * @return whether both fields are not empty
     */

    public boolean checkForEmpty(TextView amountView, TextView descriptionView) {
        boolean amountViewIsOk;
        boolean descriptionViewIsOk;

        if (amountView.getText().toString().matches("")) {
            amountView.setError("enter amount");
            amountViewIsOk = false;
        } else {
            amountViewIsOk = true;
        }

        if (descriptionView.getText().toString().matches("")) {
            descriptionView.setError("enter description");
            descriptionViewIsOk = false;
        } else {
            descriptionViewIsOk = true;
        }

        return (amountViewIsOk && descriptionViewIsOk);

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView dateText = (TextView) getActivity().findViewById(R.id.dateView);
            //set the dateText view to the date that the user chose in the dialog
            dateText.setText((month + 1) + " / " + day + " / " + year);

            chosenDay = day;
            chosenMonth = month;
            chosenYear = year;

        }
    }
}



