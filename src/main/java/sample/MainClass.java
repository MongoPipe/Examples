package sample;

import org.mongopipe.core.Pipelines;
import org.mongopipe.core.Stores;
import org.mongopipe.core.config.MongoPipeConfig;
import org.mongopipe.core.model.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.model.Order;
import sample.model.Pizza;
import sample.store.MyRestaurant;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class MainClass {
  private static Logger LOG = LoggerFactory.getLogger(MainClass.class);

  public static void main(String args[]) throws InterruptedException {
    // 0. Seed some data in MongoDB from command shell:
    //    \>"C:/Program Files/MongoDB/Server/4.4/bin/mongo" < seed_data.js

    // 1. Config
    Stores.registerConfig(MongoPipeConfig.builder()
        .uri("mongodb://testUser:password@localhost:27017/?authMechanism=SCRAM-SHA-256&authSource=testDatabase")
        .databaseName("testDatabase")
        //.mongoClient(optionallyForCustomConnection)
        .build());

    // 2. Create POJOs and stores, e.g. see MyRestaurant.java

    // 3. Create in "resources/pipelines" folder all the pipelines bson files. Will be automatically migrated in pipeline_store collection.

    // 3.1. Execute migration to get the pipelines seeded in the database. Done automatically if you use mongopipe-spring dependency.
    Pipelines.startMigration();
    Iterable<Pipeline> iterable = Pipelines.getStore().findAll();  // "pipeline_store" collection.
    for (Pipeline pipelines : iterable) {
      System.out.println(pipelines.toString());
    }

    // Run stuff.
    long total = Stores.from(MyRestaurant.class)     // Note: You can save the returned store in a field or as a bean.
        .getPizzasBySize("small")
        .count();
    assertEquals(2, total);

    Optional<Pizza> pepperoni = Stores.from(MyRestaurant.class)
        .findById("PS");
    assertEquals("Pepperoni" , pepperoni.get().getName());

    Order pepperoniOrder = Stores.from(MyRestaurant.class)
        .getTotalOrders("medium")
        .filter(order -> order.getPizza().equals("Cheese"))
        .findFirst()
        .get();
    assertEquals(Long.valueOf(50), pepperoniOrder.getTotalQuantity());

  }
}
