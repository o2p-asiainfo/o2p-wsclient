package com.ailk.eaap.o2p.client;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jws.WebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.ailk.eaap.o2p.common.cache.ICacheFactory;
import com.ailk.eaap.op2.bo.inter.PartialSerializable;

@WebService
public class MemCache implements ICache {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(MemCache.class);
	private ICacheFactory<String, Object> cacheFactory;

	public ICacheFactory<String, Object> getCacheFactory() {
		return cacheFactory;
	}

	public void setCacheFactory(ICacheFactory<String, Object> cacheFactory) {
		this.cacheFactory = cacheFactory;
	}

	public void put(String key, String value) {
		cacheFactory.getXMemcachedClient().put(key, value);
	}

	public void put(String key, String value, int TTL) {
		cacheFactory.getXMemcachedClient().put(key, TTL, value);
	}

	public String get(String key) {
		String regExp = "^[1-9].*";//start with number
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(key);
		if(!m.matches()){
			key = CacheKey.defaultTenantId + key;
		}
		String ret = "";
		String flag = "memcache";
		Object obj = cacheFactory.getXMemcachedClient().get(key);
		if (obj == null) {
			obj = cacheFactory.getConcurrentHashMap().get(key);
			flag = (obj == null ? "memcache" : "javacache");
		}

		if (obj != null) {
			try {
				if(obj instanceof PartialSerializable)
					ret = toJson(transformObj(obj), flag, key);
				else {
					if (obj instanceof Collection && !((Collection)obj).isEmpty()) 
						if(((Collection)obj).toArray()[0] instanceof PartialSerializable) return toJson(transformObj(obj), flag, key);
					ret = toJson(obj, flag, key);
				}
			} catch(Exception e) {
				ret =  obj.toString();
			}
		}

		return ret;
	}
	
	private String toJson(Object obj, String flag, String key) {
		String ret = null;
		if (obj instanceof Collection) {
			JSONArray val = JSONArray.fromObject(obj);
			ret = val.toString();
			if (log.isDebugEnabled())
				log.debug("get cache from " + flag + " the key=" + key
						+ ",val=" + ret);
		} else {
			JSONObject val = JSONObject.fromObject(obj);
			ret = val.toString();
			if (log.isDebugEnabled())
				log.debug("get cache from " + flag + " the key=" + key
						+ ",val=" + ret);
		}
		return ret;
	}
	
	private Object transformObj(Object obj) {
		if (obj instanceof Collection) {
			List<Object> resultCollection = new ArrayList<Object>();
			Iterator iterator = ((Collection<Object>)obj).iterator();
			while(iterator.hasNext()) {
				Object temp = iterator.next();
				resultCollection.add(transformObj(temp));
			}
			return resultCollection;
		} else if(obj instanceof String) {
			return obj.toString();
		} else {
			return objToMap(obj);
		}
	}

	private Map<String, Object> objToMap(Object obj) {
		if(!(obj instanceof PartialSerializable)) return null;
		Map<String, Object> params = new HashMap<String, Object>(0);
		List<String> serializableAttrList = ((PartialSerializable)obj).getSerializableAttr();
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean
					.getPropertyDescriptors(obj);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!StringUtils.equals(name, "class")) {
					if(!serializableAttrList.contains(name)) continue;
					try {
						params.put(name,propertyUtilsBean.getNestedProperty(obj, name));
					} catch(Exception ex) {
						continue;
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return params;
	}
	
	public void put(String key, String value, int TTL, boolean isPersistent) {
		// TODO Auto-generated method stub

	}

}
