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
        "OOOX X       X XOXO",
        "XX X X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X        X   X    X",
        "X XXXXXX X XXXXXX X",
        "X    X            X",
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
      char prev_direction = this.direction;
      this.direction = direction;
      updateVelocity();
      this.x += this.velocity_x;
      this.y += this.velocity_y;

      for (Block wall : walls) {
        if (collision(this, wall)) {
          this.x -= this.velocity_x;
          this.y -= this.velocity_y;
          this.direction = prev_direction;
          updateVelocity();
        }
      }
    }
    void updateVelocity() {
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

    void reset() {
      this.x = this.start_x;
      this.y = this.start_y;
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
  char[] direction = {'U', 'D', 'L', 'R'};
  Random random = new Random();
  int score = 0;
  int lives = 3;
  boolean game_over = false;

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
    for (Block ghost : ghosts) {
      char new_direction = direction[random.nextInt(4)];
      ghost.updateDirection(new_direction);
    }
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

    // Score
    g.setFont(new Font("Arial", Font.PLAIN, 22));
    if (game_over) {
      g.drawString("Game Over: " + String.valueOf(score), tile_size / 2, tile_size / 2);
    } else {
      g.drawString("LP: " + String.valueOf(lives) + " Score: " + String.valueOf(score), tile_size /2, tile_size/2);
    }

  }

  public void move() {
    pacman.x += pacman.velocity_x;
    pacman.y += pacman.velocity_y;

    //check wal collisions
    for (Block wall : walls) {
      if (collision(pacman, wall) || pacman.x <= 0 || pacman.x + pacman.width >= board_width) {
        pacman.x -= pacman.velocity_x;
        pacman.y -= pacman.velocity_y;
        break;
      }
    }
    // check ghost collisions
    for (Block ghost : ghosts) {
      if (collision(ghost, pacman)) {
        lives -= 1;
        if (lives == 0) {
          game_over = true;
          return;
        }
        resetPositions();
      }
      if (ghost.y == tile_size*9 && ghost.direction != 'U' && ghost.direction != 'D') {
        ghost.updateDirection('U');
      }
      ghost.x += ghost.velocity_x;
      ghost.y += ghost.velocity_y;
      for (Block wall : walls ) {
        if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= board_width) {
          ghost.x -= ghost.velocity_x;
          ghost.y -= ghost.velocity_y;
          char new_direction = direction[random.nextInt(4)];
          ghost.updateDirection(new_direction);
        }
      }
    }

    // Check food collision
    Block food_eaten = null;
    for (Block food : foods) {
      if (collision(pacman, food)) {
        food_eaten = food;
        score += 10;
      }
    }
    foods.remove(food_eaten);

    if (foods.isEmpty()) {
      load_map();
      resetPositions();
    }

  }

  public boolean collision(Block a, Block b) {
    return a.x < b.x + b.width &&
           a.x + a.width > b.x &&
           a.y < b.y + b.height &&
           a.y + a.height > b.y;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    move();
    repaint();
    if (game_over) {
      game_loop.stop();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
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

    switch(pacman.direction) {
      case 'U':
        pacman.image = pacman_up_image;
        break;
      case 'D':
        pacman.image = pacman_down_image;
        break;
      case 'L':
        pacman.image = pacman_left_image;
        break;
      case 'R':
        pacman.image = pacman_right_image;
        break;
      default:
        break;
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
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

    switch(pacman.direction) {
      case 'U':
        pacman.image = pacman_up_image;
        break;
      case 'D':
        pacman.image = pacman_down_image;
        break;
      case 'L':
        pacman.image = pacman_left_image;
        break;
      case 'R':
        pacman.image = pacman_right_image;
        break;
      default:
        break;
    }
  }
  // good for holding a key, add booster?
  //

  public void resetPositions() {
    pacman.reset();
    pacman.velocity_x = 0;
    pacman.velocity_y = 0;
    for (Block ghost: ghosts) {
      ghost.reset();
      char new_direction = direction[random.nextInt(4)];
      ghost.updateDirection(new_direction);
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    //System.out.println("KeyEvent: "+ e.getKeyCode());
    if (game_over) {
      load_map();
      resetPositions();
      lives = 3;
      sore = 0;
      game_over = false;
      game_loop.start();
    }
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

    switch(pacman.direction) {
      case 'U':
        pacman.image = pacman_up_image;
        break;
      case 'D':
        pacman.image = pacman_down_image;
        break;
      case 'L':
        pacman.image = pacman_left_image;
        break;
      case 'R':
        pacman.image = pacman_right_image;
        break;
      default:
        break;
    }
  }
}
