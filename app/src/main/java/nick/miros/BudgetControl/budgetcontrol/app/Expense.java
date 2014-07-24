package nick.miros.BudgetControl.budgetcontrol.app;

public class Expense {

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
}
