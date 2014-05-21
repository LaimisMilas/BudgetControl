package nick.miros.BudgetControl.budgetcontrol.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;


public class BudgetSettings extends ActionBarActivity {

    TextView currentMonth;

    String[] monthNames = { "January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November",
            "December" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_settings);

        currentMonth = (TextView) findViewById(R.id.currentMonth);

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);

        currentMonth.setText("Budget for " + monthNames[month]);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.budget_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
