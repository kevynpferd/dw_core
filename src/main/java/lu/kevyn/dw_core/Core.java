/**
 * 
 * BSD 3-Clause License
 * 
 * Copyright (c) 2018+ Kevin Olinger
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *    
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *    
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package lu.kevyn.dw_core;

import org.java_websocket.server.WebSocketServer;

import lu.kevyn.dw_core.config.Config;
import lu.kevyn.dw_core.input.InputReader;
import lu.kevyn.dw_core.util.LogUtil;

public class Core {
	
	public Boolean DEBUG = false;
	public Boolean MYSQL = false;
	
	public LogUtil log;
	public Config config;
	//public MySQL DB;
	public InputReader IR;
	public WebSocketServer server;
	
	public static String host = "localhost";
	public static Integer port = 8080;
	
	public static void main(String[] args) {
		new Core();
	}
	
	public Core() {
		log = new LogUtil();
		
		log.info("Starting ..");
		log.info("Initializing logging util(s) ..");
		
		config = new Config(this);
		IR = new InputReader(this);
		
		host = config.get("Socket server > host");
		port = config.getInt("Socket server > port");
		
		log.info("Starting socket server on "+ host +":"+ port +" ..");
		log.socket.info("Starting socket server on "+ host +":"+ port +" ..");
		
		server = new Server(this);
		
		DEBUG = config.getBool("Debug");
		
		IR.start();
		server.run();
	}
	
	public void quit() {
		log.emptyLine();
		log.info("Stopping ..");
		
		
		try {
			IR.stop();
			
			log.info("Stopping socket server ..");
			log.socket.info("Stopping socket server ..");
			
			server.stop(2500);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		
		log.info("DeviceWatcher Core stopped.");
		
		System.exit(0);
	}

}
