package com.omvoid.jmqsc;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.headers.MQDataException;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConsoleApplication {

    public void run(String[] args) {
        Options options = createCmdOptions();
        CommandLineParser cmdParser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException e) {
            printBanner(e.getMessage(), options);
            return;
        }

        if(cmd.hasOption("help")) {
            printBanner(null, options);
            return;
        }

        Configuration configuration = Configuration.defaultConfiguration();
        if(cmd.hasOption('h')) {
            configuration.setHost(cmd.getOptionValue('h'));
        }
        if(cmd.hasOption('c')) {
            configuration.setChannel(cmd.getOptionValue('c'));
        }
        if(cmd.hasOption('p')) {
            try {
                configuration.setPort(Integer.parseInt(cmd.getOptionValue('p')));
            } catch (NumberFormatException e) {
                printBanner(e.getMessage(), options);
                return;
            }
        }
        if(cmd.hasOption('l')) {
            configuration.setUserID(cmd.getOptionValue('l'));
        }
        if(cmd.hasOption('s')) {
            configuration.setPassword(cmd.getOptionValue('s'));
        }

        MQQueueManager manager;
        CommandExecutor executor;
        try {
            manager = new MQQueueManager("", configuration.getProperties());
            executor = new CommandExecutor(manager);
        } catch (MQException | MQDataException e){
            System.err.println(e.getMessage());
            return;
        }

        try(InputStream mqscSource = cmd.hasOption('i') ? new FileInputStream(cmd.getOptionValue('i')) : System.in) {
            CommandParser parser = new CommandParser(mqscSource);
            String command;
            while((command = parser.nextCommand()) != null) {
                CommandResult result = executor.executeCommand(command);
                if(result.isSuccess()) {
                    System.out.println(result.getResponseText());
                } else {
                    System.err.println(result.getResponseText());
                }
            }
        } catch (IOException | MQDataException e) {
            System.err.println(e.getMessage());
            return;
        }
    }


    public void printBanner(String additionalText, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        String jarName = new java.io.File(ConsoleApplication.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
        formatter.printHelp("java -jar " + jarName + " OPTIONS", null, options, additionalText);
    }

    private Options createCmdOptions() {
        Options options = new Options();
        options.addOption(Option.builder().longOpt("help").hasArg(false).desc("Print this help").build());
        options.addOption("h", "host", true, "MQ manager host. Default value is 127.0.0.1");
        options.addOption("p", "port", true, "Port. Default value id 1414");
        options.addOption("c", "channel", true, "Channel. Default value is DEV.ADMIN.SVRCONN");
        options.addOption("l", "login", true, "User login");
        options.addOption("s", "password", true, "User password");
        options.addOption("i", "input", true, "Source of commands. Default is standard input");
        return options;
    }

    public static void main(String[] args) {
        new ConsoleApplication().run(args);
    }

}
