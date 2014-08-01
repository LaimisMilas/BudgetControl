package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;

/**
 * Created by admin on 8/1/2014.
 */

public class ShowExpenseActivity extends Activity {

    TextView dateView;
    TextView amountView;
    TextView descriptionView;
    ExpensesDataSource dataSource;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        dataSource = new ExpensesDataSource(this);
        dataSource.open();


        dateView = (TextView) findViewById(R.id.dateView);
        amountView = (TextView) findViewById(R.id.expenseAmountView);
        descriptionView = (TextView) findViewById(R.id.expenseDescription);

        Intent intent = getIntent();
        Expense chosenExpense = dataSource.getExpenseBasedOnId(intent.getLongExtra("Expense Id", 0));

        dateView.setText((chosenExpense.getMonth() + 1) + "/" + chosenExpense.getDay() + "/" + chosenExpense.getYear());
        amountView.setText(chosenExpense.getAmount() + "");
        descriptionView.setText(chosenExpense.getDescription() + "");



    }
}
