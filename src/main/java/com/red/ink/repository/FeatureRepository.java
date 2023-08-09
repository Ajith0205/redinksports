/**
 * 
 */
package com.red.ink.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.red.ink.model.Feature;
import com.red.ink.model.ModuleTbl;

/**
 * @author ajith
 *
 */
public interface FeatureRepository extends JpaRepository<Feature, Long> {


//@Query("Select fe from Feature fe where fe.featureName =: featureName and fe.module =:moduleTbl")
//public Optional<Feature> findByModuleNameandFeatureName( ModuleTbl moduleTbl, String featureName);



public Optional<Feature> findByModuleTblAndFeatureName(ModuleTbl moduleTbl, String name);

public List<Feature> findAllByModuleTbl(ModuleTbl moduleTbl);



	

}
