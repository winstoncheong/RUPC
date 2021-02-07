The programming contest files are hosted at http://elvis.rowan.edu/rupc/

The directories marked `-old` are from the dark ages when Java was my main language (high school to early undergrad)

Python was only added as an allowed language in 2012. Before then, the allowed langages were Java, C++ and Basic. I will ignore this restriction.


Note: I have no real way of knowing whether my code is correct.. the test inputs seem to be tricker, with corner cases that the sample inputs don't illustrate. 

----
# 2011: Transposition and substitution ciphers
* Transposition involves a 25 square grid, filling in by rows, scrambling the columns, and reading by columns
  * keyword of 5 unique letters. 
* Substitution (Caesar)
  * Can also use a keyword, cycling it over the message

## Input spec
* #datasets
* for each dataset
  * transposition keyword or `PASS`
  * substitution keyword or `PASS`
  * #lines to encode
    * those lines
  * #lines to decode
    * those lines

# 2010: Catapults, bombs, and damage assessment
* Damage zones will never include a corner.
* Damage zones will not overlap.
* Cities fire until a catapult is destroyed or both cities run out of ammunition.

## Input spec
* #datasets
* for each dataset:
  * coords of city1
  * coords of catapult1
  * coords of city2
  * coords of catapult2
  * #targets : t
  * list of barrels for city1
  * list of barrels for city2
  * for each t:
    * coords of city1's target, coords of city2's target

## Output
* Some input validation:
  * #datasets, which dataset
* Result of simulation (whether catapult was destroyed, and after how many shots)
* Calculation of damage total for both cities


# 2009: Windows and Z-order
* Simplifying assumption: any window will either completely cover another window, or leave exposed a rectangular portion of any window it covers (no covering only a corner).
* Higher z-order means it is on top. Z-order will be positive integer.
* Visible area of any windows will be contiguous

## Input spec
* integer: number of datasets
* for each dataset:
  * width, heigth
  * #windows
  * 5 integers: 
    * (x, y) coord of upper-left corner
    * width, height of window
    * z order

## Output
* some input validation / printout:
  * #data sets, which dataset, screensize, #windows
* For each window (printed in order read in):
  * Visible portion (upper-left coord to lower-right coord) OR "completely obscured"

# 2008: Latin sqaures, Sudoku, and Sudoku-X
* Given a square grid of numbers, classify it as exactly as possible.
* A Latin square can be any size
* A Sudoku / SudokuX must have the number rows/colums to be a perfect square (4, 9, 16, 25, ...)
* A solved / unsolved Sudoku-X does not need to be analyzed further
* An incorrect Sudoku-X that is a solved or unsolved Sudoku does not need to be analyzed as a Latin square
* Do not need to check if a Sudoku is *solvable* to classify as unsolved. Just need to check that there are no duplicates in diagonals, rows, columns, and blocks (hence not incorrect)

## Input spec
* number of grids
* for each grid (data set)
  * number of rows and columns : `S`
  * `S` lines, each with `S` integers (1--S) for values, 0 for unfilled

## Output spec
* validation of #grids, which grid, grid size
* Puzzle classification 
  * Solved / Unsolved / Incorrect Sudoku-X
  * Solved / Unsolved / Incorrect Sudoku
  * Solved / Unsolved / Incorrect Latin square
* Error checking
  * If improper duplicates are found, print the duplicated symbol and all grid coordinates it appears in.
  * All occurrences should be listed on one line, in increasing order by row and then by column.

# 2007: 
* Read in a list of characters, their widths, then a list of groupings which have a different width than the letters would have by themselves. 
* Then read a number of lines of text.
* Output the length of each line of text, if typeset using the characters and kerning/ligature adjustments as defined
* All measurements are in 'points'. Precision only needed up to 0.1 points.

* spacing between letters is 0.0 points
* Between two words, space of 4.0 points.
* Groupings are calculated greedily. If there is more than one applicable grouping, use the grouping that is longer (in number of characters).
  * with groupings "ffi", "ff" and "fi", apply "ffi"
  * with groupings A-LT-TAT vs A-LTA-AT, the latter is used.

## Input spec
* #datasets
* For each dataset
  * #symbols : S
    * S lines, each with a character and the character's width in points
  * #special character groupings : G
    * G lines, each with the characters in the grouping and the number of points that group's width is to be adjusted.
  * #lines text : L
    * L lines of text. Each line with less than 80 characters. 

## Output spec
* Debug output of #data sets, #characters, #groupings, #lines
* For each line, the width of the line