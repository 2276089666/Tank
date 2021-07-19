package client.frame;


import client.model.GameModel;
import client.util.PropertiesUtil;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

/**
 * @Author ws
 * @Date 2021/7/14 19:29
 */
// MVC��View��
public class TankFrame extends Frame {

    public static final int GAME_WIDTH = Integer.parseInt(PropertiesUtil.get("GAME_WIDTH"));
    public static final int GAME_HEIGHT = Integer.parseInt(PropertiesUtil.get("GAME_HEIGHT"));
    private Image offScreenImage = null;

    private GameModel gameModel = null;

    private TankFrame() throws HeadlessException {
        this.setTitle("Tank Frame");
        this.setLocation(400, 100);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.addKeyListener(new TankKeyListener());
        this.gameModel = GameModel.getInstance();
    }

    public static TankFrame getInstance() {
        return TankFrameHolder.tankFrame;
    }

    @Override
    public void paint(Graphics g) {
        this.gameModel.paint(g);
    }

    // ���tank��˸����,����˫������ǰ���ڴ�����ǵ�ͼƬ����,�ٵ����Կ��Ļ���,��ʾһ����ͼƬ
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        // ��ǰ������ɫ��������
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        // ���������paint()����,����̹��
        paint(gOffScreen);
        // �����Կ��Ļ���,һ���Խ���ǰ�����˵�������tank��ʾ����Ļ��,��Ȼ���ͼƬ�����,һ���ֻ����ڴ�,
        // һ�����Ѿ����ص����Դ�,���Կ��Ļ��ʻ�������Ļ��
        // ���ճ�������tank��˸������
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public GameModel getGameModel() {
        return this.gameModel;
    }

    private void load() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File file = new File("save.txt");
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            this.gameModel = (GameModel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Ҫ��gameModel����������йض���ʵ��Serializable�ӿ�
    private void save() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File("save.txt");
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(gameModel);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ����
    private static class TankFrameHolder {
        private static final TankFrame tankFrame = new TankFrame();
    }

    // ����ȫ�ǿշ���,��������ģʽ,�������Ǹ���������д�Լ���Ҫ�ķ���
    private class TankKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_S) {
                // ����
                save();
                System.out.println("�浵���");
            } else if (key == KeyEvent.VK_L) {
                // ���ش浵
                load();
                System.out.println("�ɹ����ش浵");
            } else {
                gameModel.getPlayerTank().keyPressed(e);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            gameModel.getPlayerTank().keyReleased(e);
        }
    }

}
