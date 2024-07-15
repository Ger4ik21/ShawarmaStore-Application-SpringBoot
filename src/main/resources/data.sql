delete from Ingredient_Ref;
delete from Shawarma;
delete from Shawarma_Order;
delete from Ingredient;
insert into Ingredient (id, name, type)
values ('F', 'Мучная лепешка', 'WRAP');
insert into Ingredient (id, name, type)
values ('C', 'Кукурузная лепешка', 'WRAP');
insert into Ingredient (id, name, type)
values ('H', 'Ветчина', 'PROTEIN');
insert into Ingredient (id, name, type)
values ('B', 'Буженина', 'PROTEIN');
insert into Ingredient (id, name, type)
values ('T', 'Помидоры', 'VEGGIES');
insert into Ingredient (id, name, type)
values ('L', 'Салат-латук', 'VEGGIES');
insert into Ingredient (id, name, type)
values ('SC', 'Колбасный сыр', 'CHEESE');
insert into Ingredient (id, name, type)
values ('PC', 'Плавленый сыр', 'CHEESE');
insert into Ingredient (id, name, type)
values ('M', 'Майонез', 'SAUCE');
insert into Ingredient (id, name, type)
values ('A', 'Аджика', 'SAUCE');