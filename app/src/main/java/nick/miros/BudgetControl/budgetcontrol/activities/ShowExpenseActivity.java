package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import nick.miros.BudgetControl.budgetcontrol.app.R;

/**
 * Created by admin on 8/1/2014.
 */

public class ShowExpenseActivity extends Activity {

    TextView dateView;
    TextView amountView;
    TextView descriptionView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        dateView = (TextView) findViewById(R.id.dateView);
        amountView = (TextView) findViewById(R.id.expenseAmountView);
        descriptionView = (TextView) findViewById(R.id.expenseDescription);


        dateView.setText("date");
        amountView.setText("amount");
        descriptionView.setText("description");



    }
}
