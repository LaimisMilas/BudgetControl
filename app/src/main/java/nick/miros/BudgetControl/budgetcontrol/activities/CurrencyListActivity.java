package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import nick.miros.BudgetControl.budgetcontrol.app.Currency;
import nick.miros.BudgetControl.budgetcontrol.app.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CurrencyListActivity extends Activity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);

        listView = (ListView) findViewById(R.id.list);
        ArrayList<String> countries = new ArrayList<String>();

        // <Comment>: Code duplication.
        String eol = System.getProperty("line.separator");
        InputStream isCountry = getResources().openRawResource(R.raw.countries);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(isCountry);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + eol);
                countries.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(countries);

        ArrayList<String> currencyNames = new ArrayList<String>();

        // <Comment>: Code duplication.
        InputStream isCurrency = getResources().openRawResource(R.raw.currency_names);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(isCurrency);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + eol);
                currencyNames.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(currencyNames);

        ArrayList<String> currencySymbols = new ArrayList<String>();

        // <Comment>: Code duplication.
        InputStream isSymbol = getResources().openRawResource(R.raw.currency_symbols);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(isSymbol);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + eol);
                currencySymbols.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(currencySymbols);

        ArrayList<Currency> currencies = new ArrayList<Currency>();

        for (int i = 0; i < countries.size(); i++) {
            Currency currency = new Currency();
            String currentCountry = countries.get(i);
            String currentName = currencyNames.get(i);
            String currentSymbol = currencySymbols.get(i);
            currency.setCountry(currentCountry);
            currency.setCurrencyName(currentName);
            currency.setSymbol(currentSymbol);
            currencies.add(currency);
        }
        System.out.println(currencies);

        ArrayAdapter<Currency> adapter = new ArrayAdapter<Currency>(this,
                android.R.layout.simple_list_item_1, currencies);
        listView.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.currency_list, menu);
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
