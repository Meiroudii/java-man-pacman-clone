import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener{
    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tile_map = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

  class Block {
    int x;
    int y;
    int width;
    int height;
    Image image;

    int start_x;
    int start_y;
    char direction = 'U';
    int velocity_x = 0;
    int velocity_y = 0;

    Block(Image image, int x, int y, int width, int height) {
      this.image = image;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.start_x = x;
      this.start_y = y;
    }

    void updateDirection(char direction) {
      this.direction = direction;
      updateVelocity();
    }

    void updateVelocity() {
      /*
      if (this.direction == 'U') {
        this.velocity_x = 0;
        this.velocity_y = -tile_size/4;
      }
      else if (this.direction == 'D') {
          this.velocity_x = 0;
          this.velocity_y = tile_size/4;
      }
      else if (this.direction == 'L') {
          this.velocity_x = -tile_size/4;
          this.velocity_y = 0;
      }
      else if (this.direction == 'R') {
          this.velocity_x = tile_size/4;
          this.velocity_y = 0;
      }
      */
      switch (this.direction) {
        case 'U':
          this.velocity_x = 0;
          this.velocity_y = -tile_size/4;
          break;
        case 'D':
          this.velocity_x = 0;
          this.velocity_y = tile_size/4;
          break;
        case 'L':
          this.velocity_x = -tile_size/4;
          this.velocity_y = 0;
          break;
        case 'R':
          this.velocity_x = tile_size/4;
          this.velocity_y = 0;
          break;
        default:
          break;
      }
    }
  }

  // TODO: Create separate file for these variables
  private int row_count = 21;
  private int column_count = 19;
  private int tile_size = 32;
  private int board_width = column_count * tile_size;
  private int board_height = row_count * tile_size;

  private Image wall_image;
  private Image blue_ghost_image;
  private Image orange_ghost_image;
  private Image pink_ghost_image;
  private Image red_ghost_image;

  private Image pacman_up_image;
  private Image pacman_down_image;
  private Image pacman_left_image;
  private Image pacman_right_image;

  HashSet<Block> walls;
  HashSet<Block> foods;
  HashSet<Block> ghosts;
  Block pacman;

  Timer game_loop;

  //This is a constructor
  PacMan() {
    setPreferredSize(new Dimension(board_width, board_height));
    setBackground(Color.BLACK);
    addKeyListener(this);
    setFocusable(true);

    wall_image = new ImageIcon(getClass().getResource("./wall.png")).getImage();
    blue_ghost_image = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
    orange_ghost_image = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
    pink_ghost_image = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
    red_ghost_image = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
    pacman_up_image = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
    pacman_down_image = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
    pacman_left_image = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
    pacman_right_image = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

    load_map();
    game_loop = new Timer(50, this);
    game_loop.start();
  }

  public void load_map() {
    walls = new HashSet<Block>();
    foods = new HashSet<Block>();
    ghosts = new HashSet<Block>();

    for (int row = 0; row < row_count; row++) {
      for (int column = 0; column < column_count; column++) {
        String render_row = tile_map[row];
        char tile_map_char = render_row.charAt(column);

        int x = column*tile_size;
        int y = row*tile_size;

        if (tile_map_char == 'X') {
          Block wall = new Block(wall_image, x, y, tile_size, tile_size);
          walls.add(wall);
        }
        else if (tile_map_char == 'b') {
          Block ghost = new Block(blue_ghost_image, x, y, tile_size, tile_size);
          ghosts.add(ghost);
        }
        else if (tile_map_char == 'o') {
          Block ghost = new Block(orange_ghost_image, x, y, tile_size, tile_size);
          ghosts.add(ghost);
        }
        else if (tile_map_char == 'p') {
          Block ghost = new Block(pink_ghost_image, x, y, tile_size, tile_size);
          ghosts.add(ghost);
        }
        else if (tile_map_char == 'r') {
          Block ghost = new Block(red_ghost_image, x, y, tile_size, tile_size);
          ghosts.add(ghost);
        }
        else if (tile_map_char == 'P') {
          pacman = new Block(pacman_right_image, x, y, tile_size, tile_size);
        }
        else if (tile_map_char == ' ') {
          Block food = new Block(null, x + 14, y + 14, 4, 4);
          foods.add(food);
        }
      }
    }
  }

  // When naming a public voids you really need to write in camelcase
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

    for (Block ghost: ghosts) {
      g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
    }

    for (Block wall : walls) {
      g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
    }

    g.setColor(Color.WHITE);
    for (Block food : foods) {
      g.fillRect(food.x, food.y, food.width, food.height);
    }
  }

  public void move() {
    pacman.x += pacman.velocity_x;
    pacman.y += pacman.velocity_y;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    move();
    System.out.println("move has been moved");
    repaint();
    System.out.println("has been repaint");
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {}
  // good for holding a key, add booster?

  @Override
  public void keyReleased(KeyEvent e) {
    //System.out.println("KeyEvent: "+ e.getKeyCode());
    switch (e.getKeyCode()) {
      case KeyEvent.VK_UP:
        pacman.updateDirection('U');
        break;
      case KeyEvent.VK_DOWN:
        pacman.updateDirection('D');
        break;
      case KeyEvent.VK_LEFT:
        pacman.updateDirection('L');
        break;
      case KeyEvent.VK_RIGHT:
        pacman.updateDirection('R');
        break;
      default:
        break;
    }
    /*
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      System.out.println(KeyEvent.VK_UP);
      pacman.updateDirection('U');
    } 
    else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      System.out.println(KeyEvent.VK_DOWN);
      pacman.updateDirection('D');
    }
    else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      pacman.updateDirection('L');
    }
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      pacman.updateDirection('R');
    }
    */
  }
}
