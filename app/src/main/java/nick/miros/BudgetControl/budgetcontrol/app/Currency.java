package nick.miros.BudgetControl.budgetcontrol.app;

/**
 * Class for setting and getting countries
 */
public class Currency {
    private String country;
    private String currencyName;
    private String currencyCode;
    private String symbol;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return country + " " + currencyName + " - " + symbol;
    }

}
