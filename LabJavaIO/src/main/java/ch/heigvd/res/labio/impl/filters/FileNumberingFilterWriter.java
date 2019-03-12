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
  private int line = 0; //Rajouté ici pour garder la ligne courante.
  private char lastChar;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    try {
      String s = "";
      //Si pas encore de ligne, on rajoute le numéro de la première
      if(line == 0){
        s = ++line + "\t";
      }
      //On ne prend que la partie après l'offset
      str = str.substring(off);
      //Tant qu'il y a encore des charactères dans la string ou que nous n'avons pas atteint la longueur voulue
      for(int i = 0; i < len;){
        //On prend la prochaine ligne
        String[] nl = Utils.getNextLine(str);
        //Cas ou on est à la fin.
        if(nl[0].length() == 0){
          if(nl[1].length() >= len - i) {
            nl[1] = nl[1].substring(0, len - i);
          }
          s+= nl[1];
          i += nl[1].length();
        }else{
          if(nl[0].length() >= len - i) {
            nl[0] = nl[0].substring(0, len - i);
          }
          //On rajoute le texte à notre string
          s += nl[0];
          //On incrément la longueur de notre compteur
          i += nl[0].length();
        }
        //On ajoute le numéro de ligne suivant si on tombe sur \n ou \r à la fin de la ligne
        if(nl[0].endsWith("\n") || nl[0].endsWith("\r")) {
          s += ++line + "\t";
        }
        str = nl[1];
      }
      //On ajoute le texte entier dans notre output
      out.append(s);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException("An error occur in FileNumberingFilterWriter.");
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    try {
      String s = String.valueOf(cbuf);
      write(s, off,len);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException("An error occur in FileNumberingFilterWriter.");
    }
  }

  @Override
  public void write(int c) throws IOException {
    //Si il n'y a rien dans notre output ou que le dernier charactère était un '\r' mais que le charactère actuelle n'est pas un '\n'
    if(line == 0 || lastChar == '\r' && (char)c != '\n'){
      out.write(++line + "\t"); //On ajoute le numéro de ligne
    }
    try {
      //On ajoute le charactère
      out.write(c);
      if(c == '\n' ){ //Si charactère est '\n'
        out.write(++line + "\t"); //On ajoute la ligne
      }
      lastChar = (char)c;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException("An error occur in FileNumberingFilterWriter.");
    }
  }
}
