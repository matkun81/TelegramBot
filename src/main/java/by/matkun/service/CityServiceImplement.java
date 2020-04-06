package by.matkun.service;

import by.matkun.dao.CityRepository;
import by.matkun.model.City;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CityServiceImplement implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public Map<String,String> findAll() {
        return setCities().stream()
                .collect(Collectors.toMap(City::getName,City::getDescription));
    }

    @Override
    public Set<City> setCities() {
        return new HashSet<>(cityRepository.findAll());
    }

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public City update(City city,City cityFromDb) {
        BeanUtils.copyProperties(city,cityFromDb,"id");
        return cityRepository.save(city);
    }

    @Override
    public void delete(Long id) {
        cityRepository.deleteById(id);
    }
}
