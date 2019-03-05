package ch.heigvd.res.labio.impl.explorers;

import ch.heigvd.res.labio.interfaces.IFileExplorer;
import ch.heigvd.res.labio.interfaces.IFileVisitor;
import java.io.File;
import java.io.IOException;

/**
 * This implementation of the IFileExplorer interface performs a depth-first
 * exploration of the file system and invokes the visitor for every encountered
 * node (file and directory). When the explorer reaches a directory, it visits all
 * files in the directory and then moves into the subdirectories.
 * 
 * @author Olivier Liechti
 */
public class DFSFileExplorer implements IFileExplorer {

  @Override
  public void explore(File rootDirectory, IFileVisitor vistor) {
    if(rootDirectory.isDirectory()){
      //https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
      File[] listOfFiles = rootDirectory.listFiles();
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          try {
            vistor.visit(rootDirectory);
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else if (listOfFiles[i].isDirectory()) {
          explore(listOfFiles[i], vistor);
        }
      }
    }else{
      try {
        vistor.visit(rootDirectory);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
