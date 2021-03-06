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

package com.twitter.common.text.token;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.twitter.common.text.token.attribute.CharSequenceTermAttribute;
import com.twitter.common.text.token.attribute.TokenType;
import com.twitter.common.text.token.attribute.TokenTypeAttribute;
import org.apache.lucene.util.AttributeSource;

import java.util.List;

/**
 * Abstraction to enumerate a sequence of tokens. This class represents the central abstraction in
 * Twitter's com.twitter.common.text processing library, and is similar to Lucene's TokenStream, with the following
 * exceptions:
 *
 * <ul>
 * <li>This class assumes that the input com.twitter.common.text is a {@link CharSequence}.
 * <li>Calls support chaining.
 * <li>Instances are reusable.
 * </ul>
 *
 * For an annotated example of how this class is used in practice, refer to
 * {@link com.twitter.common.text.example.TokenizerUsageExample}.
 */
public abstract class TokenStream extends AttributeSource {
  private final CharSequenceTermAttribute termAttribute = addAttribute(CharSequenceTermAttribute.class);
  private final TokenTypeAttribute typeAttribute = addAttribute(TokenTypeAttribute.class);

  /**
   * Constructs a {@code TokenStream} using the default attribute factory.
   */
  public TokenStream() {
    super();
  }

  /**
   * Constructs a {@code TokenStream} using the supplied {@code AttributeFactory} for creating new
   * {@code Attribute} instances.
   *
   * @param factory attribute factory
   */
  protected TokenStream(AttributeFactory factory) {
    super(factory);
  }

  /**
   * Constructs a {@code TokenStream} that uses the same attributes as the supplied one.
   *
   * @param input attribute source
   */
  protected TokenStream(AttributeSource input) {
    super(input);
  }

  /**
   * Consumers call this method to advance the stream to the next token.
   *
   * @return false for end of stream; true otherwise
   */
  public abstract boolean incrementToken();

  /**
   * Resets this {@code TokenStream} (and also downstream tokens if they exist) to parse a new
   * input.
   *
   * @param input new com.twitter.common.text to parse.
   */
  public abstract void reset(CharSequence input);

  /**
   * Converts this token stream into a list of {@code Strings}.
   *
   * @return the contents of the token stream as a list of {@code Strings}.
   */
  public List<String> toStringList() {
    List<String> tokens = Lists.newArrayList();

    while (incrementToken()) {
      tokens.add(term().toString());
    }

    return tokens;
  }

  /**
   * Searches and returns an instance of a specified class in this TokenStream chain.
   *
   * @param cls class to search for
   * @return instance of the class {@code cls} if found or {@code null} if not found
   */
  public <T extends TokenStream> T getInstanceOf(Class<T> cls) {
    Preconditions.checkNotNull(cls);
    if (cls.isInstance(this)) {
      return cls.cast(this);
    }
    return null;
  }

  /**
   * Returns the offset of the current token.
   *
   * @return offset of the current token.
   */
  public int offset() {
    return termAttribute.getOffset();
  }

  /**
   * Returns the length of the current token.
   *
   * @return length of the current token.
   */
  public int length() {
    return termAttribute.getLength();
  }

  /**
   * Returns the {@code CharSequence} of the current token.
   *
   * @return {@code CharSequence} of the current token
   */
  public CharSequence term() {
    return termAttribute.getTermCharSequence();
  }

  /**
   * Returns the input {@code CharSequence}.
   *
   * @return input {@code CharSequence}
   */
  public CharSequence inputCharSequence() {
    return termAttribute.getCharSequence();
  }

  /**
   * Returns the type of the current token.
   *
   * @return type of the current token.
   */
  public TokenType type() {
    return typeAttribute.getType();
  }

  /**
   * Sets the input {@code CharSequence}.
   *
   * @param inputCharSequence {@code CharSequence} analyzed by this
   *     {@code TokenStream}
   */
  protected void updateInputCharSequence(CharSequence inputCharSequence) {
    termAttribute.setCharSequence(inputCharSequence);
  }

  /**
   * Updates the offset and length of the current token.
   *
   * @param offset new offset
   * @param length new length
   */
  protected void updateOffsetAndLength(int offset, int length) {
    termAttribute.setOffset(offset);
    termAttribute.setLength(length);
  }

  /**
   * Updates the type of the current token.
   *
   * @param type new type
   */
  protected void updateType(TokenType type) {
    typeAttribute.setType(type);
  }

  @Override
  public boolean equals(Object target) {
    // Lucene's AttributeSource.equals() returns true if this has the same
    // set of attributes as the target one. Let's make it more strict.
    return this == target;
  }

  @Override
  public int hashCode() {
    return System.identityHashCode(this);
  }
}
