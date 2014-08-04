package nick.miros.BudgetControl.budgetcontrol.activities;

/**
 * This Activity shows all of the expenses created by the user.
 * The user can click on an Expense and go to an Activity where they can edit or delete it.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;
import nick.miros.BudgetControl.budgetcontrol.helper.ExpandableListAdapter;

public class ExpenseListActivity extends Activity implements ExpandableListView.OnChildClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ExpensesDataSource dataSource;
    List<Expense> allExpenses;
    List<List<Expense>> preparedListData;
    private static final String EXPENSE_ID_KEY = "expenseIdKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSource = new ExpensesDataSource(this);
        dataSource.open();

        //if Expenses dataSource is not empty create the list of expenses and set a listview
        if (dataSource.getAllExpenses().size() != 0) {
            setContentView(R.layout.expandable_list);

            //prepare the list of Expenses to be put into an adapter
            preparedListData = prepareListData();

            //load the sorted list of lists of expense into the adapter
            listAdapter = new ExpandableListAdapter(this, preparedListData);

            expListView = (ExpandableListView) findViewById(R.id.lvExp);
            expListView.setAdapter(listAdapter);
            expListView.setOnChildClickListener(this);
        }
        //else show a layout saying the list is empty
        else {
            setContentView(R.layout.expandable_list_empty);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        // TODO Auto-generated method stub

        Intent intent = new Intent(getApplicationContext(), ShowExpenseActivity.class);

        //put the chosen expense into an intent that will be shown in ShowExpenseActivity
        intent.putExtra(EXPENSE_ID_KEY, preparedListData.get(groupPosition).get(childPosition).getId());
        startActivity(intent);
        return true;
    }

    /**
     * Makes a list of lists of Expenses sorted by dates. Each list represents a separate date.
     */
    private List<List<Expense>> prepareListData() {

        allExpenses = dataSource.getAllExpenses();
        //sort the Expenses in chronological order
        Collections.sort(allExpenses);

        //algorithm for putting Expenses into different lists when the date of the Expense
        //is different from the previous one. Because the Expenses were sorted in the chronological
        //order beforehand - they only need to be checked by whether their dates differ.
        List<List<Expense>> expensesSortedByDates = new ArrayList<List<Expense>>();
        int i = 1;
        int j = 0;
        List<Expense> firstList = new ArrayList<Expense>();
        firstList.add(allExpenses.get(0));
        expensesSortedByDates.add(firstList);
        while (i != allExpenses.size()) {

            int day = allExpenses.get(i - 1).getDay();
            int month = allExpenses.get(i - 1).getMonth();
            int year = allExpenses.get(i - 1).getYear();
            String fullDate = day + month + year + "";

            int day1 = allExpenses.get(i).getDay();
            int month1 = allExpenses.get(i).getMonth();
            int year1 = allExpenses.get(i).getYear();
            String fullDate1 = day1 + month1 + year1 + "";
            //check if the dates of the Expenses are not equal to each other
            if (!fullDate1.equals(fullDate)) {
                j++;
                //create a new list and add it an existing array of lists
                List<Expense> anotherList = new ArrayList<Expense>();
                expensesSortedByDates.add(anotherList);
            }
            //add the the Expense currently being checked
            expensesSortedByDates.get(j).add(allExpenses.get(i));
            i++;
        }
        return expensesSortedByDates;

    }

    @Override
    public void onResume() {
        super.onResume();
        //if Expenses dataSource is not empty create the list of expenses and set a listview
        if (dataSource.getAllExpenses().size() != 0) {
            setContentView(R.layout.expandable_list);

            //prepare the list of Expenses to be put into an adapter
            preparedListData = prepareListData();

            //load the sorted list of lists of expense into the adapter
            listAdapter = new ExpandableListAdapter(this, preparedListData);

            expListView = (ExpandableListView) findViewById(R.id.lvExp);
            expListView.setAdapter(listAdapter);
            expListView.setOnChildClickListener(this);
        }
        //else show a layout saying the list is empty
        else {
            setContentView(R.layout.expandable_list_empty);
        }
    }
}
