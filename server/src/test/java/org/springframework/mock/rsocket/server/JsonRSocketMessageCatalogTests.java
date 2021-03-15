/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.mock.rsocket.server;

import java.util.HashMap;

import io.rsocket.frame.FrameType;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import org.springframework.mock.rsocket.MessageMapping;
import org.springframework.mock.rsocket.json.JsonRSocketMessageCatalog;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 *
 */
public class JsonRSocketMessageCatalogTests {

	private JsonRSocketMessageCatalog catalog = new JsonRSocketMessageCatalog();

	@Test
	public void channel() throws Exception {
		catalog.afterPropertiesSet();
		MessageMapping mapping = catalog.getMapping("channel");
		assertThat(mapping.getFrameType()).isEqualTo(FrameType.REQUEST_CHANNEL);
		assertThat(mapping.handle(Flux.just(new HashMap<>())).toIterable()).hasSize(1);
		assertThat(mapping.matches(new HashMap<>(), "channel")).isTrue();
	}

	@Test
	public void stream() throws Exception {
		catalog.afterPropertiesSet();
		MessageMapping mapping = catalog.getMapping("long");
		assertThat(mapping.getFrameType()).isEqualTo(FrameType.REQUEST_STREAM);
		assertThat(mapping.handle(Flux.just(new HashMap<>())).toIterable()).hasSize(15);
	}

	@Test
	public void response() throws Exception {
		catalog.afterPropertiesSet();
		MessageMapping mapping = catalog.getMapping("response");
		assertThat(mapping.getFrameType()).isEqualTo(FrameType.REQUEST_RESPONSE);
		assertThat(mapping.matches(new HashMap<>(), "response")).isFalse();
	}

	@Test
	public void forget() throws Exception {
		catalog.afterPropertiesSet();
		MessageMapping mapping = catalog.getMapping("forget");
		assertThat(mapping.getFrameType()).isEqualTo(FrameType.REQUEST_FNF);
		assertThat(mapping.matches(new HashMap<>(), "forget")).isFalse();
	}

}
