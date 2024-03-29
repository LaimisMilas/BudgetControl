package nick.miros.BudgetControl.budgetcontrol.helper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nick.miros.BudgetControl.budgetcontrol.app.Currency;
import nick.miros.BudgetControl.budgetcontrol.app.R;

/**
 * A custom adapter used for translating an array of
 * Currency objects into a listview. Also implements
 * a filtering class that allows filtering of the listview
 * according to the input.
 */

public class CurrencyAdapter extends ArrayAdapter<Currency> {
    private final Context context;
    private List<Currency> currencies;
    private List<Currency> currenciesListOrig;
    private CurrencyFilter currencyFilter;

    public CurrencyAdapter(Context context, List<Currency> currencies) {
        super(context, R.layout.list_currency, currencies);
        this.context = context;
        this.currencies = currencies;
        this.currenciesListOrig = new ArrayList<Currency>(currencies);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_currency, parent, false);

        TextView currencyName = (TextView) rowView.findViewById(R.id.currencyName);
        TextView currencySymbol = (TextView) rowView.findViewById(R.id.currencySymbol);

        currencyName.setText(currencies.get(position).getName());
        currencySymbol.setText(currencies.get(position).getSymbol());

        //return the custom rowView for the the listView
        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (currencyFilter == null)
            currencyFilter = new CurrencyFilter();

        return currencyFilter;
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    public void resetData() {
        currencies = currenciesListOrig;
    }

    @Override
    public Currency getItem(int position) {
        return currencies.get(position);
    }


    //class that filters the listview according to the user input
    private class CurrencyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                // If there's no filter then we return the full list
                results.values = currencies;
                results.count = currencies.size();
            } else {
                // perform the filtering operation
                List<Currency> nCurrencyList = new ArrayList<Currency>();

                for (Currency c : currencies) {
                    if (c.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nCurrencyList.add(c);
                }

                results.values = nCurrencyList;
                results.count = nCurrencyList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                currencies = (List<Currency>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}


