package pl.moja.bibloteka.database.dbuitls;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import pl.moja.bibloteka.database.models.Author;
import pl.moja.bibloteka.database.models.Book;
import pl.moja.bibloteka.database.models.Category;

import java.io.IOException;
import java.sql.SQLException;

public class DbManager {

    public static final Logger LOGGER = LoggerFactory.getLogger(DbManager.class);

    private static final String JDBC_DRIVER_HD = "jdbc:h2:./libraryDB";
    private static final String USER = "admin";
    private static final String PASS = "admin";


    private static ConnectionSource connectionSource;

    // metoda inicjalizacji bazy danych - narazie dla testów nie na produkcje
    public static void initDatabase() {
        createConnectionSource();
     //   dropTable();
        createTable();
        closeConnectionSource();
    }

    // metoda obsługi połączenia
    private static void createConnectionSource() {
        try {
            connectionSource = new JdbcConnectionSource(JDBC_DRIVER_HD, USER, PASS);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }
    // motoda pobrania połączenia publiczna statyczna "geter"
    public static ConnectionSource getConnectionSource() {
        if(connectionSource == null) {
            createConnectionSource();
        }
        return connectionSource;
    }

    // metoda zamykania połączenia
    public static void closeConnectionSource() {
        if(connectionSource!=null) {
            try {
                connectionSource.close();
            } catch (IOException e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }

    // metoda do tworzenia tabel
    private static void createTable() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Author.class);
            TableUtils.createTableIfNotExists(connectionSource, Book.class);
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    // metoda na usuwanie table
    private static void dropTable() {
        try {
            TableUtils.dropTable(connectionSource, Author.class, true);
            TableUtils.dropTable(connectionSource, Book.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }

}