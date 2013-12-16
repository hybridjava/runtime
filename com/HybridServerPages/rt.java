package com.HybridServerPages;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class rt {
static private String w_reg_pref = "r-";
private static String empty_string = new String();

static public void $t(PrintWriter out,char c) throws IOException{
    if(c == '<'){
        $o(out,"&lt;");
    }else if(c == '>'){
        $o(out,"&gt;");
    }else if(c == '&'){
        $o(out,"&amp;");
    }else if(c == '\"'){
        $o(out,"&quot;");
    }else if(c == '\n'){
        $o(out,"\n");
        assert false; // use nl() !
    }else{
        out.print(c);
    }
}

static public void $t(PrintWriter out,char[] s) throws IOException{
    if(s == null)
        return;
    int len = s.length;
    for(int i = 0;i < len;i++){
        char c = s[i];
        if(c == '\r'){
            continue;
        }
        $t(out,c);
    } // for
}

static public void $t(PrintWriter out,Object obj) throws IOException{
    if(obj == null)
        return;
    out.print(obj);
}

static public void $t(PrintWriter out,byte by) throws IOException{out.print(by);}
static public void $l(PrintWriter out){out.println();}
static public void $o(PrintWriter out,char c) throws IOException{out.print(c);}
static public void $t(PrintWriter out,double d) throws IOException{out.print(d);}
static public void $t(PrintWriter out,float f) throws IOException{out.print(f);}
static public void $t(PrintWriter out,int i) throws IOException{out.print(i);}
static public void $t(PrintWriter out,long l) throws IOException{out.print(l);}
static public void $t(PrintWriter out,short sh) throws IOException{out.print(sh);}
static public void $t(PrintWriter out,boolean b) throws IOException{out.print(b);}
static public void $blank(PrintWriter out){out.print(' ');}

static public void $o(PrintWriter out,String s){
    if(s == null)
        return;
    out.print(s);
} //$o()

static public void $t(PrintWriter out,String s) throws IOException{
    // called from text
    if(s == null)
        return;
    int len = s.length(); // to be used to render strings to HTML PCDATA
    for(int i = 0;i < len;i++){
        char c = s.charAt(i);
        if(c == '\r'){
            continue;
        }
        $t(out,c);
    } //for
} //$t()

private static String getStackTrace(Throwable th){
    StringWriter s = new StringWriter();
    th.printStackTrace(new PrintWriter(s));
    return s.toString();
}

private static Class<?>[] types = new Class[2];
static{
    try{
        types[0] = Class.forName("javax.servlet.http.HttpServletRequest");
        types[1] = Class.forName("javax.servlet.http.HttpServletResponse");
    }catch(ClassNotFoundException ex){
        System.err.println("rt::static init: " + ex);
    }
} //static

/** called from Usr.$handlePage() and Usr.$renderPage
 * @param page_name fully qualified class name with _P suffix
 * @param request
 * @param response
 * @param servlet
 */
static BasePage getPageInstance(String page_name,HttpServletRequest request,
        HttpServletResponse response,HttpServlet servlet){
    HttpSession session = request.getSession();
    BasePage page = (BasePage)session.getAttribute(page_name);
    if(page == null){
        page = (BasePage)createInstance(page_name);
        session.setAttribute(page_name,page);
        if(page == null)
            System.err.println("Could not create " + page_name);
    } //if
    page.request = request;
    page.session = session;
    page.servlet = servlet;
    if(!page.$isLoaded())
        page.fillDom();
    return page;
} //getPageInstanse()

static Object createInstance(String cn){
    try{
        Class<?> cl = Class.forName(cn);
        Object ret = cl.newInstance();
        if(ret == null){
            System.err.println("Failed to create instance of " + cn);
        }
        return ret;
    }catch(ClassNotFoundException e){
        throw new RuntimeException(e);
    }catch(InstantiationException e){
        throw new RuntimeException(e);
    }catch(IllegalAccessException e){
        throw new RuntimeException(e);
    }catch(RuntimeException ex){
        System.err.println("Failed to create instance of " + cn);
        System.err.println("rt::createInstance: " + ex);
        throw ex;
    }
} //createInstance()

static void renderStack(Throwable th,PrintWriter ps,String msg){
    if(th == null)
        return;
    if(ps != null)
        ps.println(" <p><font color=red size=\"-1\"> Exception " + msg + ":<br>");
    String s = getStackTrace(th);
    String[] result = s.split("\n");
    for(int i = 0;i < result.length;i++){
        if(ps != null){
            ps.print(result[i]);
            ps.println("<br>");
        }
        System.err.println(result[i]);
        if(result[i].indexOf("doPost") != -1)
            break;
        if(result[i].indexOf("doGet") != -1)
            break;
    }// for
    if(ps != null){
        ps.println();
        ps.println("</font></p>");
    }
    System.err.println("-------------------------------");
    System.err.flush();
}// renderStack()

static BaseApplication getAS(HttpSession session,HttpServlet servlet){
    StringBuffer sb = new StringBuffer(servlet.getClass().getPackage().getName());
    sb.append(".AS"); String cn = sb.toString();
    BaseApplication as = (BaseApplication)session.getAttribute(cn);
    if(as == null){
        as = (BaseApplication)createInstance(cn);
        session.setAttribute(cn,as);
    }
    return as;
} //getAS()

/*
 * Called only from generated page code.
 * Component instances are creaded at the moment they are
 * first time delivered to the page.
 */
public static BaseComponent $getComponentInstance(BasePage page,
        HJ_Dyn dyn,int component_id, String cn, String short_name,int wc_id){
    // we need wc_id to generate ids in shared areas
	// is called only for components
    String dyn_id =  dyn.getDynId();
    String full_id = Integer.toString(component_id) + dyn_id;
    String storage_id = page.$getPageId() + full_id;
    BaseComponent component = (BaseComponent)page.session.getAttribute(storage_id);
    if(component == null){
        int share_area_id = page.$get_$SH2(component_id);
        if(share_area_id != 0){ // this is a shared component
            // this is the only place where ComponentReference instance is created
            component = new ComponentReference(page,cn, short_name,share_area_id,wc_id,dyn_id);
        } else {
            component = (BaseComponent)createInstance(cn);
        }
        component.short_name = short_name;
        component.dyn_id        = dyn_id;
        component.shallow_rank  = page.$get_$SR2(component_id);
        component.deep_rank     = page.$get_$DR2(component_id);
        component.senior_compid = page.$get_$SI2(component_id);
        component.senior_loops  = page.$get_$SL2(component_id);
        component.storage_id = storage_id;
        component.full_id = full_id;
        page.session.setAttribute(storage_id,component);
        String key = w_reg_pref + page.$getPageId();
        ComponentRegistry wr = (ComponentRegistry)page.session.getAttribute(key);
        if(wr == null){
        	wr = new ComponentRegistry();
        	page.session.setAttribute(key,wr);
        } //if
        component.page = page;
        if(component.senior_compid > -1){
            String target_dyn_id = calcDynId(component.dyn_id,component.senior_loops);
            String target_full_id = page.$getPageId() + Integer.toString(component.senior_compid) + target_dyn_id;
            component.parent_component = (BaseComponent)page.session.getAttribute(target_full_id);
        }
        wr.add(component);
    } //if(component == null)

    if(component instanceof ComponentReference){
        ComponentReference ref = (ComponentReference)component;
        ref.referred.representative = ref;
        return ref.referred;
    }
    return component;
}// $getComponentInstance()

static public void $o(PrintWriter out,HJ_Dyn dyn,int component_loops) throws IOException{
    for(int i = 0;i < component_loops;i++){
        out.print(HJ_Dyn.dyn_id_delim);
        out.print(dyn.loop_path[i]);
    }
}

static void renderPage(BasePage page,PrintWriter pw){
    // called from 2 places - Usr.$handlePage and Usr.$renderPage
    try{
        page.$render(pw);
    }catch(Throwable ex){
        String pn = page.getPageName();
        $renderError(ex,pn,null,true, -1);
    }finally {
        page.request = null;
    } //try
} //renderPage()

static void globalDispatch(String verb,String value,
		int [] ranks,BasePage page, BaseComponent submitter){
	// calls all the widget handlers
    String key = w_reg_pref + page.$getPageId();
    ComponentRegistry wr = (ComponentRegistry)page.session.getAttribute(key);
    if(wr == null){
        wr = new ComponentRegistry();
        page.session.setAttribute(key,wr);
    }
    int n = ranks.length;
    for(int k=n-1; k>=0; k--){
        ComponentSubRegistry wsr = wr.getSubRegistry(ranks[k]);
        if(wsr == null)
            continue;
        int m = wsr.size();
        for(int i=0; i<m; i++ ){
            BaseComponent ws = wr.getComponentInstance(ranks[k], i);
            ws.request = page.request;
            if(ws instanceof ComponentReference){
                ComponentReference r = (ComponentReference)ws;
                r.referred.representative = r;
            }
        } //for
    } //for
    for(int k=n-1; k>=0; k--){
    	ComponentSubRegistry wsr = wr.getSubRegistry(ranks[k]);
    	if(wsr == null)
    	    continue;
    	int m = wsr.size();
    	for(int i=0; i<m; i++ ){
    		BaseComponent ws = wr.getComponentInstance(ranks[k], i);
            try {
                if(submitter != null && ws.storage_id.equals(submitter.storage_id)){
                    ws.$handleComponent(verb,value);
                } else {
                    ws.$handleComponent(empty_string,empty_string);
                    // no action for this component
                }
            } finally {
                ws.request = null;
            }
    	} //for
    } //for
} //globalDispatch()

public static void $renderError(Throwable th,
        String name,PrintWriter ps,boolean page, int line_number){
    // this is not called for handlers
    String where;
    if(page)
        where = "in page \"";
    else
        where = "in widget \"";
    where += name + "\"";
    if(line_number != -1)
        where += " line=" + (line_number + 1) + ' ';
    rt.renderStack(th,ps,where);
} //$renderError()

static PrintWriter getPrintWriter(HttpServletResponse response){ // we should call it only for drawing page
    PrintWriter pw = null;
    try{
        pw = response.getWriter();
        return pw;
    }catch(IOException ex){
        System.err.println("Failed to get PrintStream from response");
        System.err.println(ex.toString());
        ex.printStackTrace(System.err);
        throw new Error(ex);
    }
} //getPrintWriter()

/**
 * Call it to send a standard Error Page to the client (browser)
 * @param request standard
 * @param response standard
 * @param th some exception or null
 * @param msg user message or null
 */
static void errorPage(Throwable th,String msg,
        HttpServletRequest request,HttpServletResponse response)
        { // may be called only from handler
    // no need to try to render anything else after the error page
    if(th == null && msg == null){
        msg = "No \"Throwable th\" or \"String msg\" provided by user";
    } //if
    if(response != null){
        response.setHeader("Cache-Control","no-store"); // HTTP 1.1
        response.setHeader("Pragma","no-cache"); // HTTP 1.0
        response.setDateHeader("Expires",0); // prevents caching at the proxy
    }
    if(msg != null){
        System.err.println(" @@@@@@@ Message: " + msg);
    } //if
    if(th != null){
        System.err.println(" @@@@@@@ Exception: " + th);
    } //if
    PrintWriter pw = rt.getPrintWriter(response);
    rt.renderStack(th,pw,"in handler");
    pw.println("<html><body><H1> Error Page</H1><p><font color=red>");
    if(response != null){ // not demo
        pw.println("<form METHOD=\"post\" action=\"" + request.getRequestURL() + "\">");
        pw.println("<INPUT TYPE=\"submit\" VALUE=\"OK\">");
        pw.println("</form>");
    } //if
    pw.println("</body></html>");
}// $errorPage()

/*
static String fullName(HttpServlet servlet,String cn){
    if(cn.indexOf('.') >= 0)
        return cn;
    StringBuffer sb = new StringBuffer(servlet.getClass().getPackage().getName());
    sb.append('.'); sb.append(cn);
    return sb.toString();
}  //fullName() */

static String calcDynId(String long_dyn_id,int loops){
    int diez_count = 0;
    int n = long_dyn_id.length();
    for(int i=0; i < n; i++){
        char c = long_dyn_id.charAt(i);
        if(c==HJ_Dyn.dyn_id_delim){
            diez_count++;
            if(diez_count> loops){
                return long_dyn_id.substring(0,i);
            }
        } //if(c==U.delim){
    } //for
    return "";
} //calcDynId


} //class
