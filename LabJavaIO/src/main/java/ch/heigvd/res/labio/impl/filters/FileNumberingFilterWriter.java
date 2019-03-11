package ch.heigvd.res.labio.impl.filters;

import ch.heigvd.res.labio.impl.Utils;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\tHello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
  private int line = 1; //Rajouté ici pour garder la ligne courante.

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    try {
      String s = "";
      if(out.toString().equals("")){
        s = line + "\t";
      }
      str = str.substring(off);
      for(int i = 0; i < len;){
        String[] nl = Utils.getNextLine(str);
        //Cas ou on est à la fin.
        if(nl[0].length() == 0){
          if(nl[1].length() >= len - i) {
            int j = nl[1].length();
            nl[1] = nl[1].substring(0, len - i);
          }
          s+= nl[1];
          i += nl[1].length();
        }else{
          if(nl[0].length() >= len - i) {
            nl[0] = nl[0].substring(0, len - i);
          }
          s += nl[0];
          i += nl[0].length();
        }
        if(nl[0].endsWith("\n") || nl[0].endsWith("\r")) {
          s += ++line + "\t";
        }
        str = nl[1];
      }
      out.append(s);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException("An error occur in FileNumberingFilterWriter.");
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    try {
      String s = String.valueOf(cbuf);;
      write(s, off,len);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException("An error occur in FileNumberingFilterWriter.");
    }
  }

  @Override
  public void write(int c) throws IOException {
    if(out.toString().equals("") || (out.toString().endsWith("\r") && c != '\n')){
      out.write(line++ + "\t");
    }
    try {
      out.write(c);
      if(c == '\n'){
        out.write(line++ + "\t");
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException("An error occur in FileNumberingFilterWriter.");
    }
  }
}
