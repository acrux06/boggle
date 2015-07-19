/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;
import java.util.ArrayList;
import java.util.Random;
 /**
 *
 * @author acrux
 */
public class Die {
    
    final int NUMBER_OF_SIDES = 6;
    ArrayList sidesData = new ArrayList();
    String letter;
    //int b=0;
    
    
    
    void randomLetter(){
        Random rand;
        rand = new Random(); 
        //Generator of a random Letter .
        rand.setSeed(System.currentTimeMillis());//       
        /*making variable letter equal to the letter of the index from the
        generator.*/ 
    letter = sidesData.get(rand.nextInt(NUMBER_OF_SIDES)).toString();
        
    }
    public String getLetter(){
       /*getting the letter from the method Random to be utilized out of the 
        class*/
        randomLetter();
        return letter;
    }
    
    public void addLetter(String letter){
        
        //Adding a letter to one of the sides of the die.
        sidesData.add(letter);
    }
    public void displayAllLetters(){
        
        for(int a= 0; a< NUMBER_OF_SIDES; a++){                           
                    //Displays information from the data intput.
            System.out.print(sidesData.get(a)+" ");
                
               
        }
            
            
            
            
        
    }
}
