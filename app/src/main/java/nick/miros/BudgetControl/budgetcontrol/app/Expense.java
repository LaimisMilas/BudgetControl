package nick.miros.BudgetControl.budgetcontrol.app;

public class Expense {

	private long id;
	private String date;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return date + " " + amount + " " + category + " " + description + " " + paymentMethod;
	}
}
