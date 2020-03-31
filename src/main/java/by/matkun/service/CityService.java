package by.matkun.service;

import by.matkun.model.City;

import java.util.Map;
import java.util.Set;

public interface CityService {
    Map<String,String> findAll();

    Set<City> setCities();

    City save(City city);

    City update(City city,City cityFromDb);

    void delete (Long id);
}
