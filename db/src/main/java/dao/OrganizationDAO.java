package dao;

import generated.tables.pojos.Organization;
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

import static generated.Tables.ORGANIZATION;


public final class OrganizationDAO {

    public @NotNull Organization get(String id) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(ORGANIZATION.NAME)
                    .from(ORGANIZATION).where(ORGANIZATION.NAME.eq(id));

            var rec = query.fetchOne();

            if (rec == null)
                throw new IllegalStateException("Organization with name " + id + " not found");

            return new Organization(rec.value1());
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::GET");
        }

    }

    public @NotNull List<Organization> all() {
        final List<Organization> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(ORGANIZATION.NAME)
                    .from(ORGANIZATION);

            var records = query.fetch();

            for (var rec: records) {
                result.add(new Organization(rec.value1()));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::ALL");
        }
        return result;
    }

    public Boolean dbHas(String name) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(ORGANIZATION.NAME)
                    .from(ORGANIZATION).where(ORGANIZATION.NAME.eq(name));

            var records = query.fetch();
            if (records.size() > 0)
                return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::DBHAS");
        }
        return false;
    }

    public void save(@NotNull Organization entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .insertInto(ORGANIZATION, ORGANIZATION.NAME)
                    .values(entity.getName())
                    .execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::SAVE");
        }
    }

    public void update(@NotNull Organization entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .update(ORGANIZATION)
                    .set(
                            ORGANIZATION.NAME, entity.getName()
                    )
                    .where(ORGANIZATION.NAME.eq(entity.getName()))
                    .execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::UPDATE");
        }
    }

    public void delete(@NotNull Organization entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .delete(ORGANIZATION)
                    .where(ORGANIZATION.NAME.eq(entity.getName()))
                    .execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::DELETE");
        }
    }
}
