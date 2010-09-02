package com.qcadoo.mes.core.data.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.springframework.test.util.ReflectionTestUtils;

import com.qcadoo.mes.core.data.api.DataAccessService;
import com.qcadoo.mes.core.data.api.DataDefinitionService;
import com.qcadoo.mes.core.data.beans.Entity;
import com.qcadoo.mes.core.data.definition.DataDefinition;
import com.qcadoo.mes.core.data.definition.FieldDefinition;
import com.qcadoo.mes.core.data.internal.search.SearchCriteriaImpl;
import com.qcadoo.mes.core.data.internal.types.FieldTypeFactoryImpl;
import com.qcadoo.mes.core.data.search.SearchResult;
import com.qcadoo.mes.core.data.types.FieldTypeFactory;

public class DataAccessServiceFindTest {

    private final DataDefinitionService dataDefinitionService = mock(DataDefinitionService.class);

    private final EntityService entityService = new EntityService();

    private final SessionFactory sessionFactory = mock(SessionFactory.class, RETURNS_DEEP_STUBS);

    private final FieldTypeFactory fieldTypeFactory = new FieldTypeFactoryImpl();

    private DataAccessService dataAccessService = null;

    @Before
    public void init() {
        dataAccessService = new DataAccessServiceImpl();
        ReflectionTestUtils.setField(entityService, "dataDefinitionService", dataDefinitionService);
        ReflectionTestUtils.setField(dataAccessService, "dataDefinitionService", dataDefinitionService);
        ReflectionTestUtils.setField(dataAccessService, "entityService", entityService);
        ReflectionTestUtils.setField(dataAccessService, "sessionFactory", sessionFactory);

        DataDefinition dataDefinition = new DataDefinition("test.Entity");
        dataDefinition.setFullyQualifiedClassName(SimpleDatabaseObject.class.getCanonicalName());

        FieldDefinition fieldDefinitionName = new FieldDefinition("name");
        fieldDefinitionName.setType(fieldTypeFactory.stringType());
        dataDefinition.addField(fieldDefinitionName);

        FieldDefinition fieldDefinitionAge = new FieldDefinition("age");
        fieldDefinitionAge.setType(fieldTypeFactory.integerType());
        dataDefinition.addField(fieldDefinitionAge);

        given(dataDefinitionService.get("test.Entity")).willReturn(dataDefinition);
    }

    @Test
    public void shouldReturnValidEntities() throws Exception {
        // given
        List<SimpleDatabaseObject> databaseObjects = new ArrayList<SimpleDatabaseObject>();
        databaseObjects.add(createDatabaseObject(1L, "name1", 1));
        databaseObjects.add(createDatabaseObject(2L, "name2", 2));
        databaseObjects.add(createDatabaseObject(3L, "name3", 3));
        databaseObjects.add(createDatabaseObject(4L, "name4", 4));

        SearchCriteriaImpl searchCriteria = new SearchCriteriaImpl("test.Entity");
        searchCriteria.setFirstResult(0);
        searchCriteria.setMaxResults(4);

        given(
                sessionFactory.getCurrentSession().createCriteria(SimpleDatabaseObject.class).add(any(Criterion.class))
                        .setProjection(any(Projection.class)).uniqueResult()).willReturn(4);

        given(
                sessionFactory.getCurrentSession().createCriteria(SimpleDatabaseObject.class).add(any(Criterion.class))
                        .setFirstResult(0).setMaxResults(4).addOrder(any(org.hibernate.criterion.Order.class)).list())
                .willReturn(databaseObjects);

        // when
        SearchResult resultSet = dataAccessService.find("test.Entity", searchCriteria);

        // then
        assertEquals(4, resultSet.getTotalNumberOfEntities());
        assertEquals(4, resultSet.getEntities().size());
        Assert.assertThat(resultSet.getEntities(), JUnitMatchers.hasItems(createEntity(1L, "name1", 1),
                createEntity(2L, "name2", 2), createEntity(3L, "name3", 3), createEntity(4L, "name4", 4)));
    }

    private SimpleDatabaseObject createDatabaseObject(final Long id, final String name, final int age) {
        SimpleDatabaseObject simpleDatabaseObject = new SimpleDatabaseObject(id);
        simpleDatabaseObject.setName(name);
        simpleDatabaseObject.setAge(age);
        return simpleDatabaseObject;
    }

    private Entity createEntity(final Long id, final String name, final int age) {
        Entity entity = new Entity(id);
        entity.setField("name", name);
        entity.setField("age", age);
        return entity;
    }

}
