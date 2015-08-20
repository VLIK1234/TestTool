package amtt.epam.com.amtt.datasource;

/**
 @author Artsiom_Kaliaha
 @version on 16.06.2015
 */

public interface DataSource<Params, DataSourceResult> extends Plugin {

    DataSourceResult getData(Params params) throws Exception;

}
