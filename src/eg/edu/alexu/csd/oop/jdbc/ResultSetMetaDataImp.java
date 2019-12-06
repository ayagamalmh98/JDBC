package eg.edu.alexu.csd.oop.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import eg.edu.alexu.csd.oop.db.DataCarrier;

public class ResultSetMetaDataImp implements ResultSetMetaData {
	private DataCarrier columnsInfo;
	private Object[][] productedData;
	private String tableName;

	public ResultSetMetaDataImp(Object[][] productedData, DataCarrier columnsInfo, String tableName) {
		this.productedData = productedData;
		this.columnsInfo = columnsInfo;
		this.tableName = tableName;
	}

	@Override
	public int getColumnCount() throws SQLException {
		if(productedData.length == 0) {
			return 0;	
		}else {
			return productedData[0].length;
		}

	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSearchable(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int isNullable(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		if(columnsInfo.values[column]==null)
		    return columnsInfo.columns[column];
		else
		    return columnsInfo.values[column];
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		String[] names = columnsInfo.columns;
		return names[column];
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getScale(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTableName(int column) throws SQLException {
		return tableName;
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		String[] types = columnsInfo.columnsTypes;
		if (types[column].equalsIgnoreCase("string"))
			return 12;
		else if (types[column].equalsIgnoreCase("int"))
			return 4;
		else
			return 0;
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWritable(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}
}