package nick.miros.BudgetControl.budgetcontrol.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import nick.miros.BudgetControl.budgetcontrol.app.Currency;
import nick.miros.BudgetControl.budgetcontrol.app.R;
import nick.miros.BudgetControl.budgetcontrol.helper.CurrencyAdapter;

/**
 * An Activity that contains a listview where the user chooses
 * their preferred currency. The Activity contains a filter that
 * sorts the list according to the user input.
 */

public class CurrencyListActivity extends Activity {

    private ListView listView;
    private EditText inputSearch;
    private ArrayList<String> currencyNames = new ArrayList<String>();
    private ArrayList<String> currencySymbols = new ArrayList<String>();
    private ArrayList<Currency> currencies = new ArrayList<Currency>();
    private final String nameIdentifier = "currencyName";
    private final String symbolIdentifier = "currencySymbol";
    private CurrencyAdapter adapter;
    private final int currencyRequestCode = 1;
    private final String MY_PREFS_KEY = "myPrefsKey";
    private SharedPreferences settings;
    private final String CURRENCY_SYMBOL_KEY = "currencySymbolKey";
    private final String CURRENCY_NAME_KEY = "currencyNameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);

        settings = getSharedPreferences(MY_PREFS_KEY, Context.MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.list);

        //extract the needed Strings from currency files
        currencyNames = extractFileInfo(R.raw.currency_names);
        currencySymbols = extractFileInfo(R.raw.currency_symbols);

        //create an array of Currencies to be passed to an adapter
        currencies = new ArrayList<Currency>();

        for (int i = 0; i < currencyNames.size(); i++) {

            Currency currency = new Currency();
            String currentName = currencyNames.get(i);
            String currentSymbol = currencySymbols.get(i);
            currency.setName(currentName);
            currency.setSymbol(currentSymbol);
            currencies.add(currency);
        }

        adapter = new CurrencyAdapter(this, currencies);
        listView.setAdapter(adapter);

        //gathers the chosen currency info and sends to the previous activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                Currency currency = (Currency) parent.getItemAtPosition(position);

                //return the chosen name and symbol back to the BudgetSettingsActivity
                Intent intent = new Intent();
                intent.putExtra(nameIdentifier, currency.getName());
                intent.putExtra(symbolIdentifier, currency.getSymbol());

                SharedPreferences.Editor editor = settings.edit();
                editor.putString(CURRENCY_SYMBOL_KEY, currency.getSymbol());
                editor.putString(CURRENCY_NAME_KEY, currency.getName());
                editor.commit();

                setResult(currencyRequestCode, intent);
                finish();


            }

        });

        inputSearch = (EditText) findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //check whether currently there are less letters than before in inputSearch
                if (count < before) {
                    // Resetting the adapter data because we're deleting a character
                    adapter.resetData();
                }
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public void onBackPressed() {
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
