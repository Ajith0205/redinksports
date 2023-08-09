package com.red.ink.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.red.ink.model.Role;
import com.red.ink.repository.RoleRepository;
import com.red.ink.service.RoleService;

@Service
public class RoleServiceImpl  implements RoleService{
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role getById(Long roleId) {
		// TODO Auto-generated method stub
		return roleRepository.getById(roleId);
	}

	
	

	
		

}
