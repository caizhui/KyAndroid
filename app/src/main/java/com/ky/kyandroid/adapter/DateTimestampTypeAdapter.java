package com.ky.kyandroid.adapter;

import com.solidfire.gson.JsonDeserializationContext;
import com.solidfire.gson.JsonDeserializer;
import com.solidfire.gson.JsonElement;
import com.solidfire.gson.JsonParseException;
import com.solidfire.gson.JsonPrimitive;
import com.solidfire.gson.JsonSerializationContext;
import com.solidfire.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * 类名称：时间戳类型适配<br/>
 * 类描述：<br/>
 * 
 * 创建人： Cz <br/>
 * 创建时间：2016年9月12日 上午11:45:17 <br/>
 * @updateRemark 修改备注：
 *     
 */
public class DateTimestampTypeAdapter implements JsonSerializer<Timestamp>,
		JsonDeserializer<Timestamp> {

	private final DateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Override
	public JsonElement serialize(Timestamp ts, Type t,
								 JsonSerializationContext jsc) {
		if (ts == null) {
			return new JsonPrimitive("");
		}
		String dfString = format.format(new Date(ts.getTime()));
		return new JsonPrimitive(dfString);
	}

	@Override
	public Timestamp deserialize(JsonElement json, Type t,
			JsonDeserializationContext jsc) throws JsonParseException {
		if (!(json instanceof JsonPrimitive)) {
			throw new JsonParseException("The date should be a string value");
		}
		if ("".equals(json.getAsString())) {
			return null;
		}

		String timeString = json.getAsString();

		try {
			Date date = format.parse(timeString);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			// throw new JsonParseException(e);
			try {
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date1 = format1.parse(timeString);
				return new Timestamp(date1.getTime());
			} catch (ParseException e1) {
				try {
					DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
					Date date1 = format1.parse(timeString);
					return new Timestamp(date1.getTime());
				} catch (ParseException e2) {
					throw new JsonParseException(e2);
				}
			}

		}
	}
}
