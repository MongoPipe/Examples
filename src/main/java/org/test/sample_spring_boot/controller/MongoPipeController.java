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

import org.bson.Document;
import org.mongopipe.core.model.Pipeline;
import org.mongopipe.core.runner.PipelineRunner;
import org.mongopipe.core.store.PipelineStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.test.sample_spring_boot.controller.MongoPipeController.PATH;

@RestController
@RequestMapping(PATH)
@CrossOrigin
public class MongoPipeController {
  public static final String PATH = "/mongopipe";

  @Autowired
  PipelineStore pipelineStore;

  @Autowired
  PipelineRunner pipelineRunner;

  @GetMapping(value = "/pipelines", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Pipeline>> getAll() {
    List<Pipeline> pipelines = StreamSupport.stream(pipelineStore.findAll().spliterator(), false).collect(Collectors.toList());
    return ResponseEntity.ok(pipelines);
  }

  @GetMapping(value = "/pipeline/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Pipeline> getPipeline(@PathVariable String pipelineId) {
    return ResponseEntity.ok(pipelineStore.getPipeline(pipelineId));
  }

  @PostMapping(value = "/pipeline", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Pipeline> createPipeline(@RequestBody Pipeline pipeline) {
    pipeline = pipelineStore.create(pipeline);
    return ResponseEntity.ok(pipeline);
  }

  @PostMapping(value = "/pipeline/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Pipeline> updatePipeline(@PathVariable String pipelineId, @RequestBody Pipeline pipeline) {
    pipeline.setId(pipelineId);
    pipeline = pipelineStore.update(pipeline);
    return ResponseEntity.ok(pipeline);
  }

  @DeleteMapping(value = "/pipeline/{id}")
  public ResponseEntity deletePipeline(@PathVariable String pipelineId) {
    pipelineStore.deleteById(pipelineId);
    return ResponseEntity.noContent().build();
  }

  /**
   * The post body represents the parameters:
   * <code>
   *   {
   *     "paramName": numericValue,
   *     "paramName": "stringValue,
   *     "paramName2": {...},
   *     "paramName3": []
   *   }
   * </code>
   */
  @PostMapping(value = "/pipeline-run/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Document>> runPipeline(@PathVariable String pipelineId, @RequestBody Map parametersMap) {
    return ResponseEntity.ok(pipelineRunner.runAndList(pipelineId, Document.class, parametersMap));
  }
}
