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

package io.deepsense.workflowexecutor

import akka.actor.{Actor, PoisonPill}

import io.deepsense.commons.models.Entity
import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang._
import io.deepsense.graph.DeeplangGraph.DeeplangNode
import io.deepsense.graph.Node
import io.deepsense.reportlib.model.ReportContent
import io.deepsense.workflowexecutor.WorkflowExecutorActor.Messages.{NodeCompleted, NodeFailed, NodeStarted}

/**
 * WorkflowNodeExecutorActor is responsible for execution of single node.
 * Sends NodeStarted at the beginning and NodeCompleted or NodeFailed
 * at the end of execution depending on whether the execution succeeded or failed.
 */
class WorkflowNodeExecutorActor(
    executionContext: ExecutionContext,
    node: DeeplangNode,
    input: Vector[DOperable])
  extends Actor
  with Logging {

  import io.deepsense.workflowexecutor.WorkflowNodeExecutorActor.Messages._

  val nodeDescription = s"'${node.value.name}-${node.id}'"
  var executionStart: Long = _

  override def receive: Receive = {
    case Start() =>
      executionStart = System.currentTimeMillis()
      logger.info(s"Starting execution of node $nodeDescription")
      sendStarted()
      try {
        val resultVector = executeOperation()
        val nodeExecutionResults = nodeExecutionResultsFrom(resultVector)
        sendCompleted(nodeExecutionResults)
      } catch {
        case e: Exception =>
          sendFailed(e)
      } finally {
        // Exception thrown here could result in slightly delayed graph execution
        val duration = (System.currentTimeMillis() - executionStart) / 1000.0
        logger.info(s"Ending execution of node $nodeDescription (duration: $duration seconds)")
        self ! PoisonPill
      }
  }

  def sendFailed(e: Exception): Unit = {
    logger.error(s"Workflow execution failed in node with id=${node.id}.", e)
    sender ! NodeFailed(node.id, e)
  }

  def sendCompleted(nodeExecutionResults: NodeExecutionResults): Unit = {
    val nodeCompleted = NodeCompleted(node.id, nodeExecutionResults)
    logger.debug(s"Node completed: ${nodeExecutionResults.doperables}")
    sender ! nodeCompleted
  }

  def sendStarted(): Unit = {
    val nodeStarted = NodeStarted(node.id)
    sender ! nodeStarted
    logger.debug(s"Sending $nodeStarted.")
  }

  def nodeExecutionResultsFrom(operationResults: Vector[DOperable]): NodeExecutionResults = {
    val registeredResults: Seq[(Entity.Id, DOperable)] = registerResults(operationResults)
    val registeredResultsMap: Map[Entity.Id, DOperable] = registeredResults.toMap
    val reports: Map[Entity.Id, ReportContent] = collectReports(registeredResultsMap)
    NodeExecutionResults(registeredResults.map(_._1), reports, registeredResultsMap)
  }

  private def registerResults(operables: Seq[DOperable]): Seq[(Entity.Id, DOperable)] = {
    logger.debug(s"Registering data from operation output ports in node ${node.id}")
    val results: Seq[(Entity.Id, DOperable)] = operables.map { dOperable =>
      (Entity.Id.randomId, dOperable)
    }
    logger.debug(s"Data registered for $nodeDescription: results=$results")
    results
  }

  def collectReports(results: Map[Entity.Id, DOperable]): Map[Entity.Id, ReportContent] = {
    logger.debug(s"Collecting reports for ${node.id}")
    results.map {
      case (id, dOperable) =>
        (id, dOperable.report(executionContext).content)
    }
  }

  def executeOperation(): Vector[DOperable] = {
    logger.debug(s"$nodeDescription inputVector.size = ${input.size}")
    val inputKnowledge = input.map { dOperable => DKnowledge(dOperable) }
    // if inference throws, we do not perform execution
    node.value.inferKnowledge(executionContext.inferContext)(inputKnowledge)
    val resultVector = node.value.execute(executionContext)(input)
    logger.debug(s"$nodeDescription executed (without reports): $resultVector")
    resultVector
  }
}

object WorkflowNodeExecutorActor {
  object Messages {
    sealed trait Message
    case class Start() extends Message
  }
}
