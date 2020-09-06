/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomsongmaker;

import java.util.ArrayList;

/**
 *
 * @author Mart Henrick Gamutan
 */
public class Sheet {
    public ArrayList <String> Patterns;
    public String name;
    public Sheet(ArrayList <String> Patterns, String name){
        this.Patterns = Patterns;
        this.name = name;
    }
}
