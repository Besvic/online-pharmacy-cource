package com.pharmacy.traning.model.entity;

import com.pharmacy.traning.model.pojo.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * @author Besarab Victor
 * The type Order.
 */
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
//@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_date")
    private LocalDate date;
    @Column(name = "order_quantity")
    private int quantity;

    @ManyToOne()
    @JoinColumn(name = "order_product_id")
    private Product product;

    @ManyToOne()
    @JoinColumn(name = "order_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_pharmacy_id")
    private Pharmacy pharmacy;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "order_status")
    private OrderStatus status;

    /**
     * Gets id.
     *
     * @return the id
     *//*
    public long getId() {
        return id;
    }

    *//**
     * Sets id.
     *
     * @param id the id
     *//*
    public void setId(long id) {
        this.id = id;
    }

    *//**
     * Gets product.
     *
     * @return the product
     *//*
    public Product getProduct() {
        return product;
    }

    *//**
     * Sets product.
     *
     * @param product the product
     *//*
    public void setProduct(Product product) {
        this.product = product;
    }

    *//**
     * Gets user.
     *
     * @return the user
     *//*
    public User getUser() {
        return user;
    }

    *//**
     * Sets user.
     *
     * @param user the user
     *//*
    public void setUser(User user) {
        this.user = user;
    }

    *//**
     * Gets pharmacy.
     *
     * @return the pharmacy
     *//*
    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    *//**
     * Sets pharmacy.
     *
     * @param pharmacy the pharmacy
     *//*
    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    *//**
     * Gets status.
     *
     * @return the status
     *//*
    public OrderStatus getStatus() {
        return status;
    }

    *//**
     * Sets status.
     *
     * @param status the status
     *//*
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    *//**
     * Gets date.
     *
     * @return the date
     *//*
    public LocalDate getDate() {
        return date;
    }

    *//**
     * Sets date.
     *
     * @param date the date
     *//*
    public void setDate(LocalDate date) {
        this.date = date;
    }

    *//**
     * Gets quantity.
     *
     * @return the quantity
     *//*
    public int getQuantity() {
        return quantity;
    }

    *//**
     * Sets quantity.
     *
     * @param quantity the quantity
     *//*
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
*/
   /* @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Order order = (Order) o;
        return id == order.id && quantity == order.quantity && product == null ? order.product == null : product.equals(order.product) &&
                user == null ? order.user == null : user.equals(order.user) &&
                pharmacy == null ? order.pharmacy == null : pharmacy.equals(order.pharmacy) &&
                status == null ? order.status == null : status.equals(order.status) &&
                date == null ? order.date == null : date.equals(order.date);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id) + product.hashCode() + user.hashCode() + pharmacy.hashCode() +
                status.hashCode() + date.hashCode() +  Integer.hashCode(quantity);
    }

    @Override
    public String toString() {
        return new String(new StringBuilder()
                .append("Order{")
                .append("id=").append(id)
                .append(", product=").append(product.toString())
                .append(", user=").append(user.toString())
                .append(", pharmacy=").append(pharmacy.toString())
                .append(", status=").append(status)
                .append(", date=").append(date)
                .append(", quantity=").append(quantity)
                .append('}'));
    }
*/
    /**
     * The type Order builder.
     */
    public static class OrderBuilder{

        private Long id;
        private Product product;
        private User user;
        private Pharmacy pharmacy;
        private OrderStatus status;
        private LocalDate date;
        private Integer quantity;

        public OrderBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder setProduct(Product product) {
            this.product = product;
            return this;
        }


        public OrderBuilder setUser(User user) {
            this.user = user;
            return this;
        }

        public OrderBuilder setPharmacy(Pharmacy pharmacy) {
            this.pharmacy = pharmacy;
            return this;
        }

        public OrderBuilder setStatus(OrderStatus status) {
            this.status = status;
            return this;
        }

        public OrderBuilder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public OrderBuilder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Order createOrder() {
            return new Order(id, date, quantity, product, user, pharmacy, status);
        }
    }

}
