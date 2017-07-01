package com.overstar.ildar.graph3d.util;

import com.overstar.ildar.graph3d.model.*;
import com.overstar.ildar.graph3d.view.*;
import java.awt.*;
/**
* Insert the type's description here.
* Creation date: (16.10.2003 11:39:04)
* @author: Shafigullin Ildar
*/
public class ScenarioWriter {
public static void scenarioPaint(Graph3DI graph) {
    int theHeight = graph.getSize().height;
    int theWidth = graph.getSize().width;
    graph.piace.offset = new Point(theWidth / 2, theHeight / 2);

    //создание куба
    graph.piace.figure.setNet(false);
    graph.piace.select.setFull(true);
    graph.piace.select.setColor(Color.cyan);
    graph.piace.figure.getAnim().setAnimAngle(-4.0f, -8.0f);
    graph.piace.figure.getAnim().offset = 0;
    graph.piace.figure.getAnim().isRevolving = true;
    graph.piace.savePoint3D(
        graph.piace.getPiace3D().getPoints3D().size(),
        theWidth / 8 * 3,
        theHeight / 2);
    graph.piace.turn = (float) (Math.PI / 2f); // graph.piace.turnSum = 0;
    for (int i = 0; i < 2; i++)
        graph.piace.reconstruct(graph.piace.select, "Z");
    graph.piace.copy(graph.piace.select);
    graph.piace.select.setColor(Color.blue);
    graph.piace.select.translate(graph.piace.matrix, 0, 0, theWidth / 4);
    graph.piace.newSide();
    graph.piace.select.setFull(true);
    graph.piace.select.setColor(Color.magenta);
    graph.piace.select.add(0);
    graph.piace.select.add(1);
    graph.piace.select.add(5);
    graph.piace.select.add(4);
    graph.piace.newSide();
    graph.piace.select.setFull(true);
    graph.piace.select.setColor(Color.red);
    graph.piace.select.add(2);
    graph.piace.select.add(3);
    graph.piace.select.add(7);
    graph.piace.select.add(6);
    graph.piace.newSide();
    graph.piace.select.setFull(true);
    graph.piace.select.setColor(Color.gray);
    graph.piace.select.add(0);
    graph.piace.select.add(3);
    graph.piace.select.add(7);
    graph.piace.select.add(4);
    graph.piace.figure.translate(graph.piace.matrix, -theWidth * 2 / 6, -theHeight * 2 / 6, 0);

    //создание шара
    graph.piace.newFigure();
    graph.piace.select.setFull(true);
    graph.piace.select.setColor(Color.blue);
    graph.piace.figure.setNet(false);
    graph.piace.figure.getAnim().setAnimAngle(-8.0f, -10.0f);
    graph.piace.figure.getAnim().isRevolving = true;
    graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), theWidth / 4, theHeight / 2);
    graph.piace.turn = (float) (Math.PI / 16f); // graph.piace.turnSum = 0;
    for (int i = 0; i < 5; i++)
        graph.piace.reconstruct(graph.piace.select, "Z");
    graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), theWidth / 4, theHeight / 2);
    graph.piace.turn = (float) (Math.PI / 16f);
    graph.piace.turnSum = 0;
    for (int i = 0; i < 4; i++)
        graph.piace.reconstruct(graph.piace.select, "Y");

    //создание тетраедра
    graph.piace.newFigure();
    graph.piace.select.setFull(true);
    graph.piace.select.setColor(Color.green);
    graph.piace.figure.setNet(false);
    graph.piace.figure.getAnim().setAnimAngle(9.0f, -11.0f);
    graph.piace.figure.getAnim().isRevolving = true;
    graph.piace.savePoint3D(
        graph.piace.getPiace3D().getPoints3D().size(),
        theWidth / 8 * 3,
        theHeight / 2);
    graph.piace.turnSum = 0;
    graph.piace.turn = (float) (2f * Math.PI / 3f);
    for (int i = 0; i < 2; i++)
        graph.piace.reconstruct(graph.piace.select, "Z");
    graph.piace.turnSum = 0;
    graph.piace.turn = (float) (Math.PI / 2f);
    for (int i = 0; i < 2; i++)
        graph.piace.reconstruct(graph.piace.select, "Y");
    graph.piace.select.translate(graph.piace.matrix, -theWidth * 2 / 6, -theHeight * 2 / 6, 0);

    //создание звезды
    graph.piace.newFigure();
    graph.piace.select.setFull(true);
    graph.piace.select.setColor(Color.red);
    graph.piace.figure.setNet(false);
    graph.piace.figure.getAnim().setAnimAngle(5, -5.0f);
    graph.piace.figure.getAnim().isRevolving = true;
    graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), -175, 196);
    graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), -135, 71);
    graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), -83, 205);
    graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), -194, 120);
    graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), -61, 136);
    graph.piace.copy(graph.piace.select);
    ((Side) graph.piace.select).setNormal_Left(true);
    graph.piace.select.setColor(Color.yellow);
    graph.piace.figure.setFlag_backface(true);
    //создание копии звезды
    graph.piace.copy(graph.piace.figure);
    graph.piace.figure.getAnim().setAnimAngle(0, 7.0f);
    graph.piace.figure.getAnim().isRevolving = true;

    //создание вазы
    graph.piace.newFigure();
    graph.piace.select.setFull(true);
    graph.piace.select.setColor(Color.magenta);
    graph.piace.figure.setNet(false);
    graph.piace.figure.getAnim().setAnimAngle(0, 3.0f);
    graph.piace.figure.getAnim().isRevolving = true;
    graph.piace.savePoint3D(
        graph.piace.getPiace3D().getPoints3D().size(),
        theWidth / 2,
        theHeight / 8 * 5);
    graph.piace.turnSum = 0;
    graph.piace.turn = (float) (Math.PI / 8f);
    for (int i = 0; i < 3; i++)
        graph.piace.reconstruct(graph.piace.select, "Z");
    graph.piace.savePoint3D(
        graph.piace.getPiace3D().getPoints3D().size(),
        theWidth * 9 / 20,
        theHeight / 8 * 2);
    graph.piace.savePoint3D(
        graph.piace.getPiace3D().getPoints3D().size(),
        theWidth * 8 / 20,
        theHeight / 8);
    graph.piace.turnSum = 0;
    graph.piace.turn = (float) (Math.PI / 8f);
    for (int i = 0; i < 4; i++)
        graph.piace.reconstruct(graph.piace.select, "Y");
    graph.piace.select.translate(graph.piace.matrix, 0, theHeight / 3, -theWidth / 8 * 5);
    /**
        //создание надписи "JAVA"
        graph.piace.newFigure();
        graph.piace.figure.setNet(false);
        //graph.piace.figure.setAnimAngle(0, 1.0f);
        //"J"
        graph.piace.newSide();
        graph.piace.select.setFull(true);
        graph.piace.select.setColor(Color.red);
        //graph.piace.select.kind = Element.Line;
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 230, 34);
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 256, 33);
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 255, 87);
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 244, 98);
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 234, 99);
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 225, 86);
        graph.piace.copy(graph.piace.select);
        graph.piace.select.setColor(Color.green);
        graph.piace.select.translate(graph.piace.matrix, 5, 5, 10);
        //"A"
        graph.piace.newSide();
        graph.piace.select.setFull(true);
        graph.piace.select.color = Color.red;
        graph.piace.select.kind = Element.Line;
        graph.piace.savePoint3D(
            graph.piace.getPiace3D().getPoints3D().size(),
            275,
            101);
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 295, 31);
        graph.piace.savePoint3D(
            graph.piace.getPiace3D().getPoints3D().size(),
            312,
            104);
        graph.piace.copy(graph.piace.select);
        graph.piace.select.color = Color.green;
        graph.piace.translate(graph.piace.select, 5, 5, 10);
        graph.piace.newSide();
        graph.piace.select.full = true;
        graph.piace.select.color = Color.red;
        graph.piace.select.kind = Element.Line;
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 282, 72);
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 305, 72);
        graph.piace.copy(graph.piace.select);
        graph.piace.select.color = Color.green;
        graph.piace.translate(graph.piace.select, 5, 5, 10);
        //"V"
        graph.piace.newSide();
        graph.piace.select.full = true;
        graph.piace.select.color = Color.red;
        graph.piace.select.kind = Element.Line;
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 326, 30);
        graph.piace.savePoint3D(
            graph.piace.getPiace3D().getPoints3D().size(),
            345,
            100);
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 367, 31);
        graph.piace.copy(graph.piace.select);
        graph.piace.select.color = Color.green;
        graph.piace.translate(graph.piace.select, 5, 5, 10);
        //"A"
        graph.piace.newSide();
        graph.piace.select.full = true;
        graph.piace.select.color = Color.red;
        graph.piace.select.kind = Element.Line;
        graph.piace.savePoint3D(
            graph.piace.getPiace3D().getPoints3D().size(),
            377,
            102);
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 400, 30);
        graph.piace.savePoint3D(
            graph.piace.getPiace3D().getPoints3D().size(),
            413,
            103);
        graph.piace.copy(graph.piace.select);
        graph.piace.select.color = Color.green;
        graph.piace.translate(graph.piace.select, 5, 5, 10);
        graph.piace.newSide();
        graph.piace.select.full = true;
        graph.piace.select.color = Color.red;
        graph.piace.select.kind = Element.Line;
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 386, 71);
        graph.piace.savePoint3D(graph.piace.getPiace3D().getPoints3D().size(), 409, 71);
        graph.piace.copy(graph.piace.select);
        graph.piace.select.color = Color.green;
        graph.piace.translate(graph.piace.select, 5, 5, 10);
        */
}
}
