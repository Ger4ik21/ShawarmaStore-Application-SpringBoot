package com.example.pw9.controller;


import com.example.pw9.model.Ingredient;
import com.example.pw9.repos.IngredientRepository;
import com.example.pw9.model.Shawarma;
import com.example.pw9.model.ShawarmaOrder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import com.example.pw9.model.Ingredient.Type;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("shawarmaOrder")
public class DesignShawarmaController {
    private final IngredientRepository ingredientRepo;
    @Autowired
    public DesignShawarmaController(
            @Qualifier("ingredientRepository") IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }
    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();
        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType((List<Ingredient>) ingredients, type));
        }
    }
    @ModelAttribute(name = "shawarmaOrder")
    public Shawarma order() {
        return new Shawarma();
    }
    @ModelAttribute(name = "shawarma")
    public Shawarma shawarma() {
        return new Shawarma();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        model.addAttribute("shawarmaOrder", new ShawarmaOrder());
        return "design";
    }
    private Iterable<Ingredient> filterByType(
            Iterable<Ingredient> ingredients, Ingredient.Type type) {
        return StreamSupport.stream(ingredients.spliterator(), false)
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

    @PostMapping
    public String processShawarma(@Valid Shawarma shawarma, Errors errors,
                                  @ModelAttribute ShawarmaOrder shawarmaOrder) {
        if (errors.hasErrors()) {
            return "design";
        }
        shawarmaOrder.addShawarma(shawarma);
        log.info("Processing shawarma: {}", shawarma);
        return "redirect:/orders/current";
    }


}
