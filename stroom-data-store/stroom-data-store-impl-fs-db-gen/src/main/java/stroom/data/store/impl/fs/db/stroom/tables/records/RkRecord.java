/*
 * This file is generated by jOOQ.
*/
package stroom.data.store.impl.fs.db.stroom.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;

import stroom.data.store.impl.fs.db.stroom.tables.Rk;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RkRecord extends UpdatableRecordImpl<RkRecord> implements Record7<Integer, Byte, String, String, String, Long, Long> {

    private static final long serialVersionUID = 1698266916;

    /**
     * Setter for <code>stroom.RK.ID</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.RK.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>stroom.RK.VER</code>.
     */
    public void setVer(Byte value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.RK.VER</code>.
     */
    public Byte getVer() {
        return (Byte) get(1);
    }

    /**
     * Setter for <code>stroom.RK.CRT_USER</code>.
     */
    public void setCrtUser(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.RK.CRT_USER</code>.
     */
    public String getCrtUser() {
        return (String) get(2);
    }

    /**
     * Setter for <code>stroom.RK.UPD_USER</code>.
     */
    public void setUpdUser(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>stroom.RK.UPD_USER</code>.
     */
    public String getUpdUser() {
        return (String) get(3);
    }

    /**
     * Setter for <code>stroom.RK.NAME</code>.
     */
    public void setName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>stroom.RK.NAME</code>.
     */
    public String getName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>stroom.RK.CRT_MS</code>.
     */
    public void setCrtMs(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>stroom.RK.CRT_MS</code>.
     */
    public Long getCrtMs() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>stroom.RK.UPD_MS</code>.
     */
    public void setUpdMs(Long value) {
        set(6, value);
    }

    /**
     * Getter for <code>stroom.RK.UPD_MS</code>.
     */
    public Long getUpdMs() {
        return (Long) get(6);
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
    // Record7 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Integer, Byte, String, String, String, Long, Long> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Integer, Byte, String, String, String, Long, Long> valuesRow() {
        return (Row7) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Rk.RK.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field2() {
        return Rk.RK.VER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Rk.RK.CRT_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Rk.RK.UPD_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Rk.RK.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field6() {
        return Rk.RK.CRT_MS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field7() {
        return Rk.RK.UPD_MS;
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
    public Byte component2() {
        return getVer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getCrtUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getUpdUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component6() {
        return getCrtMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component7() {
        return getUpdMs();
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
    public Byte value2() {
        return getVer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getCrtUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getUpdUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value6() {
        return getCrtMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value7() {
        return getUpdMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RkRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RkRecord value2(Byte value) {
        setVer(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RkRecord value3(String value) {
        setCrtUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RkRecord value4(String value) {
        setUpdUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RkRecord value5(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RkRecord value6(Long value) {
        setCrtMs(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RkRecord value7(Long value) {
        setUpdMs(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RkRecord values(Integer value1, Byte value2, String value3, String value4, String value5, Long value6, Long value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RkRecord
     */
    public RkRecord() {
        super(Rk.RK);
    }

    /**
     * Create a detached, initialised RkRecord
     */
    public RkRecord(Integer id, Byte ver, String crtUser, String updUser, String name, Long crtMs, Long updMs) {
        super(Rk.RK);

        set(0, id);
        set(1, ver);
        set(2, crtUser);
        set(3, updUser);
        set(4, name);
        set(5, crtMs);
        set(6, updMs);
    }
}
