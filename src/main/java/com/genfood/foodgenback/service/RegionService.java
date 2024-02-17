package com.genfood.foodgenback.service;

import com.genfood.foodgenback.repository.RegionRepository;
import com.genfood.foodgenback.repository.model.Region;
import com.genfood.foodgenback.repository.model.exception.NotFoundException;
import com.genfood.foodgenback.repository.validator.RegionValidator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegionService {
  private final RegionRepository repository;
  private final RegionValidator validator;

  public List<Region> getRegions(Integer page, Integer pageSize) {
    Pageable pageable = PageRequest.of(page, pageSize);
    return repository.findAll(pageable).toList();
  }

  public List<Region> crupdateRegions(List<Region> toCrupdate) {
    validator.accept(toCrupdate);
    return repository.saveAll(toCrupdate);
  }

  public Region getRegionById(String id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("Region name with id: "+id+" not found"));
  }

  public void removeById(String id) {
    repository.deleteById(id);
  }
}
