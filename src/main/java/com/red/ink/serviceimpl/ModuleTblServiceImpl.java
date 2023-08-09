/**
 * 
 */
package com.red.ink.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.red.ink.repository.ModuleTblRepository;
import com.red.ink.service.ModuleTblService;

/**
 * @author ajith
 *
 */
@Service
public class ModuleTblServiceImpl implements ModuleTblService{
	
	@Autowired
	ModuleTblRepository moduleTblRepository;

}
