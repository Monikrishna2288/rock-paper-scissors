import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class UpdateXML {

    public static void main(String[] argv) {

        try {
            String filepath = "/home/runner/work/rock-paper-scissors/rock-paper-scissors/pom.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);
            XPath xPath = XPathFactory.newInstance().newXPath();
            Node pom = (Node) xPath.compile("/project/version").evaluate(doc, XPathConstants.NODE);

            String strOutPomVersion= nodeToString(pom);//
            System.out.println(strOutPomVersion);

            strOutPomVersion=strOutPomVersion.replace("<version>","");//Replacing the <version> tag from the string
            strOutPomVersion=strOutPomVersion.replace("</version>","");//Replacing the </version> tag from the string
            strOutPomVersion=pomVersionIncrement(strOutPomVersion);
            pom.setTextContent(strOutPomVersion);// Updating the pomversion
            System.out.println(strOutPomVersion);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

            System.out.println("Done");

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
    public static String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }



    // Using the pomVersionIncrement method to upgrade the pomVersionNumber by 1
    public static String pomVersionIncrement(String strPomVersionReceive){
        int majorVersion, minorVersion, patchingVersion;
        String strMajorVersion, strMinorVersion, strPatchingVersion;
        strPomVersionReceive=strPomVersionReceive.replace("."," ");
        System.out.println(strPomVersionReceive);
        Scanner sc=new Scanner(strPomVersionReceive);
        majorVersion=sc.nextInt();
        System.out.println("Major Version Number is: "+majorVersion);

        minorVersion=sc.nextInt();
        System.out.println("Minor Version Number is: "+minorVersion);

        patchingVersion=sc.nextInt();
        patchingVersion++;
        System.out.println("Patch Version Number is: "+patchingVersion);

        strPomVersionReceive=null; // Assigning null to the strPomVersionReceive
        return majorVersion+"."+minorVersion+"."+patchingVersion;

    }
}
