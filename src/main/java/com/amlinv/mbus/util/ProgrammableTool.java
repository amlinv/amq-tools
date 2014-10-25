package com.amlinv.mbus.util;

import com.amlinv.mbus.util.templ.ConsumeToObjectFile;
import com.amlinv.mbus.util.templ.ConsumeToStdout;
import com.amlinv.mbus.util.templ.ProduceFromObjectFile;
import com.amlinv.mbus.util.templ.ProduceFromStdin;
import com.amlinv.mbus.util.templ.factory.*;
import com.amlinv.mbus.util.templ.impl.ActiveMQEngineImpl;
import com.amlinv.prop.util.NamedProperties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.amlinv.mbus.util.ProgrammableTool.OperationType.*;
import static com.amlinv.mbus.util.ProgrammableTool.InputOutputType.*;

/**
 * Created by art on 8/26/14.
 */
public class ProgrammableTool {
    private NamedProperties     properties = new NamedProperties();
    private Map<String, String> fixedHeaders = new TreeMap<String, String>();
    private ActiveMQEngineImpl  engine;
    private OperationType       opType;
    private InputOutputType     outputType = STDOUT;
    private InputOutputType     inputType = STDIN;
    private String              objectFileName = "messages.dat";

    private boolean             useQueue = true;

    public static void  main (String[] args) {
        ProgrammableTool    mainObj = new ProgrammableTool();

        mainObj.instanceMain(args);
    }

    public void instanceMain (String[] args) {
        try {
            String[] remainingArgs;

            remainingArgs = parseCommandLineProperties(args);

            this.setupEngine();

            this.executeEngine(remainingArgs);
        } catch ( Exception exc ) {
            exc.printStackTrace();
        }
    }

    protected void  setupEngine () {
        engine = new ActiveMQEngineImpl();
        this.engine.setConnectionFactory(new DefaultConnectionFactory());
        this.engine.setSessionFactory(new DefaultSessionFactory(true));
        this.engine.addProperties(this.properties);

        if ( this.useQueue ) {
            this.engine.setDestinationFactory(new DefaultQueueFactory());
        } else {
            this.engine.setDestinationFactory(new DefaultTopicFactory());
        }


        //
        // Setup the operation based on the type of operation requested by the user.
        //
        switch ( opType ) {
            case CONSUME:
                this.engine.setMessagingClientFactory(new DefaultMessageConsumerFactory());
                this.engine.setProcessorFactory(
                        new ProcessorFactory() {
                            public Processor createProcessor () {
                                if ( outputType == STDOUT ) {
                                    return new ConsumeToStdout();
                                } else if ( outputType == OBJECT_FILE ) {
                                    try {
                                        return new ConsumeToObjectFile(new FileOutputStream(objectFileName));
                                    } catch ( IOException ioExc ) {
                                        throw new RuntimeException("failed to access file", ioExc);
                                    }
                                } else {
                                    throw new RuntimeException("internal error: unsupported output type: " + outputType);
                                }
                            }
                        });
                break;

            case PRODUCE:
                this.engine.setMessagingClientFactory(new DefaultMessageProducerFactory());
                this.configureHeaderFactory();

                this.engine.setProcessorFactory(
                        new ProcessorFactory() {
                            public Processor createProcessor () {
                                if ( inputType == STDIN ) {
                                    return new ProduceFromStdin();
                                } else if ( inputType == OBJECT_FILE ) {
                                    try {
                                        return new ProduceFromObjectFile(new FileInputStream(objectFileName));
                                    } catch ( IOException ioExc ) {
                                        throw new RuntimeException("failed to access file", ioExc);
                                    }
                                } else {
                                    throw new RuntimeException("internal error: unsupported input type: " + inputType);
                                }
                            }
                        });
                break;

            default:
                throw new RuntimeException("operation must be specified");
        }
    }

    protected void  configureHeaderFactory () {
        DefaultHeaderFactory hdrFactory = new DefaultHeaderFactory();

        for ( Map.Entry<String, String> oneHdr : this.fixedHeaders.entrySet() ) {
            hdrFactory.addHeader(oneHdr.getKey(), oneHdr.getValue());
        }

        this.engine.setHeaderFactory(hdrFactory);
    }

    protected void  executeEngine (String[] remainingArgs) {
        try {
            this.engine.execute(remainingArgs[0], remainingArgs[1]);
        }
        catch ( Exception exc ) {
            exc.printStackTrace();
        }
    }

    protected String[]  parseCommandLineProperties (String[] args) {
        List<String>    remainList = new LinkedList<String>();

        for ( String oneArg : args ) {
            if ( oneArg.startsWith("-D") ) {
                String content = oneArg.substring(2);
                String[] parts = content.split("=", 2);

                if ( parts.length == 2 ) {
                    this.properties.setProperty(parts[0], parts[1]);
                } else {
                    this.properties.setProperty(parts[0], "");
                }
            } else if ( oneArg.startsWith("-f") ) {
                String content = oneArg.substring(2);

                if ( content.isEmpty() ) {
                    throw new RuntimeException("-f argument requires an inline argument (e.g. -fx.dat)");
                }
                objectFileName = content;
            } else if ( oneArg.startsWith("-H") ) {
                String content = oneArg.substring(2);
                String[] parts = content.split("=", 2);

                if ( parts.length == 2 ) {
                    this.fixedHeaders.put(parts[0], parts[1]);
                } else {
                    this.fixedHeaders.put(parts[0], "");
                }
            } else if ( oneArg.equalsIgnoreCase("consume") ) {
                this.opType = CONSUME;
            } else if ( oneArg.equalsIgnoreCase("produce") ) {
                this.opType = PRODUCE;
            } else if ( ( oneArg.equalsIgnoreCase("objectfile") ) || ( oneArg.equalsIgnoreCase("ofile") ) ) {
                this.outputType = OBJECT_FILE;
                this.inputType = OBJECT_FILE;
            } else if ( oneArg.equalsIgnoreCase("queue") ) {
                this.useQueue = true;
            } else if ( oneArg.equalsIgnoreCase("topic") ) {
                this.useQueue = false;
            } else {
                remainList.add(oneArg);
            }
        }

        return  remainList.toArray(new String[remainList.size()]);
    }

    public static enum OperationType {
        CONSUME,
        PRODUCE,
        UNKNOWN
    }

    public static enum InputOutputType {
        STDIN,
        STDOUT,
        OBJECT_FILE
    }
}
