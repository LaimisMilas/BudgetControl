package nick.miros.BudgetControl.budgetcontrol.helper;

/**
 * Created by mymi on 18-Jul-14.
 */

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import nick.miros.BudgetControl.budgetcontrol.app.Expense;
import nick.miros.BudgetControl.budgetcontrol.app.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter{
    private Context _context;
    private List<List<Expense>> _sortedExpenses; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context,  List<List<Expense>> sortedExpenses) {
        this._context = context;
        this._sortedExpenses = sortedExpenses;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
       // return this._listDataChild.get(this._listDataHeader.get(groupPosition))
         //       .get(childPosititon);

        return this._sortedExpenses.get(groupPosition).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(_sortedExpenses.get(groupPosition).get(childPosition).getAmount() + "");
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //return this._listDataChild.get(this._listDataHeader.get(groupPosition))
          //      .size();

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
        //String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(_sortedExpenses.get(groupPosition).get(0).getDay() + "");

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
