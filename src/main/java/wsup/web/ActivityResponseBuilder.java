package wsup.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import def.util.JsonsUtils;
import wsup.db.activity.Activity;

/**
 * Enrich a mapResponse object with activity updates.
 *
 * @author xdai2
 * @since May 7, 2015
 *
 */
public class ActivityResponseBuilder {
	
	public void build(MapResponse mapResponse, List<Activity> activList) {
		List<Map<String, Object>> actiList = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(activList)) {
			for(Activity acti : activList) {
				Map<String, Object> actiMap = new HashMap<>();
				actiMap.put(JsonField.USERNAME, acti.getPkey().getUsername());
				actiMap.put(JsonField.TIMESTAMP, acti.getPkey().getDatetime().getTime());
				actiMap.put(JsonField.TYPE, acti.getPkey().getType());
				
				Map<String, Object> contentMap = JsonsUtils.decode2Map(acti.getContent());
				actiMap.put(JsonField.CONTENT, contentMap);
				
				actiList.add(actiMap);
			}
		}
		
		mapResponse.add(JsonField.ACTIVITY_LIST, actiList);		
	}
}
