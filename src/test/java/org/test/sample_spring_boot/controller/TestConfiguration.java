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

package org.test.sample_spring_boot.controller;

import org.mongopipe.core.config.MongoPipeConfig;
import org.mongopipe.spring.MongoPipeStarter;
import org.test.sample_spring_boot.MongoDBServerStarter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class TestConfiguration {

  @Bean
  public MongoDBServerStarter mongoDBStarter() {
    return new MongoDBServerStarter();
  }

  // Library configuration beans next.
  @Bean
  @DependsOn("mongoDBStarter")  // Needed to have a running MongoDB server. Not needed if server is already running.
  public MongoPipeConfig getMongoPipeConfig(MongoDBServerStarter mongoDBStarter) {
    return MongoPipeConfig.builder()
        .uri("mongodb://localhost:" + mongoDBStarter.getPort())
        .databaseName("test")
        .scanPackage("org.test")
        .build();
  }

  @Bean
  public MongoPipeStarter getMongoPipeStarter(MongoPipeConfig mongoPipeConfig) {
    return new MongoPipeStarter(mongoPipeConfig);
  }
}
