package com.v2.controller.sync;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.v2.bean.Greeting;
import com.v2.bean.HelloMessage;

@Controller
public class GreetingController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
    	
    	System.out.println("websocket通信確率");
      Thread.sleep(1000); // simulated delay
      return new Greeting(HtmlUtils.htmlEscape(message.getName())
              + " : "
              + HtmlUtils.htmlEscape(message.getMessage()) );
    }

}
