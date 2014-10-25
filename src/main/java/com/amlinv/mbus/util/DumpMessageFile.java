package com.amlinv.mbus.util;

import com.amlinv.mbus.util.templ.ConsumeToObjectFile;
import com.amlinv.mbus.util.templ.ConsumeToStdout;
import com.amlinv.mbus.util.templ.ProduceFromObjectFile;
import com.amlinv.mbus.util.templ.ProduceFromStdin;
import com.amlinv.mbus.util.templ.factory.*;
import com.amlinv.mbus.util.templ.impl.ActiveMQEngineImpl;
import com.amlinv.prop.util.NamedProperties;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.util.ByteSequence;
import org.apache.activemq.wireformat.WireFormat;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.amlinv.mbus.util.ProgrammableTool.InputOutputType.*;
import static com.amlinv.mbus.util.ProgrammableTool.OperationType.CONSUME;
import static com.amlinv.mbus.util.ProgrammableTool.OperationType.PRODUCE;

/**
 * Dump the content of a message file to standard output.
 *
 * Created by art on 8/26/14.
 */
public class DumpMessageFile {
    private String              objectFileName = "messages.dat";
    private WireFormat          unmarshaller = new OpenWireFormat();


    public static void  main (String[] args) {
        DumpMessageFile mainObj = new DumpMessageFile();

        mainObj.instanceMain(args);
    }

    public void instanceMain (String[] args) {
        try {
            if ( args.length == 1 ) {
                objectFileName = args[0];
            } else if ( args.length > 1 ) {
                System.err.println("too many arguments; usage: DumpMessageFile [message-file-name]");
            }

            dumpObjectFile();
        } catch ( Exception exc ) {
            exc.printStackTrace();
        }
    }

    protected void  dumpObjectFile () throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(objectFileName));

        Object data = objectInputStream.readObject();

        try {
            while ( data != null ) {
                if ( data instanceof byte[] ) {
                    Object cmd = unmarshaller.unmarshal(new ByteSequence((byte[]) data));

                    System.out.println(cmd);
                } else if ( data != null ) {
                    throw new RuntimeException("unexpected object read from file: " + data.getClass().getName());
                }

                data = objectInputStream.readObject();
            }
        } catch ( EOFException eofException) {
            // End of file - totally normal
        }
    }
}
