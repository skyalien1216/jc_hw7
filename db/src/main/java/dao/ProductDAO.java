package dao;

import generated.tables.pojos.Product;
import org.java_courses.CREDS;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static generated.tables.Product.PRODUCT;


public final class ProductDAO{

    public @NotNull Product get(int id) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(
                            PRODUCT.ID,
                            PRODUCT.NAME,
                            PRODUCT.ORGANIZATION,
                            PRODUCT.AMOUNT
                    )
                    .from(PRODUCT).where(PRODUCT.ID.eq(id));

            var rec = query.fetchOne();

            if (rec == null)
                throw new IllegalStateException("Product with id " + id + " not found");

            return new Product(rec.value1(), rec.value2(), rec.value4(), rec.value3());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ProductDAO::GET");
        }
    }

    public @NotNull List<Product> all() {
        final List<Product> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(
                            PRODUCT.ID,
                            PRODUCT.NAME,
                            PRODUCT.ORGANIZATION,
                            PRODUCT.AMOUNT
                    )
                    .from(PRODUCT);

            var records = query.fetch();

            for (var rec : records) {
                result.add(new Product(rec.value1(), rec.value2(), rec.value4(), rec.value3()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Product::GET");
        }
        return result;
    }

    public void save(@NotNull Product entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .insertInto(PRODUCT, PRODUCT.AMOUNT, PRODUCT.NAME, PRODUCT.ORGANIZATION)
                    .values(entity.getAmount(), entity.getName(), entity.getOrganization())
                    .execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Product::SAVE");
        }
    }

    public void update(@NotNull Product entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .update(PRODUCT)
                    .set(
                            PRODUCT.AMOUNT, entity.getAmount()
                    )
                    .set(
                            PRODUCT.ORGANIZATION, entity.getOrganization()
                    )
                    .set(
                            PRODUCT.NAME, entity.getName()
                    )
                    .where(PRODUCT.ID.eq(entity.getId()))
                    .execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ProductDAO::UPDATE");
        }
    }

    public void delete(@NotNull Product entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .delete(PRODUCT)
                    .where(PRODUCT.ID.eq(entity.getId()))
                    .execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ProductDAO::DELETE");
        }
    }
}

