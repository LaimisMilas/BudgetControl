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

    private ExpensesDataSource datasource;
    private String date;
    private String category;
    private String description;
    private double amount;
    private String paymentMethod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Calendar c = Calendar.getInstance();

        int currentDay = c.get(Calendar.DATE);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentYear = c.get(Calendar.YEAR);

        TextView currentDateText = (TextView) findViewById(R.id.dateView);

        ImageButton dateButton = (ImageButton) findViewById(R.id.chooseDateButton);

        dateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });


        currentDateText.setText(currentDay + " / " + currentMonth + " / " + currentYear);


        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> simpleTextViewAdapter = ArrayAdapter.createFromResource(this,
                R.array.expense_category_array, R.layout.adapter_simple_textview);
        simpleTextViewAdapter.setDropDownViewResource(R.layout.adapter_simple_textview);
        categorySpinner.setAdapter(simpleTextViewAdapter);

        Spinner paymentSpinner = (Spinner) findViewById(R.id.payment_spinner);
        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this,
                R.array.payment_method, R.layout.adapter_simple_textview);
        simpleTextViewAdapter.setDropDownViewResource(R.layout.adapter_simple_textview);
        paymentSpinner.setAdapter(paymentAdapter);

        TextView AmountEntered = (TextView) findViewById(R.id.expense_amount);

        AmountEntered.setFilters(new InputFilter[]{new DecimalDigits()});

        Button addExpenseButton = (Button) findViewById(R.id.addExpense);

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

        TextView dateView = (TextView) findViewById(R.id.dateView);
        Spinner categoryView = (Spinner) findViewById(R.id.category_spinner);
        TextView descriptionView = (TextView) findViewById(R.id.expense_description);
        EditText amountView = (EditText) findViewById(R.id.expense_amount);
        Spinner paymentSpinnerView = (Spinner) findViewById(R.id.payment_spinner);

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



