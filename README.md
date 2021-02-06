The programming contest files are hosted at http://elvis.rowan.edu/rupc/

The directories marked `-old` are from the dark ages when Java was my main language (high school to early undergrad)

Python was only added as an allowed language in 2012. Before then, the allowed langages were Java, 
C++ and Basic. I will ignore this.


Note: I have no real way of knowing whether my code is correct.. the test inputs seem to be tricker, with corner cases that the sample inputs don't illustrate. 

----

# 2009
Windows and Z-order
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