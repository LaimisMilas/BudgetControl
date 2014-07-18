package nick.miros.BudgetControl.budgetcontrol.activities;

/**
 * Created by mymi on 18-Jul-14.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.data.ExpensesDataSource;
import nick.miros.BudgetControl.budgetcontrol.helper.ExpandableListAdapter;

public class ExpandableExpenseActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ExpensesDataSource datasource;
    List<Expense> allExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_list);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

       //listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        //expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {

        allExpenses = datasource.getAllExpenses();
        List<List<Expense>> expensesSortedByDates = new ArrayList<List<Expense>>();
        List<String> expenseDates = new ArrayList<String>();
        int i = 1;
        int j = 0;
        expensesSortedByDates.get(0).add(allExpenses.get(0));
        while (i != allExpenses.size() - 1) {

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
            }
            expensesSortedByDates.get(j).add(allExpenses.get(i));
        }

        expensesSortedByDates.toString();
        /*
        for (int i = 1; i < allExpenses.size(); i++) {
            int day = allExpenses.get(i).getDay();
            int month = allExpenses.get(i).getMonth();
            int year = allExpenses.get(i).getYear();
            String newDate = day + month + year + "";
            expenseDates.add(newDate);
        }
        */


        /*
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        */
    }
}
