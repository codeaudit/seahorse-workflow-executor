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

package io.deepsense.deeplang.params

import spray.json._

import io.deepsense.deeplang.params.selections.{IndexSingleColumnSelection, SingleColumnSelection}

class SingleColumnSelectorParamSpec
  extends AbstractParamSpec[SingleColumnSelection, SingleColumnSelectorParam] {

  override def className: String = "SingleColumnSelectorParam"

  override def paramFixture: (SingleColumnSelectorParam, JsValue) = {
    val param = SingleColumnSelectorParam(
      name = "Single column selector name",
      description = "Single column selector description",
      portIndex = 0)
    val expectedJson = JsObject(
      "type" -> JsString("selector"),
      "name" -> JsString(param.name),
      "description" -> JsString(param.description),
      "portIndex" -> JsNumber(param.portIndex),
      "isSingle" -> JsTrue,
      "default" -> JsNull
    )
    (param, expectedJson)
  }

  override def valueFixture: (SingleColumnSelection, JsValue) = {
    val selection = IndexSingleColumnSelection(2)
    val expectedJson = JsObject(
      "type" -> JsString("index"),
      "value" -> JsNumber(2)
    )
    (selection, expectedJson)
  }
}
