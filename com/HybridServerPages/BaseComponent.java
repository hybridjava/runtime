package com.HybridServerPages;

//import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Derive component state classes ("*_WS.java") from this class
 */
public class BaseComponent {

BaseComponent parent_component;
String storage_id;
String dyn_id;
BasePage page;
String short_name;
String full_id;
ComponentReference representative;
HttpServletRequest request;

/**
 * Returns the short name of the component
 * @return the short name of the component
 */
final public String getShortName(){return this.short_name;};

// the folowing four are extracted from generated static members
int shallow_rank;
int deep_rank;
int senior_compid = -1;
int senior_loops;

void send(int code, String msg, Object more,BaseComponent src){
    BaseComponent pc = parent_component;
    if(representative != null){
        pc = representative.parent_component;
    }
    if (pc == null){
        page.$accept(code,msg,more,src);
    } else {
        // the following assignment is for resend to work;
        pc.signal_is_here = true;
        pc.src = src;
        pc.code = code;
        pc.msg = msg;
        pc.more = more;
        pc.$accept(code,msg,more,src);
        pc.signal_is_here = false;
    }
} //$send()

int code;
String msg;
Object more;
boolean signal_is_here = false;
BaseComponent src = null;

final void resend(){
    if(signal_is_here){
        send(this.code,this.msg,this.more,this.src);
        signal_is_here = false;
    }
}

final void setBadParam(String name,String msg){
    String key = name + ':' + full_id;
    if(page.invalid_params == null)
        page.invalid_params = new HashMap<String,String>();
    page.invalid_params.put(key,msg);
} //setBadParam()

/**
 *  Returns unique id of the component state instance
 *  @return unique id of the component state instance
 */
final public String getUniqueId(){
    return storage_id;
}

/**
 * Send a signal to the parent component or to the page
 * @param code some value
 * @param msg some message
 * @param more some additional data
*/
final protected void $send(int code, String msg, Object more){
    send(code,msg,more,this);
} //$send()

/**
 * Resends the last accepted message (code,msg,more,source) upwards
*/
protected final void $resend(){
    if(representative!=null)
        representative.resend();
    else
        resend();
}

/**
 * Override this method to get signals from sub-components.
 * Otherwise the signal will be propagated up
 * @param code some value
 * @param msg some message
 * @param more some additional data
 * @param source the component that fired the signal
 */
protected void $accept(int code, String msg, Object more,BaseComponent source){
    this.src = source;
    $resend();
} //$accept()

/**
 * If the actual submission is unrelated to current component
 * then empty strings are passed to both parameters.
 * @param h1 first (name) part (before ':') of HJ_Action HTTP parameter
 * @param h2 second (value) part of HJ_Action HTTP parameter <br>
 * Using the HJ_Action is optional as HTTP request is also available <br>
 * To extract request parameters relevant to this component use
 * <code> String $getParam(String name) </code>
 * This method is called for every component with respect to their seniority
 * AND before $handlePage.<br>
*/
protected void $handleComponent(String h1,String h2){}

/**
 * Marks the parameter as bad
 * @param name of the parameter that did not pass validation
 * @param msg message to be shown at the control
 */
final protected void $setBadParam(String name,String msg){
    if(representative != null)
        representative.setBadParam(name,msg);
    else
        setBadParam(name,msg);
} //$setBadParam()

/**
 * returns message if the parameter did not pass validation
 * @param name of the parameter
 * @return msg message to be shown at the control

final public String badParam(String name){
    String key = name + ':' + full_id;
    rt.traceln("key=" + key);
    return page.badParam(key);
} */

/**
 * Access to component-related HTTP request parameters
 * @param name component-scope name of the parameter
 * @param previous_value - returned if no such parameter
 * in the current HTTP request
 * @return parameter value.
 */
final protected String $getParam(String name,String previous_value){
    if(representative!=null){
        return representative.$getParam(name,previous_value);
    }
    String key = name + ':' + full_id;
    String ret = null;
    ret = request.getParameter(key);
    if(ret == null)
        return previous_value;
    return ret;
} //$getParam()

/**
 * Sets the next page equal to the page you came here from
 * This is a support for back button
 */
final protected void $setReturn(){ // called from widget handle
    if(representative != null){
        representative.page.return_to_previous_page = true;
        return;
    }
    page.return_to_previous_page = true;
} //$setReturn()

/**
 * Sets the next page to be shown. If called more than once the last call is effective
 * @param pn name of the page a.k.a. full class name of the page
 * @return false for all calls except the first one
 */
protected final boolean $setNextPage(String pn){
    //if(Usr.dbg)System.err.println(this.getClass()+".$setNextPage(\""+pn+"\")");
    if(representative != null)
        return representative.$setNextPage(pn);
    boolean ret = page.next_page_name == null;
    page.next_page_name = pn;
    return ret;
} //$setNextPage()

/**
 * Override this method to process Ajax request at the component level
 * @param request standard
 * @param response standard
*/
protected void $ajaxComponent(HttpServletRequest request,HttpServletResponse response){}
} //class
