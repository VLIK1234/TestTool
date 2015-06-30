package amtt.epam.com.amtt.util;

import java.util.List;

/**
 * Created by Artsiom_Kaliaha on 23.06.2015.
 * All the custom views that can contain dynamically changeable text, e.g. TextInput or AutocompleteProgressVie
 * should implement this method
 */
public interface Validatable {

    void setValidators(List<Validator> validators);

    /*
    * Returns FALSE if validation is NOT PASSED
    * Returns TRUE if validation is PASSED
    * */
    boolean validate();

}
