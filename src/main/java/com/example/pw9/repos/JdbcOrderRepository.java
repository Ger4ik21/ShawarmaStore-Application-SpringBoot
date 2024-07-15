package com.example.pw9.repos;

import com.example.pw9.model.Ingredient;
import com.example.pw9.model.IngredientRef;
import com.example.pw9.model.Shawarma;
import com.example.pw9.model.ShawarmaOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.asm.Type;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public abstract class JdbcOrderRepository implements OrderRepository {
    private JdbcOperations jdbcOperations;
    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
    @Override
    @Transactional
    public ShawarmaOrder save(ShawarmaOrder order) {
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into Shawarma_Order "
                                + "(delivery_name, delivery_street, delivery_city, "
                                + "delivery_state, delivery_zip, cc_number, "
                                + "cc_expiration, cc_cvv, placed_at) "
                                + "values (?,?,?,?,?,?,?,?,?)",
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
                );
        pscf.setReturnGeneratedKeys(true);
        order.setPlacedAt(new Date());
        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                order.getDeliveryName(),
                                order.getDeliveryStreet(),
                                order.getDeliveryCity(),
                                order.getDeliveryState(),
                                order.getDeliveryZip(),
                                order.getCcNumber(),
                                order.getCcExpiration(),
                                order.getCcCVV(),
                                order.getPlacedAt()));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);
        List<Shawarma> shawarmas = order.getShawarmas();
        int i=0;
        for (Shawarma shawarma : shawarmas) {
            saveShawarma(orderId, i++, shawarma);
        }
        return order;
    }



    private long saveShawarma(Long orderId, int orderKey, Shawarma shawarma) {
        shawarma.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into Shawarma "
                                + "(name, created_at, shawarma_order, shawarma_order_key) "
                                + "values (?, ?, ?, ?)",
                        Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
                );
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                shawarma.getName(),
                                shawarma.getCreatedAt(),
                                orderId,
                                orderKey));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long shawarmaId = keyHolder.getKey().longValue();
        shawarma.setId(shawarmaId);
        saveIngredientRefs(shawarmaId, shawarma.getIngredients());
        return shawarmaId;
    }
    private void saveIngredientRefs(
            long shawarmaId,  List<Ingredient> ingredientRefs) {
        int key = 0;
        for (Ingredient ingredientRef : ingredientRefs) {
            jdbcOperations.update(
                    "insert into Ingredient_Ref (ingredient, shawarma, shawarma_key) "
                            + "values (?, ?, ?)",
                    ingredientRef.getIngredient(), shawarmaId, key++);
        }
    }


    public Optional<ShawarmaOrder> findById(Long id) {
        try {
            ShawarmaOrder order = jdbcOperations.queryForObject(
                    "select id, delivery_name, delivery_street, delivery_city, "
                            + "delivery_state, delivery_zip, cc_number, cc_expiration, "
                            + "cc_cvv, placed_at from Shawarma_Order where id=?",
                    (row, rowNum) -> {
                        ShawarmaOrder shawarmaOrder = new ShawarmaOrder();
                        shawarmaOrder.setId(row.getLong("id"));
                        shawarmaOrder.setDeliveryName(row.getString("delivery_name"));
                        shawarmaOrder.setDeliveryStreet(row.getString("delivery_street"));
                        shawarmaOrder.setDeliveryCity(row.getString("delivery_city"));
                        shawarmaOrder.setDeliveryState(row.getString("delivery_state"));
                        shawarmaOrder.setDeliveryZip(row.getString("delivery_zip"));
                        shawarmaOrder.setCcNumber(row.getString("cc_number"));
                        shawarmaOrder.setCcExpiration(row.getString("cc_expiration"));
                        shawarmaOrder.setCcCVV(row.getString("cc_cvv"));
                        shawarmaOrder.setPlacedAt(new Date(row.getTimestamp("placed_at").getTime()));
                        shawarmaOrder.setShawarmas(findShawarmasByOrderId(row.getLong("id")));
                        return shawarmaOrder;
                    }, id);
            return Optional.of(order);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }



    private List<Shawarma> findShawarmasByOrderId(long orderId) {
        return jdbcOperations.query(
                "select id, name, created_at from Shawarma "
                        + "where shawarma_order=? order by shawarma_order_key",
                (row, rowNum) -> {
                    Shawarma shawarma = new Shawarma();
                    shawarma.setId(row.getLong("id"));
                    shawarma.setName(row.getString("name"));
                    shawarma.setCreatedAt(new Date(row.getTimestamp("created_at").getTime()));
                    shawarma.setIngredients(findIngredientsByShawarmaId(row.getLong("id")));
                    return shawarma;
                },
                orderId);
    }
    private List<IngredientRef> findIngredientsByShawarmaId(long shawarmaId) {
        return jdbcOperations.query(
                "select ingredient from Ingredient_Ref "
                        + "where shawarma = ? order by shawarma_key",
                (row, rowNum) -> {
                    return new IngredientRef(row.getString("ingredient"));
                },
                shawarmaId);
    }
}
