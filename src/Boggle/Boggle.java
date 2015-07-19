package Boggle;
import inputOutput.ReadDataFile;
import inputOutput.ReadDictionary;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import userInterface.BoggleUi;
import java.util.ArrayList;
import core.Board;
/**
 *
 * @author acrux
 creating main class.
 */
public class Boggle {
    static ArrayList fileData = new ArrayList();
    static ArrayList dictionary = new ArrayList();
    static String fileName = new String();
    static String dictionaryName = new String();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ReadDictionary readWords;
        ReadDataFile readFile;
        Board boardData ;
        BoggleUi ui; 
        fileName = "BoggleData.txt";
        dictionaryName = "TemporaryDictionary.txt";
        readWords = new ReadDictionary(dictionaryName);
        readWords.populateDictionary();
        dictionary = readWords.getWords();
        
        //creating an instance of the class ReadFile.
        readFile = new ReadDataFile(fileName);
        //
        readFile.populateData();
        fileData = readFile.getData();
        boardData= new Board(fileData);
        
        //Call methods from class Board.
        boardData.populateDice();
        boardData.shakeDice();
        
        //initializing an insatance of BoggleUi and  passing boardData as an argument.
        ui = new BoggleUi(boardData, dictionary);
        
        /*passing the boarData to the method addingLetters so it can be implemented in the 
        the user interface.*/
        ui.addingLetters(boardData);
        
    }
 }
