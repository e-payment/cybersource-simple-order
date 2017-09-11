package batch;

/*
 * This example shows how to upload batch file onto CyberSource system
 *
 * You need to install Bouncy Castle to run this sample code.
 *
 */
import java.util.*;
import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.security.*;

/*
 * This class is to upload files but can be expanded to download files also.
 */
public class BatchUpload {
    
    private Properties props = new Properties(); // stores properties from property file

    /*
     * init(): initialization (load property file)
     *
     * @param propsFile properties needed for file transfer
     */
    public void init(String propsFile)  {
        try {
            props.load(new BufferedInputStream(new FileInputStream(new File(propsFile))));
        } catch (Exception e) {
            e.printStackTrace();
            //System.exit(-1);
        }
    }

    /*
     * usage()
     */
    public static void usage() {
        System.out.println("USAGE: java BatchUpload <full path property file name>");
        //System.exit(-1);
    }

    /*
     * getFactory(): get factory for authentication
     *
     * @throws IOException  if exception occurs
     */
    private SSLSocketFactory getFactory() throws IOException {

        try {
            
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks, ks1;
            char[] passphrase = props.getProperty("passPhrase").toCharArray();

            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("PKCS12", "BC");
            ks1 = KeyStore.getInstance("JKS");
            
            ks.load(new FileInputStream(props.getProperty("key")), passphrase);
            ks1.load(new FileInputStream(props.getProperty("keyStore")), passphrase);
            
            kmf.init(ks, passphrase);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks1);
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            
            return ctx.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    /*
     * getHost(): Get host from property file
     */
    private String getHost() {
        return props.getProperty("host", "localhost");
    }

    /*
     * getPort(): Get port from property file
     */
    private int getPort() {
        return Integer.parseInt(props.getProperty("port"));
    }

    /*
     * sendRequest(): Send request (file) to the server
     *
     * @param out       stream to send the data to the server
     *
     * @throws Exception    if an error occurs.
     */
    private void sendRequest(PrintWriter out) throws Exception {
        
        String path = props.getProperty("path");

        out.println("POST " + path + " HTTP/1.0");
        
        final String BOUNDARY = "7d03135102b8";
        out.println("Content-Type: multipart/form-data; boundary="+BOUNDARY);
        
        String uploadFile = props.getProperty("uploadFile");
        String authString = props.getProperty("bcUserName") + ":" + props.getProperty("bcPassword");
        String encodedAuthString = "Basic " + new sun.misc.BASE64Encoder().encode(authString.getBytes ());
        out.println("Authorization: " + encodedAuthString);
        
        final String CRLF = "\r\n";
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("--"+BOUNDARY+CRLF);
        sbuf.append("Content-Disposition: form-data; name=\"upfile\"; filename=\"" + uploadFile + "\""+CRLF);
        sbuf.append("Content-Type: text/plain"+CRLF+CRLF);
        
        
        FileReader fi = new FileReader(uploadFile);
        char[] buf = new char[1024000];
        int cnt = fi.read(buf);
        sbuf.append(buf, 0, cnt);
        sbuf.append(CRLF);
        sbuf.append("--"+BOUNDARY+"--"+CRLF);
        
        int sz = sbuf.length();
        out.println("Content-Length: "+ sz);
        
        out.println();
        out.println(sbuf);
        out.flush();
        
        // Make sure there were no surprises
        if (out.checkError())
            System.out.println("BatchUpload: java.io.PrintWriter error");
        
    }

    /*
     * readResponse(): reads response from the server
     *
     * @param in        stream to get the data from the server
     *
     * @throws Exception    if an error occurs.
     */
    private void readResponse(BufferedReader in) throws Exception {

        boolean successful = false;
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            if (inputLine.startsWith("HTTP") && inputLine.indexOf("200") >= 0) {
                successful = true;
            }
            System.out.println(inputLine);
        } 

        System.out.println("UPLOAD FILE " + (successful? "SUCCESSFUL" : "FAILED") + "!!!\n");
    }

    /*
     * upload(): upload file to server
     *
     * @throws Exception    if an error occurs.
     */
    public void upload()  throws Exception {
        try {
            SSLSocketFactory factory = getFactory();
            SSLSocket socket = (SSLSocket)factory.createSocket(getHost(), getPort());

            PrintWriter   out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            socket.startHandshake();
            sendRequest(out);
            readResponse(in);

            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public static void main(String[] args) throws Exception {

        if (args == null && args.length != 1) {
            usage();
            return;
        }

        BatchUpload batchUpload = new BatchUpload();

        batchUpload.init(args[0]);
        batchUpload.upload();

    }
}
