package nick.miros.BudgetControl.budgetcontrol.app;

public class Expense implements Comparable<Expense>{

    /**
     * Expense class with setters and getters for the fields
     */

	private long id;
    private int day;
    private int month;
    private int year;
    private long timeStamp;
	private double amount;
	private String category;
	private String description;
	private String paymentMethod;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getTimeStamp() {
    return timeStamp;}

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPaymentMethod() {
		return description;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Override
	public String toString() {
		return month + "/" + day + "/" + year + " " + amount + " " + category + " " + description + " " + paymentMethod;
	}

    public int compareTo(Expense compareExpense) {

        String compareDay;
        String compareMonth;

        if (compareExpense.getDay() < 10) {
            compareDay = "0" + compareExpense.getDay();
        }
        else {
            compareDay = compareExpense.getDay() + "";
        }

        if (compareExpense.getMonth() < 10) {
            compareMonth = "0" + compareExpense.getMonth();
        }
        else {
            compareMonth = compareExpense.getMonth() + "";
        }
        String compareYear = compareExpense.getYear() + "";

        int compareFullDate = Integer.parseInt(compareYear + compareMonth + compareDay);

        String thisDay;
        String thisMonth;

        if (compareExpense.getDay() < 10) {
            thisDay = "0" + this.getDay();
        }
        else {
            thisDay = this.getDay() + "";
        }

        if (compareExpense.getMonth() < 10) {
            thisMonth = "0" + this.getMonth();
        }
        else {
            thisMonth = this.getMonth() + "";
        }
        String thisYear = this.getYear() + "";

        int thisFullDate = Integer.parseInt(thisYear + thisMonth + thisDay);

        //ascending order
        //return  thisFullDate - compareFullDate;

        //descending order
        return compareFullDate - thisFullDate;

    }
}
