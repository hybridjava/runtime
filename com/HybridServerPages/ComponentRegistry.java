package com.HybridServerPages;

final class ComponentRegistry {
static private final int max_magic_stack = 39;
private ComponentSubRegistry [] array = new ComponentSubRegistry [max_magic_stack];

void add(BaseComponent component){
    int rank = component.deep_rank;
	if(array[rank] == null){
		array[rank] = new ComponentSubRegistry(); 
	}
	array[rank].add(component);
} //add()

BaseComponent getComponentInstance(int rank, int index){
	if(index >= array[rank].size())
		return null;
	return array[rank].get(index);
} //getComponentInstance() 

ComponentSubRegistry getSubRegistry(int k){
	return array[k];
} //getSubRegistry()

} //class
