package com.mycompany.energyconsumuption.service.advice;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CentralizedPointCutAspect {
	@Pointcut("bean(*ServiceImpl) || bean(*Controller)")
	private void allServices(){
		
	}
	
	@Pointcut("bean(*Controller)")
	private void controllers(){
		
	}
	
	@Pointcut("within(com.mycompany.energyconsumuption.service..*)")
	private void packagesToInclude() {
	}
	
	@Pointcut("within(com.mycompany.energyconsumption.controller..*)")
	private void controllerPackages() {
	}
	
	@Pointcut("(packagesToInclude() || controllerPackages()) && (allServices() || controllers()) ")
	public void serviceClassesToLog() {
	}
}
