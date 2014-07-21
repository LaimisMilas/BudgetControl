package nick.miros.BudgetControl.budgetcontrol.activities;

/**
 * Created by mymi on 18-Jul-14.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
       // prepareListData();

       //listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        //expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData1() {


        listDataHeader = new ArrayList<String>();

        allExpenses = datasource.getAllExpenses();
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
                listDataHeader.add(fullDate1);
            }
            expensesSortedByDates.get(j).add(allExpenses.get(i));
            i++;
        }

        Log.e("Lists!", expensesSortedByDates.toString());
        Log.e("Lists!", expensesSortedByDates.toString());
        Log.e("Lists!", expensesSortedByDates.toString());
        Log.e("Lists!", expensesSortedByDates.toString());
        Log.e("Lists!", expensesSortedByDates.toString());
        Log.e("Lists!", expensesSortedByDates.toString());
        Log.e("Lists!", expensesSortedByDates.toString());
        Log.e("Lists!", expensesSortedByDates.toString());
        Log.e("Lists!", expensesSortedByDates.toString());


        //listDataHeader = new ArrayList<String>();
        //listDataChild = new HashMap<String, List<String>>();
        //listDataChild = new HashMap<String, List<Expense>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // for (int f = 0; f < listDataHeader.size(); f++) {
        // listDataChild.put(listDataHeader.get(f), expensesSortedByDates.get(f));
        // }
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

    }
}
