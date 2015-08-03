package amtt.epam.com.amtt.datasource;

/**
 @author Artsiom_Kaliaha
 @version on 16.06.2015
 */

public interface DataSource<Source, Param> extends Plugin {

    Source getData(Param param) throws Exception;

}
