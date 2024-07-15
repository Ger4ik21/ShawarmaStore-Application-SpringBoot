package com.example.pw9.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
public class Shawarma {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;
    private Date createdAt = new Date();
    @Size(min=1, message="You must choose at least 1 ingredient")
    @ManyToMany()
    private List<Ingredient> ingredients = new ArrayList<>();
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void setIngredients(List<IngredientRef> ingredientRefs) {
        for (IngredientRef ingredientRef : ingredientRefs){
            Ingredient ingredient = new Ingredient();
            ingredient.setId(ingredientRef.getIngredient());
            ingredients.add(ingredient);
        }
    }

    public List<Ingredient> getIngredients()
    {
        return ingredients;
    }
}
