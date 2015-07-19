/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputOutput;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author acrux
 */
public class ReadDictionary {
    
    Scanner inputfile;
    String dictName;
    ArrayList dictionaryFile;

    public ReadDictionary(String dictName){
        this.dictName = dictName;
    }

    
      public void populateDictionary(){
        // accessing to the file data.
        URL fileURL2;
        File file2;
        dictionaryFile= new ArrayList();
        ///handeling error execption
        try{
            fileURL2 = getClass().getResource(dictName);
            file2 = new File(fileURL2.toURI());
            inputfile = new Scanner(file2);
        
            while(inputfile.hasNext()){
                dictionaryFile.add(inputfile.next());
            }
                
        }
        catch(Exception a){
            
            System.out.println(a.toString());
            a.printStackTrace();
        }
        
        finally{
            inputfile.close();
        
        }
    }
    
    //Gets Data from a file when it is called from the main Class Boggle.
    public ArrayList getWords(){
        return dictionaryFile;
    }
}
