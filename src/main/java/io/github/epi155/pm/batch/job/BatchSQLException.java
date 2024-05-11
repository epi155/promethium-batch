package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.step.BatchException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

import java.sql.SQLException;

/**
 * batch wrapper SQL exception
 */
@Getter
@Slf4j
public class BatchSQLException extends BatchException {
    /**
     * batch exception constructor
     *
     * @param e exception wrapped
     */
    public BatchSQLException(SQLException e) {
        super(PmJCL.getInstance().rcErrorSQL(), e, "* SQL Error> code:{}, state:{}\n{}", e.getErrorCode(), e.getSQLState(), e.getMessage());
    }
    /**
     * batch exception constructor
     *
     * @param format     error message pattern
     * @param objects    error message parameters
     */
    public BatchSQLException(String format, Object... objects) {
        super(PmJCL.getInstance().rcErrorSQL(), MessageFormatter.arrayFormat(format, objects).getMessage());
    }
}
