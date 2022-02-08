package org.hung;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.hung.pojo.odds.OddsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solacesystems.jcsmp.BytesMessage;
import com.solacesystems.jcsmp.DeliveryMode;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import com.solacesystems.jcsmp.SpringJCSMPFactory;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessage;
import com.solacesystems.jcsmp.XMLMessageProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BroadcastDblOdds {

	@Autowired
	private SpringJCSMPFactory solaceFactory;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Scheduled(fixedDelayString = "5000")
	public void broadcastDblOddsRaw () {
		log.info("broadcast message to solace...");
		
		//Queue queue = JCSMPFactory.onlyInstance().createQueue("#mqtt/primary/clientId-kfoBRXBe5Q");//test_queue");
		Topic topic  = JCSMPFactory.onlyInstance().createTopic("hkjc/public/raw/odds/20211117_HV/1/dbl");
		Topic topic1 = JCSMPFactory.onlyInstance().createTopic("hkjc/public/raw/odds/20211117_HV/1/win/1");
		Topic topic2 = JCSMPFactory.onlyInstance().createTopic("hkjc/public/raw/odds/20211117_HV/1/win/2");
		
		SimplePublishEventHandler pubEvtHdlr = new SimplePublishEventHandler();
		
		try {
			JCSMPSession session = solaceFactory.createSession();
			try {
				XMLMessageProducer producer = session.getMessageProducer(pubEvtHdlr);
				try {
					OddsInfo pojo = OddsInfo.genf1xf2DblOdds(34, 34);
					String json = objectMapper.writeValueAsString(pojo);
					
					sendTextMessage(producer, topic, json);
					sendTextMessage(producer, topic1, "999.99");
					sendTextMessage(producer, topic2, "888.88");				
					
				} finally {
					producer.close();
				}
			} finally {
				session.closeSession();
			}
		} catch (JCSMPException|JsonProcessingException  e) {
			log.error("",e);
		}
	}
	
	//@Scheduled(fixedDelayString = "5000")
	public void broadcastDblOddsZip () {
		log.info("broadcast message to solace...");
		
		//Queue queue = JCSMPFactory.onlyInstance().createQueue("#mqtt/primary/clientId-kfoBRXBe5Q");//test_queue");
		Topic topic  = JCSMPFactory.onlyInstance().createTopic("hkjc/public/gzip/odds/20211117_HV/1/dbl");
		Topic topic1 = JCSMPFactory.onlyInstance().createTopic("hkjc/public/gzip/odds/20211117_HV/1/win/1");
		Topic topic2 = JCSMPFactory.onlyInstance().createTopic("hkjc/public/gzip/odds/20211117_HV/1/win/2");
		
		SimplePublishEventHandler pubEvtHdlr = new SimplePublishEventHandler();
		
		try {
			JCSMPSession session = solaceFactory.createSession();
			try {
				XMLMessageProducer producer = session.getMessageProducer(pubEvtHdlr);
				try {
					OddsInfo pojo = OddsInfo.genf1xf2DblOdds(34, 34);
					String json = objectMapper.writeValueAsString(pojo);
					
					sendGZipMessage(producer, topic, json);
					sendGZipMessage(producer, topic1, "999.99");
					sendGZipMessage(producer, topic2, "888.88");				
					
				} finally {
					producer.close();
				}
			} finally {
				session.closeSession();
			}
		} catch (JCSMPException|JsonProcessingException  e) {
			log.error("",e);
		}
	}

	private void sendTextMessage(XMLMessageProducer producer, Topic topic, String text) throws JCSMPException {
		TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
		msg.setText(text);
		msg.setDeliveryMode(DeliveryMode.PERSISTENT);
		msg.setAckImmediately(true);
		producer.send(msg, topic);
		log.info("message sent"+msg.dump());		
	}

	private void sendGZipMessage(XMLMessageProducer producer, Topic topic, Object pojo) throws JCSMPException {
		try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
			GZIPOutputStream gzipOut = new GZIPOutputStream(byteOut);
			BytesMessage msg = JCSMPFactory.onlyInstance().createMessage(BytesMessage.class);
			objectMapper.writeValue(gzipOut, pojo);
			msg.setData(byteOut.toByteArray());
			msg.setDeliveryMode(DeliveryMode.PERSISTENT);
			msg.setAckImmediately(true);
			producer.send(msg, topic);
			log.info("message sent"+msg.dump());	
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	public static class SimplePublishEventHandler implements JCSMPStreamingPublishEventHandler {

		@Override
		public void handleError(String arg0, JCSMPException arg1, long arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void responseReceived(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
