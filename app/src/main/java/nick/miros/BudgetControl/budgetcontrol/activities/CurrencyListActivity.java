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

    private ArrayList<String> countries = new ArrayList<String>();
    private ArrayList<String> currencyNames = new ArrayList<String>();
    private ArrayList<String> currencySymbols = new ArrayList<String>();
    private ArrayList<Currency> currencies = new ArrayList<Currency>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);

        listView = (ListView) findViewById(R.id.list);

        //extract the needed Strings from currency files
        countries = extractFileInfo(R.raw.countries);
        currencyNames = extractFileInfo(R.raw.currency_names);
        currencySymbols = extractFileInfo(R.raw.currency_codes);

        //create an array to be passed to an adapter
        currencies = new ArrayList<Currency>();

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

        //make the listview
        ArrayAdapter<Currency> adapter = new ArrayAdapter<Currency>(this,
                android.R.layout.simple_list_item_1, currencies);
        listView.setAdapter(adapter);

    }

    /**
     * Extracts text line by line from a file specified
     * and puts it into an array of Strings
     *
     * @param fileId id of a txt file that is to be parsed
     * @return an array of Strings that were extracted from the file
     */

    public ArrayList<String> extractFileInfo(int fileId) {
        String eol = System.getProperty("line.separator");
        ArrayList<String> extractedArray = new ArrayList<String>();

        InputStream isSymbol = getResources().openRawResource(fileId);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(isSymbol);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + eol);
                extractedArray.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return extractedArray;
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
