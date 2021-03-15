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
package org.springframework.mock.rsocket.json;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.mock.rsocket.MessageMapping;
import org.springframework.mock.rsocket.RSocketMessageRegistry;
import org.springframework.util.StreamUtils;

/**
 * @author Dave Syer
 *
 */
public class JsonRSocketMessageCatalog
		implements RSocketMessageRegistry, InitializingBean {

	private ObjectMapper json = new ObjectMapper();

	private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	private Set<MessageMapping> maps = new HashSet<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		for (Resource resource : resolver.getResources("catalog/**/*.json")) {
			MessageMappingData map = json.readValue(StreamUtils
					.copyToString(resource.getInputStream(), StandardCharsets.UTF_8),
					MessageMappingData.class);
			maps.add(map.mapping());
		}
	}

	@Override
	public Collection<MessageMapping> getMappings() {
		return maps;
	}

	@Override
	public MessageMapping getMapping(String name) {
		for (MessageMapping map : maps) {
			if (name.equals(map.getPattern())) {
				return map;
			}
		}
		return null;
	}

	@Override
	public void register(MessageMapping map) {
		maps.add(map);
	}
}
