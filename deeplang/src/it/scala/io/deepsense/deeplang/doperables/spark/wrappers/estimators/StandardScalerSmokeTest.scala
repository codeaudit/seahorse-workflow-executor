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

package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.feature.{StandardScaler => SparkStandardScaler}

import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class StandardScalerSmokeTest
  extends AbstractEstimatorModelWrapperSmokeTest[SparkStandardScaler] {

  override def className: String = "StandardScaler"

  override val estimatorWrapper = new StandardScaler()

  import estimatorWrapper._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    withMean -> false,
    withStd -> true,
    inputColumn -> NameSingleColumnSelection("myFeatures"),
    outputColumn -> "testOutputColumn"
  )
}
