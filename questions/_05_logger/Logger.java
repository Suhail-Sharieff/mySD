package questions._05_logger;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
system shud support different loggers for different microservices, identified by thier name, so each logger will have a name
all loggers will be managed by facade log manager
system must support append to multiple places(liek file, console)
appending must be asynchronous to save latency
bfr appending system must support formating of plain message, each logger can have tunable formatter
*/
enum LogLevel{
    DEBUG(1),
    INFO(2),
    WARN(3),
    FATAL(4);

    final int severity;
    LogLevel(int severity){this.severity=severity;}

    public int getSeverity() {
        return severity;
    }
}

class LogMessage{
    Instant timeStamp;
    String loggerName;
    LogLevel level;
    String message;
    String threadName;

    public LogMessage(String loggerName,LogLevel level,String message){
        this.timeStamp=Instant.now();
        this.loggerName=loggerName;
        this.level=level;
        this.message=message;
        this.threadName=Thread.currentThread().getName();
    }
}

interface MessageFormatter{
    String format(LogMessage msg);
}

class Formatter1 implements MessageFormatter{
    @Override
    public String format(LogMessage msg) {
        return "["+msg.timeStamp+"] "
                +"["+msg.loggerName+"] "
                +"["+msg.level+"] "
                +msg.message;
    }
}

class Formatter2 implements MessageFormatter{
    @Override
    public String format(LogMessage msg) {
        return "{"+msg.level+"} "
                +"thread="+msg.threadName
                +" msg="+msg.message;
    }
}

interface Appender{
    void append(String formattedMessage);
}

class FileAppender implements Appender{
    File file;

    public FileAppender(File file){
        this.file=file;
    }

    @Override
    public void append(String formattedMessage) {
        // append to file
    }
}

class ConsoleAppender implements Appender{
    @Override
    public void append(String formattedMessage) {
        System.out.println(formattedMessage);
    }
}

class AsyncProcessor{
    private final ExecutorService service;

    AsyncProcessor(){
        service=Executors.newSingleThreadExecutor(r->{
            Thread t=new Thread(r,"logger-thread");
            t.setDaemon(true);//so it stops when JVM shts down
            return t;
        });
    }

    void process(String formattedMessage,List<Appender> appenders){
        service.submit(()->{
            for(Appender a:appenders){
                a.append(formattedMessage);
            }
        });
    }

    void shutdown(){
        service.shutdown();
    }
}

class LogManager{
    private final ConcurrentHashMap<String,Logger> loggers;
    private final AsyncProcessor processor;

    LogManager(){
        loggers=new ConcurrentHashMap<>();
        processor=new AsyncProcessor();
    }

    Logger createLogger(
            String loggerName,
            LogLevel threshold,
            MessageFormatter formatter,
            List<Appender> appenders){

        Logger logger=new Logger(
                loggerName,
                threshold,
                formatter,
                appenders,
                this
        );

        loggers.put(loggerName,logger);
        return logger;
    }

    Logger getLogger(String loggerName){
        return loggers.get(loggerName);
    }

    void process(LogMessage msg,
                 MessageFormatter formatter,
                 List<Appender> appenders){

        String formatted=formatter.format(msg);
        processor.process(formatted,appenders);
    }

    void shutdown(){
        processor.shutdown();
    }
}

class Logger{
    private final String loggerName;
    private final LogLevel threshold;
    private final MessageFormatter formatter;
    private final List<Appender> appenders;
    private final LogManager manager;

    public Logger(String loggerName,
                  LogLevel threshold,
                  MessageFormatter formatter,
                  List<Appender> appenders,
                  LogManager manager){

        this.loggerName=loggerName;
        this.threshold=threshold;
        this.formatter=formatter;
        this.appenders=appenders;
        this.manager=manager;
    }

    void log(LogLevel level,String msg){

        if(level.getSeverity()<threshold.getSeverity()){
            return;
        }

        LogMessage logMessage=
                new LogMessage(loggerName,level,msg);

        manager.process(
                logMessage,
                formatter,
                appenders
        );
    }

    void debug(String msg){
        log(LogLevel.DEBUG,msg);
    }

    void info(String msg){
        log(LogLevel.INFO,msg);
    }

    void warn(String msg){
        log(LogLevel.WARN,msg);
    }

    void fatal(String msg){
        log(LogLevel.FATAL,msg);
    }
}