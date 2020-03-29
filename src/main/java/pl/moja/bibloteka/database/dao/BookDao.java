package pl.moja.bibloteka.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import pl.moja.bibloteka.database.models.Book;
import pl.moja.bibloteka.utils.exceptions.ApplicationException;

import java.sql.SQLException;

public class BookDao extends CommonDao {

    // public BookDao(ConnectionSource connectionSource) {

    public BookDao() {

        super();
    }

    public void deleteByColumnName(String columnName, int id) throws ApplicationException, SQLException {
        Dao<Book, Object> dao = getDao(Book.class);
        DeleteBuilder<Book, Object> deleteBuilder = dao.deleteBuilder();
        deleteBuilder.where().eq(columnName, id);
        dao.delete(deleteBuilder.prepare());

    }
}

//
//    public List<String[]> queryRaw(String value) throws SQLException, ApplicationException {
//        GenericRawResults<String[]> where = getDao(Book.class).queryRaw("SELECT * FROM books WHERE title = 'Hobbit'");
//        return where.getResults();
//    }
//
//    public void queryWhere(String columnName, String value) throws SQLException, ApplicationException {
//        QueryBuilder<Book, Integer> queryBuilder = getQueryBuilder(Book.class);
//       queryBuilder.where().eq(columnName, value);
//        PreparedQuery<Book> prepare = queryBuilder.prepare();
//      //---------------------------------------------------------
//        List<Book> result = getDao(Book.class).query(prepare);
//       result.forEach(e->{
//            System.out.println();
//            System.out.println(e);
//            System.out.println();
//        });
//        //----------------------------------------------------------
//        // zwraca inaczej wynik - return getDao(Book.class).query(prepare);
//        //---------------------------------------------------------
//    }
//
