package com.pharmacy.traning.model.entity;

import com.pharmacy.traning.model.pojo.PharmacyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Besarab Victor
 * The type Pharmacy.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
//@Builder
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacy_id")
    private Long id;
    @Column(name = "pharmacy_city", length = 50, nullable = false)
    private String city;
    @Column(name = "pharmacy_street", length = 50, nullable = false)
    private String street;
    @Column(name = "pharmacy_number", nullable = false)
    private Integer number;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "pharmacy_status")
    private PharmacyStatus status;

    @OneToMany(mappedBy = "pharmacy")
    private List<Order> basketList = new ArrayList<>();


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
     * Gets city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets city.
     *
     * @param city the city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets street.
     *
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets street.
     *
     * @param street the street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Gets number.
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets number.
     *
     * @param number the number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public PharmacyStatus getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(PharmacyStatus status) {
        this.status = status;
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pharmacy pharmacy = (Pharmacy) o;
        return id == pharmacy.id && number == pharmacy.number && city == null ? pharmacy.city == null : city.equals(pharmacy.city)
                && street == null ? pharmacy.street == null : street.equals(pharmacy.street)
                && status == null ? pharmacy.status == null : status.equals(pharmacy.status);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id) +  city.hashCode() + street.hashCode() + Integer.hashCode(number) + status.hashCode();
    }

    @Override
    public String toString() {
        return new String(new StringBuilder()
                .append("Pharmacy{ id=").append(id)
                .append(", city='").append(city)
                .append(", street='").append(street)
                .append(", number=").append(number)
                .append( ", status=").append(status)
                .append('}'));
    }*/


    public static class PharmacyBuilder{

        private Long id;
        private String city;
        private String street;
        private Integer number;
        private PharmacyStatus status;


        public PharmacyBuilder setId(long id) {
            this.id = id;
            return this;
        }


        public PharmacyBuilder setCity(String city) {
            this.city = city;
            return this;
        }

        public PharmacyBuilder setStreet(String street) {
            this.street = street;
            return this;
        }


        public PharmacyBuilder setNumber(int number) {
            this.number = number;
            return this;
        }


        public PharmacyBuilder setStatus(PharmacyStatus status) {
            this.status = status;
            return this;
        }


        public Pharmacy createPharmacy() {
            return new Pharmacy(id, city, street, number, status, Collections.emptyList());
        }
    }
}
