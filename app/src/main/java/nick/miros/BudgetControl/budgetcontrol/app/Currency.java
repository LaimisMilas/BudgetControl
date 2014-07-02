package nick.miros.BudgetControl.budgetcontrol.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class for setting and getting currencies
 * <p/>
 * This class makes and returns Currency class
 * and also contains the current curency that is
 * used across all other classes
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

    /**
     * Returns the currency chosen by the user
     * that was saved in the SharedPreferences earlier.
     *
     * @param context context of an activity that requests the currency
     * @return current currency used
     */
    public static String getCurrentCurrencyUsed(Context context) {
        settings = context.getSharedPreferences(currencyPrefs, Context.MODE_PRIVATE);
        if (settings.contains(currencySymbolKey)) {
            currentCurrencyUsed = settings.getString(currencySymbolKey, "");
        } else {
            currentCurrencyUsed = " ";
        }
        return currentCurrencyUsed;
    }

    @Override
    public String toString() {
        return name + " - " + symbol;
    }

}
