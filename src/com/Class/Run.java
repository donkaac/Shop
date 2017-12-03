/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Class;

import java.awt.Toolkit;

/**
 *
 * @author Achi
 */
public class Run {
    public static void main(String[] args) {
        Toolkit t = Toolkit.getDefaultToolkit();
        System.out.println(t.getScreenSize().getHeight());
        System.out.println(t.getScreenSize().getWidth());
        System.out.println(t.getScreenResolution());
    }
}
