package com.pharmacy.traning.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Besarab Victor
 * The type Product.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
//@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "product_name", length = 100, nullable = false, unique = true)
    private String name;
    @Column(name = "product_dosage")
    private Double dosage;
    @Column(name = "product_manufacture", length = 40, nullable = false)
    private String manufactureCountry;
    @Column(name = "product_quantity")
    private Integer quantity;
    @Column(name = "product_price")
    private Double price;
    @Column(name = "product_date_of_delivery")
    private LocalDate dateOfDelivery;
    @Column(name = "product_measure", length = 20, nullable = false)
    private String measure;

    @OneToMany(mappedBy = "product")
    private List<Order> orderList = new ArrayList<>();

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets dosage.
     *
     * @return the dosage
     */
    public Double getDosage() {
        return dosage;
    }

    /**
     * Sets dosage.
     *
     * @param dosage the dosage
     */
    public void setDosage(Double dosage) {
        this.dosage = dosage;
    }

    /**
     * Gets manufacture country.
     *
     * @return the manufacture country
     */
    public String getManufactureCountry() {
        return manufactureCountry;
    }

    /**
     * Sets manufacture country.
     *
     * @param manufactureCountry the manufacture country
     */
    public void setManufactureCountry(String manufactureCountry) {
        this.manufactureCountry = manufactureCountry;
    }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets quantity.
     *
     * @param quantity the quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Gets date of delivery.
     *
     * @return the date of delivery
     */
    public LocalDate getDateOfDelivery() {
        return dateOfDelivery;
    }

    /**
     * Sets date of delivery.
     *
     * @param dateOfDelivery the date of delivery
     */
    public void setDateOfDelivery(LocalDate dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    /**
     * Gets measure.
     *
     * @return the measure
     */
    public String getMeasure() {
        return measure;
    }

    /**
     * Sets measure.
     *
     * @param measure the measure
     */
    public void setMeasure(String measure) {
        this.measure = measure;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Product product = (Product) o;
        return id.compareTo(product.id) == 0 && product.dosage.compareTo(dosage) == 0 &&
                manufactureCountry == null ? product.manufactureCountry == null : manufactureCountry.equals(product.manufactureCountry) &&
                quantity.compareTo(product.quantity) == 0 && (product.price.compareTo(price) == 0 &&
                measure == null ? product.measure == null : measure.equals(product.measure)  &&
                name == null ? product.name == null : name.equals(product.name) &&
                dateOfDelivery == null ? product.dateOfDelivery == null : dateOfDelivery.equals(product.dateOfDelivery);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(quantity) + Long.hashCode(id) +
                Arrays.hashCode( new BigDecimal[] {dosage, price}) + name.hashCode() + dateOfDelivery.hashCode() +
                measure.hashCode() + manufactureCountry.hashCode();
    }

    @Override
    public String toString() {
        return new String(new StringBuffer().append("Product{")
                .append("id=").append(id)
                .append(", name='").append(name)
                .append(", dosage=").append(dosage)
                .append(", manufactureId=").append(manufactureCountry)
                .append(", quantity=").append(quantity)
                .append(", price=").append(price)
                .append(", dateOfDelivery=").append(dateOfDelivery.toString())
                .append(", measure=").append(measure)
                .append('}'));
    }*/

    /**
     * The type Product builder.
     */
    public static class ProductBuilder{

        private long id;
        private String name;
        private double dosage;
        private String manufactureCountry;
        private int quantity;
        private double price;
        private LocalDate dateOfDelivery;
        private String measure;


        public ProductBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder setDosage(double dosage) {
            this.dosage = dosage;
            return this;
        }

        public ProductBuilder setManufactureCountry(String manufactureCountry) {
            this.manufactureCountry = manufactureCountry;
            return this;
        }

        public ProductBuilder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ProductBuilder setPrice(double price) {
            this.price = price;
            return this;
        }

        public ProductBuilder setDateOfDelivery(LocalDate dateOfDelivery) {
            this.dateOfDelivery = dateOfDelivery;
            return this;
        }

        public ProductBuilder setMeasure(String measure) {
            this.measure = measure;
            return this;
        }

        public Product createProduct() {
            return new Product(id, name, dosage, manufactureCountry, quantity, price, dateOfDelivery, measure, Collections.emptyList());
        }
    }
}
