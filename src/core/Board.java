/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;
import java.util.ArrayList;
/**
 *
 * @author acrux
 */
public class Board {
    final int NUMBER_OF_DICE = 16;
    final int NUMBER_OF_SIDES = 6;
    final int GRID = 4;
    ArrayList diceData = new ArrayList();
    public ArrayList<Die> dice = new ArrayList();
    
    public Board(ArrayList dData){
        diceData = dData;
    }
    public void populateDice(){
        Die d;
        //looping through all Dice.
        for(int i=0; i<NUMBER_OF_DICE;i++){
            d = new Die();
            System.out.print("Dice "+(i) +": ");
            //loops through each to the sides of the die. 
            for(int j=0; j < NUMBER_OF_SIDES;j++){
             //calls method from class die to add a letter on each side of eac die.
                d.addLetter(diceData.get(j+i*6).toString());//ask 
            }
            
            d.displayAllLetters();
            System.out.println();
            dice.add(d);
        }
    
    
    }

    public ArrayList shakeDice(){
       
        //System.out.println("\nBoggle Board");
      //loops through each of the dice to add a random letter from the classDie. 
        for(int i=0; i < NUMBER_OF_DICE ;i++){
            //prints the data in a 4X4 Grid, one LN every 4 spaces
            
    
        }
        return dice;
    }

}