package eg.edu.alexu.csd.oop.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;


import eg.edu.alexu.csd.oop.db.DBMSController;

public class StatementImp implements Statement {
	private ConnectionImp connection;
	private ResultsetImp resultSet;
	private ArrayList<String> batches;
	private Object[][] ProducedData;

	public StatementImp(ConnectionImp con) {
		this.connection = con;
	}

	@Override
	public void addBatch(String arg0) throws SQLException {
		batches.add(arg0);

	}

	@Override
	public void clearBatch() throws SQLException {
		batches.clear();

	}

	@Override
	public void close() throws SQLException {
		connection = null;
		batches = null;
		resultSet = null;
	}

	@Override
	public boolean execute(String arg0) throws SQLException {
		DBMSController executor = DBMSController.getInstance();
		executor.invoke(arg0);
		if (executeQuery(arg0).next())
			return true;
		

		return false;
	}

	@Override
	public ResultSet executeQuery(String arg0) throws SQLException {
		DBMSController executor = DBMSController.getInstance();
		String s = executor.invoke(arg0);
		String tablename = executor.getTableName();
		String [][] columnsinfo = executor.getColumnsInfo();
		String[] ss = s.split("\\n");
		ProducedData = new String[ss.length][ss[0].length()];
		for (int i = 0; i < ss.length; i++) {
			String[] t = ss[i].split("\\s");
			ProducedData[i] = t;

		}
		resultSet = new ResultsetImp(ProducedData, columnsinfo, tablename, this);
		return resultSet;

	}

	@Override
	public int executeUpdate(String arg0) throws SQLException {
		DBMSController executor = DBMSController.getInstance();
		String s = executor.invoke(arg0);
		String[] ss = s.split("\\s");
		return Integer.parseInt(ss[0]);

		 
	}

	@Override
	public int[] executeBatch() throws SQLException {
		int[] arr = new int[batches.size()];
		for (int i = 0; i < batches.size(); i++) {
			String query = batches.get(i);
			DBMSController executor = DBMSController.getInstance();
			String s = executor.invoke(query);
			if (execute(query)) arr[i] = SUCCESS_NO_INFO;
			// ":||||
			else arr[i] = executeUpdate(query);
		}
		return arr;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
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
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
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

	@Override
	public void setQueryTimeout(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

}