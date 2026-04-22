import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

/**
 * ConquestGame is the main application window (JFrame).
 * It manages the high-level user interface, including the header, status updates,
 * anf the reset button
 * 
 * 
*/

public class ConquestGame extends JFrame {

    private QueensCanvas canvas;

    private JLabel statusLabel;

    private JButton resetBtn;


    /*
    Constructor sets up the window layout, colors, and components.
     */

    public ConquestGame() {

        setTitle("The Anchor | Conquest");

        setSize(700, 850);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(245, 245, 247)); 

        setLayout(new BorderLayout(20, 20));


        // Create the header panel wit stats and reset controls
        
        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(new Color(245, 245, 247));

        header.setBorder(new EmptyBorder(25, 35, 10, 35));

        statusLabel = new JLabel("LEVEL 1", SwingConstants.LEFT);

        statusLabel.setFont(new Font("Inter", Font.BOLD, 26));

        statusLabel.setForeground(new Color(28, 28, 30));

        resetBtn = new JButton("New Puzzle");
        resetBtn.setFocusPainted(false);
        resetBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetBtn.setBackground(Color.WHITE);
        resetBtn.setFont(new Font("Inter", Font.PLAIN, 14));

        header.add(statusLabel, BorderLayout.WEST);

        header.add(resetBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);



        // Set up the game canvas and its wrapper for padding

        canvas = new QueensCanvas(this);

        JPanel canvasWrapper = new JPanel(new BorderLayout());

        canvasWrapper.setBackground(new Color(245, 245, 247));

        canvasWrapper.setBorder(new EmptyBorder(10, 30, 40, 30));

        canvasWrapper.add(canvas);

        add(canvasWrapper, BorderLayout.CENTER);


        // ActionListener to trigger a board reset

        resetBtn.addActionListener(e -> canvas.initBoard());


        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
        Update the status label and handles the win dialog.

        @param level Current game level
        @param size Current grid dimensions
        @param msg Current state message (Active, Conflict, or Win)
    
    */
    public void updateStatus(int level, int size, String msg) {

        statusLabel.setText("LEVEL " + level + " (" + size + "x" + size + ")");

        if (msg.equals("WIN")) {

            JOptionPane.showMessageDialog(this, "Success! The grid is expanding.");

            canvas.nextLevel();
        }
    }
}



/**
 * 
 * QueensCanvas is the primary game engine. It extends Jpanel to handle
 * custom 2D drawing and manages the internal logic of the puzzle.
 * 
 */

class QueensCanvas extends JPanel {

    private int size = 4, level = 1;

    private int[][] board;

    private int[][] regions;

    private ConquestGame parent;


    /**
     * 
     * Constructor sets up the mouse listener for player interaction.
     */

    public QueensCanvas(ConquestGame parent) {

        this.parent = parent;

        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        initBoard();


        /**
         * MouseListener to handle grid clicks
         */
        
        addMouseListener(new MouseAdapter() {

            @Override

            public void mousePressed(MouseEvent e) {
                
                int cw = getWidth() / size, ch = getHeight() / size;

                int col = e.getX() / cw, row = e.getY() / ch;

                if (col < size && row < size) {

                    board[row][col] = (board[row][col] + 1) % 3;

                    checkRules();
                    repaint();
                }
            }
        });
    }



    /**
     * Initializes a new board and generates a puzzle
     */



    public void initBoard() {

        board = new int[size][size];

        regions = new int[size][size];

        generateSolvableBoard();

        parent.updateStatus(level, size, "Active");

        repaint();
    }


    /**
     * This is the algorithmic generation that ensures puzzle solvability
     * by picking queens spor first and coloring the regions around them.
     * 
     */

