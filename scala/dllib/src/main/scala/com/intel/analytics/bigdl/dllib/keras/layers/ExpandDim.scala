/*
 * Copyright 2016 The BigDL Authors.
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

package com.intel.analytics.bigdl.dllib.keras.layers

import com.intel.analytics.bigdl.dllib.nn.abstractnn.AbstractModule
import com.intel.analytics.bigdl.dllib.nn.keras.KerasLayer
import com.intel.analytics.bigdl.dllib.tensor.Tensor
import com.intel.analytics.bigdl.dllib.tensor.TensorNumericMath.TensorNumeric
import com.intel.analytics.bigdl.dllib.utils.Shape
import com.intel.analytics.bigdl.dllib.keras.Net
import com.intel.analytics.bigdl.dllib.keras.layers.utils.KerasUtils

import scala.reflect.ClassTag

class ExpandDim[T: ClassTag](
      val dim: Int = 0,
      val inputShape: Shape = null)(implicit ev: TensorNumeric[T])
  extends KerasLayer[Tensor[T], Tensor[T], T](KerasUtils.addBatch(inputShape)) with Net {

  override def clearState() : this.type = {
    output = Tensor[T]()
    gradInput = Tensor[T]()
    this
  }

  override def computeOutputShape(inputShape: Shape): Shape = {
    val output = inputShape.toSingle().toArray.toBuffer
    output.insert(dim, 1)
    Shape(output.toArray)
  }

  override def doBuild(inputShape: Shape): AbstractModule[Tensor[T], Tensor[T], T] = {
    val layer = {
      com.intel.analytics.bigdl.dllib.nn.Unsqueeze(dim + 1) // one-based index in Bigdl
    }
    layer.asInstanceOf[AbstractModule[Tensor[T], Tensor[T], T]]
  }
}

object ExpandDim {
  def apply[@specialized(Float, Double) T: ClassTag](
      dim: Int,
      inputShape: Shape = null)(implicit ev: TensorNumeric[T]): ExpandDim[T] = {
    new ExpandDim[T](dim, inputShape)
  }
}
