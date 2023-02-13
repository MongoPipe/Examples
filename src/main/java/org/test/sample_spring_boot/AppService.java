/*
 * Copyright (c) 2022 - present Cristian Donoiu, Ionut Sergiu Peschir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.test.sample_spring_boot;

import org.mongopipe.core.logging.CustomLogFactory;
import org.mongopipe.core.logging.Log;
import org.mongopipe.core.migration.MigrationRunner;
import org.mongopipe.core.runner.PipelineRunner;
import org.mongopipe.core.store.PipelineStore;
import org.mongopipe.core.util.Maps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.test.sample.model.Pizza;
import org.test.sample.store.MyRestaurant;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

@Service
public class AppService implements InitializingBean {
  private static final Log LOG = CustomLogFactory.getLogger(MigrationRunner.class);

  @Autowired
  PipelineStore pipelineStore;

  @Autowired
  MyRestaurant myRestaurant;

  @Autowired
  PipelineRunner pipelineRunner;

  @Override
  public void afterPropertiesSet() throws Exception {
    LOG.info("Pipelines found:\n" +
        StreamSupport.stream(pipelineStore.findAll().spliterator(), false).collect(Collectors.toList()));
    assertEquals(Long.valueOf(2), pipelineStore.count());

    // Run a pipeline by using dedicated @Store.
    long total = myRestaurant.getPizzasBySize("small").count();
    assertEquals(2, total);

    // Run a pipeline dynamically using PipelineRunner.
    List<Pizza> smallPizzas = pipelineRunner.runAndList("matchingPizzas", Pizza.class, Maps.of("pizzaSize", "small"));
    assertEquals("small", smallPizzas.get(0).getSize());
  }
}
