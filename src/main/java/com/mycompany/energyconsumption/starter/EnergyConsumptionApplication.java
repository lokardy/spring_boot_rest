
package com.mycompany.energyconsumption.starter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mycompany.energyconsumption.entity.meter.Meter;
import com.mycompany.energyconsumption.repository.MeterRepository;

@SpringBootApplication
@EnableAutoConfiguration

@EntityScan( basePackages = {"com.mycompany.energyconsumption.entity"} )
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories("com.mycompany.energyconsumption.repository")
@ComponentScan(basePackages = {"com.mycompany.energyconsumuption.service", "com.mycompany.energyconsumption.controller", "com.mycompany.energyconsumuption.service.advice"})
public class EnergyConsumptionApplication {

	@Autowired
	private MeterRepository meterRepository;
	

	public static void main(String[] args) {
		SpringApplication.run(EnergyConsumptionApplication.class, args);
	}
	
	
	@PostConstruct
	public void postConstruct() {
		Meter meter = new Meter();
		
		meter.setMeterId("1001");
		
		Meter meter1 = meterRepository.save(meter);
		
		System.out.println(meter1.getMeterId());
	}
}
