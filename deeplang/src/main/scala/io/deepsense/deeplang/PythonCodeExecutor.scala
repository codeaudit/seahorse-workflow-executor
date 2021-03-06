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

package io.deepsense.deeplang

trait PythonCodeExecutor {

  /**
   * Validates custom operation's source code.
   * @param code Code to be validated.
   * @return True if validation passed, False otherwise.
   */
  def isValid(code: String): Boolean

  /**
   * Executes custom operation's source code
   * @param workflowId Id of the workflow.
   * @param nodeId Id of the node.
   * @param code Code to be executed.
   */
  def run(workflowId: String, nodeId: String, code: String): Unit
}
