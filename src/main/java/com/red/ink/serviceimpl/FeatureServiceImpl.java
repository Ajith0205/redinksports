/**
 * 
 */
package com.red.ink.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.red.ink.repository.FeatureRepository;
import com.red.ink.service.FeatureService;

/**
 * @author ajith
 *
 */

@Service
public class FeatureServiceImpl implements FeatureService{
	@Autowired
	private FeatureRepository featureRepository;

}
