package eg.edu.alexu.csd.oop.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;



public class DriverImp implements Driver{
	private logger logger;
	private static final String URL_REGEX = "jdbc:(\\w+)db://localhost";
    private static final Pattern urlPattern = Pattern.compile(URL_REGEX);
    private final String INVALID_URL = "Invalid url Format";

    
    @Override
    public boolean acceptsURL(String arg0) throws SQLException {
    	
    	   Matcher urlMatcher = urlPattern.matcher(arg0);
    	   if (arg0==null) throw new SQLException("url is null");
           if (!urlMatcher.matches()) return false;
        return true;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
    	logger=logger.getInstance();
		if (!acceptsURL(url)) {
			logger.log.warning("not accepted url");
		throw new SQLException("not accepted url");
		}
        File path1 = (File) info.get("path");
        String path = path1.getAbsolutePath();
      
        return new ConnectionImp(url, path);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
            throws SQLException {
        DriverPropertyInfo propertyInfos[] = new DriverPropertyInfo[info
                .keySet().size()];
        Iterator<Object> itr = info.keySet().iterator();
        int counter = 0;
        while (itr.hasNext()) {
            String str = (String) itr.next();
            propertyInfos[counter++] = new DriverPropertyInfo(str, info
                    .getProperty(str));
        }
        return propertyInfos;
    }

  

	@Override
	public int getMajorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMinorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean jdbcCompliant() {
		throw new UnsupportedOperationException();
	}

}
