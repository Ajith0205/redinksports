/**
 * 
 */
package com.red.ink.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.red.ink.constant.Constants;
import com.red.ink.dto.AcsssRightsDto;
import com.red.ink.dto.FeatureDto;
import com.red.ink.dto.ModuleTblDto;
import com.red.ink.jsonconstractor.JsonConstractor;
import com.red.ink.model.AccessRight;
import com.red.ink.model.Feature;
import com.red.ink.model.ModuleTbl;
import com.red.ink.model.Role;
import com.red.ink.model.User;
import com.red.ink.repository.AccessRightRepository;
import com.red.ink.repository.FeatureRepository;
import com.red.ink.repository.ModuleTblRepository;
import com.red.ink.repository.RoleRepository;
import com.red.ink.repository.UserRepository;
import com.red.ink.service.AccessRightService;

/**
 * @author ajith
 *
 */
@Service
public class AccessRightServiceImpl implements AccessRightService{
	
	@Autowired
	private AccessRightRepository accessRightRepository;
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	ModuleTblRepository moduleTblRepository;
	@Autowired
	FeatureRepository featureRepository;
	
	
	

	JsonConstractor jsonConstactor = new JsonConstractor();



//	@Override
//	public ResponseEntity<Object> save(AcsssRightsDto acsssRightsDto, String token) {
//		String jwtToken = token.replaceFirst("Bearer", "");
//		try {
//			DecodedJWT jwt = JWT.decode(jwtToken.trim());
//			Optional<User> loggedUser = userRepository.findByUsername(jwt.getSubject());
//			if (loggedUser.isPresent()) {
//				Optional<Role> role = roleRepository.findById(acsssRightsDto.getRoleId());	
//				if(role.isPresent()) {
//				for(ModuleTblDto module:acsssRightsDto.getModuleTblDto()) {
//					//Optional<ModuleTblDto> optionalModules = moduleTblRepository.findByModuleName(module.getModuleName());	
//					Optional<ModuleTbl>optionalModules=moduleTblRepository.findByName(module.getName());
//					
//					if (optionalModules.isPresent()) {
//						// set module in accessrights bean
//						List<FeatureDto> listFeaturesDtos = module.getFeatureDto();
//						if (listFeaturesDtos.size() > 0) {
//							for (FeatureDto featureDtos: listFeaturesDtos) {
//								Optional<Feature> feature = featureRepository
//										.findByModuleNameandFeatureName(optionalModules.get(), featureDtos.getName());
//								if (feature.isPresent()) {
//									Optional<AccessRight> accessRight = accessRightRepository
//											.findByRoleAndModuleAndFeature(role.get(), optionalModules.get(),
//													feature.get());
//									if (accessRight.isPresent()) {
//										accessRight.get().setStatus(featureDtos.isStatus());
//										
////										helper.track(accessRight.get(), accessRight.get().getId());
//										accessRightRepository.save(accessRight.get());
//									} else { 
//										AccessRight accessRights = new AccessRight();
//										accessRights.setId(accessRights.getId());
//										accessRights.setRole(role.get());
//										accessRights.setModuleTbl(optionalModules.get());
//										// set feature in accessrights bean
//										accessRights.setFeature(feature.get());
//										// set status in accessrights bean
//										accessRights.setStatus(featureDtos.isStatus());
////										helper.track(accessRights, accessRights.getId());
//										accessRightRepository.save(accessRights);
//									}
//									
//									return jsonConstactor.responseCreation(true,"Add AccessRights SucessFully",null, null,null);
//								} else {
//									return jsonConstactor.responseCreation( false,"No feature for User",null, null,null);
//								}
//							
//							}
//						}
//					} else {
//						
//					}
//					
//					}
//					
//				}
//				return jsonConstactor.responseCreation( false,"Access Denied  this User",null, null,null);
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//		return null;
//	}



//	@Override
//	public ResponseEntity<Object> findRightsByRole(String token, String roleId) {
//		String jwtToken = token.replaceFirst("Bearer", "");
//		try {
//			DecodedJWT jwt = JWT.decode(jwtToken.trim());
//			Optional<User> loggedUser = userRepository.findByUsername(jwt.getSubject());
//			if (loggedUser.isPresent()) {
//				if (roleId != null && roleId != "") {
//					Optional<Role>listrole=roleRepository.findByRole(roleId);
//					if(listrole.isPresent()) {
//						List<AccessRight> accessRights = accessRightRepository
//								.findAccessRightByRole(listrole.get());
//						if (accessRights.size() == 0) {
//							AcsssRightsDto accessRightsDto = new AcsssRightsDto();
//							List<ModuleTbl> listModules = moduleTblRepository.findAll();
//							List<ModuleTblDto> moduleDtos = new ArrayList<ModuleTblDto>();
//							for(ModuleTbl moduleTbl:listModules) {
//								List<Feature> listFeature = featureRepository.findFeatureNameByModule(moduleTbl);
//								
//								if (listFeature.size() > 0) {
//									ModuleTblDto moduleDto = new ModuleTblDto();
//									moduleDto.setId(moduleTbl.getId());
//									moduleDto.setName(moduleTbl.getModuleName());
//									moduleDto.setStatus(false);
//									
//									List<FeatureDto> listFeatureDtos = new ArrayList<FeatureDto>();
//									for(Feature feature:listFeature) {
//										FeatureDto featureDto = new FeatureDto();
//										featureDto.setId(feature.getId());
//										featureDto.setName(feature.getFeatureName());
//										featureDto.setStatus(false);
//										listFeatureDtos.add(featureDto);
//										
//									}
//									moduleDto.setFeatureDto(listFeatureDtos);
//									moduleDtos.add(moduleDto);
//								}
//								
//							}
//							accessRightsDto.setRoleId(listrole.get().getId());
//							accessRightsDto.setRoleName(listrole.get().getRole());
//							accessRightsDto.setModuleTblDto(moduleDtos);
//							return jsonConstactor.responseCreation(true,"Add AccessRights SucessFully",null, null,null);
//						}else {
//							AcsssRightsDto accessRightsDto = new AcsssRightsDto();
//							List<ModuleTbl> listModule = new ArrayList<>();
//							String moduleName = "";
//							for (AccessRight accessRights1 : accessRights) {
//								if (listModule.size() == 0) {
//									listModule.add(accessRights1.getModuleTbl());
//									moduleName = accessRights1.getModuleTbl().getModuleName();
//								} else {
//									if (!moduleName.equals(accessRights1.getModuleTbl().getModuleName())) {
//										listModule.add(accessRights1.getModuleTbl());
//										moduleName = accessRights1.getModuleTbl().getModuleName();
//									}
//								}
//							}
//							List<ModuleTblDto> moduleDtos = new ArrayList<ModuleTblDto>();
//							for (ModuleTbl module : listModule) {
//								List<AccessRight> listAccessRights = accessRightRepository
//										.findAccessRightByRoleAndModule(listrole.get(), module);
//								if (listAccessRights.size() > 0) {
//									ModuleTblDto moduleDto = new ModuleTblDto();
//									moduleDto.setId(module.getId());
//									moduleDto.setName(module.getModuleName());
//									moduleDto.setStatus(false);
//									// set features in modules
//									List<FeatureDto> listFeatureDtos = new ArrayList<FeatureDto>();
//									for (AccessRight accessRight : listAccessRights) {
//										FeatureDto featureDto = new FeatureDto();
//										featureDto.setId(accessRight.getFeature().getId());
//										featureDto.setName(accessRight.getFeature().getFeatureName());
//										featureDto.setStatus(accessRight.isStatus());
//										if (accessRight.isStatus()) {
//											moduleDto.setStatus(true);
//										}
//										listFeatureDtos.add(featureDto);
//									}
//									moduleDto.setFeatureDto(listFeatureDtos);
//									moduleDtos.add(moduleDto);
//								}
//							}
//							accessRightsDto.setRoleId(listrole.get().getId());
//							accessRightsDto.setRoleName(listrole.get().getRole());
//							accessRightsDto.setModuleTblDto(moduleDtos);
//							return jsonConstactor.responseCreation(true,"Add AccessRights SucessFully",null, null,null);
//						}
//					}
//				}
//			}
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//		return null;
//	}



