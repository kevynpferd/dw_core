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

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import lu.kevyn.dw_core.event.ConnectEvent;
import lu.kevyn.dw_core.event.DisconnectEvent;

public class Server extends WebSocketServer {
	
	Core DW;
	
	public Server(Core DW) {
		super(new InetSocketAddress(Core.host, Core.port));
		
		this.DW = DW;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		new ConnectEvent(DW, conn, handshake);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		new DisconnectEvent(DW, conn, code, reason, remote);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		DW.log.info("received message from "+ conn.getRemoteSocketAddress() + ": " + message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		DW.log.error("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
	}

	@Override
	public void onStart() {
		DW.log.socket.info("Started socket server.");
		DW.log.info("DeviceWatcher Core is now fully started.");
		DW.log.emptyLine();
	}
	
}