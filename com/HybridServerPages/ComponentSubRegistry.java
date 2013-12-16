package com.HybridServerPages;
import java.util.ArrayList;
import java.util.List;

final class ComponentSubRegistry {
private List<BaseComponent> list;

void add(BaseComponent component){
	if (list == null)
		list = new ArrayList<BaseComponent>();
	list.add(component);
} //add()

int size(){
	if(list == null)
		return 0;
	return list.size();
} //size()

BaseComponent get(int index){
	return list.get(index);
} //get()

} //class
