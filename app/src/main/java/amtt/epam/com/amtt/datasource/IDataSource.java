package amtt.epam.com.amtt.datasource;

/**
 * Created by Artsiom_Kaliaha on 16.06.2015.
 */
public interface IDataSource<Source, Param> {

    Source getData(Param param) throws Exception;

}
