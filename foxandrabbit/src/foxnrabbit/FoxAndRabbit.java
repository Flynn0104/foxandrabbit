package foxnrabbit;

import java.util.ArrayList;

import javax.swing.JFrame;

import animal.Fox;
import animal.Rabbit;
import cell.Cell;
import field.Field;
import field.Location;
import field.View;

/**
 * 逻辑实现类：实现数据之间交互逻辑，实现游戏规则和规范；
 *      1.让View显示原始数据
 *      2.在Step()内实现所有数据的更新（在游戏规则下）
 *      3.再次显示数据，循环，实现数据的多态显示
 */
public class FoxAndRabbit {
    private Field theField;
    private View theView;

    public FoxAndRabbit(int size) {
        theField = new Field(size, size);
        for (int row = 0; row < theField.getHeight(); row++) {
            for (int col = 0; col < theField.getWidth(); col++) {
                double probability = Math.random();
                if (probability < 0.05) {
                    theField.place(row, col, new Fox());
                } else if (probability < 0.15) {
                    theField.place(row, col, new Rabbit());
                }
            }
        }
        theView = new View(theField);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Cells");
        frame.add(theView);
        frame.pack();
        frame.setVisible(true);
    }

    public void start(int steps) {
        for (int i = 0; i < steps; i++) {
            step();
            theView.repaint();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void step() {
        for (int row = 0; row < theField.getHeight(); row++) {
            for (int col = 0; col < theField.getWidth(); col++) {
                Cell cell = theField.get(row, col);
                if (cell != null) {
                    animal.Animal animal = (animal.Animal) cell;
                    animal.grow();
                    if (animal.isAlive()) {
                        //	move
                        Location loc = animal.move(theField.getFreeNeighbour(row, col));
                        if (loc != null) {
                            theField.move(row, col, loc);
                        }
                        //	eat
						//  animal.eat(theField);
                        Cell[] neighbour = theField.getNeighbour(row, col);
                        ArrayList<animal.Animal> listRabbit = new ArrayList<animal.Animal>();
                        for (Cell an : neighbour) {
                            if (an instanceof Rabbit) {
                                listRabbit.add((Rabbit) an);
                            }
                        }
                        if (!listRabbit.isEmpty()) {
                            animal.Animal fed = animal.feed(listRabbit);
                            if (fed != null) {
                                theField.remove((Cell) fed);
                            }
                        }
                        //	breed
                        animal.Animal baby = animal.breed();
                        if (baby != null) {
                            theField.placeRandomAdj(row, col, (Cell) baby);
                        }
                    } else {
                        theField.remove(row, col);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        FoxAndRabbit fnr = new FoxAndRabbit(50);
        fnr.start(100);
    }

}
