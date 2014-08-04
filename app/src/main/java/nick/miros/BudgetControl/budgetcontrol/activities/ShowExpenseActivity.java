package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;

/**
 * Activity for showing the information about the Expense chosen from ExpandableListActivity by
 * the user. User has the ability to edit or delete the Expense shown in this activity via the
 * "delete" and "edit" buttons.
 */

public class ShowExpenseActivity extends Activity {

    TextView dateView;
    TextView amountView;
    TextView descriptionView;
    Button deleteButton;
    Button editButton;
    ExpensesDataSource dataSource;
    Expense chosenExpense;
    private static final String ACTIVITY_COMING_FROM_KEY = "activityComingFromKey";
    private static final int SHOW_EXPENSE_ACTIVITY_KEY = 2;
    private static final String EXPENSE_ID_KEY = "expenseIdKey";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        dataSource = new ExpensesDataSource(this);
        dataSource.open();

        dateView = (TextView) findViewById(R.id.dateView);
        amountView = (TextView) findViewById(R.id.expenseAmountView);
        descriptionView = (TextView) findViewById(R.id.expenseDescription);

        Intent receivingIntent = getIntent();
        chosenExpense = dataSource.getExpenseBasedOnId(receivingIntent.getLongExtra(EXPENSE_ID_KEY, 0));

        dateView.setText((chosenExpense.getMonth() + 1) + "/" + chosenExpense.getDay() + "/" + chosenExpense.getYear());
        amountView.setText(chosenExpense.getAmount() + "");
        descriptionView.setText(chosenExpense.getDescription() + "");

        deleteButton = (Button) findViewById(R.id.deleteExpenseButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDeleteExpensePrompt();
            }
        });

        editButton = (Button) findViewById(R.id.editExpenseButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SaveExpenseActivity.class);

                //put the SHOW_EXPENSE_ACTIVITY_KEY so that SaveExpenseActivity class will know
                //that we want to edit an Expense instead of creating a new one.
                intent.putExtra(ACTIVITY_COMING_FROM_KEY, SHOW_EXPENSE_ACTIVITY_KEY);
                intent.putExtra(EXPENSE_ID_KEY, chosenExpense.getId());
                startActivity(intent);
            }
        });




    }

    /**
     * Opens up an Alert Dialog that asks the user whether he wants to delete the current Expense
     * or not.
     */
    public void startDeleteExpensePrompt() {
        LayoutInflater li = LayoutInflater.from(ShowExpenseActivity.this);
        View promptsView = li.inflate(R.layout.alert_delete_expense_prompt, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(ShowExpenseActivity.this)
                .setView(promptsView)
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {

                        //delete the Expense and close this Activity in case the user clicks "Yes"
                        dataSource.deleteExpense(chosenExpense);
                        alertDialog.dismiss();
                        finish();
                    }
                });

                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();

    }
}
