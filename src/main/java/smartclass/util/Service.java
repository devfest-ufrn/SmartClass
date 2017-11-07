/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartclass.util;

import smartclass.entities.Attributes;
import smartclass.ui.ClassRoomUI;

/**
 *
 * @author Pedro
 */
public class Service {

    public Service(Attributes[] att, String sala) {
        ClassRoomUI c = ClassRoomUI.getInstance();
        for (Attributes a : att) {
            if(!c.selectedClass.equals(sala)){
                break;
            }
            switch (a.getName()) {
                case "presence":
                    if (Integer.parseInt(a.getValue()) == 1) {
                        c.airOn(23);
                        c.computerOn();
                        c.projectorOn();
                    }else{
                        c.lightOff();
                        c.airOff();
                        c.computerOff();
                        c.projectorOff();
                    }
                    break;
                case "brightness":
                    if (Integer.parseInt(a.getValue()) < 50) {
                        c.lightOn();
                    } else {
                        c.lightOff();
                    }
                    break;
                case "temperature":
                    c.airTemp(Integer.parseInt(a.getValue()));
                    break;
                case "time":
                    switch (Integer.parseInt(a.getValue())) {
                        case 0:
                            c.theProfessor("p1");
                            break;
                        case 1:
                            c.theProfessor("p2");
                            break;
                        case 2:
                            c.theProfessor("p3");
                            break;
                        case 3:
                            c.theProfessor("p4");
                            break;
                        default:
                            c.theProfessor("p1");
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
