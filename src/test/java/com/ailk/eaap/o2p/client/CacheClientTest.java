package com.ailk.eaap.o2p.client;

import net.rubyeye.xmemcached.MemcachedClient;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CacheClientTest {
	ApplicationContext app;
	@Before
	public void setUp(){
		app = new ClassPathXmlApplicationContext(new String[]{"classpath:eaap-op2-memCache-client.xml"});
	}
	@Test
	public void test() throws Exception{
		int cacheTime = 1*3600;
		MemcachedClient runTimeMemcachedClient = (MemcachedClient)app.getBean("runtimeMemcachedClient");
		Object key1 = runTimeMemcachedClient.get("111");
		if(key1==null)
			runTimeMemcachedClient.add("111", cacheTime,"hello !!!!");
		System.out.println(runTimeMemcachedClient.get("111"));
	}
}
