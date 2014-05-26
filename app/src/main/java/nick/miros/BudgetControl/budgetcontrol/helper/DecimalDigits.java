package nick.miros.BudgetControl.budgetcontrol.helper;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigits implements InputFilter {

Pattern mPattern;

public DecimalDigits() {
    mPattern=Pattern.compile("^(([1-9](\\d*)?)\\.?(\\d{0,2})?)|(0(\\.\\d{0,2})?)|\\.(\\d{0,2})?$");

}



@Override
public CharSequence filter(CharSequence source,
                           int sourceStart, int sourceEnd,
                           Spanned destination, int destinationStart,
                           int destinationEnd)
{
    String textToCheck = destination.subSequence(0, destinationStart).
            toString() + source.subSequence(sourceStart, sourceEnd) +
            destination.subSequence(
                    destinationEnd, destination.length()).toString();

    Matcher matcher = mPattern.matcher(textToCheck);

    // Entered text does not match the pattern
    if(!matcher.matches()){

        // It does not match partially too
        if(!matcher.hitEnd()){
            return "";
        }

    }

    return null;
}

}