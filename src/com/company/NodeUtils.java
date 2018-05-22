package com.company;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class NodeUtils {
    private static int cellSize = 20;
    private static int nodeVerticalSpacing = 20;
    private static int nodeHorizontalSpacing = 40;

    public static void display(Node node) {
        BufferedImage image = prepareImage(node);


        JFrame jFrame = new JFrame(){
            @Override
            public void paint(Graphics g) {
                g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            }
        };

        jFrame.setSize(new Dimension(image.getWidth(), image.getHeight()));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

    }

    private static BufferedImage prepareImage(Node node) {
        /*BufferedImage image = new BufferedImage(500, 500, 1);
        final Object elements[] = node.getElements();
        int nodeTotalWith = cellSize * elements.length;

        ///////////////////////////
        int startPositionX = image.getWidth() / 2 - nodeTotalWith;
        int startPositionY = 100;
        Graphics graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        fillWithData(node, graphics, startPositionX, startPositionY, 0, null);

        return image;*/


        final List<GraphicNode> parseableObject = toParseable(node);

        BufferedImage image = new BufferedImage(500, 500, 1);

        ///////////////////////////
        Graphics graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        /*for (int i = 0; i < parseableObject.size(); i++)
            for (int j = 0; j < parseableObject.get(i).size(); j++){
                GraphicNode gNode = parseableObject.get(i).get(j);

                if (gNode != null && gNode.getType() == GraphicNode.Type.VALUE) {
                    graphics.setColor(Color.BLACK);
                    graphics.setFont(new Font("Arial Black", Font.BOLD, 12));

                    String textValue = Integer.toString((Integer) (gNode.getValue().getKey()));
                    graphics.drawString(textValue, gNode.getX(), gNode.getY());
                }
        }*/

        return image;
    }

    private static List<GraphicNode> toParseable(Node node) {
        List<GraphicNode> allElemnts = new ArrayList<>();

        for (int i = 0; i < node.getElements().length; i++) {
            if (node.getElements()[i] instanceof AbstractMap.SimpleEntry)
                allElemnts.add(new GraphicNode(node.getElements()[i], null, GraphicNode.Type.VALUE, 0));
            else {
                GraphicNode gNode = new GraphicNode(null, null, GraphicNode.Type.POINTER, 0);
                allElemnts.add(gNode);

                if (node.getElements()[i] != null)
                    fillSubNode(allElemnts, gNode, (Node)node.getElements()[i], 1);
            }
        }
        //remove duplicates
        for (int i = 0; i < allElemnts.size() - 1; i++){
            for (int j = i + 1; j < allElemnts.size(); j++){
                if (allElemnts.get(i).equals(allElemnts.get(j))) {
                    allElemnts.remove(j);
                    i = -1;
                    break;
                }
            }
        }

        int maxLvl = allElemnts.stream().map(GraphicNode::getLevel).max(Integer::compare).get();
        //by parent and level
        Map<Object, List<List<GraphicNode>>> possibleRez = new HashMap<>();
        for (int i = 0; i < maxLvl; i++){
            final int requiredLevel = i;
            List<GraphicNode> localTmp = allElemnts.stream().filter(e -> e.getLevel() == requiredLevel).collect(toList());

            List<GraphicNode> parentsForThisLevel = localTmp.stream().map(GraphicNode::getParent).collect(toList());

            List<List<GraphicNode>> grouped = parentsForThisLevel.stream()
                    .map(parent -> {
                                List<GraphicNode> children = localTmp.stream()
                                        .filter(e -> Objects.equals(parent, e.getParent()))
                                        .collect(toList());

                                return children;
                    }).collect(toList());

            possibleRez.put(i, grouped);
        }

        List<List<GraphicNode>> byParrent = allElemnts.stream()
                .map(nodeParent -> allElemnts.stream()
                        .filter(n -> Objects.equals(n.getParent(), nodeParent.getParent()))
                        .collect(toList()))
                .collect(toList());

        Map<Object, List<List<GraphicNode>>> groupedInLevelByParrent = new HashMap<>();
        for (int i = 0; i <= maxLvl; i++) {
            groupedInLevelByParrent.put(i, new ArrayList<>());
            final int searchedLevel = i;

            for (int j = 0; j < byParrent.size(); j++){
                boolean canAdd = byParrent.get(j).stream()
                        .filter(e -> searchedLevel == e.getLevel())
                        .findFirst()
                        .map(e -> e.getLevel())
                        .isPresent();

                if (canAdd)
                    groupedInLevelByParrent.get(i).add(byParrent.get(j));
            }
        }

        //rearange
        /*int y = nodeHorizontalSpacing + 100;
        for (int i = 0; i < parseable.size(); i++) {
            for (int j = 0; j < parseable.get(i).size(); j++) {
                GraphicNode gNode = parseable.get(i).get(j);
                gNode.setY(y);
                gNode.setX(j * nodeHorizontalSpacing + nodeHorizontalSpacing);
            }

            y += nodeHorizontalSpacing;
        }*/

        return null;
    }

    private static void fillSubNode(List<GraphicNode> parseable, GraphicNode parent, Node node, final int level){
        for (int i = 0; i < node.getElements().length; i++) {
            if (node.getElements()[i] instanceof AbstractMap.SimpleEntry)
                parseable.add(new GraphicNode(node.getElements()[i], parent, GraphicNode.Type.VALUE, level));
            else {
                GraphicNode gNode = new GraphicNode(null, parent, GraphicNode.Type.POINTER, level);
                parseable.add(gNode);

                if (node.getElements()[i] != null)
                    fillSubNode(parseable, gNode, (Node)node.getElements()[i], level+1);
            }
        }
    }


    /*private static void fillWithData(Node node, Graphics graphics, int parentX, int parentY, final int level, Integer parentPosition) {
        Object elements[] = node.getElements();

        int nodeTotalWith = cellSize * elements.length;
        int startPositionX =
        int startPositionY = 100;

        for (int i = 0; i < elements.length; i++){
            if (elements[i] != null && elements[i] instanceof AbstractMap.SimpleEntry) {
                graphics.setColor(Color.BLACK);
                graphics.setFont(new Font("Arial Black", Font.BOLD, 12));

                String textValue = Integer.toString((Integer) ((AbstractMap.SimpleEntry) elements[i]).getKey());
                graphics.drawString(textValue, (i * nodeHorizontalSpacing + startPositionX), nodeVerticalSpacing + startPositionY);
            }
            else if (elements[i] != null && elements[i] instanceof Node){
                fillWithData(elements[i], graphics, startPositionX, startPositionY, level +1);
            }
        }
    }*/

}


