package nick.miros.BudgetControl.budgetcontrol.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class for setting and getting countries
 */
public class Currency {
    private String name;
    private String symbol;
    private static SharedPreferences settings;
    private static final String currencySymbolKey = "currencySymbolKey";
    private static final String currencyPrefs = "currenciesKey";
    public static String currentCurrencyUsed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public static String getCurrentCurrencyUsed(Context context){
        settings = context.getSharedPreferences(currencyPrefs, Context.MODE_PRIVATE);
        if (settings.contains(currencySymbolKey)) {
            currentCurrencyUsed = settings.getString(currencySymbolKey, "");
        }
        else
        {
            currentCurrencyUsed = " ";
        }
        return currentCurrencyUsed;
    }

    @Override
    public String toString() {
        return name + " - " + symbol;
    }

}
