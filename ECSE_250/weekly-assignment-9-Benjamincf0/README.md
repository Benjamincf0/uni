[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/yEwr1sOO)
# Weekly Assignment 9

## Assignment Description
Suppose we have a robot that is trying to get from point A to point B on a $m \times n$ grid. The grid contains 0s and 1s, where 0 represents a valid cell (of which the robot can pass through) while 1 represents an obstacle (invalid cell for robot to be on). Our goal is to __find the number of possible paths from point A to point B__.
For sake of simplicity, you may assume:
- Point A is always at the top left corner, point B is always at bottom right corner.
- The robot can only move down or right.
- $m$ and $n$ will be no smaller than 1

For instance, suppose we are given a 3x3 grid with an obstacle in the middle of the grid. Starting from the top left corner, there are 2 possible paths to reach the bottom right cell.  

Input (`int[][]`): `grid = [[0,0,0],[0,1,0],[0,0,0]]`

Output (`int`): 2

*Hint: you may add another method to simplify the recursive operations*

## Deliverables
- Complete `getNumPaths` method in `Solution.java`. 
- `Main.java` contains a couple of test cases to help check if the implementations are correct. You are not expected to change `Main.java` file.
- Please do only commit all Java files (those that end with a `.java` extension).

## Instructions
1. Similar to the previous assignments, we will once again start by cloning this repository onto your local machine by using the `git clone <url>` command (detailed instructions [here](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository)). If done properly, observe that a folder with the following naming pattern `weekly-assignment-9-*` (with * being your GitHub username) will be created.
2. Import project into your IDE of choice. For example, this can be performed by opening the cloned folder (with the naming pattern `weekly-assignment-9-*`) within Intellij.
3. Complete all methods marked with `TODO` (see above). 
4. Once you are satisfied with your code, perform the following git operations:
```bash
git add *
git commit -m 'adding completed code'
git push
``` 
*Please check your private repository for the assignment on GitHub to see if your code was submitted correctly -- what you see there is what will be graded.*