package com.HybridServerPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ComponentReference extends BaseComponent { 
BaseComponent referred;
static private String w_shared_pref = "s-";

// Created only in rt.$getComponentInstance()
ComponentReference(BasePage page, String cn, String short_name,int share_owner_id,int wc_id,String dyn_id){
    String shared_storage_id = w_shared_pref + share_owner_id + 'w' + wc_id + dyn_id;  
    referred = (BaseComponent)page.session.getAttribute(shared_storage_id);
    if(referred == null){ // create a real component (shared)
        referred = (BaseComponent)rt.createInstance(cn);
        page.session.setAttribute(shared_storage_id,referred);
        referred.storage_id = shared_storage_id;
        referred.short_name = short_name;
    }
    request = page.request;
    referred.representative = this;
    this.page = page;
} //~

@Override protected void $accept(int code, String msg, Object more,BaseComponent src){
    // signals propagate via primary components
    referred.$accept(code,msg,more,src);
} //$accept()

@Override protected void $handleComponent(String h1,String h2){
    referred.representative = this;
    referred.$handleComponent(h1,h2);
    referred.representative = null;
} //$handleComponent()

@Override protected void $ajaxComponent(HttpServletRequest req,HttpServletResponse resp){
    referred.representative = this;
    referred.$ajaxComponent(req,resp);
    referred.representative = null;
} //$handleComponent()

} //class
