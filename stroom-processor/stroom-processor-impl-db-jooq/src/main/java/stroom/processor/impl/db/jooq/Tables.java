/*
 * This file is generated by jOOQ.
 */
package stroom.processor.impl.db.jooq;


import javax.annotation.Generated;

import stroom.processor.impl.db.jooq.tables.Processor;
import stroom.processor.impl.db.jooq.tables.ProcessorFilter;
import stroom.processor.impl.db.jooq.tables.ProcessorFilterTask;
import stroom.processor.impl.db.jooq.tables.ProcessorFilterTracker;
import stroom.processor.impl.db.jooq.tables.ProcessorNode;


/**
 * Convenience access to all tables in stroom
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>stroom.processor</code>.
     */
    public static final Processor PROCESSOR = stroom.processor.impl.db.jooq.tables.Processor.PROCESSOR;

    /**
     * The table <code>stroom.processor_filter</code>.
     */
    public static final ProcessorFilter PROCESSOR_FILTER = stroom.processor.impl.db.jooq.tables.ProcessorFilter.PROCESSOR_FILTER;

    /**
     * The table <code>stroom.processor_filter_task</code>.
     */
    public static final ProcessorFilterTask PROCESSOR_FILTER_TASK = stroom.processor.impl.db.jooq.tables.ProcessorFilterTask.PROCESSOR_FILTER_TASK;

    /**
     * The table <code>stroom.processor_filter_tracker</code>.
     */
    public static final ProcessorFilterTracker PROCESSOR_FILTER_TRACKER = stroom.processor.impl.db.jooq.tables.ProcessorFilterTracker.PROCESSOR_FILTER_TRACKER;

    /**
     * The table <code>stroom.processor_node</code>.
     */
    public static final ProcessorNode PROCESSOR_NODE = stroom.processor.impl.db.jooq.tables.ProcessorNode.PROCESSOR_NODE;
}
