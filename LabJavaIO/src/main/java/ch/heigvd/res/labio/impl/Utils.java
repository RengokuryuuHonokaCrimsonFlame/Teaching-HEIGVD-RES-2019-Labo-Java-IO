package ch.heigvd.res.labio.impl;

import java.util.logging.Logger;

/**
 *
 * @author Olivier Liechti
 */
public class Utils {

  private static final Logger LOG = Logger.getLogger(Utils.class.getName());

  /**
   * This method looks for the next new line separators (\r, \n, \r\n) to extract
   * the next line in the string passed in arguments. 
   * 
   * @param lines a string that may contain 0, 1 or more lines
   * @return an array with 2 elements; the first element is the next line with
   * the line separator, the second element is the remaining text. If the argument does not
   * contain any line separator, then the first element is an empty string.
   */
  public static String[] getNextLine(String lines) {
    String[] tableau = {"",""};
    if (lines.length() > 0) {
      int i = 0;
      for (char c : lines.toCharArray()) {
        tableau[0] += c;
        i++;
        if (c == '\n' || c == '\r') {
          if(c == '\r' && i < lines.length() && lines.substring(i, i + 1).equals("\n")){
            tableau[0] += lines.substring(i, i + 1);
            i++;
          }
          break;
        }
      }
      String fin = tableau[0].substring(tableau[0].length() - 1);
      if(!fin.equals("\n") && !fin.equals("\r")) {
        tableau[1] = lines;
        tableau[0] = "";
      }else{
        tableau[1] = lines.substring(i, lines.length());
      }
    }
    return tableau;
  }
}
