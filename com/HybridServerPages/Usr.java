package com.HybridServerPages;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class Usr {

private Usr(){}

public static boolean dbg = true;

/**
 * Called only from the servlet (on receiving HJ_Page parameter in request)
 * @param page_name name of the page to be rendered
 * (in the form of a Java class name)
 * @param request standard
 * @param response standard
 * @param servlet this - servlet instance itself
 */
public static void $renderPage(String page_name,HttpServletRequest request,
    HttpServletResponse response,HttpServlet servlet){
    if(!page_name.endsWith("_P")){
        page_name += "_P"; // compatability with version 1.01
    }
    BasePage page = rt.getPageInstance(page_name,request,response,servlet);
    rt.renderPage(page,rt.getPrintWriter(response));
    page.request = null;
}// $renderPage()

/**
 * Called only from the servlet
 * @param handled_page_name name of the previously shown page
 * (in the form of a Java class name)
 * @param request
 * @param response
 * @param servlet
 * @return the name of the page rendered
 */
public static String $handlePage(String handled_page_name,
            HttpServletRequest request, HttpServletResponse response,
            HttpServlet servlet){
    BasePage page2handle;
    BasePage page2render;
    try{
        page2handle = rt.getPageInstance(handled_page_name,request,response,servlet);
        boolean hasAS = page2handle.$a();
        String h1 = page2handle.verb(request);
        String h3 = page2handle.target(request);
        String h2 = page2handle.value(request);
        page2handle.return_to_previous_page = false;
        page2handle.next_page_name = null;
        page2handle.dispatch(h1,h2,h3); // dispatch to components
        if(h3.length() == 0 ){
            page2handle.$handle(h1,h2);
        } else {
            page2handle.$handle("","");
        }
        if(hasAS){
            BaseApplication as = rt.getAS(page2handle.session,page2handle.servlet);
            as.$handleAS(page2handle,h1,h2);
        }
        // while handling:
        // 1) page2handle.return_to_previous_page could be set
        // 2) page2handle.invalid_params not empty
        // 3) page2handle.next_page_name set
        // 4) none of previous
        if(page2handle.return_to_previous_page){
            page2render = page2handle.popPage();
            if(page2render == null){
                page2render = page2handle; //same page
            }
        } else if(page2handle.invalid_params !=null && !page2handle.invalid_params.isEmpty()){
            page2render = page2handle; //same page
        } else if(page2handle.next_page_name != null){
            page2render = rt.getPageInstance(page2handle.next_page_name,
                    request, response, servlet);
            page2render.pushPage(page2handle);
        } else {
            page2render = page2handle; // same page
        }
    }catch(Throwable ex){
        rt.errorPage(ex,
                "Exception in the $handlePage method of page \"" + handled_page_name + "\"",
                request,response);
        return "Error page";
    }
    PrintWriter pw = rt.getPrintWriter(response);
    page2render.request = request;
    page2render.session = request.getSession();
    page2render.servlet = servlet;
    rt.renderPage(page2render,pw);
    if(page2handle.invalid_params != null){
        page2handle.invalid_params.clear();
    } //if
    page2render.request=null;
    page2handle.request = null;
    return page2render.getShortPageName();
} //$handlePage()

/**
 * Called only from the servlet (on receiving HJ_Ajax parameter in request)
 * @param id id of an element. For component level ajax request same as a component id.
 * @param request standard
 * @param response standard
 */
public static void $ajax(String page_name,String id,
        HttpServletRequest request,HttpServletResponse response){
    HttpSession session = request.getSession();
    BasePage page = (BasePage)session.getAttribute(page_name);
    if(page == null)
        return;
    String storage_id = page.$getPageId() + id;
    BaseComponent comp = (BaseComponent)page.session.getAttribute(storage_id);
    if(comp != null)
        comp.$ajaxComponent(request,response);
    else
        page.$ajax(request,response);
}// $renderPage()

} //class
