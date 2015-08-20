/**
 * Copyright 2015, CodiLime Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.deepsense.models.json.workflow

import io.deepsense.commons.exception.FailureDescription
import io.deepsense.commons.json.EnumerationSerializer
import io.deepsense.graph.Status.Status
import io.deepsense.graph.{Graph, State, Status}
import io.deepsense.models.entities.Entity
import io.deepsense.models.workflows._
import org.joda.time.DateTime
import spray.json._

trait WorkflowWithResultsJsonProtocol extends WorkflowJsonProtocol {

  implicit val statusFormat = EnumerationSerializer.jsonEnumFormat(Status)

  // excluding "progress" from serialization/deserialization
  implicit val stateFormat: JsonFormat[State] = new JsonFormat[State] {
    override def write(state: State): JsValue = {
      JsObject(
        "status" -> state.status.toJson,
        "started" -> state.started.toJson,
        "ended" -> state.ended.toJson,
        "results" -> state.results.toJson,
        "error" -> state.error.toJson
      )
    }
    override def read(json: JsValue): State = {
      val jsObject = json.asJsObject
      State(
        jsObject.fields("status").convertTo[Status],
        jsObject.fields("started").convertTo[Option[DateTime]],
        jsObject.fields("ended").convertTo[Option[DateTime]],
        progress = None,
        jsObject.fields("results").convertTo[Option[Seq[Entity.Id]]],
        jsObject.fields("error").convertTo[Option[FailureDescription]]
      )
    }
  }

  implicit val entitiesMapFormat = jsonFormat0(EntitiesMap.apply)

  implicit val executionReportFormat = jsonFormat4(ExecutionReport)

  implicit val workflowWithResultsFormat =
    jsonFormat(WorkflowWithResults, "metadata", "workflow", "thirdPartyData", "executionReport")

}