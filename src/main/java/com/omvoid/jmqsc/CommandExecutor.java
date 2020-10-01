package com.omvoid.jmqsc;

import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFException;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFMessageAgent;

import java.io.IOException;

public class CommandExecutor {

    private final PCFMessageAgent messageAgent;

    public CommandExecutor(MQQueueManager manager) throws MQDataException {
        messageAgent = new PCFMessageAgent(manager);
    }

    public CommandResult executeCommand(String mqCommand) throws IOException, MQDataException {

        PCFMessage message = new PCFMessage(MQConstants.MQCMD_ESCAPE);
        message.addParameter(MQConstants.MQIACF_ESCAPE_TYPE, MQConstants.MQET_MQSC);
        message.addParameter(MQConstants.MQCACF_ESCAPE_TEXT, mqCommand);

        try {
            return new CommandResult(true, messageAgent.send(message));
        } catch (PCFException e) {
            return new CommandResult(false, (PCFMessage[]) e.exceptionSource);
        }

    }
}
