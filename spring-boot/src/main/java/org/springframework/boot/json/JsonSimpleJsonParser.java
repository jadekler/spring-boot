/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Thin wrapper to adapt {@link org.json.simple.JSONObject} to a {@link JsonParser}.
 *
 * @author Dave Syer
 * @author Jean de Klerk
 * @since 1.2.0
 * @see JsonParserFactory
 */
public class JsonSimpleJsonParser implements JsonParser {

	@Override
    @SuppressWarnings("unchecked")
	public Map<String, Object> parseMap(String json) {
        if (json != null) {
            if (json.startsWith("{")) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                try {
                    Map<String, Object> value = (Map<String, Object>) new JSONParser()
                            .parse(json);
                    map.putAll(value);
                }
                catch (ParseException ex) {
                    throw new IllegalArgumentException("Cannot parse JSON", ex);
                }
                return map;
            } else if (json.equals("")) {
                return new HashMap<String, Object>();
            }
        }
        return null;
	}

	@Override
	public List<Object> parseList(String json) {
		List<Object> nested = new ArrayList<Object>();
		try {
			@SuppressWarnings("unchecked")
			List<Object> value = (List<Object>) new JSONParser().parse(json);
			nested.addAll(value);
		}
		catch (ParseException ex) {
			throw new IllegalArgumentException("Cannot parse JSON", ex);
		}
		return nested;
	}

}
