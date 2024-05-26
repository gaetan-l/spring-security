package com.training.demo.repository;

import com.training.demo.model.Role;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends ListCrudRepository<Role, Long> {}
