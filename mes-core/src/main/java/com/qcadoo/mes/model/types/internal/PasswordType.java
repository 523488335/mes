/**
 * ********************************************************************
 * Code developed by amazing QCADOO developers team.
 * Copyright � Qcadoo Limited sp. z o.o. (2010)
 * ********************************************************************
 */

package com.qcadoo.mes.model.types.internal;

import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.qcadoo.mes.api.Entity;
import com.qcadoo.mes.model.FieldDefinition;
import com.qcadoo.mes.model.types.FieldType;

public final class PasswordType implements FieldType {

    private final PasswordEncoder passwordEncoder;

    public PasswordType(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isSearchable() {
        return false;
    }

    @Override
    public boolean isOrderable() {
        return false;
    }

    @Override
    public boolean isAggregable() {
        return false;
    }

    @Override
    public Class<?> getType() {
        return String.class;
    }

    @Override
    public Object toObject(final FieldDefinition fieldDefinition, final Object value, final Entity validatedEntity) {
        return passwordEncoder.encodePassword(String.valueOf(value), null);
    }

    @Override
    public String toString(final Object value) {
        return null;
    }

}
