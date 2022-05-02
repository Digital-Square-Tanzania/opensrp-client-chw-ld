package org.smartregister.dao;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.chw.ld.dao.LDDao;
import org.smartregister.repository.Repository;

@RunWith(MockitoJUnitRunner.class)
public class TestDaoLD extends LDDao {

    @Mock
    private Repository repository;

    @Mock
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setRepository(repository);
    }

    @Test
    public void testGetLDTestDate() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        LDDao.getLDTestDate("123456");
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void testIsRegisteredForLD() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        boolean registered = LDDao.isRegisteredForLD("12345");
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertFalse(registered);
    }
}

