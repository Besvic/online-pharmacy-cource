package com.pharmacy.traning.model.dao.impl;

import com.pharmacy.traning.model.entity.Order;
import com.pharmacy.traning.exception.DaoException;
import com.pharmacy.traning.model.dao.OrderDao;
import com.pharmacy.traning.model.entity.Pharmacy;
import com.pharmacy.traning.model.entity.Product;
import com.pharmacy.traning.model.entity.User;
import com.pharmacy.traning.model.pojo.OrderStatus;
import com.pharmacy.traning.model.pool.ConnectionPool;
import com.pharmacy.traning.model.pool.HibernateConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pharmacy.traning.model.dao.ColumnName.*;
import static com.pharmacy.traning.model.dao.ColumnName.PRODUCT_QUANTITY;

/**
 *
 * @author Besarab Victor
 *  The type Order dao.
 */

public class OrderDaoImpl implements OrderDao {

    private static final Logger logger = LogManager.getLogger();
    private static OrderDao instance;

    private static final String SQL_ADD_ORDER = """
            insert into `orders` (order_user_id, order_date, order_product_id, order_quantity)
            values (?, ?, ?, ?);""";


    private static final String SQL_CHANGE_QUANTITY_PRODUCT_IN_ORDER = """
            update `orders`
            set order_quantity = ?
            where order_id = ?;""";

    private static final String SQL_IS_ORDER = """
            select order_id, product_quantity
            from `orders`
            join product on order_product_id = product_id
            where order_user_id = ? and order_product_id = ? and order_status = 'not completed';""";

    private static final String SQL_COMPLETED_ORDER = """
            update `orders`
            set order_status = 'completed', order_pharmacy_id = ?, order_date = ?
            where order_id = ?;""";


    private static final String SQL_FIND_ORDER_BY_ID = """
           select order_id, user_id, user_cash,  order_quantity, product_price * order_quantity as product_price, product_id, product_quantity
           from `orders`
           join product on product_id = `orders`.order_product_id
           join users on user_id = `orders`.order_user_id
           where order_id = ?;""";

    private static final String SQL_FIND_ALL_ORDER_BY_USER_ID = """
           select order_id, order_quantity, order_quantity * pr.product_price as product_price, product_id, product_name
           from `orders`
           join product pr on pr.product_id = `orders`.order_product_id
           where order_user_id = ? and order_status = 'not completed' and product_status = 'actual'
           order by product_name;""";

    private static final String SQL_FIND_ALL_COMPLETED_ORDER_BY_USER_ID = """
            select order_id, order_date, user_cash, user_name, user_login, order_quantity, product_name,
            product_price * order_quantity as product_price, pharmacy_city, pharmacy_street, pharmacy_number
            from `orders`
            join pharmacy on pharmacy_id = `orders`.order_pharmacy_id
            join product on product_id = `orders`.order_product_id
            join users on user_id = `orders`.order_user_id
            where user_id = ? and order_date = ? and order_status = 'completed'
            order by order_date desc, product_price desc;""";

    private static final String SQL_FIND_ALL_COMPLETED_ORDER = """
            select order_date, user_cash, user_name, sum(order_quantity) as order_quantity, sum(product_price * order_quantity) as product_price, user_id
            from `orders`
            join pharmacy on pharmacy_id = `orders`.order_pharmacy_id
            join product on product_id = `orders`.order_product_id
            join users on user_id = `orders`.order_user_id
            where order_status = 'completed'
            group by order_date, user_id
            order by order_date desc, product_price desc;""";

    private static final String SQL_SEARCH_ORDER_BY_NAME = """
             select order_date, user_cash, user_name, sum(order_quantity) as order_quantity, sum(product_price * order_quantity) as product_price, user_id
            from `orders`
            join pharmacy on pharmacy_id = `orders`.order_pharmacy_id
            join product on product_id = `orders`.order_product_id
            join users on user_id = `orders`.order_user_id
            where order_status = 'completed' and user_name like ?
            group by order_date, user_id
            order by order_date desc, product_price desc;""";

    private static final String SQL_DELETE_ORDER_BY_ID = """
            delete from `orders`
            where order_id = ?;""";


    /**
     * Get instance order dao.
     *
     * @return the order dao
     */
    public static OrderDao getInstance(){
        if (instance == null){
            instance = new OrderDaoImpl();
        }
        return instance;
    }

    private OrderDaoImpl(){

    }

