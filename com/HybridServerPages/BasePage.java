package com.HybridServerPages;

import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Derive page state classes ("*_PS.java") from this class
 */
public class BasePage {
protected HttpServletRequest request;
protected HttpSession session;
private String action = null;
private String[] ww = null;
BasePage prev_page;

final void pushPage(BasePage previous_page){
    if(this == previous_page)
        return; // we never set return to the same page
    if(prev_page != null){ // prev page occupied, forget all the loop
        BasePage p = previous_page;
        while(p != this){
            if(p == null){
                this.prev_page = previous_page;
                break;
            }
            BasePage tmp = p.prev_page;
            p.prev_page = null;
            p = tmp;
        } //while
    } else {
        this.prev_page = previous_page;
    } // prev_page == null
} //pushPage()

final BasePage popPage(){
    BasePage ret = prev_page;
    prev_page = null;
    return ret;
} //popPage()

/**Do not touch*/protected String $getPageId(){assert false; return "???";}
/**Do not touch*/protected int [] $R(){return null;};
/**Do not touch*/protected int [] $I(){return null;};
/**Do not touch*/protected int [] $SI(){return null;};
/**Do not touch*/protected int [] $SL(){return null;};
/**Do not touch*/protected int [] $SR(){return null;};
/**Do not touch*/protected int [] $DR(){return null;};
/**Do not touch*/protected int [] $SH(){return null;};
/**Do not touch*/protected void $set_$SI2(int[] arg){};
/**Do not touch*/protected void $set_$SL2(int[] arg){};
/**Do not touch*/protected void $set_$SR2(int[] arg){};
/**Do not touch*/protected void $set_$DR2(int[] arg){};
/**Do not touch*/protected void $set_$SH2(int[] arg){};
/**Do not touch*/protected int $get_$SI2(int index){return -999;};
/**Do not touch*/protected int $get_$SL2(int index){return -999;};
/**Do not touch*/protected int $get_$SR2(int index){return -999;};
/**Do not touch*/protected int $get_$DR2(int index){return -999;};
/**Do not touch*/protected int $get_$SH2(int index){return -999;};
/**Do not touch*/protected boolean $isLoaded(){return true;} // true if nothing to load ?
/**Do not touch*/protected void $setLoaded(){}
/**Do not touch*/protected void $render(PrintWriter $){}
/**Do not touch*/protected boolean $a(){return false;} // is a componenet
HashMap<String,String> invalid_params = null;
boolean return_to_previous_page;
HttpServlet servlet;
String next_page_name;

final void dispatch(String h1,String h2,String h3){
    BaseComponent submitter = null;
    if(h3.length() != 0 ){
        String storage_id = $getPageId() + h3;
        submitter = (BaseComponent)session.getAttribute(storage_id);
    }
    rt.globalDispatch(h1,h2,$R(),this,submitter);
} //dispatch()

/**
 * Override this method to process Ajax request at the page level
 * @param request standard
 * @param response standard
*/
protected void $ajax(HttpServletRequest request,HttpServletResponse response){}

void fillDom(){
    this.$set_$SI2(fillOneWideArray( $I(),$SI() ));
    this.$set_$SL2(fillOneWideArray( $I(),$SL() ));
    this.$set_$SR2(fillOneWideArray( $I(),$SR() ));
    this.$set_$DR2(fillOneWideArray( $I(),$DR() ));
    this.$set_$SH2(fillOneWideArray( $I(),$SH() ));
    this.$setLoaded();
} //fillDom()

private static int[] fillOneWideArray(int[] ids, int[]values){
    if(ids == null || ids.length == 0)
        return null;
    int id_max = -333;
    for(int i=0; i < ids.length; i++){
        if(ids[i] > id_max)
            id_max = ids[i];
    }
    if(id_max == -333)
        return null;
    int [] res = new int[id_max+3];
    java.util.Arrays.fill(res,-777);
    for(int i=0; i < ids.length; i++){
        int index = ids[i];
        int x = values[i];
        res[index] = x;
    } //for
    return res;
} //fillOneWideArray()

String verb(HttpServletRequest request){
    action = request.getParameter("HJ_action");
    if(action == null)
        return "";
    ww = action.split(":");
    if(ww == null)
        return "";
    if(ww.length < 1)
        return "";
    return ww[0];
}

String target(HttpServletRequest request){
    if(action == null)
        return "";
    if(ww.length < 2)
        return "";
    return ww[1];
}

String value(HttpServletRequest request){
    if(action == null)
        return "";
    if(ww == null)
        return "";
    if(ww.length < 3)
        return "";
    if(ww.length > 3){ // some more : now in the value
        int pos = ww[0].length() + ww[1].length();
        return action.substring(pos);
    }
    return ww[2];
}

/**
 * Override this method to get signals from sub-components.
 * Otherwise the signal will be propagated up
 * @param code some value
 * @param msg some message
 * @param more some additional data
 * @param source the component that fired the signal
 */
protected void $accept(int code, String msg, Object more,BaseComponent source){}

/**
 * Override this method to process HTTP request
 * @param h1 first (name) part (before ':') of HJ_Action HTTP parameter
 * @param h2 second (value) part of HJ_Action HTTP parameter
 * Note: Using the HJ_Action is optional as HTTP request is always available
*/
protected void $handle(String h1,String h2){}

/**
 * Access to Application State instance
 * @return Application State instance if you've provided AS.java class
 */
final protected BaseApplication $getAS(){
    if(servlet == null){
        String cn = this.getClass().getPackage().getName() + ".AS";
        return (BaseApplication)rt.createInstance(cn); // for TestRunner
    }
    return rt.getAS(session,servlet);
} //$getAS()

/**
 * Returns fully qualified name of the currently processed page presentation class
 * @return fully qualified name of the currently processed page presentation class
 */
protected String getPageName(){
	assert false;
	return null;
} //getPageName()

/**
 * Returns the short name of the current page
 * @return short name of the current page
 */
protected String getShortPageName(){
    assert false;
    return null;
} //getPageName()

/**
 * This is for support of the back button
 */
final protected void $setReturn(){ // called from handle
  return_to_previous_page = true;
} //$setReturn()

/**
 * returns message if the parameter did not pass validation
 * @param name of the parameter
 * @param id id of component
 * @return msg message to be shown at the control
 */

final protected String badParam(String name,String id){
    if(invalid_params == null){
        return null;
    }
    String ret =  invalid_params.get(name + ":" + id);
    invalid_params.remove(name);
    return ret;
} //badParam()

/**
 * Marks the parameter as bad
 * @param name of the parameter that did not pass validation
 * @param msg message to be shown at the control
 */
final protected void $setBadParam(String name,String msg){
    if(invalid_params == null)
        invalid_params = new HashMap<String,String>();
    invalid_params.put(name,msg);
} //$setBadParam()

/**
 * Access to page-related HTTP request parameters
 * @param name component-scope name of the parameter
 * @param previous_value returned if no such parameter in
 * the current HTTP request. null is OK
 * @return parameter value.
 */
final protected String $getParam(String name,
        String previous_value){
    String ret = null;
    ret = request.getParameter(name);
    if(ret == null)
        return previous_value;
    return ret;
} //$getParam()

/**
 * Access to current component from widget,
 * call it as getComponent($#)
 * @param id id of the component
 * @return Instance of the component; null for page
 */
final protected BaseComponent getComponent(String id){
     String storage_id = $getPageId() + id;
     return (BaseComponent)session.getAttribute(storage_id);
} //getComponent()

/**
 * Sets the next page to be shown. If called more than once the last call is effective
 * @param pn name of the page a.k.a. full class name of the page
 * @return false for all calls except the first one
 */
protected final boolean $setNextPage(String pn){
    //if(Usr.dbg)System.err.println(this.getClass()+".$setNextPage(\""+pn+"\")");
    boolean ret = next_page_name == null;
    next_page_name = pn;
    return ret;
} //$setNextPage()

} //class