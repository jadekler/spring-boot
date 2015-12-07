/*
 * Copyright 2012-2015 the original author or authors.
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Thin wrapper to adapt {@code org.json.JSONObject} to a {@link JsonParser}.
 *
 * @author Dave Syer
 * @author Jean de Klerk
 * @since 1.2.0
 * @see JsonParserFactory
 */
public class JsonJsonParser implements JsonParser {

	@Override
	public Map<String, Object> parseMap(String json) {
        if (json != null) {
            if (json.startsWith("{")) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                putAll(map, new JSONObject(json));
                return map;
            } else if (json.equals("")) {
                return new HashMap<String, Object>();
            }
        }
        return null;
	}

	private void putAll(Map<String, Object> map, JSONObject object) {
		for (Object key : object.keySet()) {
			String name = key.toString();
			Object value = object.get(name);
			if (value instanceof JSONObject) {
				Map<String, Object> nested = new LinkedHashMap<String, Object>();
				putAll(nested, (JSONObject) value);
				value = nested;
			}
			if (value instanceof JSONArray) {
				List<Object> nested = new ArrayList<Object>();
				addAll(nested, (JSONArray) value);
				value = nested;
			}
			map.put(name, value);
		}
	}

	private void addAll(List<Object> list, JSONArray array) {
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONObject) {
				Map<String, Object> nested = new LinkedHashMap<String, Object>();
				putAll(nested, (JSONObject) value);
				value = nested;
			}
			if (value instanceof JSONArray) {
				List<Object> nested = new ArrayList<Object>();
				addAll(nested, (JSONArray) value);
				value = nested;
			}
			list.add(value);
		}
	}

	@Override
	public List<Object> parseList(String json) {
        if (json != null) {
			json = json.trim();
			if (json.startsWith("[")) {
				List<Object> nested = new ArrayList<Object>();
                addAll(nested, new JSONArray(json));
                return nested;
			}
			else if (json.trim().equals("")) {
				return new ArrayList<Object>();
			}
		}
		return null;
	}

}
