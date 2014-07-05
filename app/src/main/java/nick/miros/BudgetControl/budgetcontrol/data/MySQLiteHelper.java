package nick.miros.BudgetControl.budgetcontrol.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	  public static final String TABLE_EXPENSES = "expenses";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_DAY = "day";
      public static final String COLUMN_MONTH = "month";
      public static final String COLUMN_YEAR = "year";
	  public static final String COLUMN_AMOUNT = "amount";
	  public static final String COLUMN_CATEGORY = "category";
	  public static final String COLUMN_DESCRIPTION = "description";
	  public static final String COLUMN_PAYMENT_METHOD = "payment_method";
	  
	  

	  private static final String DATABASE_NAME = "expenses1.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_EXPENSES + " (" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_DAY
          + " integer not null, " + COLUMN_MONTH
          + " integer not null, " + COLUMN_YEAR
          + " integer not null, " + COLUMN_AMOUNT
	      + " real not null, "  + COLUMN_CATEGORY
	      + " text not null, " + COLUMN_DESCRIPTION
	      + " text not null, " + COLUMN_PAYMENT_METHOD
	      + " text not null);";

	  public MySQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
	    onCreate(db);
	  }

	} 

