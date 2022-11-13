package com.foxminded.university.dao;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.model.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcAddressDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AddressDao addressDao;

    @Test
    void create_ShouldAddAddressToTable_WhenAddressIsCorrect() {
        addressDao.create(Address.builder().city("TEST").build());
        assertEquals(4, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "addresses"));
    }

    @Test
    void getAll_ShouldReturnListOfAddressesFromDb_WhenMethodCalled() {
        List<Address> addresses = addressDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "addresses"), addresses.size());
    }

    @Test
    void getById_ShouldReturnAddressFromDb_WhenAddressByIdIsExist() {
        Address actual = addressDao.getById(1);
        Address expected = new Address(1, "a", "a", "a", "a");
        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldChangeAddressInDb_WhenAddressIsCorrect() {
        addressDao.update(new Address(1, "TEST", "TEST", "TEST", "TEST"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "addresses", "city = 'TEST'"));
    }

    @Test
    void remove_ShouldRemoveAddressFromDb_WhenAddressIsExist() {
        addressDao.remove(new Address(1, "a", "a", "a", "a"));
        assertEquals(2, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "addresses"));
    }
}

