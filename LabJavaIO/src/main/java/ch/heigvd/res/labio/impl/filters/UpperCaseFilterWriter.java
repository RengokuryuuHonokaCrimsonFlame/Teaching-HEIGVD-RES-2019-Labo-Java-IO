package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Olivier Liechti
 */
public class UpperCaseFilterWriter extends FilterWriter {

  public UpperCaseFilterWriter(Writer wrappedWriter) {
    super(wrappedWriter);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    try{
      String s = "";
      for(int i = 0; i < len; ++i) {
        char c =  Character.toUpperCase(str.charAt(off + i));
        s += c;
      }
      out.append(s);
    }catch(Exception e){
      e.printStackTrace();
      throw new IOException("An error occur in UpperCaseFilterWriter.");
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    try{
      String s = "";
      for(int i = 0; i < len; ++i){
        s += Character.toUpperCase(cbuf[off + i]);
      }
      out.append(s);
    }catch(Exception e){
      e.printStackTrace();
      throw new IOException("An error occur in UpperCaseFilterWriter.");
    }
  }

  @Override
  public void write(int c) throws IOException {
    try {
      out.append((char) Character.toUpperCase(c));
    }catch(Exception e){
      e.printStackTrace();
      throw new IOException("An error occur in UpperCaseFilterWriter.");
    }
  }

}
