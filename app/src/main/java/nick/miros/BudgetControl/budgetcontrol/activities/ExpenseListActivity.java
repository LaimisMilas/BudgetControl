package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;

public class ExpenseListActivity extends ListActivity {
	private ExpensesDataSource datasource;

	 public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_expense_list);
		    
		    datasource = new ExpensesDataSource(this);
		    datasource.open();

		    List<Expense> values = datasource.getAllExpenses();

		    // use the SimpleCursorAdapter to show the
		    // elements in a ListView
		    ArrayAdapter<Expense> adapter = new ArrayAdapter<Expense>(this,
		        android.R.layout.simple_list_item_1, values);
		    setListAdapter(adapter);

		  }
	 
	  @Override
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	  }

}
