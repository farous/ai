package com.sivalabs.aidemo.ragdb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccedantRepository extends JpaRepository<Accedant, Long> {
}