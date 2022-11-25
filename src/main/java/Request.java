import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    String method;
    String path;
    String http;
    Map<String, String> mapHeadersValues;
    String getContentType(String exe) throws FileNotFoundException {
        Pattern patternExeContentType = Pattern.compile(exe + "(.+)");
        String fileName = "/home/zelyanin/IdeaProjects/servReq2.0/src/main/java/testerFiletable";
        String exeParse = null;
        try (var buff = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            while (buff.ready()) {
                String str = buff.readLine();
                Matcher matcherExeContentType = patternExeContentType.matcher(str);
                if (matcherExeContentType.find()) {
                    exeParse = matcherExeContentType.group(1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return exeParse;
    }
    String exe;
    public static final Pattern patternHeaderValue = Pattern.compile("(\\p{Lu}{1,2}.*(\\:))(\\s.*)");
    public static final Pattern patternExe = Pattern.compile("(\\.[^\s]*)");
    public Request(String method, String path, String http, String exe, Map<String, String> mapHeadersValues) {
        this.method = method;
        this.path = path;
        this.http = http;
        this.exe = exe;
        this.mapHeadersValues = mapHeadersValues;
    }

    public static Request parse(InputStream isr) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(isr));

        int visitorsCounter = 0;
        visitorsCounter++;
        if(visitorsCounter > 0) {
            try (FileWriter fileCleaner = new FileWriter("/home/zelyanin/IdeaProjects/servReq2.0/src/main/java/bodyWriterReader", false)) {
                fileCleaner.write("");
                fileCleaner.flush();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        String[] methodPathHttp = reader.readLine().split(" ");
        String methodParse = methodPathHttp[0];
        String pathParse = methodPathHttp[1];
        String httpParse = methodPathHttp[2];

        String exeParse = null;
        Matcher matcherExe = patternExe.matcher(pathParse);
        if(matcherExe.find()) {
            exeParse = matcherExe.group();
        }

        Map<String, String> mapHeadersValuesParse = new HashMap<>();
        int emptySpacesCounter = 0;
            while (reader.ready()) {
                String headersBody = reader.readLine();
                Matcher matcherHeaderValue = patternHeaderValue.matcher(headersBody);
                if (!(headersBody.length() == 0)) {
                    if(emptySpacesCounter > 0) {
                        try (FileWriter bodyWriter = new FileWriter("/home/zelyanin/IdeaProjects/servReq2.0/src/main/java/bodyWriterReader", true)) {
                            bodyWriter.write(headersBody);
                            bodyWriter.append('\n');
                            bodyWriter.flush();
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    if (matcherHeaderValue.find()) {
                        mapHeadersValuesParse.put(matcherHeaderValue.group(1), matcherHeaderValue.group(3));
                    }
                } else if (emptySpacesCounter >= 0) {
                    emptySpacesCounter++;
                }
            }
        return new Request(methodParse, pathParse, httpParse, exeParse, mapHeadersValuesParse);
    }
}