    @Override
    public boolean addOrder(Order order) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ADD_ORDER)){
            statement.setLong(1, order.getUser().getId());
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.setLong(3, order.getProduct().getId());
            statement.setInt(4, order.getQuantity());
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or addOrder method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or addOrder method is not available. " + e);
        }
    }

    @Override
    public boolean addProductQuantityInOrder(long orderId, int quantity) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_CHANGE_QUANTITY_PRODUCT_IN_ORDER)){
            statement.setInt(1, quantity);
            statement.setLong(2, orderId);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or addProductQuantityInOrder method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or addProductQuantityInOrder method is not available. " + e);
        }
    }

    @Override
    public boolean deleteOrderById(long orderId) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ORDER_BY_ID)){
            statement.setLong(1, orderId);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or deleteOrderById method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or deleteOrderById method is not available. " + e);
        }
    }

    @Override
    public boolean completedOrder(long orderId, long pharmacyId, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_COMPLETED_ORDER)){
            statement.setLong(1, pharmacyId);
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.setLong(3, orderId);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or completedOrder method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or completedOrder method is not available. " + e);
        }
    }


    @Override
    public Optional<Order> checkIsOrder(long userId, long productId) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_IS_ORDER)){
            statement.setLong(1, userId);
            statement.setLong(2, productId);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    return Optional.ofNullable(new Order.OrderBuilder()
                            .setId(result.getLong(ORDER_ID))
                            .setProduct(new Product.ProductBuilder()
                                    .setQuantity(result.getInt(PRODUCT_QUANTITY))
                                    .createProduct())
                            .createOrder());
                }
            }
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or checkIsOrder method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or checkIsOrder method is not available. " + e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findAllNotCompletedOrderById(long userId) throws DaoException {
        List<Order> orderList = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_ORDER_BY_USER_ID)){
            statement.setLong(1, userId);
            try (ResultSet result = statement.executeQuery()){
                while (result.next()){
                    orderList.add(new Order.OrderBuilder()
                            .setId(result.getLong(ORDER_ID))
                            .setProduct(new Product.ProductBuilder()
                                    .setPrice(result.getDouble(PRODUCT_PRICE))
                                    .setName(result.getString(PRODUCT_NAME))
                                    .setId(result.getLong(PRODUCT_ID))
                                    .createProduct())
                            .setQuantity(result.getInt(ORDER_QUANTITY))
                            .createOrder());
                }
            }
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or findAllNotCompletedOrderById method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or findAllNotCompletedOrderById method is not available. " + e);
        }
        return orderList;
    }

    @Override
    public List<Order> findAllCompletedOrder() throws DaoException {
        List<Order> orderList = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_COMPLETED_ORDER);
             ResultSet result = statement.executeQuery() ){
            while (result.next()){
                orderList.add(new Order.OrderBuilder()
                        .setDate(result.getDate(ORDER_DATE).toLocalDate())
                        .setUser(new User.UserBuilder()
                                .setCash(result.getDouble(USER_CASH))
                                .setName(result.getString(USER_NAME))
                                .setId(result.getLong(USER_ID))
                                .createUser())
                        .setProduct(new Product.ProductBuilder()
                                .setPrice(result.getDouble(PRODUCT_PRICE))
                                .createProduct())
                        .setQuantity(result.getInt(ORDER_QUANTITY))
                        .createOrder());

            }
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or findAllCompletedOrder method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or findAllCompletedOrder method is not available. " + e);
        }
        return orderList;
    }

    @Override
    public List<Order> findAllCompletedOrderByUserId(long userId, LocalDate date) throws DaoException {
        List<Order> orderList = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_COMPLETED_ORDER_BY_USER_ID)){
            statement.setLong(1, userId);
            statement.setDate(2, Date.valueOf(date));
            try(ResultSet result = statement.executeQuery() ){
                while (result.next()) {
                    orderList.add(new Order.OrderBuilder()
                            .setId(result.getLong(ORDER_ID))
                            .setDate(result.getDate(ORDER_DATE).toLocalDate())
                            .setUser(new User.UserBuilder()
                                    .setCash(result.getDouble(USER_CASH))
                                    .setName(result.getString(USER_NAME))
                                    .setLogin(result.getString(USER_LOGIN))
                                    .createUser())
                            .setProduct(new Product.ProductBuilder()
                                    .setPrice(result.getDouble(PRODUCT_PRICE))
                                    .setName(result.getString(PRODUCT_NAME))
                                    .createProduct())
                            .setPharmacy(new Pharmacy.PharmacyBuilder()
                                    .setCity(result.getString(PHARMACY_CITY))
                                    .setStreet(result.getString(PHARMACY_STREET))
                                    .setNumber(result.getInt(PHARMACY_NUMBER))
                                    .createPharmacy())
                            .setQuantity(result.getInt(ORDER_QUANTITY))
                            .createOrder());
                }
            }
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or findAllCompletedOrderByUserId method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or findAllCompletedOrderByUserId method is not available. " + e);
        }
        return orderList;
    }

    @Override
    public List<Order> searchOrderByName(String name) throws DaoException {
        List<Order> orderList = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SEARCH_ORDER_BY_NAME)){
            statement.setString(1, "%" + name + "%");
            try(ResultSet result = statement.executeQuery() ){
                while (result.next()) {
                    orderList.add(new Order.OrderBuilder()
                            .setDate(result.getDate(ORDER_DATE).toLocalDate())
                            .setUser(new User.UserBuilder()
                                    .setCash(result.getDouble(USER_CASH))
                                    .setName(result.getString(USER_NAME))
                                    .setId(result.getLong(USER_ID))
                                    .createUser())
                            .setProduct(new Product.ProductBuilder()
                                    .setPrice(result.getDouble(PRODUCT_PRICE))
                                    .createProduct())
                            .setQuantity(result.getInt(ORDER_QUANTITY))
                            .createOrder());
                }
            }
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or searchOrderByName method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or searchOrderByName method is not available. " + e);
        }
        return orderList;
    }

    @Override
    public Optional<Order> findOrderById(long orderId, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ORDER_BY_ID)){
            statement.setLong(1, orderId);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    return Optional.ofNullable(new Order.OrderBuilder()
                            .setId(result.getLong(ORDER_ID))
                            .setUser(new User.UserBuilder()
                                    .setId(result.getLong(USER_ID))
                                    .setCash(result.getDouble(USER_CASH))
                                    .createUser())
                            .setQuantity(result.getInt(ORDER_QUANTITY))
                            .setProduct( new Product.ProductBuilder()
                                    .setPrice(result.getDouble(PRODUCT_PRICE))
                                    .setId(result.getInt(PRODUCT_ID))
                                    .setQuantity(result.getInt(PRODUCT_QUANTITY))
                                    .createProduct())
                            .createOrder());
                }
            }
        } catch (SQLException e) {
            logger.error("PrepareStatement didn't connection or findOrderById method is not available. " + e);
            throw new DaoException("PrepareStatement didn't connection or findOrderById method is not available. " + e);
        }
        return Optional.empty();
    }
}
