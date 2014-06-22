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
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;
import nick.miros.BudgetControl.budgetcontrol.helper.DecimalDigits;

import java.util.Calendar;

public class AddExpenseActivity extends Activity {

    private String date;
    private String category;
    private String description;
    private String paymentMethod;
    private double amount;
    private int currentDay;
    private int currentMonth;
    private int currentYear;
    private TextView currentDateText;
    private TextView amountEntered;
    private TextView dateView;
    private TextView descriptionView;
    private ImageButton dateButton;
    private Button addExpenseButton;
    private Spinner categorySpinner;
    private Spinner paymentSpinner;
    private Spinner categoryView;
    private Spinner paymentSpinnerView;
    private ExpensesDataSource datasource;
    private EditText amountView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Calendar c = Calendar.getInstance();

        currentDay = c.get(Calendar.DATE);
        currentMonth = c.get(Calendar.MONTH) + 1;
        currentYear = c.get(Calendar.YEAR);
        currentDateText = (TextView) findViewById(R.id.dateView);

        dateButton = (ImageButton) findViewById(R.id.chooseDateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });


        currentDateText.setText(currentDay + " / " + currentMonth + " / " + currentYear);


        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> simpleTextViewAdapter = ArrayAdapter.createFromResource(this,
                R.array.expense_category_array, R.layout.adapter_simple_textview);
        simpleTextViewAdapter.setDropDownViewResource(R.layout.adapter_simple_textview);
        categorySpinner.setAdapter(simpleTextViewAdapter);

        paymentSpinner = (Spinner) findViewById(R.id.payment_spinner);
        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this,
                R.array.payment_method, R.layout.adapter_simple_textview);
        simpleTextViewAdapter.setDropDownViewResource(R.layout.adapter_simple_textview);
        paymentSpinner.setAdapter(paymentAdapter);

        amountEntered = (TextView) findViewById(R.id.expense_amount);
        amountEntered.setFilters(new InputFilter[]{new DecimalDigits()});

        addExpenseButton = (Button) findViewById(R.id.addExpense);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNewExpense(v);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void addNewExpense(View v) {

        dateView = (TextView) findViewById(R.id.dateView);
        categoryView = (Spinner) findViewById(R.id.category_spinner);
        descriptionView = (TextView) findViewById(R.id.expense_description);
        amountView = (EditText) findViewById(R.id.expense_amount);
        paymentSpinnerView = (Spinner) findViewById(R.id.payment_spinner);

        if (checkForEmpty(amountView, descriptionView)) {

            if (DecimalDigits.isValidInput(amountView)) {

                date = dateView.getText().toString();
                amount = Double.parseDouble(amountView.getText().toString());
                category = categoryView.getSelectedItem().toString();
                description = descriptionView.getText().toString();
                paymentMethod = paymentSpinnerView.getSelectedItem().toString();

                datasource = new ExpensesDataSource(this);
                datasource.open();
                datasource.createExpense(date, amount, category, description, paymentMethod);

                startActivity(new Intent(this, ExpenseListActivity.class));
            }
        }
    }

    /**
     * Checks whether the user entered or not
     * the information to the given textview.
     * If not - set an error to an appropriate View.
     *
     * @param amountView amount field to be checked
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

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            TextView dateText = (TextView) getActivity().findViewById(R.id.dateView);
            month += 1;
            dateText.setText(month + " / " + day + " / " + year);
        }
    }
}



