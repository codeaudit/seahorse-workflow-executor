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

import org.apache.spark.ml.feature.HashingTF

import io.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

class HashingTFTransformer extends SparkTransformerAsMultiColumnTransformer[HashingTF] {

  val numFeatures = new IntParamWrapper[HashingTF](
    name = "numFeatures",
    description = "Number of features. Should be > 0.",
    sparkParamGetter = _.numFeatures,
    validator = RangeValidator(1.0, Int.MaxValue, step = Some(1.0)))
  setDefault(numFeatures, (1 << 18).toDouble)

  override protected def getSpecificParams: Array[Param[_]] = Array(numFeatures)
}
