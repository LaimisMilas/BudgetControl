package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import nick.miros.BudgetControl.budgetcontrol.app.Currency;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.helper.CurrencyAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CurrencyListActivity extends Activity {
    private ListView listView;
    public static final String currencyUsed = "CurrencyUsedKey";

    private ArrayList<String> countries = new ArrayList<String>();
    private ArrayList<String> currencyNames = new ArrayList<String>();
    private ArrayList<String> currencySymbols = new ArrayList<String>();
    private ArrayList<Currency> currencies = new ArrayList<Currency>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);
        final SharedPreferences settings = getPreferences(MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.list);

        //extract the needed Strings from currency files
        currencyNames = extractFileInfo(R.raw.currency_names);
        currencySymbols = extractFileInfo(R.raw.currency_symbols);

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

        CurrencyAdapter adapter = new CurrencyAdapter(this, currencyNames, currencySymbols);
        listView.setAdapter(adapter);

        if (settings.contains(currencyUsed)) {

            Toast.makeText(getApplicationContext(), settings.getString(currencyUsed, ""), Toast.LENGTH_SHORT).show();
        }

        //saves the currency chosen from the list to sharePreferences
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                SharedPreferences.Editor editor = settings.edit();
                editor.putString(currencyUsed, currencySymbols.get(position));
                editor.commit();



            }

        });

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
