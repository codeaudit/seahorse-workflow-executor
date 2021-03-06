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

import org.apache.spark.ml.feature.{Binarizer => SparkBinarizer}

import io.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper

class Binarizer extends SparkTransformerAsMultiColumnTransformer[SparkBinarizer] {

  val threshold = new DoubleParamWrapper[SparkBinarizer](
    name = "threshold",
    description = "Threshold used to binarize continuous features. Feature values greater than " +
      "the threshold will be binarized to 1.0. Remaining values will be binarized to 0.0.",
    sparkParamGetter = _.threshold)
  setDefault(threshold, 0.0)

  override protected def getSpecificParams: Array[Param[_]] = Array(threshold)
}
