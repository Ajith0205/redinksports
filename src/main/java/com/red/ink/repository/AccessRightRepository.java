/**
 * 
 */
package com.red.ink.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.red.ink.model.AccessRight;
import com.red.ink.model.Feature;
import com.red.ink.model.ModuleTbl;
import com.red.ink.model.Role;

/**
 * @author ajith
 *
 */
public interface AccessRightRepository extends JpaRepository<AccessRight, Long>{
	
   


public	List<AccessRight> findAccessRightByRole(Role role);

//public List<AccessRight> findAccessRightByRoleAndModule(Role role, ModuleTbl module);

public Optional<AccessRight> findByRoleAndModuleTblAndFeature(Role role, ModuleTbl moduleTbl, Long id);

public List<AccessRight> findAccessRightsByRole(Role role);

public List<AccessRight> findAllByRoleAndModuleTbl(Role role, ModuleTbl modules);





}
