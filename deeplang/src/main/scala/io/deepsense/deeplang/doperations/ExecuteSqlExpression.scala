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

package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.doperables.SqlExpression

class ExecuteSqlExpression extends TransformerAsOperation[SqlExpression] {

  override val name: String = "Execute SQL Expression"
  override val id: Id = "6cba4400-d966-4a2a-8356-b37f37b4c73f"
  override val description: String =
    "Executes an SQL expression on a DataFrame"

  override lazy val tTagTO_1: TypeTag[SqlExpression] = typeTag
}
