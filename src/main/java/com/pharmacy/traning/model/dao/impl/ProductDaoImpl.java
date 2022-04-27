package com.pharmacy.traning.model.dao.impl;

import com.pharmacy.traning.model.entity.Product;
import com.pharmacy.traning.exception.DaoException;
import com.pharmacy.traning.model.dao.ProductDao;
import com.pharmacy.traning.model.pool.ConnectionPool;
import com.pharmacy.traning.model.pool.HibernateConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pharmacy.traning.model.dao.ColumnName.*;

/**
 * @author Besarab Victor
 * The type Product dao.
 */
public class ProductDaoImpl implements ProductDao {

    private static final Logger logger = LogManager.getLogger();
    private static  ProductDaoImpl instance;

    private static final String SQL_ADD_PRODUCT = """
            insert into product (product_name, product_dosage, product_manufacture, product_quantity,
            product_price, product_date_of_delivery, product_measure)
            values (?, ?, ?, ?, ?, ?, ?);""";

    private static final String SQL_FIND_ALL_PRODUCT = """
            select product_id, product_name, product_dosage, product_manufacture, product_quantity,
            product_price, product_date_of_delivery, product_measure
            from product
            where product_status = 'actual'
            order by product_name;""";

    private static final String SQL_SEARCH_ALL_PRODUCT_BY_NAME = """
            select product_id, product_name, product_dosage, product_manufacture, product_quantity,
            product_price, product_date_of_delivery, product_measure
            from product
            where product_status = 'actual' and product_name like ?
            order by product_name;""";

    private static final String SQL_FIND_ALL_DELETE_PRODUCT = """
            select product_id, product_name, product_dosage, product_manufacture, product_quantity,
            product_price, product_date_of_delivery, product_measure
            from product
            where product_status = 'delete'
            order by product_name;""";

    private static final String SQL_SEARCH_DELETE_PRODUCT_BY_NAME = """
            select product_id, product_name, product_dosage, product_manufacture, product_quantity,
            product_price, product_date_of_delivery, product_measure
            from product
            where product_status = 'delete' and product_name like ?
            order by product_name;""";

    private static final String SQL_DELETE_PRODUCT_BY_PRODUCT_ID = """
            update product
            set product_status = 'delete'
            where product_id = ?""";

    private static final String SQL_CHANGE_PRODUCT_BY_PRODUCT_ID = """
            update product
            set product_name = ?, product_dosage = ?, product_manufacture = ?,
            product_price = ?, product_date_of_delivery = ?, product_measure = ?
            where product_id = ?;""";

    private static final String SQL_ADD_PRODUCT_QUANTITY_BY_PRODUCT_ID = """
            update product
            set product_quantity = product_quantity + ?
            where product_id = ?;""";

    private static final String SQL_REDUCE_PRODUCT_QUANTITY_BY_PRODUCT_ID = """
            update product
            set product_quantity = product_quantity - ?
            where product_id = ?;""";
    private static final String SQL_FIND_PRODUCT_BY_ID = """
            select product_id, product_name, product_dosage, product_manufacture, product_quantity,
            product_price, product_date_of_delivery, product_measure
            from product
            where product_id = ?;""";

    private static final String SQL_REALLY_DELETE_PRODUCT_BY_ID = """
            delete from product
            where product_id = ?;""";

    private static final String SQL_RESTORE_PRODUCT_BY_ID = """
            update product
            set product_status = 'actual'
            where product_id = ?;""";

    /**
     * Get instance product dao.
     *
     * @return the product dao
     */
    public static ProductDaoImpl getInstance(){
        if (instance == null){
            instance = new ProductDaoImpl();
        }
        return instance;
    }

    private ProductDaoImpl(){

    }

