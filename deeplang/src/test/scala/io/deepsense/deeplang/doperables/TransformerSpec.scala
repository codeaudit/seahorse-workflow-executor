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

package io.deepsense.deeplang.doperables

import org.apache.spark.sql.types.StructType
import org.mockito.Matchers.any
import org.mockito.Mockito._

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.deeplang.{DKnowledge, ExecutionContext, UnitSpec}

class TransformerSpec extends UnitSpec {

  private def transformer = {
    val t = mock[Transformer]
    when(t.transform) thenCallRealMethod()
    when(t._transformSchema(any(), any())) thenCallRealMethod()
    t
  }

  val inputDF = mock[DataFrame]
  val outputDF = mock[DataFrame]

  val inputSchema = mock[StructType]
  val outputSchema = mock[StructType]

  val execCtx = mock[ExecutionContext]
  val inferCtx = mock[InferContext]

  val emptyWarnings = InferenceWarnings.empty

  "Transformer" should {
    "transform DataFrame" in {
      val t = transformer
      when(t._transform(execCtx, inputDF)) thenReturn outputDF
      t.transform(execCtx)(())(inputDF) shouldBe outputDF
    }
    "infer schema" when {
      "it's implemented" when {
        val t = transformer
        when(t._transformSchema(inputSchema)) thenReturn Some(outputSchema)

        val expectedOutputDKnowledge = DKnowledge(DataFrame.forInference(outputSchema))
        "input DKnowledge contains exactly one type" in {
          val inputDKnowledge = DKnowledge(DataFrame.forInference(inputSchema))
          val output = t.transform.infer(inferCtx)(())(inputDKnowledge)
          output shouldBe (expectedOutputDKnowledge, emptyWarnings)
        }
        "input DKnowledge contains more than one type" in {
          val inputDKnowledge = DKnowledge(
            DataFrame.forInference(inputSchema),
            DataFrame.forInference(inputSchema)
          )
          val output = t.transform.infer(inferCtx)(())(inputDKnowledge)
          output shouldBe (expectedOutputDKnowledge, emptyWarnings)
        }
      }
    }
    "not infer schema" when {
      "it's not implemented" in {
        val t = transformer
        when(t._transformSchema(inputSchema)) thenReturn None
        val inputDKnowledge = DKnowledge(DataFrame.forInference(inputSchema))
        val expectedOutputDKnowledge = DKnowledge(DataFrame.forInference(None))
        val output = t.transform.infer(inferCtx)(())(inputDKnowledge)
        output shouldBe (expectedOutputDKnowledge, emptyWarnings)
      }
    }
  }
}


