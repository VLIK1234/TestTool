package amtt.epam.com.amtt.util;

/**
 @author Artsiom_Kaliaha
 @version on 23.06.2015
 */

public interface Validator {

    String getMessage(CharSequence viewHint);

    boolean validate(TextEditable editable);

}