    private void generateSolvableBoard() {

        for (int[] row : regions) Arrays.fill(row, -1);

        ArrayList<Point> solution = new ArrayList<>();
        Random rand = new Random();


        // This is to make sure that the crwon positions are valid

        while (solution.size() < size) {

            solution.clear();

            ArrayList<Integer> cols = new ArrayList<>();

            for(int i=0; i<size; i++) cols.add(i);

            Collections.shuffle(cols);

            for (int r = 0; r < size; r++) {

                int c = cols.get(r);

                boolean conflict = false;

                for (Point p : solution) {

                    if (Math.abs(p.x - c) <= 1 && Math.abs(p.y - r) <= 1) conflict = true;
                }

                if (!conflict) solution.add(new Point(c, r));
            }
        }


        /**
         * 
         * Now that we now the valid Queen postions, we start assigning them a unique Region ID.
         * Thse act as the starting points from which the colored zones will eventually fill the entire
         * board
         */
        for (int i = 0; i < size; i++) {

            Point p = solution.get(i);

            regions[p.y][p.x] = i; // this assign an unique ID (0 to N-1) to the solution coordinates
        }


        /**
         *
         * This while loop continues until every single cell on the grid has been assigned a start position.
         * */
        

        boolean full = false; // assume the board is full until we find an unassigned cell

        while (!full) {

            full = true;

            for (int r = 0; r < size; r++) {

                for (int c = 0; c < size; c++) {

                    if (regions[r][c] == -1) { // Found  and empty cell

                        full = false;




                        // Check for immediate neighbors (North, South, East, West)

                        int[][] neighbors = {{r-1,c}, {r+1,c}, {r,c-1}, {r,c+1}};

                        for (int[] n : neighbors) {
                            /**
                             * We add a random 80% chance for the square to join the neigbor's color.
                             * This 20% delay is what creates the puzzle shapes
                             */

                            if (n[0]>=0 && n[0]<size && n[1]>=0 && n[1]<size && regions[n[0]][n[1]] != -1)
                                 {
                                if (rand.nextInt(10) < 8) { 
                                    regions[r][c] = regions[n[0]][n[1]]; {
                                        break; //Square has hoin to its neighbor, move to the next square
                                     }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * Go to the next level and increase complexity
     */

    public void nextLevel() { level++; if (size < 9) size++; initBoard(); }


    /*
    Check the current board states against the game rules.
    */
    private void checkRules() {

        int queens = 0;

        HashSet<Integer> usedReg = new HashSet<>();

        for (int r = 0; r < size; r++) {

            for (int c = 0; c < size; c++) {

                if (board[r][c] == 1) {

                    queens+=1;

                    if (!isSafe(r, c) || usedReg.contains(regions[r][c])) {

                        parent.updateStatus(level, size, "Conflict"); return;
                    }
                    usedReg.add(regions[r][c]);
                }
            }
        }
        if (queens == size){

         parent.updateStatus(level, size, "WIN");
        }
        else parent.updateStatus(level, size, "Active");
    }


    /**
     * Validates if a crown placement respects horizontal, vertical, and diagonal adjency constraints
     */
    private boolean isSafe(int r, int c) {

        for (int i = 0; i < size; i++) {

            if (i != c && board[r][i] == 1) {
                return false;
            }
            if (i != r && board[i][c] == 1) {
                return false;
            } 
        }
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1}, dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {

            int nr = r + dr[i], nc = c + dc[i];

            if (nr >= 0 && nr < size && nc >= 0 && nc < size && board[nr][nc] == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * The primary paint method that renders the board, shapes, and icons.
     */

    @Override

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cw = getWidth() / size, ch = getHeight() / size;


        // We add pastel color for the regions

        Color[] palette = {

            new Color(211, 235, 226), new Color(255, 236, 179), 
            new Color(209, 196, 233), new Color(255, 204, 188),
            new Color(200, 230, 201), new Color(178, 235, 242),
            new Color(248, 187, 208), new Color(225, 190, 231),
            new Color(255, 249, 196)
        };

        for (int r = 0; r < size; r++) {

            for (int c = 0; c < size; c++) {

                g2.setColor(palette[regions[r][c] % palette.length]);
                g2.fillRect(c * cw, r * ch, cw, ch);
                
                g2.setColor(new Color(0,0,0,25));
                g2.drawRect(c * cw, r * ch, cw, ch);
                
                g2.setColor(new Color(45, 45, 45));
                g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (c < size-1 && regions[r][c] != regions[r][c+1]) {

                     g2.drawLine((c+1)*cw, r*ch, (c+1)*cw, (r+1)*ch);
                }

                if (r < size-1 && regions[r][c] != regions[r+1][c]) {

                     g2.drawLine(c*cw, (r+1)*ch, (c+1)*cw, (r+1)*ch);
                }


                //* we render the crown Icon */

                if (board[r][c] == 1) { 

                    g2.setColor(new Color(28, 28, 30));

                    int cx = c * cw + cw / 2, cy = r * ch + ch / 2;
                    int[] x = {cx-20, cx+20, cx+20, cx+12, cx, cx-12, cx-20};
                    int[] y = {cy+15, cy+15, cy-5, cy+5, cy-15, cy+5, cy-5};

                    g2.fill(new Polygon(x, y, 7));
                    g2.fillOval(cx-3, cy-22, 6, 6);

                } 


                /**
                We render the X-Marker to let the user know where they have place crown before.
                */


                
                else if (board[r][c] == 2) { 

                    g2.setColor(new Color(0,0,0,70));
                    g2.setStroke(new BasicStroke(3));

                    int p = cw/3;

                    g2.drawLine(c*cw+p, r*ch+p, (c+1)*cw-p, (r+1)*ch-p);
                    g2.drawLine((c+1)*cw-p, r*ch+p, c*cw+p, (r+1)*ch-p);
                }
            }
        }
    }
}