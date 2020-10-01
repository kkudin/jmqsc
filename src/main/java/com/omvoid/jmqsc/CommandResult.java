package com.omvoid.jmqsc;

import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFParameter;

public class CommandResult {

    private final String responseText;
    private final boolean success;

    public CommandResult(boolean isSuccess, PCFMessage[] responseMessages) {
        this.success = isSuccess;
        StringBuilder sb = new StringBuilder();

        for(PCFMessage message : responseMessages) {
            PCFParameter parameter = message.getParameter(MQConstants.MQCACF_ESCAPE_TEXT);
            if(parameter != null) {
                sb.append(parameter.getStringValue()).append("\n");
            }
        }
        responseText = sb.toString();
    }

    public String getResponseText() {
        return responseText;
    }

    public boolean isSuccess() {
        return success;
    }
}
