
package com.mycompany.energyconsumption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.energyconsumption.entity.profile.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>  {

	Profile findByName(String name);
}
