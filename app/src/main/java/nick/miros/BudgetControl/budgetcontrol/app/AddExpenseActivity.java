package nick.miros.BudgetControl.budgetcontrol.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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
		setContentView(R.layout.add_expense);
		
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
		
		
		Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_category_array, R.layout.my_spinner_textview);
		adapter.setDropDownViewResource(R.layout.my_spinner_textview);
		spinner.setAdapter(adapter);
		
		Spinner paymentSpinner = (Spinner) findViewById(R.id.payment_spinner);
		ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this,
                R.array.payment_method, R.layout.my_spinner_textview);
		adapter.setDropDownViewResource(R.layout.my_spinner_textview);
		paymentSpinner.setAdapter(paymentAdapter);
		
		TextView AmountEntered = (TextView) findViewById(R.id.expense_amount);
		
		AmountEntered.setFilters(new InputFilter[] {new DecimalDigits(7,2)});
		
		Button addExpenseButton = (Button) findViewById(R.id.addExpense);
		
		addExpenseButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	addNewExpense(v);
		    }
		});
		
	}
	
	public void addNewExpense(View v)
	{
		
		TextView dateView = (TextView) findViewById(R.id.dateView);
		Spinner categoryView = (Spinner) findViewById(R.id.category_spinner);
		TextView descriptionView = (TextView) findViewById(R.id.expense_description);
		TextView amountView = (TextView) findViewById(R.id.expense_amount);
		Spinner paymentSpinnerView = (Spinner) findViewById(R.id.payment_spinner);
		
		date = dateView.getText().toString();
		amount = Double.parseDouble(amountView.getText().toString());
		category = categoryView.toString();
		description = descriptionView.getText().toString();
		paymentMethod = paymentSpinnerView.toString();

		
		datasource = new ExpensesDataSource(this);
	    datasource.open();
		datasource.createExpense(date, amount, category, description, paymentMethod);
		
		startActivity(new Intent(this, ExpenseList.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			TextView dateText = (TextView)getActivity().findViewById(R.id.dateView);
            month += 1;
			dateText.setText(month + " / " + day + " / " + year);
		}
		}
}



