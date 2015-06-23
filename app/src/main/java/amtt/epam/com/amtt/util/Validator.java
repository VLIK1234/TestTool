package amtt.epam.com.amtt.util;

/**
 * Created by Artsiom_Kaliaha on 23.06.2015.
 */
public interface Validator {

    String getMessage(CharSequence viewHint);

    boolean validate(TextEditable editable);

}
