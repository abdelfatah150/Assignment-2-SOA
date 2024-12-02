import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class XMLHandler {
    private static final String FILE_PATH = "students.xml";

    public static List<Student> loadStudents() throws Exception {
        List<Student> students = new ArrayList<>();
        File xmlFile = new File(FILE_PATH);

        if (!xmlFile.exists()) return students;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        NodeList nodeList = doc.getElementsByTagName("Student");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element studentElement = (Element) nodeList.item(i);
            String id = studentElement.getAttribute("ID");
            String firstName = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
            String lastName = studentElement.getElementsByTagName("LastName").item(0).getTextContent();
            String gender = studentElement.getElementsByTagName("Gender").item(0).getTextContent();
            double gpa = Double.parseDouble(studentElement.getElementsByTagName("GPA").item(0).getTextContent());
            int level = Integer.parseInt(studentElement.getElementsByTagName("Level").item(0).getTextContent());
            String address = studentElement.getElementsByTagName("Address").item(0).getTextContent();

            students.add(new Student(id, firstName, lastName, gender, gpa, level, address));
        }
        return students;
    }

    public static void saveStudents(List<Student> students) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("University");
        doc.appendChild(root);

        for (Student student : students) {
            Element studentElement = doc.createElement("Student");
            studentElement.setAttribute("ID", student.getId());
            appendChildElement(doc, studentElement, "FirstName", student.getFirstName());
            appendChildElement(doc, studentElement, "LastName", student.getLastName());
            appendChildElement(doc, studentElement, "Gender", student.getGender());
            appendChildElement(doc, studentElement, "GPA", String.valueOf(student.getGpa()));
            appendChildElement(doc, studentElement, "Level", String.valueOf(student.getLevel()));
            appendChildElement(doc, studentElement, "Address", student.getAddress());

            root.appendChild(studentElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(FILE_PATH));
        transformer.transform(source, result);
    }

    private static void appendChildElement(Document doc, Element parent, String tagName, String value) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(value));
        parent.appendChild(element);
    }
    public static boolean doesIdExist(String id) throws Exception {
        List<Student> students = loadStudents();

        for (Student student : students) {
            if (student.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    public static void sortStudentsById() throws Exception {
        List<Student> students = loadStudents();

        students.sort(Comparator.comparing(Student::getId));

        saveStudents(students);
    }

}