	@Override
	public ResponseEntity<Object> createAccessRights(AcsssRightsDto acsssRightsDto, String token) {
		String jwtToken = token.replace("Bearer", "");
		try {
			DecodedJWT jwt = JWT.decode(jwtToken.trim());
			Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
			if (loggedInUser.isPresent()) {
				if (acsssRightsDto.getRoleId() != null) {
					Optional<Role> optUserRole = roleRepository.findById(acsssRightsDto.getRoleId());
					if (optUserRole.isPresent()) {
						if (acsssRightsDto.getModuleTblDto().size() > 0) {
							for (ModuleTblDto moduleTblDTO : acsssRightsDto.getModuleTblDto()) {
								Optional<ModuleTbl> optionalModules = moduleTblRepository
										.findByModuleName(moduleTblDTO.getName());
								if (optionalModules.isPresent()) {
									// set module in accessrights bean
									if (moduleTblDTO.getFeatureDto().size() > 0) {
										for (FeatureDto featureDTO : moduleTblDTO.getFeatureDto()) {
											Optional<Feature> feature = featureRepository.findByModuleTblAndFeatureName(
													optionalModules.get(), featureDTO.getName());
											Set<Feature> setFeature = new HashSet<Feature>();
											List<Long> featureIds = new ArrayList<Long>();
											featureIds.add(feature.get().getId());
											setFeature.add(feature.get());
											if (feature.isPresent()) {
												Optional<AccessRight> accessRight = accessRightRepository
														.findByRoleAndModuleTblAndFeature(optUserRole.get(),
																optionalModules.get(), feature.get().getId());
												// updation
												if (accessRight.isPresent()) {
													accessRight.get().setStatus(featureDTO.isStatus());
												
													accessRightRepository.save(accessRight.get());
												}
												// new creation
												else {
													AccessRight accessRights = new AccessRight();
													// set role in accessrights bean
													accessRights.setRole(optUserRole.get());
													// set status in accessrights bean
													accessRights.setModuleTbl(optionalModules.get());
													// set feature in accessrights bean
													Set<Feature> setFeature1 = new HashSet<Feature>();
													setFeature1.add(feature.get());
													accessRights.setFeature(setFeature1);
													accessRights.setStatus(featureDTO.isStatus());
													System.out.println(accessRights);
													accessRightRepository.saveAndFlush(accessRights);
												}
											} else {
												return jsonConstactor.responseCreation(false,"Failed","Feature is not present", null,null);
												
											}
										}
									}
								} else {
									return jsonConstactor.responseCreation(false, "Failed", "Module is empty", null,null);
								}
							}
							// success response
							return jsonConstactor.responseCreation(true, "Success", "Access Rights given successfully", null,null);
						} else {
							return jsonConstactor.responseCreation(false, "Failed", "User Role is not present", null,null);
						}
					} else {
						return jsonConstactor.responseCreation(false, "Failed", "User Role is not present", null,null);
					}
				} else {
					return jsonConstactor.responseCreation(false, "Failed", "User Role id is not valid", null,null);
				}
			} else {
				return jsonConstactor.responseCreation(false, "Failed", "Unauthorized User", null,null);
			}
		} catch (Exception e) {
			return jsonConstactor.responseCreation(false, "Failed", "try - catch exception", null,null);
		}
	}



@Override
public ResponseEntity<Object> getRightsByRole(String token, Long roleId) {
	String jwtToken = token.replaceFirst("Bearer", "");
	try {
		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		if (loggedInUser.isPresent()) {
			if (roleId != null) {
				Optional<Role> optUserRole = roleRepository.findById(roleId);
				if (optUserRole.isPresent()) {
					List<AccessRight> listAccessRights = accessRightRepository
							.findAccessRightsByRole(optUserRole.get());
					if (listAccessRights.size() == 0) {
						AcsssRightsDto accessRightsDto = new AcsssRightsDto();
						List<ModuleTbl> listModules = moduleTblRepository.findAll();
						List<ModuleTblDto> listModuleDtos = new ArrayList<ModuleTblDto>();
						List<String> modulesList = Constants.MODULES;
						if (listModules.size() > 0) {
							for (ModuleTbl moduleTbl : listModules) {
								if(modulesList.contains(moduleTbl.getModuleName())) {
									List<Feature> listFeatures = featureRepository.findAllByModuleTbl(moduleTbl);
									if (listFeatures.size() > 0) {
										ModuleTblDto moduleDto = new ModuleTblDto();
										moduleDto.setName(moduleTbl.getModuleName());
										moduleDto.setId(moduleTbl.getId());
										moduleDto.setStatus(false);
										List<FeatureDto> listFeatureDtos = new ArrayList<>();
										for (Feature feature : listFeatures) {
											FeatureDto featureDto = new FeatureDto();
											featureDto.setId(feature.getId());
											featureDto.setName(feature.getFeatureName());
											featureDto.setStatus(false);
											listFeatureDtos.add(featureDto);
										}
										moduleDto.setFeatureDto(listFeatureDtos);
										listModuleDtos.add(moduleDto);
									}
								}
							}
							accessRightsDto.setRoleId(optUserRole.get().getId());
							accessRightsDto.setRoleName(optUserRole.get().getRole());
							accessRightsDto.setModuleTblDto(listModuleDtos);
							
							return jsonConstactor.responseCreation(true, "success", "Access rights listed successfully by role",
								null);
						}
					} else {
						AcsssRightsDto accessRightsDto = new AcsssRightsDto();
						List<ModuleTbl> lisModules = new ArrayList<>();
						String modulesName = "";
						for (AccessRight accessRights : listAccessRights) {
							if (lisModules.size() == 0) {
								lisModules.add(accessRights.getModuleTbl());
								modulesName = accessRights.getModuleTbl().getModuleName();
							} else {
								if (!modulesName.equals(accessRights.getModuleTbl().getModuleName())) {
									lisModules.add(accessRights.getModuleTbl());
									modulesName = accessRights.getModuleTbl().getModuleName();

								}
							}
						}
						List<ModuleTblDto> listModuleDtos = new ArrayList<>();
						for (ModuleTbl modules : lisModules) {
							List<AccessRight> listAccessRights1 = accessRightRepository
									.findAllByRoleAndModuleTbl(optUserRole.get(), modules);
							if (listAccessRights1.size() > 0) {
								ModuleTblDto moduleDto1 = new ModuleTblDto();
								moduleDto1.setId(modules.getId());
								moduleDto1.setName(modules.getModuleName());
								moduleDto1.setStatus(false);
								List<FeatureDto> listFeatureDtos = new ArrayList<>();
								for (AccessRight accessRights : listAccessRights1) {
//									System.out.println(accessRights.getId());
//									System.out.println(accessRights.getFeature());
									for (Feature feature : accessRights.getFeature()) {
										FeatureDto featureDto = new FeatureDto();
										featureDto.setId(feature.getId());
										featureDto.setName(feature.getFeatureName());
										featureDto.setStatus(accessRights.isStatus());
										if (accessRights.isStatus()) {
											moduleDto1.setStatus(true);
										}
										listFeatureDtos.add(featureDto);
									}
								}
								moduleDto1.setFeatureDto(listFeatureDtos);
								listModuleDtos.add(moduleDto1);
							}
						}
						accessRightsDto.setModuleTblDto(listModuleDtos);
						accessRightsDto.setRoleId(optUserRole.get().getId());
						accessRightsDto.setRoleName(optUserRole.get().getRole());
						
						
						return jsonConstactor.responseCreation(true, "success", "Access rights listed successfully by role",
							null);
					}
				} else {
					return jsonConstactor.responseCreation(false, "Failed", "User Role id is not Present", null,null);
				}
			} else {
				return jsonConstactor.responseCreation(false, "Failed", "User Role id is not valid", null,null);
			}
		} else {
			return jsonConstactor.responseCreation(false, "Failed", "Unauthorized User", null,null);
		}
	} catch (Exception e) {
		return jsonConstactor.responseCreation(false, "Failed", "try - catch exception", null,null);
	}
	return null;
}



	




	
	}