    @Override
    public boolean addProduct(Product product) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ADD_PRODUCT)){
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getDosage());
            statement.setString(3, product.getManufactureCountry());
            statement.setInt(4, product.getQuantity());
            statement.setDouble(5, product.getPrice());
            statement.setDate(6, Date.valueOf(product.getDateOfDelivery()));
            statement.setString(7, product.getMeasure());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or addProduct method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or addProduct method is not available. " + e);
        }
    }

    @Override
    public List<Product> findAllProduct() throws DaoException {
        return findAllProductByScript(SQL_FIND_ALL_PRODUCT);
    }

    @Override
    public List<Product> searchProductByName(String name) throws DaoException {
        return searchProductByNameAndScript(name, SQL_SEARCH_ALL_PRODUCT_BY_NAME);
    }

    @Override
    public List<Product> findAllDeleteProduct() throws DaoException {
        return findAllProductByScript(SQL_FIND_ALL_DELETE_PRODUCT);
    }

    @Override
    public List<Product> searchDeleteProductByName(String name) throws DaoException {
        return searchProductByNameAndScript(name, SQL_SEARCH_DELETE_PRODUCT_BY_NAME);
    }

    private List<Product> searchProductByNameAndScript(String name, String script) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(script)){
            statement.setString(1, "%" + name + "%");
            try (ResultSet result = statement.executeQuery()) {
                List<Product> productList = new ArrayList<>();
                while (result.next()) {
                    productList.add(new Product.ProductBuilder()
                            .setId(result.getLong(PRODUCT_ID))
                            .setName(result.getString(PRODUCT_NAME))
                            .setQuantity(result.getInt(PRODUCT_QUANTITY))
                            .setDosage(result.getDouble(PRODUCT_DOSAGE))
                            .setMeasure(result.getString(PRODUCT_MEASURE))
                            .setPrice(result.getDouble(PRODUCT_PRICE))
                            .setDateOfDelivery(result.getDate(PRODUCT_DATE_OF_DELIVERY).toLocalDate())
                            .setManufactureCountry(result.getString(PRODUCT_MANUFACTURE))
                            .createProduct());
                }
                return productList;
            }
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or searchProductByNameAndScript method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or searchProductByNameAndScript method is not available. " + e);
        }
    }
    private List<Product> findAllProductByScript(String script) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(script);
             ResultSet result = statement.executeQuery()){
            List<Product> productList = new ArrayList<>();
            while (result.next()){
                productList.add(new Product.ProductBuilder()
                        .setId(result.getLong(PRODUCT_ID))
                        .setName(result.getString(PRODUCT_NAME))
                        .setQuantity(result.getInt(PRODUCT_QUANTITY))
                        .setDosage(result.getDouble(PRODUCT_DOSAGE))
                        .setMeasure(result.getString(PRODUCT_MEASURE))
                        .setPrice(result.getDouble(PRODUCT_PRICE))
                        .setDateOfDelivery(result.getDate(PRODUCT_DATE_OF_DELIVERY).toLocalDate())
                        .setManufactureCountry(result.getString(PRODUCT_MANUFACTURE))
                        .createProduct());
            }
            return productList;
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or findAllProductByScript method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or findAllProductByScript method is not available. " + e);
        }
    }

    @Override
    public boolean deleteProductById(long id) throws DaoException {
        return executeScriptById(SQL_DELETE_PRODUCT_BY_PRODUCT_ID, id);
    }

    @Override
    public boolean restoreProductById(long id) throws DaoException {
        return executeScriptById(SQL_RESTORE_PRODUCT_BY_ID, id);
    }

    @Override
    public boolean reallyDeleteProductById(long id) throws DaoException {
        return executeScriptById(SQL_REALLY_DELETE_PRODUCT_BY_ID, id);
    }

    private boolean executeScriptById(String script, long id) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(script)){
            statement.setLong(1, id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or executeScriptById method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or executeScriptById method is not available. " + e);
        }
    }


    @Override
    public boolean changeProductByProductId(Product product) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_CHANGE_PRODUCT_BY_PRODUCT_ID)){
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getDosage());
            statement.setString(3, product.getManufactureCountry());
            statement.setDouble(4, product.getPrice());
            statement.setDate(5, Date.valueOf(product.getDateOfDelivery()));
            statement.setString(6, product.getMeasure());
            statement.setLong(7, product.getId());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or changeProductByProductId method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or changeProductByProductId method is not available. " + e);
        }
    }

    @Override
    public boolean addProductQuantityByProductId(int productQuantity, long productId) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ADD_PRODUCT_QUANTITY_BY_PRODUCT_ID)){
            statement.setInt(1, productQuantity);
            statement.setLong(2, productId);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or addProductQuantityByProductId method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or addProductQuantityByProductId method is not available. " + e);
        }
    }

    @Override
    public boolean reduceProductQuantityByProductId(int productQuantity, long productId, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_REDUCE_PRODUCT_QUANTITY_BY_PRODUCT_ID)){
            statement.setInt(1, productQuantity);
            statement.setLong(2, productId);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or reduceProductQuantityByProductId method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or reduceProductQuantityByProductId method is not available. " + e);
        }
    }

    @Override
    public Optional<Product> findProductById(long productId) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_PRODUCT_BY_ID)){
            statement.setLong(1, productId);
            try(ResultSet result = statement.executeQuery()){
                if (result.next()){
                    return Optional.of(new Product.ProductBuilder()
                            .setId(result.getLong(PRODUCT_ID))
                            .setName(result.getString(PRODUCT_NAME))
                            .setQuantity(result.getInt(PRODUCT_QUANTITY))
                            .setDosage(result.getDouble(PRODUCT_DOSAGE))
                            .setMeasure(result.getString(PRODUCT_MEASURE))
                            .setPrice(result.getDouble(PRODUCT_PRICE))
                            .setDateOfDelivery(result.getDate(PRODUCT_DATE_OF_DELIVERY).toLocalDate())
                            .setManufactureCountry(result.getString(PRODUCT_MANUFACTURE))
                            .createProduct());
                }
            }
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or findProductById method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or findProductById method is not available. " + e);
        }
        return Optional.empty();
    }
}
