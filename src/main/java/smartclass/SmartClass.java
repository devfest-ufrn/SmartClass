/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartclass;

import smartclass.ui.ClassRoomUI;
import smartclass.ui.ClassUI;

/**
 *
 * @author Pedro
 */
public class SmartClass {

    /*
 * Sala de aula inteligente, monitorando, no minimo, 3 elementos: projetor,
 * arcondicionado, computador. Colocar a exibicao dos slides de aula quando um
 * determinado professor entrar na sala. Deve considerar, no minimo, 4 professores e
 * as preferencias de cada professor quanto a temperatura do ar. Colocar os slides
 * especificos do professor que entrar na sala.
     */
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ClassRoomUI classRoomUI = ClassRoomUI.getInstance();
        classRoomUI.setVisible(true);
        ClassUI classUI = new ClassUI();
        classUI.setVisible(true);
    }

}
