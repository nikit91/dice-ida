package com.poc;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.poc.ResponseBean;

/**
 * Serializer class for the {@link ResponseBean}
 * 
 * @author Nikit
 *
 */
public class RespBeanSerializer extends StdSerializer<ResponseBean> {

	/**
	 * auto generated serial version id
	 */
	private static final long serialVersionUID = 1L;
	
	public RespBeanSerializer() {
		this(null);
	}

	public RespBeanSerializer(Class<ResponseBean> t) {
		super(t);
	}
	/**
	 * Method to implement the serializer
	 */
	@Override
	public void serialize(ResponseBean value, JsonGenerator jgen, SerializerProvider arg2)
			throws IOException, JsonGenerationException {
		jgen.writeStartObject();
		jgen.writeNumberField("actnCode", value.getActnCode());
		jgen.writeNumberField("errCode", value.getErrCode());
		jgen.writeStringField("errMsg", value.getErrMsg());
		jgen.writeObjectField("payload", value.getPayload());
		jgen.writeObjectField("chatmsg", value.getChatmsg());
		jgen.writeEndObject();
	}

}
