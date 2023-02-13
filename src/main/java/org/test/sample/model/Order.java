package org.test.sample.model;

import org.bson.codecs.pojo.annotations.BsonId;

public class Order {
  @BsonId
  String pizza;
  Long totalQuantity;

  public String getPizza() {
    return pizza;
  }

  public void setPizza(String pizza) {
    this.pizza = pizza;
  }

  public Long getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(Long totalQuantity) {
    this.totalQuantity = totalQuantity;
  }
}
