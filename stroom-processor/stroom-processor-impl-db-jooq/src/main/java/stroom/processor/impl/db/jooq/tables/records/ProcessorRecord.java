/*
 * This file is generated by jOOQ.
 */
package stroom.processor.impl.db.jooq.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.UpdatableRecordImpl;
import stroom.processor.impl.db.jooq.tables.Processor;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ProcessorRecord extends UpdatableRecordImpl<ProcessorRecord> implements Record10<Integer, Integer, Long, String, Long, String, String, String, String, Boolean> {

    private static final long serialVersionUID = -1453990092;

    /**
     * Setter for <code>stroom.processor.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.processor.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>stroom.processor.version</code>.
     */
    public void setVersion(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.processor.version</code>.
     */
    public Integer getVersion() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>stroom.processor.create_time_ms</code>.
     */
    public void setCreateTimeMs(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.processor.create_time_ms</code>.
     */
    public Long getCreateTimeMs() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>stroom.processor.create_user</code>.
     */
    public void setCreateUser(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>stroom.processor.create_user</code>.
     */
    public String getCreateUser() {
        return (String) get(3);
    }

    /**
     * Setter for <code>stroom.processor.update_time_ms</code>.
     */
    public void setUpdateTimeMs(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>stroom.processor.update_time_ms</code>.
     */
    public Long getUpdateTimeMs() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>stroom.processor.update_user</code>.
     */
    public void setUpdateUser(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>stroom.processor.update_user</code>.
     */
    public String getUpdateUser() {
        return (String) get(5);
    }

    /**
     * Setter for <code>stroom.processor.uuid</code>.
     */
    public void setUuid(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>stroom.processor.uuid</code>.
     */
    public String getUuid() {
        return (String) get(6);
    }

    /**
     * Setter for <code>stroom.processor.task_type</code>.
     */
    public void setTaskType(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>stroom.processor.task_type</code>.
     */
    public String getTaskType() {
        return (String) get(7);
    }

    /**
     * Setter for <code>stroom.processor.pipeline_uuid</code>.
     */
    public void setPipelineUuid(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>stroom.processor.pipeline_uuid</code>.
     */
    public String getPipelineUuid() {
        return (String) get(8);
    }

    /**
     * Setter for <code>stroom.processor.enabled</code>.
     */
    public void setEnabled(Boolean value) {
        set(9, value);
    }

    /**
     * Getter for <code>stroom.processor.enabled</code>.
     */
    public Boolean getEnabled() {
        return (Boolean) get(9);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Integer, Integer, Long, String, Long, String, String, String, String, Boolean> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Integer, Integer, Long, String, Long, String, String, String, String, Boolean> valuesRow() {
        return (Row10) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Processor.PROCESSOR.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return Processor.PROCESSOR.VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return Processor.PROCESSOR.CREATE_TIME_MS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Processor.PROCESSOR.CREATE_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return Processor.PROCESSOR.UPDATE_TIME_MS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Processor.PROCESSOR.UPDATE_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Processor.PROCESSOR.UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Processor.PROCESSOR.TASK_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return Processor.PROCESSOR.PIPELINE_UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field10() {
        return Processor.PROCESSOR.ENABLED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component2() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component3() {
        return getCreateTimeMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getCreateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component5() {
        return getUpdateTimeMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getUpdateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getTaskType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getPipelineUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean component10() {
        return getEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getCreateTimeMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getCreateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getUpdateTimeMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getUpdateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getTaskType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getPipelineUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value10() {
        return getEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord value2(Integer value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord value3(Long value) {
        setCreateTimeMs(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord value4(String value) {
        setCreateUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord value5(Long value) {
        setUpdateTimeMs(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord value6(String value) {
        setUpdateUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord value7(String value) {
        setUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord value8(String value) {
        setTaskType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord value9(String value) {
        setPipelineUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord value10(Boolean value) {
        setEnabled(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessorRecord values(Integer value1, Integer value2, Long value3, String value4, Long value5, String value6, String value7, String value8, String value9, Boolean value10) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ProcessorRecord
     */
    public ProcessorRecord() {
        super(Processor.PROCESSOR);
    }

    /**
     * Create a detached, initialised ProcessorRecord
     */
    public ProcessorRecord(Integer id, Integer version, Long createTimeMs, String createUser, Long updateTimeMs, String updateUser, String uuid, String taskType, String pipelineUuid, Boolean enabled) {
        super(Processor.PROCESSOR);

        set(0, id);
        set(1, version);
        set(2, createTimeMs);
        set(3, createUser);
        set(4, updateTimeMs);
        set(5, updateUser);
        set(6, uuid);
        set(7, taskType);
        set(8, pipelineUuid);
        set(9, enabled);
    }
}
