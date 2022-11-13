package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.AddressDao;
import com.foxminded.university.dao.jdbc.mappers.AddressMapper;
import com.foxminded.university.model.Address;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcAddressDao implements AddressDao {

    private static final String CREATE_ADDRESS_QUERY = "INSERT INTO addresses (city, street, postcode, district) VALUES (?, ?, ?, ?)";
    private static final String GET_ADDRESSES_QUERY = "SELECT * FROM addresses";
    private static final String GET_ADDRESS_BY_ID_QUERY = "SELECT * FROM addresses WHERE id = ?";
    private static final String UPDATE_ADDRESS_QUERY = "UPDATE addresses SET city=?, district=?, street=? WHERE id = ?";
    private static final String DELETE_ADDRESS_QUERY = "DELETE from addresses WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    private final AddressMapper addressMapper;

    public JdbcAddressDao(JdbcTemplate jdbcTemplate, AddressMapper addressMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.addressMapper = addressMapper;
    }

    @Override
    public void create(Address address) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_ADDRESS_QUERY, new String[]{"id"});
            statement.setString(1, address.getCity());
            statement.setString(2, address.getStreet());
            statement.setString(3, address.getPostcode());
            statement.setString(4, address.getDistrict());

            return statement;
        }, keyHolder);

        address.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public List<Address> getAll() {
        return jdbcTemplate.query(GET_ADDRESSES_QUERY, addressMapper);
    }

    @Override
    public Address getById(int id) {
        return jdbcTemplate.queryForObject(GET_ADDRESS_BY_ID_QUERY, new Object[]{id}, addressMapper);
    }

    @Override
    public void update(Address address) {
        jdbcTemplate.update(UPDATE_ADDRESS_QUERY, address.getCity(), address.getDistrict(),
            address.getStreet(), address.getId());
    }

    @Override
    public void remove(Address address) {
        jdbcTemplate.update(DELETE_ADDRESS_QUERY, address.getId());
    }
}
