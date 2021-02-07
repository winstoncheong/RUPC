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