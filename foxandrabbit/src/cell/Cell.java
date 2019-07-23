package cell;

import java.awt.Graphics;

/**
 * Cell: 抽象化为格子，不在具体表现为一个细胞
 * Cell:  规范是可以自己draw自己，即将自己显示出来
 * 所有实现Cell接口的类实例均可被放置在Field中存储
 */
public interface Cell {
    void draw(Graphics g, int x, int y, int size);
}
