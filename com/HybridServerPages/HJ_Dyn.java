package com.HybridServerPages;

// only one HJ_Dyn object is created for a whole page
final public class HJ_Dyn {
final static char dyn_id_delim = '-';
int depth = -1; // total loops above = depth + 1;
private final StringBuffer sb = new StringBuffer();
final int[] loop_path = new int[10]; // size may be calculated exactly

//@SuppressWarnings("unchecked")
public HJ_Dyn(){} //~

public void $eL(){
    depth++; // 0 for the first loop
    loop_path[depth] = -1; // N of executions
}// $eL()

public void $iL(){
    loop_path[depth]++; // N of iteration starting from 0
}// iL()

public void $lL(){
    loop_path[depth] = -1;
    depth--;
}// $lL()

public String $compId(int component_loops){
    if(component_loops == 0)
        return "";
    StringBuffer ret = new StringBuffer();
    for(int i = 0;i < component_loops;i++){
        ret.append(dyn_id_delim);
        ret.append(loop_path[i]);
    }
    return ret.toString();    
}

public String getDynId(){
    if(depth == -1){
        return "";
    }else{
        sb.setLength(0);
        for(int i = 0;i <= depth; i++){
            sb.append(dyn_id_delim);
            sb.append(loop_path[i]);
        }
        String ret = sb.toString(); 
        return ret;
    }// else
} //getDynId()

} //class

