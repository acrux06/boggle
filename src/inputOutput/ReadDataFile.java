/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputOutput;
import java.util.Scanner;
import java.util.ArrayList;
import java.net.URL;
import java.io.File;

/**
 *
 * @author acrux
 */
public class ReadDataFile {
    Scanner ifile;
    String fName;
    ArrayList dFile;
    
    public ReadDataFile(String fName){
        this.fName = fName;
    }

    public void populateData(){
        // accessing to the file data.
        URL fileURL;
        File file;
        dFile= new ArrayList();
        ///handeling error execption
        try{
            fileURL = getClass().getResource(fName);
            file = new File(fileURL.toURI());
            ifile = new Scanner(file);
        
            while(ifile.hasNext()){
                dFile.add(ifile.next());
            }
                
        }
        catch(Exception e){
            
            System.out.println(e.toString());
            e.printStackTrace();
        }
        
        finally{
            ifile.close();
        
        }
    }
    
    //Gets Data from a file when it is called from the main Class Boggle.
    public ArrayList getData(){
        return dFile;
    }
}