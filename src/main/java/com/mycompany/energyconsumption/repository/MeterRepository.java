
package com.mycompany.energyconsumption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.energyconsumption.entity.meter.Meter;

@Repository
public interface MeterRepository extends JpaRepository<Meter, String> {

}
