package io.github.epi155.pm.batch.fault;

import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

import java.sql.SQLException;

import static io.github.epi155.pm.batch.fault.Fixed.RC_ERR_SQL;

/**
 * batch wrapper SQL exception
 */
@Getter
public class BatchSQLException extends BatchException {
    private static final long serialVersionUID = 4030873104809313217L;

    /**
     * batch exception constructor
     *
     * @param e exception wrapped
     */
    public BatchSQLException(SQLException e) {
        super(RC_ERR_SQL, e, "* SQL Error> code:{}, state:{}\n{}", e.getErrorCode(), e.getSQLState(), e.getMessage());
    }

    /**
     * batch exception constructor
     *
     * @param format  error message pattern
     * @param objects error message parameters
     */
    public BatchSQLException(String format, Object... objects) {
        super(RC_ERR_SQL, MessageFormatter.arrayFormat(format, objects).getMessage());
    }
}
