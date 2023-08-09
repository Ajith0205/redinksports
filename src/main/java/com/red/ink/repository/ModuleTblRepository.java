/**
 * 
 */
package com.red.ink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.red.ink.dto.ModuleTblDto;
import com.red.ink.model.ModuleTbl;

/**
 * @author ajith
 *
 */
public interface ModuleTblRepository extends JpaRepository<ModuleTbl, Long>{

	Optional<ModuleTbl> findByModuleName(String moduleName);

//	Optional<ModuleTbl> findByName(String name);

}
