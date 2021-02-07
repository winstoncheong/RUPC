from collections import defaultdict
from pprint import pprint
from typing import DefaultDict
import math
import sys


if len(sys.argv) == 2:
    file = sys.argv[1]
else: 
    file = 'SampleInput1.txt'
f = open(file)

squares = [i**2 for i in range(1, 51)]

def check_latin_square(grid, size):
    """Check rows, columns.
    Return whether solved, unsolved, or incorrect. If incorrect, put all errors
    """

    # populate `result` as Solved or Unsolved
    result = 'Solved'
    for i in range(size):
        for j in range(size):
            if grid[i][j] == 0:
                result = 'Unsolved'
                break
        else:
            continue
        break

    errors = ''

    # error check columns
    for c in range(size):
        # print(f'Checking column {c+1}')
        # Populate a dictionary val -> coords for this column
        d = DefaultDict(list)
        for r in range(size):
            d[grid[r][c]] += [(r, c)]
        
        for k, v in d.items():
            if len(v) > 1 and k != 0:
                result = 'Incorrect'
                errors += f'    {k} is repeated in column {c+1}\n'
                errors += f'      ' 
                # print coords of duplicates:
                for coord in v:
                    errors += f'({coord[0]+1},{coord[1]+1}) '
                errors += '\n'

    # error check rows
    for r in range(size):
        # print(f'Checking row {r+1}')
        # Populate a dictionary val -> coords for this row
        d = DefaultDict(list)
        for c in range(size):
            d[grid[r][c]] += [(r, c)]
        
        for k, coords in d.items(): # for each character
            # print(k, coords)
            if len(coords) > 1 and k != 0: # if duplicated
                result = 'Incorrect'
                errors += f'    {k} is repeated in row {r+1}\n'
                errors += f'      ' 
                # print coords of duplicates:
                for coord in coords:
                    # print(coord)
                    errors += f'({coord[0]+1},{coord[1]+1}) '
                errors += '\n'

    return result, errors

def check_sudoku(grid, size):
    """Check rows, columns, boxes.
    Return whether solved, unsolved, or incorrect. If incorrect, put all errors
    """

    # populate `result` as Solved or Unsolved
    result = 'Solved'
    for i in range(size):
        for j in range(size):
            if grid[i][j] == 0:
                result = 'Unsolved'
                break
        else:
            continue
        break

    errors = ''

    # error check blocks
    root_size = int(math.sqrt(size))
    # iterate through blocks
    for x in range(root_size):
        for y in range(root_size):
            # in a block with upper-left corner (x*rootsize, y*rootsize)

            # for the block, populate dictionary of values
            # dict: val -> [coords]
            d = defaultdict(list)

            for r in range(root_size):
                for c in range(root_size):
                    posX = x*root_size + r
                    posY = y*root_size + c
                    d[grid[posX][posY]] += [(posX, posY)]


            # check for duplicates in the block
            for k, coords in d.items():
                if len(coords) > 1 and k != 0:
                    result = 'Incorrect'
                    errors += f'    {k} is repeated in block {x*root_size + y + 1}\n'
                    errors += f'      ' 
                    # print coords of duplicates:
                    for coord in coords:
                        # print(coord)
                        errors += f'({coord[0]+1},{coord[1]+1}) '
                    errors += '\n'
    # end of error checking blocks

    # use check_latin_square to error check rows and columns
    result_sub, errors_sub = check_latin_square(grid, size)

    # Any errors from Latin Square will be lifted to Sudoku
    if result_sub == 'Incorrect':
        result = 'Incorrect'

    errors += errors_sub

    return result, errors

def check_sudoku_x(grid, size):
    """Check diagonals, rows, columns, boxes.
    Return whether solved, unsolved, or incorrect. If incorrect, put all errors
    """

    # populate `result` as Solved or Unsolved
    result = 'Solved'
    for i in range(size):
        for j in range(size):
            if grid[i][j] == 0:
                result = 'Unsolved'
                break
        else:
            continue
        break

    errors = ''

    # check diagonals

    # check diagonal 1 for duplicates
    d = DefaultDict(list)
    for i in range(size):
        d[grid[i][i]] += [(i, i)]

    for k, coords in sorted(d.items()):
        if len(coords) > 1 and k != 0:
            result = 'Incorrect'
            errors += f'    {k} is repeated in diagonal 1\n'
            errors += f'      ' 
            # print coords of duplicates:
            for coord in coords:
                # print(coord)
                errors += f'({coord[0]+1},{coord[1]+1}) '
            errors += '\n'

    # check diagonal 2 for duplicates
    d = DefaultDict(list)
    for i in range(size):
        d[grid[i][size - i - 1]] += [(i, size - i - 1)]

    for k, coords in sorted(d.items()):
        if len(coords) > 1 and k != 0:
            result = 'Incorrect'
            errors += f'    {k} is repeated in diagonal 2\n'
            errors += f'      ' 
            # print coords of duplicates:
            for coord in coords:
                # print(coord)
                errors += f'({coord[0]+1},{coord[1]+1}) '
            errors += '\n'

    # use check_sudoku to error check rows and columns, boxes
    result_sub, errors_sub = check_sudoku(grid, size)

    # Any errors from Sudoku will be lifted to Sudoku-X
    if result_sub == 'Incorrect':
        result = 'Incorrect'
    errors += errors_sub
    return result, errors

num_grids = int(f.readline())
print(f'Analyzing {num_grids} Grids')
for g in range(num_grids):
    print(f' Grid {g+1}:')
    grid = []
    grid_size = int(f.readline())
    print(f'  Size: {grid_size} x {grid_size}')
    for i in range(grid_size):
        row = list(map(int, f.readline().split()))
        grid.append(row)
    
    # pprint(grid)
    # analyse grid

    # If verdict is 'Solved' or 'Unsolved', don't have to check further rulesets
    verdict = 'Incorrect'
    if grid_size in squares: # possible to be Sudoku or Sudoku-X
        result, errors = check_sudoku_x(grid, grid_size)
        verdict = result
        print(f'  {result} Sudoku-X')
        print(errors)

        if verdict == 'Incorrect':
            result, errors = check_sudoku(grid, grid_size)
            verdict = result
            print(f'  {result} Sudoku')
            print(errors)

    # check Latin square conditions
    if verdict == 'Incorrect':
        result, errors = check_latin_square(grid, grid_size)
        print(f'  {result} Latin Square')
        print(errors)