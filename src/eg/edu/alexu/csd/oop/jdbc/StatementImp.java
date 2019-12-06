package eg.edu.alexu.csd.oop.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;

import eg.edu.alexu.csd.oop.db.DBMSController;
import eg.edu.alexu.csd.oop.db.DataCarrier;

public class StatementImp implements Statement {

	private logger logger;
	private ConnectionImp connection;
	private ResultsetImp resultSet;
	private ArrayList<String> batches;
	private Object[][] ProducedData;
	private int time = 0;
	private boolean isClose;
	DBMSController executor;

	public StatementImp(ConnectionImp con) {
		logger = logger.getInstance();
		executor = DBMSController.getInstance();
		logger.log.info("Building Statement object.");
		this.connection = con;
		isClose = false;
	}

	@Override
	public void addBatch(String arg0) throws SQLException {
		try {
			if (isClose) {

				throw new SQLException("Statement is closed ..Can't do operations");
			}
			logger.log.info("adding new Batch.");
			batches.add(arg0);

		} catch (Exception e) {
			logger.log.info("Error in adding Batch");

		}

	}

	@Override
	public void clearBatch() throws SQLException {
		try {
			if (isClose) {
				throw new SQLException("Statement is closed ..Can't do operations");
			}
			logger.log.info("Empties list of SQL commands.");
			batches.clear();

		} catch (Exception e) {
			logger.log.info("Error in clearing Batch List");

		}

	}

	@Override
	public void close() throws SQLException {
		connection = null;
		batches = null;
		resultSet = null;
		isClose = true;
	}

	@Override
	public boolean execute(String arg0) throws SQLException {
		if (isClose) {
			logger.log.warning("Statement is closed.");
			throw new SQLException("Statement is closed ..Can't do operations");
		}
		logger.log.info("Executing the given SQL statement");

		if (arg0.trim().split("\\s+")[0].equalsIgnoreCase("create")
				|| arg0.trim().split("\\s+")[0].equalsIgnoreCase("drop")) {
			String s = executor.invoke(arg0);
			if(s.contains("wasn't")) {
				return false;
			} else {
				return true;
			}
		} else if (arg0.trim().split("\\s+")[0].equalsIgnoreCase("insert")
				|| arg0.trim().split("\\s+")[0].equalsIgnoreCase("delete")
				|| arg0.trim().split("\\s+")[0].equalsIgnoreCase("update")) {
			int result = executeUpdate(arg0);
			return result > 0;
		} else if (arg0.trim().split("\\s+")[0].equalsIgnoreCase("select")) {
			logger.log.info("Generating result of select query..");
			ResultSet result = executeQuery(arg0);
			int k = result.getMetaData().getColumnCount();
			return result.getMetaData().getColumnCount() > 0;
		}
		return false;
	}

	
	@Override
	public ResultSet executeQuery(String arg0) throws SQLException {
		if (isClose) {
			logger.log.warning("Statement is closed.");
			throw new SQLException("Statement is closed ..Can't do operations");
		}
		logger.log.info("Executing the given SQL statement and returning ResultSet ");
		Object[][] ProducedData = executor.invokee(arg0);
		String tablename = executor.getTableName(arg0);
		DataCarrier columnsinfo = executor.getColumnsInfo(arg0);
		resultSet = new ResultsetImp(ProducedData, columnsinfo, tablename, this);
		return resultSet;
	}

	@Override
	public int executeUpdate(String arg0) throws SQLException {
		if (isClose) {
			logger.log.warning("Statement is closed.");
			throw new SQLException("Statement is closed ..Can't do operations");
		}
		logger.log.info("Executing the given SQL Update statement.");

		String s = executor.invoke(arg0);
		String[] ss = s.split("\\s");
		return Integer.parseInt(ss[0]);

	}

	@Override
	public int[] executeBatch() throws SQLException {
		if (isClose) {
			logger.log.warning("Statement is closed.");
			throw new SQLException("Statement is closed ..Can't do operations");
		}
		logger.log.info("Executing list of SQL commands.");
		int[] arr = new int[batches.size()];
		for (int i = 0; i < batches.size(); i++) {
			String query = batches.get(i);
			String s = executor.invoke(query);
			if (execute(query))
				arr[i] = SUCCESS_NO_INFO;

			else
				arr[i] = executeUpdate(query);
		}
		return arr;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		if (isClose) {
			logger.log.warning("Statement is closed.");
			throw new SQLException("Statement is closed ..Can't do operations");
		}
		logger.log.info("getting the current query timeout limit ");

		return time;
	}

	@Override
	public void setQueryTimeout(int arg0) throws SQLException {
		if (isClose) {
			logger.log.warning("Statement is closed.");
			throw new SQLException("Statement is closed ..Can't do operations");
		}
		logger.log.info("setting the current query timeout limit ");
		time = arg0;

	}

	@Override
	public Connection getConnection() throws SQLException {
		if (isClose) {
			logger.log.warning("Statement is closed.");
			throw new SQLException("Statement is closed ..Can't do operations");
		}
		logger.log.info("Getting the connection that produced this statement");
		return connection;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		if (isClose) {
			logger.log.warning("Statement is closed.");
			throw new SQLException("Statement is closed ..Can't do operations");
		}
		logger.log.info("Getting the current result as a ResultSet object ");
		return this.resultSet;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancel() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String arg0, int arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String arg0, int[] arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String arg0, String[] arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String arg0, int arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String arg0, String[] arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxRows() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetType() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isClosed() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPoolable() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCursorName(String arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchDirection(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchSize(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxFieldSize(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxRows(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPoolable(boolean arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

}