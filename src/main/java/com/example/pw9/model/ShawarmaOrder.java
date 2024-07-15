package com.example.pw9.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
public class ShawarmaOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date placedAt = new Date();
    @NotBlank(message="Укажите название")
    private String deliveryName;
    @NotBlank(message="Укажите улицу")
    private String deliveryStreet;
    @NotBlank(message="Укажите город")
    private String deliveryCity;
    @NotBlank(message="Укажите страну")
    private String deliveryState;
    @NotBlank(message="Укажите индекс")
    private String deliveryZip;
    @CreditCardNumber(message="Недействительный номер кредитной карты")
    private String ccNumber;
    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$",
            message="Срок дейсвия должен быть в формате MM/YY")
    private String ccExpiration;
    @Digits(integer=3, fraction=0, message="Недействительный код CVV")
    private String ccCVV;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Shawarma> shawarmas = new ArrayList<>();
    public void addShawarma(Shawarma shawarma) {
        this.shawarmas.add(shawarma);
    }
}
