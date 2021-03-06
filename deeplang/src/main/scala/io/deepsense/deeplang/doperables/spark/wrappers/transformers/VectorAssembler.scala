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

package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.{Report, SparkTransformerWrapper}
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.wrappers.spark.{SingleColumnCreatorParamWrapper, ColumnSelectorParamWrapper}

import org.apache.spark.ml.feature.{VectorAssembler => SparkVectorAssembler}

class VectorAssembler extends SparkTransformerWrapper[SparkVectorAssembler] {

  val inputColumns = new ColumnSelectorParamWrapper[SparkVectorAssembler](
    name = "input columns",
    description = "Input columns",
    sparkParamGetter = _.inputCols,
    portIndex = 0)

  val outputColumn = new SingleColumnCreatorParamWrapper[SparkVectorAssembler](
    name = "output column",
    description = "Name of created output column",
    sparkParamGetter = _.outputCol)

  override def report(executionContext: ExecutionContext): Report = Report()
  override val params: Array[Param[_]] = declareParams(inputColumns, outputColumn)
}
