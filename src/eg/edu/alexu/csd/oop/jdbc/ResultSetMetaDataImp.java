package eg.edu.alexu.csd.oop.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
public class ResultSetMetaDataImp implements ResultSetMetaData {
    private String [][] columnsInfo;
    private Object [][] productedData;
    private String tableName;
    public ResultSetMetaDataImp ( Object [][] productedData, String [][] columnsInfo, String tableName  ) {
        this.productedData=productedData;
        this.columnsInfo=columnsInfo;
        this.tableName=tableName;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return  productedData[0].length;
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
        return null;
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return columnsInfo[0][column];
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
       if(columnsInfo[1][column].equalsIgnoreCase("string"))
           return 12;
       else
           return 4;
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
