/*
 * Copyright 2016 Crown Copyright
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

package stroom.entity.shared;

import stroom.util.shared.EqualsBuilder;
import stroom.util.shared.HashCodeBuilder;

import java.io.Serializable;

/**
 * Class that holds a range of some number type. A null upper or lower bound
 * means an open ended range. The upper bound is not included i.e. [0..10) means
 * 0,1,2,3,4,5,6,7,8,9 or this can be represented by the toString [0..9]
 */
public class Range<T extends Number> implements Serializable, HasIsConstrained {
    private static final long serialVersionUID = -7405632565984023195L;

    T from;
    T to;
    boolean matchNull = false;

    public Range() {
        init(null, null);
    }

    public Range(final T from, final T to) {
        init(from, to);
    }

    protected void init(final T from, final T to) {
        this.from = from;
        this.to = to;
    }

    public boolean isMatchNull() {
        return matchNull;
    }

    public void setMatchNull(final boolean matchNull) {
        this.matchNull = matchNull;
    }

    /**
     * Does this range contain this number?
     */
    public boolean contains(final long num) {
        // If we have a lower bound check that the time is not before it.
        if (from != null && num < from.longValue()) {
            return false;
        }
        // If we have an upper bound check that the time is not ON or after it.
        if (to != null && num >= to.longValue()) {
            return false;
        }

        // Must be covered in our period then
        return true;
    }

    /**
     * Is this period after a time?
     */
    public boolean after(final long num) {
        return from != null && from.longValue() > num;
    }

    /**
     * Is this period before a time?
     */
    public boolean before(final long num) {
        return to != null && to.longValue() < num;
    }

    public T getFrom() {
        return from;
    }

    protected void setFrom(final T from) {
        this.from = from;
    }

    /**
     * @param other value to return if from is null
     * @return The from value or if that is null, the supplied other value
     */
    public T getFromOrElse(final T other) {
        return from != null ? from : other;
    }

    public T getTo() {
        return to;
    }

    protected void setTo(final T to) {
        this.to = to;
    }

    /**
     * @param other value to return if to is null
     * @return The to value or if that is null, the supplied other value
     */
    public T getToOrElse(final T other) {
        return to != null ? to : other;
    }

    /**
     * @return have we an upper and lower bound?
     */
    public boolean isBounded() {
        return from != null && to != null;
    }

    /**
     * Are we empty ? i.e. the lower bound is the same as the upper one.
     */
    public boolean isEmpty() {
        return isBounded() && from.longValue() >= to.longValue();
    }

    public Number size() {
        if (!isBounded()) {
            return null;
        }
        return getTo().longValue() - getFrom().longValue();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Range<T> union(final Range other) {
        return new Range(this.from.longValue() < other.from.longValue() ? this.from : other.from,
                this.to.longValue() > other.to.longValue() ? this.to : other.to);

    }

    /**
     * @return have we an upper or lower bound or we are to just match null?
     */
    @Override
    public boolean isConstrained() {
        return from != null || to != null || matchNull;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Range<?>)) {
            return false;
        }

        final Range<?> other = (Range<?>) obj;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.from, other.from);
        builder.append(this.to, other.to);
        builder.append(this.matchNull, other.matchNull);
        return builder.isEquals();
    }

    /**
     * Determine if a supplied time is within this period. Used for mock stream
     * store.
     */
    public boolean isMatch(final T num) {
        if (isConstrained()) {
            return true;
        }
        if (from == null) {
            return num.longValue() < to.longValue();
        }
        if (to == null) {
            return num.longValue() >= from.longValue();
        }
        return num.longValue() >= from.longValue() && num.longValue() < to.longValue();
    }

    @Override
    public final int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(from);
        builder.append(to);
        builder.append(matchNull);
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (from != null) {
            sb.append("[");
            sb.append(from);
        }
        sb.append("..");
        if (to != null) {
            sb.append(to.longValue());
            sb.append(")");
        }
        return sb.toString();
    }

}
