package nick.miros.BudgetControl.budgetcontrol.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;

public class ExpensesDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
	      MySQLiteHelper.COLUMN_DATE,
	      MySQLiteHelper.COLUMN_AMOUNT,
	      MySQLiteHelper.COLUMN_CATEGORY,
	      MySQLiteHelper.COLUMN_DESCRIPTION,
	      MySQLiteHelper.COLUMN_PAYMENT_METHOD};

	  public ExpensesDataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context);
	  }
	  

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	    //database.execSQL("DROP TABLE IF EXISTS " + dbHelper.TABLE_EXPENSES);
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public Expense createExpense(String date, double amount, String category, String description, String paymentMethod) {
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_DATE, date);
	    values.put(MySQLiteHelper.COLUMN_AMOUNT, amount);
	    values.put(MySQLiteHelper.COLUMN_CATEGORY, category);
	    values.put(MySQLiteHelper.COLUMN_DESCRIPTION, description);
	    values.put(MySQLiteHelper.COLUMN_PAYMENT_METHOD, paymentMethod);
	    long insertId = database.insert(MySQLiteHelper.TABLE_EXPENSES, null,
	        values);
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_EXPENSES,
	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Expense newExpense = cursorToExpense(cursor);
	    cursor.close();
	    return newExpense;
	  }

	  public void deleteExpense(Expense expense) {
	    long id = expense.getId();
	    System.out.println("Expense deleted with id: " + id);
	    database.delete(MySQLiteHelper.TABLE_EXPENSES, MySQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<Expense> getAllExpenses() {
	    List<Expense> expenses = new ArrayList<Expense>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_EXPENSES,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Expense expense = cursorToExpense(cursor);
	      expenses.add(expense);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return expenses;
	  }

	  private Expense cursorToExpense(Cursor cursor) {
		Expense expense = new Expense();
	    expense.setId(cursor.getLong(0));
	    expense.setDate(cursor.getString(1));
	    expense.setAmount(cursor.getDouble(2));
	    expense.setCategory(cursor.getString(3));
	    expense.setDescription(cursor.getString(4));
	    expense.setPaymentMethod(cursor.getString(5));
	    return expense;
	  }
}
