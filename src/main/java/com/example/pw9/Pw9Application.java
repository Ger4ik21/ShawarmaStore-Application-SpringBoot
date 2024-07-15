package com.example.pw9;

import com.example.pw9.model.Ingredient;
import com.example.pw9.repos.IngredientRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Pw9Application {

    @Bean
    public CommandLineRunner dataLoader(@Qualifier("ingredientRepository") IngredientRepository repo) {
        return args -> {
            repo.save(new com.example.pw9.model.Ingredient("F", "Мучная лепешка", Ingredient.Type.WRAP));
            repo.save(new Ingredient("C", "Кукурузная лепешка", Ingredient.Type.WRAP));
            repo.save(new Ingredient("H", "Ветчина", Ingredient.Type.PROTEIN));
            repo.save(new Ingredient("B", "Буженина", Ingredient.Type.PROTEIN));
            repo.save(new Ingredient("T", "Помидоры", Ingredient.Type.VEGGIES));
            repo.save(new Ingredient("L", "Салат-латук", Ingredient.Type.VEGGIES));
            repo.save(new Ingredient("SC", "Колбасный сыр", Ingredient.Type.CHEESE));
            repo.save(new Ingredient("PC", "Плавленый сыр", Ingredient.Type.CHEESE));
            repo.save(new Ingredient("M", "Майонез", Ingredient.Type.SAUCE));
            repo.save(new Ingredient("A", "Аджика", Ingredient.Type.SAUCE));
        };
    }


    public static void main(String[] args) {
        SpringApplication.run(Pw9Application.class, args);
    }

}

//21497862