package log.monitor;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.server.ServerEndpoint;

import log.monitor.util.JSONifier;


@ServerEndpoint("/websocketmain")



public class Endpoint {  
	
	
	/**
	* Continuosly query Cassandra every 0.7 sec and push JSON to client
	
	
	**/
	
	@OnOpen
	public void handleOpen(Session session) throws InterruptedException, IOException {
		System.out.println("client is now connected");
		RemoteEndpoint.Basic other = session.getBasicRemote();
			
		QueryCassandra qs =  new QueryCassandra("test", "casstest","logs");
	    while(True){
	    	Map<String, List<Integer>> out = qs.get();
	    	String json = JSONifier.JSONify(out);
	    	other.sendText(json);
	    	Thread.sleep(700);
	    	i++;
	    }
		
	}
	

	@OnClose
	public void handleClose(){
		System.out.println("client is now disconnected");
	}

}
