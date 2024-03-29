package nick.miros.BudgetControl.budgetcontrol.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;

public class ExpensesDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
	      MySQLiteHelper.COLUMN_DAY,
          MySQLiteHelper.COLUMN_MONTH,
          MySQLiteHelper.COLUMN_YEAR,
          MySQLiteHelper.COLUMN_TIME_STAMP,
	      MySQLiteHelper.COLUMN_AMOUNT,
	      MySQLiteHelper.COLUMN_CATEGORY,
	      MySQLiteHelper.COLUMN_DESCRIPTION,
	      MySQLiteHelper.COLUMN_PAYMENT_METHOD};

      private String[] dateFilterColumns = { MySQLiteHelper.COLUMN_DAY,
          MySQLiteHelper.COLUMN_MONTH,
          MySQLiteHelper.COLUMN_YEAR,
          MySQLiteHelper.COLUMN_AMOUNT};

      private String[] timeStampColumns = { MySQLiteHelper.COLUMN_TIME_STAMP,
         MySQLiteHelper.COLUMN_AMOUNT
      };

	  public ExpensesDataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context);
	  }
	  

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public Expense createExpense(int day, int month, int year, double amount, String category, String description, String paymentMethod) {
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_DAY, day);
        values.put(MySQLiteHelper.COLUMN_MONTH, month);
        values.put(MySQLiteHelper.COLUMN_YEAR, year);

        Calendar c = Calendar.getInstance();
        final long currentSecond = c.getTimeInMillis() / 1000;
        values.put(MySQLiteHelper.COLUMN_TIME_STAMP, currentSecond);
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
	    //close the cursor
	    cursor.close();
	    return expenses;
	  }

      public double getAllTodayExpenses () {

          double amountSpent = 0;

          Calendar c = Calendar.getInstance();
          int currentDay = c.get(Calendar.DATE);
          int currentMonth = c.get(Calendar.MONTH) + 1;
          int currentYear = c.get(Calendar.YEAR);

          Cursor cursor = database.query(MySQLiteHelper.TABLE_EXPENSES, dateFilterColumns,
                  MySQLiteHelper.COLUMN_DAY + "=" + currentDay + " AND "
                + MySQLiteHelper.COLUMN_MONTH + "=" + currentMonth + " AND "
                + MySQLiteHelper.COLUMN_YEAR + "=" + currentYear, null, null, null, null);

          cursor.moveToFirst();
          while (!cursor.isAfterLast()) {
              amountSpent+=cursor.getDouble(3);
              cursor.moveToNext();
          }
          //close the cursor
          cursor.close();
          return amountSpent;
      }

      public double getAllMonthlyExpenses () {
          double amountSpent = 0;

          Calendar c = Calendar.getInstance();
          int currentMonth = c.get(Calendar.MONTH) + 1;
          int currentYear = c.get(Calendar.YEAR);

          Cursor cursor = database.query(MySQLiteHelper.TABLE_EXPENSES, dateFilterColumns,
                  MySQLiteHelper.COLUMN_MONTH + "=" + currentMonth + " AND "
                  + MySQLiteHelper.COLUMN_YEAR + "=" + currentYear, null, null, null, null);

          cursor.moveToFirst();
          while (!cursor.isAfterLast()) {
              amountSpent+=cursor.getDouble(3);
              cursor.moveToNext();
          }
          //close the cursor
          cursor.close();
          return amountSpent;
      }


      public double getExpensesStartingFrom(int day) {

          double expenses = 0;

          Calendar c = Calendar.getInstance();
          int currentDay = c.get(Calendar.DATE);
          int currentMonth = c.get(Calendar.MONTH) + 1;
          int currentYear = c.get(Calendar.YEAR);

          Cursor cursor = database.query(MySQLiteHelper.TABLE_EXPENSES, dateFilterColumns,
                  MySQLiteHelper.COLUMN_DAY + " BETWEEN " + day + " AND " + currentDay + " AND "
                  + MySQLiteHelper.COLUMN_MONTH + "=" + currentMonth + " AND "
                  + MySQLiteHelper.COLUMN_YEAR + "=" + currentYear, null, null, null, null);

          cursor.moveToFirst();
          while (!cursor.isAfterLast()) {
              expenses+=cursor.getDouble(3);
              cursor.moveToNext();
          }
          //close the cursor
          cursor.close();

          return expenses;
      }

    public double getExpensesFromTimeStamp(long timeStamp) {

        double expenses = 0;

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EXPENSES, timeStampColumns,
                MySQLiteHelper.COLUMN_TIME_STAMP + " > " + timeStamp , null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expenses+=cursor.getDouble(1);
            cursor.moveToNext();
        }
        //close the cursor
        cursor.close();

        return expenses;
    }

	  private Expense cursorToExpense(Cursor cursor) {
		Expense expense = new Expense();
	    expense.setId(cursor.getLong(0));
	    expense.setDay(cursor.getInt(1));
        expense.setMonth(cursor.getInt(2));
        expense.setYear(cursor.getInt(3));
        expense.setTimeStamp(cursor.getLong(4));
	    expense.setAmount(cursor.getDouble(5));
	    expense.setCategory(cursor.getString(6));
	    expense.setDescription(cursor.getString(7));
	    expense.setPaymentMethod(cursor.getString(8));
	    return expense;
	  }
}
