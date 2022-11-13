package com.foxminded.university.dao.jdbc.mappers;

import com.foxminded.university.model.Address;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AddressMapper implements RowMapper<Address> {

    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = new Address();
        address.setId(rs.getInt("id"));
        address.setCity(rs.getString("city"));
        address.setStreet(rs.getString("street"));
        address.setPostcode(rs.getString("postcode"));
        address.setDistrict(rs.getString("district"));

        return address;
    }
}
