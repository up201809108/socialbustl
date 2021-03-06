// =================================================================================================
// Copyright 2011 Twitter, Inc.
// -------------------------------------------------------------------------------------------------
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this work except in compliance with the License.
// You may obtain a copy of the License in the LICENSE file, or at:
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// =================================================================================================

package com.twitter.common.text.util;

import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;

/**
 * (De)Serializes {@link org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute)s.
 */
public class PositionIncrementAttributeSerializer implements TokenStreamSerializer.AttributeSerializer {
  private PositionIncrementAttribute posIncrAttribute;

  @Override
  public void initialize(AttributeSource attributeSource, TokenStreamSerializer.Version version)
      throws IOException {
    this.posIncrAttribute = attributeSource.addAttribute(PositionIncrementAttribute.class);
  }

  @Override
  public void serialize(TokenStreamSerializer.AttributeOutputStream output) throws IOException {
    output.writeVInt(posIncrAttribute.getPositionIncrement());
  }

  @Override
  public void deserialize(TokenStreamSerializer.AttributeInputStream input,
                          CharSequence charSequence) throws IOException {
    posIncrAttribute.setPositionIncrement(input.readVInt());
  }
}
