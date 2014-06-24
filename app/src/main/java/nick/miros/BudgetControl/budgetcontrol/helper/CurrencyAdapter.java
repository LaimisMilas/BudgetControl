package nick.miros.BudgetControl.budgetcontrol.helper;

/**
 * Created by mymi on 24-Jun-14.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nick.miros.BudgetControl.budgetcontrol.app.R;

public class CurrencyAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> currencyNames;
    private final ArrayList<String> currencySymbols;

    public CurrencyAdapter(Context context, ArrayList<String> currencyNames, ArrayList<String> currencySymbols) {
        super(context, R.layout.list_currency, currencyNames);
        this.context = context;
        this.currencyNames = currencyNames;
        this.currencySymbols = currencySymbols;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_currency, parent, false);
        TextView currencyName = (TextView) rowView.findViewById(R.id.currencyName);
        TextView currencySymbol = (TextView) rowView.findViewById(R.id.currencySymbol);
        currencyName.setText(currencyNames.get(position));
        currencySymbol.setText(currencySymbols.get(position));

        return rowView;
    }
}
