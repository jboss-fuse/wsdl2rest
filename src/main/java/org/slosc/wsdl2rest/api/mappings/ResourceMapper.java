package org.slosc.wsdl2rest.api.mappings;

import java.util.List;

import org.slosc.wsdl2rest.api.service.ClassDefinition;

public interface ResourceMapper {
	void mapResources(String resourceName);
	List<String> getResources();
	String toString();

    void assignResources(List<ClassDefinition> svcClasses);
}
