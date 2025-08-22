import javax.swing.JFrame;

public class App {
  public static void main(String[] args) {
    int row_count = 21;
    int column_count = 19;
    int tile_size = 32;
    int board_width = column_count * tile_size;
    int board_height = row_count * tile_size;

    JFrame frame = new JFrame("PacMan: Java Edition");
    //frame.setVisible(true);
    frame.setSize(board_width, board_height);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    PacMan pacman_game = new PacMan();
    frame.add(pacman_game);
    frame.pack();
    pacmanGame.requestFocus();
    frame.setVisible(true);
  }
}
