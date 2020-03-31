package by.matkun.controller;

import by.matkun.model.City;
import by.matkun.service.CityServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/guide")
public class CityController {

    @Autowired
    private CityServiceImplement cityService;

    @GetMapping
    public Set<City> cities() {
        return cityService.setCities();
    }

    @PostMapping
    public City create(@RequestBody City city){
      return cityService.save(city);
    }

    @PutMapping("{id}")
    public City update (@PathVariable ("id") City cityFromDb,
                        @RequestBody City city){
       return cityService.update(city,cityFromDb);
    }

    @DeleteMapping("{id}")
    public void delete (@PathVariable ("id") City city){
        cityService.delete(city.getId());
    }
}
