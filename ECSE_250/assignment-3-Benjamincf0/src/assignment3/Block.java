package assignment3; // testing upstream

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth;
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random(); 
 
 
 /*
  * These two constructors are here for testing purposes. 
  */
 public Block() {}
 
 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }
 
 

 /*
  * Creates a random block given its level and a max depth. 
  * 
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth) {
  /*
   * ADD YOUR CODE HERE
   */
  this.level = lvl;
  this.maxDepth = maxDepth;
  if (this.level < this.maxDepth && gen.nextDouble() < Math.exp(-0.25*this.level)) {
    this.children = new Block[]{new Block(this.level + 1, this.maxDepth), new Block(this.level + 1, this.maxDepth), new Block(this.level + 1, this.maxDepth), new Block(this.level + 1, this.maxDepth)};
    return;
  }

  this.children = new Block[0];
  color = GameColors.BLOCK_COLORS[gen.nextInt(GameColors.BLOCK_COLORS.length)];

  
 }


 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the 
  * blocks. 
  * 
  *  The size is the height and width of the block. (xCoord, yCoord) are the 
  *  coordinates of the top left corner of the block. 
  */
 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
  /*
   * ADD YOUR CODE HERE
   */
  if (size < 1 || size%Math.pow(2, maxDepth - level) != 0) {
    throw new IllegalArgumentException("Invalid size");
  }
  this.xCoord = xCoord;
  this.yCoord = yCoord;
  this.size = size;

  if (this.children.length != 4 || this.level == this.maxDepth /* not required */ || size % 2 != 0) {
    return;
  }
  this.children[0].updateSizeAndPosition(size/2, xCoord + size/2, yCoord);
  this.children[1].updateSizeAndPosition(size/2, xCoord, yCoord);
  this.children[2].updateSizeAndPosition(size/2, xCoord, yCoord + size/2);
  this.children[3].updateSizeAndPosition(size/2, xCoord + size/2, yCoord + size/2);

}

 
 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  * 
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  * 
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *  
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {
  /*
   * ADD YOUR CODE HERE
   */

  ArrayList<BlockToDraw> arr = new ArrayList<BlockToDraw>();
  getPreOrder(this, arr); // recursion base case
  return arr;

 }

 private void getPreOrder(Block block, ArrayList<BlockToDraw> out) {
  if (block.children.length == 0) {    
    BlockToDraw block1 = new BlockToDraw(block.color, block.xCoord, block.yCoord, block.size, 0);
    BlockToDraw block2 = new BlockToDraw(GameColors.FRAME_COLOR, block.xCoord, block.yCoord, block.size, 3);
    out.add(block1);
    out.add(block2);
  }

  for (Block childBlock : block.children) {
    getPreOrder(childBlock, out); // recursive step
  }
 }


 /*
  * This method is provided and you should NOT modify it. 
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }
 
 
 
 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than 
  * the lowest block at the specified location, then return the block 
  * at the location with the closest level value.
  * 
  * The location is specified by its (x, y) coordinates. The lvl indicates 
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will 
  * contain the location (x, y) too. This is why we need lvl to identify 
  * which Block should be returned. 
  * 
  * Input validation: 
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
    if (lvl > maxDepth || lvl < this.level) {
        throw new IllegalArgumentException("invalid lvl search");
    }

    if (x < xCoord || x >= xCoord + size || y < yCoord || y >= yCoord + size) {
        return null;
    }

    if (lvl == this.level) {
        return this;
    }

    for (Block block : this.children) {
        if (block != null && block.xCoord <= x && x < block.xCoord + block.size && block.yCoord <= y && y < block.yCoord + block.size) {
            return block.getSelectedBlock(x, y, lvl);
        }
    }

    return null;
 }

 
 

 /*
  * Swaps the child Blocks of this Block. 
  * If input is 1, swap vertically. If 0, swap horizontally. 
  * If this Block has no children, do nothing. The swap 
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  * 
  */
 public void reflect(int direction) {
  if (direction != 0 && direction != 1) {
    throw new IllegalArgumentException("Direction must be either 0 or 1");
  }

  if (children.length == 0) {
    return;
  }

  int d = direction;
  Block temp1 = children[d];
  Block temp2 = children[1+d];
  children[d] = children[(3+d)%4];
  children[1+d] = children[2+d];
  children[2+d] = temp2;
  children[(3+d)%4] = temp1;

  updateSizeAndPosition(this.size, this.xCoord, this.yCoord);

  // Reflect recursively
  for (Block block : children) {
    block.reflect(direction);
  }
 }
 

 
 /*
  * Rotate this Block and all its descendants. 
  * If the input is 1, rotate clockwise. If 0, rotate 
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  /*
   * ADD YOUR CODE HERE
   */
  if (direction != 0 && direction != 1) {
    throw new IllegalArgumentException("Direction must be either 0 or 1");
  }

  if (children.length == 0) {
    return;
  }

  int d = 2*direction - 1;
  Block temp = children[0];

  for (int i = 0; i < children.length - 1; i++) {
    children[(i*d + 4) % 4] = children[(i*d + d + 4) % 4];
  }
  children[d+2] = temp;

  // clockwise: direction = 1
  // children[0] = children[1];
  // children[1] = children[2];
  // children[2] = children[3];
  // children[3] = temp;

  // counter-clockwise: direction = 0
  // children[0] = children[3];
  // children[3] = children[2];
  // children[2] = children[1];
  // children[1] = temp;

  updateSizeAndPosition(size, this.xCoord, this.yCoord);

  for (Block block : children) {
    block.rotate(direction);
  }
 }
 


 /*
  * Smash this Block.
  * 
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.  
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  * 
  * A Block can be smashed iff it is not the top-level Block 
  * and it is not already at the level of the maximum depth.
  * 
  * Return True if this Block was smashed and False otherwise.
  * 
  */
 public boolean smash() {
  /*
   * ADD YOUR CODE HERE
   */
  if (level == 0 || level == maxDepth) {
    return false;
  }

  children = new Block[]{new Block(level + 1, maxDepth), new Block(level + 1, maxDepth), new Block(level + 1, maxDepth), new Block(level + 1, maxDepth)};

  updateSizeAndPosition(size, xCoord, yCoord);

  return true;
 }
 
 
 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  * 
  * Return and array arr where, arr[i] represents the unit cells in row i, 
  * arr[i][j] is the color of unit cell in row i and column j.
  * 
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */
 public Color[][] flatten() {
    int dim = (int) Math.pow(2, maxDepth - level);
    Color[][] arr = new Color[dim][dim];
    int inc = size / dim;

    if (this.children.length == 0) {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                arr[i][j] = this.color;
            }
        }
    } else {
        for (Block block : children) {
            Color[][] childColors = block.flatten();
            int i = (block.yCoord - yCoord) / inc;
            int j = (block.xCoord - xCoord) / inc;
            for (Color[] colors : childColors) {
                System.arraycopy(colors, 0, arr[i], j, colors.length);
                i++;
            }
        }
    }
    return arr;
 }
 
 
 // These two get methods have been provided. Do NOT modify them. 
 public int getMaxDepth() {
  return this.maxDepth;
 }
 
 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block. 
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);   
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }
 
 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }
 
}
