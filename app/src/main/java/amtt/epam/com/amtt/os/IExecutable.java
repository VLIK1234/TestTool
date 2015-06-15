package amtt.epam.com.amtt.os;

/**
 * Created by Artsiom_Kaliaha on 15.06.2015.
 */
public interface IExecutable<ExecutionResult> {

    ExecutionResult execute() throws Exception;

}
