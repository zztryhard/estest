package com.java1234;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class TestCon {

	private static String host="47.98.202.230"; // 服务器地址
    private static int port=9300; // 端口
    
    public static final String CLUSTER_NAME="elasticsearch"; // 集群名称
    
    private static Settings.Builder settings=Settings.builder().put("cluster.name",CLUSTER_NAME);
	
	public static void main(String[] args) throws Exception{
		@SuppressWarnings({ "resource", "unchecked" })
		TransportClient client = new PreBuiltTransportClient(settings.build())
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),port));
		System.out.println(client);
		client.close();
	}
	public void test(){
		System.out.println("你说的是什么");
	}
}
