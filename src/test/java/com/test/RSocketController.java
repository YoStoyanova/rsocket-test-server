package com.test;

import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component("hello")
public class RSocketController
		implements Function<Message<Map<String, Object>>, Message<Map<String, Object>>> {

	private static final Logger log = LoggerFactory.getLogger(RSocketController.class);

	private final RSocketMessageCatalog catalog;

	public RSocketController(RSocketMessageCatalog catalog) {
		this.catalog = catalog;
	}

	@Override
	public Message<Map<String, Object>> apply(Message<Map<String, Object>> t) {
		log.info("Incoming: " + t);
		// create a single response and return it
		MessageMap map = catalog.getMapping("hello");
		return MessageBuilder.withPayload(map.getResponse()).copyHeaders(t.getHeaders())
				.build();
	}

}