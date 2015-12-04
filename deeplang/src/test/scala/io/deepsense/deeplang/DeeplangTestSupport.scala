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

import org.scalatest.mock.MockitoSugar

import io.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import io.deepsense.deeplang.doperables.dataframe.DataFrameBuilder
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.entitystorage.EntityStorageClient

trait DeeplangTestSupport extends MockitoSugar {

  protected def createInferContext(
      dOperableCatalog: DOperableCatalog,
      fullInference: Boolean): InferContext =
    InferContext(
      mock[DataFrameBuilder],
      mock[EntityStorageClient],
      "testTenantId",
      dOperableCatalog,
      fullInference = fullInference)
}