package nick.miros.BudgetControl.budgetcontrol.activities;

/**
 * Created by mymi on 18-Jul-14.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;
import nick.miros.BudgetControl.budgetcontrol.helper.ExpandableListAdapter;

public class ExpandableExpenseActivity extends Activity implements ExpandableListView.OnChildClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ExpensesDataSource datasource;
    List<Expense> allExpenses;
    List<List<Expense>> preparedListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datasource = new ExpensesDataSource(this);
        datasource.open();

        if (datasource.getAllExpenses().size() != 0) {
            setContentView(R.layout.expandable_list);

            // get the listview
            expListView = (ExpandableListView) findViewById(R.id.lvExp);

            preparedListData = prepareListData();

            listAdapter = new ExpandableListAdapter(this, preparedListData);

            // setting list adapter
            expListView.setAdapter(listAdapter);
            expListView.setOnChildClickListener(this);
        }
        else
        {
            setContentView(R.layout.expandable_list_empty);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        // TODO Auto-generated method stub

        Intent intent = new Intent(getApplicationContext(), ShowExpenseActivity.class);
        preparedListData = prepareListData();
        intent.putExtra("Expense Id", preparedListData.get(groupPosition).get(childPosition).getId());
        startActivity(intent);
        return true;
    }

    /*
     * Preparing the list data
     */
    private List<List<Expense>> prepareListData() {

        allExpenses = datasource.getAllExpenses();
        Collections.sort(allExpenses);
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
            if (!fullDate1.equals(fullDate)) {
                j++;
                List<Expense> anotherList = new ArrayList<Expense>();
                expensesSortedByDates.add(anotherList);
            }
            expensesSortedByDates.get(j).add(allExpenses.get(i));
            i++;
        }
        return expensesSortedByDates;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (datasource.getAllExpenses().size() != 0) {
            setContentView(R.layout.expandable_list);

            // get the listview
            expListView = (ExpandableListView) findViewById(R.id.lvExp);

            preparedListData = prepareListData();

            listAdapter = new ExpandableListAdapter(this, preparedListData);

            // setting list adapter
            expListView.setAdapter(listAdapter);
            expListView.setOnChildClickListener(this);
        }
        else
        {
            setContentView(R.layout.expandable_list_empty);
        }
    }
}
