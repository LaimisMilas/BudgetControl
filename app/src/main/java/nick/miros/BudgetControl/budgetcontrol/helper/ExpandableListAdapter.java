package nick.miros.BudgetControl.budgetcontrol.helper;

/**
 * A custom adapter that translates a list of lists of Expenses objects sorted by dates
 * into an Expandable List.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;
import nick.miros.BudgetControl.budgetcontrol.app.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter{
    private Context _context;
    private List<List<Expense>> _sortedExpenses;
    public static final String[] monthNames = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November",
            "December"};

    public ExpandableListAdapter(Context context,  List<List<Expense>> sortedExpenses) {
        this._context = context;
        this._sortedExpenses = sortedExpenses;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._sortedExpenses.get(groupPosition).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_item, null);
        }

        TextView groupChildDescription = (TextView) convertView
                .findViewById(R.id.group_expense_description);
        TextView groupChildAmount = (TextView) convertView
                .findViewById(R.id.group_expense_amount);

        groupChildDescription.setText(_sortedExpenses.get(groupPosition).get(childPosition).getDescription() + "");
        groupChildAmount.setText(_sortedExpenses.get(groupPosition).get(childPosition).getAmount() + "");
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _sortedExpenses.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._sortedExpenses.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._sortedExpenses.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_group, null);
        }

        Expense expense = _sortedExpenses.get(groupPosition).get(0);

        TextView groupDate = (TextView) convertView
                .findViewById(R.id.groupDate);
        groupDate.setTypeface(null, Typeface.BOLD);
        groupDate.setText(monthNames[expense.getMonth()] + " " + expense.getDay() + " " + expense.getYear());

        //set the group amount total from all of the Expense amounts in the group
        double groupAmount = 0;
        for (int i = 0; i < _sortedExpenses.get(groupPosition).size(); i++) {
            groupAmount+=_sortedExpenses.get(groupPosition).get(i).getAmount();
        }

        TextView groupAmountView = (TextView) convertView.findViewById(R.id.groupAmount);
        groupAmountView.setTypeface(null, Typeface.BOLD);
        groupAmountView.setText(groupAmount + "");

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
