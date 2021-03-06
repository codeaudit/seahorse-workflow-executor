/**
 * Copyright 2015, deepsense.io
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

package io.deepsense.models.json.graph

import org.joda.time.DateTime
import spray.json._

import io.deepsense.commons.exception.FailureDescription
import io.deepsense.commons.exception.json.FailureDescriptionJsonProtocol
import io.deepsense.commons.json.DateTimeJsonProtocol._
import io.deepsense.commons.models.Entity
import io.deepsense.graph.nodestate._

trait NodeStatusJsonProtocol
  extends DefaultJsonProtocol
  with FailureDescriptionJsonProtocol
  with NullOptions {

  implicit object NodeStatusFormat
    extends JsonFormat[NodeStatus]
    with DefaultJsonProtocol
    with NullOptions {

    val viewFormat = jsonFormat5(NodeStatusView)
    val abortedFormat = jsonFormat(Aborted, "results")
    val runningFormat = jsonFormat(Running, "started", "results")
    val queuedFormat = jsonFormat(Queued, "results")
    val draftFormat = jsonFormat(Draft, "results")
    val completedFormat = jsonFormat(Completed, "started", "ended", "results")
    val failedFormat = jsonFormat(Failed, "started", "ended", "error")

    override def write(state: NodeStatus): JsValue = {

      val view = state match {
        case Running(started, results) =>
          NodeStatusView(state.name, started = Some(started), results = Some(results))
        case Aborted(results) => NodeStatusView(state.name, results = Some(results))
        case Draft(results) => NodeStatusView(state.name, results = Some(results))
        case Queued(results) => NodeStatusView(state.name, results = Some(results))
        case Completed(started, ended, results) =>
          NodeStatusView(state.name,
            started = Some(started),
            ended = Some(ended),
            results = Some(results))
        case Failed(started, ended, error) =>
          NodeStatusView(state.name,
            started = Some(started),
            ended = Some(ended),
            error = Some(error)
          )
        case _ => NodeStatusView(state.name)
      }

      view.toJson(viewFormat)
    }

    override def read(json: JsValue): NodeStatus = {
      val jsObject = json.asJsObject
      val status = jsObject.fields.get("status")
      if (status.isEmpty) {
        throw new DeserializationException("Expected 'status' field does not exist")
      }

      status.get.convertTo[String] match {
        case "DRAFT" => jsObject.convertTo[Draft](draftFormat)
        case "QUEUED" => jsObject.convertTo[Queued](queuedFormat)
        case "RUNNING" => jsObject.convertTo[Running](runningFormat)
        case "COMPLETED" => jsObject.convertTo[Completed](completedFormat)
        case "FAILED" => jsObject.convertTo[Failed](failedFormat)
        case "ABORTED" => jsObject.convertTo[Aborted](abortedFormat)
      }
    }

    case class NodeStatusView(
      status: String,
      started: Option[DateTime] = None,
      ended: Option[DateTime] = None,
      results: Option[Seq[Entity.Id]] = None,
      error: Option[FailureDescription] = None)
  }
}

object NodeStatusJsonProtocol extends NodeStatusJsonProtocol {
  val Status = "status"
  val Started = "started"
  val Ended = "ended"
  val Results = "results"
  val Error = "error"
}
